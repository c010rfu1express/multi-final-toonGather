
# multi-final-toonGather
![spring](https://img.shields.io/badge/spring-c0ff96?style=for-the-badge&logo=spring)
![gradle](https://img.shields.io/badge/gradle-01303a?style=for-the-badge&logo=gradle)
![java](https://img.shields.io/badge/java-5582a1?style=for-the-badge&logo=java)
![MySQL](https://img.shields.io/badge/mysql-f29111?style=for-the-badge&logo=mysql)


## 멀티잇 백엔드 개발 25회차 파이널 프로젝트 [툰게더 toonGather]
'toonGather'는 'toon'(웹툰)과 'gather'(모으다)의 결합으로, 웹툰 문화의 모든 요소를 한데 모아 새로운 가치를 창출하는 플랫폼으로 기획했습니다.

## 💫 팀 소개
팀 툰게더는 소비자 및 창작자 모두를 위한 웹툰 통합 플랫폼을 지향합니다.
- 팀장 및 프로젝트 매니징 총괄
  - 최서윤
- 팀원
  - 박재형 
  - 박서영
  - 김동현
  - 김희철
  - 강현구

## ⚙️ 기술환경
작성 중...

## ⚡️ Git flow
팀 툰게더는 Git flow Workflow를 적용하여, Master/Develop/Feature Branch로 구분한 후 개발에 착수하였습니다.
- Master Branch : 프로덕트로 출시될 수 있는 브랜치로, 이곳에서 배포 가능한 상태만을 관리하도록 합니다.
- Develop Branch : 배포 가능한 안정적인 상태의 브랜치로, Master Branch로 merged 이전 상태이기도 합니다.
- Feature Branch : 기능 단위로 분류한 브랜치로, 실질적 개발작업이 이루어지는 브랜치입니다. 아래는 담당 feature에 대한 Feature Branch의 목록입니다.
  - test_webtoon : 박재형님 담당(webtoon)
  - test_social_new : 최서윤님 담당(social)
  - test_recruit : 박서영님 담당(recruit)
  - test_introduction : 김동현님 담당(introduction)
  - test_cs : 김희철님 담당(cs)
  - test_user : 강현구님 담당(user)


## 🪙 프로젝트 컨벤션
팀 툰게더는 프로젝트 기간동안 다음의 프로젝트 컨벤션을 준수하여 개발하였습니다.
- 데일리 스크럼 : 매일 오전 9시
  - 팀원 모두가 각자의 개발 진행상황을 공유합니다.
  - feature 단위로 통합된 브랜치(develop)로의 merging을 위한 pull request를 실시합니다.
- 커밋 메시지 태그
  - project ::  프로젝트를 세팅
  - build ::  시스템 또는 외부 종속 파일에 영향을 미치는 설정을 변경
  - docs ::  프로젝트 관련 문서 등을 추가/수정 (README.md 등)
  - feat ::  새로운 기능 추가
  - chore ::  build 관련, 패키지 설정 등 자잘한 작업 수행
  - fix ::  기존 프로젝트의 버그 수정
  - delete ::  파일 등을 삭제
  - revert ::  커밋을 롤백
  - merge ::  브랜치를 main 브랜치에 병합
- 커밋/머징시 메시지 예시
  - [최서윤-240723-01] feat :: 로그인 기능 구현
  - [김희철-240724] merge :: 수정 사항 머지


## ✨ 협업 관리
팀 툰게더는 프로젝트 기간동안 다양한 협업 툴을 활용, 보다 효율적인 개발을 위해 지속적으로 고민하였습니다.
- Discord와 Notion을 활용하여 팀 툰게더의 전체적인 협업 상황을 한눈에 볼 수 있도록 하였습니다.
- Google Docs를 활용하여 매일 진행되는 회의 내용과 TO-DO-LIST를 실시간으로 확인할 수 있도록 하였습니다.

## ⚓️ API Docs
팀 툰게더 프로젝트에 활용된 API 문서 목록입니다.
- 웹툰 통합 API 
  - https://korea-webtoon-api-cc7dda2f0d77.herokuapp.com/webtoons
  - https://korea-webtoon-api-cc7dda2f0d77.herokuapp.com/api-docs/#/Health%20status/get_health_check
- 카카오 웹툰 API
  - https://gateway-kw.kakao.com/section/v2/timetables/days?placement=timetable_mon
- 네이버 웹툰 API
  - https://comic.naver.com/api/webtoon/titlelist/weekday?order=user
- 카카오 맵 API
  - https://apis.map.kakao.com/
- 네이버 CLOVA OCR API
  - https://www.ncloud.com/product/aiService/ocr
- 네이버 CLOVA Chatbot API
  - https://www.ncloud.com/product/aiService/chatbot
- PortOne 포트원 (아임포트) API
  - https://portone.io/korea/ko
- 카카오/네이버 로그인 API
  - https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api
  - https://developers.naver.com/products/login/api/api.md
