@ECHO OFF

set SERVER=localhost
if "%COMPUTERNAME%"=="BERNADETTE-PD" set SERVER=ILAB-KITCHEN
if "%SERVER%"=="" GOTO ERROR

start "Recog" /B Recog.exe -grammar recog.xml -stomphost %SERVER%
GOTO END

:ERROR
ECHO Unknown PC: %COMPUTERNAME%
GOTO END

:END
 