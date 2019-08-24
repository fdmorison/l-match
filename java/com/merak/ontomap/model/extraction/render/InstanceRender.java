// Package
///////////////
package com.merak.ontomap.model.extraction.render;

// Imports
///////////////
import java.util.*;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.merak.core.text.*;
import com.merak.ontomap.model.extraction.*;
import com.merak.ontomap.model.extraction.vocabulary.Vocabulary;

// Public Class
///////////////
public class InstanceRender {

  //~ Inner Classes ////////////////////////////////////////////////////////////
  //****************************************************************************
  private static class XProperty {
    public Property name;
    public RDFNode  value;
  }

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private Vocabulary     vocabulary     = null;
  private CategoryRender categoryRender = null;
  private PropertyRender propertyRender = null;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public InstanceRender(Vocabulary vocabulary) {

    // Attribute Initialization
    this.vocabulary     = vocabulary;
    this.categoryRender = new CategoryRender(vocabulary);
    this.propertyRender = new PropertyRender(vocabulary);

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private String renderContent(Individual instance,String extraPrefix) {

    // Auxiliar
    String           text;
    List<Individual> links;

    // Escrever classes da instancia
    text = categoryRender.render(instance,extraPrefix);

    // Escrever o nome da instancia
    text += vocabulary.format( extraPrefix+Prefix.INSTANCE_NAME, instance.getLocalName() ) + "\n\n";

    // Escrever as propriedades da instancia
    text += propertyRender.render(instance,extraPrefix);

    // Retornar texto
    return text;

  }
  //****************************************************************************
  public String render(Individual instance) {

    return renderContent(instance,"");

  }
  //****************************************************************************
  public void setMapTable(Map<String,MapList> table) {

    categoryRender.setMapTable(table);

  }
  //****************************************************************************

}
