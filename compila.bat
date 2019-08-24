echo off
cls

SET APP_NAME=ontomap
SET APP_HOME=application
SET JAVADOC=false

SET SOURCE=java\com\merak\ontomap\extraction java\com\merak\ontomap\*

ECHO  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
ECHO +******************************************************************+
ECHO +******************************************************************+
ECHO +****      ***      ***  *****  **      ***  **  *********   ******+
ECHO +***   ******   **   **   ***   **  ***  **  **  ********  *  *****+
ECHO +***  *******  ****  **    *    **  ***  **  **  *******  ***  ****+
ECHO +***  *******  ****  **  *   *  **      ***  **  ******         ***+
ECHO +***   ******   **   **  ** **  **  *******  **  ******   ***   ***+
ECHO +****      ***      ***  *****  **  *******  **      **   ***   ***+
ECHO +******************************************************************+
ECHO +******************************************************************+
ECHO +* Merak Computing, by Fabricio DMorison :)                       *+
ECHO +******************************************************************+
ECHO +******************************************************************+
ECHO +++++++++++++++----------------------------------------------------+
ECHO ++++++++++++++++++++++++-------------------------------------------+
ECHO ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
echo * 
echo * PREPARANDO...

IF EXIST bin (
 rd /S/Q bin
)
mkdir bin

echo * COMPILANDO ARQUIVOS JAVA...
javac -encoding latin1 -extdirs lib -d bin -sourcepath java java\com\merak\ontomap\*.java
IF ERRORLEVEL 1 goto erro
echo *

echo * EMPACOTANDO EM %APP_NAME%.jar
jar cf %APP_NAME%.jar -C bin ./
IF ERRORLEVEL 1 goto erro
echo *

if %JAVADOC%==true (
 echo * CRIANDO JAVADOCS ...
 javadoc -locale pt_BR -quiet -d javadocs java/*.java
 IF ERRORLEVEL 1 goto erro
)

goto exit

:erro
echo *
echo * ABORTANDO DEVIDO A ERROS DURANTE A COMPILACAO.
echo *
pause

:exit
