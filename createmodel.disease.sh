export CLASSPATH=${CLASSPATH}:ontomap.jar:lib/*

java com.merak.ontomap.CreateModel ApplicationData/ontologias/disease/disease1.owl ApplicationData/ontologias/disease/disease2.owl model.disease computedSet.txt
