@echo off
set JAVA_HOME=C:\PROGRA~1\JAVA\JDK-12
set PACKAGE=kp/web
set MAINCLASS=MainForWeb

set SOURCEPATH=src/main/java
set JARFILE=.\target\%MAINCLASS%.jar
cd ..
rmdir /S /Q target
mkdir target
%JAVA_HOME%\bin\javac -encoding UTF-8 -d target -sourcepath %SOURCEPATH% %SOURCEPATH%/%PACKAGE%/*.java
%JAVA_HOME%\bin\jar -cf %JARFILE% -C target %PACKAGE% -C src\main\resources .
%JAVA_HOME%\bin\java -cp %JARFILE% %PACKAGE%.%MAINCLASS%
pause