// Package
///////////////
package com.merak.ontomap.model;

// Imports
///////////////
import java.util.*;
import com.merak.core.IdentifiedNamedObject;

public class Category extends IdentifiedNamedObject implements Iterable<Instance> {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private int            ontologicalId = 0;
  private Ontology       ontology      = null;
  private List<Category> parents       = new ArrayList();
  private Category[]     children      = null;
  private Instance[]     instanceSet   = null;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  protected Category(int id,String name) {

    // Super Attributes Initialization
    super(id,name);

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  /* @return as categorias desta ontologia
   */
  private void notifyParent(Category category) {

    parents.add(category);

  }
  //****************************************************************************
  /* @return as categorias desta ontologia
   */
  protected void setOntology(Ontology ontology,int ontologicalId) {

    this.ontology      = ontology;
    this.ontologicalId = ontologicalId;

  }
  //****************************************************************************
  /* @return as categorias desta ontologia
   */
  protected void setPertinence(int[] idList,MapModel model) {

    // Auxiliar
    int innerId;
    int id;

    // Allocate instanceSet
    this.instanceSet = new Instance[idList.length];

    // Fill instanceSet
    for (innerId=0; innerId<idList.length; innerId++) {
      id = idList[innerId];
      this.instanceSet[innerId] = model.getInstance(id);
    }

  }
  //****************************************************************************
  /* @return as categorias desta ontologia
   */
  protected void setChildren(int[] idList,MapModel model) {

    // Auxiliar
    int innerId;
    int id;

    // Allocate children
    this.children = new Category[idList.length];

    // Fill children
    for (innerId=0; innerId<idList.length; innerId++) {
      id = idList[innerId];
      children[innerId] = model.getCategory(id);
      children[innerId].notifyParent(this);
    }

  }
  //****************************************************************************
  /* @return as categorias desta ontologia
   */
  public Ontology getOntology() {

    return ontology;

  }
  //****************************************************************************
  /* @return as categorias desta ontologia
   */
  public int getOntologicalId() {

    return ontologicalId;

  }
  //****************************************************************************
  /* @return as categorias desta ontologia
   */
  public Category getParent(int innerId) {

    return parents.get(innerId);

  }
  //****************************************************************************
  /* @return as categorias desta ontologia
   */
  public Category getParent(String name) {

    for (int i=0; i<parents.size(); i++) {
      if ( parents.get(i).hasName(name) ) {
        return parents.get(i);
      }
    }
    return null;

  }
  //****************************************************************************
  /* @return as categorias desta ontologia
   */
  public List<Category> getParents() {

    return parents;

  }
  //****************************************************************************
  /* @return as categorias desta ontologia
   */
  public Category[] getSiblings() {

    Set<Category> siblings = new HashSet<Category>();
    for ( Category parent : parents ) {
      for ( Category child : parent.getChildren() ) {
         if (child != this) siblings.add(child);
      }
    }
    return siblings.toArray( new Category[ siblings.size() ] );    

  }  
  //****************************************************************************
  /* Returns the categories from whom this category is descended: the parents
   * and the more remote than a parent.
   */
  public Set<Category> getAncestors() {

    return getAncestors(new HashSet<Category>());

  }
  //****************************************************************************
  /* Returns the categories from whom this category is descended: the parents
   * and the more remote than a parent.
   */
  public Set<Category> getAncestors(Set<Category> output) {

    // For each parent
    for (Category parent : parents) {
      output.add(parent);
      parent.getAncestors(output);
    }
    return output;

  }
  //****************************************************************************
  /* @return o conjunto de categorias abstratas desta ontologia, isto e,
   *         categorias que nao possuem instancias.
   */
  public Category getChild(String name) {

    for (int i=0; i<children.length; i++) {
      if ( children[i].hasName(name) ) {
        return children[i];
      }
    }
    return null;

  }
  //****************************************************************************
  /* @return o conjunto de categorias abstratas desta ontologia, isto e,
   *         categorias que nao possuem instancias.
   */
  public Category getChild(int innerId) {

    return children[innerId];

  }
  //****************************************************************************
  /* @return o conjunto de categorias raiz desta ontologia
   */
  public Category[] getChildren() {

    return children;

  }
  //****************************************************************************
  /* @return o conjunto de categorias raiz desta ontologia
   */
  public Set<Category> getDescendents() {

    // Auxiliar
    return getDescendents(new HashSet<Category>());

  }
  //****************************************************************************
  /* @return o conjunto de categorias raiz desta ontologia
   */
  public Set<Category> getDescendents(Set<Category> output) {

    // Add direct children
    for (int i=0; i<children.length; i++) {
      output.add(children[i]);
      children[i].getDescendents(output);
    }
    return output;

  }
  //****************************************************************************
  /* @return o conjunto de categorias abstratas desta ontologia, isto e,
   *         categorias que nao possuem instancias.
   */
  public Instance getDirectInstance(String name) {

    for (int i=0; i<instanceSet.length; i++) {
      if ( instanceSet[i].hasName(name) ) {
        return instanceSet[i];
      }
    }
    return null;

  }
  //****************************************************************************
  /* @return o conjunto de categorias abstratas desta ontologia, isto e,
   *         categorias que nao possuem instancias.
   */
  public Instance getDirectInstance(int innerId) {

    return instanceSet[innerId];

  }
  //****************************************************************************
  /* @return o conjunto de categorias abstratas desta ontologia, isto e,
   *         categorias que nao possuem instancias.
   */
  public Instance[] getDirectInstances() {

    return instanceSet;

  }
  //****************************************************************************
  /* Procura uma classe por nome dentro da ontologia.
   * @return as categorias raiz (root) desta ontologia
   */
  public int getNumberOfParents() {

    return parents.size();

  }
  //****************************************************************************
  /* @return a quantidade de categorias na ontologia
   */
  public int getNumberOfChildren() {

    return children.length;

  }
  //****************************************************************************
  /* @return a quantidade de instancias na ontologia
   */
  public int getNumberOfDirectInstances() {

    return instanceSet.length;

  }
  //****************************************************************************
  /* @return a quantidade de instancias na ontologia
   */
  public int getNumberOfAllInstances() {

    int n = instanceSet.length; 
    for ( Category child : children ) {
        n += child.getNumberOfAllInstances();
    }
    return n;

  }  
  //****************************************************************************
  /* @return a quantidade de instancias na ontologia
   */
  public boolean hasAncestor(Category category) {

    for (Category parent : parents) {
      if (parent==category             ) return true;
      if (parent.hasAncestor(category) ) return true;
    }
    return false;

  }
  //****************************************************************************
  /* @return a quantidade de instancias na ontologia
   */
  public boolean hasDescendent(Category category) {

    for (Category child : children) {
      if (child==category               ) return true;
      if (child.hasDescendent(category) ) return true;
    }
    return false;

  }
  //****************************************************************************
  /* Responde se esta categoria eh uma raiz da taxonomia.
   */
  public boolean isRoot() {

    return parents.isEmpty();

  }
  //****************************************************************************
  /* Responde se esta categoria eh uma raiz da taxonomia.
   */
  public boolean isLeaf() {

    return children.length == 0;

  }
  //****************************************************************************
  /* Responde se esta categoria eh abstrata (se nao tem instancias diretas).
   */
  public boolean isAbstract() {

    return instanceSet.length == 0;

  }
  //****************************************************************************
  /* Responde se esta categoria eh concreta (se tem instancias diretas).
   */
  public boolean isConcrete() {

    return instanceSet.length > 0;

  }
  //****************************************************************************
  /* Cria um iterador sobre a categoria, a qual eh um conjunto de instancias
   */
  public Iterator<Instance> iterator() {

    return Arrays.asList(instanceSet).iterator();

  }
  //****************************************************************************

}
