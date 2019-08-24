// Package
///////////////
package com.merak.core;

public class IdentifiedNamedObject implements Identified, Named {

 //~ Attributes ////////////////////////////////////////////////////////////////
 //*****************************************************************************
 private Integer id;
 private String  name;

 //~ Constructors //////////////////////////////////////////////////////////////
 //*****************************************************************************
 protected IdentifiedNamedObject(int id,String name) {

   // Attribute Initialization
   this.id   = new Integer(id);
   this.name = name;

 }
 //*****************************************************************************
 protected IdentifiedNamedObject(Integer id,String name) {

   // Attribute Initialization
   this.id   = id;
   this.name = name;

 }

 //~ Methods ///////////////////////////////////////////////////////////////////
 //*****************************************************************************
 protected void setId(int id) {

   this.id = new Integer(id);

 }
 //*****************************************************************************
 protected void setName(String name) {

   this.name = name;

 }
 //*****************************************************************************
 public int getId() {

   return id.intValue();

 }
 //*****************************************************************************
 public Integer getWrappedId() {

   return id;

 }
 //*****************************************************************************
 public String getName() {

   return name;

 }
 //*****************************************************************************
 public boolean hasId(int value) {

   return id.intValue()==value;

 }
 //*****************************************************************************
 public boolean hasId(Integer value) {

   return id.equals(value);

 }
 //*****************************************************************************
 public boolean hasName(String value) {

   return name.equals(value);

 }
 //*****************************************************************************
 public boolean selfsame(Identified object) {

   return getClass().isInstance(object) && id.equals(object.getId());

 }
 //*****************************************************************************
 public boolean selfnamed(Named object) {

   return name.equals(object.getName());

 }
 //*****************************************************************************
 public final int hashCode() {

   return id.hashCode();

 }
 //*****************************************************************************
 public boolean equals(Object object) {

   // Falso se os objetos forem de classes diferentes
   if ( !getClass().isInstance(object) ) return false;

   // Verdadeiro se os objetos tiverem o mesmo id
   IdentifiedNamedObject o = (IdentifiedNamedObject)object;
   return id.equals(o.getId());

 }
 //*****************************************************************************
 public String toString() {

   return id +":"+ name;

 }
 //*****************************************************************************

}