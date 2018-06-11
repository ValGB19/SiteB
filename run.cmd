::"*******************  BUILDING MODULE  *****************************************"
::mvn clean
::mvn install
::"*******************  COLLECTING DEPENDENCIES  *********************************"
::mvn dependency:copy-dependencies

forfiles /p target\dependency\ /c "cmd /c echo @path >> logfile.txt"
set _CLASPATH=
setlocal EnableDelayedExpansion
set "string=findstr /R "^^" target\dependency\logfile.txt"
for /f %%a in ('!string!') do set _CLASPATH=!_CLASPATH!%%a;
set _CLASPATH= !_CLASPATH!"%cd%\target\classes"
set _CLASPATH=!_CLASPATH:"=!
del target\dependency\logfile.txt


echo "*******************  EXECUTING PROGRAM******************************************"
java -cp !_CLASPATH! -Dactivejdbc.log prode.App

