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
- [배포-과정정](#배포-과정)
- [문제 해결](#문제-해결)
- [고민 사항](#고민-사항)
- [향후 계획](#향후-계획)

## 개요
"(스스로) 문제를 내고 스스로 푼다, Moon-Spoon 입니다."  

스스로 문제를 내고 문제를 풀 수 있는 서비스를 제공합니다.   

주어진 문제 형식과 다른 방식으로 테스트를 하고자 하는 사람에게 도움이 될 수 있습니다.  

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


## ERD 구성

![image](https://github.com/hamlsy/Moon-Spoon/assets/70877744/7f577b28-2fa1-4143-a617-e992d9f56694)

## API 문서
api 문서 링크 ..

## 주요 기능

1. 문제집 등록
  - 
  - 
3. 문제 등록
4. 테스트 방식 정의
5. 테스트
6. 테스트 결과 및 제출

## 배포 과정


## 문제 해결
### 문제 1: [ n+1 문제 ] [#36](https://github.com/hamlsy/Moon-Spoon/pull/36#issue-2384029208)
- 개요: 
- 원인: []
- 해결과정:
  1. Fetch join
  2. Batch size
- 학습 내용: [얻은 인사이트 ..

### 문제 2: [ 엔티티 다수 업데이트 성능 문제 ]
- 개요: 각 문제들의 정답 횟수를 업데이트하는 과정에서 발생한 성능 문제
- 원인: []
- 해결과정:
  1. 벌크연산
  2. ...
- 학습 내용: [얻은 인사이트 ..
 
### 문제 3: [ 회원가입 동시성 문제 ]
- 개요:
- 원인: []
- 해결과정:
  1. Unique Key
  2. Synchronized
- 학습 내용: [얻은 인사이트 ..

### 문제 4: [조회 성능 문제]
- 개요:
- 원인: []
- 해결과정:
  1. Unique Key
  2. Synchronized
- 학습 내용: [얻은 인사이트 ..

## 고민 사항
### 고민 1: [ DTO의 변환 위치 ]
- 개요:


## 향후 계획
