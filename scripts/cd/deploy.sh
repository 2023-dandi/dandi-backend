PROJECT_ROOT="/home/ubuntu/dandi-project"
JAR_FILE="$PROJECT_ROOT/dandi.jar"

APP_LOG="$PROJECT_ROOT/application-log"
ERROR_LOG="$PROJECT_ROOT/error-log"
DEPLOY_LOG="$PROJECT_ROOT/deploy-log"

CURRENT_TIME=$(date +%c)

echo "$CURRENT_TIME > $JAR_FILE 파일 복사" >>$DEPLOY_LOG
cp $PROJECT_ROOT/build/libs/*.jar $PROJECT_ROOT

echo "$CURRENT_TIME > $JAR_FILE 파일 실행" >>$DEPLOY_LOG
nohup java -jar "-Dspring.profiles.active=dev \
-DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector \
-Dlog4j2.enable.threadlocals=true -Dlog4j2.enable.direct.encoders=true" $JAR_FILE &

EXECUTED_PROCESS_PID=$(pgrep -f $JAR_FILE)
echo "$CURRENT_TIME > executed process pid = $EXECUTED_PROCESS_PID" >>$DEPLOY_LOG
curl -s -d "payload={\"text\":\"Application Execution : $EXECUTED_PROCESS_PID\"}" "${SLACK_WEBHOOK_URI}"
