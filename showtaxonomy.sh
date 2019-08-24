export CLASSPATH=${CLASSPATH}:ontomap.jar:lib/*
model=${1}
ontology=${2}

java com.merak.ontomap.ShowTaxonomy -d ${model} ${ontology}
