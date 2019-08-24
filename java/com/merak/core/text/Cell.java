// Package
///////////////
package com.merak.core.text;

// Imports
///////////////
import java.util.*;


public class Cell {

  //~ Attributes ///////////////////////////////////////////////////////////////
  private Row    row;
  private String innerText;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  protected Cell(Row row) {

    this.row       = row;
    this.innerText = "";

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  protected Cell(Row row,String innerText) {

    this.row       = row;
    this.innerText = innerText;

  }
  //****************************************************************************
  public String getInnerText() {

    return innerText;

  }
  //****************************************************************************
  public void setInnerText(String value) {

    innerText = value;

  }
  //****************************************************************************
  public int length() {

    return innerText.length();

  }
  //****************************************************************************

}