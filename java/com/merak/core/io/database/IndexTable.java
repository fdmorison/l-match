// Package
///////////////
package com.merak.core.io.database;

// Imports
///////////////
import java.util.*;
import java.io.*;

// Public Class
///////////////
public abstract class IndexTable extends Table {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public IndexTable(String name,File directory,Mode mode) {

    // Super Attribute Initialization
    super(name,directory,mode);

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  /* Loads the content of the entry
   * @param id the entry's unique identifier
   * @return an array of bytes
   */
  public abstract byte[] select(int id) throws IOException;

  //****************************************************************************
  /* Loads the content of the entry as a string of caracteres
   * @param id the entry's unique identifier
   * @return the data as a string
   */
  public final String selectString(int id) throws IOException {

    return new String(select(id));

  }

  //****************************************************************************
  /* Creates a new entry in the table.
   * @param data the content of the entry
   * @return the Id assigned to the recently created entry
   */
  public abstract int insert(byte[] data) throws IOException;

  //****************************************************************************
  /* Creates a new entry in the table.
   * @param data the content of the entry
   * @return the Id assigned to the recently created entry
   */
  public final int insert(String data) throws IOException {

    return insert(data.getBytes());

  }

  //****************************************************************************
  /** Appends data to the end of the data file. Appended data is suposed to be part
   *  of the last created entry's content. If there is no entry in the table, the
   *  appended data will be in the file header.
   */
  public abstract void append(byte[] data) throws IOException;

  //****************************************************************************
  /** Appends a string to the last entry.
   */
  public final void append(String data) throws IOException {

    append(data.getBytes());

  }
  //****************************************************************************

}
