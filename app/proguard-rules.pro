# 여기에 프로젝트별 ProGuard 규칙을 추가하세요.
-keepattributes SourceFile,LineNumberTable

# Hilingual 특정 규칙

##---------------시작: kotlin serialization ----------
# DTO를 위해 필수적인, 프로젝트 내 @Serializable 어노테이션이 붙은 클래스를 유지합니다.
-keepclassmembers,allowobfuscation,allowshrinking @kotlinx.serialization.Serializable class com.hilingual.** {
    *** Companion;
    kotlinx.serialization.KSerializer serializer(...);
}

# 직렬화 가능 클래스의 Companion 객체를 유지합니다.
-if @kotlinx.serialization.Serializable class com.hilingual.**
-keepclassmembers class com.hilingual.<1>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}

# @SerialName 어노테이션이 붙은 필드를 유지합니다.
-keepclassmembers class com.hilingual.** {
    @kotlinx.serialization.SerialName <fields>;
}

# 생성된 serializer를 유지합니다.
-keep,allowobfuscation,allowshrinking class **$$serializer {
    *** Companion;
    *** INSTANCE;
}

# kotlinx.serialization 기본 클래스를 유지합니다.
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**
##---------------종료: kotlin serialization ----------

##---------------시작: Hilt ----------
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel
##---------------종료: Hilt ----------

##---------------시작: Room ----------
# Room은 자체 consumer rule을 가지고 있지만, 페이징 등을 위해 안전하게 추가합니다.
-keep class androidx.room.paging.** { *; }
-dontwarn androidx.room.paging.**
##---------------종료: Room ----------

##---------------시작: Coil 3 ----------
-keep class * extends coil3.util.DecoderServiceLoaderTarget { *; }
##---------------종료: Coil 3 ----------

##---------------시작: ML Kit ----------
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**
##---------------종료: ML Kit ----------

##---------------시작: AndroidX Credentials ----------
-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** { *; }
-keep class androidx.credentials.fido.** { *; }
##---------------종료: AndroidX Credentials ----------

##---------------시작: Timber ----------
-keep class timber.log.Timber$Tree { *; }
-keep class timber.log.Timber$DebugTree { *; }
##---------------종료: Timber ----------
