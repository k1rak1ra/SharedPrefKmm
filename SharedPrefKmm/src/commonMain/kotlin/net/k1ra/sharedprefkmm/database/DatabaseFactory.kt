package net.k1ra.sharedprefkmm.database

internal object DatabaseFactory {
    fun provideDatabase(collection: String) : SharedPrefQueries {
        return SharedPrefQueries(DriverFactory.createDriver(collection))
    }
}