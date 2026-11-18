package net.k1ra.sharedprefkmm.database

import androidx.room3.RoomDatabase
import net.k1ra.sharedprefkmm.util.IODispatcher

/** Platform builder, including the SQLiteDriver. Must be built (not just constructed) so Room initializes its coroutine scope. */
internal expect fun createDatabaseBuilder(collection: String): RoomDatabase.Builder<PreferenceDatabase>

internal fun buildPreferenceDatabase(collection: String): PreferenceDatabase =
    createDatabaseBuilder(collection)
        .setQueryCoroutineContext(IODispatcher)
        .build()
