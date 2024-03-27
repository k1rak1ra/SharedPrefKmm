package net.k1ra.sharedprefkmm.database

import app.cash.sqldelight.db.SqlDriver

internal expect object DriverFactory {
    fun createDriver(collection: String): SqlDriver
}