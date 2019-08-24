// Package
///////////////
package com.merak.core.io.database;

// Imports
///////////////
import java.util.*;
import java.io.*;

// Public Class
///////////////
public abstract class Table {

  //~ Inner Classes ////////////////////////////////////////////////////////////
  //****************************************************************************
  public enum Mode { Insert, Select, Update, InsertSelect, All }

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final Mode   mode;
  private final String name;
  private final File   directory;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public Table(String name,File directory,Mode mode) {

    // Attribute Initialization
    this.mode      = mode;
    this.name      = name;
    this.directory = directory;

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  /* Returns the access mode used when opening the table
   */
  public final Mode getMode() {

    return mode;

  }
  //****************************************************************************
  /* Returns the table's name
   */
  public final String getName() {

    return name;

  }
  //****************************************************************************
  /* Returns the directory containing the table or null if it is not the case.
   */
  public final File getDirectory() {

    return directory;

  }
  //****************************************************************************
  /* Returns the total size in bytes of the data file
   */
  public abstract int getSizeOfData();

  //****************************************************************************
  /* Returns the total size in bytes of the index file
   */
  public abstract int getSizeOfIndex();

  //****************************************************************************
  /* Returns the ID to be assigned to the next created entry
   */
  public abstract int getNextId();

  //****************************************************************************
  /* Returns if this table was opened with select permision
   */
  public final boolean hasSelectPermition() {

    switch(mode) {
      case Select       : return true;
      case InsertSelect : return true;
      case All          : return true;
    }
    return false;

  }
  //****************************************************************************
  /* Returns if this table was opened with insert permision
   */
  public final boolean hasInsertPermition() {

    switch(mode) {
      case Insert       : return true;
      case InsertSelect : return true;
      case All          : return true;
    }
    return false;

  }
  //****************************************************************************
  /* Returns if this table was opened with update permision
   */
  public final boolean hasUpdatePermition() {

    switch(mode) {
      case Update : return true;
      case All    : return true;
    }
    return false;

  }
  //****************************************************************************
  /* Returns if the table has no entries yet. If the table has a header but no
   * entries, it is considered empty anyway.
   */
  public final boolean isEmpty() {

    return getNextId()==0;

  }
  //****************************************************************************
  /* Returns the number of entries in the table
   */
  public final int length() {

    return getNextId();

  }
  //****************************************************************************
  /* Releases any resources that have been open, as files and conections.
   */
  public abstract void close();

  //****************************************************************************

}
