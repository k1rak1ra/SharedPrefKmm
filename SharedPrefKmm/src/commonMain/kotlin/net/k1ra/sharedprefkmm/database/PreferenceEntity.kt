package net.k1ra.sharedprefkmm.database

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class PreferenceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val key: String,
    val data: ByteArray,
    val iv: ByteArray
)