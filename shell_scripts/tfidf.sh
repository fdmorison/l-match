model=${1}


if [ ${model} ]; then 

rainbow -t 1 -m tfidf --uniform-class-priors --test-set=0.2 -d ${model} > saida.txt
rainbow-stats saida.txt < saida.txt > stats.txt

cat stats.txt

fi
