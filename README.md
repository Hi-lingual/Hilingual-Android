# Hi-lingual

![Kotlin](https://img.shields.io/badge/Kotlin-2.3.0-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-34A853?style=flat&logo=android&logoColor=white)
![MinSDK](https://img.shields.io/badge/minSdk-30-3DDC84?style=flat&logo=android&logoColor=white)

**하이링구얼: 영어 일기, 기록**

<p align="center">
  <img width="1024" height="500" alt="image" src="https://github.com/user-attachments/assets/75940f68-5748-4bcd-958a-925555c09c36" />
</p>

> "영어를 부담스러운 공부가 아닌, 나를 표현하는 언어로 느낄 수 있도록"

하이링구얼은 일상 속 영어를 함께하는 일기 기록 서비스입니다.

- 시간에 쫓겨 영어 공부를 미루셨던 분
- 교과서 영어가 아닌 실생활 영어를 배우고 싶었던 분
- 일상 기록에 동기부여를 바라는 분

모두, 하루를 기록하며 영어를 나의 것으로 만들어보세요 😊

## Download

<a href="https://play.google.com/store/apps/details?id=com.hilingual">
  <img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="80"/>
</a>

## Tech Stack

| Category | Stack |
| --- | --- |
| **Architecture** | Recommended App Architecture |
| **UI** | Jetpack Compose |
| **DI** | Dagger-Hilt |
| **Asynchronous** | Kotlin Coroutine, Flow |
| **Modularization** | Android App Modularization |
| **Build Configuration** | Gradle Version Catalog, Custom Convention Plugins |

## Module Dependency Graph

### High-Level Architecture

```mermaid
graph TD
    A[app] --> P[Presentation Layer]
    P --> D[Data Layer]
    D --> C[Core Layer]
```

### Presentation Layer Dependencies

> **presentation:main** 모듈은 아래 그래프의 모든 Presentation 모듈을 포함하며,  
> 모든 Presentation 모듈은 공통적으로 **core:ui**와 **core:navigation** 모듈에 의존합니다.

```mermaid
graph TD
    subgraph Presentation Layer
        auth["auth"]
        diaryfeedback["diaryfeedback"]
        diarywrite["diarywrite"]
        feed["feed"]
        feeddiary["feeddiary"]
        feedprofile["feedprofile"]
        home["home"]
        mypage["mypage"]
        notification["notification"]
        signup["signup"]
        splash["splash"]
        voca["voca"]
    end

    subgraph Data Layer
        data_auth["data:auth"]
        data_calendar["data:calendar"]
        data_config["data:config"]
        data_diary["data:diary"]
        data_feed["data:feed"]
        data_user["data:user"]
        data_voca["data:voca"]
    end

    auth --> data_auth
    auth --> data_user
    diaryfeedback --> data_diary
    diarywrite --> data_calendar
    diarywrite --> data_diary
    feed --> data_feed
    feed --> data_diary
    feed --> data_user
    feeddiary --> data_diary
    feeddiary --> data_feed
    feeddiary --> data_user
    feedprofile --> data_feed
    feedprofile --> data_user
    feedprofile --> data_diary
    home --> data_user
    home --> data_diary
    home --> data_calendar
    mypage --> data_user
    mypage --> data_auth
    notification --> data_user
    signup --> data_user
    splash --> data_auth
    splash --> data_user
    splash --> data_config
    voca --> data_voca
    voca --> data_diary
```

### Data Layer Dependencies

```mermaid
graph TD
    subgraph Data Layer
        auth["auth"]
        calendar["calendar"]
        config["config"]
        diary["diary"]
        feed["feed"]
        presigned["presigned"]
        user["user"]
        voca["voca"]
    end

    subgraph Core Layer
        core_network["core:network"]
        core_localstorage["core:localstorage"]
        core_common["core:common"]
    end

    auth --> core_network
    auth --> core_localstorage
    auth --> core_common

    calendar --> core_network
    calendar --> core_localstorage
    calendar --> core_common

    config --> core_network
    config --> core_localstorage
    config --> core_common

    diary --> presigned
    diary --> core_network
    diary --> core_localstorage
    diary --> core_common

    feed --> core_network
    feed --> core_localstorage
    feed --> core_common

    presigned --> core_network
    presigned --> core_localstorage
    presigned --> core_common

    user --> presigned
    user --> core_network
    user --> core_localstorage
    user --> core_common

    voca --> core_network
    voca --> core_localstorage
    voca --> core_common
```

### Core Layer Dependencies

```mermaid
graph TD
    subgraph Core Layer
        ui["ui"]
        designsystem["designsystem"]
        network["network"]
        localstorage["localstorage"]
        common["common"]
        crypto["crypto"]
        navigation["navigation"]
        work["work"]
        notification["notification"]
    end

    ui --> designsystem
    ui --> common
    designsystem --> common
    network --> localstorage
    network --> common
    localstorage --> crypto
    work --> notification
    notification --> designsystem
```

## Contributors
<div align="center">

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/angryPodo"><img src="https://github.com/angryPodo.png?s=100" width="100px;" alt="Han Minjae"/><br /><sub><b>Han Minjae</b></sub></a><br /><a href="https://github.com/Hi-lingual/Hilingual-Android/commits?author=angryPodo" title="Code">💻</a> <a href="#infra-angryPodo" title="Infrastructure (Hosting, Build-Tools, etc)">🚇</a> <a href="#maintenance-angryPodo" title="Maintenance">🚧</a> <a href="https://github.com/Hi-lingual/Hilingual-Android/pulls?q=is%3Apr+reviewed-by%3AangryPodo" title="Reviewed Pull Requests">👀</a>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/nahy-512"><img src="https://github.com/nahy-512.png?s=100" width="100px;" alt="Kim Nahyun"/><br /><sub><b>Kim Nahyun</b></sub></a><br /><a href="https://github.com/Hi-lingual/Hilingual-Android/commits?author=nahy-512" title="Code">💻</a> <a href="https://github.com/Hi-lingual/Hilingual-Android/pulls?q=is%3Apr+reviewed-by%3Anahy-512" title="Reviewed Pull Requests">👀</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/nhyeonii"><img src="https://github.com/nhyeonii.png?s=100" width="100px;" alt="Kim Nahyun"/><br /><sub><b>Kim Nahyun</b></sub></a><br /><a href="https://github.com/Hi-lingual/Hilingual-Android/commits?author=nhyeonii" title="Code">💻</a> <a href="https://github.com/Hi-lingual/Hilingual-Android/pulls?q=is%3Apr+reviewed-by%3Anhyeonii" title="Reviewed Pull Requests">👀</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/Daljyeong"><img src="https://github.com/Daljyeong.png?s=100" width="100px;" alt="Moon Jiyoung"/><br /><sub><b>Moon Jiyoung</b></sub></a><br /><a href="https://github.com/Hi-lingual/Hilingual-Android/commits?author=Daljyeong" title="Code">💻</a> <a href="https://github.com/Hi-lingual/Hilingual-Android/pulls?q=is%3Apr+reviewed-by%3ADaljyeong" title="Reviewed Pull Requests">👀</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/Hyobeen-Park"><img src="https://github.com/Hyobeen-Park.png?s=100" width="100px;" alt="Park Hyobeen"/><br /><sub><b>Park Hyobeen</b></sub></a><br /><a href="https://github.com/Hi-lingual/Hilingual-Android/commits?author=Hyobeen-Park" title="Code">💻</a> <a href="https://github.com/Hi-lingual/Hilingual-Android/pulls?q=is%3Apr+reviewed-by%3AHyobeen-Park" title="Reviewed Pull Requests">👀</a></td>
    </tr>
  </tbody>
  <tfoot>
    <tr>
      <td align="center" size="13px" colspan="7">
        <img src="https://raw.githubusercontent.com/all-contributors/all-contributors-cli/1b8533af435da9854653492b1327a23a4dbd0a10/assets/logo-small.svg">
          <a href="https://all-contributors.js.org/docs/en/bot/usage">Add your contributions</a>
        </img>
      </td>
    </tr>
  </tfoot>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->
 </div>

<p align="center">
  Made with by Hi-lingual Team
</p>
