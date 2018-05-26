@echo off
echo [INFO] Use maven jetty-plugin run the project.


set MAVEN_OPTS=%MAVEN_OPTS% -XX:MaxPermSize=128m
call mvn jetty:run -Djetty.port=8888

cd bin
pause