// Package
///////////////
package com.merak.ontomap.model.extraction.vocabulary;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.merak.core.*;
import com.merak.ai.nlp.dictionary.util.*;

public class Vocabulary implements Iterable<String> {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private Map<String,String> reductionMap = new HashMap<String,String>();
  private NameTools          tools        = null;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public Vocabulary(NameTools nameTools) {

     // Attribute Initialization
     this.tools = nameTools;

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private void saveWords(File path) {

    try {
      // Auxiliar
      FileWriter writer = new FileWriter(path+"/vocabulary.txt");
      String[]   words  = toArray();

      // Save each word
      Arrays.sort(words);
      for (int i=0; i<words.length; i++) {
        writer.write(words[i]+"\n");
      }
      writer.close();
    }
    catch (IOException ex) {
      Application.debug.print("Vocabulary.saveWords(File)",ex);
    }

  }
  //****************************************************************************
  private void saveReductions(File path) throws Exception {

    // Auxiliar
    ExpansionMap     expansionMap   = new ExpansionMap();
    Iterator<String> words          = iterator();
    String           word           = null;
    String           reduction      = null;

    // Create an expansion map: one reduced word is mapped to a list of expanded words
    while (words.hasNext()) {
      word      = words.next();
      reduction = reductionMap.get(word);
      expansionMap.expandTo(reduction,word);
    }
    expansionMap.writeTo(new File(path+"/synset.txt"));

  }
  //****************************************************************************
  public void put(String source,String reducedForm) {

    reductionMap.put(source,reducedForm);

  }
  //****************************************************************************
  public void put(String[] source,String reducedForm) {

    for (int i=0; i<source.length; i++) reductionMap.put(source[i],reducedForm);

  }
  //****************************************************************************
  public boolean has(String source) {

    return reductionMap.containsKey(source);

  }
  //****************************************************************************
  public boolean hasReduction(String source) {

    return reductionMap.get(source)!=null;

  }
  //****************************************************************************
  public String get(String inputName) {

    return reductionMap.get(inputName);

  }
  //****************************************************************************
  public String format(String prefix,String inputName) {

    // Auxiliar
    String   reducedName = reductionMap.get(inputName);
    String   reducedWord = null;
    String[] words       = null;

    // If the name is shared, just prefix the found name (shared names are already reduced)
    if (reducedName!=null) {
      reducedName = tools.prefixIt(reducedName,prefix);
    }
    // Else, split input name, reduce and prefix each word, merging words separated by blank spaces
    else {
      words = tools.splitIt(inputName);
      if (words.length==0) return "";
      for (int i=0; i<words.length; i++) {
        reducedWord = reductionMap.get(words[i]);
        if (reducedWord!=null) {
          words[i] = reducedWord;
        }
      }
      reducedName = tools.mergeIt(words,prefix);
    }

    // Return the reduced name
    return reducedName;

  }
  //****************************************************************************
  public Iterator<String> iterator() {

    return reductionMap.keySet().iterator();

  }
  //****************************************************************************
  public String[] toArray() {

    return reductionMap.keySet().toArray( new String[reductionMap.size()] );

  }
  //****************************************************************************
  public void save(File path) {

    try {
      saveWords(path);
      saveReductions(path);
    }
    catch (Exception ex) {
      Application.debug.print("Vocabulary.save()",ex);
    }

  }
  //****************************************************************************
  public int length() {

    return reductionMap.size();

  }
  //****************************************************************************


}
