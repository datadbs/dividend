# Dividend
Dividend는 주식 종목별 예상 배당금을 조회할 수 있는 웹 서비스입니다.

## 프로젝트 개요
Dividend는 다음과 같은 기능을 제공합니다.

- 사용자 로그인/로그아웃
- 사용자별 관심 종목 등록/삭제
- 등록된 관심 종목의 예상 배당금 조회
- 관심 종목의 배당 이력 조회

Dividend는 크게 다음과 같은 기술 스택(Tech Stack)을 사용하여 개발되었습니다.

- Java
- Spring Boot
- JPA
- Redis
- H2 Database
- JWT
- Jsoup
- Selenium

# 설치 및 실행 방법
Dividend를 설치하고 실행하는 방법은 다음과 같습니다.

- Git을 이용하여 프로젝트를 Clone합니다.
<code>
git clone https://github.com/datadbs/dividend.git
</code>
- 프로젝트 루트 디렉토리로 이동하여 Gradle 빌드를 실행합니다.
./gradlew build
- 빌드 결과로 생성된 Jar 파일을 실행합니다.
java -jar build/libs/{project_name}.jar
- 웹 브라우저를 열고 http://localhost:8080으로 접속합니다.

# 사용 방법
Dividend 서비스를 사용하는 방법은 다음과 같습니다.

- 웹 페이지에서 회원 가입을 합니다.
- 회원 가입 후 로그인을 합니다.
- 메인 페이지에서 종목 검색을 합니다.
- 검색된 종목 중 관심 종목으로 등록할 종목을 선택합니다.
- 등록된 관심 종목은 메인 페이지에서 확인할 수 있습니다.
- 등록된 관심 종목의 배당금 예상액은 배당 예측 페이지에서 확인할 수 있습니다.
- 등록된 관심 종목의 배당 이력은 배당 이력 페이지에서 확인할 수 있습니다.
- 로그아웃을 하여 서비스를 종료합니다.