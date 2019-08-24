// Package
///////////////
package com.merak.core.text;

// Imports
///////////////

// Public Class
///////////////
public abstract class Stemmer {

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public abstract String stem(String word);

  //****************************************************************************
  public String[] stem(String[] words) {

    for (int i=0; i<words.length; i++) {
      words[i] = stem(words[i]);
    }
    return words;

  }
  //****************************************************************************

}