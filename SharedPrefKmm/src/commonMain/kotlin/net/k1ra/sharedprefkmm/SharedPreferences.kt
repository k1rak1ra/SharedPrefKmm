package net.k1ra.sharedprefkmm

import app.cash.sqldelight.async.coroutines.awaitAsOne
import app.cash.sqldelight.async.coroutines.awaitAsOneOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import net.k1ra.sharedprefkmm.cryptography.CipherMode
import net.k1ra.sharedprefkmm.cryptography.Cryptography
import net.k1ra.sharedprefkmm.database.DatabaseFactory
import net.k1ra.sharedprefkmm.database.SharedPrefQueries
import net.k1ra.sharedprefkmm.util.IODispatcher
import kotlin.reflect.typeOf

class SharedPreferences(private val collection: String) {
    private var db: SharedPrefQueries? = null
    private val dbMutex = Mutex()
    val writeMutex = Mutex()
    private val crypto = Cryptography(collection)
    var kotlinJson = Json { ignoreUnknownKeys = true }

    suspend inline fun <reified T> get(key: String) : T? {
        val data = getFromDbAndDecrypt(key)
        data ?: return null
        return convertResponseBody<T>(data)
    }

    private suspend fun getDb() : SharedPrefQueries {
        db?.let { return it }
        return dbMutex.withLock {
            db ?: DatabaseFactory.provideDatabase(collection).also { db = it }
        }
    }

    suspend fun delete(key: String) {
        writeMutex.withLock {
            getDb().delete(key, collection)
        }
    }

    suspend inline fun <reified B> set(key: String, value: B?) {
        withContext(IODispatcher) {
            writeMutex.withLock {
                setToDbAndEncrypt(key, convertRequestBody(value))
            }
        }
    }

    inline fun <reified B> convertRequestBody(body: B? = null) : ByteArray {
        return when (body) {
            null -> ByteArray(0)
            is Unit -> ByteArray(0)
            is ByteArray -> body
            is String -> body.encodeToByteArray()
            else -> kotlinJson.encodeToString(body).encodeToByteArray()
        }
    }

    inline fun <reified T> convertResponseBody(data: ByteArray) : T {
        return when (typeOf<T>()) {
            typeOf<Unit>() -> Unit as T
            typeOf<ByteArray>() -> data as T
            typeOf<String>() -> data.decodeToString() as T
            else -> kotlinJson.decodeFromString(data.decodeToString())
        }
    }

    suspend fun getFromDbAndDecrypt(key: String) : ByteArray? {
        val data = getDb().get(key, collection).awaitAsOneOrNull()
        data ?: return null

        return crypto.runAes(data.value_, data.iv, CipherMode.DECRYPT)
    }

    suspend fun setToDbAndEncrypt(key: String, data: ByteArray) {
        var iv = crypto.generateIv()

        while (getDb().getByIv(iv).awaitAsOne() > 0) {
            iv = crypto.generateIv()
        }

        val encryptedData = crypto.runAes(data, iv, CipherMode.ENCRYPT)

        if (getDb().get(key, collection).awaitAsOneOrNull() != null)
            getDb().update(encryptedData, iv, key)
        else
            getDb().insert(collection, key, encryptedData, iv)
    }
}
