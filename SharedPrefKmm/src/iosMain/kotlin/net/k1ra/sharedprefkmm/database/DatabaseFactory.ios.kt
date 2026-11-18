package net.k1ra.sharedprefkmm.database

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
private fun databaseDirectory(): String {
    // The app's home directory isn't writable on device; Application Support is, but may not exist yet
    val url = NSFileManager.defaultManager.URLForDirectory(
        directory = NSApplicationSupportDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = true,
        error = null
    )
    return requireNotNull(url?.path) { "Unable to resolve the Application Support directory" }
}

internal actual fun createDatabaseBuilder(collection: String): RoomDatabase.Builder<PreferenceDatabase> {
    return Room.databaseBuilder<PreferenceDatabase>("${databaseDirectory()}/SharedPrefKmm-$collection.db") {
        PreferenceDatabaseConstructor.initialize()
    }.setDriver(BundledSQLiteDriver())
}
