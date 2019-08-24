// Package
///////////////
package com.merak.core.text;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.merak.core.Application;
import com.merak.core.text.Stemmer;
import com.merak.core.text.PorterStemmer;
import com.merak.core.text.util.*;

// Public Class
///////////////
public class ClusteringStemmer extends Stemmer {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final Stemmer            stemmer;
  private final Map<String,String> dictionary = new HashMap<String,String>();

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public ClusteringStemmer() {

    // Attribute Initialization
    this.stemmer = new PorterStemmer();

  }
  //****************************************************************************
  public ClusteringStemmer(Stemmer stemmer) {

    // Attribute Initialization
    this.stemmer = (stemmer!=null) ? stemmer : new PorterStemmer();

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public Set<String> clusterWords(Iterator<String> words) {

    // Auxiliar
    ExpansionMap clusterMap = new ExpansionMap();
    Set<String>  vocabulary = new HashSet<String>();
    WordSet[]    clusters   = null;
    String       stem       = null;
    String       w;

    try {
      // Agrupar palavras sob o radical encontrado por algoritmo
      while (words.hasNext()) {
        w    = words.next();
        stem = stemmer.stem(w);
        clusterMap.expandTo(stem,w);
      }
      // Selecionar clusters encontrados e limpar memoria
      clusters = clusterMap.toArray();
      clusterMap.clear();
      Arrays.sort(clusters);

      // Definir como radical a menor palavra de cada cluster
      FileWriter  log        = new FileWriter("clusters-morph.txt");
      vocabulary = new HashSet<String>(clusterMap.length());
      for (WordSet cluster : clusters) {
        // Redefinir o radical do cluster como sendo a menor palavra do cluster
        stem = cluster.getShorterWord();
        cluster.setDefinition(stem);
        // Reduzir palavras do cluster para o radical redefinido
        for (String word : cluster) {
          dictionary.put(word,stem);
        }
        // Adicionar radical para o vocabulario
        vocabulary.add(stem);
        log.write(cluster.toString()+"\n");
      }
      log.close();
    }
    catch (Exception ex) {
      Application.debug.print("ClusteringStemmer.clusterWords(Iterator<String>)",ex);
    }

    return vocabulary;

  }
  //****************************************************************************
  public String stem(String word) {

    // Consultar dicionario primeiramente
    String stem = dictionary.get(word);

    // Se nenhuma entrada for encontrada, utilizar solucao algoritmica
    if (stem==null) stem = stemmer.stem(word);

    // Retornar o radical da palavra
    return stem;

  }
  //****************************************************************************

}