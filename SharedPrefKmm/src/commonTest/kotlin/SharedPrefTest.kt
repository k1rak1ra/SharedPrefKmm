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
    fun allPrimitivesSynch() {
        //String
        val str: String = "test str"
        pref.setSynchronously("sstr", str)
        assertEquals(str, pref.getSynchronously<String>("sstr"))

        //Char
        val char: Char = 'c'
        pref.setSynchronously("schar", char)
        assertEquals(char, pref.getSynchronously<Char>("schar"))

        //Bool
        val bool: Boolean = true
        pref.setSynchronously("sbool", bool)
        assertEquals(bool, pref.getSynchronously<Boolean>("sbool"))

        //Byte
        val byte: Byte = 50
        pref.setSynchronously("sbyte", byte)
        assertEquals(byte, pref.getSynchronously<Byte>("sbyte"))

        //Short
        val short: Short = 4
        pref.setSynchronously("sshort", short)
        assertEquals(short, pref.getSynchronously<Short>("sshort"))

        //Int
        val int: Int = 20
        pref.setSynchronously("sint", int)
        assertEquals(int, pref.getSynchronously<Int>("sint"))

        //Long
        val long: Long = 32432
        pref.setSynchronously("slong", long)
        assertEquals(long, pref.getSynchronously<Long>("slong"))

        //UByte
        val ubyte: UByte = 50u
        pref.setSynchronously("subyte", ubyte)
        assertEquals(ubyte, pref.getSynchronously<UByte>("subyte"))

        //UShort
        val ushort: UShort = 4u
        pref.setSynchronously("sushort", ushort)
        assertEquals(ushort, pref.getSynchronously<UShort>("sushort"))

        //UInt
        val uint: UInt = 20u
        pref.setSynchronously("suint", uint)
        assertEquals(uint, pref.getSynchronously<UInt>("suint"))

        //ULong
        val ulong: ULong = 32432u
        pref.setSynchronously("sulong", ulong)
        assertEquals(ulong, pref.getSynchronously<ULong>("sulong"))

        //Unit
        val unit = Unit
        pref.setSynchronously("sunit", unit)
        assertEquals(unit, pref.getSynchronously<Unit>("sunit"))

        //ByteArray
        val barr = "test".encodeToByteArray()
        pref.setSynchronously("sbarr", barr)
        assertEquals(barr.decodeToString(), pref.getSynchronously<ByteArray>("sbarr")!!.decodeToString())
    }

    @Test
    fun obj() {
        runBlocking {
            val obj = TestObject("test", 123)
            pref.set("obj", obj)
            assertEquals(obj, pref.get<TestObject>("obj"))
        }
    }

    @Test
    fun objSynch() {
        val obj = TestObject("test", 123)
        pref.setSynchronously("sobj", obj)
        assertEquals(obj, pref.getSynchronously<TestObject>("sobj"))
    }
}