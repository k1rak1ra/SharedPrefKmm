package net.k1ra.sharedprefkmm.util

import kotlinx.coroutines.CoroutineDispatcher

object Constants {
    const val AES_256_KEY_LENGTH = 256
    const val AES_256_IV_LENGTH = 32
}

expect val IODispatcher: CoroutineDispatcher