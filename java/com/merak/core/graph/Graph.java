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
import com.merak.core.graph.util.*;

// Implementation
/////////////////
public class Graph extends NamedObject {

 //~Attributes /////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 private int                    vertex_id_seq  = 0;
 private int                    link_id_seq    = 0;
 private HashMap<Object,Vertex> vertexKeyMap   = new HashMap<Object,Vertex>();
 private ArrayList<Vertex>      vertexIdMap    = new ArrayList<Vertex>();
 private ArrayList<Link>        linkIdMap      = new ArrayList<Link>();
 private boolean                skipSelfLoops  = true;

 //~Constructors ///////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public Graph(String name) {

   super( (name!=null) ? name : "Unnamed Graph" );

 }

 //~Methods ////////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public Vertex createVertex(Object key) {

   if ( vertexKeyMap.containsKey(key) ) return null;

   // Auxiliar
   int    id     = vertex_id_seq++;
   Vertex vertex = new Vertex(id,key,this);

   // Store new vertex
   vertexKeyMap.put(key,vertex);
   vertexIdMap.add(vertex);

   // Return
   return vertex;

 }
 /*****************************************************************************/
 public Link createLink(Object sourceKey,Object targetKey,Object linkKey) {

  // Skip Self Loops, if flaged
  if (skipSelfLoops && sourceKey.equals(targetKey)) {
    return null;
  }

  // Searching for the source and target vertices
  Vertex source = getVertex(sourceKey); if (source==null) return null;
  Vertex target = getVertex(targetKey); if (target==null) return null;

  // Built, Store and Return the link
  Link link = new Link(link_id_seq++,linkKey,source,target);
  linkIdMap.add(link);
  source.addOutlink(link);

  // Return the created link
  return link;

 }
 /*****************************************************************************/
 public GraphWalker createWalker() {

  return new GraphWalker(this);

 }
 /*****************************************************************************/
 public Graph createTransposeGraph() {

  // Auxiliar
  Graph transposeGraph = new Graph( "Transpose("+getName()+")" );

  // Copiando Vertices no Grafo Transposto
  for (int id=0; id<getNumberOfVertices(); id++) {
    this.getVertex(id).copyTo(transposeGraph);
  }

  // Invertendo e Copiando Links
  for (int id=0; id<getNumberOfVertices(); id++) {
    for (Link link : this.getVertex(id).getOutlinks()) {
      link.copyTo(transposeGraph,true);
    }
  }

  // Retornando o grafo complementar
  return transposeGraph;

 }
 /*****************************************************************************/
 public Graph createUndirectedGraph() {

  // Auxiliar
  Graph undirectedGraph = new Graph( "Undirected("+getName()+")" );

  // Copiando Vertices no Grafo Nao-Direcionado
  for (int id=0; id<getNumberOfVertices(); id++) {
    this.getVertex(id).copyTo(undirectedGraph);
  }

  // Copiando e duplicando o sentido dos Links
  for (int id=0; id<getNumberOfVertices(); id++) {
    for (Link link : this.getVertex(id).getOutlinks()) {
      link.copyTo(undirectedGraph,true );  // Copiando o link original
      link.copyTo(undirectedGraph,false);  // Copiando o link original invertido
    }
  }

  // Retornando o grafo complementar
  return undirectedGraph;

 }
 /*****************************************************************************/
 public Vertex getVertex(int id) {

  return vertexIdMap.get(id);

 }
 /*****************************************************************************/
 public Vertex getVertex(Object key) {

  return vertexKeyMap.get(key);

 }
 /*****************************************************************************/
 public Vertex[] getVertices() {

  return vertexIdMap.toArray( new Vertex[vertexIdMap.size()] );

 }
 /*****************************************************************************/
 public Link getLink(int id) {

  return linkIdMap.get(id);

 }
 /*****************************************************************************/
 public Link getLink(Object sourceKey,Object targetKey) {

  // Auxiliar
  Vertex source = getVertex(sourceKey); if (source==null) return null;
  Vertex target = getVertex(targetKey); if (target==null) return null;

  // Find and return the link, if exists
  return source.getLinkTo(target);

 }
 /*****************************************************************************/
 public Link[] getLinks() {

   return linkIdMap.toArray( new Link[linkIdMap.size()] );

 }
 /*****************************************************************************/
 public List[] getStronglyConnectedComponents(Object startKey) {

  // 1. Ordenar vertices por tempos de termino
  List rank = createWalker().topologicalSort();

  // 2. Caminhar em profundidade em todos os vértices do Grafo Complementar
  //    na ordem dada por (1)
  return createTransposeGraph().createWalker().depthFirstSearch(rank);

 }
 /*****************************************************************************/
 public int getNumberOfVertices() {

   return vertex_id_seq;

 }
 /*****************************************************************************/
 public int getNumberOfLinks() {

   return link_id_seq;

 }
 /*****************************************************************************/
 public void setSkipSelfLoops(boolean flag) {

   skipSelfLoops = flag;

 }
 /*****************************************************************************/
 public String toString() {

   // Auxiliar
   Vertex[] array  = getVertices();
   String   string = "G: "+getName()+", |V|="+getNumberOfVertices()+", |E|="+getNumberOfLinks()+", {\n";

   for (int id=0; id<getNumberOfVertices(); id++) {
     string += getVertex(id) + "\n";
   }

   return string + "}";

 }
 /*****************************************************************************/

}
