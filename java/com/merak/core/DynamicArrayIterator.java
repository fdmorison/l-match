// Package
///////////////
package com.merak.core;

// Imports
///////////////
import java.util.Iterator;

// Class
//////////////
public class DynamicArrayIterator<T> implements Iterator<T> {
    
  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final DynamicArray<T> array;
  private       int             i;
            
  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public DynamicArrayIterator(DynamicArray array) {

    // Attributes Initialization
    this.array = array;
    this.i     = 0;
    
  }
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public boolean hasNext() {

    return i < array.length(); 
    
  }
  //****************************************************************************
  public T next() {

    return array.get(i++);
    
  }  
  //****************************************************************************
  public void remove() {

    throw new UnsupportedOperationException();
    
  }    
  //****************************************************************************
}
