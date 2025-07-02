import com.hilingual.build_logic.configureKotlin

plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(17)
}

configureKotlin()