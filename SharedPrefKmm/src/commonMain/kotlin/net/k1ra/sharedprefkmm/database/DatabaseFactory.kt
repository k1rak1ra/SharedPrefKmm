package net.k1ra.sharedprefkmm.database

internal object DatabaseFactory {
    suspend fun provideDatabase(collection: String) : SharedPrefQueries {
        return SharedPrefQueries(DriverFactory.createDriver(collection))
    }
}