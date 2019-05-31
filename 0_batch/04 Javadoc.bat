@echo off
set JAVA_HOME=C:\PROGRA~1\JAVA\JDK-12
set PARAMS=-d ..\docs\apidocs
set PARAMS=%PARAMS% -encoding UTF-8
set PARAMS=%PARAMS% -charset UTF-8
set PARAMS=%PARAMS% -docencoding UTF-8
set PARAMS=%PARAMS% -sourcepath ..\src\main\java
set PARAMS=%PARAMS% -subpackages kp
set PARAMS=%PARAMS% a.latest.java
%JAVA_HOME%\bin\javadoc %PARAMS%
pause