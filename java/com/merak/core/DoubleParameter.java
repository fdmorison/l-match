package com.merak.core;

public class DoubleParameter {

 //~ Attributes ////////////////////////////////////////////////////////////////
 //*****************************************************************************
 public String identifier;
 public double value;

 //~ Constructors //////////////////////////////////////////////////////////////
 //*****************************************************************************
 public DoubleParameter() {
 	
   // Nothing to do here :)
   
 }
 //*****************************************************************************
 public DoubleParameter(String identifier,double value) {
 	
   this.identifier = identifier;
   this.value      = value;
   
 }
 
 //~ Methods ///////////////////////////////////////////////
 //*****************************************************************************
 public Object clone() {
 	
   return new Parameter(identifier,value);
   
 }
 //*****************************************************************************
 public String toString() {
 	
   return identifier+":"+value;
   
 }
 //*****************************************************************************

}