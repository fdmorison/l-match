// Package
///////////////
package com.merak.ontomap.model;

// Imports
///////////////
import java.util.*;
import com.merak.ai.classification.Rank;

// Class
///////////////
public class CategoryRank extends Rank<Category,Category> {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public CategoryRank(int capacity) {

    // Super Attributes Initialization
    super(capacity);

  }
  //****************************************************************************
  public CategoryRank(Category category,int capacity) {

    // Super Attributes Initialization
    super(category,capacity);

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public Ontology getOntology() {

    return getQuery().getOntology();

  }
  //****************************************************************************
  public boolean hasSomeAncestorOf(Category category) {

    // Procure um ancestal de category no rank
    for (Pair entry : this) {
      if ( category.hasAncestor(entry.entity) ) return true;
    }
    return false;

  }
  //****************************************************************************
  public boolean hasSomeDescendentOf(Category category) {

    // Procure um ancestal de category no rank
    for (Pair entry : this) {
      if ( entry.entity.hasAncestor(category) ) return true;
    }
    return false;

  }
  //****************************************************************************
  public List<Category> selectLessGeneral() {

    // Auxiliar
    List<Category> selection = new ArrayList<Category>( length() );

    // SELECIONE CATEGORIAS DO RANK SEM DESCENDENTES APARECENDO NO RANK
    // Para cada categoria do rank,
    for (Pair entry : this) {
      // Se o rank nao tem categoria menos generica que category, selecione category
      if ( !hasSomeDescendentOf(entry.entity) ) selection.add(entry.entity);
    }
    return selection;

  }
  //****************************************************************************
  public void keepMoreGeneral() {

    // Auxiliar
    List<Pair> selection = new ArrayList<Pair>( length() );

    // SELECIONE CATEGORIAS DO RANK SEM ANCESTRAIS APARECENDO NO RANK
    // Para cada categoria do rank,
    for (Pair entry : this) {
      // Se o rank nao tem categoria mais generica que category, selecione category
      if ( !hasSomeAncestorOf(entry.entity) ) selection.add(entry);
    }

    // Matenha no rank apenas as classes selecionadas
    clear();
    for (Pair entry : selection) {
      createEntry( entry.entity, entry.priority );
    }

  }
  //****************************************************************************

}
