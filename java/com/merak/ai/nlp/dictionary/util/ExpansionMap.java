// Package
///////////////
package com.merak.ai.nlp.dictionary.util;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.merak.core.*;

// Class
///////////////
public class ExpansionMap {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final Map<String,WordSet> map = new HashMap<String,WordSet>();

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public void expandTo(String reduction,String word) throws Exception {

    // Check Error
    if (reduction==null) throw new Exception("NullWord expanding to '"+word+"' is not allowed");
    if (word==null     ) throw new Exception(reduction+" expanding to NullWord is not allowed");

    // Auxiliar
    WordSet expansion = map.get(reduction);

    // Create a list to add the expanded words, if it is not created yet
    if (expansion==null) {
      expansion = new WordSet(reduction);
      map.put(reduction,expansion);
    }

    // Add to expansion list
    expansion.add(word);

  }
  //****************************************************************************
  public WordSet expand(String reduction) {

    return map.get(reduction);

  }
  //****************************************************************************
  public Iterator<WordSet> iterator() {

    return map.values().iterator();

  }
  //****************************************************************************
  public void writeTo(File out) throws Exception {

    // Auxiliar
    FileWriter writer     = null;
    WordSet[]  reducedSet = map.values().toArray( new WordSet[map.size()] );

    // Sort reduced words
    Arrays.sort(reducedSet);

    // Writing the reductions
    writer = new FileWriter(out);
    for (int i=0; i<reducedSet.length; i++) {
      writer.write( reducedSet[i] + "\n");
    }
    writer.close();

  }
  //****************************************************************************


}
