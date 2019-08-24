@echo off

set o1=%1
set o2=%2
set model=%3

SET CLASSPATH=%CLASSPATH%;ontomap.jar;lib/*



java com.merak.ontomap.CreateModel %o1% %o2% %model%

pause