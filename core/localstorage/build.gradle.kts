import com.hilingual.build_logic.setNamespace

plugins {
    alias(libs.plugins.hilingual.library)
}

android {
    setNamespace("core.localstorage")
}

dependencies {
    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Room
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
}