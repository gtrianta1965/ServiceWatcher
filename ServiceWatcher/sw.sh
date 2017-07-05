 if [ "$1" != null ] || ["$2" == null];                                                                
then
     echo "File name cant be null Give -conf <propertiefile>"
elif (["$1" == null ] && ["$2" == null]) || (["$1" == "" ] && ["$2" == ""])
then
java -cp ./target/lib/ojdbc6-11.2.0.3.jar:./target/ServiceWatcher-1.0-SNAPSHOT.jar -jar ./target/ServiceWatcher-1.0-SNAPSHOT.jar $1 $2
fi
