// Package
///////////////
package com.merak.ontomap.model.extraction;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.Individual;
import com.merak.core.*;

// Public Class
///////////////
public class NewCategory extends IdentifiedNamedObject {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private NewOntology       ontology          = null;
  private OntClass          source            = null;
  private List<NewCategory> children          = new LinkedList<NewCategory>();
  private int               numberOfInstances = 0;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public NewCategory(int id,String name,OntClass source,NewOntology ontology) {

    // Super Attribute Initialization
    super(id,name);

    // Attribute Initialization
    this.ontology = ontology;
    this.source   = source;

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public OntClass getSource() {

    return source;

  }
  //****************************************************************************
  public NewOntology getOntology() {

    return ontology;

  }
  //****************************************************************************
  public List<NewCategory> getChildren() {

    return children;

  }
  //****************************************************************************
  public void notifyInstance(NewInstance individual) {

    numberOfInstances++;

  }
  //****************************************************************************
  public void notifyChild(NewCategory newChild) {

    children.add(newChild);

  }
  //****************************************************************************
  public int getNumberOfChildren() {

    return children.size();

  }
  //****************************************************************************
  public int getNumberOfInstances() {

    return numberOfInstances;

  }
  //****************************************************************************
  public boolean hasChild() {

    return getNumberOfChildren() > 0;

  }
  //****************************************************************************
  public boolean hasInstance() {

    return getNumberOfInstances() > 0;

  }
  //****************************************************************************
  public boolean isRecent() {

    return (getId()+1) == ontology.getStore().categoryTable.getNextId();

  }
  //****************************************************************************

}