@echo off
set ROOT_F=%1
mvn clean install -Dmaven.test.skip=true
:: Validate path
:: TODO: VALIDATE THE PATH
echo "\n[INFO] Deploying at path %ROOT_F%\n"
:: Make dir structure
mkdir "%ROOT_F%"ServiceWatcher/
mkdir "%ROOT_F%"ServiceWatcher/target/
mkdir "%ROOT_F%"ServiceWatcher/target/lib/
:: Copy needed files for stand alone
xcopy /s *report_template.html "%ROOT_F%"ServiceWatcher/
xcopy /s target/lib/* "%ROOT_F%"ServiceWatcher/target/lib/
xcopy /s target/ServiceWatcher* "%ROOT_F%"ServiceWatcher/target/
xcopy /s sample-config.properties "%ROOT_F%"ServiceWatcher/config.properties
:: Copy executable scripts for win/linux
xcopy /s sw.bat "%ROOT_F%"ServiceWatcher/
xcopy /s appendSW.bat "%ROOT_F%"ServiceWatcher/
xcopy /s sw.sh "%ROOT_F%"ServiceWatcher/
echo "[INFO] Application deployed at -> %ROOT_F%\n"
echo "[INFO] To run Service Watcher\n-> Linux run './sw.sh' in a Terminal\n-> Windows run 'sw.bat' in CMD\nLocated in '%ROOT_F%'ServiceWatcher/\n"
