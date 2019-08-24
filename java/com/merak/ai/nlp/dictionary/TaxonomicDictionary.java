// Package
///////////////
package com.merak.ai.nlp.dictionary;

// Imports
///////////////
import com.merak.core.Filter;

// Public Class
///////////////
public interface TaxonomicDictionary extends Thesaurus {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public String[] getHypernymsOf(String lemma);

  //****************************************************************************
  public String[] getHypernymsOf(String[] lemmas,Filter context);

  //****************************************************************************
  public String[] getHypernymsOf(String[] lemmas,Filter context,int level);

  //****************************************************************************
  public String[] getHyponymsOf(String lemma);

  //****************************************************************************
  public String[] getHyponymsOf(String[] lemmas,Filter context);

  //****************************************************************************
  public String[] getHyponymsOf(String[] lemmas,Filter context,int level);

  //****************************************************************************

}