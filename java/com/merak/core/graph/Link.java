/**
 * @author <a href="mailto:fabricio.dmorison@gmail.com">Fabrício D'Morison</a>
 */

// Package
///////////////
package com.merak.core.graph;

// Imports
///////////////
import java.util.*;

// Implementation
/////////////////
public class Link extends GraphElement {

 //~Attributes /////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 private Vertex source;
 private Vertex target;

 //~Constructors ///////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public Link(int id,Object key,Vertex source,Vertex target) {

   // Super Attribute Initialization
   super(id,key);

   // Attribute Initialization
   this.source = source;
   this.target = target;

 }

 //~Methods ////////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public Graph getGraph() {

   return source.getGraph();

 }
 /*****************************************************************************/
 public Vertex getSource() {

   return source;

 }
 /*****************************************************************************/
 public Vertex getTarget() {

   return target;

 }
 /*****************************************************************************/
 public boolean hasSource(Vertex vertex) {

   return source.equals(vertex);

 }
 /*****************************************************************************/
 public boolean hasTarget(Vertex vertex) {

   return target.equals(vertex);

 }
 /*****************************************************************************/
 public boolean isLoop() {

   return source.equals(target);

 }
 /*****************************************************************************/
 public boolean isInfinite() {

   return getCost() == INFINITY;

 }
 /*****************************************************************************/
 public Link copyTo(Graph g,boolean invert) {

   // Auxiliar
   Link link;

   // Copiar o link, invertendo seu sentido se necessario
   if (invert) {
     link = g.createLink(target.getKey(),source.getKey(),getKey());
   }
   else {
     link = g.createLink(source.getKey(),target.getKey(),getKey());
   }
   if (link==null) return null;

   // Copiar atributos do link
   link.setDemand(getDemand());
   link.setCost(getCost());
   return link;

 }
 /*****************************************************************************/
 public boolean equals(Object o) {

   return this == o;

 }
 /*****************************************************************************/
 public String toString() {

   return source.getKey() + "-("+getCost()+")->" + target.getKey();

 }
 /*****************************************************************************/

}
