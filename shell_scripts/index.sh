database=${1}
model=${2}

tokenizingOptions=""
#"--lex-alphanum --no-stemming --no-stoplist"

echo ${infoGainOptions}

if [ -z ${model} ]; then 
model=${database} 
fi 

if [ ${database} ]; then 
rainbow ${tokenizingOptions} -v 0 -i -d ${model}.model ${database}/*
rainbow -d ${model}.model -B=sin > ${model}.model/svmvector
fi
