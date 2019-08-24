// Package
///////////////
package com.merak.ontomap.model;

// Imports
///////////////
import java.util.*;
import java.io.File;
import com.merak.core.Interval;
import com.merak.core.IdentifiedNamedObject;

public class Ontology extends IdentifiedNamedObject implements Iterable<Category> {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private MapModel             model;
  private File                 path;
  private Category[]           rootSet;
  private Category[]           categorySet;
  private Category[]           abstractSet;
  private Category[]           concreteSet;
  private Map<String,Category> categoryNameMap;
  private Map                  parameterSet;
  private Interval             instanceInterval;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  protected Ontology(int id,String name,Interval categoryInterval,Interval instanceInterval,MapModel model) {

    // Super Attributes Initialization
    super(id,name);

    // Auxiliar
    List<Category> rootList       = new LinkedList<Category>();
    List<Category> abstractList   = new LinkedList<Category>();
    List<Category> concreteList   = new LinkedList<Category>();
    Category       category       = null;
    int            categoryId     = categoryInterval.offset;
    int            categoryOntoId = 0;

    // Attribute Initialization
    this.model            = model;
    this.path             = new File(model.getPath()+"/ontology"+getId());
    this.categorySet      = new Category[ categoryInterval.length()+1 ];
    this.categoryNameMap  = new HashMap( (int)(1.5 * categorySet.length) );
    this.parameterSet     = new HashMap();
    this.instanceInterval = instanceInterval;

    // Organizando as classes
    while (categoryOntoId<categorySet.length) {
      // Proxima categoria
      category = categorySet[categoryOntoId] = model.getCategory(categoryId++);
      category.setOntology(this,categoryOntoId++);
      categoryNameMap.put(category.getName(),category);

      // Selecionando classes que sao root
      if (category.isRoot()) rootList.add(category);

      // Selecionando classes que sao abstratas/concretas
      if (category.isAbstract()) abstractList.add(category);
      else                       concreteList.add(category);
    }

    // Finalizando
    rootSet     = rootList.toArray( new Category[rootList.size()]         );
    abstractSet = abstractList.toArray( new Category[abstractList.size()] );
    concreteSet = concreteList.toArray( new Category[concreteList.size()] );

    // Set default parameters
    parameterSet.put("rainbowInput",getPath()+"/rainbow_input");
    parameterSet.put("rainbowModel",getPath()+"/rainbow_model");

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public File getPath() {

    return path;

  }
  //****************************************************************************
  public MapModel getModel() {

    return model;

  }
  //****************************************************************************
  public Ontology getPair() {

    return hasId(0) ? model.getOntology(1) : model.getOntology(0);

  }  
  //****************************************************************************
  /* @return uma categoria dado o seu id ontologico, isto e, o id da categoria
   *         dentro da ontologia, o qual eh diferente do id global da categoria.
   */
  public Category getCategory(int ontologicalId) {

    return categorySet[ontologicalId];

  }
  //****************************************************************************
  /* Procura uma classe por nome dentro da ontologia.
   */
  public Category getCategory(String name) {

    return categoryNameMap.get(name);

  }
  //****************************************************************************
  /* @return as categorias desta ontologia
   */
  public Category[] getCategories() {

    return categorySet;

  }
  //****************************************************************************
  /* @return o conjunto de categorias raiz desta ontologia
   */
  public Category[] getRootCategories() {

    return rootSet;

  }
  //****************************************************************************
  /* @return o conjunto de categorias abstratas desta ontologia, isto e,
   *         categorias que nao possuem instancias.
   */
  public Category[] getAbstractCategories() {

    return abstractSet;

  }
  //****************************************************************************
  /* @return o conjunto de categorias concretas desta ontologia, isto e, categorias instanciadas.
   */
  public Category[] getConcreteCategories() {

    return concreteSet;

  }
  //****************************************************************************
  /* @return a quantidade de categorias na ontologia
   */
  public int getNumberOfCategories() {

    return categorySet.length;

  }
  //****************************************************************************
  /* @return a quantidade de categorias instanciadas na ontologia
   */
  public int getNumberOfRootCategories() {

    return rootSet.length;

  }
  //****************************************************************************
  /* @return a quantidade de categorias instanciadas na ontologia
   */
  public int getNumberOfConcreteCategories() {

    return concreteSet.length;

  }
  //****************************************************************************
  /* @return a quantidade de categorias nao instanciadas na ontologia
   */
  public int getNumberOfAbstractCategories() {

    return abstractSet.length;

  }
  //****************************************************************************
  /* @return a quantidade de instancias na ontologia
   */
  public int getNumberOfInstances() {

    return instanceInterval.length()+1;

  }
  //****************************************************************************
  /* @return a quantidade de instancias na ontologia
   */
  public String getParameter(String name) {

    return (String) parameterSet.get(name);

  }
  //****************************************************************************
  /* @return a quantidade de instancias na ontologia
   */
  public boolean contains(Category category) {

    //if ( category == null ) return false;
    return category.getOntology() == this;

  }
  //****************************************************************************
  /* Cria um iterador sobre a ontologia, que antes de tudo eh um conjunto de categorias
   */
  public Iterator<Category> iterator() {

    return Arrays.asList(categorySet).iterator();

  }
  //****************************************************************************

}
