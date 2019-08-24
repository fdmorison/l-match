package com.merak.core;

public class Parameter<TValue> {

 //~ Attributes ////////////////////////////////////////////////////////////////
 //*****************************************************************************
 public String identifier;
 public TValue value;

 //~ Constructors //////////////////////////////////////////////////////////////
 //*****************************************************************************
 public Parameter() {
 	
   // Nothing to do here :)
   
 }
 //*****************************************************************************
 public Parameter(String identifier,TValue value) {
 	
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