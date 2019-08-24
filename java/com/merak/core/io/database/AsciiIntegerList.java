// Package
///////////////
package com.merak.core.io.database;

// Imports
///////////////
import java.util.*;
import java.io.*;
import com.merak.core.text.*;

// Public Class
///////////////
public class AsciiIntegerList extends Table {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final IndexTable table;
  private final Tokenizer  tokenizer = new Tokenizer();

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public AsciiIntegerList(String name,File directory) throws IOException {

    this(name,directory,IndexTable.Mode.InsertSelect);

  }
  //****************************************************************************
  public AsciiIntegerList(String name,File directory,IndexTable.Mode mode) throws IOException {

    // Super Attribute Initialization
    super(name,directory,mode);

    // Attribute Initialization
    this.table = new BasicIndexTable(name,directory,mode);

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public synchronized int[] select(int id) throws IOException {

    // Tokenize the ascii list
    List<String> asciiList  = tokenizer.lexWhite( table.select(id) ).getLine(0);
    int[]        binaryList = new int[asciiList.size()-1];

    // Check id
    if ( id != Integer.parseInt(asciiList.get(0)) ) {
      throw new IOException("Table '"+table.getName()+"' is corrupted: the id "+id+" doesnt match the stored id "+asciiList.get(0));
    }

    // Parse each ascii id
    for (int i=0; i<binaryList.length; i++) {
      binaryList[i] = Integer.parseInt(asciiList.get(i+1));
    }

    // Return the binary set
    return binaryList;

  }
  //****************************************************************************
  public synchronized int insert(int[] data) throws IOException {

    // Auxiliar
    int id;

    // Create an id for a new entry in the table
    if ( isEmpty() ) {
      id = table.insert( table.getNextId() + " " );
    }
    else {
      id = table.insert( "\n" + table.getNextId() + " " );
    }

    // Return if there is no data to be written to the entry
    if (data==null) return id;

    // Insert the id list
    append(data);

    // Return the recently created entry id
    return id;

  }
  //****************************************************************************
  public synchronized void append(int data) throws IOException {

    table.append( data + " " );

  }
  //****************************************************************************
  public synchronized void append(int[] data) throws IOException {

    for (int i=0; i<data.length; i++) {
      table.append( data[i] + " " );
    }

  }
  //****************************************************************************
  /* Returns the total size in bytes of the data file
   */
  public int getSizeOfData() {

    return table.getSizeOfData();

  }
  //****************************************************************************
  /* Returns the total size in bytes of the index file
   */
  public int getSizeOfIndex() {

    return table.getSizeOfIndex();

  }
  //****************************************************************************
  public int getNextId() {

    return table.getNextId();

  }
  //****************************************************************************
  public synchronized void close() {

    try {
      table.close();
    }
    catch(Exception ex) {}

  }
  //****************************************************************************


}
