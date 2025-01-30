package net.k1ra.sharedprefkmm.cryptography

internal expect object KeyManager {
    fun getKey(collection: String) : ByteArray?
}