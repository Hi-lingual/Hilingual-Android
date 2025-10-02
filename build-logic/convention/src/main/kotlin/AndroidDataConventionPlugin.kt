import com.android.build.api.dsl.LibraryExtension
import com.hilingual.buildlogic.DependencyManager
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidDataConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // data 모듈에 필요한 기반 플러그인들을 적용합니다.
            pluginManager.apply("hilingual.android.library")
            pluginManager.apply("hilingual.hilt")
            pluginManager.apply("hilingual.serialization")

            // 리소스 패키징 옵션을 설정합니다.
            extensions.configure<LibraryExtension> {
                packaging {
                    resources {
                        excludes.add("META-INF/**")
                    }
                }
            }

            // data 모듈에 공통적으로 필요한 의존성들을 추가합니다.
            DependencyManager.addDataDependencies(this)
        }
    }
}
