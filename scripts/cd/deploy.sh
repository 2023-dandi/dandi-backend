PROJECT_ROOT="/home/ubuntu/dandi-project"
DEPLOY_LOG="$PROJECT_ROOT/deploy-log"

CURRENT_TIME=$(date +%c)
cd $PROJECT_ROOT/build/libs

nohup java -jar -Dspring.profiles.active=dev -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector -Dlog4j2.enable.threadlocals=true -Dlog4j2.enable.direct.encoders=true *.jar &
echo "$CURRENT_TIME > jar 파일 실행" >>$DEPLOY_LOG

EXECUTED_PROCESS_PID=$(pgrep -f $JAR_FILE)
echo "$CURRENT_TIME > executed process pid = $EXECUTED_PROCESS_PID" >>$DEPLOY_LOG
curl -s -d "payload={\"text\":\"Application Execution : $EXECUTED_PROCESS_PID\"}" "${SLACK_WEBHOOK_URI}"
