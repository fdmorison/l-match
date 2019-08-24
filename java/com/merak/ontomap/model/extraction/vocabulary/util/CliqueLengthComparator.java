// Package
///////////////
package com.merak.ontomap.model.extraction.vocabulary.util;

// Imports
///////////////
import java.util.*;
import com.merak.core.graph.Component;

// Public Class
///////////////
public class CliqueLengthComparator implements Comparator<Component> {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final boolean ascendant;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public CliqueLengthComparator(boolean ascendant) {

    // Attribute Initialization
    this.ascendant = ascendant;

  }
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public int compare(Component a,Component b) {

    return (ascendant) ? a.length()-b.length() : b.length()-a.length() ;

  }
  //****************************************************************************

 }