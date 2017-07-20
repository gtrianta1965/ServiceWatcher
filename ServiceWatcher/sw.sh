 if [ "$1" != null ] || ["$2" == null];                                                                
then
	java -cp /home/Akorilli/jdeveloper/mywork/ServiceWatcher_2/ServiceWatcher/target/ServiceWatcher-1.0-SNAPSHOT.jar:./target/lib/* com.cons.Main $1 $2
elif (["$1" != null ] && ["$2" == null]) || (["$1" == "" ] && ["$2" != null])
then
	echo "File name cant be null Give -conf <propertiefile>"
fi
