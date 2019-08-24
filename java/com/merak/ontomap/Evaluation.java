// Package
/////////////////
package com.merak.ontomap;

// Imports
/////////////////
import java.util.*;

// Implementation
/////////////////
public class Evaluation {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  public double computed;
  public double ideal;
  public double idealComputed;

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public Evaluation() {

    reset();

  }  
  //****************************************************************************
  public Evaluation(int idealComputed,int ideal,int computed) {

    // Check
    if (idealComputed>ideal   ) throw new RuntimeException("Conjunto ideal eh maior que a intersecao com conjunto computado");
    if (idealComputed>computed) throw new RuntimeException("Conjunto computado eh maior que a intersecao com conjunto ideal");

    // Attribute Initialization
    this.idealComputed = (double) idealComputed;
    this.ideal         = (double) ideal;
    this.computed      = (double) computed;

  }
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public static double getFMeasure(double precision,double recall) {

    return (precision + recall)==0
           ? 1
           : (2 * precision * recall) / (precision + recall);

  }  
  //****************************************************************************
  public double getComputedSize() {

    return computed;

  }
  //****************************************************************************
  public double getIdealSize() {

    return ideal;

  }
  //****************************************************************************
  public double getIdealComputedSize() {

    return idealComputed;

  }
  //****************************************************************************
  public double getPrecision() {

    return computed==0 
           ? 1
           : idealComputed / computed;

  }
  //****************************************************************************
  public double getRecall() {

    return ideal==0
           ? 1
           : (idealComputed / ideal);

  }
  //****************************************************************************
  public double getFMeasure() {

    // Calcular F-Measure
    return getFMeasure( getPrecision() , getRecall() );

  }
  //****************************************************************************
  public boolean hasComputedElements() {

    return computed > 0;

  }
  //****************************************************************************
  public boolean hasIdealElements() {

    return ideal > 0;

  }
  //****************************************************************************
  public boolean hasIdealComputedElements() {

    return idealComputed > 0;

  }
  //****************************************************************************
  public void sum(Evaluation source) {

    this.computed      += source.computed;
    this.ideal         += source.ideal;
    this.idealComputed += source.idealComputed;
    
  }
  //****************************************************************************
  public void reset() {

    computed      = 0;
    ideal         = 0;
    idealComputed = 0;

  }
  //****************************************************************************
  public String toString() {

    return getPrecision() +":"+ getRecall() +":"+ getFMeasure();

  }
  //****************************************************************************
}