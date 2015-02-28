#/bin/bash
MAIN=shell.mysql.Phonebook
p1=--query 
v1=address ## or all|email|phone

CLASSPATH=$CLASSPATH:libs/okhttp-2.2.0.jar:libs/gson-2.3.1.jar:libs/joda-time-2.7.jar:libs/mysql-connector-java-5.1.34-bin.jar:libs/gmapservice.jar:libs/okio.jar:gshellmap.jar
echo "java -cp" $CLASSPATH $MAIN $p1 $v1
java -cp $CLASSPATH $MAIN $p1 $v1
