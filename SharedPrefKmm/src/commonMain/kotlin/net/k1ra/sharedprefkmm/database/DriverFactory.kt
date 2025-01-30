package net.k1ra.sharedprefkmm.database

import app.cash.sqldelight.db.SqlDriver

internal expect object DriverFactory {
    suspend fun createDriver(collection: String): SqlDriver
}