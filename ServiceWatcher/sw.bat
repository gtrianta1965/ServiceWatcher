set SW=.

for %%i in (.\target\lib\*.jar) do call appendSW %%i
for %%i in (.\target\*.jar) do call appendSW %%i

echo Using classpath:  %SW%

java -cp %SW% com.cons.Main %1 %2 %3 %4 %5 %6

REM Old way
REM java -cp target\ServiceWatcher-1.0-SNAPSHOT.jar;target\lib\ojdbc6-11.2.0.3.jar;target\lib\mail-1.4.7.jar;target\lib\jsoup-1.8.3.jar;target\lib\json-20170516.jar;target\lib\jsch-0.1.54.jar;target\lib\commons-codec-1.3.jar com.cons.Main %1 %2 %3 %4 %5 %6

