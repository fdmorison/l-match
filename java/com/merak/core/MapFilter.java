// Package
///////////////
package com.merak.core;

// Imports
///////////////
import java.util.*;

// Public Class
///////////////
public class MapFilter<T> extends Filter<T> {

 //~ Attributes ////////////////////////////////////////////////////////////////
 //*****************************************************************************
 private final Map<T,T> map;

 //~ Constructors /////////////////////////////////////////////////////////////
 //****************************************************************************
 public MapFilter() {

   // Attribute Initialization
   this.map = new HashMap<T,T>();

 }
 //****************************************************************************
 public MapFilter(Map<T,T> map) {

   // Attribute Initialization
   this.map = (map!=null) ? map : new HashMap<T,T>();

 }

 //~ Methods ///////////////////////////////////////////////////////////////////
 //*****************************************************************************
 public void put(T object) {

   map.put(object,object);

 }
 //*****************************************************************************
 public void put(T[] array) {

   for (int i=0; i<array.length; i++) {
     map.put(array[i],array[i]);
   }

 }
 //*****************************************************************************
 public boolean accept(T object) {

   return map.get(object) != null;

 }
 //*****************************************************************************

}