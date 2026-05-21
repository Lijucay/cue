plugins {
    alias(libs.plugins.android.library)
    id("com.vanniktech.maven.publish") version "0.36.0"
}

android {
    namespace = "de.lijucay.cue"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    api(project(":cue-write"))
    api(project(":cue-read"))
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("de.lijucay", "cue", libs.versions.dependencyVer.get())

    pom {
        name = "Cue"
        description = "NFC library for Android"
        url = "https://github.com/Lijucay/Cue"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "lijucay"
                name = "Luca"
                url = "https://lijucay.de"
            }
        }
        scm {
            url = "https://github.com/Lijucay/Cue"
            connection = "scm:git:git://github.com/Lijucay/Cue.git"
            developerConnection = "scm:git:ssh://git@github.com/Lijucay/Cue.git"
        }
    }
}