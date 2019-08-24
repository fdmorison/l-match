// Package
///////////////
package com.merak.ontomap.model.extraction.render;

// Imports
///////////////
import java.util.*;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.merak.core.text.*;
import com.merak.ontomap.model.extraction.vocabulary.Vocabulary;

// Public Class
///////////////
public class PropertyRender {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private Vocabulary       vocabulary = null;
  private List<Individual> links      = new ArrayList<Individual>();

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public PropertyRender(Vocabulary vocabulary) {

    // Attribute Initialization
    this.vocabulary = vocabulary;

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private void render(Property property,RDFNode value,RenderTable table,String extraPrefix) {

    // Auxiliar
    Individual individual = null;
    Literal    literal    = null;
    Resource   type       = null;
    String     sName      = null;
    String     sValue     = null;
    String     sType      = null;

    // 3.1. Renderizando nome da propriedade
    sName = vocabulary.format(extraPrefix+Prefix.PROPERTY_NAME,property.getLocalName());

    // 3.2. Renderizando valor da propriedade
    if (value.isResource()) {
      individual = (Individual)value.as(Individual.class);
      type       = individual.getRDFType();
      sValue     = vocabulary.format(extraPrefix+Prefix.PROPERTY_VALUE,individual.getLocalName());
      sType      = vocabulary.format(extraPrefix+Prefix.PROPERTY_TYPE ,type.getLocalName()      );
      links.add(individual);
    }
    else if (value.isLiteral()) {
      literal = (Literal)value.as(Literal.class);
      sValue  = vocabulary.format(extraPrefix+Prefix.PROPERTY_VALUE,literal.getLexicalForm());
      sType   = "";
    }

    // 4. Incluindo tripla Nome,Valor,Tipo na tabela
    Row row = table.createRow();
    row.createCell( sName  ); row.createCell("=");
    row.createCell( sValue ); row.createCell(":");
    row.createCell( sType  );

  }
  //****************************************************************************
  public String render(Individual instance,String extraPrefix) {

    // Auxiliar
    StmtIterator properties = instance.listProperties();
    RenderTable  table      = new RenderTable();
    Statement    st         = null;
    Property     property   = null;
    RDFNode      value      = null;

    // Preparar
    links.clear();

    // Renderizando propriedade por propriedade
    while (properties.hasNext()) {
      // 1. Proxima propriedade
      st       = (Statement) properties.next();
      property = st.getPredicate();
      value    = st.getObject();

      // 2. Ignorando meta-propriedades
      if ( value==null                               ) continue;
      if ( property.getLocalName().equals("type")    ) continue;
      if ( property.getLocalName().equals("comment") ) continue;

      // 3. Renderizando propriedade
      render(property,value,table,extraPrefix);
    }

    // Finalizar
    properties.close();
    return table.toString();

  }
  //****************************************************************************
  public Individual[] getLinks() {

    return links.toArray( new Individual[links.size()] );

  }
  //****************************************************************************


}
