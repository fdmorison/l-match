// Package
///////////////
package com.merak.ontomap.model.extraction;

// Imports
///////////////
import java.util.*;

// Public Class
///////////////
public class MapList {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************	
  private List<String> equivalent   = new LinkedList<String>();
  private List<String> more_general = new LinkedList<String>();

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************	
  public void addEquivalent(String category) {

    equivalent.add(category);

  }
  //****************************************************************************	
  public void addMoreGeneral(String category) {

    more_general.add(category);

  }  
  //****************************************************************************	
  public List<String> getEquivalent() {

    return equivalent;

  }
  //****************************************************************************	
  public List<String> getMoreGeneral() {

    return more_general;

  }    	
  //****************************************************************************	
	
}