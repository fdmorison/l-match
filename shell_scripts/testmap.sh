shopt -s nullglob

db1="db/ecolingua.stemmer.rdftype.db"
db2="db/apes.stemmer.rdftype.db"

class1=${1} 
class2=${2} 
method=${3}

prefix1=${db1////-}-${class1}
prefix2=${db2////-}-${class2}

if [ ! -d ${db1}/${class1} ]; then
echo Diretório ${db1}/${class1} não existe
exit
fi

if [ ! -d ${db2}/${class2} ]; then
echo Diretório ${db2}/${class2} não existe
exit
fi

if [ -z ${method} ]; then
method="svm"
fi

if [ ! -d saida ]; then
mkdir saida
fi

#INDEXANDO ONTOLOGIAS
./index.sh ${db1} saida/db1
./index.sh ${db2} saida/db2

# PARA CADA INSTANCIA DE DB1/CLASS1, CLASSIFICAR INSTANCIA EM DB2
for name in ${db1}/${class1}/*; do
rainbow -m ${method} -v 0 -d saida/db2.model --query=${name} > saida/${name////-}
done

# PARA CADA INSTANCIA DE DB2/CLASS2, CLASSIFICAR INSTANCIA EM DB1
for name in ${db2}/${class2}/*; do
rainbow -m ${method} -v 0 -d saida/db1.model --query=${name} > saida/${name////-}
done

# PARA CADA CLASS, COMBINAR CLASSIFICAÇÃO DE SUAS INSTANCIAS
/opt/jdk1.6.0_02/bin/java -cp ontomap.jar com.merak.ontomap.rainbow.RainbowQueryOutput saida/${prefix1}-*.txt > saida/${prefix1}.txt
/opt/jdk1.6.0_02/bin/java -cp ontomap.jar com.merak.ontomap.rainbow.RainbowQueryOutput saida/${prefix2}-*.txt > saida/${prefix2}.txt

gedit saida/${prefix1}.txt saida/${prefix2}.txt &
