# 수강신청 Demo Application
<p align="center">
<img src="" alt="로고" width="30%" height="30%">
</p>

# 개발 기간
> 2025.10 ~ 2025.10 (배포 종료)

## 참여 인원
|<img src="https://avatars.githubusercontent.com/u/104479096?v=4" width="150" height="150"/>|
|:-:|
|xub2<br/>[@xub2](https://github.com/xub2)|

## 프로젝트 소개

- Java 코드만으로는 동시성을 제어할 수 없기 때문에, DB 레벨에서 적절한 락 방식(비관적, 낙관적)을 테스트하고, 해당 락 방식을 구현할 구현 방법(공유락, 베탁락 ,Gap락 등 )을 비교하기 위해 시작하였습니다.
- 실제 프론트 페이지까지 있는 웹 애플리케이션을 A-Z 까지 완성해보는 것이 목표였으며, AWS + Github actions 를 통해 무중단 배포 환경을 구축하였습니다.


# 개발 목적

**동시성 문제를 해결할 수 있는 가장 기본적인 시나리오**

- 수강신청이란 상황은 학부생들을 대상으로 한 서비스이기 때문에, 추가되는 인원 없이 기존에 지정한 인원들에 대해서만 빠르고 쉽게 구현이 가능했습니다.
- 정확하게 지점한 시점(수강신청 오픈)을 측정하여, 해당 시점에 부하를 테스트 할 수 있어 객관인 수치로 성과를 분석하기 용이할 것이라고 판단했습니다.
- Thymeleaf 와 같은 템플릿 엔진을 통해 SSR 방식으로 애플리케이션 전체 사이클을 스스로 기획 및 구현하여 전체적인 웹 애플리케이션의 데이터 흐름을 분석하고 학습하기 위해 개발하였습니다.
- AWS 를 통해 실제 프로젝트를 배포해보고, 복잡한 인프라 환경설정 대신 코드로써 인프라를 관리하여 배포 효율성을 극대화 하고자 하였습니다.

## 📚 Tech Stack

<img src="https://img.shields.io/badge/Java-3578E5?style=for-the-badge&logo=Java&logoColor=white"> <img src="https://img.shields.io/badge/SpringBoot-336F19?style=for-the-badge&logo=Springboot&logoColor=white"> 
<img src="https://img.shields.io/badge/Spring-336F19?style=for-the-badge&logo=Spring&logoColor=white"> 
<img src="https://img.shields.io/badge/MySQL-A2A99F?style=for-the-badge&logo=mySQL&logoColor=white"> 
<img src="https://img.shields.io/badge/Hibernate-5a999f?style=for-the-badge&logo=Hibernate&logoColor=white">
<img src="https://img.shields.io/badge/Docker-3578E5?style=for-the-badge&logo=Docker&logoColor=white">
<img src="https://img.shields.io/badge/Redis-fc2003?style=for-the-badge&logo=Redis&logoColor=white">

## 📈ERD
<p align="center">
<img width="1654" height="534" alt="Image" src="https://github.com/user-attachments/assets/79a5bbf4-c136-4c69-aaef-73dc4da84bfe" />

## 🔧개선 사항

### 1) 인덱스 없이 Full Text Scan 문제 발생
- 증상 : 티켓 및 예약 조회 API의 평균 응답 속도가 70ms 이상 소요되며, 동시 접속 시 DB CPU 부하 발생하였습니다.
- 원인 : where 조건절과 order by 절에 포함된 컬럼들에 적절한 인덱스의 부재 -> Full Text Scan 및 FileSort 가 발생하였습니다.
- 결과 : 카디널리티를 고려하여 최적의 복합인덱스(커버링 인덱스)를 설계하고 적용하여 평균 조회 응답시간 약 71% 단축 + Full Text Scan 및 FileSort 를 제거하였습니다.

### 2) 인덱스 오버헤드로 인한 응답 지연 분석 및 최적화 (쿼리 플랜의 Rows 가 줄어든다고 무조건 성능 좋은게 아님)
- 증상 : 특정 이벤트의 티켓 목록을 조회할 때, 쿼리 최적화를 위해 복합 인덱스(event_id, status)를 추가하였으나 Query Explain 상 조회되는 Row 수는
9개 → 5개로 줄어들었으나, 실제 API 응답 시간은 오히려 미세하게 증가하는 현상이 발생하였습니다.
- 원인 : 기존 테스트 데이터 표본의 개수가 이미 FK 인덱스를 통해 충분한 속도를 확보 하고 있었습니다. 그러나 복합 인덱스 추가로 b-tree의 구조가 깊어졌고, 결과적으로 복잡한 인덱스 구조를 탐색 하는 비용이 적은 데이터를 메모리에서 필터링하는 비용보다 높게 측정 되었습니다.
- 결과 : 해당 엔티티를 조회하는 쿼리는 소량의 데이터를 빈번하게 조회하는 패턴임을 파악하였고, 무리한 인덱스 적용을 철회하여 기존의 FK 인덱스 전략을 유지하는것으로 리팩토링하여 쓰기 성능 저하를 방지하였습니다.

### 3) 트랜잭션 범위 최적화 및 TPS 개선
- 증상 1 : 외부 API가 서비스에 엮여 돌아갈 때, 장애 발생 시 예약 데이터까지 롤백되어 데이터 정합성 문제가 발생하였습니다
- 증상 2 : 트랜잭션이 길어지는 만큼 DB 커넥션 점유 시간이 길어져 기대치 보다 낮은 TPS가 측정 되었습니다. (349 TPS)

- 원인 1 : DB 트랜잭션 내부에 외부 API 호출이 포함되어 있어, DB 커넥션 점유 시간이 불필요하게 길어졌습니다. (네트워크를 타는 작업은 항상 오류 가능성 높음)
- 원인 2 : 강한 결합으로 인해 부가 기능의 실패가 메인 로직에 영향을 주고 있었습니다.

- 결과 : DB 작업은 트랜잭션 내부에서, 외부 API는 트랜잭션 외부에서 호출하도록 범위를 조정하여 부가 기능이 메인 로직에 영향을 주지 않도록 방지 하였고, Spring의 EventPublisher을 통해 부가 로직 (User Notification 등)은 비동기적으로 분리하였습니다
- 트랜잭션의 평균 수행 시간이 2.86ms -> 1.78ms 로 개선되었고 TPS가 349.6 -> 516.8 로 약 61% 향상 되었습니다.  
