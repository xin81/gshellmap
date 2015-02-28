#!/bin/bash
## variables
arg=$1
string="sh help.sh [ gshellmap | gshellmetry | gphonebook ]"
mainjar=gshellmap.jar ## main-jar file

CLASSPATH=$CLASSPATH:libs/okhttp-2.2.0.jar:libs/gson-2.3.1.jar:libs/joda-time-2.7.jar:libs/mysql-connector-java-5.1.34-bin.jar:libs/gmapservice.jar:libs/okio.jar
echo "set CLASSPATH=§CLASSPATH"$CLASSPATH

if [ $# -eq 0 ] ; then
	echo $string
else
	if [ $arg = "help" ] ; then
		echo $string
	fi
	
	if [ $arg = "gshellmap" ] ; then
		## echo "ghsellmap --help"
		java -cp $CLASSPATH:$mainjar shell.distance.GMapShell --help
	fi	

	if [ $arg = "gshellmetry" ] ; then
		## echo "gshellmetry --help"
		java -cp $CLASSPATH:$mainjar shell.geometry.GooMetryShell --help
	fi

	if [ $arg = "gphonebook" ] ; then
		## echo "phonebook --help"
		java -cp $CLASSPATH:$mainjar shell.mysql.GPhonebook --help
	fi
fi
