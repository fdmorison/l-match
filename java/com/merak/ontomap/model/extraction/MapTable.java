// Package
///////////////
package com.merak.ontomap.model.extraction;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.merak.core.text.*;

// Public Class
///////////////
public class MapTable {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************	
  public final Map<String,MapList> ontologyA = new HashMap<String,MapList>();
  public final Map<String,MapList> ontologyB = new HashMap<String,MapList>();

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public MapTable(File file) {

     if (file==null) return;
     load(file);

  }
  
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************	
  public void load(File file) {

    // Auxiliar
    TextDocument document       = new Tokenizer().lexWhite(file);
    List<String> line           = null;
    String       relation       = null;
    String       sourceCategory = null;
    String       targetCategory = null;    
    int          i;

    // Parse header lines
    for (i=0; i<document.getNumberOfLines(); i++) {
      // Agir conforme o tipo de linha
      line = document.getLine(i);
      if ( line.isEmpty()             ) continue; // Ignorar linha vazia
      if ( line.get(0).startsWith("#")) continue; // Ignorar linha de comentario
      if (!line.get(0).startsWith("@")) break;    // Finalizar a leitura do header ao achar a primeira linha de mapeamento
      // Variavel encontrada
      // Se a variavel for @direction, entao checar se as ontologias no arquivo seriam as mesmas ontologias deste MappingSet
    }
    
    // Parse mapping lines
    while (i<document.getNumberOfLines()) {
      // Next document line
      line = document.getLine(i++);
      // Skip comment line
      if ( line.isEmpty()             ) continue; // Ignorar linha vazia
      if ( line.get(0).startsWith("#")) continue; // Ignorar linha de comentario
      // Parse next mapping attributes
      relation       = line.get(0);
      sourceCategory = line.get(1);
      targetCategory = line.get(2);
      
      if (ontologyA.get(sourceCategory)==null) ontologyA.put(sourceCategory,new MapList());
      if (ontologyB.get(targetCategory)==null) ontologyB.put(targetCategory,new MapList());
      
      // Fill the mapping table
      if ( relation.equals("equivalence") ) {
        ontologyA.get(sourceCategory).addEquivalent(targetCategory);
        ontologyB.get(targetCategory).addEquivalent(sourceCategory);
      }
      else if ( relation.equals("more_general") ) {
      	ontologyB.get(targetCategory).addMoreGeneral(sourceCategory);
      }
      else if ( relation.equals("less_general") ) {
      	ontologyA.get(sourceCategory).addMoreGeneral(targetCategory);
      }      
      	
    }
    
  }	
  //****************************************************************************	
	
}