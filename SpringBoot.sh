pkill -9 -ef MediaStation
nohup java -jar /volume1/tomcat/bin/MediaStation.jar -Dspring.profiles.active=syn &
