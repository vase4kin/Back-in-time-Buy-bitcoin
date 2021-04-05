import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import app.buildSrc.Libs

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("io.gitlab.arturbosch.detekt")
    id ("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {
    android()
    ios {
        binaries {
            framework {
                baseName = "shared-libraries:coindesk-service"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Libs.Kmm.Ktor.clientCore)
                implementation(Libs.Kmm.Ktor.clientCio)
                implementation(Libs.Kmm.Ktor.clientSerialization)
                implementation(Libs.Kmm.Ktor.logBack)
                implementation(Libs.Kmm.Ktor.clientLogging)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Libs.Kmm.Ktor.clientAndroid)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Libs.Kmm.Ktor.clientIos)
            }
        }
    }
}

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

detekt {
    input = files(
        "src/commonMain/kotlin",
        "src/iOSMain/kotlin",
        "src/jvmMain/kotlin"
    )
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework =
        kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)