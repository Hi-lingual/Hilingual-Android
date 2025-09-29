import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    alias(libs.plugins.hilingual.android.test)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.hilingual.baselineprofile"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    }

    targetProjectPath = ":app"

    testOptions {
        managedDevices {
            allDevices {
                create<ManagedVirtualDevice>("pixel8proApi35") {
                    device = "Pixel 8 Pro"
                    apiLevel = 35
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }
}

baselineProfile {
    managedDevices.add("pixel8proApi35")
    useConnectedDevices = false
}

dependencies {
    implementation(libs.androidx.junit)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}

androidComponents {
    onVariants { v ->
        val artifactsLoader = v.artifacts.getBuiltArtifactsLoader()
        v.instrumentationRunnerArguments.put(
            "targetAppId",
            v.testedApks.map { artifactsLoader.load(it)?.applicationId }
        )
    }
}
