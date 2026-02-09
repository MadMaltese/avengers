# Requirements Verification Questions

요구사항 정의서를 분석한 결과, 아래 항목들에 대한 명확화가 필요합니다.
각 질문의 [Answer]: 태그 뒤에 선택지 알파벳을 입력해주세요.

---

## Question 1
프로젝트의 기술 스택(프로그래밍 언어 및 프레임워크)은 무엇으로 하시겠습니까?

A) TypeScript + React (Frontend) / TypeScript + Node.js + Express (Backend)
B) TypeScript + React (Frontend) / Java + Spring Boot (Backend)
C) TypeScript + Next.js (Full-stack)
D) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 2
데이터베이스는 어떤 것을 사용하시겠습니까?

A) PostgreSQL (관계형 DB)
B) MySQL (관계형 DB)
C) MongoDB (NoSQL Document DB)
D) DynamoDB (AWS NoSQL)
E) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 3
배포 환경은 어떻게 계획하고 계십니까?

A) AWS 클라우드 (EC2, ECS, Lambda 등)
B) 로컬/온프레미스 서버
C) Docker 컨테이너 기반 (배포 환경 미정)
D) 배포는 나중에 결정, 로컬 개발 환경만 우선 구성
E) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 4
매장(Store)과 관리자(Admin) 계정의 관계는 어떻게 되나요?

A) 1개 매장 = 1개 관리자 계정 (단일 관리자)
B) 1개 매장 = 다수 관리자 계정 (다중 관리자)
C) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 5
메뉴 이미지는 어떻게 관리하시겠습니까? (이미지 리사이징/최적화는 제외 범위이므로 저장 방식만)

A) 외부 이미지 URL 직접 입력 (별도 이미지 호스팅 사용)
B) 서버에 이미지 파일 업로드 후 URL 자동 생성
C) 이미지 없이 텍스트만으로 MVP 진행
D) Other (please describe after [Answer]: tag below)

[Answer]: C

## Question 6
테이블 세션의 자동 만료 정책이 필요합니까? (예: 고객이 주문 후 관리자가 이용 완료 처리를 잊은 경우)

A) 자동 만료 불필요 - 관리자가 수동으로만 세션 종료
B) 일정 시간(예: 4시간) 후 자동 만료
C) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 7
동시 접속 규모는 어느 정도를 예상하십니까? (성능 설계 기준)

A) 소규모 - 단일 매장 (테이블 20개 이하)
B) 중규모 - 소수 매장 (2~5개 매장, 총 테이블 100개 이하)
C) 대규모 - 다수 매장 (10개 이상 매장)
D) MVP는 단일 매장 기준으로 시작, 추후 확장
E) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 8
주문 상태 실시간 업데이트(고객 화면)는 MVP에 포함하시겠습니까? (요구사항에 "선택사항"으로 표기됨)

A) MVP에 포함 - SSE로 고객 화면에서도 주문 상태 실시간 반영
B) MVP에서 제외 - 고객은 페이지 새로고침으로 상태 확인
C) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 9
메뉴 관리 기능은 MVP에 포함하시겠습니까? (요구사항 3.2.4에 정의되어 있으나 MVP 범위 섹션에 명시적으로 언급되지 않음)

A) MVP에 포함 - 관리자가 메뉴 CRUD 가능
B) MVP에서 제외 - 초기 메뉴 데이터는 DB에 직접 입력
C) Other (please describe after [Answer]: tag below)

[Answer]: A
