/**
 * @author <a href="mailto:fabricio.dmorison@gmail.com">Fabrício D'Morison</a>
 */

// Package
///////////////
package com.merak.core.graph.clique;

// Imports
///////////////
import java.util.*;
import com.merak.core.*;
import com.merak.core.graph.*;
import com.merak.core.graph.util.*;

// Implementation
/////////////////
public class OptimalMaximalCliqueEnumerator {

 //~Attributes /////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 private Graph           graph;
 private Marker          linkControl   = new Marker();
 private Marker          vertexControl = new Marker();
 private List<Component> solution      = new ArrayList<Component>();

 //~Constructors ///////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public OptimalMaximalCliqueEnumerator(Graph graph) {

   // Attribute Initialization
   this.graph = graph;

 }

 //~Methods ////////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 /**
  * Verifica se um vertice pode ser incluido em um clique para gerar um clique
  * maior.
  */
 private boolean isElegible(Vertex vertex,Component clique) {

   // Nao se vertex ja esta no clique
   if (vertexControl.discovers(vertex)) return false;

   // Nao se vertex tem menos links que o necessario (lower bound)
   if (vertex.getOutdegree() < clique.length()) return false;
   if (vertex.getIndegree()  < clique.length()) return false;

   // Nao se vertex nao aponta para todo vertice do clique
   for ( Vertex source : clique ) {
     if ( !source.hasLinkTo(vertex) ) return false;
     if ( !vertex.hasLinkTo(source) ) return false;
   }

   // Sim, o vertex eh elegivel
   return true;

 }
 /*****************************************************************************/
 /**
  */
 private void doElect(Vertex vertex,Component clique) {

   // Bloquear todos os links entre vertex e o clique, para que o clique nao seja recalculado
   for ( Vertex source : clique ) {
     linkControl.setFinished( source.getLinkTo(vertex) );
     linkControl.setFinished( vertex.getLinkTo(source) );
   }

   // Expandir o clique
   clique.add(vertex);                  // Insira vertex no clique e
   vertexControl.setDiscovered(vertex); // Marque vertex como estando no clique

 }
 /*****************************************************************************/
 /**
  * Procura o maior clique usando força bruta. Deve-se fornecer inicialmente um
  * um clique trivial (vazio), o qual sera expandido gradativamente durante uma
  * pesquisa em profundidade.
  *
  * @param clique Clique a expandir
  * @param vertex Vertice candidato a expandir o clique
  * @return 'true' se o clique expandiu, 'false' se o clique nao expandiu
  */
 private void expand(Component clique,Vertex vertex,int maximalLength) {

   // Assumir que clique U {vertex} eh maximo ate o momento
   boolean isMaximal = true;

   // 2) EXPANDIR O CLIQUE
   doElect(vertex,clique);

   //  Adjacencia(vertex) impoe uma restricao de tamanho maximo ao clique
   if ( maximalLength > (vertex.getOutdegree()+1) ) {
     maximalLength = vertex.getOutdegree()+1;
   }

   // 3) CONTINUAR EXPANDINDO CLIQUE, SE ELE AINDA FOR EXPANSIVEL
   if (clique.length() < maximalLength) {
     // Tentar expandir clique com a adjacencia de candidate
     for (Link link : vertex.getOutlinks()) {
       // Se a expansao nao eh aceitavel, ignorar vertice adjacente
       if (!isElegible(link.getTarget(),clique)) continue;
       // A expansao eh aceitavel, logo o clique nao eh maximo
       isMaximal = false;
       // Porem, se esta expansao ja foi feita antes, evite recalcula-la
       if (linkControl.finishes(link)) continue;
       // Expandir o clique atraves de link
       expand(clique,link.getTarget(),maximalLength);
     }
   }

   // 4) VERIFICAR SE CLIQUE ATUAL EH MAXIMO
   // Se clique nao foi expandido, entao clique eh maximal
   if (isMaximal) solution.add( clique.copy() );

   // 5) FINALIZAR
   vertexControl.setUnknown(vertex); // Esquecer vertex, pois este pode expandir outro clique posteriormente
   clique.remove();                  // Remover candidate do clique

 }
 /*****************************************************************************/
 public Component[] getDistinctCliques() {

   // Auxiliar
   Component clique = new Component();
   Vertex    vertex = null;

   // Preparando marcadores
   linkControl.ensureCapacity(graph.getNumberOfLinks());
   linkControl.setUnknownToAll();
   vertexControl.ensureCapacity(graph.getNumberOfVertices());
   vertexControl.setUnknownToAll();

   // Procurando cliques
   for (int id=0; id<graph.getNumberOfVertices(); id++) {
     vertex = graph.getVertex(id);
     expand(clique,vertex,vertex.getOutdegree()+1);
   }

   Component[] array = solution.toArray( new Component[solution.size()] );
   solution.clear();
   return array;

 }
 /*****************************************************************************/
 public Component[] getDistinctCliquesOf(Object key) {

   // Auxiliar
   Component clique = new Component();
   Vertex    vertex = graph.getVertex(key);

   // Preparando marcadores
   linkControl.ensureCapacity(graph.getNumberOfLinks());
   linkControl.setUnknownToAll();
   vertexControl.ensureCapacity(graph.getNumberOfVertices());
   vertexControl.setUnknownToAll();

   // Procurando cliques
   expand(clique,vertex,vertex.getOutdegree()+1);

   // Retornando cliques distintos
   return solution.toArray( new Component[solution.size()] );

 }
 /*****************************************************************************/

}