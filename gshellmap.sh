#/bin/bash
MAIN=shell.distance.GMapShell
p1=--fuzzy
v1=$1 ## false|true
p2=--language
v2=en-GB
p3=--unit
v3=IMPERIAL ## or METRIC
p4=--mode
v4=driving ## or walking|driving|bicycling|transit

CLASSPATH=$CLASSPATH:libs/okhttp-2.2.0.jar:libs/gson-2.3.1.jar:libs/joda-time-2.7.jar:libs/gmapservice.jar:libs/okio.jar:gshellmap.jar
echo "java -cp" $CLASSPATH $MAIN $p1 $v1 $p2 $v2 $p3 $v3 $p4 $v4
java -cp $CLASSPATH $MAIN $p1 $v1 $p2 $v2 $p3 $v3 $p4 $v4
