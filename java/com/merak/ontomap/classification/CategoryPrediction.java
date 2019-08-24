// Package
/////////////////
package com.merak.ontomap.classification;

// Imports
/////////////////
import java.io.*;
import java.text.*;
import java.util.*;
import com.merak.core.*;
import com.merak.core.text.*;
import com.merak.ai.classification.*;
import com.merak.ontomap.model.*;

// Implementation
/////////////////
public class CategoryPrediction {

  //****************************************************************************
  public static class Entry {
    public double similarity = 0;
    public int    overlap    = 0;
  }

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final Entry[][] table;
  public  final Ontology  ontologyA;
  public  final Ontology  ontologyB;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public CategoryPrediction(Ontology ontologyA,Ontology ontologyB) {

    // Auxiliar
    int rows = ontologyA.getNumberOfCategories();
    int cols = ontologyB.getNumberOfCategories();

    // Attributes Initialization
    this.table     = new Entry[rows][cols];
    this.ontologyA = ontologyA;
    this.ontologyB = ontologyB;

    // Fill the table
    for (int i=0; i<rows; i++) {
      for (int j=0; j<cols; j++) table[i][j] = new Entry();
    }

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public Entry get(Category row,Category col)
    throws PredictionException
  {

    // Check error
    if ( row.getOntology()!=ontologyA || col.getOntology()!=ontologyB) {
      throw new PredictionException("As categorias nao pertencem as ontologias que criaram esta tabela.");
    }

    // Return the entry
    return table[ row.getOntologicalId() ][ col.getOntologicalId() ];

  }
  //****************************************************************************
  public Entry[] getRow(Category row)
    throws PredictionException
  {

    // Check error
    if ( row.getOntology() != ontologyA ) {
      throw new PredictionException("A categoria '"+row.getName()+"' nao pertence a ontologia-linha desta tabela");
    }

    // Return the entry
    return table[ row.getOntologicalId() ];

  }
  //****************************************************************************
  public Entry[] getColumn(Category col)
    throws PredictionException
  {

    // Check error
    if ( col.getOntology() != ontologyB ) {
      throw new PredictionException("A categoria '"+col.getName()+"' nao pertence a ontologia-coluna desta tabela");
    }

    // Auxiliar
    Entry[] column = new Entry[ontologyB.getNumberOfCategories()];

    // Fill the column array
    for (int row=0; row<column.length; row++) {
      column[row] = table[row][col.getOntologicalId()];
    }

    // Return the column
    return column;

  }
  //****************************************************************************
  public Rank rankRow(Category row)
    throws PredictionException
  {

    // Check Error
    if (ontologyA!=row.getOntology()) {
      throw new PredictionException("A categoria '"+row.getName()+"' nao pertence a ontologia-linha desta tabela");
    }

    // Auxiliar
    Rank     rank             = new Rank( row.getName(), ontologyB.getNumberOfConcreteCategories() );
    Category trainingCategory = null;
    int      j,categoryId;

    for (j=categoryId=0; categoryId<ontologyB.getNumberOfCategories(); categoryId++) {
      // Proxima categoria de treino a entrar no rank
      trainingCategory = ontologyB.getCategory(categoryId);

      // Ignorar categorias se abstrata (vazia)
      if ( trainingCategory.isAbstract() ) continue;

      // Adicionar categoria se concreta (instanciada)
      rank.set(j++, trainingCategory.getName(), table[row.getOntologicalId()][categoryId].similarity );
    }
    rank.sort();

    // Return the sorted rank
    return rank;

  }
  //****************************************************************************
  public Rank rankColumn(Category col)
    throws PredictionException
  {

    // Check Error
    if (ontologyB!=col.getOntology()) {
      throw new PredictionException("A categoria '"+col.getName()+"' nao pertence a ontologia-coluna desta tabela");
    }

    // Auxiliar
    Rank     rank             = new Rank( col.getName(), ontologyA.getNumberOfConcreteCategories() );
    Category trainingCategory = null;
    int      j,categoryId;

    for (j=categoryId=0; categoryId<ontologyA.getNumberOfCategories(); categoryId++) {
      // Proxima categoria de treino a entrar no rank
      trainingCategory = ontologyA.getCategory(categoryId);

      // Ignorar categorias se abstrata (vazia)
      if ( trainingCategory.isAbstract() ) continue;

      // Adicionar categoria se concreta (instanciada)
      rank.set( j++, trainingCategory.getName(), table[categoryId][col.getOntologicalId()].similarity );
    }
    rank.sort();

    // Return the sorted rank
    return rank;

  }
  //****************************************************************************
  public CategoryPrediction combineWith(CategoryPrediction transpose)
    throws PredictionException
  {

    // Check error
    if ( ontologyA!=transpose.ontologyB || ontologyB!=transpose.ontologyA) {
      throw new PredictionException("As tabelas nao sao transpostas.");
    }

    // Auxiliar
    CategoryPrediction combination = new CategoryPrediction(ontologyA,ontologyB);

    // Combine
    for (int i=0; i<ontologyA.getNumberOfCategories(); i++) {
      for (int j=0; j<ontologyB.getNumberOfCategories(); j++) {
        combination.table[i][j].overlap     = table[i][j].overlap + transpose.table[j][i].overlap;
        combination.table[i][j].similarity  = (table[i][j].similarity + transpose.table[j][i].similarity)/2.0;
        combination.table[i][j].similarity *= StatTools.getEntropy(new double[]{ table[i][j].similarity,transpose.table[j][i].similarity });
      }
    }
    return combination;

  }
  //****************************************************************************
  public void writeToFile(String path)
    throws IOException
  {

    // Auxiliar
    NumberFormat nf     = NumberFormat.getInstance();
    FileWriter   writer = new FileWriter(path);
    RenderTable  render = new RenderTable();
    Row          row    = null;
    Rank         rank   = null;
    int          rows   = ontologyA.getNumberOfCategories();

    // Write a header comment
    writer.write( "#@fun: " + ontologyA.getName() + " X " + ontologyB.getName() + "\n\n");
    
    nf.setMaximumFractionDigits(5);
    nf.setMinimumFractionDigits(5);    

    // Write the content
    try {
      // Render the results into an ascii file
      for (int i=0; i<rows; i++) {
        // Crie uma linha no renderizador para o rank da proxima categoria
        rank = rankRow(ontologyA.getCategory(i));
        if (rank.get(0).value == 0) continue;
        
        row  = render.createRow();
        row.createCell( rank.getName() );
        // Para cada posicao do rank, renderize o par <categoria,similaridade>
        rank.pruneWithRelativeThreshold(0.8);
        for (int j=0; j<rank.length(); j++) {
          row.createCell( nf.format(rank.get(j).value) + " " + rank.get(j).identifier  );
        }
      }
      // Finalizando
      writer.write( render.toString() );
      writer.close();
    }
    catch (PredictionException ex) {
      Application.debug.print("CategoryPrediction.writeToFile(String)",ex);
    }

  }
  //****************************************************************************

}
