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
public class GraphWalker {

 //~Attributes /////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 private Graph     graph;
 private boolean[] visited;

 //~Constructors ///////////////////////////////////////////////////////////////
 /*****************************************************************************/
 protected GraphWalker(Graph graph) {

  this.graph = graph;

 }

 //~Methods ////////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 /**
  * Redimensiona e reseta o vetor com as flags de visita
  */
 private void resetVisited() {

  visited = new boolean[graph.getNumberOfVertices()];
  for (int i=0; i<visited.length; i++) visited[i]=false;

 }
 /*****************************************************************************/
 /**
  * Faz um caminhamento recursivo Primeiro na Profundidade no grafo
  * @param vertexId vértice a partir do qual a busca deve iniciar
  * @param tree     lista para inserir vértices na ordem em que são descobertos
  * @param visited  flags que marcam cada vértice como descoberto ou não
  */
 private void depthFirstSearchByDiscoverTime(int vertexId,LinkedList tree) {

  Vertex destiny;
  Vertex vertex     = graph.getVertex(vertexId);
  Link[] links      = vertex.getOutlinks();
  visited[vertexId] = true;

  tree.addLast(vertex);
  for (int i=0; i<links.length; i++) {
   destiny = links[i].getTarget();
   if (visited[destiny.getId()]) continue;
   depthFirstSearchByDiscoverTime(destiny.getId(),tree);
  }

 }
 /*****************************************************************************/
 /**
  * Faz um caminhamento recursivo Primeiro na Profundidade no grafo
  * @param vertexId vértice a partir do qual a busca deve iniciar
  * @param tree     lista para inserir vértices na ordem em que são descobertos
  * @param visited  flags que marcam cada vértice como descoberto ou não
  */
 private void depthFirstSearchByDecrescentFinishTime(int vertexId,LinkedList tree) {

  Vertex destiny;
  Vertex vertex     = graph.getVertex(vertexId);
  Link[] links      = vertex.getOutlinks();
  visited[vertexId] = true;

  for (int i=0; i<links.length; i++) {
   destiny = links[i].getTarget();
   if (visited[destiny.getId()]) continue;
   depthFirstSearchByDecrescentFinishTime(destiny.getId(),tree);
  }
  tree.addFirst(vertex);

 }
 /*****************************************************************************/
 /**
  * Prepara um caminhamento Primeiro na Profundidade no grafo a partir de apenas
  * um vétice específico.
  * @param rootVertexKey identifica o vértice a partir do qual a busca deve iniciar
  * @return uma lista com os vértices na ordem em que foram descobertos
  */
 public List depthFirstSearch(Object rootVertexKey) {

  // Declarando algumas variáveis
  LinkedList tree   = new LinkedList();
  Vertex     vertex = graph.getVertex(rootVertexKey);

  // Marcando todos os vértices como não visitados
  resetVisited();

  // Fazendo caminhamento recursivo usando pilha
  depthFirstSearchByDiscoverTime(vertex.getId(),tree);

  // Retornando apenas os vértices encontrados a partir do ponto inicial
  return tree;

 }
 /*****************************************************************************/
 /**
  * Faz um caminhamento Primeiro na Profundidade no grafo considerando todos os
  * vértices na ordem em que estão dispostos na lista de entrada
  * @return uma floresta de árvores (listas)
  */
 public List[] depthFirstSearch(List rank) {

  LinkedList forest = new LinkedList();
  LinkedList tree   = null;
  Vertex     vertex = null;
  int        i;

  // Marcando todos os vértices como não visitados
  resetVisited();

  // Fazendo caminhamento recursivo usando pilha em todos os vértices
  Iterator iter = rank.iterator();
  while (iter.hasNext()) {
   vertex = (Vertex)iter.next();
   if (visited[vertex.getId()]) continue;
   tree = new LinkedList();
   depthFirstSearchByDiscoverTime(vertex.getId(),tree);
   forest.add(tree);
  }

  // Retornando floresta
  return (List[]) forest.toArray( new List[0] );

 }
 /*****************************************************************************/
 /**
  * Faz um caminhamento Primeiro na Profundidade no grafo considerando
  * todos os vértices.
  * @return uma floresta de árvores (listas) com vértices na ordem de descoberta
  */
 public List[] depthFirstSearch() {

  LinkedList forest  = new LinkedList();
  LinkedList tree    = null;
  int        i;

  // Marcando todos os vértices como não visitados
  resetVisited();

  // Fazendo caminhamento recursivo usando pilha em todos os vértices
  for (i=0; i<visited.length; i++) {
   if (visited[i]) continue;
   tree = new LinkedList();
   depthFirstSearchByDiscoverTime(i,tree);
   forest.add(tree);
  }

  // Retornando floresta
  return (List[]) forest.toArray( new List[0] );

 }
 /*****************************************************************************/
 /**
  * Faz uma ordenação topológica sobre os vértices do grafo
  * @return uma lista de vértices na ordem topológica
  */
 public List topologicalSort() {

  LinkedList sortedList = new LinkedList();

  // Marcando todos os vértices como não visitados
  resetVisited();

  // Fazendo caminhamento recursivo usando pilha em todos os vértices
  for (int i=0; i<visited.length; i++) {
   if (visited[i]) continue;
   depthFirstSearchByDecrescentFinishTime(i,sortedList);
  }

  // Retornando vértices na ordem topológica
  return sortedList;

 }
 /*****************************************************************************/
 /**
  * Faz um caminhamento Primeiro na Largura no grafo
  * @return uma lista com os vértices na ordem em que são encontrados
  */
 public List breadthFirstSearch(Object rootVertexKey) {

  Vertex     vertex  = graph.getVertex(rootVertexKey);
  Link[]     links   = null;
  LinkedList queue   = new LinkedList();
  LinkedList tree    = new LinkedList();

  // Marcando todos os vértices como não visitados
  resetVisited();

  // Preparando primeiro vértice
  visited[vertex.getId()] = true;
  queue.add(vertex);

  // Fazendo a pesquisa
  while (!queue.isEmpty()) {
   vertex = (Vertex) queue.removeFirst();
   links  = vertex.getOutlinks();
   tree.add(vertex);
   for (int i=0; i<links.length; i++) {
   	vertex = links[i].getTarget();
  	if (visited[vertex.getId()]) continue;
  	visited[vertex.getId()] = true;
  	queue.addLast(vertex);
   }
  }

  return tree;

 }
 /*****************************************************************************/
}