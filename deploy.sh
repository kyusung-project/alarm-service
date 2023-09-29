kill $(cat /home/leekyusung/service/alarm/pid.file) &&
  rm /home/leekyusung/service/alarm/pid.file | true
nohup ./start.sh &