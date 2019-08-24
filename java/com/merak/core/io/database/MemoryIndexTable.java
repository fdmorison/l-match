// Package
///////////////
package com.merak.core.io.database;

// Imports
///////////////
import java.util.*;
import java.io.*;

// Public Class
///////////////
public class MemoryIndexTable extends IndexTable {

  //~ Constants ////////////////////////////////////////////////////////////////
  //****************************************************************************
  public static int INDEX_SIZE = 4;

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private ArrayList<ByteArrayOutputStream> table      = null;
  private int                              sizeOfData = 0;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public MemoryIndexTable() {

    this("Unnamed",null,Mode.InsertSelect,0);

  }
  //****************************************************************************
  public MemoryIndexTable(String name,File directory,Mode mode,int capacity) {

    // Super Attribute Initialization
    super(name,directory,mode);

    // Attribute Initialization
    this.table = new ArrayList(capacity);

  }
  //****************************************************************************
  public MemoryIndexTable(IndexTable dataSource) throws IOException {

    // Super Attribute Initialization
    super(dataSource.getName(),dataSource.getDirectory(),dataSource.getMode());

    // Auxiliar
    ByteArrayOutputStream entry    = null;
    byte[]                content  = null;
    int                   capacity = dataSource.length();

    // Attribute Initialization
    this.table      = new ArrayList(capacity);
    this.sizeOfData = dataSource.getSizeOfData();

    // Migrate data from source to here
    for (int id=0; id<capacity; id++) {
      content = dataSource.select(id);
      entry   = new ByteArrayOutputStream(content.length);
      entry.write(content);
      table.add(entry);
    }

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public synchronized byte[] select(int id) throws IOException {

    // Check possible out of bound id
    if (id>=table.size()) return null;

    // Return the data
    return table.get(id).toByteArray();

  }
  //****************************************************************************
  public synchronized int insert(byte[] data) throws IOException {

    // Write data
    ByteArrayOutputStream entry = new ByteArrayOutputStream(data.length);
    entry.write(data);
    table.add(entry);

    // Update attributes
    sizeOfData += data.length;

    // Return the Id
    return table.size()-1;

  }
  //****************************************************************************
  public synchronized void append(byte[] data) throws IOException {

    // Auxiliar
    ByteArrayOutputStream entry = null;

    // Initialize the output
    if ( table.isEmpty() ) {
      entry = new ByteArrayOutputStream();
      table.add(entry);
    }
    else {
      entry = table.get( table.size()-1 );
    }

    // Append the data
    entry.write(data);

  }
  //****************************************************************************
  public synchronized int getNextId() {

    return table.size();

  }
  //****************************************************************************
  public int getSizeOfIndex() {

    return INDEX_SIZE * table.size();

  }
  //****************************************************************************
  public int getSizeOfData() {

    return sizeOfData;

  }
  //****************************************************************************
  public synchronized void close() {

    table.clear();

  }
  //****************************************************************************


}
