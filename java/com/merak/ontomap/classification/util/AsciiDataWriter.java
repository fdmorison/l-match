// Package
///////////////
package com.merak.ontomap.classification.util;

// Imports
///////////////
import java.io.*;
import java.text.*;
import java.util.*;
import com.merak.core.*;
import com.merak.core.text.*;
import com.merak.ai.classification.*;
import com.merak.ontomap.model.*;
import com.merak.ontomap.classification.*;

// Class
///////////////
public class AsciiDataWriter extends DataWriter {

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public AsciiDataWriter(MapModel model,int fractionDigits) {

    // Super Attibute Initialization
    super(model,fractionDigits);

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private void renderSimilarityRank(Rank rank,RenderTable render) {

    // Skip rank without similarity
    if (rank.get(0).value <= 0) return;

    // Prune the rank
    rank.pruneWithRelativeThreshold(0.8);

    // Render a line for the rank
    Row row = render.createRow();
    row.createCell( rank.getName() );
    row.createCell( " | " );
    for (int j=0; j<rank.length(); j++) {
      row.createCell( numberFormat.format(rank.get(j).value) + " " + rank.get(j).identifier );
      row.createCell( " | " );
    }

  }
  //****************************************************************************
  private void renderOverlapRank(Rank rank,RenderTable render) {

    // Skip rank without similarity
    if (rank.get(0).value <= 0) return;

    // Prune the rank
    rank.pruneWithAbsoluteThreshold(1);

    // Render a line for the rank
    Row row = render.createRow();
    row.createCell( rank.getName() );
    for (int j=0; j<rank.length(); j++) {
      row.createCell( ((int)rank.get(j).value) + " " + rank.get(j).identifier );
      row.createCell( " | " );
    }

  }
  //****************************************************************************
  public void save(OneWayCategoryPrediction prediction) {

    try {
      // Auxiliar
      FileWriter  writer = null;
      RenderTable render = null;
      Rank        rank   = null;

      // 1. SALVAR RANKS DE SIMILARIDADE
      writer = new FileWriter( outputDirectory + "/OneWay.Similarity_"+prediction.sourceOntology.getName()+".to."+prediction.targetOntology.getName()+".txt" );
      render = new RenderTable();
      // Para cada categoria de teste, rankear as classes de treino por similaridade
      for (Category source : prediction.sourceOntology) {
        rank = prediction.similarityTable.toRank(source);
        renderSimilarityRank(rank,render);
      }
      writer.write(render.toString());
      writer.close();

      // 2. SALVAR RANKS DE INTERSECAO
      writer = new FileWriter( outputDirectory + "/OneWay.Overlap_"+prediction.sourceOntology.getName()+".to."+prediction.targetOntology.getName()+".txt" );
      render = new RenderTable();
      // Para cada categoria de teste, rankear as classes de treino por intersecao
      for (Category source : prediction.sourceOntology) {
        rank = prediction.overlapTable.toRank(source);
        rank.setName( source.getName() + "("+source.getNumberOfDirectInstances()+")" );
        renderOverlapRank(rank,render);
      }
      writer.write(render.toString());
      writer.close();
    }
    catch (Exception ex) {
      Application.debug.print("DataWriter.save(OneWayCategoryPrediction)",ex);
    }

  }
  //****************************************************************************
  public void save(TwoWayCategoryPrediction prediction) {

    try {
      // Auxiliar
      FileWriter  writer = null;
      RenderTable render = new RenderTable();
      Rank        rank   = null;

      // 1. SALVAR DADOS DO PONTO DE VISTA DA ONTOLOGIA A
      // 1.a) SALVAR RANKS DE SIMILARIDADE
      writer = new FileWriter( outputDirectory + "/TwoWay.Similarity_"+model.ontologyA.getName()+".and."+model.ontologyB.getName()+".txt" );
      render = new RenderTable();
      for (Category category : model.ontologyA) {
        rank = prediction.getSimilarityFrom(category);
        renderSimilarityRank(rank,render);
      }
      writer.write(render.toString());
      writer.close();

      // 1.b) SALVAR RANKS DE OVERLAP
      writer = new FileWriter( outputDirectory + "/TwoWay.Overlap_"+model.ontologyA.getName()+".and."+model.ontologyB.getName()+".txt" );
      render = new RenderTable();
      for (Category category : model.ontologyA) {
        rank = prediction.getOverlapFrom(category);
        rank.setName( category.getName() + "("+category.getNumberOfDirectInstances()+")" );
        renderOverlapRank(rank,render);
      }
      writer.write(render.toString());
      writer.close();

      // 2. SALVAR DADOS DO PONTO DE VISTA DA ONTOLOGIA B
      writer = new FileWriter( outputDirectory + "/TwoWay.Similarity_"+model.ontologyB.getName()+".and."+model.ontologyA.getName()+".txt" );
      render = new RenderTable();
      for (Category category : model.ontologyB) {
        rank = prediction.getSimilarityFrom(category);
        renderSimilarityRank(rank,render);
      }
      writer.write(render.toString());
      writer.close();

      // 2.b) SALVAR RANKS DE OVERLAP
      writer = new FileWriter( outputDirectory + "/TwoWay.Overlap_"+model.ontologyB.getName()+".and."+model.ontologyA.getName()+".txt" );
      render = new RenderTable();
      for (Category category : model.ontologyB) {
        rank = prediction.getOverlapFrom(category);
        rank.setName( category.getName() + "("+category.getNumberOfDirectInstances()+")" );
        renderOverlapRank(rank,render);
      }
      writer.write(render.toString());
      writer.close();
    }
    catch (Exception ex) {
      Application.debug.print("DataWriter.save(TwoWayCategoryPrediction)",ex);
    }

  }
  //****************************************************************************
}