package net.k1ra.sharedprefkmm.database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver


internal actual object DriverFactory {
    actual suspend fun createDriver(collection: String): SqlDriver {
        return NativeSqliteDriver(SharedPrefDatabase.Schema.synchronous(), "SharedPrefKmm.db")
    }
}