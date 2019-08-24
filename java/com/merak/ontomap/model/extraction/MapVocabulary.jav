// Package
///////////////
package com.merak.ontomap.model.extraction;

// Imports
///////////////
import java.io.*;
import java.util.*;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.util.iterator.Filter;

import com.merak.core.*;
import com.merak.core.text.*;
import com.merak.ontomap.model.extraction.dictionary.*;
import com.merak.ontomap.model.extraction.dictionary.wordnet.*;

public class MapVocabulary {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  Thesaurus         thesaurus   = null;
  MapFilter         filter      = new MapFilter();
  Stemmer           stemmer     = new Stemmer();
  HashSet<String>   stopwords   = new HashSet();
  HashSet<String>   common      = null;
  HashSet<String>   vocabulary1 = null;
  HashSet<String>   vocabulary2 = null;
  File              mapModel    = null;
  boolean           doStemming  = true;
  boolean           doSplitting = true;
  boolean           doPrefixing = true;

  List specialList = new LinkedList();

//  NameFormat nameFormat = new NameFormat();

  ArrayList<String> splitBuffer = new ArrayList();

  //~ Cosntructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public MapVocabulary(File mapModel) {

     this.mapModel = mapModel;

     try {
      thesaurus   = new WordNetThesaurus(new File("ApplicationData/wordnet"));
     }
     catch (Exception ex) {
     }

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private boolean isUpperCase(char c) {

    return Character.isUpperCase(c);

  }
  //****************************************************************************
  private boolean isBodyCharacter(char c) {

    return Character.isLowerCase(c) || Character.isDigit(c);

  }
  //****************************************************************************
  private boolean isHeadCharacter(char c) {

    return Character.isLetterOrDigit(c);
    //return Character.isLetter(c);

  }
  //****************************************************************************
  private String getCharacter(char ch) {

    switch(ch) {
      case '0': return "zero";
      case '1': return "one";
      case '2': return "two";
      case '3': return "three";
      case '4': return "four";
      case '5': return "five";
      case '6': return "six";
      case '7': return "seven";
      case '8': return "eigth";
      case '9': return "nine";
      default : return "" + Character.toLowerCase(ch);
    }

  }
  //****************************************************************************
  private String[] splitName(String name) {

    // Para o caso em que não se deve separar os termos de um nome
    if (!doSplitting) return new String[]{name};

    // Para o caso contrário
    String word         = null;
    String standardWord = null;
    int    length       = name.length();
    int    i            = 0;
    char   ch           = 0;

    while (i<length) {

      // 1) Procurando o inicio da proxima palavra: uma letra ou digito
      while ( i<length && !isHeadCharacter(name.charAt(i)) ) i++;
      if (i==length) continue;

      // 2) Separando Siglas/Acronimos iniciais como palavras separadas
      ch   = name.charAt(i++);
      word = "";
      if (isUpperCase(ch)) {
        while ( i<length && isUpperCase(name.charAt(i)) ) {
          word += Character.toLowerCase(ch);
          ch    = name.charAt(i++);
        }
      }

      // 3) Procurando o final da palavra
      if ( word.length() > 0 ) {
        if (i==length || !isBodyCharacter(name.charAt(i))) word+=ch; else i--;
//        if (word.length()==1) continue;
//        continue;
      }
      else {
        word +=  getCharacter(ch);
        while ( i<length && isBodyCharacter(name.charAt(i)) ) {
          word += getCharacter(name.charAt(i++));
        }
      }

      // Normalizando, removendo stopwords e adicionando palavras
      standardWord = standardTokenOf(word);
      if ( stopwords.contains(standardWord)/* && word.length()>1*/) continue;
      splitBuffer.add(word);

    }

//    if ( splitBuffer.size()>1 ) {
      for (i=0; i < (splitBuffer.size()-1) ; i++) {
        word = splitBuffer.get(i);
        if ( word.length()==1 ) splitBuffer.remove(i);
      }
//    }

    String[] res = splitBuffer.toArray( new String[splitBuffer.size()] );
    splitBuffer.clear();
    return res;

  }
  //****************************************************************************
  private String joinName(String[] name) {

    String string = "";
    for (int i=0; i<name.length; i++) {
      string += name[i];
    }
    return string;

  }
  //****************************************************************************
  private String joinName(String prefix,String[] name) {

    String string = "";
    for (int i=0; i<name.length; i++) {
      string += prefixName(prefix,name[i]) + " ";
    }
    return string;

  }
  //****************************************************************************
  private String joinName(String prefix,String[] name,HashSet<String> includeAndSkip) {

    String string = "";
    for (int i=0; i<name.length; i++) {
      if (includeAndSkip.contains(name[i])) continue;
      string += prefixName(prefix,name[i]) + " ";
      includeAndSkip.add( name[i] );
    }
    return string;

  }
  //****************************************************************************
  private String stemName(String name) {

    if (!doStemming) return name;
    stemmer.add( name.toCharArray() , name.length() );
    stemmer.stem();
    return stemmer.toString();

  }
  //****************************************************************************
  private String[] stemName(String[] name) {

    if (!doStemming) return name;
    for (int i=0; i<name.length; i++) {
      name[i] = stemName(name[i]);
    }
    return name;

  }
  //****************************************************************************
  private String[] prefixName(String prefix,String[] name) {

    if (!doPrefixing) return name;
    for (int i=0; i<name.length; i++) {
      name[i] = prefixName(prefix,name[i]);
    }
    return name;

  }
  //****************************************************************************
  private String prefixName(String prefix,String name) {

    if (!doPrefixing || name.isEmpty()) return name;
    return prefix + name;

  }
  //****************************************************************************
  private String standardNameOf(String word) {

    return joinName( stemName(splitName(word)) );

  }
  //****************************************************************************
  private String standardTokenOf(String word) {

    return stemName(word);

  }
  //****************************************************************************
  private HashSet extractVocabulary(OntModel ontology,boolean flag) {

    HashSet<String> vocabulary = new HashSet();
    Iterator        iter;
    Resource        resource;

    // 1) Extrair nomes de conceitos
    iter = ontology.listNamedClasses();
    while (iter.hasNext()) {
      resource = (Resource) iter.next();
      vocabulary.add( standardNameOf(resource.getLocalName()) );
      filter.put(splitName(resource.getLocalName()));
      if (flag) addSpecial(resource.getLocalName());
    }

    // 2) Extrair nomes de instancias
    iter = ontology.listIndividuals();
    while (iter.hasNext()) {
      resource = (Resource) iter.next();
      vocabulary.add( standardNameOf(resource.getLocalName()) );
      filter.put(splitName(resource.getLocalName()));
      if (flag) addSpecial(resource.getLocalName());
    }

    // 3) Extrair nomes de propriedades de objetos
    iter = ontology.listObjectProperties();
    while (iter.hasNext()) {
      resource = (Resource) iter.next();
      vocabulary.add( standardNameOf(resource.getLocalName()) );
      filter.put(splitName(resource.getLocalName()));
      if (flag) addSpecial(resource.getLocalName());
    }

    if (!flag) {
      while (!specialList.isEmpty()) {
        String   s = (String) specialList.remove(0);
        String[] a = thesaurus.getSynonyms(s,filter);
        System.out.print("*"+s+": ");
        for (int i=0; i<a.length; i++) {
          System.out.print(a[i]+",");
        }
        System.out.println("\n");
      }
    }

    return vocabulary;

  }

  private void addSpecial(String s) {

    String[] a = splitName(s);
    for (int i=0; i<a.length; i++) {
       specialList.add(a[i]);
    }


  }


  //****************************************************************************
  private void writeToMoldel() {

    try {
      FileWriter writer = new FileWriter( mapModel.getPath() + "/common-vocabulary.txt" );
      Iterator   iter   = common.iterator();
      while (iter.hasNext()) {
        writer.write( iter.next()+"\n" );
      }
      writer.close();
    }
    catch (Exception ex) {
      MsgLogger.print("MapVocabulary.writeToMoldel()",ex);
    }

  }
  //****************************************************************************
  public void useStopwords(File file) {

     TextDocument document = new Tokenizer().lexWhite(file);
     List<String> line     = null;

     for (int i=0; i<document.getNumberOfLines(); i++) {
       line = document.getLine(i);
       for (int j=0; j<line.size(); j++) {
         stopwords.add( standardTokenOf(line.get(j)) );
       }
     }

     //nameFormat.setTokenizingOptions(doStemming,file);

  }
  //****************************************************************************
  public void noStemming() {

    doStemming  = false;

  }
  //****************************************************************************
  public void noSplitting() {

    doSplitting = false;

  }
  //****************************************************************************
  public void noPrefixing() {

    doPrefixing = false;

  }
  //****************************************************************************
  public void init(OntModel o1,OntModel o2) {

    Iterator iter;
    String   word;

    try {
      // Extraindo palavras distintas de cada ontologia
      vocabulary1 = extractVocabulary(o1,true);
      vocabulary2 = extractVocabulary(o2,false);

      // Calculando interceção entre vocabulários
      common = new HashSet();
      iter   = vocabulary1.iterator();
      while (iter.hasNext()) {
        word = (String)iter.next();
        if (!vocabulary2.contains(word)) continue;
        common.add(word);
      }

      // Salvando vocabulário comum
      writeToMoldel();
    }
    catch(Exception ex) {
      MsgLogger.print("MapVocabulary.init(OntModel,OntModel)",ex);
      ex.printStackTrace();
    }

  }
  //****************************************************************************
  public String get(String prefix,String name) {

//    return nameFormat.format(prefix,name);

    String standardForm = standardNameOf(name);  // split,stem,stopword,join
    if ( common.contains(standardForm) ) {
      return prefixName(prefix,standardForm);
    }
    else {
      return joinName(prefix,stemName(splitName(name)));
    }

  }
  //****************************************************************************
  public String get(String prefix,String name,HashSet<String> includeAndSkip) {

//    return nameFormat.format(prefix,name);
    String[] splitForm    = stemName(splitName(name));
    String   standardForm = joinName(splitForm);

    if ( common.contains(standardForm) ) {
      if ( includeAndSkip.contains(standardForm) ) return "";
      includeAndSkip.add(standardForm);
      for (int i=0; i<splitForm.length; i++) {
        includeAndSkip.add(splitForm[i]);
      }
      return prefixName(prefix,standardForm);
    }
    else {
      return joinName(prefix,splitForm,includeAndSkip);
    }

  }
  //****************************************************************************
  public String[] getTokens(String prefix,String name) {

    String standardForm = standardNameOf(name);
    if ( common.contains(standardForm) ) {
      return prefixName(prefix,new String[]{standardForm});
    }
    else {
      return prefixName(prefix,stemName(splitName(name)));
    }

  }
  //****************************************************************************


}
