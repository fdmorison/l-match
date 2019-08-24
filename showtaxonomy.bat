@echo off

set CLASSPATH=%CLASSPATH%;ontomap.jar;lib/*



java com.merak.ontomap.ShowTaxonomy %1 %2 %3 %4 %5
