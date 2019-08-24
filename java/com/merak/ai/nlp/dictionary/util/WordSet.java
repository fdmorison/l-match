// Package
///////////////
package com.merak.ai.nlp.dictionary.util;

// Imports
///////////////
import java.util.*;
import com.merak.core.*;

// Public Class
///////////////
public class WordSet implements Comparable {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private String       definition = "UndefinedWordSet";
  private List<String> set        = new LinkedList<String>();

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public WordSet(String definition) {

    // Attribute Initialization
    setDefinition(definition);

  }
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public String getDefinition(){

    return definition;

  }
  //****************************************************************************
  public String getShorterWord() {

    if (isEmpty()) return null;

    // Auxiliar
    Iterator<String> iter    = iterator();
    String           shorter = iter.next();
    int              length  = shorter.length();
    String           word    = null;

    // Search shorter word
    while (iter.hasNext()) {
      word = iter.next();
      if ( word.length() < length ) {
        shorter = word;
        length  = shorter.length();
      }
    }
    return shorter;

  }
  //****************************************************************************
  public String getLongerWord() {

    if (isEmpty()) return null;

    // Auxiliar
    Iterator<String> iter    = iterator();
    String           longer  = iter.next();
    int              length  = longer.length();
    String           word    = null;

    // Search shorter word
    while (iter.hasNext()) {
      word = iter.next();
      if ( word.length() > length ) {
        longer  = word;
        length  = longer.length();
      }
    }
    return longer;

  }
  //****************************************************************************
  public void setDefinition(String definition) {

    if (definition!=null) this.definition = definition;

  }
  //****************************************************************************
  public void add(String word) {

    set.add(word);

  }
  //****************************************************************************
  public Iterator<String> iterator() {

    return set.iterator();

  }
  //****************************************************************************
  public String[] toArray() {

    return set.toArray( new String[set.size()] );

  }
  //****************************************************************************
  public String toString() {

    // Auxiliar
    String           string = definition + " <- ";
    Iterator<String> iter   = iterator();

    // Rendering the string
    while (iter.hasNext()) {
      string += iter.next() + " ";
    }
    return string;

  }
  //****************************************************************************
  public int compareTo(Object object) {

    if ( object!=null && object instanceof WordSet) {
      return definition.compareTo(((WordSet)object).definition);
    }
    return -1;

  }
  //****************************************************************************
  public boolean isEmpty() {

    return set.isEmpty();

  }
  //****************************************************************************
  public int length() {

    return set.size();

  }
  //****************************************************************************

}