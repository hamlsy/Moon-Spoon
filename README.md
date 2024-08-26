# Moon-Spoon 프로젝트 제작

## 목차
- [개요](#개요)
- [개발 목적](#개발-목적)
- [개발 기간](#개발-기간)
- [기술 스택](#기술-스택)
- [주요 기능](#주요-기능)
- [화면 구성](#화면-구성)
- [ERD 구성](#erd-구성)
- [API 문서](#api-문서)
- [배포-과정](#배포-과정)
- [문제 해결](#문제-해결)
- [고민 사항](#고민-사항)
- [향후 계획](#향후-계획)

## 개요
"문제를 만들고 스스로 푼다, Moon-Spoon 입니다."  

스스로 문제를 내고 문제를 풀 수 있는 서비스를 제공합니다.   

주어진 문제 형식과 다른 방식으로 테스트를 하고자 하는 사람에게 도움이 될 수 있습니다.  

## Moon-Spoon 서비스
https://www.moonspoon.site

## 개발 목적

### "Moon-Spoon" 서비스는 개인적인 경험에 의해 개발되었습니다.   
저는 대학교 수강 과목의 암기 파트나 서술형 파트를 공부할 때 백지 공부법을 자주 하는 편입니다.  
하지만 종이에 적거나, 메모장에 반복적으로 적는 것에 불편함을 느꼈습니다.  
'마치 실제 시험처럼 내가 문제를 만들어 스스로 테스트할 수 있으면 얼마나 좋을까?' 라는 생각이 들기 시작했습니다.   
제 주변 지인도 저와 같은 불편함을 겪고 있었다는 사실을 알았습니다.  
그래서 저와 지인, 그리고 다른 사용자들의 불편함도 해소하기 위해 이 프로젝트를 개발하게 되었습니다.   

이 서비스의 의의는 사용자가 스스로 문제를 만들고 테스트하여 암기 부문 학습에 도움을 줄 수 있도록 지원하는 것입니다.  
이를 통해 사용자는 학습 효율을 높이고 자신만의 학습 문제집을 만들어 더 효과적으로 공부할 수 있습니다.  

앞서 언급했던 암기 과목에 유용할 뿐만 아니라, 면접 준비에도 큰 도움이 될 수 있습니다.   
사용자가 면접관의 입장이 되어 어떤 문제를 낼지 고민하고, 그 문제에 대한 답을 정리하여 스스로 테스트할 수 있다는 장점이 있습니다.  

필요에 의해 시작한 개발인 만큼, 다양한 의견과 피드백을 수용하여 발전시킬 계획입니다. 

## 개발 기간
2024.06.22 ~

## 기술스택


### Front-end
<img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white">
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">

### Back-end

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">

### Database



## 화면 구성

- 로그인
- 로그아웃
- 메인화면
- 내 문제집
- 문제집 상세
- 테스트
- 채점
- 공유 문제집
- 공유 문제집 상세


## ERD 구성
![problem_workspace](https://github.com/user-attachments/assets/c2c4b393-7e11-4b24-98a9-ea3243f55d3a)

## API 문서
api 문서 링크 ..

## 주요 기능

1. 문제집 기능
  - 문제집을 등록하여 문제들을 분류하여 관리할 수 있습니다.
  - 수정 및 삭제가 가능하며, 문제집을 공유하면 다른 이용자들도 열람 및 테스트가 가능합니다.
2. 문제 기능
  - 문제와 정답을 직접 입력할 수 있습니다.
  - 수정 및 삭제 기능과 정답률이 표시됩니다.
3. 테스트 방식 정의
  - 랜덤, 비랜덤, 출제 순서, 출제 수를 선택할 수 있습니다.
  - 만약 (랜덤 && 정답률 낮은 순) 설정이면 정답률 낮은 순으로 출제 수만큼 추출하고, 순서를 랜덤으로 조정합니다.
4. 테스트
  - 설정 값에 따라 문제들이 출제됩니다. 정답은 서술형으로 입력하며, 입력한 문항은 푸른색으로 표시, 입력하지 않은 문항은 빨간색으로 표시됩니다.
  - 이전, 다음 버튼이나 문항을 직접 클릭하는 것으로 문제 이동이 가능합니다.
5. 테스트 결과 및 제출
  - 테스트를 제출하면 문자열을 비교하여 정답과 오답 여부를 판단합니다.
  - 서술형 특성상 문자열이 달라도 의미가 같다고 생각되는 경우, 직접 채점하여 정답을 판단할 수 있습니다.
  - 제출 후 맞은 문항, 틀린 문항, 점수가 출력되며 각 문제에 대한 정답률에 결과가 반영됩니다.

## 배포 과정

### Vue
S3

### Spring
Docker, Nginx, EC2

### 배포
Github Action, CodeDeploy

### 도메인 연결
Route53, 가비아

### Https 인증


## 문제 해결
### 문제 1: [ n+1 문제 ] [#36](https://github.com/hamlsy/Moon-Spoon/pull/36#issue-2384029208)
- 개요: 
- 원인: []
- 해결과정:
  1. Fetch join
  2. Batch size
- 학습 내용: Fetch join 개념, Fetch Lazy 원리, 프록시

### 문제 2: [ 엔티티 다수 업데이트 성능 문제 ] [#38](https://github.com/hamlsy/Moon-Spoon/pull/38#issue-2387878580)
- 개요: 각 문제들의 정답 횟수를 업데이트하는 과정에서 발생한 성능 문제
- 원인: [ 반복된 Update 쿼리 ]
- 해결과정:
  1. 반복 Update 쿼리
  2. 벌크연산
- 학습 내용: 

### 문제 3: [ 동시 계정 회원가입 문제 ] [#45](https://github.com/hamlsy/Moon-Spoon/issues/45#issuecomment-2212428574)
- 개요: 회원가입 버튼을 연속으로 클릭 시 동일한 정보의 회원이 여러번 등록되는 문제
- 원인: [ 회원가입 동시성을 고려하지 않음 ]
- 해결과정:
  1. Synchronized
  2. Unique Key
- 학습 내용: 

### 문제 4: [조회 성능 문제] [#67] (https://github.com/hamlsy/Moon-Spoon/issues/67#issue-2482470774)
- 개요: 리스트 페이지 조회 시 성능 저하 발생
- 원인: [ 과도한 fetch join ]
- 해결과정:
  1. 
- 학습 내용:
  - 끌어오는 데이터의 수가 적을 경우 fetch join 대신 따로 쿼리 사용
  - FULL TEXT가 Like에 비해 항상 우세한 건 아님
  - FULL TEXT는 JPQL에서 해결 X Native Query로 해결해야함
  - TEXT 데이터 타입은 인덱스 설정 안됨
  

## 고민 사항
### 고민 1: [ DTO의 변환 위치 ]
- 개요: 
- 방법:
  1. Service 계층에서 변환
  2. Controller 계층에서 변환
     
- 결론:

### 고민 2: [ 방문자 수 처리 ]
- 개요: 본 사이트의 방문자 수를 관리하기 위해 방문자 수 처리 설계를 해야합니다.
  사이트의 전체, 오늘 방문자 수 (회원, 비회원) , 단일 회원의 전체 방문 수를 카운트 해야 하는데, 
  어떤 방식이 가장 효율적인지에 대한 고민하게 되었습니다.
  제가 생각한 방법은 이렇습니다.
  
- 방문자 관리 방법:
  1. User 엔티티에 visit 데이터 필드 정의 후 방문시 업데이트
    - 1번의 경우 가장 단순하고 직관적인 방식이나 사이트의 비회원 방문 통계는 관리할 수 없다는 단점이 있습니다.
      
  2. 별도의 방문 엔티티를 만들어 방문 마다 방문 엔티티 생성
    - 한번에 많인 회원들이 몰릴 경우 다수 엔티티의 생성으로 DB에 부하가 갈 수 있습니다.
      
  3. 방문 엔티티에 방문 수, 최초 방문 데이터를 기록 후 한 엔티티 단위로 관리
    - 전체적인 방문 통계를 파악, 관리하기 용이하고 User 엔티티와 연관관계를 매핑하여 단일 User의 방문 수도 조회할 수 있다는 장점이 있습니다.

  4. Redis를 사용하여 주기적으로 DB에 동기화
    - 
    
- 방문 카운트 로직:
  1. 해당 날짜의 첫 로그인만 카운트
  2. 모든 방문마다 카운트

- 방문 카운트 구현 방법:
  1. session 방식
  2. 
  
- 결론:

### 고민 3: [ 캐싱 전략 선택 ] (#67) (https://github.com/hamlsy/Moon-Spoon/issues/67#issuecomment-2308669925)
- 개요: 캐싱 종류 선택과 전략 고민
- 방법:
- 캐싱 종류:
  1. 로컬 캐시
  2. 분산 캐시
  3. 웹 캐시
- 캐싱 전략:
  1. 읽기 중심 캐시 전략 - 삽입, 업데이트 시 캐시 초기화, 전체 조회시 Cacheable 하는 
  2. 세밀한 캐시 관리 전략 - 삽입, 업데이트 시 개별 캐시 Put, 전체 조회를 별도 엔티티의 캐시 키로 관리
     
- 결론:
  단일 인스턴스 환경이므로 로컬 캐시를 사용합니다. 하지만 분산 환경 대비를 위해 Redis 사용도 추후 구현해보도록 하겠습니다.
  캐싱 전략은 읽기 중심 전략을 선택했습니다. 개별 항목 별로 캐시를 만들지 않아 메모리 관리에 용이하고 대량의 데이터 관리에 효과적이라고 판단하여 읽기 중심 전략을 사용합니다.
  

## 향후 계획


