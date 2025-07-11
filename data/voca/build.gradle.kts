import com.hilingual.build_logic.setNamespace

plugins {
    alias(libs.plugins.hilingual.data)
}

android {
    setNamespace("data.voca")
}
