package net.k1ra.sharedprefkmm.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual val IODispatcher: CoroutineDispatcher
    get() = Dispatchers.IO