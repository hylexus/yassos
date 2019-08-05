#!/bin/sh

export base_dir=`cd $(dirname $0)/../; pwd`
jar_name='yassos-server.jar'
ABS_JAR_PATH=${base_dir}/lib/${jar_name}

export default_spring_config_locations="classpath:/,classpath:/config/,file:./,file:./config/"
export spring_config_locations=${default_spring_config_locations},file:${base_dir}/conf/

APP_JAVA_OPT='-XX:PermSize=512M -XX:MaxPermSize=512M -Xmx1024M -Xms1024M -Xss256k -XX:ParallelGCThreads=10 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC'
APP_JAVA_OPT="${APP_JAVA_OPT} -jar ${ABS_JAR_PATH}"
APP_JAVA_OPT="${APP_JAVA_OPT} --spring.config.location=${spring_config_locations}"
APP_JAVA_OPT="${APP_JAVA_OPT} --logging.config=${base_dir}/conf/logback.xml"
#APP_JAVA_OPT="${APP_JAVA_OPT} -Dyassos.home=${base_dir}"
APP_JAVA_OPT="${APP_JAVA_OPT} --yassos.home=${base_dir}"
APP_JAVA_OPT="${APP_JAVA_OPT} -Dloader.path=${base_dir}/lib/spring-boot-plugin/,${base_dir}/lib/ext/"

reloadPid() { APP_PID=`ps -ef | grep java | grep ${jar_name} | awk '{print $2}'`;}
# $1:PID
isRunning() { ps -p $1 &> /dev/null; }
# ANSI Colors
echoRed() { echo $'\e[0;31m'"$1"$'\e[0m'; }
echoGreen() { echo $'\e[0;32m'"$1"$'\e[0m'; }
echoYellow() { echo $'\e[0;33m'"$1"$'\e[0m'; }

status() {
    reloadPid
    [ ${APP_PID} ] || { echoRed "Not running"; return 3; }
    isRunning ${APP_PID} || { echoRed "Not running (process ${APP_PID} not found)"; return 1; }
    echoGreen "Running [${APP_PID}]"
    return 0
}

force_stop() {
    reloadPid
    [ ${APP_PID} ] || { echoYellow "Not running (pid not found)"; return 0; }
    isRunning ${APP_PID} || { echoYellow "Not running (process ${APP_PID})."; return 0; }

    # force stop
    kill -9 ${APP_PID} &> /dev/null || { echoRed "Unable to kill process ${APP_PID}"; return 1; }
    return 0
}

# Function: stop
doStopGracefully() {
  reloadPid
  [ ${APP_PID} ] || { echoYellow "Not running (pid not found)"; return 1; }
  isRunning ${APP_PID} || { echoYellow "Not running (process ${APP_PID})."; return 1; }

    kill -TERM ${APP_PID}
    echo -ne "Stopping ...";

    WAIT_TIME=20;

    count=0;
    while kill -0 ${APP_PID} 2>/dev/null && [ ${count} -le ${WAIT_TIME} ]; do
      printf "...";
      sleep 1;
      (( count++ ));
    done;

    [ ${count} -gt ${WAIT_TIME} ] || { echoGreen ' [OK]' ; return 0; }

    printf "process is still running after %d seconds, killing process" ${WAIT_TIME};
    kill ${APP_PID};
    sleep 3;

    [ kill -0 ${APP_PID} 2>/dev/null ] || { echoGreen ' [OK]' ; return 0; }

    # if it's still running use kill -9
    echo "process is still running, using kill -9";
    kill -9 ${APP_PID}
    sleep 3;

    [ kill -0 ${APP_PID} 2>/dev/null ] || { echoGreen ' [OK]' ; return 0; }

    echoRed "process is still running, I give up";
    return 1
}

stop() {
    reloadPid
    doStopGracefully
}

start() {
    reloadPid
    isRunning ${APP_PID} && { echoYellow "Already running [${APP_PID}]"; return 0; }

    yassos_log_dir=${base_dir}/logs
    if [ ! -d ${yassos_log_dir} ] ; then
      mkdir -p ${yassos_log_dir}
      echoGreen "${yassos_log_dir} was auto created."
    fi

#    java ${APP_JAVA_OPT} >> /dev/null 2>&1 &
    echoGreen "java-process-info: java ${APP_JAVA_OPT}" >> ${yassos_log_dir}/yassos.out 2>&1 &
    nohup java ${APP_JAVA_OPT} >> ${yassos_log_dir}/yassos.out 2>&1 &
    APP_PID=$!
    disown ${APP_PID}

    [ -f ${ABS_JAR_PATH} ] || { echoRed "The Jar file path is invalid : ${ABS_JAR_PATH}";return 1; }
    echoGreen "Starting [${APP_PID}]"
    echoGreen "YaSSOS is starting，you can check the output in file : ${yassos_log_dir}/yassos.out"
    return 0
}

restart() {
    reloadPid;
    stop;
    STOP_STATUS=$?;

    if [ ${STOP_STATUS} -eq 0 ]; then
        echoGreen "Stopped [${APP_PID}]";
        start
    elif [ ${STOP_STATUS} -eq 1 ]; then
        start
    fi
}

case $1 in
    start)
      start; exit $?;;
    stop)
      stop; exit $?;;
    force-stop)
      force_stop; exit $?;;
    restart)
      restart; exit $?;;
    status)
      status; exit $?;;
    *)
      echo "Usage: $0 {start|stop|force-stop|restart|status}";
      exit 1;
esac
