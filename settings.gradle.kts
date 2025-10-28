pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Hi-lingual"
// app
include(":app")

// core
include(":core:common")
include(":core:crypto")
include(":core:designsystem")
include(":core:localstorage")
include(":core:navigation")
include(":core:network")

// data
include(":data:auth")
include(":data:calendar")
include(":data:diary")
include(":data:user")
include(":data:voca")
include(":data:feed")
include(":data:presigned")

//presentation
include(":presentation:auth")
include(":presentation:diaryfeedback")
include(":presentation:diarywrite")
include(":presentation:home")
include(":presentation:voca")
include(":presentation:main")
include(":presentation:onboarding")
include(":presentation:splash")
include(":presentation:mypage")
include(":presentation:otp")
include(":presentation:feed")
include(":presentation:feedprofile")
include(":presentation:notification")
include(":presentation:feeddiary")

// Benchmarks
include(":baselineprofile")
