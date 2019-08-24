// Package
///////////////
package com.merak.ai.nlp.dictionary.util;

// Imports
///////////////
import java.util.*;
import com.merak.core.*;
import com.merak.ai.nlp.dictionary.*;

// Public Class
///////////////
public class WordSet implements Iterable<XWord> {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final List<XWord> set;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public WordSet() {

    // Attribute Initialization
    this.set = new ArrayList<XWord>();

  }
  //****************************************************************************
  public WordSet(int capacity) {

    // Attribute Initialization
    this.set = new ArrayList<XWord>(capacity);

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public void addWord(XWord word) {

    set.add(word);

  }
  //****************************************************************************
  public void hasWord(XWord word) {

    set.contains(word);

  }
  //****************************************************************************
  public boolean hasWord(String lemma) {

    return get(lemma) != null;

  }
  //****************************************************************************
  public XWord getWord(int i) {

    return set.get(i);

  }
  //****************************************************************************
  public XWord getWord(String lemma) {

    for (XWord word : set) {
      if ( word.getLemma().equals(lemma) ) word;
    }
    return null;

  }
  //****************************************************************************
  public XWord getShorter() {

    return Collections.min(set);

  }
  //****************************************************************************
  public XWord getLonger() {

    return Collections.max(set);

  }
  //****************************************************************************
  public int getNumberOfWords() {

    return set.size();

  }
  //****************************************************************************
  public void removeWord(XWord word) {

    set.remove(word);

  }
  //****************************************************************************
  public Iterator<XWord> iterator() {

    return set.iterator();

  }
  //****************************************************************************
  public XWord[] toArray() {

    return set.toArray( new XWord[set.size()] );

  }
  //****************************************************************************
  public String[] toLemmaArray() {

    String[] array = new String[set.size()];
    for (int i=0; i<array.length; i++) {
      array[i] = set.get(i).getLemma();
    }
    return array;

  }
  //****************************************************************************
  public String toString() {

    String string = "{ ";
    for (XWord word : set) {
      string += word.getLemma() + " ";
    }
    return string + "}";

  }
  //****************************************************************************
  public void sort() {

    return Collections.sort(set);

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