import com.hilingual.build_logic.configureKotlinAndroid
import com.hilingual.build_logic.configureCoroutineAndroid
import com.hilingual.build_logic.configureHiltAndroid

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureCoroutineAndroid()
configureHiltAndroid()