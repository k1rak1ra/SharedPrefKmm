package net.k1ra.sharedprefkmm.database

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.createDefaultWebWorkerDriver

internal actual object DriverFactory {
    actual suspend fun createDriver(collection: String): SqlDriver {
        val driver = createDefaultWebWorkerDriver()
        SharedPrefDatabase.Schema.awaitCreate(driver)
        return driver
    }
}