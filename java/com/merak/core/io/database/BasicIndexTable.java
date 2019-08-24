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
public class BasicIndexTable extends IndexTable {

  //~ Constants ////////////////////////////////////////////////////////////////
  //****************************************************************************
  public static int INDEX_SIZE = 4;

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private RandomAccessFile file_dat    = null;
  private RandomAccessFile file_idx    = null;
  private int              id_sequence = 0;
  private int              sizeOfData  = 0;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public BasicIndexTable(String name,File directory) throws IOException {

    this(name,directory,Mode.InsertSelect);

  }
  //****************************************************************************
  public BasicIndexTable(String name,File directory,Mode mode) throws IOException {

    // Super Attribute Initialization
    super(name,directory,mode);

    // Auxiliar
    File    path_dat = new File(directory + "/" + name + ".dat");
    File    path_idx = new File(directory + "/" + name + ".idx");
    String  openMode;
    boolean truncate;
    boolean mustExist;

    // Processando parametro modo
    switch (mode) {
      case Insert      : openMode="rw"; truncate=true ; mustExist=false; break;
      case Select      : openMode="r" ; truncate=false; mustExist=true ; break;
      case InsertSelect: openMode="rw"; truncate=false; mustExist=false; break;
      default          : throw new IOException("Modo '"+mode+"' nao suportado.");
    }

    // Verificando se a tabela existe
    if (mustExist) {
      if (!path_dat.isFile()) throw new IOException("Arquivo "+path_dat+" nao existe");
      if (!path_idx.isFile()) throw new IOException("Arquivo "+path_idx+" nao existe");
    }

    // Abrindo a tabela
    this.file_dat = new RandomAccessFile(path_dat,openMode);
    this.file_idx = new RandomAccessFile(path_idx,openMode);

    // Esvaziando a tabela
    if (truncate) {
      this.file_dat.setLength(0);
      this.file_idx.setLength(0);
    }

    // Attribute Initialization
    this.sizeOfData  = (int) file_dat.length();
    this.id_sequence = (int) file_idx.length() / INDEX_SIZE;

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private int seekToEntry(int id) throws IOException {

    // Seek and Read from Index
    file_idx.seek(id * 4);
    int offset = file_idx.readInt();
    int endset = ((id+1)==id_sequence) ? sizeOfData: file_idx.readInt();

    // Seek data
    file_dat.seek(offset);

    // Return the length of the entry
    return endset - offset;

  }
  //****************************************************************************
  public synchronized byte[] select(int id) throws IOException {

    if (!hasSelectPermition()) throw new IOException("No permission for selecting");

    // Check possible out of bound id
    if (id>=id_sequence) return null;

    // Auxiliar
    int    length = seekToEntry(id);
    byte[] buffer = new byte[length];

    // Read data file to recover the entry
    file_dat.readFully(buffer,0,length);

    // Return the data
    return buffer;

  }
  //****************************************************************************
  public synchronized int insert(byte[] data) throws IOException {

    if (!hasInsertPermition()) throw new IOException("No permission for inserting");

    // Write index
    file_idx.seek(getSizeOfIndex());
    file_idx.writeInt(getSizeOfData());

    // Write data
    if (data!=null && data.length>0) {
      append(data);
    }

    // Return the Id
    return id_sequence++;

  }
  //****************************************************************************
  public synchronized void append(byte[] data) throws IOException {

    if (!hasInsertPermition()) throw new IOException("No permission for appending");

    // Appending
    file_dat.seek(getSizeOfData());   // Move in file
    file_dat.write(data);             // Write the data

    // Updating attributes
    sizeOfData += data.length;

  }
  //****************************************************************************
  public int getNextId() {

    return id_sequence;

  }
  //****************************************************************************
  public int getSizeOfIndex() {

    return INDEX_SIZE * id_sequence;

  }
  //****************************************************************************
  public int getSizeOfData() {

    return sizeOfData;

  }
  //****************************************************************************
  public synchronized void close() {

    try {
      file_dat.close();
      file_idx.close();
    }
    catch(Exception ex) {
      MsgLogger.print("BasicIndexTable.close()",ex);
    }

  }
  //****************************************************************************


}
