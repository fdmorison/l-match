/**
 * @author <a href="mailto:fabricio.dmorison@gmail.com">Fabrício D'Morison</a>
 */

// Package
///////////////
package com.merak.ai.nlp.dictionary;

// Imports
///////////////
import java.util.*;
import com.merak.core.*;

// Implementation
/////////////////
public class XWord extends IdentifiedObject implements Iterable<XSynset> {

 //~Attributes /////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 private String        lemma    = null;
 private List<XSynset> meanings = new ArrayList<XSynset>();

 //~Constructors ///////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public XWord(int id,String lemma) {

   // Super Attribute Initialization
   super(id);

   // Attribute Initialization
   this.lemma = lemma;

 }
 //~Methods ////////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public String getLemma() {

   return lemma;

 }
 /*****************************************************************************/
 public XSynset getMeaning(int i) {

   return meanings.get(i);

 }
 /*****************************************************************************/
 public List<XSynset> getMeanings() {

   return meanings;

 }
 /*****************************************************************************/
 public int getNumberOfMeanings() {

   return meanings.size();

 }
 /*****************************************************************************/
 public void addMeaning(XSynset synset) {

   meanings.add(synset);

 }
 /*****************************************************************************/
 public XSynset[] createMeaningArray() {

   return meanings.toArray( new XSynset[meanings.size()] );

 }
 /*****************************************************************************/
 public void removeMeaning(XSynset synset) {

   meanings.remove(synset);

 }
 /*****************************************************************************/
 public boolean isAmbiguous() {

   return meanings.size() > 1;

 }
 /*****************************************************************************/
 public Iterator<XSynset> iterator() {

   return meanings.iterator();

 }
 /*****************************************************************************/
 public boolean equals(Object o) {

   return (o instanceof XWord) && lemma.equals( ((XWord)o).lemma );

 }
 /*****************************************************************************/
 public Object clone() {

   return new XWord( getId(), lemma );

 }
 /*****************************************************************************/
 public String toString() {

   String string = lemma+" : ";
   for (XSynset synset : meanings) {
     string += synset + " ";
   }
   return string;

 }
 /*****************************************************************************/
}
