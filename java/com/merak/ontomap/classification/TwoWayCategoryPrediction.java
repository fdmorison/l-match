// Package
///////////////
package com.merak.ontomap.classification;

// Imports
///////////////
import com.merak.ontomap.model.*;
import com.merak.ontomap.classification.util.DoubleTable;
import com.merak.ontomap.classification.util.IntegerTable;
import com.merak.ai.classification.Rank;

// Class
///////////////
public class TwoWayCategoryPrediction {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  public final Ontology                 ontologyA;
  public final Ontology                 ontologyB;
  public final OneWayCategoryPrediction predictionFromA;
  public final OneWayCategoryPrediction predictionFromB;
  public final DoubleTable              similarityTable;
  public final IntegerTable             overlapTable;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  protected TwoWayCategoryPrediction(OneWayCategoryPrediction predictionFromA,OneWayCategoryPrediction predictionFromB) {

    // Attribute Initialization
    this.ontologyA       = predictionFromA.sourceOntology;
    this.ontologyB       = predictionFromB.sourceOntology;
    this.predictionFromA = predictionFromA;
    this.predictionFromB = predictionFromB;
    this.similarityTable = new DoubleTable( ontologyA, ontologyB, predictionFromA.similarityTable.relativeThreshold );
    this.overlapTable    = new IntegerTable( ontologyA, ontologyB );

    // Check potential error
    if (!predictionFromA.overlapTable.isTranspose(predictionFromB.overlapTable)) {
      throw new RuntimeException("Tabelas nao sao transpostas");
    }

    // Combine predicition tables
    combine(predictionFromA,predictionFromB);

  }
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private void combine(OneWayCategoryPrediction predictionFromA,OneWayCategoryPrediction predictionFromB) {

    // Auxiliar
    double[] similarities = new double[2];
    double   similarity   = 0;
    double   sum          = 0;

    // Combine
    for (Category a : ontologyA) {
      for (Category b : ontologyB) {
        // Recuperar similaridades computadas nos 2 sentidos
        similarities[0] = predictionFromA.similarityTable.get(a,b);
        similarities[1] = predictionFromB.similarityTable.get(b,a);

        // Combinando similaridades
        sum         = similarities[1] + similarities[0];
        similarity  = sum==0 ? 0 : (2 * similarities[1] * similarities[0]) / sum;
        similarity *= StatTools.getEntropy(similarities);
                  
        // Guardando similaridade combinada em nova tabela
        similarityTable.set(a,b,similarity);
        overlapTable.set(a,b, predictionFromA.overlapTable.get(a,b) + predictionFromB.overlapTable.get(b,a)   );
      }
    }

  }
  //****************************************************************************
  public double getOverlap(Category x,Category y) {

    double total = x.getNumberOfAllInstances();
    return (total==0) ? 0 : getAbsoluteOverlap(x,y) / total;

  }
  //****************************************************************************
  public int getAbsoluteOverlap(Category x,Category y) {
    
    // Retornando valor de overlap
    if ( ontologyA.contains(x) ) return predictionFromA.overlapTable.get(x,y);
    if ( ontologyB.contains(x) ) return predictionFromB.overlapTable.get(x,y);

    // Descrevendo erro: x nao esta contido em nenhuma das ontologias conhecidas
    throw new RuntimeException("Categoria desconhecida: '"+x+"'");

  }  
  //****************************************************************************
  public double getSimilarity(Category x,Category y) {

    // Retornando valor de overlap
    if ( ontologyA.contains(x) ) return predictionFromA.similarityTable.get(x,y);
    if ( ontologyB.contains(x) ) return predictionFromB.similarityTable.get(x,y);

    // Descrevendo erro: x nao esta contido em nenhuma das ontologias conhecidas
    throw new RuntimeException("Categoria desconhecida: '"+x+"'");

  }
  //****************************************************************************
  public double getTwoWaySimilarity(Category x,Category y) {

    // Retornando valor de overlap
    if ( ontologyA.contains(x) ) return similarityTable.get(x,y);
    if ( ontologyB.contains(x) ) return similarityTable.get(y,x);

    // Descrevendo erro: x nao esta contido em nenhuma das ontologias conhecidas
    throw new RuntimeException("Categoria desconhecida: '"+x+"'");

  }  
  //****************************************************************************
  public double getTwoWayOverlap(Category x,Category y) {

    double total = x.getNumberOfAllInstances() + y.getNumberOfAllInstances();    
    return (total==0) ? 0 : getAbsoluteTwoWayOverlap(x,y) / total;

  }      
  //****************************************************************************
  public int getAbsoluteTwoWayOverlap(Category x,Category y) {

    // Retornando valor de overlap
    if ( ontologyA.contains(x) ) return predictionFromA.overlapTable.get(x,y) + predictionFromB.overlapTable.get(y,x);
    if ( ontologyB.contains(x) ) return predictionFromA.overlapTable.get(y,x) + predictionFromB.overlapTable.get(x,y);

    // Descrevendo erro: x nao esta contido em nenhuma das ontologias conhecidas
    throw new RuntimeException("Categoria desconhecida: '"+x+"'");

  }    
  //****************************************************************************
  public CategoryRank rankSimilarity(Category x) {

    // Ranquear classes similares a classe x
    return similarityTable.toRank(x);

  }
  //****************************************************************************
  public CategoryRank rankOverlap(Category x) {

    // Ranquear classes que se sobrepoem a classe x
    if ( ontologyA.contains(x) ) return predictionFromA.overlapTable.toRank(x);
    if ( ontologyB.contains(x) ) return predictionFromB.overlapTable.toRank(x);

    // Descrevendo erro: x nao esta contido em nenhuma das ontologias conhecidas
    throw new RuntimeException("Categoria desconhecida: '"+x+"'");

  }
  //****************************************************************************
}

