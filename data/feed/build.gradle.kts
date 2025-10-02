import com.hilingual.buildlogic.setNamespace

plugins {
    alias(libs.plugins.hilingual.android.data)
}

android {
    setNamespace("data.feed")
}
