#/bin/bash
MAIN=shell.geometry.GooMetryShell
p1=--latitude
v1=25.033333
p2=--longitude
v2=121.633333

CLASSPATH=$CLASSPATH:libs/okhttp-2.2.0.jar:libs/gson-2.3.1.jar:libs/joda-time-2.7.jar:libs/gmapservice.jar:libs/okio.jar:gshellmap.jar
echo "java -cp" $CLASSPATH $MAIN $p1 "secret" $p2 $v2 $p3 $v3
java -cp $CLASSPATH $MAIN $p1 $v1 $p2 $v2
