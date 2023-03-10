PROJECT_ROOT="/home/ubuntu/dandi-project"
JAR_FILE="$PROJECT_ROOT/dandi.jar"

DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

CURRENT_PID=$(pgrep -f $JAR_FILE)

if [ -z $CURRENT_PID ]; then
  echo "$TIME_NOW > no process" >> $DEPLOY_LOG
else
  kill -15 $CURRENT_PID
  echo "$TIME_NOW > process $CURRENT_PID terminated" >> $DEPLOY_LOG
fi