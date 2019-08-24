model=${1}


if [ ${model} ]; then 

rainbow -t 1 -m naivebayes --test-set=0.2 --lex-alphanum -d ${model} > saida.txt
rainbow-stats saida.txt < saida.txt > stats.txt

cat stats.txt

fi
