package net.k1ra.sharedprefkmm.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor

@Database(entities = [PreferenceEntity::class], version = 1)
@ConstructedBy(PreferenceDatabaseConstructor::class)
abstract class PreferenceDatabase : RoomDatabase() {
    abstract fun getDao(): PreferenceDao
}

@Suppress("KotlinNoActualForExpect")
expect object PreferenceDatabaseConstructor : RoomDatabaseConstructor<PreferenceDatabase> {
    override fun initialize(): PreferenceDatabase
}