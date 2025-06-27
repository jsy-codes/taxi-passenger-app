
---

# 🚕 taxi-passenger-app

---

## Git Repository 정보

### 🔗 Repository 주소

[https://github.com/CornCar/taxi-passenger-app.git](https://github.com/CornCar/taxi-passenger-app.git)

---

## 🛠️ 실행 방법

```bash
git clone https://github.com/CornCar/taxi-passenger-app.git
cd taxi-passenger-app
./gradlew bootRun
```

---

## ✅ 필수 설정

### 🔧 Lombok

* IntelliJ: Lombok 플러그인 설치 및 `Annotation Processing` 활성화
* Eclipse: lombok.jar 실행하여 설치 후, Annotation Processing 활성화
* Gradle 설정:

  ```groovy
  dependencies {
      compileOnly 'org.projectlombok:lombok'
      annotationProcessor 'org.projectlombok:lombok'
  }
  ```

---

##  H2 Database 설정

본 프로젝트는 개발 및 테스트 용도로 **H2 데이터베이스**를 사용합니다.

###  연결 정보

| 항목         | 값                                                                    |
| ---------- | -------------------------------------------------------------------- |
| JDBC URL   | `jdbc:h2:tcp://localhost/~/eonet_db`                                 |
| Username   | `sa`                                                                 |
| Password   | `1234`                                                               |
| H2 Console | [http://localhost:8080/h2-console](http://localhost:8080/h2-console) |

###  H2 콘솔 접속 시 설정

* JDBC URL: `jdbc:h2:tcp://localhost/~/eonet_db`
* 사용자명: `sa`
* 비밀번호: `1234`
* Driver Class: 자동 설정됨 (org.h2.Driver)

###  application.properties 요약

```properties
spring.application.name=eonet

# H2 DB
spring.datasource.url=jdbc:h2:tcp://localhost/~/eonet_db
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=1234

# JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

---


