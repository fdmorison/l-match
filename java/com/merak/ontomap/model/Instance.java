// Package
///////////////
package com.merak.ontomap.model;

// Imports
///////////////
import java.util.*;
import com.merak.core.IdentifiedNamedObject;

public class Instance extends IdentifiedNamedObject {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  protected Instance(int id,String name) {

    // Super Attributes Initialization
    super(id,name);

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  /* @return as categorias desta ontologia
   */
  public int getOntologicalId() {

    throw new UnsupportedOperationException();

  }
  //****************************************************************************

}
