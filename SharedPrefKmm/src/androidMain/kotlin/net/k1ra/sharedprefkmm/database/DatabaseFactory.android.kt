package net.k1ra.sharedprefkmm.database

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import net.k1ra.sharedprefkmm.SharedPrefKmmInitContentProvider
import net.k1ra.sharedprefkmm.util.TestConfig

internal actual fun createDatabaseBuilder(collection: String): RoomDatabase.Builder<PreferenceDatabase> {
    //If we're in test mode and can't use Context, use in-memory DB instead
    if (TestConfig.testMode)
        return Room.inMemoryDatabaseBuilder<PreferenceDatabase> { PreferenceDatabaseConstructor.initialize() }
            .setDriver(BundledSQLiteDriver())

    return Room.databaseBuilder<PreferenceDatabase>(
        context = SharedPrefKmmInitContentProvider.appContext,
        name = "SharedPrefKmm-$collection.db"
    ) { PreferenceDatabaseConstructor.initialize() }.setDriver(BundledSQLiteDriver())
}
