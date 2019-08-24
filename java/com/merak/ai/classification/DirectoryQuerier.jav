// Package
///////////////
package com.merak.ai.classification;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.merak.core.io.*;

public class DirectoryQuerier {

  //~ Attributes ///////////////////////////////////////////////////////////////
  /****************************************************************************/
  private FileLoader loader     = new FileLoader();
  private Classifier classifier = null;

  //~ Constructors /////////////////////////////////////////////////////////////
  /****************************************************************************/
  public DirectoryQuerier(Classifier classifier) {

    this.classifier = classifier;

  }
  
  //~ Methods //////////////////////////////////////////////////////////////////
  /****************************************************************************/
  private void query(File input,List<RankGroup> output) 
    throws ClassificationException 
  {
  	// Auxiliares
    File[]     entries = input.listFiles();
    String     text    = null;
    Rank       rank    = null;
    List<Rank> list    = new LinkedList<Rank>();
    int        i,j;
    
    // Classificando
    for (i=j=0; i<entries.length; i++) {
      // Guardando subdiretorios para posterior consulta
      if (entries[i].isDirectory()) {
      	entries[j++] = entries[i];
      }
      // Classificando arquivos
      else {
      	text = loader.loadString(entries[i]);
      	rank = classifier.query(text);
        if (rank==null) continue;
        rank.setName(entries[i].getName());
        list.add( rank );
      }
    }    
    
    // Inserindo o novo grupo de ranks na saida
    output.add( new RankGroup(input.getName(),list) );
    
    // Consultando subdiretorios
    for (i=0; i<j; i++) { 
      query(entries[i],output);
    }
  	
  }   
  /****************************************************************************/
  public List<RankGroup> query(File directory) 
    throws ClassificationException 
  {
  	
    // Checar se o diretorio existe
    if (!directory.isDirectory()) {
      throw new ClassificationException("O diretorio '"+directory+"' nao existe");
    }
   
    // Auxiliar
    File[]          entries = directory.listFiles();
    List<RankGroup> output  = new LinkedList<RankGroup>();
    
    // Classificar recursivamente todas as classes da hierarquia
    for (int i=0; i<entries.length; i++) {
      if (entries[i].isDirectory()) {
      	query(entries[i],output);
      }
    }    

    // Retornar resultados da classificação
    return output;
  	
  }  
  /****************************************************************************/

}
