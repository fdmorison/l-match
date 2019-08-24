// Package
///////////////
package com.merak.ai.classification.rainbow;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.merak.core.text.*;
import com.merak.ai.classification.*;

public class OutputParser {

  //~ Attributes ///////////////////////////////////////////////////////////////
  private Tokenizer tokenizer = new Tokenizer();

  //~ Cosntructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private Rank<String,String> parseQueryOutput(String name,TextDocument document) {

    // Auxiliar
    List<String> line       = null;
    String       token      = null;
    double       similarity = 0;
    int          i          = 0;

    // Ignorando linhas iniciais produzidas por uma classificacao com o SVM
    while (i<document.getNumberOfLines()) {
      token = document.getLine(i).get(0);
      if (token.equals("user:") ) i+=3; else
      if (token.equals("random")) i++ ; else break;
    }

    // Lendo classes rankeadas por probabilidades
    int                 size = document.getNumberOfLines()-i;
    Rank<String,String> rank = new Rank(name,size);
    while (i<document.getNumberOfLines()) {
      line       = document.getLine(i++);
      token      = line.get(0);
      similarity = Double.parseDouble(line.get(1));
      rank.createEntry(token,similarity);
    }
    rank.normalize();
    return rank;

  }
  //****************************************************************************
  public Rank<String,String> parseQueryOutput(String name,String output)
    throws ClassificationException
  {

    try {
      TextDocument document = tokenizer.lexWhite(output);
      return parseQueryOutput(name,document);
    }
    catch (Exception ex) {
      throw new ClassificationException("Erro ao processar saída da rainbow: conteúdo inválido.",ex);
    }

  }
  //****************************************************************************
  public Rank<String,String> parseQueryOutput(String name,File output)
    throws ClassificationException
  {

    try {
      TextDocument document = tokenizer.lexWhite(output);
      return parseQueryOutput(name,document);
    }
    catch (Exception ex) {
      throw new ClassificationException("Erro ao processar saída da rainbow: conteúdo inválido.",ex);
    }

  }
  //****************************************************************************/


}
