## Release {{VERSION}} 🚀

| 항목 | 값 |
| --- | --- |
| versionName | `{{VERSION}}` |
| versionCode | `{{CODE}}` |

## 릴리즈 체크리스트 ✅
- [ ] `./gradlew spotlessApply` 실행 및 커밋
- [ ] `./gradlew ktlintCheck` 통과
- [ ] 베이스라인 프로파일 재생성 및 커밋 (`baseline-prof.txt`, `startup-prof.txt`)
- [ ] 패치노트 작성 (`fastlane/metadata/android/ko-KR/changelogs/default.txt`)
- [ ] Firebase QA 빌드 배포 및 QA 통과
- [ ] PR CI(lint / build) 통과

## 이번 릴리즈 내용 ✏️
- 내용

## QA 결과 🔍
- 결과 요약 / 남은 이슈

## 머지 방법 ⚠️
- **반드시 `Create a merge commit` 으로 머지한다.** squash 로 머지하면 main 과 develop 의 히스토리가 갈라져 다음 릴리즈에서 충돌한다.
- 머지 후 `Deploy to Play Store` → `Post Release` 워크플로가 순서대로 돌면서 Play Store 업로드, 태그/릴리즈 생성, develop 동기화까지 자동으로 처리된다.
- Play Store 는 `draft` 로 올라가므로 Play Console 에서 수동으로 출시해야 한다.
