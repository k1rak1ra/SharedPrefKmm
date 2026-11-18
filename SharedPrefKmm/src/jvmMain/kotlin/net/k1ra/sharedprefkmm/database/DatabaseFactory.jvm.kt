package net.k1ra.sharedprefkmm.database

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import net.k1ra.sharedprefkmm.StorageManager
import java.io.File

internal actual fun createDatabaseBuilder(collection: String): RoomDatabase.Builder<PreferenceDatabase> {
    val dir = File(StorageManager.getLocalStorageDir(collection)).apply { mkdirs() }
    return Room.databaseBuilder<PreferenceDatabase>(File(dir, "$collection.db").absolutePath) {
        PreferenceDatabaseConstructor.initialize()
    }.setDriver(BundledSQLiteDriver())
}
