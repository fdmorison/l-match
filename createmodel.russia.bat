@echo off

SET CLASSPATH=%CLASSPATH%;ontomap.jar;lib/*



java com.merak.ontomap.CreateModel ApplicationData/ontologias/russia/russia1.owl ApplicationData/ontologias/russia/russia2.owl model.russia > saida.txt

pause