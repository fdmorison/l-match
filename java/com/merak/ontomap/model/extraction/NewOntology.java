// Package
///////////////
package com.merak.ontomap.model.extraction;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.Individual;
import com.merak.core.*;
import com.merak.ontomap.model.ModelStore;

// Public Class
///////////////
public class NewOntology extends IdentifiedNamedObject {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private File                    path          = null;
  private Map<String,NewInstance> instanceMap   = new HashMap<String,NewInstance>();
  private Map<String,NewCategory> categoryMap   = new HashMap<String,NewCategory>();
  private ModelStore              store         = null;
  private File                    rainbow_input = null;
  private File                    rainbow_model = null;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public NewOntology(int id,String name,ModelStore store) {

    // Super Attribute Initialization
    super(id,name);

    // Atribute Initialization
    this.store         = store;
    this.path          = new File(store.getPath() + "/ontology" + id);
    this.rainbow_input = new File(path + "/rainbow_input");
    this.rainbow_model = new File(path + "/rainbow_model");

    // Clear previous Rainbow Input Directory
    try {
      Runtime.getRuntime().exec("rm -r "+path+"/rainbow_input").waitFor();
    }
    catch (Exception ex) {
      Application.debug.print("NewOntology(int,String,ModelStore)",ex.getMessage());
    }

    // Create directories
    this.path.mkdir();
    this.rainbow_input.mkdir();
    this.rainbow_model.mkdir();

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public File getPath() {

    return path;

  }
  //****************************************************************************
  public ModelStore getStore() {

    return store;

  }
  //****************************************************************************
  public File getRainbowInput() {

    return rainbow_input;

  }
  //****************************************************************************
  public File getRainbowModel() {

    return rainbow_model;

  }
  //****************************************************************************
  public NewInstance getInstance(String name) throws IOException {

    return instanceMap.get(name);

  }
  //****************************************************************************
  public NewInstance createInstance(String name,String text) throws IOException {

    int         id          = store.createInstance(name,text);
    NewInstance newInstance = new NewInstance(id,name);
    instanceMap.put(name,newInstance);
    return newInstance;

  }
  //****************************************************************************
  public NewCategory notifyCategory(OntClass oldCategory) throws IOException {

    // Procurando a categoria
    String      name     = oldCategory.getLocalName();
    NewCategory category = categoryMap.get(name);

    // Se a categoria nao foi criada ainda, entao
    if ( category == null ) {
      int id   = store.createCategory(name);
      category = new NewCategory(id,name,oldCategory,this);
      categoryMap.put(name,category);
    }

    // Retorne a categoria
    return category;

  }
  //****************************************************************************

}
