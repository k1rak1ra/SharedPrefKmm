package net.k1ra.sharedprefkmm.cryptography

import korlibs.crypto.SecureRandom
import net.k1ra.sharedprefkmm.StorageManager
import net.k1ra.sharedprefkmm.util.Constants
import java.io.File

internal actual object KeyManager {
    private var key: ByteArray? = null

    actual fun getKey(collection: String): ByteArray? {
        if (key != null)
            return key!!

        val keyFile = File("${StorageManager.getLocalStorageDir(collection)}$.k")

        if (keyFile.exists() && keyFile.readBytes().size == Constants.AES_256_KEY_LENGTH) {
            key = keyFile.readBytes()
        } else {
            key = generateNewKey()
            keyFile.delete()
            keyFile.writeBytes(key!!)
        }

        return key!!
    }

    private fun generateNewKey() : ByteArray {
        val byteArray = ByteArray(Constants.AES_256_KEY_LENGTH)
        SecureRandom.nextBytes(byteArray)
        return byteArray
    }
}