// Package
///////////////
package com.merak.ontomap;

// Imports
///////////////
import java.io.*;
import com.merak.core.*;
import com.merak.core.text.*;
import com.merak.ai.classification.*;
import com.merak.ontomap.model.*;
import com.merak.ontomap.classification.*;
import com.merak.ontomap.classification.util.*;
import com.merak.ontomap.mapping.*;

public class MatchClasses {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  static MapModel          model     = null;
  static MapTable          idealSet  = null;
  static PredictionBuilder predictor = null;

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public static void main (String args[]) throws Exception {

    // Parse Shell Params
    ShellParameterSet parameters = parse(args);
    String            pIdeal     = parameters.getParameter("ideal") ;

    // Get Shell Params
    String   pModel  = parameters.getParameter("model" );
    String[] pMethod = parameters.getParameter("method").split("\\+");

    // Initialize Model and Predictor
    model     = new MapModel( new File(pModel) );
    predictor = new PredictionBuilder( model );

    // Initialize IdealSet
    if (pIdeal!=null) {
      idealSet = new MapTable( model );      
      idealSet.load(new File(pIdeal));
    }
    
    // Set Predictor's Methods
    for ( String method : pMethod ) {
      predictor.addMethod( ClassificationMethod.parseValue(method) , 1.0/pMethod.length, null);
    }

    // Start matching
    runPrediction();

  }
  //****************************************************************************
  public static void runPrediction() {

    // Predict similarities beteween classes
    TwoWayCategoryPrediction resultSet  = predictor.predictFromModel();
    predictor.close();

    // 2. Propagar dados de similaridade e overlap das subclasses para as superclasses (bottom-up)
    Propagator propagator = new Propagator();
    propagator.propagate( resultSet.predictionFromA.overlapTable        );
    propagator.propagate( resultSet.predictionFromB.overlapTable        );
    propagator.propagate( resultSet.overlapTable                        );
    propagator.propagateRows( resultSet.predictionFromA.similarityTable );    
    propagator.propagateRows( resultSet.predictionFromB.similarityTable );
    propagator.propagateRows( resultSet.similarityTable                 );

    // 3. Salvar tabelas para debug. Elas nao serao carregadas e nem usadas depois.
    DataWriter writer = new HtmlDataWriter(model,idealSet,3);
    writer.save( resultSet );

    propagator.propagateColumns( resultSet.predictionFromA.similarityTable );
    propagator.propagateColumns( resultSet.predictionFromB.similarityTable );
    propagator.propagateColumns( resultSet.similarityTable                 );
    
    // Mappear
    Mapper        lmatch      = new LMatch(model,resultSet);
    MapTable      computedSet = lmatch.start();
    MapEvaluation evaluation  = new MapEvaluation(idealSet,computedSet);   
    System.out.println( "\n" + evaluation + "\n" );    
    
    try { computedSet.save( new File( "computedSet.txt") ); } catch(Exception e) {}
    
  }
  //****************************************************************************
  public static ShellParameterSet parse(String[] args) {

    // Auxiliar
    ShellParameter    parameter    = null;
    ShellParameterSet parameterSet = new ShellParameterSet("OntoMap - OWL Ontology Classifier Module");

    // About this program
    parameterSet.setDescription("Estimates similarity between two ontologies using classification");
    parameterSet.setUsage("MatchClasses [method] <model>");

    // Specify the Method Parameter
    parameter = parameterSet.createParameter("method",false,"The classification algorithm or a combination of them.");
    parameter.setPossibleValues("svm","knn, naivebayes, svm, maxent, nbshrinkage, tfidf");
    parameter.setExample("--method=knn, --method=knn+svm+maxent");
    parameter.addShortcut('m');

    // Specify the Directory Parameter
    parameter = parameterSet.createParameter("model",false,"A directory containing the extracted data");
    parameter.setExample("--model=mymodel, -d=mymodel");
    parameter.addShortcut('d');

    // Specify the Ideal Parameter
    parameter = parameterSet.createParameter("ideal",false,"A file containing the expected and true mappings");
    parameter.setExample("--ideal=mappings.txt, -I=mappings.txt");
    parameter.addShortcut('I');

    // Set final comment
    parameterSet.setComment("Report bugs to <fabricio.dmorison@gmail.com>. Thank you :)");

    // Parse command line arguments
    parameterSet.parse(args);
    return parameterSet;

  }
  //****************************************************************************
}
