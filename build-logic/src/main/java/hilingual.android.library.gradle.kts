import com.android.build.gradle.LibraryExtension
import com.hilingual.build_logic.configureBuildTypes
import com.hilingual.build_logic.configureCoroutineAndroid
import com.hilingual.build_logic.configureHiltAndroid
import com.hilingual.build_logic.configureKotlinAndroid

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureCoroutineAndroid()
configureHiltAndroid()


extensions.configure<LibraryExtension> {
    configureBuildTypes(this)
}