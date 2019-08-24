// Package
///////////////
package com.merak.ontomap.mapping.util;

// Imports
///////////////
import java.util.*;
import com.merak.ontomap.model.Category;

// Class
///////////////
public class CategoryPair implements Comparable<CategoryPair> {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  public final Category source;
  public final Category target;
  public final double   similarity;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public CategoryPair(Category source,Category target,double similarity) {

    // Attributes Initialization
    this.source     = source;
    this.target     = target;
    this.similarity = similarity;

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public int compareTo(CategoryPair pair) {

    if ( similarity > pair.similarity ) return  1; 
    if ( similarity < pair.similarity ) return -1;
    return 0;

  }
  //****************************************************************************
  public String toString() {

    return "{" + source.getName() +","+ target.getName() +"}";

  }  
  //****************************************************************************
}