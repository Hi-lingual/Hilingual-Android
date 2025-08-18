## 🚀 Project Core Technology & Code Style Guide

### **Directory Structure**

ver 2025.08.15
```
Directory structure:
└── hi-lingual-hilingual-android/
    ├── README.md
    ├── build.gradle.kts
    ├── gradle.properties
    ├── gradlew
    ├── gradlew.bat
    ├── LICENSE
    ├── renovate.json
    ├── settings.gradle.kts
    ├── app/
    │   ├── build.gradle.kts
    │   ├── proguard-rules.pro
    │   └── src/
    │       ├── debug/
    │       │   └── res/
    │       │       ├── mipmap-anydpi-v26/
    │       │       │   ├── ic_launcher.xml
    │       │       │   └── ic_launcher_round.xml
    │       │       ├── mipmap-hdpi/
    │       │       │   ├── ic_launcher.webp
    │       │       │   ├── ic_launcher_foreground.webp
    │       │       │   └── ic_launcher_round.webp
    │       │       ├── mipmap-mdpi/
    │       │       │   ├── ic_launcher.webp
    │       │       │   ├── ic_launcher_foreground.webp
    │       │       │   └── ic_launcher_round.webp
    │       │       ├── mipmap-xhdpi/
    │       │       │   ├── ic_launcher.webp
    │       │       │   ├── ic_launcher_foreground.webp
    │       │       │   └── ic_launcher_round.webp
    │       │       ├── mipmap-xxhdpi/
    │       │       │   ├── ic_launcher.webp
    │       │       │   ├── ic_launcher_foreground.webp
    │       │       │   └── ic_launcher_round.webp
    │       │       ├── mipmap-xxxhdpi/
    │       │       │   ├── ic_launcher.webp
    │       │       │   ├── ic_launcher_foreground.webp
    │       │       │   └── ic_launcher_round.webp
    │       │       └── values/
    │       │           ├── ic_launcher_background.xml
    │       │           └── string.xml
    │       ├── main/
    │       │   ├── AndroidManifest.xml
    │       │   ├── java/
    │       │   │   └── com/
    │       │   │       └── hilingual/
    │       │   │           └── App.kt
    │       │   └── res/
    │       │       ├── drawable/
    │       │       │   ├── ic_launcher_background.xml
    │       │       │   └── ic_launcher_foreground.xml
    │       │       ├── mipmap-anydpi/
    │       │       │   ├── ic_launcher.xml
    │       │       │   └── ic_launcher_round.xml
    │       │       ├── mipmap-anydpi-v26/
    │       │       │   ├── ic_launcher.xml
    │       │       │   └── ic_launcher_round.xml
    │       │       ├── mipmap-hdpi/
    │       │       │   ├── ic_launcher.webp
    │       │       │   ├── ic_launcher_foreground.webp
    │       │       │   └── ic_launcher_round.webp
    │       │       ├── mipmap-mdpi/
    │       │       │   ├── ic_launcher.webp
    │       │       │   ├── ic_launcher_foreground.webp
    │       │       │   └── ic_launcher_round.webp
    │       │       ├── mipmap-xhdpi/
    │       │       │   ├── ic_launcher.webp
    │       │       │   ├── ic_launcher_foreground.webp
    │       │       │   └── ic_launcher_round.webp
    │       │       ├── mipmap-xxhdpi/
    │       │       │   ├── ic_launcher.webp
    │       │       │   ├── ic_launcher_foreground.webp
    │       │       │   └── ic_launcher_round.webp
    │       │       ├── mipmap-xxxhdpi/
    │       │       │   ├── ic_launcher.webp
    │       │       │   ├── ic_launcher_foreground.webp
    │       │       │   └── ic_launcher_round.webp
    │       │       ├── values/
    │       │       │   ├── colors.xml
    │       │       │   ├── ic_launcher_background.xml
    │       │       │   └── themes.xml
    │       │       └── xml/
    │       │           ├── backup_rules.xml
    │       │           ├── data_extraction_rules.xml
    │       │           └── file_paths.xml
    │       └── release/
    │           └── res/
    │               ├── mipmap-anydpi-v26/
    │               │   ├── ic_launcher.xml
    │               │   └── ic_launcher_round.xml
    │               ├── mipmap-hdpi/
    │               │   ├── ic_launcher.webp
    │               │   ├── ic_launcher_foreground.webp
    │               │   └── ic_launcher_round.webp
    │               ├── mipmap-mdpi/
    │               │   ├── ic_launcher.webp
    │               │   ├── ic_launcher_foreground.webp
    │               │   └── ic_launcher_round.webp
    │               ├── mipmap-xhdpi/
    │               │   ├── ic_launcher.webp
    │               │   ├── ic_launcher_foreground.webp
    │               │   └── ic_launcher_round.webp
    │               ├── mipmap-xxhdpi/
    │               │   ├── ic_launcher.webp
    │               │   ├── ic_launcher_foreground.webp
    │               │   └── ic_launcher_round.webp
    │               ├── mipmap-xxxhdpi/
    │               │   ├── ic_launcher.webp
    │               │   ├── ic_launcher_foreground.webp
    │               │   └── ic_launcher_round.webp
    │               └── values/
    │                   ├── ic_launcher_background.xml
    │                   └── string.xml
    ├── build-logic/
    │   ├── build.gradle.kts
    │   ├── settings.gradle.kts
    │   └── src/
    │       └── main/
    │           └── java/
    │               ├── hilingual.android.application.gradle.kts
    │               ├── hilingual.android.compose.gradle.kts
    │               ├── hilingual.android.data.gradle.kts
    │               ├── hilingual.android.feature.gradle.kts
    │               ├── hilingual.android.library.gradle.kts
    │               ├── hilingual.kotlin.library.gradle.kts
    │               └── com/
    │                   └── hilingual/
    │                       └── build_logic/
    │                           ├── AppNameExtension.kt
    │                           ├── ComposeAndroid.kt
    │                           ├── CoroutineAndroid.kt
    │                           ├── Extentions.kt
    │                           ├── HiltAndroid.kt
    │                           ├── KotlinAndroid.kt
    │                           └── SerializationAndroid.kt
    ├── core/
    │   ├── common/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           └── java/
    │   │               └── com/
    │   │                   └── hilingual/
    │   │                       └── core/
    │   │                           └── common/
    │   │                               ├── constant/
    │   │                               │   └── UrlConstant.kt
    │   │                               ├── extension/
    │   │                               │   ├── ContextExt.kt
    │   │                               │   ├── FlowExt.kt
    │   │                               │   ├── ModfierExt.kt
    │   │                               │   ├── RunCatchingExt.kt
    │   │                               │   └── StateFlowExt.kt
    │   │                               ├── provider/
    │   │                               │   ├── SnackbarTrigger.kt
    │   │                               │   └── SystemBarsColor.kt
    │   │                               └── util/
    │   │                                   ├── EmojiFilter.kt
    │   │                                   ├── SuspendRunCatching.kt
    │   │                                   └── UiState.kt
    │   ├── designsystem/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           ├── java/
    │   │           │   └── com/
    │   │           │       └── hilingual/
    │   │           │           └── core/
    │   │           │               └── designsystem/
    │   │           │                   ├── component/
    │   │           │                   │   ├── bottomsheet/
    │   │           │                   │   │   ├── HilingualBasicBottomSheet.kt
    │   │           │                   │   │   ├── HilingualImagePickerBottomSheet.kt
    │   │           │                   │   │   ├── HilingualMenuBottomSheet.kt
    │   │           │                   │   │   └── HilingualYearMonthPickerBottomSheet.kt
    │   │           │                   │   ├── button/
    │   │           │                   │   │   ├── DialogButton.kt
    │   │           │                   │   │   ├── HilingualButton.kt
    │   │           │                   │   │   ├── HilingualFloatingButton.kt
    │   │           │                   │   │   └── UserActionButton.kt
    │   │           │                   │   ├── content/
    │   │           │                   │   │   └── UserActionItem.kt
    │   │           │                   │   ├── datapicker/
    │   │           │                   │   │   ├── HilingualBasicPicker.kt
    │   │           │                   │   │   └── HilingualYearMonthPicker.kt
    │   │           │                   │   ├── dialog/
    │   │           │                   │   │   ├── HilingualBasicDialog.kt
    │   │           │                   │   │   ├── HIlingualErrorDialog.kt
    │   │           │                   │   │   ├── OneButtonDialog.kt
    │   │           │                   │   │   ├── TwoButtonDialog.kt
    │   │           │                   │   │   └── diary/
    │   │           │                   │   │       ├── DiaryDeleteDialog.kt
    │   │           │                   │   │       ├── DiaryPublishDialog.kt
    │   │           │                   │   │       └── DiaryUnpublishDialog.kt
    │   │           │                   │   ├── dropdown/
    │   │           │                   │   │   └── HilingualBasicDropdownMenu.kt
    │   │           │                   │   ├── image/
    │   │           │                   │   │   ├── HilingualLottieImage.kt
    │   │           │                   │   │   └── NetworkImage.kt
    │   │           │                   │   ├── picker/
    │   │           │                   │   │   └── ProfileImagePicker.kt
    │   │           │                   │   ├── snackbar/
    │   │           │                   │   │   └── TextSnackBar.kt
    │   │           │                   │   ├── tabrow/
    │   │           │                   │   │   └── HilingualBasicTabRow.kt
    │   │           │                   │   ├── tag/
    │   │           │                   │   │   └── WordPhraseTypeTag.kt
    │   │           │                   │   ├── textfield/
    │   │           │                   │   │   ├── HilingualBasicTextField.kt
    │   │           │                   │   │   ├── HilingualLongTextField.kt
    │   │           │                   │   │   ├── HilingualSearchTextField.kt
    │   │           │                   │   │   └── HilingualShortTextField.kt
    │   │           │                   │   ├── toggle/
    │   │           │                   │   │   └── HilingualBasicToggleSwitch.kt
    │   │           │                   │   └── topappbar/
    │   │           │                   │       ├── BackAndMoreTopAppBar.kt
    │   │           │                   │       ├── BackTopAppBar.kt
    │   │           │                   │       ├── CloseOnlyTopAppBar.kt
    │   │           │                   │       ├── CloseTopAppBar.kt
    │   │           │                   │       ├── HilingualBasicTopAppBar.kt
    │   │           │                   │       ├── TitleCenterAlignedTopAppBar.kt
    │   │           │                   │       └── TitleLeftAlignedTopAppBar.kt
    │   │           │                   ├── event/
    │   │           │                   │   ├── DialogProvider.kt
    │   │           │                   │   ├── LocalDialogEventProvider.kt
    │   │           │                   │   └── LocalSharedTransitionScope.kt
    │   │           │                   └── theme/
    │   │           │                       ├── Color.kt
    │   │           │                       ├── Theme.kt
    │   │           │                       └── Type.kt
    │   │           └── res/
    │   │               └── drawable/
    │   │                   ├── ic_alarm_28.xml
    │   │                   ├── ic_arrow_down_20_gray.xml
    │   │                   ├── ic_arrow_down_24_black.xml
    │   │                   ├── ic_arrow_left_24.xml
    │   │                   ├── ic_arrow_left_24_back.xml
    │   │                   ├── ic_arrow_right_16_bold.xml
    │   │                   ├── ic_arrow_right_16_thin.xml
    │   │                   ├── ic_arrow_right_24.xml
    │   │                   ├── ic_arrow_up_20.xml
    │   │                   ├── ic_arrow_up_fab_24.xml
    │   │                   ├── ic_az_24.xml
    │   │                   ├── ic_bell_24.xml
    │   │                   ├── ic_block_24_black.xml
    │   │                   ├── ic_block_24_gray.xml
    │   │                   ├── ic_book_24.xml
    │   │                   ├── ic_bubble_16.xml
    │   │                   ├── ic_camera_20.xml
    │   │                   ├── ic_camera_24.xml
    │   │                   ├── ic_change_20.xml
    │   │                   ├── ic_check_24.xml
    │   │                   ├── ic_check_circle_28_checked.xml
    │   │                   ├── ic_check_circle_28_unchecked.xml
    │   │                   ├── ic_close_24.xml
    │   │                   ├── ic_close_circle_20.xml
    │   │                   ├── ic_community_24.xml
    │   │                   ├── ic_customer_24.xml
    │   │                   ├── ic_delete_24.xml
    │   │                   ├── ic_document_24.xml
    │   │                   ├── ic_error_16.xml
    │   │                   ├── ic_fire_16.xml
    │   │                   ├── ic_gallary_24.xml
    │   │                   ├── ic_google_20.xml
    │   │                   ├── ic_hide_24.xml
    │   │                   ├── ic_home_24.xml
    │   │                   ├── ic_image_24.xml
    │   │                   ├── ic_info_24.xml
    │   │                   ├── ic_like_24_empty.xml
    │   │                   ├── ic_like_24_filled.xml
    │   │                   ├── ic_list_24.xml
    │   │                   ├── ic_listdown_24.xml
    │   │                   ├── ic_logout_24.xml
    │   │                   ├── ic_more_24.xml
    │   │                   ├── ic_my_24.xml
    │   │                   ├── ic_pen_24.xml
    │   │                   ├── ic_plus_16.xml
    │   │                   ├── ic_report_24.xml
    │   │                   ├── ic_save_28_empty.xml
    │   │                   ├── ic_save_28_filled.xml
    │   │                   ├── ic_scan_16.xml
    │   │                   ├── ic_search_20.xml
    │   │                   ├── ic_setting_24.xml
    │   │                   ├── ic_time_16.xml
    │   │                   └── ic_upload_24.xml
    │   ├── localstorage/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           └── java/
    │   │               └── com/
    │   │                   └── hilingual/
    │   │                       └── core/
    │   │                           └── localstorage/
    │   │                               ├── TokenManager.kt
    │   │                               ├── TokenManagerImpl.kt
    │   │                               └── di/
    │   │                                   └── LocalStorageModule.kt
    │   ├── navigation/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           └── java/
    │   │               └── com/
    │   │                   └── hilingual/
    │   │                       └── core/
    │   │                           └── navigation/
    │   │                               ├── MainTabRoute.kt
    │   │                               └── Route.kt
    │   └── network/
    │       ├── build.gradle.kts
    │       ├── consumer-rules.pro
    │       └── src/
    │           └── main/
    │               ├── AndroidManifest.xml
    │               └── java/
    │                   └── com/
    │                       └── hilingual/
    │                           └── core/
    │                               └── network/
    │                                   ├── AuthInterceptor.kt
    │                                   ├── BaseResponse.kt
    │                                   ├── ContentUriRequestBody.kt
    │                                   ├── HeaderConstants.kt
    │                                   ├── JsonStringExt.kt
    │                                   ├── NetworkModule.kt
    │                                   ├── Qualifiers.kt
    │                                   ├── TokenAuthenticator.kt
    │                                   └── service/
    │                                       └── TokenRefreshService.kt
    ├── data/
    │   ├── auth/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           └── java/
    │   │               └── com/
    │   │                   └── hilingual/
    │   │                       └── data/
    │   │                           └── auth/
    │   │                               ├── datasource/
    │   │                               │   ├── AuthRemoteDataSource.kt
    │   │                               │   └── GoogleAuthDataSource.kt
    │   │                               ├── datasourceimpl/
    │   │                               │   ├── AuthRemoteDataSourceImpl.kt
    │   │                               │   └── GoogleAuthDataSourceImpl.kt
    │   │                               ├── di/
    │   │                               │   ├── DataSourceModule.kt
    │   │                               │   ├── RepositoryModule.kt
    │   │                               │   ├── ServiceModule.kt
    │   │                               │   └── TokenRefreshServiceModule.kt
    │   │                               ├── dto/
    │   │                               │   ├── request/
    │   │                               │   │   └── LoginRequestDto.kt
    │   │                               │   └── response/
    │   │                               │       ├── LoginResponseDto.kt
    │   │                               │       └── ReissueTokenResponseDto.kt
    │   │                               ├── model/
    │   │                               │   └── LoginModel.kt
    │   │                               ├── repository/
    │   │                               │   └── AuthRepository.kt
    │   │                               ├── repositoryimpl/
    │   │                               │   └── AuthRepositoryImpl.kt
    │   │                               └── service/
    │   │                                   ├── LoginService.kt
    │   │                                   ├── ReissueService.kt
    │   │                                   └── TokenRefreshServiceImpl.kt
    │   ├── calendar/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           └── java/
    │   │               └── com/
    │   │                   └── hilingual/
    │   │                       └── data/
    │   │                           └── calendar/
    │   │                               ├── datasouceimpl/
    │   │                               │   └── CalendarRemoteDataSourceImpl.kt
    │   │                               ├── datasource/
    │   │                               │   └── CalendarRemoteDataSource.kt
    │   │                               ├── di/
    │   │                               │   ├── DataSourceModule.kt
    │   │                               │   ├── RepositoryModule.kt
    │   │                               │   └── ServiceModule.kt
    │   │                               ├── dto/
    │   │                               │   └── response/
    │   │                               │       ├── CalendarResponseDto.kt
    │   │                               │       ├── DiaryThumbnailResponseDto.kt
    │   │                               │       └── TopicResponseDto.kt
    │   │                               ├── model/
    │   │                               │   ├── CalendarModel.kt
    │   │                               │   ├── DiaryThumbnailModel.kt
    │   │                               │   └── TopicModel.kt
    │   │                               ├── repository/
    │   │                               │   └── CalendarRepository.kt
    │   │                               ├── repositoryimpl/
    │   │                               │   └── CalendarRepositoryImpl.kt
    │   │                               └── service/
    │   │                                   └── CalendarService.kt
    │   ├── diary/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           └── java/
    │   │               └── com/
    │   │                   └── hilingual/
    │   │                       └── data/
    │   │                           └── diary/
    │   │                               ├── datasource/
    │   │                               │   └── DiaryRemoteDataSource.kt
    │   │                               ├── datasourceimpl/
    │   │                               │   └── DiaryRemoteDataSourceImpl.kt
    │   │                               ├── di/
    │   │                               │   ├── DataSourceModule.kt
    │   │                               │   ├── RepositoryModule.kt
    │   │                               │   └── ServiceModule.kt
    │   │                               ├── dto/
    │   │                               │   ├── request/
    │   │                               │   │   └── BookmarkRequestDto.kt
    │   │                               │   └── response/
    │   │                               │       ├── DiaryContentResponseDto.kt
    │   │                               │       ├── DiaryFeedbackCreateResponseDto.kt
    │   │                               │       ├── DiaryFeedbackResponseDto.kt
    │   │                               │       └── DiaryRecommendExpressionResponseDto.kt
    │   │                               ├── model/
    │   │                               │   ├── DiaryFeedbackCreateModel.kt
    │   │                               │   ├── DiaryFeedbackModel.kt
    │   │                               │   ├── DiaryModel.kt
    │   │                               │   ├── DiaryRecommendExpressionModel.kt
    │   │                               │   └── PhraseBookmarkModel.kt
    │   │                               ├── repository/
    │   │                               │   └── DiaryRepository.kt
    │   │                               ├── repositoryimpl/
    │   │                               │   └── DiaryRepositoryImpl.kt
    │   │                               └── service/
    │   │                                   └── DiaryService.kt
    │   ├── user/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           └── java/
    │   │               └── com/
    │   │                   └── hilingual/
    │   │                       └── data/
    │   │                           └── user/
    │   │                               ├── datasouceimpl/
    │   │                               │   └── UserRemoteDataSourceImpl.kt
    │   │                               ├── datasource/
    │   │                               │   └── UserRemoteDataSource.kt
    │   │                               ├── di/
    │   │                               │   ├── DataSourceModule.kt
    │   │                               │   ├── RepositoryModule.kt
    │   │                               │   └── ServiceModule.kt
    │   │                               ├── dto/
    │   │                               │   ├── reponse/
    │   │                               │   │   ├── NicknameResponseDto.kt
    │   │                               │   │   └── UserInfoResponseDto.kt
    │   │                               │   └── request/
    │   │                               │       └── UserProfileRequestDto.kt
    │   │                               ├── model/
    │   │                               │   ├── NicknameValidationResult.kt
    │   │                               │   ├── UserInfoModel.kt
    │   │                               │   └── UserProfileModel.kt
    │   │                               ├── repository/
    │   │                               │   └── UserRepository.kt
    │   │                               ├── repositoryimpl/
    │   │                               │   └── UserRepositoryImpl.kt
    │   │                               └── service/
    │   │                                   └── UserService.kt
    │   └── voca/
    │       ├── build.gradle.kts
    │       └── src/
    │           └── main/
    │               ├── AndroidManifest.xml
    │               └── java/
    │                   └── com/
    │                       └── hilingual/
    │                           └── data/
    │                               └── voca/
    │                                   ├── datasource/
    │                                   │   └── VocaDataSource.kt
    │                                   ├── datasourceimpl/
    │                                   │   └── VocaDataSourceImpl.kt
    │                                   ├── di/
    │                                   │   ├── DataSourceModule.kt
    │                                   │   ├── RepositoryModule.kt
    │                                   │   └── ServiceModule.kt
    │                                   ├── dto/
    │                                   │   └── response/
    │                                   │       ├── VocaDetailResponseDto.kt
    │                                   │       ├── VocaGroupResponseDto.kt
    │                                   │       ├── VocaItemResponseDto.kt
    │                                   │       └── VocaListResponseDto.kt
    │                                   ├── model/
    │                                   │   ├── VocaDetailModel.kt
    │                                   │   └── VocaListModel.kt
    │                                   ├── repository/
    │                                   │   └── VocaRepository.kt
    │                                   ├── repositoryimpl/
    │                                   │   └── VocaRepositoryImpl.kt
    │                                   └── service/
    │                                       └── VocaService.kt
    ├── gradle/
    │   ├── libs.versions.toml
    │   └── wrapper/
    │       └── gradle-wrapper.properties
    ├── presentation/
    │   ├── auth/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           ├── java/
    │   │           │   └── com/
    │   │           │       └── hilingual/
    │   │           │           └── presentation/
    │   │           │               └── auth/
    │   │           │                   ├── AuthScreen.kt
    │   │           │                   ├── AuthViewModel.kt
    │   │           │                   ├── component/
    │   │           │                   │   └── GoogleSignButton.kt
    │   │           │                   └── navigation/
    │   │           │                       └── AuthNavigation.kt
    │   │           └── res/
    │   │               └── drawable/
    │   │                   └── ic_google_20.xml
    │   ├── community/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           └── java/
    │   │               └── com/
    │   │                   └── hilingual/
    │   │                       └── presentation/
    │   │                           └── community/
    │   │                               ├── CommunityNavigation.kt
    │   │                               └── CommunityScreen.kt
    │   ├── diaryfeedback/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           ├── java/
    │   │           │   └── com/
    │   │           │       └── hilingual/
    │   │           │           └── presentation/
    │   │           │               └── diaryfeedback/
    │   │           │                   ├── DiaryFeedbackScreen.kt
    │   │           │                   ├── DiaryFeedbackUiState.kt
    │   │           │                   ├── DiaryFeedbackViewModel.kt
    │   │           │                   ├── ModalImage.kt
    │   │           │                   ├── component/
    │   │           │                   │   ├── DiaryCard.kt
    │   │           │                   │   ├── DiaryViewModeToggle.kt
    │   │           │                   │   ├── FeedbackCard.kt
    │   │           │                   │   ├── FeedbackEmptyCard.kt
    │   │           │                   │   ├── FeedbackReportBottomSheet.kt
    │   │           │                   │   ├── FeedbackReportDialog.kt
    │   │           │                   │   └── RecommendExpressionCard.kt
    │   │           │                   ├── model/
    │   │           │                   │   ├── DiaryContentUiModel.kt
    │   │           │                   │   ├── FeedbackContentUiModel.kt
    │   │           │                   │   └── RecommendExpressionUiModel.kt
    │   │           │                   ├── navigation/
    │   │           │                   │   └── DiaryFeedbackNavigation.kt
    │   │           │                   └── tab/
    │   │           │                       ├── GrammarSpellingScreen.kt
    │   │           │                       └── RecommendExpressionScreen.kt
    │   │           └── res/
    │   │               └── drawable/
    │   │                   ├── ic_feedback_ai.xml
    │   │                   ├── ic_feedback_my.xml
    │   │                   ├── ic_report_24.xml
    │   │                   ├── ic_save_saved_28.xml
    │   │                   └── ic_save_unsaved_28.xml
    │   ├── diarywrite/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           ├── java/
    │   │           │   └── com/
    │   │           │       └── hilingual/
    │   │           │           └── presentation/
    │   │           │               └── diarywrite/
    │   │           │                   ├── DiaryFeedbackStatusScreen.kt
    │   │           │                   ├── DiaryWriteScreen.kt
    │   │           │                   ├── DiaryWriteUiState.kt
    │   │           │                   ├── DiaryWriteViewModel.kt
    │   │           │                   ├── component/
    │   │           │                   │   ├── DiaryFeedbackState.kt
    │   │           │                   │   ├── DiaryWriteCancelDialog.kt
    │   │           │                   │   ├── FeedbackCompleteContent.kt
    │   │           │                   │   ├── FeedbackFailureContent.kt
    │   │           │                   │   ├── FeedbackLoadingContent.kt
    │   │           │                   │   ├── ImageSelectBottomSheet.kt
    │   │           │                   │   ├── PhotoSelectButton.kt
    │   │           │                   │   ├── RecommendedTopicDropdown.kt
    │   │           │                   │   ├── TextScanButton.kt
    │   │           │                   │   └── WriteGuideTooltip.kt
    │   │           │                   └── navigation/
    │   │           │                       └── DiaryWriteNavigation.kt
    │   │           └── res/
    │   │               └── drawable/
    │   │                   ├── ic_arrow_down_20.xml
    │   │                   ├── ic_camera_20.xml
    │   │                   ├── ic_camera_24.xml
    │   │                   ├── ic_change_20.xml
    │   │                   ├── ic_delete_circle_22.xml
    │   │                   ├── ic_error_16.xml
    │   │                   ├── ic_gallary_24.xml
    │   │                   ├── ic_scan_16.xml
    │   │                   └── ic_tooltip_arrow.xml
    │   ├── home/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           ├── java/
    │   │           │   └── com/
    │   │           │       └── hilingual/
    │   │           │           └── presentation/
    │   │           │               └── home/
    │   │           │                   ├── HomeScreen.kt
    │   │           │                   ├── HomeUiState.kt
    │   │           │                   ├── HomeViewModel.kt
    │   │           │                   ├── component/
    │   │           │                   │   ├── HomeHeader.kt
    │   │           │                   │   ├── calendar/
    │   │           │                   │   │   ├── CalendarHeader.kt
    │   │           │                   │   │   ├── DayItem.kt
    │   │           │                   │   │   ├── DaysOfWeekTitle.kt
    │   │           │                   │   │   └── HilingualCalendar.kt
    │   │           │                   │   └── footer/
    │   │           │                   │       ├── DiaryDateInfo.kt
    │   │           │                   │       ├── DiaryEmptyCard.kt
    │   │           │                   │       ├── DiaryPreviewCard.kt
    │   │           │                   │       ├── DiaryTimeInfo.kt
    │   │           │                   │       ├── TodayTopic.kt
    │   │           │                   │       └── WriteDiaryButton.kt
    │   │           │                   ├── model/
    │   │           │                   │   ├── DateUiModel.kt
    │   │           │                   │   ├── DiaryThumbnailUiModel.kt
    │   │           │                   │   ├── TodayTopicUiModel.kt
    │   │           │                   │   └── UserProfileUiModel.kt
    │   │           │                   └── navigation/
    │   │           │                       └── HomeNavigation.kt
    │   │           └── res/
    │   │               └── drawable/
    │   │                   ├── ic_arrow_down_28.xml
    │   │                   ├── ic_arrow_left_28.xml
    │   │                   ├── ic_arrow_right_28.xml
    │   │                   ├── ic_bubble_16.xml
    │   │                   ├── ic_bubble_34.xml
    │   │                   ├── ic_fire_16.xml
    │   │                   ├── ic_plus_16.xml
    │   │                   ├── ic_time_16.xml
    │   │                   ├── ic_today_4.xml
    │   │                   └── ic_transfer_28.xml
    │   ├── main/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           ├── java/
    │   │           │   └── com/
    │   │           │       └── hilingual/
    │   │           │           └── presentation/
    │   │           │               └── main/
    │   │           │                   ├── MainActivity.kt
    │   │           │                   ├── MainScreen.kt
    │   │           │                   ├── MainTab.kt
    │   │           │                   ├── component/
    │   │           │                   │   └── MainBottomBar.kt
    │   │           │                   ├── monitor/
    │   │           │                   │   ├── NetworkMonitor.kt
    │   │           │                   │   ├── NetworkMonitorImpl.kt
    │   │           │                   │   └── NetworkMonitorModule.kt
    │   │           │                   └── state/
    │   │           │                       ├── DialogStateHolder.kt
    │   │           │                       └── MainAppState.kt
    │   │           └── res/
    │   │               └── drawable/
    │   │                   ├── ic_book_24.xml
    │   │                   ├── ic_community_24.xml
    │   │                   ├── ic_home_24.xml
    │   │                   └── ic_my_24.xml
    │   ├── mypage/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           └── java/
    │   │               └── com/
    │   │                   └── hilingual/
    │   │                       └── presentation/
    │   │                           └── mypage/
    │   │                               ├── MypageNavigation.kt
    │   │                               └── MypageScreen.kt
    │   ├── onboarding/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           ├── java/
    │   │           │   └── com/
    │   │           │       └── hilingual/
    │   │           │           └── presentation/
    │   │           │               └── onboarding/
    │   │           │                   ├── OnboardingScreen.kt
    │   │           │                   ├── OnboardingUiState.kt
    │   │           │                   ├── OnboardingViewModel.kt
    │   │           │                   ├── component/
    │   │           │                   │   └── TermsBottomSheet.kt
    │   │           │                   └── navigation/
    │   │           │                       └── OnboardingNavigation.kt
    │   │           └── res/
    │   │               └── drawable/
    │   │                   └── ic_check_circle_28_and.xml
    │   ├── otp/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           └── java/
    │   │               └── com/
    │   │                   └── hilingual/
    │   │                       └── presentation/
    │   │                           └── otp/
    │   │                               ├── OtpScreen.kt
    │   │                               ├── component/
    │   │                               │   └── OtpTextField.kt
    │   │                               └── navigation/
    │   │                                   └── OtpNavigation.kt
    │   ├── splash/
    │   │   ├── build.gradle.kts
    │   │   └── src/
    │   │       └── main/
    │   │           ├── AndroidManifest.xml
    │   │           └── java/
    │   │               └── com/
    │   │                   └── hilingual/
    │   │                       └── presentation/
    │   │                           └── splash/
    │   │                               ├── SplashScreen.kt
    │   │                               ├── SplashViewModel.kt
    │   │                               └── navigation/
    │   │                                   └── SplashNavigation.kt
    │   └── voca/
    │       ├── build.gradle.kts
    │       └── src/
    │           └── main/
    │               ├── AndroidManifest.xml
    │               ├── java/
    │               │   └── com/
    │               │       └── hilingual/
    │               │           └── presentation/
    │               │               └── voca/
    │               │                   ├── VocaScreen.kt
    │               │                   ├── VocaUiState.kt
    │               │                   ├── VocaViewModel.kt
    │               │                   ├── component/
    │               │                   │   ├── AddVocaButton.kt
    │               │                   │   ├── VocaBottomSheet.kt
    │               │                   │   ├── VocaCard.kt
    │               │                   │   ├── VocaDialog.kt
    │               │                   │   ├── VocaEmptyCard.kt
    │               │                   │   ├── VocaHeader.kt
    │               │                   │   └── VocaInfo.kt
    │               │                   └── navigation/
    │               │                       └── VocaNavigation.kt
    │               └── res/
    │                   └── drawable/
    │                       ├── ic_az_24.xml
    │                       ├── ic_check_24.xml
    │                       ├── ic_list_24.xml
    │                       ├── ic_listdown_24.xml
    │                       ├── ic_save_28_empty.xml
    │                       ├── ic_save_28_filled.xml
    │                       ├── ic_save_36_empty.xml
    │                       └── ic_save_36_filled.xml
    ├── spotless/
    │   └── spotless.license.kt
    ├── .github/
    │   ├── CODEOWNERS
    │   ├── pull_request_template.md
    │   ├── ISSUE_TEMPLATE/
    │   │   └── issue_template.md
    │   └── workflows/
    │       └── pr_checker.yml
    └── .run/
        ├── Hilingual [ktLintCheck].run.xml
        └── Hilingual [ktLintFormat].run.xml

```

### **Architecture**

1. **Google Recommended Architecture**: The project follows Google's recommended app architecture (UI Layer - Data Layer). The Domain Layer is not used.
2. **MVVM Pattern**: The architecture is based on the MVVM (Model-View-ViewModel) pattern and aims for a Unidirectional Data Flow (UDF).
3. **Single Activity Architecture**: The app consists of a single `Activity` and multiple Composable `Screen`s.
4. **Unidirectional Dependency**: Module dependencies flow in one direction: **`:presentation` → `:data` → `:core`**.
5. **ViewModel's Role**: The `ViewModel` is responsible for managing the UI State and calling business logic from the Repository.
6. **Repository Pattern**: Access to data sources (Local, Remote) is abstracted and managed using the `Repository` pattern.
7. **DTO and Data Model Separation**: The `Repository` does not return network DTOs directly. It converts DTOs received from the `DataSource` into Data Models for use in the UI Layer.
8. **DI (Dependency Injection)**: Dependency injection is implemented using **Hilt**.
9. **Modularization**: The project is modularized by feature (`:feature`) and layer (`:data`, `:core`) to improve build speed and code reusability.

### **Jetpack Compose & UI**

1. **Separation of Route and Screen**: The navigation graph definition in **`Route`** files (e.g., `AuthNavigation.kt`) is clearly separated from the actual UI implementation in **`Screen`** files (e.g., `AuthScreen.kt`).
2. **Lifecycle-aware State Collection**: When collecting state from a Flow in the UI layer, use **`collectAsStateWithLifecycle`** instead of `collectAsState` to be lifecycle-aware.
3. **State Hoisting**: State should be lifted to the highest possible Composable (**State Hoisting**) to make Composables stateless.
4. **Pure Functions**: Composable functions should be written as **pure functions** without side effects.
5. **Prevent Unnecessary Recomposition**: Use `remember` to prevent the unnecessary recreation of objects during recomposition.
6. **Optimize Derived State**: For state that is calculated based on other state values, use **`derivedStateOf`** for performance optimization.
7. **Side Effect Handling**: For tasks that need to be synchronized with the Composable's lifecycle, use appropriate Effect Handlers like **`LaunchedEffect`** and **`DisposableEffect`**.
8. **Modifier Parameter**: The first parameter of a reusable Composable should always be `modifier: Modifier = Modifier`.
9. **Utilize Lazy Layouts**: For displaying large lists, use **`LazyColumn`** instead of `Column` and **`LazyRow`** instead of `Row` to ensure performance.
10. **Performance Optimization**: Add **`@Stable`** or **`@Immutable`** annotations to data classes representing UI state to reduce unnecessary recompositions.
11. **Active Use of Previews**: Actively use the `@Preview` annotation to check various UI states in advance and speed up development.

### **Kotlin & Coroutines**

1. **StateFlow State Updates**: When updating the state of a `MutableStateFlow` in a `ViewModel`, use the atomic **`.update { ... }`** function instead of `.value =`.
2. **Utilize `suspendRunCatching`**: In the `Repository`, use the provided **`suspendRunCatching`** utility function instead of `try-catch` to return a `Result`. The `ViewModel` then handles the result using `onSuccess` and `onLogFailure` onLogFailure is in core:common.
3. **State Stream**: For delivering continuous state, **`StateFlow`** should be used in preference to `SharedFlow`.
4. **UI Event Handling**: One-time UI events (like toasts, snackbars, etc.) are handled using a `Channel` or `SharedFlow(replay=0)`.
5. **Utilize `viewModelScope`**: Within a `ViewModel`, use **`viewModelScope`** for managing the lifecycle of coroutines.
6. **Prohibit `GlobalScope`**: The use of **`GlobalScope`** is strictly prohibited.
7. **Specify Dispatchers**: When running a coroutine, explicitly use the appropriate dispatcher for the task when necessary: **`Dispatchers.IO`** (network, DB), **`Dispatchers.Default`** (heavy computations), **`Dispatchers.Main`** (UI).
8. **Immutability**: Prefer **`val`** over `var` to promote immutability.
9. **Utilize Data Classes**: Actively use **`data class`** to represent state or models.

### **Android Jetpack Libraries**

1. **Type-Safe Navigation**: Screen navigation is implemented in a type-safe manner using **Compose Navigation** based on **Kotlinx Serialization**.
2. **JSON Parsing**: JSON serialization and deserialization are handled using **Kotlinx Serialization**.
3. **Data Storage**: For simple key-value data, **`DataStore`** is used instead of `SharedPreferences`.
4. **Local DB**: The local database is **Room**. All database access is handled asynchronously via `suspend` functions or `Flow`.
5. **Network Communication**: Network communication is handled using **Retrofit** and **OkHttp**.

### **Build & Dependency Management (Gradle)**

1. **Version Catalog**: Library versions are managed using a **Version Catalog (`libs.versions.toml`)**.
2. **Convention Plugins**: Common build logic is extracted into **Convention Plugins** (in the `build-logic` module) to reduce duplication in `build.gradle.kts` files.
3. **Sensitive Information Management**: All sensitive information, such as API keys, is managed through the **`local.properties`** file and is never committed to Git.
4. **API vs. Implementation**: Dependencies that do not need to be exposed outside a module are declared with **`implementation`** to optimize build times.
5. **Minimal Build Configuration**: Each module's `build.gradle.kts` file should only contain the settings and dependencies essential for that module.

my name is PODO.
dont use never trailing comma
Answer is always to be Korean
---
## 포도님의 개발 스타일 및 선호사항

- **커밋 스타일:** `[태그/#이슈번호] 요약` 형식의 짧은 커밋 메시지를 선호합니다. (예: `[FEAT/#123] 로그인 기능 추가`) 긴 커밋 본문은 사용하지 않습니다.
- **커밋 단위:** 관련된 변경사항을 하나의 논리적 단위로 묶어, 가능한 작은 단위로 커밋하는 것을 선호합니다.
- **아키텍처:** 기존 코드의 패턴을 따르기보다는, 'Now in Android'와 같은 공식 레퍼런스 아키텍처의 원칙을 따르는 것을 선호합니다.
- **코드 가독성:** 함수 호출 시, 파라미터의 순서에 의존하기보다 각 인자의 역할을 명확히 알 수 있는 방법을 선호합니다. (예: 여러 파라미터를 전달하는 대신 `data class`로 래핑하여 명명된 인자 사용)
- **패키지 구조:** 관심사의 분리(SoC) 원칙에 따라, 각 패키지의 역할과 책임이 명확하게 나뉘는 구조를 선호합니다.
- 절대 시발 시발 GEMINI.md 파일 커밋하지도 푸시하지도 마라.
