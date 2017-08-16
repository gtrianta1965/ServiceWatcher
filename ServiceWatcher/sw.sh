JARS=""
PROGRAM="target/ServiceWatcher-1.0-SNAPSHOT.jar"
MAIN="com.cons.Main"

for i in target/lib/*.jar; do
	[ -f "$i" ] || break
		if [ "$JARS" = "" ]
		then
			JARS="$i"
		else
			JARS="$JARS:$i"
		fi
done

EXEC="$PROGRAM:$JARS"

java -cp $EXEC $MAIN $1 $2 $3 $4 $5 $6
