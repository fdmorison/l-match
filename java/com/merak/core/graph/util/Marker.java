/**
 * @author <a href="mailto:fabricio.dmorison@gmail.com">Fabrício D'Morison</a>
 */

// Package
///////////////
package com.merak.core.graph.util;

// Imports
///////////////
import java.util.*;
import com.merak.core.graph.*;

// Implementation
/////////////////
public class Marker {

 //~Inner Classes //////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public enum Status { UNKNOWN, DISCOVERED, FINISHED }

 //~Attributes /////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 private ArrayList<Status> marks = new ArrayList<Status>();

 //~Constructors ///////////////////////////////////////////////////////////////
 /*****************************************************************************/
 //~Methods ////////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public void ensureCapacity(int value) {

   marks.ensureCapacity(value);
   while (marks.size()<value) {
     marks.add(Status.UNKNOWN);
   }

 }
 /*****************************************************************************/
 public boolean knows(GraphElement element) {

   return marks.get(element.getId()) != Status.UNKNOWN;

 }
 /*****************************************************************************/
 public boolean discovers(GraphElement element) {

   return marks.get(element.getId()) == Status.DISCOVERED;

 }
 /*****************************************************************************/
 public boolean finishes(GraphElement element) {

   return marks.get(element.getId()) == Status.FINISHED;

 }
 /*****************************************************************************/
 public void setUnknown(GraphElement element) {

   marks.set(element.getId(),Status.UNKNOWN);

 }
 /*****************************************************************************/
 public void setUnknownToAll() {

   for (int id=0; id<marks.size(); id++) marks.set(id,Status.UNKNOWN);

 }
 /*****************************************************************************/
 public void setUnknownWhere(Status status) {

   for (int id=0; id<marks.size(); id++) {
     if (marks.get(id)==status) marks.set(id,Status.UNKNOWN);
   }

 }
 /*****************************************************************************/
 public void setUnknownWhereNot(Status status) {

   for (int id=0; id<marks.size(); id++) {
     if (marks.get(id)!=status) marks.set(id,Status.UNKNOWN);
   }

 }
 /*****************************************************************************/
 public void setDiscovered(GraphElement element) {

   marks.set(element.getId(),Status.DISCOVERED);

 }
 /*****************************************************************************/
 public void setDiscoveredToAll() {

   for (int id=0; id<marks.size(); id++) marks.set(id,Status.DISCOVERED);

 }
 /*****************************************************************************/
 public void setDiscoveredWhere(Status status) {

   for (int id=0; id<marks.size(); id++) {
     if (marks.get(id)==status) marks.set(id,Status.DISCOVERED);
   }

 }
 /*****************************************************************************/
 public void setDiscoveredWhereNot(Status status) {

   for (int id=0; id<marks.size(); id++) {
     if (marks.get(id)!=status) marks.set(id,Status.DISCOVERED);
   }

 }
 /*****************************************************************************/
 public void setFinished(GraphElement element) {

   marks.set(element.getId(),Status.FINISHED);

 }
 /*****************************************************************************/
 public void setFinishedToAll() {

   for (int id=0; id<marks.size(); id++) marks.set(id,Status.FINISHED);

 }
 /*****************************************************************************/
 public void setFinishedWhere(Status status) {

   for (int id=0; id<marks.size(); id++) {
     if (marks.get(id)==status) marks.set(id,Status.FINISHED);
   }

 }
 /*****************************************************************************/
 public void setFinishedWhereNot(Status status) {

   for (int id=0; id<marks.size(); id++) {
     if (marks.get(id)!=status) marks.set(id,Status.FINISHED);
   }

 }
 /*****************************************************************************/



}