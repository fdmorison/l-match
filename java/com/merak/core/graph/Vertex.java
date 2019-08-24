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
public class Vertex extends GraphElement {

 //~Attributes /////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 private Graph           graph     = null;
 private ArrayList<Link> outlinks  = new ArrayList<Link>();
 private int             degree    = 0;
 private int             indegree  = 0;
 private int             outdegree = 0;

 //~Constructors ///////////////////////////////////////////////////////////////
 /*****************************************************************************/
 protected Vertex(int id,Object key,Graph g) {

   // Super Attribute Initialization
   super(id,key);

   // Attribute Initialization
   this.graph  = g;

 }

 //~Methods ////////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 protected void addOutlink(Link link) {

   // Auxiliar
   Vertex target = link.getTarget();

   if (link.isLoop()) System.out.println("Loop: "+link);

   // Atualizar Grau do Vertice
   if (!hasLinkTo(target)) {
      outdegree++;
      target.indegree++;
      if (!target.hasLinkTo(this)) {
        degree++;
        if (target!=this) target.degree++;
      }
   }

   // Adicionar o link na saida
   outlinks.add(link);

 }
 /*****************************************************************************/
 public Graph getGraph() {

   return graph;

 }
/*****************************************************************************/
 public int getDegree() {

   return degree;

 }
 /*****************************************************************************/
 public int getIndegree() {

   return indegree;

 }
 /*****************************************************************************/
 public int getOutdegree() {

   return outdegree;

 }
 /*****************************************************************************/
 public Link[] getOutlinks() {

  return outlinks.toArray( new Link[outlinks.size()] );

 }
 /*****************************************************************************/
 public Vertex[] getTargets() {

  // Auxiliar
  Vertex[]       targets = new Vertex[outlinks.size()];
  Iterator<Link> links   = outlinks.iterator();

  for (int i=0; links.hasNext(); i++) {
    targets[i] = links.next().getTarget();
  }
  return targets;

 }
 /*****************************************************************************/
 /**
  * Procura por link com determinado destino
  * @param target o destino
  */
 public Link getLinkTo(Vertex target) {

  for (Link link : outlinks) {
    if ( link.getTarget().equals(target) ) return link;
  }
  return null;

 }
 /*****************************************************************************/
 /**
  * Verifica se existe link com determinado destino
  * @param target o destino
  */
 public boolean hasLinkTo(Vertex target) {

   return getLinkTo(target) != null;

 }
 /*****************************************************************************/
 public boolean hasLinkToAll(List<Vertex> vertices) {

   for (Vertex target : vertices) {
     if (equals(target)    ) continue;
     if (!hasLinkTo(target)) return false;
   }
   return true;

 }
 /*****************************************************************************/
 public Link linkTo(Object targetKey,Object linkKey) {

   return graph.createLink(getKey(),targetKey,linkKey);

 }
 /*****************************************************************************/
 public Vertex copyTo(Graph graph) {

   // Criar um vertice similar ao exemplo
   Vertex vertex = graph.createVertex( getKey() );
   if (vertex==null) return null;

   // Copiar atributos
   vertex.setCost( getCost() );
   vertex.setDemand( getDemand() );
   return vertex;

 }
 /*****************************************************************************/
 public String toString() {

   String string = getKey()+"("+degree+")"+" -> { ";
   for (Link link : outlinks) {
     string += link.getTarget().getKey() + " ";
   }
   return string + "}";

 }
 /*****************************************************************************/

}
