/**
 * @author <a href="mailto:fabricio.dmorison@gmail.com">Fabrício D'Morison</a>
 */

// Package
///////////////
package com.merak.core.graph;

// Imports
///////////////
import java.util.*;
import com.merak.core.IdentifiedObject;

// Implementation
/////////////////
public abstract class GraphElement extends IdentifiedObject {

 //~Constants //////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public static final double INFINITY = 1.0D/0.0D;

 //~Attributes /////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 private Object key;
 private double cost;
 private double demand;

 //~Constructors ///////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public GraphElement(int id,Object key) {

   // Super Attribute Initialization
   super(id);

   // Attribute Initialization
   this.key    = key;
   this.cost   = 0.0;
   this.demand = 0.0;

 }
 /*****************************************************************************/
 public GraphElement(int id,Object key,double demand,double cost) {

   // Super Attribute Initialization
   super(id);

   // Attribute Initialization
   this.key    = key;
   this.demand = demand;
   this.cost   = cost;

 }

 //~Methods ////////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public abstract Graph getGraph();

 /*****************************************************************************/
 public Object getKey() {

   return key;

 }
 /*****************************************************************************/
 public double getCost() {

   return cost;

 }
 /*****************************************************************************/
 public double getDemand() {

   return demand;

 }
 /*****************************************************************************/
 public void setCost(double value) {

   cost = value;

 }
 /*****************************************************************************/
 public void setDemand(double value) {

   demand = value;

 }
 /*****************************************************************************/
 public boolean equals(Object obj) {

   return this == obj;

 }
 /*****************************************************************************/

}