// Package
///////////////
package com.merak.ontomap.model.extraction.vocabulary;

// Imports
///////////////
import java.util.*;
import java.text.*;

import com.merak.core.*;
import com.merak.core.text.*;
import com.merak.core.graph.*;
import com.merak.core.graph.clique.*;

import com.merak.ai.nlp.dictionary.*;
import com.merak.ontomap.model.extraction.util.*;
import com.merak.ontomap.model.extraction.vocabulary.util.*;

// Public Class
///////////////
public class MonosemicThesaurus implements Thesaurus {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private TaxonomicDictionary dictionary    = null;
  private Map<String,XWord>   words         = new HashMap<String,XWord>();
  private List<XSynset>       synsets       = new ArrayList<XSynset>();
  private int                 word_id_seq   = 0;
  private int                 synset_id_seq = 0;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public MonosemicThesaurus(TaxonomicDictionary source,Vocabulary vocabulary) {

/*
    System.out.println("Creating the Graph\n");
    createVertex(graph,"fundamental");
    System.out.println(graph);
    System.out.println("Maximal Cliques Enumeration");
    OptimalMaximalCliqueEnumerator oc = new OptimalMaximalCliqueEnumerator(g);
    oc.getDistinctCliques();
    System.exit(0);
*/
    // Auxiliar
    XSynset synset    = null;
    XWord   satellite = null;
    XWord   word      = null;
    String  stemLemma = null;

    // Attribute Initialization
    this.dictionary = source;

    // 0) CREATE MORPHOLOGICAL CLUSTERS OF WORDS - USING STEMMING
    ClusteringStemmer stemmer         = new ClusteringStemmer();
    Set<String>       morphVocabulary = stemmer.clusterWords(vocabulary.iterator());
    dictionary.setFilter( new WordFilter(morphVocabulary) );

    // 1) For each stemmed lemma, create a XWord
    for (String lemma : morphVocabulary) {
      createWord(lemma);
    }

    // 1) CREATE SEMANTIC CLUSTERS OF WORDS - USING EXTERNAL DICTIONARY
    // a) Enumerate Maximal Cliques of similar words
    createSynonymClusters(morphVocabulary);

    // b) Remove very polissemic cliques
    removeVeryPolissemicSynsets();

    // 3) MERGE MORPHOLOGICAL SYNSETS WITH SEMANTIC SYNSETS
    // c) Add morphological synsets to semantic synsets
    for (String lemma : vocabulary) {
      // Se a proxima palavra eh um cluster, entao
      word = words.get(lemma.toLowerCase());
      if (word!=null) {
        // If the word has no meanings, create one for it
        if (word.getMeanings().isEmpty()) {
          synset = createSynset();
          word.addMeaning(synset);
          synset.addWord(word);
        }
      }
      // Senao, a palavra eh um satelite
      else {
        // Indexar o lemma como satelite do cluster
        stemLemma = stemmer.stem(lemma).toLowerCase();
        word      = words.get(stemLemma);
        satellite = createWord(lemma,word);
      }
    }

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private XWord createWord(String lemma) {

    XWord word = words.get(lemma = lemma.toLowerCase());
    if (word!=null) return null;

    word = new XWord( word_id_seq++ , lemma );
    words.put(lemma,word);
    return word;

  }
  //****************************************************************************
  private XWord createWord(String lemma,XWord cluster) {

    // Attribute Initialization
    XWord satellite = createWord(lemma);

    // If the word has no meanings, create one for it
    if (cluster.getMeanings().isEmpty()) {
      XSynset synset = createSynset();
      cluster.addMeaning(synset);
      synset.addWord(cluster);
    }

    // Associate satellite with cluster's meanings
    for ( XSynset synset : cluster ) {
      synset.addWord(satellite);    // Add satellite to cluster's meanings
      satellite.addMeaning(synset); // Add cluster's meanings to satellite
    }
    return satellite;

  }
  //****************************************************************************
  private XSynset createSynset() {

    XSynset synset = new XSynset( synset_id_seq++ );
    synsets.add(synset);
    return synset;

  }
  //****************************************************************************
  private Graph createSynonymGraph(Set<String> vocabulary) {

    // Auxiliar
    Graph  graph = new Graph("SynonymGraph");
    String lemma = null;

    // For each lemma, create a XWord and a Vertex
    for (String w: words.keySet()) {
      graph.createVertex(w);
    }

    // Now, create links between synonym words
    for (Vertex source : graph.getVertices()) {
      lemma = (String) source.getKey();
      if (lemma.length()<3) continue;
      for (String synonym : dictionary.getSynonymsOf(lemma)) {
        source.linkTo(synonym.toLowerCase(),null);
      }
    }

    // Return the graph
    return graph;

  }
  //****************************************************************************
  private void createSynonymClusters(Set<String> vocabulary) {

    // Auxiliar
    XSynset synset = null;
    XWord   word   = null;
    String  lemma  = null;

    // Create a Synonym Graph and Enumerate Maximal Cliques
    Graph                          graph    = createSynonymGraph(vocabulary);
    OptimalMaximalCliqueEnumerator builder  = new OptimalMaximalCliqueEnumerator(graph);
    Component[]                    cliques  = builder.getDistinctCliques();

    // For each clique
    for (Component clique : cliques) {
      synset = createSynset();
      for (Vertex vertex : clique) {
        // Retrieve word
        lemma = (String)vertex.getKey();
        word  = words.get(lemma);
        // Associate word and meaning
        word.addMeaning(synset);
        synset.addWord(word);
      }
      if (synset.getNumberOfWords()>1) System.out.println(synset);
    }
    System.out.println();

  }
  //****************************************************************************
  private void removeVeryPolissemicSynsets() {

    // Auxiliar
    NumberFormat nf              = NumberFormat.getInstance();
    XWord[]      words           = null;
    String[]     lemmas          = null;
    double       polyssemyFactor = 1;

    nf.setMaximumFractionDigits(4);
    nf.setMinimumFractionDigits(4);
    System.out.println();
    for (XWord word : this.words.values()) {
      if (word.isAmbiguous()) System.out.println(word);
    }
    System.out.println();

    XSynset[] array = synsets.toArray( new XSynset[synsets.size()] );
    Arrays.sort(array,new SynsetPolysemyComparator(false));

    for (XSynset synset : array) {
      // Se o synset n�o � muito polissemico, entao continue
      polyssemyFactor = synset.getPolysemyFactor();

      //if ( polyssemyFactor < 0.5 ) {
      //  if (polyssemyFactor>0) System.out.println(nf.format(polyssemyFactor) + " " + synset + " false" );
      //  continue;
      //}
      // Senao, elimine as palavras polisemicas do synset
/*
      lemmas = synset.createLemmaArray();
      lemmas = dictionary.getHypernymsOf(lemmas,null);
      System.out.print("HypernymsOf("+synset+") = ");
      for (String s : lemmas) {
        System.out.print(s+" ");
      }
      System.out.println();
*/
      System.out.print(nf.format(polyssemyFactor) + " " + synset + " --> " );
      words = synset.createWordArray();
      for (XWord word : words) {
        if ( !word.isAmbiguous() ) continue;
        synset.removeWord(word);
        word.removeMeaning(synset);
      }
      System.out.println(synset);

    }

    System.err.println(" foi! ");

    System.out.println("\n");
    for (XWord word : this.words.values()) {
      if ( word.getMeaning(0).getNumberOfWords() > 1 ) System.out.println(word);
    }

    //System.exit(0);
    //System.out.println(array.length+" Synsets and "+this.words.size()+" Words");

  }
  //****************************************************************************
  public void setFilter(Filter<String> vocabularyDelimiter) {
    // ignore this method !!! It must not be implemented!!
  }
  //****************************************************************************
  public String[] getSynonymsOf(String lemma) {

    // Buscar a reducao semantica da palavra
    XWord word = words.get(lemma.toLowerCase());
    if (word==null) return new String[0];

    // A palavra nao pode ser polissemica!
    Application.debug.exit( word.isAmbiguous(), "MonosemicThesaurus.getSynonymsOf(String)", "A palavra "+word.getLemma()+" continua ambigua" );

    // Retornar o primeiro e unico synset
    return word.getMeaning(0).createLemmaArray();

  }
  //****************************************************************************
  public String[] getAntonymsOf(String lemma) { return new String[0]; }

  //****************************************************************************
  public boolean  areSynonyms(String lemmaA,String lemmaB) { return false; }

  //****************************************************************************
  public boolean  areAntonyms(String lemmaA,String lemmaB) { return false; }

  //****************************************************************************

}
