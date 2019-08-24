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
  private Instance value;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public XProperty(Property predicate,Instance value) {

    this.predicate = vocabulary;
    this.value     = value;

  }
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private Property getPredicate() {

    return predicate;

  }
  //****************************************************************************
  private Instance getValue() {

    return value;

  }
  //****************************************************************************

}