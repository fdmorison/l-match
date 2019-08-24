// Package
///////////////
package com.merak.ontomap.model;

// Imports
///////////////
import java.util.*;
import java.io.*;
import com.merak.core.*;
import com.merak.core.io.database.*;
import com.merak.ontomap.model.extraction.*;

// Public Class
///////////////
public class ModelStore {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //**************************************************************************
  public final File             path;
  public final AsciiIndexTable  ontologyTable;
  public final AsciiIndexTable  categoryTable;
  public final AsciiIndexTable  instanceTable;
  public final AsciiIntegerList taxonomyTable;
  public final AsciiIntegerList pertinenceTable;
  public final BasicIndexTable  textTable;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public ModelStore(File path,Table.Mode mode) throws IOException {

    // Attribute Initialization
    this.path            = path;
    this.taxonomyTable   = new AsciiIntegerList("taxonomy",path,mode);    //"#categoryId, childrenIdList");
    this.pertinenceTable = new AsciiIntegerList("pertinence",path,mode);  //"#categoryId, instanceIdList");
    this.textTable       = new BasicIndexTable("text",path,mode);

    if (mode == Table.Mode.Insert) {
      this.ontologyTable = new AsciiIndexTable(path+"/ontology","#id, name, categoryIdInterval, instanceIdInterval");
      this.categoryTable = new AsciiIndexTable(path+"/category","#id, name");
      this.instanceTable = new AsciiIndexTable(path+"/instance","#id, name");
    }
    else {
      this.ontologyTable = null;
      this.categoryTable = null;
      this.instanceTable = null;
    }

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public File getPath() {

    return path;

  }
  //****************************************************************************
  public NewOntology createOntology(String name) throws IOException {

    int id = ontologyTable.createEntry(name);
    return new NewOntology(id,name,this);

  }
  //****************************************************************************
  public NewOntology createOntology(String name,Interval categories,Interval instances) throws IOException {

    int id = ontologyTable.createEntry(name+" "+categories+" "+instances);
    return new NewOntology(id,name,this);

  }
  //****************************************************************************
  public int createCategory(String name) throws IOException {

    return categoryTable.createEntry(name);

  }
  //****************************************************************************
  public int createInstance(String name,String text) throws IOException {

    // Save descriptor
    int id = instanceTable.createEntry(name);

    // Save text
    textTable.insert(text);

    // Construct the saved object
    return id;

  }
  //****************************************************************************
  public void createChildrenList(NewCategory category) throws IOException {

    // Auxiliar
    List<NewCategory> children = category.getChildren();
    NewCategory       child    = null;
    int               id       = taxonomyTable.insert(null);

    // Check id
    if ( id != category.getId() ) throw new IOException("Os ids da categoria e da lista nao sao iguais");

    // Save list
    while (!children.isEmpty()) {
      child = children.remove(0);
      taxonomyTable.append(child.getId());
    }

  }
  //****************************************************************************
  public int[] selectChildren(int categoryId) throws IOException {

    return taxonomyTable.select(categoryId);

  }
  //****************************************************************************
  public int[] selectInstances(int categoryId) throws IOException {

    return pertinenceTable.select(categoryId);

  }
  //****************************************************************************
  public String selectText(int instanceId) throws IOException {

    return textTable.selectString(instanceId);

  }
  //****************************************************************************
  public void close() {

    try {
      ontologyTable.close();
      instanceTable.close();
      categoryTable.close();
      taxonomyTable.close();
      pertinenceTable.close();
      textTable.close();
    }
    catch(Exception ex) {}

  }
  //****************************************************************************


}
