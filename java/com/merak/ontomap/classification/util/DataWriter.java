// Package
///////////////
package com.merak.ontomap.classification.util;

// Imports
///////////////
import java.io.*;
import java.text.*;
import com.merak.ontomap.model.*;
import com.merak.ontomap.classification.*;

// Class
///////////////
public abstract class DataWriter {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  protected final MapModel     model;
  protected final NumberFormat integerFormat;
  protected final NumberFormat doubleFormat;
  protected final File         outputDirectory;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public DataWriter(MapModel model,int fractionDigits) {

    // Attibute Initialization
    this.model           = model;
    this.integerFormat   = NumberFormat.getInstance();
    this.doubleFormat    = NumberFormat.getInstance();
    this.outputDirectory = new File( model.getPath() + "/stats" );

    // Prepare Number Format and create Output Directory
    doubleFormat.setMaximumFractionDigits(fractionDigits);
    doubleFormat.setMinimumFractionDigits(fractionDigits);
    integerFormat.setMaximumFractionDigits(0);
    outputDirectory.mkdir();

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public abstract void save(OneWayCategoryPrediction prediction);

  //****************************************************************************
  public abstract void save(TwoWayCategoryPrediction prediction);

  //****************************************************************************
}