/**
 * @author <a href="mailto:fabricio.dmorison@gmail.com">Fabr�cio D'Morison</a>
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
  * @param vertexId v�rtice a partir do qual a busca deve iniciar
  * @param tree     lista para inserir v�rtices na ordem em que s�o descobertos
  * @param visited  flags que marcam cada v�rtice como descoberto ou n�o
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
  * @param vertexId v�rtice a partir do qual a busca deve iniciar
  * @param tree     lista para inserir v�rtices na ordem em que s�o descobertos
  * @param visited  flags que marcam cada v�rtice como descoberto ou n�o
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
  * um v�tice espec�fico.
  * @param rootVertexKey identifica o v�rtice a partir do qual a busca deve iniciar
  * @return uma lista com os v�rtices na ordem em que foram descobertos
  */
 public List depthFirstSearch(Object rootVertexKey) {

  // Declarando algumas vari�veis
  LinkedList tree   = new LinkedList();
  Vertex     vertex = graph.getVertex(rootVertexKey);

  // Marcando todos os v�rtices como n�o visitados
  resetVisited();

  // Fazendo caminhamento recursivo usando pilha
  depthFirstSearchByDiscoverTime(vertex.getId(),tree);

  // Retornando apenas os v�rtices encontrados a partir do ponto inicial
  return tree;

 }
 /*****************************************************************************/
 /**
  * Faz um caminhamento Primeiro na Profundidade no grafo considerando todos os
  * v�rtices na ordem em que est�o dispostos na lista de entrada
  * @return uma floresta de �rvores (listas)
  */
 public List[] depthFirstSearch(List rank) {

  LinkedList forest = new LinkedList();
  LinkedList tree   = null;
  Vertex     vertex = null;
  int        i;

  // Marcando todos os v�rtices como n�o visitados
  resetVisited();

  // Fazendo caminhamento recursivo usando pilha em todos os v�rtices
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
  * todos os v�rtices.
  * @return uma floresta de �rvores (listas) com v�rtices na ordem de descoberta
  */
 public List[] depthFirstSearch() {

  LinkedList forest  = new LinkedList();
  LinkedList tree    = null;
  int        i;

  // Marcando todos os v�rtices como n�o visitados
  resetVisited();

  // Fazendo caminhamento recursivo usando pilha em todos os v�rtices
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
  * Faz uma ordena��o topol�gica sobre os v�rtices do grafo
  * @return uma lista de v�rtices na ordem topol�gica
  */
 public List topologicalSort() {

  LinkedList sortedList = new LinkedList();

  // Marcando todos os v�rtices como n�o visitados
  resetVisited();

  // Fazendo caminhamento recursivo usando pilha em todos os v�rtices
  for (int i=0; i<visited.length; i++) {
   if (visited[i]) continue;
   depthFirstSearchByDecrescentFinishTime(i,sortedList);
  }

  // Retornando v�rtices na ordem topol�gica
  return sortedList;

 }
 /*****************************************************************************/
 /**
  * Faz um caminhamento Primeiro na Largura no grafo
  * @return uma lista com os v�rtices na ordem em que s�o encontrados
  */
 public List breadthFirstSearch(Object rootVertexKey) {

  Vertex     vertex  = graph.getVertex(rootVertexKey);
  Link[]     links   = null;
  LinkedList queue   = new LinkedList();
  LinkedList tree    = new LinkedList();

  // Marcando todos os v�rtices como n�o visitados
  resetVisited();

  // Preparando primeiro v�rtice
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