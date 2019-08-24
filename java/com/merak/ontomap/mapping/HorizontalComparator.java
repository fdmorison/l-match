// Package
/////////////////
package com.merak.ontomap.mapping;

// Imports
/////////////////
import java.util.*;
import com.merak.ontomap.model.Category;
import com.merak.ontomap.classification.TwoWayCategoryPrediction;

// Implementation
/////////////////
public class HorizontalComparator implements Comparator<Category> {
    
  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  public TwoWayCategoryPrediction predictions;
  public Category                 context;

  //~ Constructor //////////////////////////////////////////////////////////////
  //****************************************************************************
  public HorizontalComparator(TwoWayCategoryPrediction predictions) {

    // Attribute Initialization
    this.predictions = predictions;
    this.context     = null;

  }   
  //****************************************************************************
  public HorizontalComparator(TwoWayCategoryPrediction predictions,Category category) {

    // Attribute Initialization
    this.predictions = predictions;
    this.setContext(category);

  }  
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public final void setContext(Category category) {
      
    if (category==null) throw new RuntimeException( "O contexto nao pode ser uma categoria igual a 'null'" );
    this.context = category;
      
  }
  //****************************************************************************
  public final int compare(Category a,Category b) {

    // Se der empate, compare a similaridade
    double comparation2 = predictions.getTwoWaySimilarity(a,context) - predictions.getTwoWaySimilarity(b,context);     
    if (comparation2 > 0 ) return  1;
    if (comparation2 < 0 ) return -1;
    
    // Compare o overlap primeiro
    double comparation1 = predictions.getTwoWayOverlap(a,context) - predictions.getTwoWayOverlap(b,context);
    if (comparation1 > 0 ) return  1;
    if (comparation1 < 0 ) return -1;
    
    // Senao as classes sao igualmente equivalentes
    return 0;
   
  } 
  //****************************************************************************
  public final boolean moreEquivalent(Category a,Category b) {

    return compare(a,b) > 0;
   
  }   
  //****************************************************************************
  public final boolean lessEquivalent(Category a,Category b) {

    return compare(a,b) < 0;
   
  }     
  //****************************************************************************
  public final boolean equalyEquivalent(Category a,Category b) {

    return compare(a,b) == 0;
   
  }   
  //****************************************************************************
  
}















