@echo off

set VERSION=1.0.0
set JAR_FILE=homw-tool

if not exist "%JAVA_HOME%\bin\java.exe" echo Please set the JAVA_HOME variable in your environment, we need java(x64)! jdk7 or later is better! & exit /b 1
set "JAVA=%JAVA_HOME%\bin\java.exe"

set BASE_DIR=%~dp0
rem added double quotation marks to avoid the issue caused by the folder names containing spaces.
rem removed the last 5 chars(which means \bin\) to get the base dir.
set BASE_DIR="%BASE_DIR:~0,-5%"

set "JAVA_OPT=-Xms128m -Xmx128m -Xmn64m -Dlogpath=%BASE_DIR%/logs"
set "JAVA_OPT=%JAVA_OPT% -jar %BASE_DIR%\target\%JAR_FILE%-%VERSION%.jar"

call "%JAVA%" %JAVA_OPT% %*
pause