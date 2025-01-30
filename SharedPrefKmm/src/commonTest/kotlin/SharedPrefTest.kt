import kotlinx.coroutines.runBlocking
import net.k1ra.sharedprefkmm.SharedPreferences
import net.k1ra.sharedprefkmm.util.TestConfig
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SharedPrefTest {
    val pref = SharedPreferences("UnitTests")

    @BeforeTest
    fun setup() {
        TestConfig.testMode = true
    }

    @Test
    fun allPrimitivesAsync() {
        runBlocking {
            //String
            val str: String = "test str"
            pref.set("str", str)
            assertEquals(str, pref.get<String>("str"))

            //Char
            val char: Char = 'c'
            pref.set("char", char)
            assertEquals(char, pref.get<Char>("char"))

            //Bool
            val bool: Boolean = true
            pref.set("bool", bool)
            assertEquals(bool, pref.get<Boolean>("bool"))

            //Byte
            val byte: Byte = 50
            pref.set("byte", byte)
            assertEquals(byte, pref.get<Byte>("byte"))

            //Short
            val short: Short = 4
            pref.set("short", short)
            assertEquals(short, pref.get<Short>("short"))

            //Int
            val int: Int = 20
            pref.set("int", int)
            assertEquals(int, pref.get<Int>("int"))

            //Long
            val long: Long = 32432
            pref.set("long", long)
            assertEquals(long, pref.get<Long>("long"))

            //UByte
            val ubyte: UByte = 50u
            pref.set("ubyte", ubyte)
            assertEquals(ubyte, pref.get<UByte>("ubyte"))

            //UShort
            val ushort: UShort = 4u
            pref.set("ushort", ushort)
            assertEquals(ushort, pref.get<UShort>("ushort"))

            //UInt
            val uint: UInt = 20u
            pref.set("uint", uint)
            assertEquals(uint, pref.get<UInt>("uint"))

            //ULong
            val ulong: ULong = 32432u
            pref.set("ulong", ulong)
            assertEquals(ulong, pref.get<ULong>("ulong"))

            //Unit
            val unit = Unit
            pref.set("unit", unit)
            assertEquals(unit, pref.get<Unit>("unit"))

            //ByteArray
            val barr = "test".encodeToByteArray()
            pref.set("barr", barr)
            assertEquals(barr.decodeToString(), pref.get<ByteArray>("barr")!!.decodeToString())
        }
    }

    @Test
    fun obj() {
        runBlocking {
            val obj = TestObject("test", 123)
            pref.set("obj", obj)
            assertEquals(obj, pref.get<TestObject>("obj"))
        }
    }

}