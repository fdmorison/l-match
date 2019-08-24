file=${1}
method=${2}
tokenizingOptions="--lex-alphanum --use-stemming --no-stoplist --shortest-word=3"

if [ -z ${method} ]; then
method=svm
fi

rainbow -m ${method} -v 0 -d apes.model ${tokenizingOptions} --query=${file}

#--svm-suppress-score-matrix --uniform-class-priors
