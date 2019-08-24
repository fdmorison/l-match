// Package
///////////////
package com.merak.ontomap.model.extraction.util;

// Imports
///////////////
import java.io.*;
import java.util.*;

// Class
///////////////
public class ReductionMap {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final Map<String,String> map = new HashMap<String,String>();

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public void reduceTo(String source,String reduction) {

     map.put(source,reduction);

  }
  //****************************************************************************
  public void reduceTo(String[] source,String reduction) {

    for (int i=0; i<source.length; i++) {
      map.put(source[i],reduction);
    }

  }
  //****************************************************************************
  public String reduce(String source) {

    String reduction = map.get(source);
    return (reduction==null) ? source : reduction ;

  }
  //****************************************************************************
  public void isReduced(String source) {

    return map.get(source)!=null;

  }
  //****************************************************************************
  public void writeTo(File out) {

    ExpansionMap expansionMap = new ExpansionMap(this);
    expansionMap.writeTo(out);

  }
  //****************************************************************************
  public Iterator<String> getSourceIterator() {

    return map.keySet().iterator();

  }
  //****************************************************************************


}
