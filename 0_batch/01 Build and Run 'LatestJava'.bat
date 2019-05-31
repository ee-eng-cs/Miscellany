@echo off
set JAVA_HOME=C:\PROGRA~1\JAVA\JDK-12
set MAINCLASS=LatestJava

set LINT=-Xlint -Xdoclint 
set SOURCEPATH=src/main/java
set JARFILE=.\target\%MAINCLASS%.jar
cd ..
rmdir /S /Q target
mkdir target
%JAVA_HOME%\bin\javac %LINT% -encoding UTF-8 -d target -sourcepath %SOURCEPATH% %SOURCEPATH%/a/latest/java/*.java %SOURCEPATH%/kp/utils/*.java
%JAVA_HOME%\bin\jar -cf %JARFILE% -C target a\latest\java -C target kp\utils -C src\main\resources .
chcp 65001
%JAVA_HOME%\bin\java -Dfile.encoding=UTF-8 -cp %JARFILE% a.latest.java.%MAINCLASS%
pause