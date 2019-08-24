// Package
///////////////
package com.merak.core.text;

// Imports
///////////////
import java.util.*;

public class TextDocument implements Iterable<List<String>> {

  //~ Attributes ///////////////////////////////////////////////////////////////
  private ArrayList<List<String>> lines = new ArrayList<List<String>>();

  //~ Cosntructors /////////////////////////////////////////////////////////////
  /****************************************************************************/
  public TextDocument() {

  	//...

  }
  /****************************************************************************/
  public TextDocument(int capacity) {

  	lines.ensureCapacity(capacity);

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  /****************************************************************************/
  public List<String> createLine() {

    List<String> line = new LinkedList<String>();
    lines.add( line );
    return line;

  }
  /****************************************************************************/
  public List<String> getLine(int index) {

  	return lines.get(index);

  }
  /****************************************************************************/
  public List<String> removeLine(int index) {

  	return lines.remove(index);

  }
  /****************************************************************************/
  public boolean isEmpty() {

  	return lines.isEmpty();

  }
  /****************************************************************************/
  public int getNumberOfLines() {

  	return lines.size();

  }
  /****************************************************************************/
  public Iterator<List<String>> iterator() {

    return lines.iterator();

  }
  /****************************************************************************/


}
