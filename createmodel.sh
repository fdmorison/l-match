o1=${1}
o2=${2}
model=${3}

export CLASSPATH=${CLASSPATH}:ontomap.jar:lib/*

java com.merak.ontomap.CreateModel ${o1} ${o2} ${model}