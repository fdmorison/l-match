// Package
///////////////
package com.merak.core.text;

// Imports
///////////////
import java.util.*;

public class RenderTable {

  //~ Attributes ///////////////////////////////////////////////////////////////
  private   List<Row>     rows            = new LinkedList<Row>();
  private   StringBuilder builder         = new StringBuilder(100);
  protected int           largerRowLength = 0;

  //~ Constructors /////////////////////////////////////////////////////////////
  //~ Methods //////////////////////////////////////////////////////////////////
  /****************************************************************************/
  public String ident(int length) {

    String space = "";
    for (int i=0; i<length; i++) space += " ";
    return space;

  }
  /****************************************************************************/
  public Row createRow() {

    Row row = new Row(this);
    rows.add(row);
    return row;

  }
  /****************************************************************************/
  public String toString() {

    int[] collumLength = new int[largerRowLength];
    Row   row;
    Cell  cell;

    for (int i=0; i<largerRowLength; i++) {
      collumLength[i] = 0;
    }

    for (int i=0; i<rows.size(); i++) {
      row = rows.get(i);
      for (int j=0; j<row.length(); j++) {
        cell = row.getCell(j);
        if (cell.length()>collumLength[j]) collumLength[j] = cell.length();
      }
    }

    builder.setLength(0);
    for (int i=0; i<rows.size(); i++) {
      row = rows.get(i);
      for (int j=0; j<row.length(); j++) {
        cell = row.getCell(j);
        builder.append( cell.getInnerText() );
        builder.append( ident(collumLength[j]-cell.length()) + "  " );
      }
      builder.append('\n');
    }

    return builder.toString();

  }
  /****************************************************************************/

}

