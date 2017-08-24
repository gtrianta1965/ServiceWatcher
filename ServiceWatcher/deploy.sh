OS=$1
ROOT_F=$2
echo "Deploying at path $ROOT_F/"
mkdir "$ROOT_F"
mkdir "$ROOT_F"/bin/
mkdir "$ROOT_F"/bin/target/
mkdir "$ROOT_F"/bin/target/lib/
cp ../sw.png "$ROOT_F"/
cp report_template.html "$ROOT_F"/bin/
cp target/lib/* "$ROOT_F"/bin/target/lib/
cp target/ServiceWatcher* "$ROOT_F"/bin/target/
if [ "$OS" = "-win" ]
then
	cp sw.bat "$ROOT_F"/bin/
	cp appendSW.bat "$ROOT_F"/bin/
	echo "Application deployed at $ROOT_F"
	echo "To run Service Watcher 'run ./sw.bat' located in "$ROOT_F"/bin/"
elif [ "$OS" = "-unix" ]
then
	cp sw.sh "$ROOT_F"/bin/
	echo "Application deployed at $ROOT_F"
	echo "To run Service Watcher 'run ./sw.sh' located in "$ROOT_F"/bin/"
else
	rm -rf "$ROOT_F"/
	echo "Wrong OS paremeter make sure you used './deploy.sh -win/unix <path>'"
fi
