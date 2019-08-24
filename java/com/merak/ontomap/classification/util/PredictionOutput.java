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
public class PredictionOutput implements Closeable {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final NumberFormat numberFormat;
  private final FileWriter   writer;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public PredictionOutput(String output,int fractionDigits)
    throws IOException
  {

    // Attibute Initialization
    this(new File(output),fractionDigits);

  }
  //****************************************************************************
  public PredictionOutput(File outputDirectory,int fractionDigits)
    throws IOException
  {

    // Attibute Initialization
    this.numberFormat = NumberFormat.getInstance();
    this.writer       = new FileWriter(outputDirectory);

    // Prepare Number Format
    numberFormat.setMaximumFractionDigits(fractionDigits);
    numberFormat.setMinimumFractionDigits(fractionDigits);

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private void addRankToRender(Rank rank,RenderTable render,String prefix) {

    // Create a line for the rank in the render table
    Row row = render.createRow();

    // Create a cell for each rank entry
    row.createCell( prefix + rank.getName() );
    for (DoubleParameter param : rank) {
      row.createCell( numberFormat.format(param.value)+" "+param.identifier );
    }

  }
  //****************************************************************************
  public void save(Category category,CategoryPrediction categoryPrediction,InstancePrediction instancePrediction)
    throws Exception
  {

    // Auxiliar
    RenderTable render = new RenderTable();
    Rank        rank   = null;

    // 1. Render Instance Predictions
    // for each instance,
    for (int instanceId=0; instanceId<category.getNumberOfDirectInstances(); instanceId++) {
      rank = instancePrediction.rankInstance(instanceId);
      rank.pruneWithRelativeThreshold(0.8);
      addRankToRender(rank,render,"+");
    }

    // 2. Render Category Prediction
    rank = categoryPrediction.rankRow(category);
    rank.pruneWithRelativeThreshold(0.8);
    addRankToRender(rank,render,"@");

    // Finalizando
    writer.write( render.toString() + "\n" );

  }
  //****************************************************************************
  public void close() {

    try {
      writer.close();
    }
    catch (Exception ex) {
      Application.debug.print("PredictionOutput.close()",ex);
    }

  }
  //****************************************************************************

}
