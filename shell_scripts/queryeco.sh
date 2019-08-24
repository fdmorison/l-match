tokenizingOptions="--lex-alphanum --use-stemming --no-stoplist --shortest-word=3"

method=${1}
if [ -z ${method} ]; then
method=svm
fi

for (( i=2; i<${#*}; i++ )); do 
rainbow -m ${method} -v 0 -d eco.model ${tokenizingOptions} --query=${!i}
done

#--svm-suppress-score-matrix --uniform-class-priors
