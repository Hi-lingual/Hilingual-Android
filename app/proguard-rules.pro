# 여기에 프로젝트별 ProGuard 규칙을 추가하세요.
-keepattributes SourceFile,LineNumberTable

# Hilingual 특정 규칙

##---------------시작: kotlin serialization ----------
# kotlinx.serialization 라이브러리 클래스를 유지합니다.
-keep,includedescriptorclasses class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

# @Serializable 어노테이션이 붙은 모든 클래스 및 멤버를 유지합니다.
# 이렇게 하면 R8이 직렬화에 필요한 생성자나 프로퍼티를 제거하는 것을 방지합니다.
-keep @kotlinx.serialization.Serializable class com.hilingual.** { *; }

# 생성된 serializer 클래스를 유지합니다.
-keep,includedescriptorclasses class **$serializer { *; }
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
