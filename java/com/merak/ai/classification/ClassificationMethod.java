// Package
///////////////
package com.merak.ai.classification;

// Imports
///////////////
import java.text.ParseException;

// Class
///////////////
public enum ClassificationMethod {

  //~ Inner Classes ////////////////////////////////////////////////////////////
  /****************************************************************************/
  KNN, NAIVEBAYES, SVM, MAXENT, NBSHRINKAGE, TFIDF, PRIND, EM_SIMPLE;

  //~ Methods //////////////////////////////////////////////////////////////////
  /****************************************************************************/
  /* Converte uma string num metodo de classificao
   */
  public static ClassificationMethod parseValue(String method) throws ParseException {

    try {
      return valueOf( method.toUpperCase() );
    }
    catch (Exception ex) {
      throw new ParseException("Metodo de classificacao desconhecido: '"+method+"'",0);
    }

  }
  /****************************************************************************/

}





