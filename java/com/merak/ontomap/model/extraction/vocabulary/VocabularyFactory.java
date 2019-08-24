// Package
///////////////
package com.merak.ontomap.model.extraction.vocabulary;

// Imports
///////////////
import java.io.*;
import java.util.*;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.shared.PrefixMapping;

import com.merak.core.*;
import com.merak.core.text.*;
import com.merak.core.graph.*;
import com.merak.core.graph.clique.*;

import com.merak.ai.nlp.dictionary.*;
import com.merak.ai.nlp.dictionary.util.*;
import com.merak.ai.nlp.dictionary.wordnet.*;

import com.merak.ontomap.model.extraction.util.*;

// Class
///////////////
public class VocabularyFactory {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private NameTools           tools      = new NameTools();
  private File                path       = null;
  private TaxonomicDictionary dictionary = null;
  private File                synset_txt = null;
  private FileWriter          shared_txt = null;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public VocabularyFactory(File model) throws Exception {

     // Attribute Initialization
     this.path = new File(model+"/vocabulary");
     this.path.mkdir();
     this.dictionary = new JWordNetDictionary(tools.getTokenizer());
     this.synset_txt = new File(path+"/synset-reduction.txt");
     this.shared_txt = new FileWriter(path+"/shared-vocabulary.txt");

     // Configure
     tools.allowStemming(true);
     tools.allowPrefixes(true);

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private void extractResourceNames(Iterator resources,Vocabulary vocabulary,Set<String> nameSet) {

    // Auxiliar
    Resource resource = null;
    String   name     = null;
    String[] words    = null;

    // Extract
    while (resources.hasNext()) {
      // Add Resource's name to NameSet
      resource = (Resource) resources.next();
      name     = resource.getLocalName();
      nameSet.add(name);
      // Add words of Resource's name to Vocabulary
      words = tools.splitIt(name);
      vocabulary.put(words,null);
    }

  }
  //****************************************************************************
  private void extractVocabulary(OntModel ontology,Vocabulary vocabulary,Set<String> nameSet) {

    // Extrair nomes das entidades ontologicas:
    extractResourceNames( ontology.listNamedClasses()     , vocabulary, nameSet ); // Conceitos
    extractResourceNames( ontology.listIndividuals()      , vocabulary, nameSet ); // Instancias
    extractResourceNames( ontology.listObjectProperties() , vocabulary, nameSet ); // Propriedades de Objetos

  }
  //****************************************************************************
  private void reduceWords(Vocabulary vocabulary)
    throws Exception
  {

    System.out.println("Reduzindo cada nome a um unico sinonimo");

    // Auxiliar
    String[] synonyms   = null;
    String   definition = null;

    // Compute monosemous synsets
    Thesaurus thesaurus = new MonosemicThesaurus( dictionary , vocabulary );

    // Reduce each words to an unique definition
    for (String word : vocabulary) {
      // Skip reduced word
      if (vocabulary.hasReduction(word)) continue;
      // Choose the shortest word to be the synset definition
      synonyms   = thesaurus.getSynonymsOf(word);
      definition = word;
      for (String synonym : synonyms) {
        if ( synonym.compareTo(definition) < 0 ) definition = synonym;
      }
      // Reduce synonyms to definition
      for (String synonym : synonyms) {
        vocabulary.put(synonym,definition);
      }
      // Reduce word to definition
      vocabulary.put(word,definition);
    }

  }
  //****************************************************************************
  private void reduceNames(Set<String> nameSet,Vocabulary vocabulary,ExpansionMap expansionMap)
    throws Exception
  {

    System.out.println("Recompor nomes dada a reducao das palavras");

    // Auxiliar
    String[] names       = nameSet.toArray( new String[nameSet.size()] );
    String   reducedName = null;
    String[] words       = null;

    // Reset nameSet to receive reduced names later, instead of the original names
    nameSet.clear();

    // Reduce names
    for (int i=0; i<names.length; i++) {
      // Split the name, reduce its words and so reduce the name
      words = tools.splitIt(names[i]);
      if (words.length==0) continue;
      reducedName = vocabulary.get(words[0]);
      for (int j=1; j<words.length; j++) {
        reducedName += vocabulary.get(words[j]);
      }
      // Different names can reduce to the same definition name. So, add this name to an expansion list
      expansionMap.expandTo(reducedName,names[i]);
      nameSet.add(reducedName);
    }

  }
  //****************************************************************************
  private void selectSharedNames(Set<String> namesA,Set<String> namesB,Vocabulary vocabulary,ExpansionMap nameExpansion)
    throws Exception
  {

    // Auxiliar
    Iterator<String> names       = namesA.iterator();
    List<String>     shared      = new LinkedList<String>();
    String           reducedName = null;
    WordSet          expansion   = null;

    // Computar vocabulario comum
    while (names.hasNext()) {
      // Continue if the next name isnt shared by both ontologies
      reducedName = names.next();
      if (!namesB.contains(reducedName)) continue;
      // The name is shared!!
      shared.add(reducedName);
    }
    Collections.sort(shared);

    // Salvar vocabulario comum
    while (!shared.isEmpty()) {
      expansion = nameExpansion.expand( shared.remove(0) );
      names     = expansion.iterator();
      while (names.hasNext()) {
        vocabulary.put(names.next(),expansion.getDefinition());
      }
      shared_txt.write(expansion + "\n");
    }

  }
  //****************************************************************************
  public void useStopwords(File file) {

    tools.getTokenizer().addStopWords(file);

  }
  //****************************************************************************
  public Vocabulary create(OntModel o1,OntModel o2) throws Exception {

    // Auxiliar
    Vocabulary   vocabulary    = new Vocabulary(tools);
    ExpansionMap nameExpansion = new ExpansionMap();
    Set<String>  namesA        = new HashSet<String>();
    Set<String>  namesB        = new HashSet<String>();

    // Extrair vocabulario
    extractVocabulary(o1,vocabulary,namesA); // Extract names and words from o1
    extractVocabulary(o2,vocabulary,namesB); // Extract names and words from o2

    // Reduzir palavras equivalentes a uma unica definicao
    reduceWords(vocabulary);

    // Reduzir nomes, dada a reducao das palavras, para um mapa de reducoes
    reduceNames(namesA,vocabulary,nameExpansion); // Now, namesA contains reduced versions of its original names
    reduceNames(namesB,vocabulary,nameExpansion); // Now, namesB contains reduced versions of its original names

    // Computar e incluir vocabulario comum
    selectSharedNames(namesA,namesB,vocabulary,nameExpansion);

    // Salvar e retornar o vocabulario
    vocabulary.save(path);
    return vocabulary;

  }
  //****************************************************************************
  public void close() {

    try {
      shared_txt.close();
    }
    catch(Exception ex) {
      Application.debug.print("MapVocabulary.close()",ex);
    }

  }
  //****************************************************************************


}
