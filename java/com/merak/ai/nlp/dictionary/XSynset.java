/**
 * @author <a href="mailto:fabricio.dmorison@gmail.com">Fabr�cio D'Morison</a>
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
public class XSynset extends IdentifiedObject implements Iterable<XWord> {

 //~Attributes /////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 private List<XWord> words = new ArrayList<XWord>();

 //~Constructors ///////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public XSynset(int id) {

   // Super Attribute Initialization
   super(id);

 }

 //~Methods ////////////////////////////////////////////////////////////////////
 /*****************************************************************************/
 public void addWord(XWord word) {

   words.add(word);

 }
 /*****************************************************************************/
 public XWord getWord(int i) {

   return words.get(i);

 }
 /*****************************************************************************/
 public XWord[] getWords() {

   return words.toArray( new XWord[words.size()] );

 }
 /*****************************************************************************/
 public int getNumberOfWords() {

   return words.size();

 }
 /*****************************************************************************/
 public double getPolysemyFactor() {

   // Auxiliar
   int overlappingMeanings = 0;
   int ambiguousWords      = 0;

   double[] overlap   = new double[ getNumberOfWords() ];
   double[] ambiguity = new double[ getNumberOfWords() ];
   int      i         = 0;

   // Counting overlapping meanings and ambiguous words
   for (XWord w : words) {
     overlappingMeanings += w.getNumberOfMeanings();
     if (w.isAmbiguous()) ambiguousWords++;
     overlap[i]   = w.getNumberOfMeanings();
     ambiguity[i] = w.isAmbiguous() ? 1 : 0 ;
   }

   double entropy1 = 1 - com.merak.ontomap.classification.StatTools.getEntropy( overlap   );
   double entropy2 = 1 - com.merak.ontomap.classification.StatTools.getEntropy( ambiguity );
   double entropy  = 1 - (1-entropy1)*(1-entropy2);

   // Measure polysemy
   double factor1 = ambiguousWords / (double)getNumberOfWords();
   double factor2 = 1 - (getNumberOfWords() / (double)overlappingMeanings);
   double factor  = 1 - (1-factor1)*(1-factor2);

   //return entropy;
   return entropy*factor;

 }
 /*****************************************************************************/
 public void removeWord(XWord word) {

   words.remove(word);

 }
 /*****************************************************************************/
 public XWord[] createWordArray() {

   return words.toArray( new XWord[words.size()] );

 }
 /*****************************************************************************/
 public String[] createLemmaArray() {

   String[] lemmaArray = new String[words.size()];
   for (int i=0; i<words.size(); i++) {
     lemmaArray[i] = words.get(i).getLemma();
   }
   return lemmaArray;

 }
 /*****************************************************************************/
 public boolean isAmbiguous() {

   for (XWord w : words) {
     if ( w.isAmbiguous() ) return true;
   }
   return false;

 }
 /*****************************************************************************/
 public Iterator<XWord> iterator() {

   return words.iterator();

 }
 /*****************************************************************************/
 public boolean equals(Object o) {

   return this==o;

 }
 /*****************************************************************************/
 public String toString() {

   String string = "{ ";
   for (XWord w : words) {
     string += w.getLemma() + " ";
   }
   return string+"}";

 }
 /*****************************************************************************/
}