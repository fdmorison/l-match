// Package
///////////////
package com.merak.ontomap.model.extraction.vocabulary;

// Imports
///////////////
import java.io.File;
import java.util.*;
import com.merak.core.text.*;
import com.merak.ai.nlp.dictionary.util.NameTokenizer;
import com.merak.ontomap.model.extraction.util.*;

public class NameTools {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private       boolean       allowPrefixes = true;
  private       boolean       allowStemming = true;
  private       boolean       allowSplit    = true;
  private final NameTokenizer tokenizer     = new NameTokenizer();
  //private final PorterStemmer stemmer       = new PorterStemmer();

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  //~ Methods //////////////////////////////////////////////////////////////////
  /****************************************************************************
  private String doStem(String value) {

    stemmer.add( value.toCharArray() , value.length() );
    stemmer.stem();
    return stemmer.toString();

  }
  //****************************************************************************/
  public void allowPrefixes(boolean flag) {

    allowPrefixes = flag;

  }
  //****************************************************************************
  public void allowStemming(boolean flag) {

    allowStemming = flag;

  }
  //****************************************************************************
  public void allowSplit(boolean flag) {

    allowSplit = flag;

  }
  //****************************************************************************
  public NameTokenizer getTokenizer() {

    return tokenizer;

  }
  /****************************************************************************
  public String stemIt(String name) {

    // Stem if allowed
    return (allowStemming) ? doStem(name) : name ;

  }
  //****************************************************************************
  public String[] stemIt(String[] words) {

    // Check Permission
    if (!allowStemming) return words;

    // Stem the name
    for (int i=0; i<words.length; i++) words[i] = doStem(words[i]);
    return words;

  }
  //****************************************************************************/
  public String prefixIt(String name,String prefix) {

    // Check Permission
    if (!allowPrefixes) return name;

    // Prefix the name
    return name + prefix;

  }
  //****************************************************************************
  public String[] prefixIt(String[] words,String prefix) {

    // Check Permission
    if (!allowPrefixes) return words;

    // Prefix the words
    for (int i=0; i<words.length; i++) words[i] = prefix + words[i];
    return words;

  }
  //****************************************************************************
  public String capitalizeIt(String name) {

    return Character.toUpperCase(name.charAt(0)) + name.substring(1);

  }
  //****************************************************************************
  public String[] splitIt(String name) {

    // Check Permission
    if (!allowSplit) return new String[]{name};

    // Split words from name
    return tokenizer.getTokens(name);

  }
  //****************************************************************************
  public String mergeIt(String[] words,String prefix) {

    // Auxiliar
    String name = "";

    // Format
    for (int i=0; i<words.length; i++) {
      name += prefixIt( words[i], prefix ) + ' ';
    }
    return name;

  }
  //****************************************************************************
  public String joinIt(String[] words) {

    // Auxiliar
    String name = "";

    // Format
    for (int i=0; i<words.length; i++) {
      name += capitalizeIt(words[i]);
    }
    return name;

  }
  //****************************************************************************

}






//  private HashMap<String,String> dictionary = new HashMap<String,String>();