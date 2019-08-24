export CLASSPATH=${CLASSPATH}:ontomap.jar:lib/*

java com.merak.ontomap.CreateModel ApplicationData/ontologias/eco_apes/Ecolingua.owl ApplicationData/ontologias/unit-individuals-2.0.owl model.ecounit
