# YASSOS

**Y**et **A**nother **S**ingle **S**ign-**O**n **S**ystem.

A lightweight, simple single sign-on system based on spring-boot.

See the wiki for full documentation, examples, custom-configuration and other information.

## Introduction

```sh
~ tree -d -L 1
.
├── docs
├── yassos-client
├── yassos-client-spring-boot-starter
├── yassos-common
├── yassos-distribution
└── yassos-server
```

- **docs:** Document
- **yassos-client:** API for single sign-on system clients.
- **yassos-client-spring-boot-starter:** A `spring-boot-starter`  provided to the 
- **yassos-common:** The common module used by YASSOS.
spring-boot based clients.
- **yassos-distribution:** **This module is under development.**
- **yassos-server:** Server side of YASSOS.

## Quick Start

> Here is an example that loading user data from `MySQL`.

See the wiki for full documentation, examples, custom-configuration and other information.

> In this example, we will have the following domain mapping in file `/etc/hosts` :

```sh
127.0.0.1	sso.mine.com
127.0.0.1	web-01.mine.com
127.0.0.1	web-02.mine.com
```

### 1. Configure and start the server side

- Download and build sources

```sh
git clone https://github.com/hylexus/yassos.git
```

- Create Database to load user details

Script location : `yassos/docs/sql/quick-start.sql`

- Modify the database configuration in the file `yassos-server/src/main/resources/application.yml`

```yaml
server:
  port: 5201
  servlet:
    context-path: /
spring:
  datasource:
    username: yourUserName
    password: IDoNotknow
    url: jdbc:mysql://localhost:3306/yassos_server?useUnicode=true&useSSL=true&zeroDateTimeBehavior=convertToNull&serverTimezone=GMT%2B8
    driver-class-name: com.mysql.cj.jdbc.Driver
```

- Build and Start YASSOS server

```sh
cd yassos

# package
mvn clean package -DskipTests

# start server(default port: 5201)
java -jar yassos-server/target/yassos-server.jar
```

- Check the Result

If all goes well, you'll see something like this:

![yassos-server-output](docs/images/yassos-server-statistics.png)

Access http://sso.mine.com:5201/login in your browser.

### 2. Run client samples

- Download and build samples project

```sh
# download sources
git clone https://github.com/hylexus/yassos.git

cd yassos-samples
# package
mvn clean package -DskipTests
```

#### Run the client project based on spring-boot

```
java -jar yassos-client-sample-spring-boot/target/yassos-client-sample-spring-boot-1.0-SNAPSHOT.jar
```

Acess the protected resource http://web-01.mine.com:1010/client/user/me in your browser. 

And then you will be redirected to login page.

Type the username (`admin`)  and password (`1234561`) to sign-on.

#### Run the client project based on traditional Java-web-app

> In order to this example to work, you need to install a Tomcat container on your computer.

```sh
# copy the traditional java-web-app to your Tomcat 
cp yassos-client-sample-web-cookie/target/yassos-client-sample-web-cookie.war /path/to/apache-tomcat-8.5.41/webapps
```

- start your tomcat container 

And then access the protected resource http://web-02.mine.com:8080/yassos-client-sample-web-cookie/protected-resources/resource.jsp .

Congratulations, you can access protected resources without logging in this time.



See the wiki for full documentation, examples, custom-configuration and other information.
