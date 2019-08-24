// Package
///////////////
package com.merak.ontomap.model;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.merak.core.Interval;
import com.merak.core.io.database.Table.Mode;
import com.merak.core.text.TextDocument;
import com.merak.core.text.Tokenizer;

public class MapModel {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  public final Ontology    ontologyA;
  public final Ontology    ontologyB;
  private      ModelStore  store       = null;
  private      Ontology[]  ontologySet = null;
  private      Category[]  categorySet = null;
  private      Instance[]  instanceSet = null;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public MapModel( File pathToTheModel )
    throws IOException
  {

    // 0) Attribute Initialization
    this.store = new ModelStore(pathToTheModel,Mode.Select);

    // 1) Load instance, category and ontology data
    loadInstances(pathToTheModel);
    loadCategories(pathToTheModel);
    loadOntologies(pathToTheModel);

    // 2) Attribute Initialization
    this.ontologyA = ontologySet[0];
    this.ontologyB = ontologySet[1];

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private void loadInstances( File pathToTheModel ) {

    // Auxiliar
    File         file     = new File( pathToTheModel + "/instance.txt" );
    TextDocument document = new Tokenizer().lexWhite(file);
    List<String> line     = null;
    String       name     = null;
    int          id;

    // Preparar container para as instancias
    instanceSet = new Instance[ document.getNumberOfLines()-1 ];

    // Carregar instancias no container
    for (int i=1; i<document.getNumberOfLines(); i++) {
      // Lendo uma linha do arquivo
      line = document.getLine(i);
      // Extrair valores dos atributos
      id   = i-1;
      name = line.get(1);
      // Construir o objeto 'Instance'
      instanceSet[id] = new Instance(id,name);
    }

  }
  //****************************************************************************
  private void loadCategories( File pathToTheModel )
    throws IOException
  {
    // Auxiliar
    TextDocument document = new Tokenizer().lexWhite(new File( pathToTheModel + "/category.txt" ));
    List<String> line     = null;
    String       name;
    int          id;

    // 1) Load Category Descriptors
    categorySet = new Category[ document.getNumberOfLines()-1 ];
    for (int i=1; i<document.getNumberOfLines(); i++) {
      line            = document.getLine(i);
      id              = i-1;
      name            = line.get(1);
      categorySet[id] = new Category(id,name);
    }

    // 2) Load and set the pertinence(instance,category) relations
    for (id=0; id<categorySet.length; id++) {
      categorySet[id].setPertinence( store.selectInstances(id) , this );
    }

    // 3) Load and set the taxonomy(category,category) relations, also called "is-a" relations
    for (id=0; id<categorySet.length; id++) {
      categorySet[id].setChildren( store.selectChildren(id) , this );
    }

  }
  //****************************************************************************
  private void loadOntologies( File pathToTheModel ) {

    // Auxiliar
    File         file             = new File( pathToTheModel + "/ontology.txt" );
    TextDocument document         = new Tokenizer().lexWhite(file);
    List<String> line             = null;
    String       name             = null;
    Interval     instanceInterval = null;
    Interval     categoryInterval = null;
    int          id;

    // Preparar container para as ontologias
    ontologySet = new Ontology[ document.getNumberOfLines()-1 ];

    // Carregar ontologias no container
    for (int i=1; i<document.getNumberOfLines(); i++) {
      // Lendo uma linha do arquivo
      line = document.getLine(i);
      // Extrair valores dos atributos basicos
      id   = i-1;
      name = line.get(1);
      // Extrair valores dos intervalos
      categoryInterval = new Interval( line.get(2) );
      instanceInterval = new Interval( line.get(3) );
      // Construir o objeto 'Instance'
      ontologySet[id]  = new Ontology(id,name,categoryInterval,instanceInterval,this);
    }

  }
  //****************************************************************************
  public ModelStore getStore() {

    return store;

  }
  //****************************************************************************
  public File getPath() {

    return store.getPath();

  }
  //****************************************************************************
  public Ontology getOntology( int id ) {

    return ontologySet[id];

  }
  //****************************************************************************
  public Category getCategory( int id ) {

    return categorySet[id];

  }
  //****************************************************************************
  public String getTextOf( Instance instance ) throws IOException {

    return store.selectText(instance.getId());

  }
  //****************************************************************************
  public Instance getInstance( int id ) {

    return instanceSet[id];

  }
  //****************************************************************************
  public int getNumberOfCategories() {

    return categorySet.length;

  }
  //****************************************************************************
  public int getNumberOfInstances() {

    return instanceSet.length;

  }
  //****************************************************************************

}
