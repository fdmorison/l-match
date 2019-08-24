// Package
///////////////
package com.merak.ontomap.classification.util;

// Imports
///////////////
import com.merak.ontomap.model.Ontology;
import com.merak.ontomap.model.Category;
import com.merak.ontomap.model.CategoryRank;
import com.merak.ai.classification.Rank;

// Class
///////////////
public class DoubleTable {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  public  final Ontology   rowOntology;
  public  final Ontology   colOntology;
  public  final double     relativeThreshold;
  private final double[][] table;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public DoubleTable(Ontology rowOntology,Ontology colOntology,double relativeThreshold) {

    // Attribute Initialization
    this.rowOntology       = rowOntology;
    this.colOntology       = colOntology;
    this.table             = new double[ rowOntology.getNumberOfCategories() ][ colOntology.getNumberOfCategories() ];
    this.relativeThreshold = relativeThreshold;

  }
  //****************************************************************************
  public DoubleTable(Ontology rowOntology,Ontology colOntology,double relativeThreshold,double initialValue) {

    // Attribute Initialization
    this(rowOntology,colOntology,relativeThreshold);

    // Table Initialization
    // for each Category from RowOntology, do
    for (int row=0; row<rowOntology.getNumberOfCategories(); row++) {
      // for each Category from ColOntology, do
      for (int col=0; col<colOntology.getNumberOfCategories(); col++) {
        table[row][col] = initialValue;
      }
    }

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private final void checkNamespace(Category row, Category col) {

    // Check namespace of row and col categories
    if (!rowOntology.contains(row)) throw new RuntimeException("Categoria "+row+" nao pertence a ontologia "+rowOntology);
    if (!colOntology.contains(col)) throw new RuntimeException("Categoria "+col+" nao pertence a ontologia "+colOntology);

  }
  //****************************************************************************
  private final CategoryRank rankRow(final Category row) {

    // Auxiliar
    CategoryRank rank     = new CategoryRank( row, colOntology.getNumberOfConcreteCategories() );
    double       priority = 0;

    for (Category col : colOntology) {
      // Ignorar categoria diferente
      priority = table[ row.getOntologicalId() ][ col.getOntologicalId() ];
      if ( priority==0 ) continue;
      rank.createEntry(col,priority);
    }
    rank.sort();
    rank.pruneWithRelativeThreshold(relativeThreshold);
    return rank;

  }
  //****************************************************************************
  private final CategoryRank rankColumn(final Category col) {

    // Auxiliar
    CategoryRank rank     = new CategoryRank( col, colOntology.getNumberOfConcreteCategories() );
    double       priority = 0;

    for (Category row : rowOntology) {
      // Ignorar categoria diferente
      priority = table[ row.getOntologicalId() ][ col.getOntologicalId() ];
      if ( priority==0 ) continue;
      rank.createEntry(row,priority);
    }
    rank.sort();
    rank.pruneWithRelativeThreshold(relativeThreshold);
    return rank;

  }
  //****************************************************************************
  public final double get(Category row, Category col) {

    // Return the value of table[row][col]
    checkNamespace(row,col);
    return table[ row.getOntologicalId() ][ col.getOntologicalId() ];

  }
  //****************************************************************************
  public final void set(Category row, Category col, double value) {

    // Set a value to table[row][col]
    checkNamespace(row,col);
    table[ row.getOntologicalId() ][ col.getOntologicalId() ] = value;

  }
  //****************************************************************************
  public final double sum(Category row, Category col, double value) {

    // Sum to the value in table[row][col]
    checkNamespace(row,col);
    return table[ row.getOntologicalId() ][ col.getOntologicalId() ] += value;

  }
  //****************************************************************************
  public final double subtract(Category row, Category col, double value) {

    // Subtract from the value in table[row][col]
    checkNamespace(row,col);
    return table[ row.getOntologicalId() ][ col.getOntologicalId() ] -= value;

  }
  //****************************************************************************
  public final double multiply(Category row, Category col, double value) {

    // Multiply the value in table[row][col]
    checkNamespace(row,col);
    return table[ row.getOntologicalId() ][ col.getOntologicalId() ] *= value;

  }
  //****************************************************************************
  public final double divide(Category row, Category col, double value) {

    // Divide the value in table[row][col]
    checkNamespace(row,col);
    return table[ row.getOntologicalId() ][ col.getOntologicalId() ] /= value;

  }
  //****************************************************************************
  public final CategoryRank toRank(Category category) {

    if ( rowOntology.contains(category) ) return rankRow(category);
    if ( colOntology.contains(category) ) return rankColumn(category);
    throw new RuntimeException("Categoria "+category+" nao pertence as ontologias "+rowOntology+" e "+colOntology);

  }
  //****************************************************************************
  public final double[] toArray(Category category) {

    // Se 'category' eh indice de linha, entao retorne a linha como array
    if ( rowOntology.contains(category) ) {
      return table[ category.getOntologicalId() ];
    }

    // Se 'category' nao eh indice nem de linha e nem de coluna, entao retorne erro
    if ( !colOntology.contains(category) ) {
      throw new RuntimeException("Categoria "+category+" nao pertence as ontologias "+rowOntology+" e "+colOntology);
    }

    // Se 'category' eh indice de coluna, entao aloque um array com os valores da coluna
    double[] column = new double[ colOntology.getNumberOfCategories() ];
    for (int row=0; row<column.length; row++) {
      column[row] = table[ row ][ category.getOntologicalId() ];
    }
    return column;

  }
  //****************************************************************************
  public boolean isTranspose(DoubleTable transpose) {

    return rowOntology==transpose.colOntology && colOntology==transpose.rowOntology ;

  }
  //****************************************************************************
}

