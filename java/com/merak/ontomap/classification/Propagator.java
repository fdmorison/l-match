// Package
///////////////
package com.merak.ontomap.classification;

// Imports
///////////////
import com.merak.ontomap.model.Ontology;
import com.merak.ontomap.model.Category;
import com.merak.ontomap.classification.util.IntegerTable;
import com.merak.ontomap.classification.util.DoubleTable;

// Class
///////////////
public class Propagator {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public Propagator() {

  }
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private void propagateRow(Category source,IntegerTable table) {

    // Se a categoria for uma folha, nao ha como propagar dados de subclasses
    if (source.isLeaf()) return;

    // 1. INICIAR POR CLASSES MAIS PROFUNDAS
    // A propagacao deve ser no sentido 'bottom-up'
    for (Category child : source.getChildren()) {
      propagateRow(child,table);
    }

    // Auxiliar
    int value = 0;

    // 2. PROPAGAR VALORES DAS SUBCLASSES PARA A CATEGORIA
    // Para cada categoria de treino,
    for (Category target : table.colOntology) {
      // Iniciando
      value = 0;
      // Propagando por soma
      for (Category child : source.getChildren()) {
        value += table.get(child,target);
      }
      // Finalizando
      table.sum(source,target,value);
    }

  }
  //****************************************************************************
  private void propagateColumn(Category column,IntegerTable table) {

    // Se a categoria for uma folha, nao ha como propagar dados de subclasses
    if (column.isLeaf()) return;

    // 1. INICIAR POR CLASSES MAIS PROFUNDAS
    // A propagacao deve ser no sentido 'bottom-up'
    for (Category child : column.getChildren()) {
      propagateColumn(child,table);
    }

    // Auxiliar
    int value = 0;

    // 2. PROPAGAR VALORES DAS SUBCLASSES PARA A CATEGORIA
    // Para cada categoria de treino,
    for (Category target : table.rowOntology) {
      // Iniciando
      value = 0;
      // Propagando por soma
      for (Category child : column.getChildren()) {
        value += table.get(target,child);
      }
      // Finalizando
      table.sum(target,column,value);
    }

  }  
  //****************************************************************************
  private void propagateRow(Category source,DoubleTable table) {

    // Se a categoria for uma folha, nao ha como propagar dados de subclasses
    if (source.isLeaf()) return;

    // 1. INICIAR POR CLASSES MAIS PROFUNDAS
    // A propagacao deve ser no sentido 'bottom-up'
    for (Category child : source.getChildren()) {
      propagateRow(child,table);
    }

    // Auxiliar
    double[] values = new double[ source.getNumberOfChildren() + (source.isConcrete()?1:0) ];
    double   value  = 0;
  
    // 2. PROPAGAR VALORES DAS SUBCLASSES PARA A CATEGORIA
    // Para cada categoria de treino,
    for (Category target : table.colOntology) {
      // Iniciando
      values[values.length-1] = table.get(source,target); // So tera efeito se a categoria source for concreta
      // Propagando
      for (int i=0; i<source.getNumberOfChildren(); i++) {
        values[i] = table.get(source.getChild(i), target);
      }
      // Finalizando
      value  = StatTools.getNoisyOr(values);
      value *= StatTools.getEntropy(values);
      table.set(source,target,value);
    }

  }  
  //****************************************************************************
  private void propagateColumn(Category column,DoubleTable table) {

    // Se a categoria for uma folha, nao ha como propagar dados de subclasses
    if (column.isLeaf()) return;

    // 1. INICIAR POR CLASSES MAIS PROFUNDAS
    // A propagacao deve ser no sentido 'bottom-up'
    for (Category child : column.getChildren()) {
      propagateColumn(child,table);
    }

    // Auxiliar
    double[] values = new double[ column.getNumberOfChildren() + (column.isConcrete()?1:0) ];
    double   value  = 0;
  
    // 2. PROPAGAR VALORES DAS SUBCLASSES PARA A CATEGORIA
    // Para cada categoria de treino,
    for (Category target : table.rowOntology) {
      // Iniciando
      values[values.length-1] = table.get(target,column); // So tera efeito se a categoria column for concreta
      // Propagando
      for (int i=0; i<column.getNumberOfChildren(); i++) {
        values[i] = table.get(target,column.getChild(i));
      }
      // Finalizando
      value  = StatTools.getNoisyOr(values);
      value *= StatTools.getEntropy(values);
      table.set(target,column,value);
    }

  }   
  //****************************************************************************
  public void propagate(IntegerTable table) {

    for (Category root : table.rowOntology.getRootCategories()) {
      propagateRow(root,table);
    }
    
    for (Category root : table.colOntology.getRootCategories()) {
      propagateColumn(root,table);
    } 
    
  }
  //****************************************************************************
  public void propagateRows(IntegerTable table) {

    for (Category root : table.rowOntology.getRootCategories()) {
      propagateRow(root,table);
    }
    
  }  
  //****************************************************************************
  public void propagateColumns(IntegerTable table) {
   
    for (Category root : table.colOntology.getRootCategories()) {
      propagateColumn(root,table);
    } 
    
  }  
  //****************************************************************************
  public void propagate(DoubleTable table) {

    for (Category root : table.rowOntology.getRootCategories()) {
      propagateRow(root,table);
    }
  
    for (Category root : table.colOntology.getRootCategories()) {
      propagateColumn(root,table);
    }    

  }
  //****************************************************************************
  public void propagateRows(DoubleTable table) {

    for (Category root : table.rowOntology.getRootCategories()) {
      propagateRow(root,table);
    }    

  }  
  //****************************************************************************
  public void propagateColumns(DoubleTable table) {
 
    for (Category root : table.colOntology.getRootCategories()) {
      propagateColumn(root,table);
    }    

  }  
  //****************************************************************************
}