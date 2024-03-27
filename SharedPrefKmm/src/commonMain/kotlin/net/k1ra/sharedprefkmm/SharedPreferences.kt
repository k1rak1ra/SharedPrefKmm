package net.k1ra.sharedprefkmm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.k1ra.sharedprefkmm.cryptography.CipherMode
import net.k1ra.sharedprefkmm.cryptography.Cryptography
import net.k1ra.sharedprefkmm.database.DatabaseFactory
import kotlin.coroutines.resume
import kotlin.reflect.typeOf

class SharedPreferences(private val collection: String) {
    private val db = DatabaseFactory.provideDatabase(collection)
    private val crypto = Cryptography(collection)
    var kotlinJson = Json { ignoreUnknownKeys = true }

    inline fun <reified T> getSynchronously(key: String) : T? {
        val data = getFromDbAndDecrypt(key)
        data ?: return null
        return convertResponseBody<T>(data)
    }

    suspend inline fun <reified T> get(key: String) : T? = suspendCancellableCoroutine { continuation ->
        CoroutineScope(Dispatchers.IO).launch {
            val data = getFromDbAndDecrypt(key)
            if (data == null)
                continuation.resume(null as T?)
            else
                continuation.resume(convertResponseBody<T>(data))
        }
    }

    fun deleteSynchronously(key: String) {
        db.delete(key, collection)
    }

    suspend fun delete(key: String) : Unit = suspendCancellableCoroutine { continuation ->
        CoroutineScope(Dispatchers.IO).launch {
            deleteSynchronously(key)
            continuation.resume(Unit)
        }
    }

    inline fun <reified B> setSynchronously(key: String, value: B?) {
        deleteSynchronously(key)
        setToDbAndEncrypt(key, convertRequestBody(value))
    }

    suspend inline fun <reified B> set(key: String, value: B?) : Unit = suspendCancellableCoroutine { continuation ->
        CoroutineScope(Dispatchers.IO).launch {
            delete(key)
            setToDbAndEncrypt(key, convertRequestBody(value))
            continuation.resume(Unit)
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

    fun getFromDbAndDecrypt(key: String) : ByteArray? {
        val data = db.get(key, collection).executeAsOneOrNull()
        data ?: return null

        return crypto.runAes(data.value_, data.iv, CipherMode.DECRYPT)
    }

    fun setToDbAndEncrypt(key: String, data: ByteArray) {
        var iv = crypto.generateIv()

        while(db.getByIv(iv).executeAsOne() > 0)
            iv = crypto.generateIv()

        val encryptedData = crypto.runAes(data, iv, CipherMode.ENCRYPT)
        db.insert(collection, key, encryptedData, iv)
    }
}