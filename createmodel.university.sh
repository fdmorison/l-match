export CLASSPATH=${CLASSPATH}:ontomap.jar:lib/*

java com.merak.ontomap.CreateModel ApplicationData/ontologias/cornell_washington/cu.owl ApplicationData/ontologias/cornell_washington/wu.owl model.university
