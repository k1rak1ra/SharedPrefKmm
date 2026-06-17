package net.k1ra.sharedprefkmm.cryptography

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import net.k1ra.sharedprefkmm.SharedPrefKmmInitContentProvider
import net.k1ra.sharedprefkmm.util.Constants
import net.k1ra.sharedprefkmm.util.TestConfig
import java.nio.ByteBuffer
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

internal actual object KeyManager {
    private const val wrappingAlias = "SharedPrefDbKeyWrapping"
    private const val prefsName = "shared_pref_kmm_secure"
    private const val wrappedKeyField = "wrapped_key"
    private var key: ByteArray? = null
    private val keyLock = Any()

    @OptIn(InternalCoroutinesApi::class)
    actual fun getKey(collection: String): ByteArray? {
        val cached = key
        if (cached != null) return cached

        return synchronized(keyLock) {
            val cachedInside = key
            if (cachedInside != null) return@synchronized cachedInside

            if (TestConfig.testMode) {
                val raw = generateNewKey()
                key = raw
                return@synchronized raw
            }

            val context = SharedPrefKmmInitContentProvider.appContext
            val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

            ensureWrappingKey()

            val wrappedB64 = prefs.getString(wrappedKeyField, null)
            val raw = if (wrappedB64 == null) {
                val newKey = generateNewKey()
                val wrapped = encryptAesKey(newKey)
                prefs.edit()
                    .putString(wrappedKeyField, android.util.Base64.encodeToString(wrapped, android.util.Base64.NO_WRAP))
                    .commit()
                newKey
            } else {
                val wrapped = android.util.Base64.decode(wrappedB64, android.util.Base64.DEFAULT)
                decryptAesKey(wrapped)
            }

            key = raw
            raw
        }
    }

    private fun generateNewKey(): ByteArray {
        val out = ByteArray(Constants.AES_256_KEY_LENGTH)
        SecureRandom().nextBytes(out)
        return out
    }

    private fun ensureWrappingKey() {
        val ks = KeyStore.getInstance("AndroidKeyStore")
        ks.load(null)
        if (ks.containsAlias(wrappingAlias)) return

        val kg = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

        val purposes = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT

        val spec = KeyGenParameterSpec.Builder(wrappingAlias, purposes)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(true)
            .build()

        kg.init(spec)
        kg.generateKey()
    }

    private fun encryptAesKey(raw: ByteArray): ByteArray {
        val ks = KeyStore.getInstance("AndroidKeyStore")
        ks.load(null)
        val wrappingKey = ks.getKey(wrappingAlias, null) as SecretKey

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, wrappingKey)
        val iv = cipher.iv
        val ciphertext = cipher.doFinal(raw)

        return ByteBuffer.allocate(4 + iv.size + ciphertext.size)
            .putInt(iv.size)
            .put(iv)
            .put(ciphertext)
            .array()
    }

    private fun decryptAesKey(payload: ByteArray): ByteArray {
        val buffer = ByteBuffer.wrap(payload)
        val ivLength = buffer.int

        val iv = ByteArray(ivLength)
        buffer.get(iv)

        val ciphertext = ByteArray(buffer.remaining())
        buffer.get(ciphertext)

        val ks = KeyStore.getInstance("AndroidKeyStore")
        ks.load(null)
        val wrappingKey = ks.getKey(wrappingAlias, null) as SecretKey

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, wrappingKey, GCMParameterSpec(128, iv))
        return cipher.doFinal(ciphertext)
    }
}
