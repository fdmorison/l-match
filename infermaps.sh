export CLASSPATH=${CLASSPATH}:ontomap.jar:lib/*
model=${1}
mapfile=${2}

java com.merak.ontomap.InferAllMap ${model} ${mapfile}
