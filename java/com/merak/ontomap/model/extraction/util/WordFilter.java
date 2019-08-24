// Package
///////////////
package com.merak.ontomap.model.extraction.util;

// Imports
///////////////
import java.util.*;
import com.merak.core.Filter;

// Public Class
///////////////
public class WordFilter extends Filter<String> {

 //~ Attributes ////////////////////////////////////////////////////////////////
 //*****************************************************************************
 private final Set<String> set;

 //~ Constructors /////////////////////////////////////////////////////////////
 //****************************************************************************
 public WordFilter() {

   // Attribute Initialization
   this.set = new HashSet<String>();

 }
 //****************************************************************************
 public WordFilter(Set<String> set) {

   // Attribute Initialization
   this.set = (set!=null) ? set : new HashSet<String>();

 }

 //~ Methods ///////////////////////////////////////////////////////////////////
 //*****************************************************************************
 public void put(String object) {

   set.add(object);

 }
 //*****************************************************************************
 public void put(String[] array) {

   for (int i=0; i<array.length; i++) {
     set.add(array[i]);
   }

 }
 //*****************************************************************************
 public boolean accept(String object) {

   return set.contains(object.toLowerCase());

 }
 //*****************************************************************************

}