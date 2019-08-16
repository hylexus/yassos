@echo off

setlocal

rem Check if we have a usable JDK
if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
goto okJava

:noJavaHome
echo The JAVA_HOME environment variable is not defined correctly.
echo Please configure the JAVA_HOME environment variable, you won't lose anything
goto end

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
goto end

:isRunning

rem ANSI Colors
:echoRed
echo [31m%~1[0m
EXIT /B 0
:echoGreen
echo [32m%~1[0m
EXIT /B 0
:echoYellow
echo [33m%~1[0m
EXIT /B 0

:doStatus
call :reloadPid
if %PID% == ""(
call :echoRed "Not running"
EXIT /B 3
go end
)

:doForceStop
call :reloadPid
if %PID% == ""(
call :echoYellow "Not running (pid not found)"
go end
)
taskkill /F /pid %PID%
goto end

:doStop
call :reloadPid
taskkill /F /pid %PID%
goto end

:doStart
call :reloadPid
call :echoRed "s   tart"
call "%JAVA_HOME%\bin\java.exe" %APP_JAVA_OPT%
goto end

:doRestart
call :reloadPid
goto end

:reloadPid
for /f "usebackq tokens=1" %%i in (`jps -l ^| find "yassos-server.jar"`) do set PID=%%i
EXIT /B 0

goto end
:end
EXIT /B 0