## Release {{VERSION}} 🚀

| 항목 | 값 |
| --- | --- |
| versionName | `{{VERSION}}` |
| versionCode | `{{CODE}}` |

## 릴리즈 체크리스트 ✅
- [ ] `./gradlew spotlessApply` 실행 및 커밋
- [ ] `./gradlew ktlintCheck` 통과
- [ ] 베이스라인 프로파일 재생성 및 커밋 (`app/src/release/generated/baselineProfiles/`)
- [ ] Firebase QA 빌드 배포 및 QA 통과
- [ ] 패치노트 작성 (`fastlane/metadata/android/ko-KR/changelogs/default.txt`) — 비어 있으면 배포가 중단된다
- [ ] PR CI(lint / build / release build) 통과

순서는 권장일 뿐 강제되지 않는다. 패치노트는 머지 시점까지만 채워져 있으면 되므로, 릴리즈 노트를 QA 이후에 받는다면 나중에 커밋해도 된다.

## 이번 릴리즈 내용 ✏️
- 내용

## QA 결과 🔍
- 결과 요약 / 남은 이슈

## 머지하면 일어나는 일 ⚠️
이 PR 을 머지하면 `Release Publish` 워크플로가 아래를 순서대로 자동 처리한다.

1. **사전 검증** — main 조상 관계, 버전, 태그 중복, 패치노트 존재 여부
2. **Play Store 업로드** — 이 PR 의 머지 커밋으로 AAB 를 빌드해 `draft` 로 업로드
3. **main 승격** — 업로드가 성공한 뒤에만 main 을 이 커밋으로 fast-forward
4. **태그 / GitHub Release 생성** — `v{{VERSION}}`

main 은 배포가 성공한 뒤에만 움직이므로, 배포가 실패하면 main 은 그대로 남는다.
업로드는 `draft` 상태이므로 **Play Console 에서 수동으로 출시**해야 최종 반영된다.
