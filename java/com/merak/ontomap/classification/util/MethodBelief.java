// Package
///////////////
package com.merak.ontomap.classification.util;

// Imports
///////////////
import java.util.*;
import com.merak.ai.classification.ClassificationMethod;

// Class
///////////////
public class MethodBelief implements Comparable<MethodBelief> {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  public final ClassificationMethod  method;
  public final double                belief;
  public final Map<String,Object>    params;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public MethodBelief(ClassificationMethod method,double belief,Map<String,Object> params) {

    // Attibute Initialization
    this.method = method;
    this.belief = belief;
    this.params = params;

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public int compareTo(MethodBelief mb) {

    return method.compareTo(mb.method);

  }
  //****************************************************************************

}
