## Release {{VERSION}} 🚀

| 항목 | 값 |
| --- | --- |
| versionName | `{{VERSION}}` |
| versionCode | `{{CODE}}` |

## 릴리즈 체크리스트 ✅
- [ ] `./gradlew spotlessApply` 실행 및 커밋
- [ ] `./gradlew ktlintCheck` 통과
- [ ] 베이스라인 프로파일 재생성 및 커밋 (`baseline-prof.txt`, `startup-prof.txt`)
- [ ] 패치노트 작성 (`fastlane/metadata/android/ko-KR/changelogs/default.txt`) — 비어 있으면 배포가 중단된다
- [ ] Firebase QA 빌드 배포 및 QA 통과
- [ ] PR CI(lint / build) 통과

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
