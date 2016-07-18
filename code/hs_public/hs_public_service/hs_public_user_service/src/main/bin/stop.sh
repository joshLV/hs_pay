#! /bin/sh
#停止方法
PID=$(cat pid)
kill -9 $PID
