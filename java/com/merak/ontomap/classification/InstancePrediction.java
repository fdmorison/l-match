// Package
/////////////////
package com.merak.ontomap.classification;

// Imports
/////////////////
import java.util.*;
import java.text.NumberFormat;
import com.merak.core.text.*;
import com.merak.core.DoubleParameter;
import com.merak.ai.classification.*;
import com.merak.ontomap.model.*;

// Implementation
/////////////////
public class InstancePrediction {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final Category category;                  // Categoria de Teste
  private final Ontology trainSet;                  // Base de Treino
  private final double   instancePrediction[][][];  // Resultado da classificacao para as instancias da categoria de teste

  //~ Constructors /////////////////////////////////////////////////////////////
  //*******************************************NumberFormat*********************************
  public InstancePrediction(Category testCategory,Ontology trainSet,int n) {

    // Auxiliar
    int rows = trainSet.getNumberOfCategories();
    int cols = testCategory.getNumberOfDirectInstances();

    // Attributes Initialization
    this.category           = testCategory;
    this.trainSet           = trainSet;
    this.instancePrediction = new double[rows][cols][n];
/*
    // Fill the table
    for (int i=0; i<rows; i++) {
      for (int j=0; j<cols; j++) instancePrediction[i][j] = 1;
    }
*/
  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public Category getCategory() {

    return category;

  }
  //****************************************************************************
  public Ontology getTrainSet() {

    return trainSet;

  }
  //****************************************************************************
  public double getSimilarity(Category trainCategory,int instanceCategoricalId) {

    if (!trainSet.contains(trainCategory)) {
        throw new RuntimeException("Category '"+trainCategory.getName()+"' isnt contained in ontology '"+trainSet.getName()+"'");
    }
    
    double[] array = instancePrediction[trainCategory.getOntologicalId()][instanceCategoricalId];
    return StatTools.getEntropy(array) * StatTools.getNoisyOr(array);
    
    //return 1.0 - instancePrediction[trainCategory.getOntologicalId()][instanceCategoricalId];

  }
  //****************************************************************************
  /* Organize the similarities from a rank of categories into an matrix whose
   * rows are indexed by CategoryId and columns by InstanceId.
   * @param rank the resulting rank of an instance classification
   */
  public void combine(int instanceId, Rank<String,String> rank,double belief,int n) {

    if ( rank.isEmpty() ) return;
      
    // Auxiliar
    double   threshold = 0.2 * rank.getPriority(0);
    int      categoryId;
    String   categoryName;
    Category trainCategory;

    // Index the ranked values into an column of the similarity matrix (each column is an instance,each line is a category)
    for (int i=0; i<rank.length() && rank.getPriority(i)>=threshold ; i++) {
      // Identify the ranked category from the train set
      categoryName  = rank.getEntity(i);
      categoryId    = Integer.parseInt(categoryName.split("_")[0]);
      trainCategory = trainSet.getModel().getCategory(categoryId);
      // Store the classification similarity(instance,train_category)
      instancePrediction[trainCategory.getOntologicalId()][instanceId][n] = belief*rank.getPriority(i);
      //instancePrediction[trainCategory.getOntologicalId()][instanceId] *= (1.0d - belief*rank.getPriority(i));
      //instancePrediction[trainCategory.getOntologicalId()][instanceId] *= (1.0d - belief*rank.getPriority(i));
    }

  }
  //****************************************************************************
  /* Calculate the category predictions based in the instance predictions
   * @returns an array indexed with category ids
   */
  public void predict(OneWayCategoryPrediction resultSet,int overlapDepth)
    throws Exception
  {
   
    if ( category.isAbstract() ) return;     
      
    // Auxiliar
    Category[] trainCategories   = trainSet.getConcreteCategories();
    int        numberOfInstances = category.getNumberOfDirectInstances();
    double[]   similarities      = new double[numberOfInstances];
    double     similarity1;
    double     similarity2;
    double     similarity;    
    int        overlapK = 2;
    int        k;

    // ETAPA 2: COMPUTAR A INTERSECAO ENTRE CATEGORIA DE TESTE COM CADA CATEGORIA DE TREINO
    // Para cada instancia da categoria de teste
    InstanceRank rank = new InstanceRank(trainCategories.length);
    for (int i=0; i<numberOfInstances; i++) {
      // Rankear classe de treino por similaridade com a instancia de teste
      rank = rankInstance(i,rank);  
      //rank. pruneWithRelativeThreshold(0.80);
      
      if (rank.isEmpty() || rank.getPriority(0)==0) continue;
      // Incrementar intersecao com a primeira classe do rank (k=1)  
      
      //k = (overlapK > rank.length()) ? rank.length() : overlapK ;    
      //for (int j=0; j<k && rank.getPriority(j)>0; j++) {
      //	resultSet.overlapTable.sum(category, rank.getEntity(j), 1);   
      //}
                
      resultSet.overlapTable.sum(category, rank.getEntity(0), 1);     
    }
    
    // ETAPA 1: COMPUTAR SIMILARIDADE ENTRE CATEGORIA DE TESTE COM CADA CATEGORIA DE TREINO
    // Para cada categoria de treino
    for (Category trainCategory : trainCategories) {
      // Finalizar similaridades das instancias com trainCategory
      for (int i=0; i<numberOfInstances; i++) {
        similarities[i] = getSimilarity(trainCategory,i);
      }
      // Combinar similaridade de cada instancia
      similarity1  = resultSet.overlapTable.get(category,trainCategory) / (double) category.getNumberOfDirectInstances();
      similarity2  = StatTools.getNoisyOr(similarities);  
      similarity   = 1 - (1-similarity1)*(1-similarity2);
      similarity  *= StatTools.getEntropy(similarities);    
      resultSet.similarityTable.set(category,trainCategory,similarity);
    }

  }
  //****************************************************************************
  public InstanceRank rankInstance(int instanceCategoricalId) {

    return rankInstance(instanceCategoricalId,new InstanceRank(trainSet.getNumberOfConcreteCategories()));

  }
  //****************************************************************************
  public InstanceRank rankInstance(int instanceCategoricalId,InstanceRank rank) {

    // Auxiliar
    Category[] trainCategories = trainSet.getConcreteCategories();
    Instance   instance        = category.getDirectInstance(instanceCategoricalId);
    double     similarity      = 0;

    // Assegurar o tamanho do rank
    rank.setQuery(instance);
    rank.setLength(trainCategories.length);

    // Para cada categoria concreta da Base de Treino, faca
    for (int i=0; i<trainCategories.length; i++) {
      similarity = getSimilarity(trainCategories[i],instanceCategoricalId);
      rank.setEntry(i, trainCategories[i], similarity);
    }
    rank.sort();
    return rank;

  }
  //****************************************************************************
  public String toString() {

    // Auxiliar
    NumberFormat nf     = NumberFormat.getInstance();
    RenderTable  render = new RenderTable();
    InstanceRank rank   = null;
    Row          row    = null;

    // Rendering
    // a) configure number format to render probabilities
    nf.setMaximumFractionDigits(3);
    nf.setMinimumFractionDigits(3);

    // b) for each instance,
    for (int instanceId=0; instanceId<category.getNumberOfDirectInstances(); instanceId++) {
      // rank the instanced train categories
      rank = rankInstance(instanceId);
      rank.pruneWithRelativeThreshold(0.8);
      // create a line for the rank in the render table
      row  = render.createRow();
      row.createCell( ">" + rank.getQuery() );
      for (Rank.Pair entry : rank) {
        row.createCell( nf.format(entry.priority)+" "+entry.entity );
      }
    }
    return render.toString();

  }
  //****************************************************************************

}








