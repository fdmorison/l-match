/**
 * @author <a href="mailto:fabricio.dmorison@gmail.com">Fabrício D'Morison</a>
 */

// Package
///////////////
package com.merak.core.graph.util;

// Imports
///////////////
import java.util.*;
import com.merak.core.graph.Vertex;

public class VertexDegreeComparator implements Comparator {

 //~Attributes /////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 private final boolean ascendent;

 //~Constructors ///////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public VertexDegreeComparator(boolean ascendent) {

   // Attribute Initialization
   this.ascendent = ascendent;

 }

 //~Methods ////////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public int compare(Object v,Object u) {

   if (ascendent) {
     return ((Vertex)v).getDegree() - ((Vertex)u).getDegree();
   }
   else {
     return ((Vertex)u).getDegree() - ((Vertex)v).getDegree();
   }

 }
 /*****************************************************************************/

}