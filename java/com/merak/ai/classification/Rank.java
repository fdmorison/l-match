// Package
///////////////
package com.merak.ai.classification;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.merak.core.DynamicArray;

public class Rank<TQuery,TResult> implements Iterable< Rank<TQuery,TResult>.Pair > {

  //~ InnerClasses /////////////////////////////////////////////////////////////
  //****************************************************************************
  public class Pair {
    public TResult entity;
    public double  priority;
  }

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  public TQuery             query;
  public DynamicArray<Pair> array;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public Rank() {

    // Attribute initialization
    this(null,0);

  }
  //****************************************************************************
  public Rank(int capacity) {

    // Attribute initialization
    this(null,capacity);

  }
  //****************************************************************************
  public Rank(TQuery query,int capacity) {

    // Attribute initialization
    this.query = query;
    this.array = new DynamicArray<Pair>(capacity);

  }
  //****************************************************************************
  private Rank(TQuery query,Rank<TQuery,TResult> source) {

    // Attribute initialization
    this(query,source.length());

    // Copy the source rank
    for (Pair pair : source.array) {
      array.append(pair);
    }

  }
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public void createEntry(TResult entity,double priority) {

    // Create an entry
    Pair entry = new Pair();

    // Set all attibutes of the entry
    entry.entity   = entity;
    entry.priority = priority;

    // Add the entry to rank
    array.append(entry);

  }
  //****************************************************************************
  public TQuery getQuery() {

    return query;

  }
  //****************************************************************************
  public Pair getEntry(int i) {

    return array.get(i);

  }
  //****************************************************************************
  public TResult getEntity(int i) {

    return array.get(i).entity;

  }
  //****************************************************************************
  public double getPriority(int i) {

    return array.get(i).priority;

  }
  //****************************************************************************
  public void setQuery(TQuery query) {

    this.query = query;

  }
  //****************************************************************************
  public void setEntry(int i,TResult entity,double priority) {

    // Recover the entry
    Pair entry = array.get(i);

    // Set all attibutes of the entry
    entry.entity   = entity;
    entry.priority = priority;

  }
  //****************************************************************************
  public void setLength(int newLength) {

    // Altere a capacidade do rank
    array.ensureCapacity(newLength);

    // Se o rank for menor, inicialize novas posicoes
    while ( array.length() < newLength ) {
      createEntry(null,0);
    }

  }
  //****************************************************************************
  public void sort() {

    // Estabelecendo um comparador para ordenar o rank
    Comparator<Pair> cmp = new Comparator<Pair>() {
      public int compare(Pair a,Pair b) {
        if (a.priority > b.priority) return -1;
        if (a.priority < b.priority) return  1;
        return 0;
      }
    };

    // Ordenando
    array.sort(cmp);

  }
  //****************************************************************************
  public void normalize() {

    // Auxiliar
    double factor    = 0.0d;  // Fator de Normalizacao, que eh igual ao somatorios das probabilidades
    double increment = 0.0d;  // Incremento para resolver probabilidades negativas

    // Calculando incremento, isto �, -1 * menorProbabilidadeNegativa.
    for (Pair entry : array) {
      if ( entry.priority < increment ) increment = entry.priority;
    }
    if (increment<0) increment = -1 * increment;

    // Somar o incremento a todas as probabilidades, de maneira a garantir que todas sejam positivas
    for (Pair entry : array) {
      entry.priority += increment;
      factor         += entry.priority;
    }

    // Cancelar normalizacao se o divisor for zero
    if (factor==0.0d) return;

    // Normalizando cada probabilidades
    for (Pair entry : array) {
      entry.priority /= factor;
    }

  }
  //****************************************************************************
  public final void pruneWithTopK(int k) {

    // trim the end of the array
    if (k<length()) array.setLength(k);

  }
  //****************************************************************************
  public final void pruneWithAbsoluteThreshold(double threshold) {

    // Auxiliar
    int length = 0;

    // Find the new length of the array: newLength <= oldLength
    for (int i=0; i<array.length(); i++) {
      if (array.get(i).priority >= threshold) length++; else break;
    }
    pruneWithTopK(length);

  }
  //****************************************************************************
  public final void pruneWithRelativeThreshold(double threshold) {

    if (length()>=2) {
      pruneWithAbsoluteThreshold( threshold * array.get(0).priority );
    }

  }
  //****************************************************************************
  public final void pruneWithDifferenceThreshold(double threshold) {

    if (length()<2) return;

    if ( getEntry(1).priority < getEntry(0).priority*threshold ) {
      pruneWithTopK(1);
      return;
    }

    // Auxiliar
    Pair   back   = getEntry(0);
    Pair   next   = getEntry(1);
    double diff   = next.priority - back.priority;
    int    length = 1;

    // Atualizar o limiar
    threshold = diff + diff*(1-threshold);

    // Find the new length of the array: newLength <= oldLength
    for (int i=2; i<array.length(); i++) {
      back = next;
      next = getEntry(i);
      diff = next.priority - back.priority;
      if (diff<=threshold) length++; else break;
    }
    pruneWithTopK(length);


  }
  //****************************************************************************
  public final Iterator<Pair> iterator() {

    return array.iterator();

  }
  //****************************************************************************
  public final boolean isEmpty() {

    return array.isEmpty();

  }
  //****************************************************************************
  public final int length() {

    return array.length();

  }
  //****************************************************************************
  public final void clear() {

    array.clear();

  }
  //****************************************************************************
  public Rank<TQuery,TResult> copy() {

    return new Rank<TQuery,TResult>(query,this);

  }
  //****************************************************************************
  public Object clone() {

    return new Rank<TQuery,TResult>(query,this);

  }
  //****************************************************************************
  public final Pair[] toArray() {

    Pair[] entryArray = (Pair[]) new Object[length()];
    for (int i=0; i<entryArray.length; i++) {
      entryArray[i] = getEntry(i);
    }
    return entryArray;

  }
  //****************************************************************************
  public final double[] toPriorityArray() {

    double[] priorityArray = new double[length()];
    for (int i=0; i<priorityArray.length; i++) {
      priorityArray[i] = getPriority(i);
    }
    return priorityArray;

  }
  //****************************************************************************
  public final TResult[] toEntityArray() {

    TResult[] entityArray = (TResult[]) new Object[length()];
    for (int i=0; i<entityArray.length; i++) {
      entityArray[i] = getEntity(i);
    }
    return entityArray;

  }
  //****************************************************************************
  public final String toString() {

    return toStringAsLine();

  }
  //****************************************************************************
  public String toStringAsLine() {

    String string = query + " ";
    for (Pair entry : array) {
      string += entry.entity + " : " + entry.priority + " ";
    }
    return string;

  }
  //****************************************************************************
  public String toStringAsColumn() {

    String string = query + "\n";
    for (Pair entry : array) {
      string += entry.entity + " : " + entry.priority + "\n";
    }
    return string;

  }
  //****************************************************************************


}
