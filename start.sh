#!/bin/bash
cp build/libs/*.jar alarm-service.jar
java -jar alarm-service.jar --spring.profiles.active=prod &
echo $! >/home/leekyusung/alarm-service/pid.file &