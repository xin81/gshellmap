#/bin/bash
MAIN=shell.distance.GMapShell
p1=--apiKey ## api-Key
v1=AIzaSyAg-oNoJ39Qx__eTo-8art7b4_TViUn7H8 #$3 # API-KEY
p2=--fuzzy ## fuzzy
v2=$1
p3=--language ## language
v3=en-GB #
p4=--unit ## unit
v4=IMPERIAL ##|METRIC
p5=--mode ## mode
v5=driving ## walking|driving|bicycling|transit

CLASSPATH=$CLASSPATH:gshell_lib/okhttp-2.2.0.jar:gshell_lib/gson-2.3.1.jar:gshell_lib/joda-time-2.7.jar:gmapservice.jar:okio.jar:gshellmap.jar
echo "java -cp" $CLASSPATH $MAIN $p1 "secret" $p2 $v2 $p3 $v3 $p4 $v4 $p5 $v5
java -cp $CLASSPATH $MAIN $p1 $v1 $p2 $v2 $p3 $v3 $p4 $v4 $p5 $v5
