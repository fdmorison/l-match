// Package
///////////////
package com.merak.ai.nlp.dictionary.wordnet;

// Imports
///////////////
import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import edu.mit.jwi.item.*;
import edu.mit.jwi.dict.*;

import com.merak.core.*;
import com.merak.core.graph.*;
import com.merak.core.graph.clique.*;
import com.merak.ai.nlp.dictionary.util.NameTokenizer;
import com.merak.ai.nlp.dictionary.Thesaurus;
import com.merak.ontomap.model.extraction.vocabulary.NameTools;

// Public Class
///////////////
public class MITWordNetThesaurus implements Thesaurus {

  //~ Constants ////////////////////////////////////////////////////////////////
  //****************************************************************************
  private static final String DEFAULT_PATH = Application.getInstance().getDataPath("wordnet/dict");

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final IDictionary            dictionary;
  private final NameTokenizer          tokenizer;
  private final Map<ISynsetID,ISynset> cache;
  private       Filter<String>         filter = Filter.getTrueInstance();

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public MITWordNetThesaurus(NameTokenizer tokenizer)
    throws MalformedURLException
  {

    this(new Dictionary(new URL("file",null,DEFAULT_PATH)), tools);

  }
  //****************************************************************************
  public MITWordNetThesaurus(String wordNetHome,NameTools tools)
    throws MalformedURLException
  {

    this(new Dictionary(new URL("file",null,wordNetHome+"/dict")), tools);

  }
  //****************************************************************************
  public MITWordNetThesaurus(IDictionary dictionary,NameTools tools) {

    // Attribute Initialization
    this.tools      = tools;
    this.cache      = new HashMap<ISynsetID,ISynset>(256);
    this.dictionary = dictionary;
    this.dictionary.open();

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private ISynset[] getSensesOf(String lemma,PartOfSpeech pos) {

    // check error
    IIndexWord idxWord = dictionary.getIndexWord(lemma,pos);
    if (idxWord==null) return new ISynset[0];

    // auxiliar
    IWordID[] wordIDs = idxWord.getWordIDs();
    ISynset[] senses  = new ISynset[ wordIDs.length ];

    // convert wordIDs into synsets
    for (int i=0; i<wordIDs.length; i++) {
      senses[i] = cache.get(wordIDs[i].getSynsetID());
      if ( senses[i] == null ) {
        senses[i] = dictionary.getSynset( wordIDs[i].getSynsetID() );
        cache.put(senses[i].getID(),senses[i]);
      }
    }
    return senses;

  }
  //****************************************************************************
  private void getSimilarTo(ISynset source,HashSet<ISynset> set) {

    // Auxiliar
    List<ISynset> control   = new LinkedList<ISynset>();
    ISynsetID[]   synsetIDs = null;
    ISynset       sense     = null;

    // Prepare the queue
    control.add( source );

    // Select unique similar senses
    while ( !control.isEmpty() ) {
      sense = control.remove(0);
      if (!set.contains(sense)) {
        set.add(sense);
        synsetIDs = sense.getRelatedSynsets(WordnetPointerType.SIMILAR_TO);
        for (int i=0; i<synsetIDs.length; i++) {
          control.add( dictionary.getSynset(synsetIDs[i]) );
        }
      }
    }

  }
  //****************************************************************************
  public void setFilter(Filter<String> filter) {

    if (filter!=null) this.filter = filter;

  }
  //****************************************************************************
  public String[] getSynonymsOf(String lemma){

    // Auxiliar
    HashSet<ISynset>  senseMap = new HashSet<ISynset>();
    HashSet<String>   lemmaMap = new HashSet<String>();
    Iterator<ISynset> iter     = null;
    ISynset[]         senses   = null;
    ISynset           sense    = null;
    IWord[]           words    = null;
    String            lemma    = null;
    int               i;

    // Find synonym adjectives
    senses = getSensesOf(lemma,PartOfSpeech.ADJECTIVE);
    for (i=0; i<senses.length; i++) getSimilarTo(senses[i],senseMap);
    // Find synonym nouns
    senses = getSensesOf(lemma,PartOfSpeech.NOUN);
    for (i=0; i<senses.length; i++) getSimilarTo(senses[i],senseMap);
    // Find synonym verbs/adverbs if nothing has been found previously
    if (senseMap.isEmpty()) {
      // Find synonym verbs
      senses = getSensesOf(lemma,PartOfSpeech.VERB);
      for (i=0; i<senses.length; i++) getSimilarTo(senses[i],senseMap);
      // Find synonym adverbs
      senses = getSensesOf(lemma,PartOfSpeech.ADVERB);
      for (i=0; i<senses.length; i++) getSimilarTo(senses[i],senseMap);
    }

    // Select unique words among the synonyms
    iter = senseMap.iterator();
    while (iter.hasNext()) {
      // Next sense
      sense = iter.next();
      words = sense.getWords();
      // Select unique and acceptable words from the current sense
      for (i=0; i<words.length; i++) {
        lemma = words[i].getLemma();
        if (!filter.accept(lemma)) continue;
        lemmaMap.add(lemma);
      }
    }

    // Return the words inside an array
    return lemmaMap.toArray( new String[lemmaMap.size()] );

  }
  //****************************************************************************
  public String[] getAntonymsOf(String lemma){

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