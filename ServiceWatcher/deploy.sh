ROOT_F=$1
./rebuild.sh
printf "\n[\033[1;38;2;0;255;0mINFO\033[0m] Deploying at path $ROOT_F/\n"
# Make dir structure
mkdir "$ROOT_F"
mkdir "$ROOT_F"/target/
mkdir "$ROOT_F"/target/lib/
# Copy needed files for stand alone
cp report_template.html "$ROOT_F"/
cp target/lib/* "$ROOT_F"/target/lib/
cp target/ServiceWatcher* "$ROOT_F"/target/
cp sample-config.properties "$ROOT_F"/
# Copy executable scripts for win/linux
cp sw.bat "$ROOT_F"/
cp appendSW.bat "$ROOT_F"/
cp sw.sh "$ROOT_F"/
printf "[\033[1;38;2;0;255;0mINFO\033[0m] Application deployed at -> $ROOT_F\n"
printf "[\033[1;38;2;0;255;0mINFO\033[0m] To run Service Watcher\n-> Linux run './sw.sh' in a Terminal\n-> Windows run 'sw.bat' in CMD\nLocated in "$ROOT_F"/bin/\n"
