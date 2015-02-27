#/bin/bash
MAIN=shell.mysql.Phonebook
p1=--apiKey ## api-Key
v1=AIzaSyAg-oNoJ39Qx__eTo-8art7b4_TViUn7H8 #$3 # API-KEY
p2=--query ## all|address|email|phone
v2=$1

CLASSPATH=$CLASSPATH:gshell_lib/okhttp-2.2.0.jar:gshell_lib/gson-2.3.1.jar:gshell_lib/joda-time-2.7.jar:gshell_lib/mysql-connector-java-5.1.34-bin.jar:gmapservice.jar:okio.jar:gshellmap.jar
echo "java -cp" $CLASSPATH $MAIN $p1 "secret" $p2 $v2
java -cp $CLASSPATH $MAIN $p1 $v1 $p2 $v2
