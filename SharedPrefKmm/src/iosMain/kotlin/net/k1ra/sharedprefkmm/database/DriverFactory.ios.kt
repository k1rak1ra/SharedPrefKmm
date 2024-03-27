package net.k1ra.sharedprefkmm.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver


internal actual object DriverFactory {
    actual fun createDriver(collection: String): SqlDriver {
        return NativeSqliteDriver(SharedPrefDatabase.Schema, "SharedPrefKmm.db")
    }
}