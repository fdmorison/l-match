// Package
///////////////
package com.merak.ontomap.classification;

// Imports
///////////////
import java.io.*;
import java.net.URL;
import java.util.*;
import com.merak.core.*;
import com.merak.ai.classification.*;
import com.merak.ai.classification.rainbow.*;
import com.merak.ontomap.model.*;
import com.merak.ontomap.classification.util.*;
import com.merak.ontomap.mapping.MapTable;

// Class
///////////////
public class PredictionBuilder implements Closeable {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private MapModel           model      = null;
  private MapTable           idealSet   = null;
  private Classifier         classifier = null;
  private List<MethodBelief> methods    = new LinkedList();

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public PredictionBuilder(MapModel model)
    throws ClassificationException
  {

    // Attribute Initialization
    this(model,new RainbowServer(),1);

  }
  //****************************************************************************
  public PredictionBuilder(MapModel model,Classifier classifier,int overlapDepth)
    throws ClassificationException
  {

    // Attribute Initialization
    this.model      = model;
    this.classifier = classifier;

    // Criar um diretorio no modelo para salvar as estatisticas que vao ser geradas
    new File( model.getPath()+"/stats" ).mkdir();

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  /*
   * +---------------------------+
   * |       CLASSIFICACAO       |
   * +---------------------------+
   *
   *       InstancePrediction
   *         by some method
   *     _______________________
   *    |%%%%%%% test.c? %%%%%%|
   *    +--------+-------------|
   *    |   T1   |i1   i2   i3 |
   *    +--------+-------------|
   *    |train.c1|p11  p12  p13|
   * b1*|train.c2|p21  p22  p23|
   *    |train.c3|p31  p32  p33|
   *    |train.c4|p41  p42  p43|
   *    +--------+-------------+
   *    > b1 = belief in the method
   */
  private void classify(Ontology testSet,Ontology trainSet,MethodBelief mb,InstancePrediction[] resultSet,int n)
    throws Exception
  {

    // Auxiliar
    URL                 location   = new URL( "file",null,trainSet.getParameter("rainbowModel") );
    Instance            instance   = null;
    Rank<String,String> rank       = null;
    int                 instanceId = 0;    // id de instancia no escopo de categoria

    // Configure o classificador para usar determinado algoritmo sobre o treino
    classifier.setTrainParams(mb.method,location,mb.params);

    // Para cada categoria da base de teste, fa�a
    for (Category category : testSet) {
      // Para cada instancia da categoria de teste, fa�a
      for (instanceId=0; instanceId<category.getNumberOfDirectInstances(); instanceId++) {
        // Classifique a instancia de teste
        instance = category.getDirectInstance(instanceId);
        rank     = classifier.query( model.getTextOf(instance) );
        // Verifique se a classificacao gerou resultado para a instancia
        if (rank==null) {
          //System.err.println("AVISO: Nada foi recebido sobre a classificacao de '"+instance.getName()+"'!"); 
          continue;
        }
        // Combine o resultado da classicacao no ResultSet correspondente
        rank.setQuery( instance.getName() );
        resultSet[category.getOntologicalId()].combine(instanceId,rank,mb.belief,n);
      }
    }

  }
  //****************************************************************************
  /*
   * +------------------------------------------------------------------------------------+
   * |                                     COMBINAC�O DE CLASSIFICADORES                  |
   * +------------------------------------------------------------------------------------+
   *
   *       InstancePrediction            InstancePrediction         InstancePrediction
   *            by SVM                      by NaiveBayes        by classifier combination
   *     _______________________       ______________________     ______________________
   *    |%%%%%%% test.c? %%%%%%|      |%%%%%%% test.c? %%%%%%|   |%%%%%%% test.c? %%%%%%|
   *    +--------+-------------|      +--------+-------------|   +--------+-------------|
   *    |   T1   |i1   i2   i3 |      |   T2   |i1   i2   i3 |   | T1+T2  |i1   i2   i3 |
   *    +--------+-------------|      +--------+-------------|   +--------+-------------|
   *    |train.c1|p11  p12  p13|      |train.c1|p11  p12  p13|   |train.c1|p11  p12  p13|
   * b1*|train.c2|p21  p22  p23| + b2*|train.c2|p21  p22  p23| = |train.c2|p21  p22  p23|
   *    |train.c3|p31  p32  p33|      |train.c3|p31  p32  p33|   |train.c3|p31  p32  p33|
   *    |train.c4|p41  p42  p43|      |train.c4|p41  p42  p43|   |train.c4|p41  p42  p43|
   *    +--------+-------------+      +--------+-------------+   +--------+-------------+
   *    > b1 = belief in SVM          > b2 = belief in NaiveBayes
   */
  private InstancePrediction[] classify(Ontology testSet,Ontology trainSet)
    throws Exception
  {
    // Auxiliar
    InstancePrediction[] resultSet        = new InstancePrediction[ testSet.getNumberOfCategories() ];
    boolean              useDefaultMethod = methods.isEmpty();
    int n = 0;

    // 1) Aloque uma tabela (por categoria de teste) para guardar os resultados da classificacao
    for (Category category : testSet) {
      resultSet[ category.getOntologicalId() ] = new InstancePrediction( category, trainSet, methods.size() );
    }

    // 2) Para cada metodo de classificacao,
    if (useDefaultMethod) addMethod(classifier.getDefaultMethod(),1.0,null);
    for ( MethodBelief methodBelief : methods ) {
      // Classify the TestSet against the TrainSet, using a method and the belief in such method
      // and store the results (for each instance grouped by category) into ResultSet
      System.err.print("CLASSIFY "+testSet.getName()+" INTO "+trainSet.getName()+" USING METHOD='"+methodBelief.method+"' AND BELIEF="+methodBelief.belief);
      classify(testSet,trainSet,methodBelief,resultSet,n++);
      System.err.println(" (DONE)");
    }
    if (useDefaultMethod) methods.clear();

    // 3) Return the result set of the classification step
    return resultSet;

  }
  //****************************************************************************
  /*
   * +-------------------------------------------------------------------------+
   * |                         COMBINAC�O DE INST�NCIAS                        |
   * +-------------------------------------------------------------------------+
   *
   *       InstancePrediction                 CategoryPrediction
   *    by classifier combination          by instance combination
   *     ______________________            ________________________
   *    |%%%%%%% test.c? %%%%%%|          |%%%%%%% test.c? %%%%%%%%|
   *    +--------+-------------|          +--------+---------------|
   *    | T1+T2  |i1   i2   i3 |          |   TF   |    test.c?    |
   *    +--------+-------------|          +--------+---------------|
   *    |train.c1|p11  p12  p13| combine  |train.c1|p11 + p12 + p13| * spread[train.c1]
   *    |train.c2|p21  p22  p23| -------> |train.c2|p21 + p22.+ p23| * spread[train.c2]
   *    |train.c3|p31  p32  p33|          |train.c3|p31 + p32 + p33| * spread[train.c2]
   *    |train.c4|p41  p42  p43|          |train.c4|p41 + p42 + p43| * spread[train.c2]
   *    +--------+-------------+          +--------+---------------+
   *    > b1 = belief in SVM              > b2 = belief in NaiveBayes
   */
  private OneWayCategoryPrediction compareCategories(Ontology testSet,Ontology trainSet,int overlapDepth)
    throws Exception
  {

    // Auxiliar
    InstancePrediction[]     instancePredictions = classify(testSet,trainSet);
    OneWayCategoryPrediction categoryPrediction  = new OneWayCategoryPrediction(testSet,trainSet,0.7);

    // Calcular SIMILARIDADE e INTERCECAO entre as classes de TESTE e de TREINO
    for (InstancePrediction instancePrediction : instancePredictions) {
      // Combinar classificacao das intancias numa medida de similaridade entre categorias
      instancePrediction.predict(categoryPrediction,overlapDepth);
    }

    // Return predictions for categories
    return categoryPrediction;

  }
  //****************************************************************************/
  public void setIdealSet(MapTable idealSet) {

    this.idealSet = idealSet;

  }
  //****************************************************************************/
  public void addMethod(ClassificationMethod method,double belief,Map extraParams)
    throws PredictionException
  {

    // Check for invalid method
    if (!classifier.supports(method)) {
      throw new PredictionException("Classifier '"+classifier.getName()+"' doesn't support the method '"+method+"'");
    }

    // Store method params
    methods.add(  new MethodBelief(method,belief,extraParams)  );

  }
  //****************************************************************************/
  public TwoWayCategoryPrediction predictFromModel() {

    // Auxiliar
    boolean useDefaultMethod = methods.isEmpty();

    try {
      // 0. Preparar
      if (useDefaultMethod) addMethod(classifier.getDefaultMethod(),1.0,null);
      else                  Collections.sort(methods);

      // 1. Classificar ontologias nas duas direcoes e, em seguida, combinar numa tabela soh
      OneWayCategoryPrediction fromA = compareCategories( model.ontologyA, model.ontologyB, 1 ); // ontologiaA --> ontologiaB
      OneWayCategoryPrediction fromB = compareCategories( model.ontologyB, model.ontologyA, 1 ); // ontologiaA <-- ontologiaB
      TwoWayCategoryPrediction AandB = new TwoWayCategoryPrediction(fromA,fromB);

      // 4. Finalizar
      if (useDefaultMethod) methods.clear();
      return AandB;
    }
    catch(Exception ex) {
      Application.debug.print("Predictor.predictFromModel(int)",ex);
    }

    // Return null if any error
    return null;

  }
  //****************************************************************************
  public void close() {

    classifier.close();

  }
  //****************************************************************************
}
