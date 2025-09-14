# 이 파일은 이 모듈에 의존하는 모든 프로젝트에 자동으로 적용됩니다.

##---------------시작: Retrofit & OkHttp ----------
# JSR 305 어노테이션은 null 허용 여부 정보를 포함하기 위한 것입니다.
-dontwarn javax.annotation.**
# 리소스가 상대 경로로 로드되므로 이 클래스의 패키지는 유지되어야 합니다.
-adaptresourcefilenames okhttp3/internal/publicsuffix/PublicSuffixDatabase.gz
# Animal Sniffer는 이전 버전의 Java와 API 호환성을 보장하기 위한 compileOnly 의존성입니다.
-dontwarn org.codehaus.mojo.animal_sniffer.*
# OkHttp 플랫폼은 JVM에서만 사용되며, Conscrypt 및 기타 보안 제공자가 있을 때 사용됩니다.
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Retrofit은 제네릭 파라미터, 메소드 및 파라미터 어노테이션에 리플렉션을 사용합니다.
-keepattributes Signature, InnerClasses, EnclosingMethod, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, AnnotationDefault

# 최적화 시 서비스 메소드 파라미터를 유지합니다.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# 빌드 도구에 사용되는 어노테이션 경고를 무시합니다.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
# 클래스패스에 있을 때만 NoClassDefFoundError try/catch로 보호되어 사용됩니다.
-dontwarn kotlin.Unit
# Kotlin에서만 사용할 수 있는 최상위 함수에 대한 경고를 무시합니다.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# R8 전체 모드에서는 Retrofit 인터페이스의 하위 유형을 볼 수 없으므로 (프록시로 생성됨)
# 모든 잠재적 값을 null로 대체합니다. 인터페이스를 명시적으로 유지하면 이를 방지할 수 있습니다.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Call, Response의 제네릭 시그니처를 유지합니다 (R8 전체 모드는 유지되지 않은 항목에서 시그니처를 제거합니다).
-keep interface retrofit2.Call { *; }
-keep class retrofit2.Response { *; }

# Suspend 함수는 타입 인자가 사용되는 continuation으로 래핑됩니다.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
##---------------종료: Retrofit & OkHttp ----------