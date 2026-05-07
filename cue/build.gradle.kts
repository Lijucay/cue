plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
}

android {
    namespace = "de.lijucay.cue"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
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

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "de.lijucay"
            artifactId = "cue"
            version = "1.0.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}