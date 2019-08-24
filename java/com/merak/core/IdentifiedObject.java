// Package
///////////////
package com.merak.core;

public class IdentifiedObject implements Identified {

 //~ Attributes ////////////////////////////////////////////////////////////////
 //*****************************************************************************
 private Integer id;

 //~ Constructors //////////////////////////////////////////////////////////////
 //*****************************************************************************
 /* Construtor único que obriga a classe a ser sempre inicializada com um id.
  * O construtor é protegido pois a idéia é que
  */
 protected IdentifiedObject(int id) {

   // Attribute Initialization
   this.id = new Integer(id);

 }
 //*****************************************************************************
 protected IdentifiedObject(Integer id) {

   // Attribute Initialization
   this.id = id;

 }

 //~ Methods ///////////////////////////////////////////////////////////////////
 //*****************************************************************************
 protected void setId(int id) {

   this.id = new Integer(id);

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
 public boolean hasId(int value) {

   return id.intValue()==value;

 }
 //*****************************************************************************
 public boolean hasId(Integer value) {

   return id.equals(value);

 }
 //*****************************************************************************
 public boolean selfsame(Identified object) {

   return getClass().isInstance(object) && id.equals(object.getId());

 }
 //*****************************************************************************
 public final int hashCode() {

   return id.hashCode();

 }
 //*****************************************************************************
 public boolean equals(Object object) {

   // Falso se os objetos forem de classes diferentes
   if ( !getClass().isInstance(object) ) return false;

   // Verdadeiro se os objetos tiverem o mesmo nome e mesmo id
   IdentifiedObject o = (IdentifiedObject)object;
   return id.equals(o.getId());

 }
 //*****************************************************************************
 public String toString() {

   return id.toString();

 }
 //*****************************************************************************

}