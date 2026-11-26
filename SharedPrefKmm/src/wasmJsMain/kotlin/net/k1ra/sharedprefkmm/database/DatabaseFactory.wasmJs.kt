package net.k1ra.sharedprefkmm.database

import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import com.eygraber.sqldelight.androidx.driver.opfs.OpfsMultiTabMode
import com.eygraber.sqldelight.androidx.driver.opfs.opfsWorker

internal actual fun createDatabaseBuilder(collection: String): RoomDatabase.Builder<PreferenceDatabase> {
    return Room.databaseBuilder<PreferenceDatabase>("SharedPrefKmm-$collection.db") {
        PreferenceDatabaseConstructor.initialize()
    }.setDriver(WebWorkerSQLiteDriver(opfsWorker(multiTabMode = OpfsMultiTabMode.Shared).worker)).setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
}
