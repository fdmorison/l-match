// Package
///////////////
package com.merak.ai.classification;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.merak.core.DoubleParameter;

public class Rank implements Iterable<DoubleParameter> {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  public String            name  = null;
  public DoubleParameter[] array = null;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public Rank(int capacity) {

    // Attribute initialization
    this.name  = "Unnamed";
    this.array = new DoubleParameter[capacity];

    // Rank array initialization
    for (int i=0; i<array.length; i++) {
      array[i] = new DoubleParameter("",0.0);
    }

  }
  //****************************************************************************
  public Rank(String name,int capacity) {

    // Attribute initialization
    this.name  = name;
    this.array = new DoubleParameter[capacity];

    // Rank array initialization
    for (int i=0; i<array.length; i++) {
      array[i] = new DoubleParameter("",0.0);
    }

  }
  //****************************************************************************
  public Rank(String name,Rank rank) {

    // Attribute initialization
    this.name  = name;
    this.array = new DoubleParameter[rank.length()];

    // Rank array initialization
    for (int i=0; i<array.length; i++) {
      array[i] = (DoubleParameter) rank.array[i].clone();
    }

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public void setName(String name) {

  	this.name = name;

  }
  //****************************************************************************
  public String getName() {

  	return name;

  }
  //****************************************************************************
  public DoubleParameter get(int i) {

  	return array[i];

  }
  //****************************************************************************
  public void set(int i,String classname,double priority) {

  	array[i].identifier = classname;
  	array[i].value      = priority;

  }
  //****************************************************************************
  public void sort() {

    // Estabelecendo um comparador para ordenar o rank
  	Comparator cmp = new Comparator<DoubleParameter>() {
  		public int compare(DoubleParameter a,DoubleParameter b) {
  		    if (a.value>b.value) return -1;
  		    if (a.value<b.value) return  1;
  		    return 0;
  		}
  	};

  	// Ordenando
    Arrays.sort(array,cmp);

  }
  //****************************************************************************
  public void ensureCapacity(int capacity) {

    if (capacity <= array.length) return;

    // Auxiliar
    DoubleParameter[] newArray = new DoubleParameter[capacity];
    int               i;

    // Copiando elementos do array anterior
    for (i=0; i<array.length; i++)  {
      newArray[i] = array[i];
    }

    // Setando novas posicoes do array
    while (i<newArray.length)  {
      newArray[i++] = new DoubleParameter();
    }

    // Atualizando atributo
    array = newArray;

  }
  //****************************************************************************
  public void normalize() {

  	double normalizingConstant = 0.0d;  // Fator de Normalização, que é igual ao somatórios das probabilidades
  	double increment           = 0.0d;  // Incremento para resolver probabilidades negativas

    // Calculando incremento, isto é, -1 * menorProbabilidadeNegativa.
    // Este incremento será somado a todas as probabilidades, de maneira a garantir que todas sejam positivas
  	for (int i=0; i<array.length; i++) {
  	  if (array[i].value<increment) increment = array[i].value;
  	}
  	if (increment<0) increment = -1 * increment;

  	// Somando todas as probabilidades e incrementando probabilidades para sumir com negativos
  	for (int i=0; i<array.length; i++) {
  	  array[i].value      += increment;
  	  normalizingConstant += array[i].value;
  	}

    // Ignorando normalização se o fator for 0, mesmo porque não existe divisao por zero
  	if (normalizingConstant==0.0d) return;

  	// Normalizando cada probabilidades
  	for (int i=0; i<array.length; i++) {
  	  array[i].value /= normalizingConstant;
  	}

  }
  //****************************************************************************
  public final void pruneWithAbsoluteThreshold(double threshold) {

    // Auxiliar
    DoubleParameter[] prunedArray = null;
    int               length      = 0;

    // Find the position to cut the array: the fist invalid value's position
    while ( length < array.length ) {
      if (array[length].value >= threshold) length++; else break;
    }

    // Cut the array
    prunedArray = new DoubleParameter[length];
    for (int i=0; i < prunedArray.length; i++) {
      prunedArray[i] = array[i];
    }
    array = prunedArray;

  }
  //****************************************************************************
  public final void pruneWithRelativeThreshold(double threshold) {

    if ( !isEmpty() ) pruneWithAbsoluteThreshold( threshold * array[0].value );

  }
  //****************************************************************************
  public final String toString() {

    return toStringAsLine();

  }
  //****************************************************************************
  public String toStringAsLine() {

  	String string = name + " ";
  	for (int i=0; i<array.length; i++) {
  	  string += array[i].toString() + " ";
  	}
    return string;

  }
  //****************************************************************************
  public String toStringAsColumn() {

  	String string = name + "\n";
  	for (int i=0; i<array.length; i++) {
      string += array[i].toString() + "\n";
  	}
    return string;

  }
  //****************************************************************************
  public final Iterator<DoubleParameter> iterator() {

    return Arrays.asList(array).iterator();

  }
  //****************************************************************************
  public final boolean isEmpty() {

    return array.length == 0;

  }
  //****************************************************************************
  public final int length() {

    return array.length;

  }
  //****************************************************************************
  public Object clone() {

    return new Rank(name,this);

  }
  //****************************************************************************


}
