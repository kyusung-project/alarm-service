kill $(cat /home/leekyusung/alarm-service/pid.file) &&
  rm /home/leekyusung/alarm-service/pid.file | true
nohup ./start.sh &