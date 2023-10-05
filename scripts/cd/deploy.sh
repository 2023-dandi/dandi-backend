ROOT_PATH="/home/ubuntu"
DEPLOY_LOG="$ROOT_PATH/deploy.log"
ORIGINAL_JAR_FILE="$ROOT_PATH/dandi-project/build/libs/*.jar"

CURRENT_TIME=$(date +%c)

cp $ORIGINAL_JAR_FILE $ROOT_PATH

cd $ROOT_PATH
nohup java -jar -Dspring.profiles.active=dev -Duser.timezone=Asia/Seoul -DimageAccessUrl=https://d288bcoosdr1l2.cloudfront.net/ -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector -Dlog4j2.enable.threadlocals=true -Dlog4j2.enable.direct.encoders=true *.jar /dev/null 2> /dev/null < /dev/null &
echo "$CURRENT_TIME > jar 파일 실행" >>$DEPLOY_LOG

EXECUTED_PROCESS_PID=$(lsof -t -i tcp:8080)
echo "$CURRENT_TIME > executed process pid = $EXECUTED_PROCESS_PID" >>$DEPLOY_LOG
curl -s -d "payload={\"text\":\"Application Execution : $EXECUTED_PROCESS_PID\"}" "${SLACK_WEBHOOK_URI}"
