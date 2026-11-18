package net.k1ra.sharedprefkmm.database

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query

@Dao
interface PreferenceDao {
    @Insert
    suspend fun insert(item: PreferenceEntity)

    @Query("DELETE FROM PreferenceEntity WHERE (key = :key)")
    suspend fun delete(key: String)

    @Query("UPDATE PreferenceEntity SET data = :data, iv = :iv WHERE key = :key")
    suspend fun update(data: ByteArray, iv: ByteArray, key: String)

    @Query("SELECT * FROM PreferenceEntity WHERE (key = :key)")
    suspend fun get(key: String) : PreferenceEntity?

    @Query("SELECT count(*) FROM PreferenceEntity WHERE (iv = :iv)")
    suspend fun getByIv(iv: ByteArray) : Int
}