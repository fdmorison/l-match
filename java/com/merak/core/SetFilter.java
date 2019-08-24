// Package
///////////////
package com.merak.core;

// Imports
///////////////
import java.util.*;

// Public Class
///////////////
public class SetFilter<T> extends Filter<T> {

 //~ Attributes ////////////////////////////////////////////////////////////////
 //*****************************************************************************
 private final Set<T> set;

 //~ Constructors /////////////////////////////////////////////////////////////
 //****************************************************************************
 public SetFilter() {

   // Attribute Initialization
   this.set = new HashSet<T>();

 }
 //****************************************************************************
 public SetFilter(Set<T> set) {

   // Attribute Initialization
   this.set = (set!=null) ? set : new HashSet<T>();

 }

 //~ Methods ///////////////////////////////////////////////////////////////////
 //*****************************************************************************
 public void put(T object) {

   set.add(object);

 }
 //*****************************************************************************
 public void put(T[] array) {

   for (int i=0; i<array.length; i++) {
     set.add(array[i]);
   }

 }
 //*****************************************************************************
 public boolean accept(T object) {

   return set.contains(object);

 }
 //*****************************************************************************

}