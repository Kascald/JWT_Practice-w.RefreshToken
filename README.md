# JWT Authentication Project with Refresh Token

이 프로젝트는 JWT (JSON Web Token) 인증을 구현한 예제 프로젝트로, Refresh Token을 사용하여 사용자 세션을 안전하게 유지하는 기능을 포함하고 있습니다.

## 소개
이 프로젝트는 JWT를 사용한 간단한 인증 시스템을 구현하였으며, Refresh Token을 사용하여 Access Token을 갱신하는 기능을 포함하고 있습니다. Access Token은 보안상의 이유로 짧은 수명을 가지며, Refresh Token은 사용자가 다시 인증하지 않고도 새로운 Access Token을 받을 수 있도록 합니다.

## 주요 기능
- 사용자 등록 및 로그인
- JWT Access Token 발급
- Refresh Token 발급 및 갱신
- 토큰 검증 및 사용자 인증
- 토큰의 안전한 저장 및 관리

## 사전 준비
프로젝트를 시작하기 전에 다음 사항을 확인하세요. 이 프로젝트는 JDK 17, MySQL을 사용하였습니다.:
- JDK 17 이상 설치
- MySQL 데이터베이스 설치 및 실행

## 설치
1. 리포지토리 클론:
    ```bash
    git clone https://github.com/Kascald/JWT_Practice-w.RefreshToken.git
    cd JWT_Practice-w.RefreshToken
    ```

2. MySQL 데이터베이스 설정:
   - MySQL 서버를 설치하고 실행합니다.
   - MySQL에 `preon` 데이터베이스를 생성합니다.
     ```sql
     CREATE DATABASE preon;
     ```

3. 프로젝트 설정 수정:
   - `src/main/resources/application.properties` 파일을 열어 데이터베이스 접속 정보를 본인의 환경에 맞게 수정합니다.
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/preon?useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&useSSL=false
     spring.datasource.username=mysql_username
     spring.datasource.password=mysql_password
     ```

4. 프로젝트 빌드 및 실행:
   - Gradle을 사용하여 프로젝트를 빌드하고 실행합니다.
     ```bash
     ./gradlew clean build
     ./gradlew bootRun
     ```

5. 웹 애플리케이션 접속:
   - 웹 브라우저에서 `http://localhost:8080`으로 접속하여 애플리케이션을 확인합니다.

## 사용법
이 프로젝트는 Postman과 같은 API 클라이언트를 사용하여 테스트할 수 있습니다. 프론트는 BootStrap과 Thymeleaf를 적절히 섞어 사용했습니다.
주요 API 엔드포인트는 다음과 같습니다.

## API 엔드포인트
- `POST /user/api/signup`: 사용자 등록
- `POST /login`: 사용자 로그인 및 JWT 발급
- `POST /reissue`: Refresh Token을 사용한 Access Token 갱신
- `GET /roleTest/userInfo`: 인증된 사용자 정보 조회


회원가입 -> 로그인 -> JWT 액세스토큰 , 리프레시토큰 발급 (로컬 스토리지, 쿠키에 각각 저장) -> 결과 보기 페이지로 리디렉션

![image](https://github.com/user-attachments/assets/9031a3ed-ae70-4235-b174-b7ac057f90da)
