package net.k1ra.sharedprefkmm.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val IODispatcher: CoroutineDispatcher
    get() = Dispatchers.IO