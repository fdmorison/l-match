// Package
/////////////////
package com.merak.ontomap.mapping;

// Imports
/////////////////
import java.util.*;

import com.merak.core.DynamicArray;
import com.merak.core.io.IO;

import com.merak.ontomap.util.*;
import com.merak.ontomap.model.*;
import com.merak.ontomap.classification.*;
import com.merak.ontomap.mapping.util.*;

// Implementation
/////////////////
public class LMatch extends Mapper {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final TwoWayCategoryPrediction predictions;
  private final MapTable                 computedSet;
  private final HorizontalComparator     siblingComparator; 
  private final DynamicThreshold         isContainedSimilarityThreshold = new DynamicThreshold(0.90);
  private final DynamicThreshold         isContainedOverlapThreshold    = new DynamicThreshold(0.85);

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public LMatch(MapModel model,TwoWayCategoryPrediction predictions) {

    // Super Attributes Initialization
    super(model);

    // Attributes Initialization
    this.predictions       = predictions;
    this.computedSet       = new MapTable(model);
    this.siblingComparator = new HorizontalComparator(predictions);
    
    // Prepare similarity threshold
    isContainedSimilarityThreshold.setBounds(0.70,0.90);    
    isContainedSimilarityThreshold.setMultiplier(0.95);
    
    // Prepare overlap threshold
    isContainedOverlapThreshold.setBounds(0.65,0.85);
    isContainedOverlapThreshold.setMultiplier(0.95);

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private boolean isContained(Category a,Category b) {
       
    // reply heuristically
    double similarityFromA = predictions.getSimilarity(a,b);
    double similarityFromB = predictions.getSimilarity(b,a);
    double overlapFromA    = predictions.getOverlap(a,b);
    double overlapFromB    = predictions.getOverlap(b,a);
    double similarity      = (similarityFromA*similarityFromB);
    
    boolean rule1 = ( overlapFromA    >= isContainedOverlapThreshold.getValue()    );
    boolean rule2 = ( similarityFromA >= isContainedSimilarityThreshold.getValue() );
   
    return ( rule1 && rule2 ) && (overlapFromB>0.1 || similarity>0.3);
    
  } 
  //****************************************************************************
  private boolean isOverlapping(Category a,Category b) {
 
    // If a LessGeneral relation is already known
    if ( computedSet.get(a,b).isOverlapping() ) return true;
      
    // Else, reply heuristically   
    double overlapFromA = predictions.getOverlap(a,b);
    double overlapFromB = predictions.getOverlap(b,a);
    
    return (overlapFromA>0.3) && (overlapFromB>0.3);// || predictions.getTwoWaySimilarity()>0.95;
    
  }      
  //****************************************************************************
  private boolean isLessGeneral(Category a,Category b) {

    // If the relation is already known
    if ( computedSet.get(a,b).isLessGeneral() ) return true;
    
    // Else, reply heuristically
    return isContained(a,b);
    
  }     
  //****************************************************************************
  private boolean isMoreGeneral(Category a,Category b) {
      
    // If the relation is already known
    if ( computedSet.get(a,b).isMoreGeneral() ) return true;
    
    // Else, reply heuristically
    return isContained(b,a);
    
  }     
  //****************************************************************************
  private boolean isEquivalence(Category a,Category b) {

    // If the relation is already known
    if ( computedSet.get(a,b).isEquivalence() ) return true;
    
    // Else, reply heuristically
    return isContained(a,b) && isContained(b,a);
    
  }   
  //****************************************************************************
  private Relation getRelation(Category a,Category b) {

     if ( isEquivalence(a,b) ) return Relation.EQUIVALENCE;
     if ( isMoreGeneral(a,b) ) return Relation.MORE_GENERAL;
     if ( isLessGeneral(a,b) ) return Relation.LESS_GENERAL;
     if ( isOverlapping(a,b) ) return Relation.OVERLAPPING;
     return Relation.UNKNOWN;
    
  }    
  //****************************************************************************
  private void print(Relation r,Category s,Category t,String ident) {
          
    double overlapFromA    = predictions.getOverlap(s,t);
    double overlapFromB    = predictions.getOverlap(t,s);
    double similarityFromA = predictions.getSimilarity(s,t);
    double similarityFromB = predictions.getSimilarity(t,s);
    double sim             = predictions.getSimilarity(s,t) * predictions.getSimilarity(t,s);
    
    java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
    nf.setMaximumFractionDigits(5);    
    nf.setMinimumFractionDigits(5);
      
    if (r!=null) System.out.print(r+": ");
    System.out.print(ident+s.getName()+" e "+t.getName());
    System.out.print(" | " + predictions.getAbsoluteOverlap(s,t) + "<->" + predictions.getAbsoluteOverlap(t,s)   );
    System.out.print(" | " + nf.format(overlapFromA)    + "<->" + nf.format(overlapFromB)   );
    System.out.print(", "  + nf.format( predictions.getTwoWayOverlap(s,t) ) );
    System.out.print(" | " + nf.format(similarityFromA) + "<->" + nf.format(similarityFromB));
    System.out.print(", "  + nf.format(sim) ); 
    System.out.print(" | " + nf.format(this.isContainedOverlapThreshold.getValue()    ) );
    System.out.print(", "  + nf.format(this.isContainedSimilarityThreshold.getValue() ) );
    System.out.println();
    
    //IO.pause();                          
      
  }
  //****************************************************************************
  private boolean areSiblings(Category a,Category b) {
    
    // A e B sao irmaos se existe alguma equivalencia computada entre seus pais
    for (Category parentA : a.getParents()) {
      for (Category parentB : b.getParents()) { if (computedSet.get(parentA,parentB).isEquivalence()) return true; } 
    }
    return false;
    
  }    
  //****************************************************************************
  private Category getMostEquivalentTo(Category[] sourceCandidates,final Category targetQuery) {
        
    if (sourceCandidates.length==0) return null;
      
    // Auxiliar
    Category mostEquivalentCandidate = sourceCandidates[0];
 
    // Elect the candidate that is the most equivalent to query
    siblingComparator.setContext(targetQuery);
    for (int i=1; i<sourceCandidates.length; i++) {        
      if ( siblingComparator.moreEquivalent(sourceCandidates[i],mostEquivalentCandidate) ) {
        mostEquivalentCandidate = sourceCandidates[i];
      }
    }
    return mostEquivalentCandidate;    
    
  }  
  //****************************************************************************
  private Category alignEquivalence(Category source,final Category target) {
  
    // Auxiliar
    Category candidate;
    double   comparation1;
      
    // 0. Alinhamento horizontal
    candidate = getMostEquivalentTo(source.getSiblings(),target);            
    if ( candidate!=null && isEquivalence(candidate,target) && siblingComparator.moreEquivalent(candidate,source) ) {
      // Trivially, compare the names of the classes
      if ( source.selfnamed(target) ) return source;         
      // Para ser eleito, o candidato nao pode ser equivalente a mais ninguem
      if ( computedSet.count(candidate,Relation.EQUIVALENCE)==0 ) source = candidate;
    }   
  
    // 1. Alinhamento vertical
    while ( !source.isLeaf() && predictions.getOverlap(source,target)>0 ) {  
      // Trivially, compare the names of the classes
      if ( source.selfnamed(target) ) return source; 
    
      // Select the most equivalent target's child (horizontal choice)
      candidate    = getMostEquivalentTo(source.getChildren(),target);
      comparation1 = predictions.getTwoWayOverlap(candidate,target) - predictions.getTwoWayOverlap(source,target);
     
      // Question: If Overlap(parent,target) == Overlap(child,target), who is equivalent to source? Source or its child?
      // Answer  : I dont know now... for while, lets choose the child.
      if ( comparation1>=0 ) source = candidate; else break;
    }
    return source;
    
  } 
  //****************************************************************************
  private void searchRelationsOf(Category source,Category[] targets,String ident) {
      
    // Comparar categorias com source
    for ( Category target : targets ) switchRelationsOf(source,target,ident);
    
  }   
  //****************************************************************************
  private void searchRelationsOf(Category[] sources,final Category target,String ident) {
  
    // Comparar categorias com target
    for ( Category source : sources ) switchRelationsOf(source,target,ident);
    
  }    
  //****************************************************************************
  private void searchRelationsOf(Category[] sources,Category[] targets,String ident) {   
   
    for ( Category source : sources ) searchRelationsOf(source,targets,ident);
    
  }    
  //****************************************************************************
  private Relation switchRelationsOf(Category source,Category target,String ident) {
    
    // Discover the relation 
    Relation relation = getRelation(source,target);
    boolean  flag1;
    boolean  flag2;
    Category s,t;
    
    // Avoid redundant processing
    if ( !relation.isUnknown() ) {
       if ( computedSet.get(source,target).isEqualOrStrongerThan(relation) ) return computedSet.get(source,target);
    }

    // Act
    while (true) switch (relation) {
        
      case EQUIVALENCE  : // Align the equivalence
                          source = alignEquivalence(source,target); 
                          target = alignEquivalence(target,source);          
                          computedSet.set(source,target,Relation.EQUIVALENCE);
                          // Relax thresholds
                          flag1 = isContainedSimilarityThreshold.decrease();
                          flag2 = isContainedOverlapThreshold.decrease();
                          // Compare children from the equivalent category
                          searchRelationsOf(source.getChildren(),target.getChildren(),ident) ; 
                          searchRelationsOf(target.getChildren(),source.getChildren(),ident) ; 
                          // Restore thresholds
                          if (flag1) isContainedSimilarityThreshold.increase();
                          if (flag2) isContainedOverlapThreshold.increase();
                          return relation;
      
      case MORE_GENERAL : // Se existe equivalencia entre os pais de source e target, entao equivalente(source,target)
                          if ( areSiblings(source,target) ) { relation = Relation.EQUIVALENCE; ident+="&"; continue; } 
                          // Senao source contem target
                          computedSet.set(source,target,relation);                          
                          searchRelationsOf(source.getChildren(),target,ident+"  ") ; 
                          return relation;
                          
      case LESS_GENERAL : // Se existe equivalencia entre os pais de source e target, entao source e target sao equivalentes
                          if ( areSiblings(source,target) ) { relation = Relation.EQUIVALENCE; ident+="&"; continue; }     
                          // Senao source esta contido em target
                          computedSet.set(source,target,relation);                          
                          searchRelationsOf(source,target.getChildren(),ident) ;                          
                          return relation;
                          
      case OVERLAPPING  : computedSet.set(source,target,relation);
                          searchRelationsOf(source              ,target.getChildren(),ident) ;
                          searchRelationsOf(source.getChildren(),target              ,ident) ;
                          return relation;                          
                          
      default           : searchRelationsOf(source              ,target.getChildren(),ident) ;
                          searchRelationsOf(source.getChildren(),target              ,ident) ;
                          return relation;
    } 
    
  }     
  //****************************************************************************
  public MapTable start() {
      
    searchRelationsOf( model.ontologyA.getRootCategories() , model.ontologyB.getRootCategories(), "" );
    return computedSet;

  }
  //****************************************************************************
  public void close() {
    //...
  }
  //****************************************************************************

}