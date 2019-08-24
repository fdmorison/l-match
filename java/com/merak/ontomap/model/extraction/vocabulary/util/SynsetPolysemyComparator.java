// Package
///////////////
package com.merak.ontomap.model.extraction.vocabulary.util;

// Imports
///////////////
import java.util.*;
import com.merak.ai.nlp.dictionary.XSynset;

// Public Class
///////////////
public class SynsetPolysemyComparator implements Comparator<XSynset> {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final boolean ascendant;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public SynsetPolysemyComparator(boolean ascendant) {

    // Attribute Initialization
    this.ascendant = ascendant;

  }
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public int compare(XSynset a,XSynset b) {

    double factorA,factorB;

    if (ascendant) {
      factorA = a.getPolysemyFactor();
      factorB = b.getPolysemyFactor();
    }
    else {
      factorB = a.getPolysemyFactor();
      factorA = b.getPolysemyFactor();
    }

    if (factorA>factorB) return  1;
    if (factorA<factorB) return -1;
    return 0;

  }
  //****************************************************************************

 }