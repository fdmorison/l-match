// Package
///////////////
package com.merak.ontomap.classification;

// Imports
///////////////
import java.util.*;
import com.merak.ontomap.Evaluation;
import com.merak.ontomap.model.*;
import com.merak.ontomap.mapping.*;

// Class
///////////////
public class Evaluator {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private MapTable      idealSet    = null;
  private int[]         idealCount  = null;  
  private Set<Category> computedSet = new HashSet<Category>(256);

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public Evaluator(MapTable idealSet) {

    // Attribute Initialization
    this.idealSet = idealSet;

  }
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private Evaluation evaluateStrongOverlap(CategoryRank rank) {

    // Auxiliar
    Category query             = rank.getQuery();
    Relation idealMapping      = null;
    int      idealComputedSize = 0;
    int      idealSize;

    // 0. Manter no rank classes mais especificas
    List<Category> returned = rank.selectLessGeneral();
    
    // 1. Calcular tamanho do conjunto Ideal
    idealSize  = idealCount[Relation.OVERLAPPING.ordinal()];
    idealSize += idealCount[Relation.LESS_GENERAL.ordinal()];
    idealSize += idealCount[Relation.MORE_GENERAL.ordinal()];
    idealSize += idealCount[Relation.EQUIVALENCE.ordinal() ];

    // 2. Calcular conjunto Computado: Expandir o rank pra baixo e pra cima
    computedSet.clear();
    for (Category c : returned) {
      computedSet.add(c);
      c.getAncestors(computedSet);   // Procurar ancestrais de mapeamento Equivalente/MenosGeral/Sobreposto
      c.getDescendents(computedSet); // Procurar subclasses da mapeamento Equivalente/MaisGeral
    }

    // 3. Calcular tamanho da intersecao entre os conjuntos Ideal e Computado
    for (Category result : computedSet) {
      idealMapping = idealSet.get(query,result);
      if ( idealMapping.isStrongerThan(Relation.MISMATCHING) ) {
        idealComputedSize ++;
      }
    }

    // 4. Calcular precisao e revocacao
    return new Evaluation(idealComputedSize,idealSize,computedSet.size());

  }
  //****************************************************************************
  private Evaluation evaluateWeakOverlap(CategoryRank rank) {

    // Auxiliar
    Category query             = rank.getQuery();
    Relation idealMapping      = null;
    int      idealComputedSize = 0;
    int      idealSize;
 
    // 1. Calcular tamanho do conjunto Ideal
    idealSize  = idealCount[Relation.OVERLAPPING.ordinal()];
    idealSize += idealCount[Relation.LESS_GENERAL.ordinal()];

    // 0. Manter no rank classes mais especificas
    List<Category> returned = rank.selectLessGeneral();
    
    // 2. Calcular conjunto Computado: Expandir o rank pra cima
    computedSet.clear();
    for (Category c : returned) {
      computedSet.add(c);
      c.getAncestors(computedSet); // Procurar ancestrais de mapeamento MenosGeral/Sobreposto
    }

    // 3. Calcular tamanho da intersecao entre os conjuntos Ideal e Computado
    for (Category result : computedSet) {
      idealMapping = idealSet.get(query,result);
      if ( idealMapping==Relation.OVERLAPPING || idealMapping==Relation.LESS_GENERAL ) {
        idealComputedSize ++;
      }
    }

    // 4. Calcular precisao e revocacao
    return new Evaluation(idealComputedSize,idealSize,computedSet.size());

  }  
  //****************************************************************************
  private Evaluation evaluateNoOverlap(CategoryRank rank) {

    // Calcular precisao e revocacao
    return new Evaluation(0,0,rank.length());

  }   
  //****************************************************************************
  public Evaluation evaluate(CategoryRank rank) {

    // If we dont know the ideal, we are ideal!!!
    if (idealSet==null) return new Evaluation(0,0,0);
    
    // Auxiliar
    idealCount = idealSet.countEachRelation(rank.getQuery());
    
    // Call the proper evaluator
    if ( idealCount[Relation.EQUIVALENCE.ordinal() ] > 0 ) return evaluateStrongOverlap(rank);
    if ( idealCount[Relation.MORE_GENERAL.ordinal()] > 0 ) return evaluateStrongOverlap(rank);
    if ( idealCount[Relation.LESS_GENERAL.ordinal()] > 0 ) return evaluateWeakOverlap(rank);
    if ( idealCount[Relation.OVERLAPPING.ordinal( )] > 0 ) return evaluateWeakOverlap(rank);
    return evaluateNoOverlap(rank);

  }  
  //****************************************************************************

}






