@echo off

set SERVER=localhost
set DIR=demo-data
if "%COMPUTERNAME%"=="ILAB-KITCHEN" set SERVER=ILAB-KITCHEN
if "%COMPUTERNAME%"=="ILAB-KITCHEN" set DIR=DTW-data
if "%COMPUTERNAME%"=="CL-KITCHEN" set SERVER=CL-KITCHEN
if "%COMPUTERNAME%"=="CL-KITCHEN" set DIR=CL-DTW-data
if "%SERVER%"=="" GOTO ERROR

cd /d %~dp0

rem echo Cleaning up logs...
rem if exist *._csv del *._csv
rem if exist *.csv ren *.csv *._csv

echo Starting WAX input manager...
start "WAX-Input" WaxTest.exe -inport * -outstomp %SERVER%

ping 127.0.0.1 -n 2 >nul
echo Starting WAX DTW activity recognizer...
start "WAX-DTW" WaxDTW -connect -port "" -stomp %SERVER% -out %SERVER% -config %DIR%\dtw.txt -data %DIR%

ping 127.0.0.1 -n 2 >nul
echo Starting WAX Simple activity recognizer...
start "WAX-Simple" WaxTest.exe -instomp %SERVER% -out %SERVER% -file %DIR%\simple.txt

ping 127.0.0.1 -n 5 >nul

GOTO END

:ERROR
ECHO This is an unknown host: %COMPUTERNAME%
GOTO END

:END
exit
