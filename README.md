# 수강신청 Demo Application
<p align="center">
<img src="https://github.com/user-attachments/assets/2e946bb3-c49d-4bd0-af5c-12d3405e62ef" alt="로고" width="50%" height="50%">
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
<img src="https://img.shields.io/badge/SpringSecurity-336F19?style=for-the-badge&logo=SpringSecurity&logoColor=white">
<img src="https://img.shields.io/badge/JwtToken-5a999f?style=for-the-badge&logo=JwtToken&logoColor=white">

## 📈 ERD
<p align="center">
<img width="1654" height="534" alt="Image" src="https://github.com/user-attachments/assets/79a5bbf4-c136-4c69-aaef-73dc4da84bfe" />

## 연관관계 엔티티
- 처음 JPA를 쓰다보니, 개발 생산성을 위해 연관관계 다이어그램도 필요하다고 생각했습니다.
<img width="60%" height="60%" alt="image" src="https://github.com/user-attachments/assets/4969343a-629f-4f73-8db2-bdfde12b2a37" />


## 🔐 JWT 로그인 아키텍처
- 자세한 로그인 구현은 아래 참조
- https://xuv2.notion.site/Login-Auth-2946d43c39778066b686e187dc9288f7?source=copy_link
<img width="60%" height="60%" alt="image" src="https://github.com/user-attachments/assets/08d0fce8-8cbc-4940-b07f-2be4f57cf2d9" />


## View 및 기능
<table>
  <tr>
    <td><img width="2598" height="1426" alt="image" src="https://github.com/user-attachments/assets/054d42d2-9c16-47ef-bea3-ebe2e0ef54bf" /></td>
    <td><img width="2502" height="1614" alt="image" src="https://github.com/user-attachments/assets/9eb6bf26-f929-47bf-a935-b641d08ae608" /></td>
  </tr>
  <tr>
    <td><img width="2748" height="1672" alt="image" src="https://github.com/user-attachments/assets/3277864b-002b-4831-9239-c281d434cd38" /></td>
    <td><img width="2750" height="1216" alt="image" src="https://github.com/user-attachments/assets/04a06d06-ed86-4961-9d91-6912a1e7e729" /></td>
  </tr>
</table>
<img width="979" height="232" alt="image" src="https://github.com/user-attachments/assets/ebe95b76-9c7f-471b-b0d5-793837dfb018" />



## 🔧 문제 및 개선 사항

### 1) 예비 수강신청 후 일괄 신청시 DeadLock 문제 발생
- 원인 : 예비 수강신청 일관 신청시 다수의 트랜잭션이 충돌하며 DeadLock 문제가 발생했습니다.
- 증상 : 예를 들어 사용자1(트랜잭션1)이 (A과목, B과목)을 신청하고, 사용자2(트랜잭션2)가 (B과목, A과목)을 신청하는 과정에서, 각각 1순위 과목을 신청하고 2순위 과목을 신청하면서 서로의 베타락을 기다리는 문제가 발생했습니다(DeadLock).
- 결과 : 사용자마다 예비 수강 장바구니에 담는 순서가 달라 문제가 발생하였으므로, 비즈니스 로직 진입 시점에 장바구니의 모든 강의들을 courses의 PK 오름차순으로 정렬하여 순차적으로 락을 획득하도록 강제하였습니다.
- 이로써 순환 대기가 물리적으로 차단되어 71% 의 트랜잭션 롤백률이 0% 로 감소하였고, 사용자에겐 오류 화면 대신 대기를 제공함으로써 사용자 경험을 개선할 수 있게 되었습니다.

### 2) Stateless 아키텍처 도입을 통한 서버 확장성 확보(Session -> JWT)
- 배경 : 개발 초기에는 SSR 방식에서 보편적이고 보안성이 높은 Session - Cookie 방식을 채택하였지만, 수강신청 시작 시점에 수천명의 트래픽이 몰리는 상황을 가정하였습니다.
- 증상 : 결국 Session 방식은 별도의 로그인 DB를 관리해야하기 때문에, 세션 DB에서 I/O 부하가 발생하였습니다.
- 단순 Scale-up + DB 커넥션을 늘리기를 통해 해결할 수 있고, 금전적인 한계가 있다면 Scale-out 방식을 채택하여 데이터베이스 샤딩, 세션 클러스터링 관리등을 통하여 확장을 고려할 수 있지만, 인프라 관리 복잡도가 많이 높아질 것으로 예상되었습니다.
- 해결 : 서버가 상태를 저장하지 않는 stateless 한 JWT 방식을 채택하여 세션 DB가 부담하던 비용을 제거하고, 별도의 저장소 없이도 인가 처리가 가능하도록 구현하여 DB 비용을 절감할 수 있게 되었습니다.
- 앞으로 개선할 점:
- Access Token의 탈취 위험을 줄이기 위한 대표적인 방식으로 Refresh Token 방식이 존재하는데, 만약 이 Refresh Token 자체가 탈취 당하게 되면, 해커가 지속적으로 Access Token을 재발급 받을 수 있는 보안 위협 존재 -> 어떻게 해결할 수 있는지 생각해보기.

### 3) 수동 배포의 비효율성 개선 및 무중단 배포 파이프라인 구축 (배포만 한다고 끝이 아니다)
- 증상 : 로컬 빌드 후  Jar 파일을 업로드(도커 이미징)하고 서버를 재시작하는 수동 배포 환경이 반복되었습니다. 인프라를 A-Z 까지 설정하기엔 실수 가능성이 높고 많은 시간이 소요 될 가능성이 높았습니다.
- 해결 : IaC의 대표적인 방식인 Terraform을 이용하여 AWS 인프라를 코드로 정의하여 배포 환경을 자동화 하였습니다.
- Github Actions를 통해 IDE에서 레포지토리에 Push 하게 되면 아래와 같은 작업이 자동으로 수행 되도록 파이프라인을 구현하였습니다.
- [변경 사항 Push → CI(빌드 시작) → ECR에 이미지 빌드 및 컨테이너화 → CD(SSH 접속 없이 SSM을 통해 EC2 인스턴스 명령전송)→ EC2 의 기존 애플리케이션 종료 및 삭제 → Secret을 애플리케이션에 주입후 컨테이너 실행]
- 결과 : 이를 통해 배포 소요시간이 크게 감축된 것에 이어, 실행 환경과 개발 환경의 운영 환경 일치를 보장할 수 있게 되었습니다.

## (배포 주변 반응 이모저모)
<table>
  <td><img width="603" height="666" alt="image" src="https://github.com/user-attachments/assets/33a51ea1-62a4-4108-9012-cb5e71b4a5b3" /></td>
  <td><img width="510" height="669" alt="image" src="https://github.com/user-attachments/assets/b04b24a4-ca9c-4a5d-a1fe-3d48d6eb1ad5" /></td>
  <td><img width="531" height="666" alt="image" src="https://github.com/user-attachments/assets/a3475018-cb56-4e3e-a25e-cb008ea0abdb" /></td>
</table>
