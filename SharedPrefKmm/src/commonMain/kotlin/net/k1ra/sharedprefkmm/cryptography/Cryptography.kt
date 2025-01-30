package net.k1ra.sharedprefkmm.cryptography

import korlibs.crypto.AES
import korlibs.crypto.CipherPadding
import korlibs.crypto.SecureRandom
import net.k1ra.sharedprefkmm.util.Constants

internal class Cryptography(private val collection: String) {
    fun runAes(input: ByteArray, iv: ByteArray, cipherMode: CipherMode) : ByteArray {
        //Key null to disable cryptography
        KeyManager.getKey(collection)?.let {
            return when(cipherMode) {
                CipherMode.ENCRYPT -> AES.encryptAesCbc(input, it, iv, CipherPadding.PKCS7Padding)
                CipherMode.DECRYPT -> AES.decryptAesCbc(input, it, iv, CipherPadding.PKCS7Padding)
            }
        }

        return input
    }

    fun generateIv() : ByteArray {
        val ivBytes = ByteArray(Constants.AES_256_IV_LENGTH)
        SecureRandom.nextBytes(ivBytes)

        return ivBytes
    }
}