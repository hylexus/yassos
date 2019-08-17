<p align="center">
  <img src="docs/images/logo.svg">
</p>
<p align="center" style="font-size: 2.5rem;color:#333">YaSSOS</p>
[![Build Status](https://travis-ci.com/hylexus/yassos.svg?branch=master)](https://travis-ci.com/hylexus/yassos)  [![license](https://img.shields.io/badge/LICENSE-Apache%202-7AD6FD.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)  [![maven](https://img.shields.io/badge/maven%20central-3.4.6-brightgreen.svg)](https://search.maven.org/search?q=g:io.github.hylexus.yassos)

**Y**et **a**nother **S**ingle **S**ign-**O**n **S**ystem.

A lightweight, simple single sign-on system based on spring-boot.

See the wiki (Writing…)  for full documentation, examples, custom-configuration and other information.

## Introduction

```sh
~ tree -d -L 1
.
├── docs
├── yassos-client
├── yassos-client-spring-boot-starter
├── yassos-common
├── yassos-distribution
├── yassos-server
├── yassos-server-plugin
│   ├── yassos-session-manager-memory
│   ├── yassos-session-manager-redis
│   ├── yassos-user-loader-file
│   └── yassos-user-loader-jdbc
└── yassos-server-support
```

- **docs:** Document
- **yassos-client:** API for single sign-on system clients.
- **yassos-client-spring-boot-starter:** A `spring-boot-starter`  provided to the spring-boot based clients.
- **yassos-common:** The common module used by YaSSOS.
- **yassos-distribution:** script for build package.
- **yassos-server:** Server side of YaSSOS.
- **yassos-server-support:** Yassos server plugin support
- **yassos-server-plugin:** Builtin YaSSOS Server-Side plugins
  - **yassos-session-manager-memory:** A `memory-based` Session-Manager
  - **yassos-session-manager-redis:** A `redis-based` Session-Manager
  - **yassos-user-loader-file:** A `file-based` user details loader
  - **yassos-user-loader-jdbc:** A `jdbc-based` user details loader, for example, if you want to load user data from MySQL, this plugin may be useful.

## Quick Start

See the wiki (Writing…)  for full documentation, examples, custom-configuration and other information.

> In this example, we will have the following domain mapping (due to `cookie restriction strategy` )  in file `/etc/hosts` :

```sh
127.0.0.1	sso.mine.com
127.0.0.1	web-01.mine.com
127.0.0.1	web-02.mine.com
```

### 1. Start the server-side

- Download & build  server-side

```sh
git clone https://github.com/hylexus/yassos.git

cd yassos
./gradlew clean build install
```

- Start the YaSSOS server-side

```sh
# start server(default port: 5201)
java -jar yassos-server/build/libs/yassos-server.jar
```

- Check the Result

If all goes well, you'll see something like this:

![yassos-server-output](docs/images/yassos-server-statistics.png)

Access http://sso.mine.com:5201/login in your browser.

### 2. Run client samples

- Download and build samples project

```sh
# download sources
git clone https://github.com/hylexus/yassos-samples.git

cd yassos-samples
# package
mvn clean package -DskipTests
```

#### Run the client project based on spring-boot

```
java -jar yassos-client-sample-spring-boot/target/yassos-client-sample-spring-boot-1.0-SNAPSHOT.jar
```

Acess the protected resource http://web-01.mine.com:1010/client/user/me in your browser. And then you will be redirected to login page.

Type the username (`yassos`)  and password (`yassos`) to sign-on.

#### Run the client project based on traditional Java-web-app

> In order to this example to work, you need to install a Tomcat container on your computer.

```sh
# copy the traditional java-web-app to your Tomcat 
cp yassos-client-sample-web-cookie/target/yassos-client-sample-web-cookie.war /path/to/apache-tomcat-8.5.41/webapps
```

- start your tomcat container 

And then access the protected resource http://web-02.mine.com:8080/yassos-client-sample-web-cookie/protected-resources/resource.jsp .

Congratulations, you can access protected resources without logging in this time.



## distribution

Currently supported parameters:

| Key               | Value                    |
| ----------------- | ------------------------ |
| `user-loader`     | `file-user-loader`       |
|                   | `jdbc-user-loader`       |
| `session-manager` | `memory-session-manager` |
|                   | `redis-session-manager`  |

- `file-user-loader`
  - A builtin `user-loader` that load user info from a file specified by `yassos.user-store.file.file-location` in `application.yml` 
- `jdbc-user-loader`
  - A `JDBC-Based` `UserLoader`
  - You should specify the  configuration `spring.datasource.*` in `application.yml`，see `${installation_dir}/conf/yassos-server-example-full-config.yml` for full config samples
- `memory-session-manager`
  - A `Memory-Based SessionManager`
- `redis-session-manager`
  - A `Redis-Based SessionManager`
  - You should specify the configuraion `spring.redis.*` in  `application.yml`，see `${installation_dir}/conf/yassos-server-example-full-config.yml` for full config samples

```sh
./gradlew clean build releaseYassosServer \
-Duser-loader=file-user-loader \
-Dsession-manager=memory-session-manager
```

> Note:
>
> `-Duser-loader=file-user-loader` means that you will use a builtin `UserLoader` to load user info from a file  specified by `yassos.user-store.file.file-location` in `application.yml` .
>
> `-Dsession-manager=memory-session-manager` means that you wil use a builtin SessionManager base on memory.



And then, `yassos-server-1.0-SNAPSHOT.tar.gz` and `yassos-server-1.0-SNAPSHOT.tar.zip` was generated in `build/distributions`.



You can copy `yassos-server-1.0-SNAPSHOT.tar.gz` to you installation directory. 

```sh
# copy tar.gz to you installation directory
cp build/distributions/yassos-server-1.0-SNAPSHOT.tar.gz /usr/local/opt/yassos
# Decompression
cd /usr/local/opt/yassos/yassos-server-1.0-SNAPSHOT
# start the yassos server
bin/yassos-server.sh start
```

- distribution structure

```sh
~ tree  -L 2
.
├── LICENSE
├── NOTICE
├── bin
│   ├── yassos-server.bat
│   └── yassos-server.sh
├── conf
│   ├── application.yml
│   ├── logback.xml
│   └── yassos-server-example-full-config.yml # full configuration samples
└── lib
    └── yassos-server.jar
```



See the wiki (Writing…)  for full documentation, examples, custom-configuration and other information.