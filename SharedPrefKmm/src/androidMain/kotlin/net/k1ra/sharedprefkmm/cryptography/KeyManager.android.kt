package net.k1ra.sharedprefkmm.cryptography

import androidx.security.crypto.EncryptedSharedPreferences
import korlibs.crypto.SecureRandom
import korlibs.encoding.fromBase64
import korlibs.encoding.toBase64
import net.k1ra.sharedprefkmm.SharedPrefKmmInitContentProvider
import net.k1ra.sharedprefkmm.util.Constants
import net.k1ra.sharedprefkmm.util.TestConfig


internal actual object KeyManager {
    private const val keyAlias = "SharedPrefDbKey"
    private var key: ByteArray? = null

    actual fun getKey(collection: String) : ByteArray {
        if (key != null)
            return key!!

        //If we're in test mode and can't use EncryptedSharedPref, generate and use a temp key just for this run
        if (TestConfig.testMode) {
            key = generateNewKey()
            return key!!
        }

        val sharedPreferences = EncryptedSharedPreferences.create(
            keyAlias,
            keyAlias,
            SharedPrefKmmInitContentProvider.appContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        if (sharedPreferences.getString(keyAlias, null) == null) {
            //Generate and store new key
            val editor = sharedPreferences.edit()
            editor.putString(keyAlias, generateNewKey().toBase64())
            editor.apply()
        }

        key = sharedPreferences.getString(keyAlias, null)!!.fromBase64()
        return key!!
    }

    private fun generateNewKey() : ByteArray {
        val byteArray = ByteArray(Constants.AES_256_KEY_LENGTH)
        SecureRandom.nextBytes(byteArray)
        return byteArray
    }
}