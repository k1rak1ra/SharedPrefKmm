package net.k1ra.sharedprefkmm.database

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import net.k1ra.sharedprefkmm.SharedPrefKmmInitContentProvider
import net.k1ra.sharedprefkmm.util.TestConfig

internal actual object DriverFactory {
    actual suspend fun createDriver(collection: String): SqlDriver {
        //If we're in test mode and can't use Context, use in-memory DB instead
        return if (TestConfig.testMode) {
            JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
                SharedPrefDatabase.Schema.awaitCreate(this)
            }
        } else {
            AndroidSqliteDriver(
                schema = SharedPrefDatabase.Schema.synchronous(),
                context = SharedPrefKmmInitContentProvider.appContext,
                name = "SharedPrefKmm.db"
            )
        }
    }
}