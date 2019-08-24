// Package
///////////////
package com.merak.ontomap.model.extraction.render;

// Imports
///////////////
import java.util.*;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;

// Public Class
///////////////
public class InstanceProperty {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private Property predicate;
  private Literal  value;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public XProperty(Property predicate,Literal value) {

    this.predicate = vocabulary;
    this.value     = value;

  }
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private Property getPredicate() {

    return predicate;

  }
  //****************************************************************************
  private Literal getValue() {

    return value;

  }
  //****************************************************************************

}