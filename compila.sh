shopt -s nullglob
clear

APP_HOME=application
APP_NAME=ontomap
JAVADOC=false
TOMCAT=false

printf "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n"
printf "+******************************************************************+\n"
printf "+******************************************************************+\n"
printf "+****      ***      ***  *****  **      ***  **  *********   ******+\n"
printf "+***   ******   **   **   ***   **  ***  **  **  ********  *  *****+\n"
printf "+***  *******  ****  **    *    **  ***  **  **  *******  ***  ****+\n"
printf "+***  *******  ****  **  *   *  **      ***  **  ******         ***+\n"
printf "+***   ******   **   **  ** **  **  *******  **  ******   ***   ***+\n"
printf "+****      ***      ***  *****  **  *******  **      **   ***   ***+\n"
printf "+******************************************************************+\n"
printf "+******************************************************************+\n"
printf "+* Merak Computing, by Fabricio D'Morison :)                      *+\n"
printf "+******************************************************************+\n"
printf "+******************************************************************+\n"
printf "+++++++++++++++----------------------------------------------------+\n"
printf "++++++++++++++++++++++++-------------------------------------------+\n"
printf "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n"
printf "*\n"
printf "* PREPARANDO DIRETÓRIOS...\n"

if [ ! -d bin ]; then
mkdir bin
fi

printf "* COMPILANDO ARQUIVOS JAVA...\n"
javac -encoding latin1 -extdirs lib -d bin -sourcepath java java/com/merak/ontomap/*.java
printf "*\n"

printf "* CRIANDO JAR: ${APP_NAME}.jar\n"
jar cf ${APP_NAME}.jar -C bin ./
printf "*\n"

#if [ -d javadocs ]; then
#rm -r javadocs
#fi
#mkdir javadocs
