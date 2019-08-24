// Package
///////////////
package com.merak.ontomap.extraction;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.merak.core.text.*;

public class NameFormat {

  //~ Attributes ///////////////////////////////////////////////////////////////
  /****************************************************************************/
  private HashSet<String> stopwords      = new HashSet();
  private HashSet<String> vocabulary     = new HashSet();
  private Stemmer         stemmer        = new Stemmer();
  private boolean         splitNamesFlag = true;
  private boolean         usePrefixFlag  = true;
  private boolean         useStemmerFlag = true;

  //~ Constructors /////////////////////////////////////////////////////////////
  /****************************************************************************/

  //~ Methods //////////////////////////////////////////////////////////////////
  /****************************************************************************/
  private boolean isUpperCase(char c) {

    return Character.isUpperCase(c);

  }
  /****************************************************************************/
  private boolean isLowerOrDigit(char c) {

    return Character.isLowerCase(c) || Character.isDigit(c);

  }
  /****************************************************************************/
  private boolean isLetterOrDigit(char c) {

    return Character.isLetterOrDigit(c);

  }
  /****************************************************************************/
  private String getNormalForm(String word) {

    if (useStemmerFlag) {
      stemmer.add( word.toCharArray() , word.length() );
      stemmer.stem();
      word = stemmer.toString();
    }

    vocabulary.add(word);

    return word;

  }
  /****************************************************************************/
  private boolean isStopword(String word) {

	return stopwords.contains(word);

  }
  /****************************************************************************/
  private void loadStopwords(File file) {

     TextDocument document = new Tokenizer().lexWhite(file);
     List<String> line     = null;
     String       token    = null;

     for (int i=0; i<document.getLineCount(); i++) {
       line = document.getLine(i);
       for (int j=0; j<line.size(); j++) {
         token = getNormalForm(line.get(j));
         stopwords.add(token);
       }
     }

  }
  /****************************************************************************/
  public void setTokenizingOptions(boolean useStemmer,File stopwords) {

    useStemmerFlag = useStemmer;
    loadStopwords(stopwords);

  }
  /****************************************************************************/
  public void usePrefix(boolean flag) {

    usePrefixFlag = flag;

  }
  /****************************************************************************/
  public void splitNames(boolean flag) {

    splitNamesFlag = flag;

  }
  /****************************************************************************/
  public Iterator<String> getVocabulary() {

    return vocabulary.iterator();

  }
  /****************************************************************************/
  public String format(String prefix,String name) {

    String output = "";
    String word   = null;
    int    length = name.length();
    int    i      = 0;

    while (i<length) {

      word = "";

      // 1) Procurando o inicio da proxima palavra
      while ( i<length && !isLetterOrDigit(name.charAt(i)) ) {
        i++;
      }

      // 2) Convertendo para minusculo o prefixo da palavra
      while ( i<length && isUpperCase(name.charAt(i)) ) {
        word += Character.toLowerCase(name.charAt(i++));
      }

      // 3) Procurando o final da palavra
      while ( i<length && isLowerOrDigit(name.charAt(i)) ) {
        word += name.charAt(i++);
      }

      // 5) Normalizando, prefixando e incluindo no texto final
      word = getNormalForm(word);
      if ( isStopword(word) ) continue;
      if ( usePrefixFlag    ) output += prefix + word; else output += word;
      if ( splitNamesFlag   ) output += " ";

    }

    return output;

  }
  /****************************************************************************/
  public void printVocabulary(File file) {

    try {
      FileWriter writer = new FileWriter(file);
      Iterator   iter   = vocabulary.iterator();
      while (iter.hasNext()) {
        String s = (String) iter.next();
        writer.write(s+"\n");
      }
      writer.close();
      vocabulary.clear();
    }
    catch (Exception ex) {}

  }

  /****************************************************************************/

}

