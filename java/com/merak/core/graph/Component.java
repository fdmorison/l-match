/**
 * @author <a href="mailto:fabricio.dmorison@gmail.com">Fabrício D'Morison</a>
 */

// Package
///////////////
package com.merak.core.graph;

// Imports
///////////////
import java.util.*;
import com.merak.core.*;

// Implementation
/////////////////
public class Component implements Iterable<Vertex> {

 //~Attributes /////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 private List<Vertex> component = new ArrayList<Vertex>();

 //~Constructors ///////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public Component() {
   // default
 }
 /*****************************************************************************/
 public Component(Component source) {

   component.addAll(source.component);

 }

 //~Methods ////////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public void add(Vertex vertex) {

   component.add(0,vertex);

 }
 /*****************************************************************************/
 public boolean has(Vertex vertex) {

   Iterator<Vertex> vertices = component.iterator();
   while (vertices.hasNext()) {
     if (vertex.equals(vertices.next())) return true;
   }
   return false;

 }
 /*****************************************************************************/
 public void remove() {

   component.remove(0);

 }
 /*****************************************************************************/
 public void remove(Vertex vertex) {

   component.remove(vertex);

 }
 /*****************************************************************************/
 public boolean isTargetFrom(Vertex vertex) {

   // Falso se vertice nao tem link suficiente para clicar este componente
   if ( vertex.getOutdegree() < length() ) return false;

   // Falso se este componente tem pelo menos 1 vertice nao apontado por vertex
   Iterator<Vertex> vertices = iterator();
   while (vertices.hasNext()) {
     if ( !vertex.hasLinkTo(vertices.next()) ) return false;
   }
   return true;

 }
 /*****************************************************************************/
 public Graph toGraph() {

   // Auxiliar
   Graph componentGraph = new Graph("ComponentGraph");

   // Adicionar Vertices ao grafo
   for (Vertex vertex : component) {
     vertex.copyTo(componentGraph);
   }

   // Adicionar arestas ao grafo
   for (Vertex vertex : component) {
     for (Link link : vertex.getOutlinks()) {
       link.copyTo(componentGraph,false);
     }
   }
   return componentGraph;

 }
 /*****************************************************************************/
 public Vertex[] toArray() {

   return component.toArray( new Vertex[component.size()] );

 }
 /*****************************************************************************/
 public String toString() {

   // Auxiliar
   String           string   = "{ ";
   Iterator<Vertex> vertices = iterator();

   // Render this object
   while (vertices.hasNext()) {
     string += vertices.next().getKey() + " ";
   }
   return string + "}";

 }
 /*****************************************************************************/
 public int length() {

   return component.size();

 }
 /*****************************************************************************/
 public Iterator<Vertex> iterator() {

   return component.iterator();

 }
 /*****************************************************************************/
 public Component copy() {

   return new Component(this);

 }
 /*****************************************************************************/
 public Object clone() {

   return new Component(this);

 }
 /*****************************************************************************/
 public boolean equals(Object o) {

   return this == o;

 }
 /*****************************************************************************/

}