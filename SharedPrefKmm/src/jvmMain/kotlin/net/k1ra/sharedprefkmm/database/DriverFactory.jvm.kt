package net.k1ra.sharedprefkmm.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import net.k1ra.sharedprefkmm.StorageManager
import java.io.File

internal actual object DriverFactory {
    actual fun createDriver(collection: String): SqlDriver {
        val dbLocation = File(StorageManager.getLocalStorageDir(collection))
        if (!dbLocation.exists())
            dbLocation.mkdirs()

        val dbFile = File("${StorageManager.getLocalStorageDir(collection)}$collection.db")
        return if (dbFile.exists()) {
            JdbcSqliteDriver("jdbc:sqlite:${StorageManager.getLocalStorageDir(collection)}$collection.db")
        } else {
            JdbcSqliteDriver("jdbc:sqlite:${StorageManager.getLocalStorageDir(collection)}$collection.db").apply {
                SharedPrefDatabase.Schema.create(this)
            }
        }
    }
}