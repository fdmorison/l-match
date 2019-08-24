// Package
///////////////
package com.merak.ontomap.model.extraction.util;

// Imports
///////////////
import java.io.File;
import java.util.*;
import com.merak.core.text.*;

public class NameTokenizer {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private StringBuffer    buffer    = new StringBuffer();
  private List<String>    tokens    = new ArrayList<String>(10);
  private HashSet<String> stopwords = new HashSet<String>();

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private static boolean isDelimiter(char c) {

    return !ASCII.isLetter(c) && !ASCII.isDigit(c);

  }
  //****************************************************************************
  private static boolean isBodyCharacter(char c) {

    return ASCII.isLowerCase(c) || ASCII.isDigit(c);

  }  
  //****************************************************************************
  private static boolean isUpperCase(char c) {

    return ASCII.isUpperCase(c);

  }
  //****************************************************************************
  private static boolean isLowerCase(char c) {

    return ASCII.isLowerCase(c);

  }
  //****************************************************************************
  private String formatCharacter(char c) {

    switch (c) {
        case '0': return "NBzero";
        case '1': return "NBone";
        case '2': return "NBtwo";
        case '3': return "NBthree";
        case '4': return "NBfour";
        case '5': return "NBfive";
        case '6': return "NBsix";
        case '7': return "NBseven";
        case '8': return "NBeight";
        case '9': return "NBnine";
        default : return Character.toLowerCase();
    }

  }   
  //****************************************************************************
  public void addStopWords(File file) {

    // Auxiliar
    TextDocument document = new Tokenizer().lexWhite(file);
    List<String> line     = null;

    // Store each stop words in the hash
    for (int i=0; i<document.getNumberOfLines(); i++) {
      line = document.getLine(i);
      for (int j=0; j<line.size(); j++) {
        stopwords.add( line.get(j) );
      }
    }

  }
  //****************************************************************************
  /* "BeautifulHome" "Beautiful<delimiter>Home" "Beautiful<delimiter>home"
   */
  public String[] getTokens(String name) {

    // Auxiliar
    String word   = null;
    int    length = name.length();

    // Prepare
    tokens.clear();

    // Spliting the name
    for (int i=0; i<length;) {
      // Enquanto for delimitador e nao chegar no fim da palavra, ignore      
      while ( i<length && isDelimiter(name.charAt(i)) ) i++;
      if (i==length) break;
          
      /// Initialize the Token
      word = "";
      
      // Tentar extrair palavra
      // Enquanto for maiuscula  , adicione como minuscula
      while ( i<length && isUpperCase(name.charAt(i)) ) word += Character.toLowerCase(name.charAt(i++));

      // Se apenas a inicial da palavra era maiuscula
      if ( word.length()<2 ) {         
        // Enquanto for minuscula ou numero, adicione
        while ( i<length && isBodyCharacter(name.charAt(i)) ) {
            word += formatCharacter( name.charAt(i++) );
        }
      }

      // Skip invalid tokens
      if ( word.isEmpty() ) continue;
      if (!tokens.isEmpty() && stopwords.contains(word)) continue;

      // Inclua o token formatado
      tokens.add( word );
    }

    // Remove first stop words
    if (tokens.size()>1) {
      if ( stopwords.contains(tokens.get(0)) ) tokens.remove(0);
    }

    // Return an array of words
    return tokens.toArray( new String[tokens.size()] );

  }
  //****************************************************************************

}