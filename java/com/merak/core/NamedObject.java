// Package
///////////////
package com.merak.core;

// Implementation
/////////////////
public class NamedObject implements Named {

 //~ Attributes ////////////////////////////////////////////////////////////////
 //*****************************************************************************
 private String name;

 //~ Constructors //////////////////////////////////////////////////////////////
 //*****************************************************************************
 protected NamedObject() {

   // Attribute Initialization
   this.name = "Unnamed";

 }
 //*****************************************************************************
 protected NamedObject(String name) {

   // Attribute Initialization
   this.name = name;

 }

 //~ Methods ///////////////////////////////////////////////////////////////////
 //*****************************************************************************
 protected void setName(String name) {

   this.name = name;

 }
 //*****************************************************************************
 public String getName() {

   return name;

 }
 //*****************************************************************************
 public boolean hasName(String value) {

   return name.equals(value);

 }
 //*****************************************************************************
 public boolean selfnamed(Named object) {

   return name.equals(object.getName());

 }
 //*****************************************************************************
 public String toString() {

   return name;

 }
 //*****************************************************************************
 public boolean equals(Object object) {

   // Falso se os objetos forem de classes diferentes
   if ( !getClass().isInstance(object) ) return false;

   // Verdadeiro se os objetos tiverem o mesmo nome e mesmo id
   NamedObject o = (NamedObject)object;
   return name.equals(o.getName());

 }
 //*****************************************************************************

}