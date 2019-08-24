// Package
///////////////
package com.merak.ontomap.classification;

// Imports
///////////////
import com.merak.ontomap.model.Ontology;
import com.merak.ontomap.model.Category;
import com.merak.ontomap.classification.util.IntegerTable;
import com.merak.ontomap.classification.util.DoubleTable;
import com.merak.ontomap.classification.util.DataWriter;

// Class
///////////////
public class OneWayCategoryPrediction {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  public final Ontology     sourceOntology;
  public final Ontology     targetOntology;
  public final IntegerTable overlapTable;
  public final DoubleTable  similarityTable;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  protected OneWayCategoryPrediction(Ontology sourceOntology,Ontology targetOntology,double relativeThreshold) {

    // Attribute Initialization
    this.sourceOntology  = sourceOntology;
    this.targetOntology  = targetOntology;
    this.overlapTable    = new IntegerTable(sourceOntology,targetOntology);
    this.similarityTable = new DoubleTable(sourceOntology,targetOntology,relativeThreshold);

  }
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private void sourceRecursivePropagation(Category category) {

    // Se a categoria 'parent' for uma folha, nao ha como propagar dados de subclasses
    if (category.isLeaf()) return;

    // 1. PROPAGAR SIMILARIDADE E INTERCECAO DOS DESCENDENTES PARA AS SUBCLASSES
    // As categorias mais profundas devem ser alcancadas primeiro,
    // de forma a garantir que a propagacao seja no sentido 'bottom-up'
    for (Category child : category.getChildren()) {
      sourceRecursivePropagation(child);
    }

    // Auxiliar
    Category child        = null;
    double[] similarities = new double[ 1 + category.getNumberOfChildren() ];
    int      overlap      = 0;
    double   similarity   = 0;

    // 2. PROPAGAR SIMILARIDADE E INTERSECAO DAS SUBCLASSES PARA A CATEGORIA
    // Para cada categoria de treino,
    for (Category target : targetOntology) {
      similarities[0] = similarityTable.get(category, target);
      overlap         = 0;
      // Para cada subclasse de 'source',
      for (int i=1; i<similarities.length; i++) {
        child           = category.getChild(i-1);
        overlap        += overlapTable.get(child,target);
        similarities[i] = similarityTable.get(child,target);
      }
      // overlap eh propagado atraves de soma
      overlapTable.sum(category, target, overlap);
      // similaridade eh propagada atraves de noisyOr
      similarity = StatTools.getNoisyOr(similarities);
      similarity*= StatTools.getEntropy(similarities,1);
      similarityTable.set(category, target, similarity);
    }

  }
  //****************************************************************************
  private void targetRecursivePropagation(Category category) {

    // Se a categoria 'parent' for uma folha, nao ha como propagar dados de subclasses
    if (category.isLeaf()) return;

    // 1. PROPAGAR SIMILARIDADE E INTERCECAO DOS DESCENDENTES PARA AS SUBCLASSES
    // As categorias mais profundas devem ser alcancadas primeiro,
    // de forma a garantir que a propagacao seja no sentido 'bottom-up'
    for (Category child : category.getChildren()) {
      targetRecursivePropagation(child);
    }

        // Auxiliar
    Category child        = null;
    double[] similarities = new double[ 1 + category.getNumberOfChildren() ];
    int      overlap      = 0;
    double   similarity   = 0;

    // 2. PROPAGAR SIMILARIDADE E INTERSECAO DAS SUBCLASSES PARA A CATEGORIA
    // Para cada categoria de treino,
    for (Category source : sourceOntology) {
      if ( similarityTable.get(source,category) > 0 ) continue;
      similarities[0] = similarityTable.get(source,category);
      overlap         = overlapTable.get(source,category);
      // Para cada subclasse de 'source',
      for (int i=1; i<similarities.length; i++) {
        child           = category.getChild(i-1);
        overlap        += overlapTable.get(source,child);
        similarities[i] = similarityTable.get(source,child);
      }
      // similaridade eh propagada atraves de noisyOr
      similarity = StatTools.getNoisyOr(similarities);
      similarity*= StatTools.getEntropy(similarities,1);
      similarityTable.set(source, category, similarity);
      // overlap eh propagado atraves de soma
      overlapTable.set(source, category, overlap);
    }

  }
  //****************************************************************************
  public void propagate() {

    for (Category root : sourceOntology.getRootCategories()) {
      sourceRecursivePropagation(root);
    }
/*
    for (Category root : targetOntology.getRootCategories()) {
      targetRecursivePropagation(root);
    }
  */
  }
  //****************************************************************************
}

