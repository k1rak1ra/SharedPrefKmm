plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    id("maven-publish")
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.kover)
    alias(libs.plugins.dokka)
}

group = "net.k1ra.shared_pref_kmm"
version = System.getenv("releaseName") ?: "999999.999999.999999"

sqldelight {
    databases {
        create("SharedPrefDatabase") {
            packageName.set("net.k1ra.sharedprefkmm.database")
        }
    }
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
        publishLibraryVariants("release", "debug")
    }

    targets.all {
        compilations.all {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "SharedPrefKmm"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.coroutines)
            implementation(libs.kotlin.serialization)
            implementation(libs.kotlin.datetime)
            implementation(libs.kotlin.reflect)
            implementation(libs.korlibs.crypto)
            implementation(libs.bundles.sqldelight.common)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.sqldelight.driver.android)
            implementation(libs.sqldelight.driver.jdbc)
            implementation(libs.androidx.crypto)
        }

        iosMain.dependencies {
            implementation(libs.sqldelight.driver.ios)
        }

        jvmMain.dependencies {
            implementation(libs.sqldelight.driver.jdbc)
        }
    }
}

android {
    namespace = "net.k1ra.sharedprefkmm"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

publishing {
    repositories {
        maven {
            name = "k1ra-nexus"
            url = uri("https://k1ra.net/nexus/repository/public/")

            credentials(PasswordCredentials::class) {
                username = System.getenv("NEXUS_USERNAME") ?: "anonymous"
                password = System.getenv("NEXUS_PASSWORD") ?: ""
            }
        }
    }
}

tasks{
    register<Jar>("dokkaJar") {
        from(dokkaHtml)
        dependsOn(dokkaHtml)
        archiveClassifier.set("javadoc")
    }
}