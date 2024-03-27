package net.k1ra.sharedprefkmm

import java.util.Locale

object StorageManager {
    fun getLocalStorageDir(collection: String): String {
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())
        return if (os.contains("win"))
            System.getenv("APPDATA") + "/KmmSharedPref-$collection/"
        else if (os.contains("mac"))
            System.getProperty("user.home") + "/Library/Application Support/KmmSharedPref-$collection/"
        else if (os.contains("nux"))
            System.getProperty("user.home") + "/.KmmSharedPref-$collection/"
        else
            System.getProperty("user.dir") + "/.KmmSharedPref-$collection/"
    }
}