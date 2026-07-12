plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.vanniktech.maven)
}

android {
    namespace = "de.lijucay.cue.read"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("de.lijucay", "cue-read", libs.versions.dependencyVer.get())

    pom {
        name = "Cue"
        description = "Cue NFC reader library for Android"
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

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
