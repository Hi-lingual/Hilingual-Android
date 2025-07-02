import com.hilingual.build_logic.configureHiltAndroid
import com.hilingual.build_logic.configureKotlinAndroid


plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()