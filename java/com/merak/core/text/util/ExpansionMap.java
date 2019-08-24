// Package
///////////////
package com.merak.core.text.util;

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
  public WordSet[] toArray() {

    return map.values().toArray( new WordSet[map.size()] );

  }
  //****************************************************************************
  public Iterator<WordSet> iterator() {

    return map.values().iterator();

  }
  //****************************************************************************
  public void clear() {

    map.clear();

  }
  //****************************************************************************
  public int length() {

    return map.size();

  }
  //****************************************************************************
  public void writeTo(File out) {

    // Auxiliar
    WordSet[] reducedSet = map.values().toArray( new WordSet[map.size()] );

    // Sort reduced words
    Arrays.sort(reducedSet);
    try {
      // Writing the reductions
      FileWriter writer = new FileWriter(out);
      for (WordSet set : reducedSet) writer.write( set + "\n");
      writer.close();
    }
    catch(Exception ex) {
    }

  }
  //****************************************************************************


}
