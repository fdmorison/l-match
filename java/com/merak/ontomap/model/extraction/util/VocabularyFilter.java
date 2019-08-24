// Package
///////////////
package com.merak.ontomap.model.extraction.util;

// Imports
///////////////
import java.util.*;
import com.merak.core.*;
import com.merak.ontomap.model.extraction.Vocabulary;

// Public Class
///////////////
public class VocabularyFilter extends Filter<String> {

 //~ Attributes ////////////////////////////////////////////////////////////////
 //*****************************************************************************
 private final Vocabulary vocabulary;

 //~ Constructors /////////////////////////////////////////////////////////////
 //****************************************************************************
 public VocabularyFilter(Vocabulary vocabulary) {

   // Attribute Initialization
   this.vocabulary = vocabulary;

 }

 //~ Methods ///////////////////////////////////////////////////////////////////
 //*****************************************************************************
 public boolean accept(String word) {

   return vocabulary.has( word.toLowerCase() );

 }
 //*****************************************************************************

}