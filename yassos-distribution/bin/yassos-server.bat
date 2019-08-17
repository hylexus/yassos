@echo off

setlocal

rem Check if we have a usable JDK
if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
goto okJava

:noJavaHome
echo The JAVA_HOME environment variable is not defined correctly.
echo Please configure the JAVA_HOME environment variable, you won't lose anything
goto exit0

:okJava
cd ..
set "base_dir=%cd%"
set jar_name=yassos-server.jar
set "ABS_JAR_PATH=%base_dir%\lib\%jar_name%"
set default_spring_config_locations=classpath:\,classpath:\config\,file:.\,file:.\config\
set "spring_config_locations=%default_spring_config_locations%,file:%base_dir%\conf\"
rem set APP_JAVA_OPT=-XX:PermSize=512M -XX:MaxPermSize=512M -Xmx1024M -Xms1024M -Xss256k -XX:ParallelGCThreads=10 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC
set APP_JAVA_OPT=-XX:PermSize=512M -XX:MaxPermSize=512M -Xmx1024M -Xms1024M -Xss256k -XX:+UseConcMarkSweepGC -XX:+UseParNewGC
set "APP_JAVA_OPT=%APP_JAVA_OPT% -Dyassos.home=%base_dir%"
set "APP_JAVA_OPT=%APP_JAVA_OPT% -Dloader.path=%base_dir%\lib\spring-boot-plugin\,%base_dir%\lib\ext\"
set "APP_JAVA_OPT=%APP_JAVA_OPT% -jar %ABS_JAR_PATH%"
set "APP_JAVA_OPT=%APP_JAVA_OPT% --spring.config.location=%spring_config_locations%"
set "APP_JAVA_OPT=%APP_JAVA_OPT% --logging.config=%base_dir%\conf\logback.xml"

if ""%1"" == ""status"" goto doStatus
if ""%1"" == ""force-stop"" goto doForceStop
if ""%1"" == ""stop"" goto doStop
if ""%1"" == ""start"" goto doStart
if ""%1"" == ""restart"" goto doRestart
echo Usage: yassos-server.bat {start^|stop^|force-stop^|restart^|status}
goto exit0

:reloadPid
for /f "usebackq tokens=1" %%i in (`jps -l ^| find "yassos-server.jar"`) do set APP_PID=%%i
goto exit0

:isRunning

rem ANSI Colors
:echoRed
echo [31m%~1[0m
goto exit0
:echoGreen
echo [32m%~1[0m
goto exit0
:echoYellow
echo [33m%~1[0m
goto exit0

:doStatus
call :reloadPid
if "%APP_PID%" == "" (
call :echoRed "Not running"
goto exit2
) else (
call :echoGreen "Running %APP_PID%"
goto exit0
)

:doForceStop
call :reloadPid
if "%APP_PID%" == "" (
call :echoRed "Not running"
goto exit0
)
taskkill /F /pid %APP_PID%
goto exit0

:doStop
call :reloadPid
if "%APP_PID%" == "" (
call :echoRed "Not running"
goto exit2
)
taskkill /F /pid %APP_PID%
goto exit0

:doStart
call :reloadPid
if not "%APP_PID%" == "" (
call :echoYellow "Already running [%APP_PID%]"
goto exit0
)
if not exist %ABS_JAR_PATH% (
call :echoRed "The Jar file path is invalid : %ABS_JAR_PATH%"
goto exit0
)
call :echoGreen "Start..."
call "%JAVA_HOME%\bin\java.exe" %APP_JAVA_OPT%
goto exit0

:doRestart
call :reloadPid
call :doStop
call :echoGreen "Start..."
call "%JAVA_HOME%\bin\java.exe" %APP_JAVA_OPT%
goto exit0

:exit0
EXIT /B 0
:exit1
EXIT /B 1
:exit2
EXIT /B 2