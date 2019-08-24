// Package
///////////////
package com.merak.ontomap.mapping;

// Imports
///////////////
import java.io.*;
import java.util.*;
import java.text.ParseException;
import com.merak.core.*;
import com.merak.core.text.*;
import com.merak.ontomap.model.*;

// Class
///////////////
public class MapTablePersistence {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final MapModel model;
  private final File     file;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public MapTablePersistence(MapModel model,File file) {

    // Attribute Initialization
    this.model = model;
    this.file  = file;

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public MapTable load() {

    return null;

  }
  //****************************************************************************
  public void save(MapTable table) {

  }
  //****************************************************************************
}