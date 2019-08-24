// Package
///////////////
package com.merak.ai.nlp.dictionary.wordnet;

// Imports
///////////////
import java.io.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.*;
import net.didion.jwnl.dictionary.Dictionary;

import com.merak.core.Filter;
import com.merak.core.Application;
import com.merak.core.graph.*;
import com.merak.core.graph.clique.*;

import com.merak.ai.nlp.dictionary.util.NameTokenizer;
import com.merak.ai.nlp.dictionary.TaxonomicDictionary;

import com.merak.ontomap.model.extraction.vocabulary.NameTools;

// Public Class
///////////////
public class JWordNetDictionary implements TaxonomicDictionary {

  //~ Constants ////////////////////////////////////////////////////////////////
  //****************************************************************************
  private static final String DEFAULT_PROPERTY_PATH = Application.getInstance().getPluginPath("jwln_properties.xml");

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final Dictionary     dictionary;
  private final NameTokenizer  tokenizer;
  private final Set<String>    lemmaMap = new HashSet<String>();
  private       Filter<String> filter   = Filter.getTrueInstance();

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public JWordNetDictionary(NameTokenizer tokenizer)
    throws JWNLException,FileNotFoundException
  {

    // Initialize the singleton JWordNet, if not yet
    if ( !JWNL.isInitialized() ) {
      JWNL.initialize( new FileInputStream(DEFAULT_PROPERTY_PATH) );
    }

    // Attribute Initialization
    this.dictionary = Dictionary.getInstance(); // Load Dictionary
    this.tokenizer  = tokenizer;

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private Synset[] getSensesOf(String lemma,POS pos) {

    // check error
    try {
      IndexWord word = dictionary.lookupIndexWord(pos,lemma);
      if (word!=null) return word.getSenses();
    }
    catch (JWNLException ex) {
      Application.debug.print("JWordNetDictionary.getSensesOf(String,POS)",ex);
    }
    return new Synset[0];

  }
  //****************************************************************************
  private void getSimilarTo(Synset source,Set<Synset> set) {

    // Auxiliar
    List<Synset> control = new LinkedList<Synset>();
    Synset       similar = null;
    Synset[]     synsets = null;

    // Prepare the queue
    control.add( source );

    // Select unique similar senses
    try {
      while ( !control.isEmpty() ) {
        similar = control.remove(0);
        if (set.contains(similar)) continue;
        set.add(similar);
        for (Pointer p : similar.getPointers(PointerType.SIMILAR_TO) ) {
          control.add( p.getTargetSynset() );
        }
      }
    }
    catch (Exception ex) {
      Application.debug.print("JWordNetDictionary.getSimilarTo(Synset,Set<Synset>)",ex);
    }

  }
  //****************************************************************************
  private void getSeeAlso(Synset source,Set<Synset> set) {

    // Auxiliar
    List<Synset> control = new LinkedList<Synset>();
    Synset       similar = null;
    Synset[]     synsets = null;

    // Prepare the queue
    control.add( source );

    // Select unique similar senses
    try {
      // Prepare the queue
      for (Pointer p : source.getPointers(PointerType.SEE_ALSO) ) {
        control.add( p.getTargetSynset() );
      }
      while ( !control.isEmpty() ) {
        similar = control.remove(0);
        if (set.contains(similar)) continue;
        set.add(similar);
        for (Pointer p : similar.getPointers(PointerType.SIMILAR_TO) ) {
          control.add( p.getTargetSynset() );
        }
      }
    }
    catch (Exception ex) {
      Application.debug.print("JWordNetDictionary.getSimilarTo(Synset,Set<Synset>)",ex);
    }

  }
  //****************************************************************************
  private void getDifferentTo(Synset source,Set<Synset> set) {

    // Auxiliar
    List<Synset> control = new LinkedList<Synset>();
    Synset       similar = null;
    Synset[]     synsets = null;

    try {
      // Prepare the queue
      for (Pointer p : source.getPointers(PointerType.ANTONYM) ) {
        control.add( p.getTargetSynset() );
      }
      // Select unique similar senses
      while ( !control.isEmpty() ) {
        similar = control.remove(0);
        if (set.contains(similar)) continue;
        set.add(similar);
        for (Pointer p : similar.getPointers(PointerType.SIMILAR_TO) ) {
          control.add( p.getTargetSynset() );
        }
      }
    }
    catch (Exception ex) {
      Application.debug.print("JWordNetDictionary.getDifferentTo(Synset,Set<Synset>)",ex);
    }

  }
  //****************************************************************************
  private void getMoreGeneralTo(Synset source,Set<Synset> set) {

    // Auxiliar
    List<Synset> control = new LinkedList<Synset>();
    Synset       similar = null;
    Synset[]     synsets = null;

    try {
      // Prepare the queue
      for (Pointer p : source.getPointers(PointerType.HYPERNYM) ) {
        control.add( p.getTargetSynset() );
      }
      // Select unique similar senses
      while ( !control.isEmpty() ) {
        similar = control.remove(0);
        if (set.contains(similar)) continue;
        set.add(similar);
        for (Pointer p : similar.getPointers(PointerType.HYPERNYM) ) {
          control.add( p.getTargetSynset() );
        }
      }
    }
    catch (Exception ex) {
      Application.debug.print("JWordNetDictionary.getMoreGeneralTo(Synset,Set<Synset>)",ex);
    }

  }
  //****************************************************************************
  private String[] getUniqueWords(Set<Synset> senseMap) {

    // Auxiliar
    String[] lemmas = null;

    // For each unique synset, do
    for (Synset synset : senseMap) {
      // Select acceptable words from the synset
      for (Word word : synset.getWords()) {
        lemmas = tokenizer.getTokens( word.getLemma() );
        for (String lemma: lemmas) {
          if (filter.accept(lemma)) lemmaMap.add(lemma);
        }
      }
    }

    // Return words inside an array
    lemmas = lemmaMap.toArray( new String[lemmaMap.size()] );
    lemmaMap.clear();
    return lemmas;

  }
  //****************************************************************************
  public void setFilter(Filter<String> filter) {

    if (filter!=null) this.filter = filter;

  }
  //****************************************************************************
  public String[] getSynonymsOf(String lemma){

    // Auxiliar
    Set<Synset> senseMap = new HashSet<Synset>();

    // Find synonym of nouns and adjectives
    for (Synset synset : getSensesOf(lemma,POS.NOUN     )) { getSimilarTo(synset,senseMap); getSeeAlso(synset,senseMap); }
    for (Synset synset : getSensesOf(lemma,POS.ADJECTIVE)) { getSimilarTo(synset,senseMap); getSeeAlso(synset,senseMap); }

    // Find synonym of verbs and adverbs, if nothing has been found previously
    for (Synset synset : getSensesOf(lemma,POS.VERB  )) { getSimilarTo(synset,senseMap); getSeeAlso(synset,senseMap); }
    for (Synset synset : getSensesOf(lemma,POS.ADVERB)) { getSimilarTo(synset,senseMap); getSeeAlso(synset,senseMap); }

    // Return unique words
    return getUniqueWords( senseMap );

  }
  //****************************************************************************
  public String[] getAntonymsOf(String lemma){

    // Auxiliar
    HashSet<Synset> senseMap = new HashSet<Synset>();

    // Find synonym of nouns and adjectives
    for (Synset synset : getSensesOf(lemma,POS.NOUN     )) getDifferentTo(synset,senseMap);
    for (Synset synset : getSensesOf(lemma,POS.ADJECTIVE)) getDifferentTo(synset,senseMap);
    // Find synonym of verbs and adverbs, if nothing has been found previously
    for (Synset synset : getSensesOf(lemma,POS.VERB  )) getDifferentTo(synset,senseMap);
    for (Synset synset : getSensesOf(lemma,POS.ADVERB)) getDifferentTo(synset,senseMap);

    // Return unique words
    return getUniqueWords( senseMap );

  }
  //****************************************************************************
  public String[] getHypernymsOf(String lemma) {

    // Auxiliar
    HashSet<Synset> senseMap = new HashSet<Synset>();

    // Query dictionary for hypernyms
    // Find hypernyms of nouns and adjectives
    for (Synset synset : getSensesOf(lemma,POS.NOUN     )) getMoreGeneralTo(synset,senseMap);
    for (Synset synset : getSensesOf(lemma,POS.ADJECTIVE)) getMoreGeneralTo(synset,senseMap);
    // Find hypernyms of verbs and adverbs, if nothing has been found previously
    for (Synset synset : getSensesOf(lemma,POS.VERB     )) getMoreGeneralTo(synset,senseMap);
    for (Synset synset : getSensesOf(lemma,POS.ADVERB   )) getMoreGeneralTo(synset,senseMap);

    // Return unique words
    return getUniqueWords( senseMap );

  }
  //****************************************************************************
  public String[] getHypernymsOf(String[] lemmas,Filter context) {

    if (lemmas.length==0) return new String[0];

    // Auxiliar
    HashSet<Synset> commonSenses   = new HashSet<Synset>();
    HashSet<Synset> specificSenses = new HashSet<Synset>();

    // Query dictionary for hypernyms
    // Find hypernyms of nouns and adjectives
    for (Synset synset : getSensesOf(lemmas[0],POS.NOUN     )) getMoreGeneralTo(synset,commonSenses);
    for (Synset synset : getSensesOf(lemmas[0],POS.ADJECTIVE)) getMoreGeneralTo(synset,commonSenses);
    // Find hypernyms of verbs and adverbs, if nothing has been found previously
    for (Synset synset : getSensesOf(lemmas[0],POS.VERB     )) getMoreGeneralTo(synset,commonSenses);
    for (Synset synset : getSensesOf(lemmas[0],POS.ADVERB   )) getMoreGeneralTo(synset,commonSenses);

    for (int i=1; i<lemmas.length; i++){
      // Find hypernyms of nouns and adjectives
      for (Synset synset : getSensesOf(lemmas[i],POS.NOUN     )) getMoreGeneralTo(synset,specificSenses);
      for (Synset synset : getSensesOf(lemmas[i],POS.ADJECTIVE)) getMoreGeneralTo(synset,specificSenses);
      // Find hypernyms of verbs and adverbs, if nothing has been found previously
      for (Synset synset : getSensesOf(lemmas[i],POS.VERB     )) getMoreGeneralTo(synset,specificSenses);
      for (Synset synset : getSensesOf(lemmas[i],POS.ADVERB   )) getMoreGeneralTo(synset,specificSenses);
      // Intersection
      for (Synset synset : specificSenses) {
        if (!commonSenses.contains(synset)) commonSenses.remove(synset);
      }
    }

    // Return unique words
    return getUniqueWords( commonSenses );

  }
  //****************************************************************************
  public String[] getHypernymsOf(String[] lemmas,Filter context,int level) {

    return new String[0]; // not implemented yet

  }
  //****************************************************************************
  public String[] getHyponymsOf(String lemma) {

    return new String[0]; // not implemented yet

  }
  //****************************************************************************
  public String[] getHyponymsOf(String[] lemmas,Filter context) {

    return new String[0]; // not implemented yet

  }
  //****************************************************************************
  public String[] getHyponymsOf(String[] lemmas,Filter context,int level) {

    return new String[0]; // not implemented yet

  }
  //****************************************************************************
  public boolean areSynonyms(String lemmaA,String lemmaB){

    return false; // not implemented yet

  }
  //****************************************************************************
  public boolean areAntonyms(String lemmaA,String lemmaB){

    return false; // not implemented yet

  }
  //****************************************************************************

}