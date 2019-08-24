// Package
///////////////
package com.merak.ontomap.util;

// Imports
///////////////
import java.util.*;
import com.merak.ontomap.model.Category;

// Class
///////////////
public class DynamicThreshold {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  public double upperBound = 1.0;
  public double lowerBound = 0.0;
  public double multiplier = 0.9;
  public double value      = upperBound;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public DynamicThreshold() {
    // Just the default constructor :)
  }  
  //****************************************************************************
  public DynamicThreshold(double value) {

    // Attributes Initialization   
    setValue(value);

  }

  //~ Methods ////////////////////////////////////////////////////////////////// 
  //****************************************************************************
  public void setMultiplier(double multiplier) {

    // Check range
    if ( multiplier <= 0 ) throw new RuntimeException("Valor do multiplicador esta alem do limite inferior: "+multiplier+" <= 0");
    
    // Set multiplier
    this.multiplier = multiplier;

  }   
  //****************************************************************************
  public void setBounds(double lowerBound,double upperBound) {

    // Check range
    if ( lowerBound < 0          ) throw new RuntimeException("Valor do limite inferior esta negativo: "+lowerBound+" < 0");
    if ( lowerBound > upperBound ) throw new RuntimeException("Limite Inferior maior que Limite Superior: intervalor invalido "+lowerBound+">"+upperBound);
    
    // Set bounds
    this.upperBound = upperBound;
    this.lowerBound = lowerBound;

  }    
  //****************************************************************************
  public void setValue(double value) {

    // Check range
    if ( value > upperBound ) throw new RuntimeException("Valor do limiar esta alem do limite superior: "+value+" > "+upperBound);
    if ( value < lowerBound ) throw new RuntimeException("Valor do limiar esta alem do limite inferior: "+value+" < "+lowerBound);
    
    // Set threshold
    this.value = value;

  }  
  //****************************************************************************
  public double getValue() {

    return value;

  }  
  //****************************************************************************
  public boolean decrease() {

    double newValue = value * multiplier; 
    if ( newValue < lowerBound ) return false;
    value = newValue;
    return true;

  }
  //****************************************************************************
  public boolean increase() {

    double newValue = value / multiplier; 
    if ( newValue > upperBound ) return false;
    value = newValue;
    return true;
      
  }
  //****************************************************************************
}