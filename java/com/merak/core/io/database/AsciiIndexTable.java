// Package
///////////////
package com.merak.core.io.database;

// Imports
///////////////
import java.util.*;
import java.io.*;
import com.merak.core.MsgLogger;

// Public Class
///////////////
public class AsciiIndexTable {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private int         id_sequence = 0;
  private FileWriter  file_dat    = null;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public AsciiIndexTable(String path) throws IOException {

    // Attribute Initialization
    file_dat = new FileWriter(path + ".txt");

  }
  //****************************************************************************
  public AsciiIndexTable(String path,String header) throws IOException {

    // Attribute Initialization
    this(path);
    file_dat.write("#"+header+"\n");

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public int createEntry(String data) throws IOException {

    // Auxiliar
    int id = id_sequence++;

    // Write data
    if (id==0) {
      file_dat.write(id + " " + data);
    }
    else {
      file_dat.write("\n" + id + " " + data);
    }

    // Return the Id
    return id;

  }
  //****************************************************************************
  public void append(String data) throws IOException {

    // Write data
    file_dat.write(data);

  }
  //****************************************************************************
  public int getNextId() {

    return id_sequence;

  }
  //****************************************************************************
  public void close() {

    try {
      file_dat.close();
    }
    catch(Exception ex) {
      MsgLogger.print("AsciiIndexTable.close()",ex);
    }

  }
  //****************************************************************************


}
