// Package
///////////////
package com.merak.ontomap;

// Imports
///////////////
import java.io.*;
import java.util.*;

import com.merak.core.*;
import com.merak.ai.classification.*;

import com.merak.ontomap.*;
import com.merak.ontomap.model.*;
import com.merak.ontomap.classification.*;
import com.merak.ontomap.classification.util.*;
import com.merak.ontomap.mapping.*;

public class Testa {

  public static void main(String[] args) throws Exception {

    MapModel model = new MapModel(new File("model.eco+apes"));
    MapTable table = new MapTable(model.ontologyA,model.ontologyB);

    //table.load( new File("eco.to.apes.evaluation.test.txt") );
    //System.out.println(table);

    Evaluator obj = new Evaluator(table);

/*
    MapEvaluation eval  = new MapEvaluation(table,table);
    System.out.println("Accuracy  = "+eval.getAccuracy());
    System.out.println("Precision = "+eval.getPrecision());
    System.out.println("Recovery  = "+eval.getRecovery());
    System.out.println();
    for (Relation relation : Relation.values()) {
      System.out.println(relation+" Precision = "+eval.getPrecision(relation));
      System.out.println(relation+" Recovery  = "+eval.getRecovery(relation));
    }
*/
  }

  public static void testaShellParameter(String[] args) throws Exception {

    // Auxiliar
    ShellParameter    parameter    = null;
    ShellParameterSet parameterSet = new ShellParameterSet("OntoMap - OWL Ontology Data Extractor Module");

    // About this program
    parameterSet.setDescription("Prepare (extract, index, etc) data for Ontology Matching");
    parameterSet.setUsage("CreateModel [options] <ontologyA.owl> <ontologyB.owl>");

    // Specify the Method Parameter
    parameter = parameterSet.createParameter("method",false,"The classification algorithm or a combination of them.");
    parameter.setPossibleValues("svm","knn, naivebayes, svm, maxent");
    parameter.setExample("--method=knn, --method=knn+svm+maxent");
    parameter.addShortcut('m');

    // Specify the Directory Parameter
    parameter = parameterSet.createParameter("model",false,"A directory path to store the extracted data");
    parameter.setPossibleValues("The directory \"model.ontologyA+ontologyB\"",null);
    parameter.setExample("--model=mymodel, -d=mymodel");
    parameter.addShortcut('d');

    // Specify boolean parameters
    parameter = parameterSet.createParameter("no-synonyms" ,true,"Skips Synonym Reduction Step");
    parameter = parameterSet.createParameter("no-prefixes" ,true,"Skips Word Prefixing");
    parameter = parameterSet.createParameter("no-stopwords",true,"Skips Stopword Cleaning");

    // Set final comment
    parameterSet.setComment("Report bugs to <fabricio.dmorison@gmail.com>. Thank you :)");

    // Parse command line arguments
    parameterSet.parse(args);

  }

}
