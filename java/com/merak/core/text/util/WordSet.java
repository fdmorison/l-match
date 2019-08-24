// Package
///////////////
package com.merak.core.text.util;

// Imports
///////////////
import java.util.*;
import com.merak.core.*;

// Public Class
///////////////
public class WordSet implements Comparable<WordSet>,Iterable<String> {

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

    ListIterator<String> words = set.listIterator();
    int                  cmp;

    while (words.hasNext()) {
      cmp = word.compareTo(words.next());
      if ( cmp==0 ) return;
      if ( cmp> 0 ) break;
    }

    words.add(word);

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
    String           string = definition + " = { ";
    Iterator<String> iter   = iterator();

    // Rendering the string
    while (iter.hasNext()) {
      string += iter.next() + " ";
    }
    return string + "}";

  }
  //****************************************************************************
  public int compareTo(WordSet object) {

    return ( object==null )
           ? -1
           : definition.compareTo(object.definition)
           ;

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