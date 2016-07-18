#! /bin/sh
#启动方法
# eth=$(cat ../config/eth)
#ip=$(ifconfig eth$eth | grep "inet addr" | awk '{ print $2}' | awk  -F: '{print $2}')	
#java -jar ../lib.jar $ip ../config/applicationContext.xml &
java -Djava.rmi.server.hostname=120.27.198.183 -jar ../*.jar ../config/applicationContext.xml &
echo $! > pid 
