model=${1}
method=${2}
ideal=${3}

java -cp ontomap.jar:lib/* com.merak.ontomap.MatchClasses --model ${model} -m ${method} ${3} ${4}
#gedit 1-to-2.txt &
#gedit 2-to-1.txt &
