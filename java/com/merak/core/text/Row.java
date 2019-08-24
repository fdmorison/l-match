// Package
///////////////
package com.merak.core.text;

// Imports
///////////////
import java.util.*;

public class Row {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private RenderTable table = null;
  private List<Cell>  cells = new LinkedList();

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  protected Row(RenderTable table) {

    this.table = table;

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public Cell createCell() {

  	// Criando a c�lula
    Cell cell = new Cell(this);
    cells.add(cell);

    // Atualizando tabela com o tamanho da linha
    if (table.largerRowLength<cells.size()) table.largerRowLength = cells.size();

    // Retornando a c�lula
    return cell;

  }
  //****************************************************************************
  public Cell createCell(String innerText) {

  	// Criando a c�lula
    Cell cell = new Cell(this,innerText);
    cells.add(cell);

    // Atualizando tabela com o tamanho da linha
    if (table.largerRowLength<cells.size()) table.largerRowLength = cells.size();

    // Retornando a c�lula
    return cell;

  }
  //****************************************************************************
  public Cell createCell(double n) {

    return createCell( String.valueOf(n) ); 

  }  
  //****************************************************************************
  public Cell createCell(float n) {

    return createCell( String.valueOf(n) );

  }    
  //****************************************************************************
  public Cell createCell(int n) {

    return createCell( String.valueOf(n) ); 

  }   
  //****************************************************************************
  public Cell createCell(long n) {

    return createCell( String.valueOf(n) );

  }   
  //****************************************************************************
  public Cell createCell(boolean n) {

    return createCell( String.valueOf(n) );

  } 
  //****************************************************************************
  public Cell createCell(char n) {

    return createCell( String.valueOf(n) );

  }   
  //****************************************************************************
  public Cell createCell(Object object) {

    return createCell( object.toString() ); 

  }    
  //****************************************************************************
  public Cell getCell(int i) {

    return cells.get(i);

  }
  //****************************************************************************
  public int length() {

    return cells.size();

  }
  //****************************************************************************

}
