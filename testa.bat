@echo off

set CLASSPATH=%CLASSPATH%;ontomap.jar;lib/*



rem java com.merak.ontomap.Testa %1 %2 %3
java com.merak.ontomap.classification.StatTools

pause