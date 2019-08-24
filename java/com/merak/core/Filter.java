// Package
///////////////
package com.merak.core;

// Public Class
///////////////
public abstract class Filter<T> {

 //~ Methods ///////////////////////////////////////////////////////////////////
 //*****************************************************************************
 private static final Filter trueInstance  = new Filter() { public boolean accept(Object object) { return true;  } };
 private static final Filter falseInstance = new Filter() { public boolean accept(Object object) { return false; } };

 //~ Methods ///////////////////////////////////////////////////////////////////
 //*****************************************************************************
 public static Filter getTrueInstance() {

   return trueInstance;

 }
 //*****************************************************************************
 public static Filter getFalseInstance() {

   return falseInstance;

 }
 //*****************************************************************************
 public abstract boolean accept(T object);

 //*****************************************************************************

}