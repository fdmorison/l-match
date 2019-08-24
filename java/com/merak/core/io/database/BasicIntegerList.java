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
public class BasicIntegerList extends Table {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final IndexTable table;
  private final Tokenizer  tokenizer = new Tokenizer();

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public BasicIntegerList(String name,File directory) throws IOException {

    this(name,directory,IndexTable.Mode.InsertSelect);

  }
  //****************************************************************************
  public BasicIntegerList(String name,File directory,IndexTable.Mode mode) throws IOException {

    // Super Attribute Initialization
    super(name,directory,mode);

    // Attribute Initialization
    this.table = new BasicIndexTable(name,directory,mode);

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  /* Append an array of 32bit integer to the last entry.
   */
  private final byte[] toByteArray(int[] intArray) {
    /*
        0        1        2       3
    -------- -------- -------- --------
    00000000 00000000 00000000 00000000
    -------- -------- -------- --------
    01234567 89012345 67890123 45678901
    */
    byte[] byteArray = new byte[ 4*intArray.length ];
    int    integer;

    for (int i=0,j=0; i<intArray.length; i++,j+=4) {
      integer        = intArray[i];
      byteArray[j+3] = (byte)(integer);       // aaaaaaaa bbbbbbbb cccccccc xxxxxxxx (>>8)
      byteArray[j+2] = (byte)(integer >>= 8); //          aaaaaaaa bbbbbbbb xxxxxxxx (>>8)
      byteArray[j+1] = (byte)(integer >>= 8); //                   aaaaaaaa xxxxxxxx (>>8)
      byteArray[j  ] = (byte)(integer >>  8); //                            xxxxxxxx
    }

    return byteArray;

  }
  //****************************************************************************
  /* Append an array of 32bit integer to the last entry.
   */
  private final int[] toIntArray(byte[] byteArray) {

    int[] intArray = new int[ byteArray.length/4 ];
    int   integer;

    for (int i=0,j=0; i<intArray.length; i++,j+=4) {
      integer     = byteArray[j];
      integer     = (integer<<8) | byteArray[j+1];
      integer     = (integer<<8) | byteArray[j+2];
      integer     = (integer<<8) | byteArray[j+3];
      intArray[i] = integer;
    }

    return intArray;

  }
  //****************************************************************************
  public synchronized int[] select(int id) throws IOException {

    return toIntArray( table.select(id) );

  }
  //****************************************************************************
  public synchronized int insert(int[] data) throws IOException {

    return table.insert( toByteArray(data) );

  }
  //****************************************************************************
  public synchronized void append(int data) throws IOException {

    table.append(data);

  }
  //****************************************************************************
  public synchronized void append(int[] data) throws IOException {

    table.append( toByteArray(data) );

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
