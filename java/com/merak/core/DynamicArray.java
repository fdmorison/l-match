// Package
/////////////////
package com.merak.core;

// Imports
/////////////////
import java.util.*;
import java.lang.reflect.Array;

// Implementation
/////////////////
public class DynamicArray<T> implements Iterable<T> {

  //~ Constants ////////////////////////////////////////////////////////////////
  //****************************************************************************
  public final int MINIMUM_CAPACITY = 10;

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private T[] array;
  private int length;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public DynamicArray() {

    // Attribute Initialization
    this.array  = (T[]) new Object[MINIMUM_CAPACITY];
    this.length = 0;

  }
  //****************************************************************************
  public DynamicArray(int capacity) {

    // Attribute Initialization
    this.array  = alloc(capacity);
    this.length = 0;

  }
  //****************************************************************************
  public DynamicArray(T[] source) {

    // Attribute Initialization
    this(source.length);
    
    // Copy elements from the source array
    copyFrom(source);

  }
  
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private final T[] alloc(int capacity) {

    return (T[]) new Object[ (capacity<MINIMUM_CAPACITY) ? MINIMUM_CAPACITY : capacity ];

  }
  //****************************************************************************
  private final void realloc(int newCapacity) {

    // Ignore unnecessary reallocation
    if (newCapacity == array.length) return;

    // Change (increase or decrease) current capacity to newCapacity
    T[] newArray = alloc(newCapacity);
    
    // Adjust length, if necessary
    if (length>newCapacity) length=newCapacity;
    
    // (a) Copy old values and (b) replace old array
    for (int i=0; i<length; i++) newArray[i] = array[i];
    array = newArray;

  }
  //****************************************************************************
  public final T get(int i) {

    if (i>=length) throw new IndexOutOfBoundsException("Length is "+length+", but inclusive index "+i+" was received");
    return array[i];

  }
  //****************************************************************************
  public final T set(int i,T value) {

    if (i>=length) throw new IndexOutOfBoundsException("Length is "+length+", but inclusive index "+i+" was received");
    return array[i] = value;

  }
  //****************************************************************************
  public final T append(T value) {

    if (length==array.length) realloc( 2*length );
    array[length++] = value;
    return value;

  }
  //****************************************************************************
  public final void setLength(int newLength) {

    if (newLength<0) throw new IndexOutOfBoundsException("Negative length "+newLength);

    // Ensure capacity to hold newLength
    ensureCapacity(newLength);

    // Change the length
    length = newLength;

  }
  //****************************************************************************/
  public final void ensureCapacity(int minimumCapacity) {

    // Increase current capacity to minCapacity, if necessary
    if ( minimumCapacity > array.length ) realloc(minimumCapacity);

  }
  //****************************************************************************
  public final void trimCapacity() {

    realloc(length);

  }
  //****************************************************************************/
  public final Iterator<T> iterator() {

    return new DynamicArrayIterator(this);

  }
  //****************************************************************************
  public final void copyFrom(T[] source) {

    setLength(source.length);
    for (int i=0; i<length; i++) array[i] = source[i];

  }  
  //****************************************************************************
  public final void sort() {

    Arrays.sort(array,0,length);

  }
  //****************************************************************************
  public final void sort(Comparator<T> cmp) {

    Arrays.sort(array,0,length,cmp);

  }
  //****************************************************************************
  public final int capacity() {

    return array.length;

  }
  //****************************************************************************
  public final int length() {

    return length;

  }
  //****************************************************************************
  public final boolean isEmpty() {

    return length==0;

  }
  //****************************************************************************
  public final boolean hasElements() {

    return length>0;

  }
  //****************************************************************************
  public final void clear() {

    length = 0;

  }
  //****************************************************************************
  public final String toString() {

    return Arrays.toString(array);

  }
  //****************************************************************************
}
