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
public class CategoryRender {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private Vocabulary          vocabulary    = null;
  private StringBuilder       buffer        = new StringBuilder();
  private List<OntClass>      directClasses = new ArrayList<OntClass>();
  private List<OntClass>      superClasses  = new ArrayList<OntClass>();
  private Map<String,MapList> mapTable      = null;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public CategoryRender(Vocabulary vocabulary) {

    this.vocabulary = vocabulary;

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public void setMapTable(Map<String,MapList> table) {

    mapTable = table;

  }  
  //****************************************************************************
  private boolean validate(OntClass concept) {

    if (concept.isUnionClass()) {
      System.out.println(" Concept '"+concept.getLocalName()+"' has super UnionClass");
    }
    else if (concept.isIntersectionClass()) {
      System.out.println(" Concept '"+concept.getLocalName()+"' has super IntersectionClass");
    }
    else if (concept.isComplementClass()) {
      System.out.println(" Concept '"+concept.getLocalName()+"' has super ComplementClass");
    }
    else if (concept.isRestriction()) {
      // esse caso ja eh coberto por renderProperties
    }
    else if (concept.isEnumeratedClass()) {
      System.out.println(" Concept '"+concept.getLocalName()+"' has super EnumeratedClass");
    }
    else if (concept.isAnon()) {
      System.out.println(" Concept '"+concept.getLocalName()+"' has AnonimousClass");
    }
    else {
      return true;
    }
    return false;

  }
  //****************************************************************************
  private void selectSuperClasses(OntClass concept) {

    // Auxiliar
    List<OntClass> queue = new LinkedList<OntClass>();
    Iterator       iter  =  concept.listSuperClasses();

    // Prepare the control queue with concept's parents
    while ( iter.hasNext() ) {
      queue.add((OntClass)iter.next());
    }

    // Select ancestrors
    while (!queue.isEmpty()) {
      // Add parents of next concept to control queue
      concept = queue.remove(0);
      iter    =  concept.listSuperClasses();
      while ( iter.hasNext() ) {
        queue.add((OntClass)iter.next());
      }
      // Add concept to super class list
      if (validate(concept)) superClasses.add(0,concept);
    }

  }
  //****************************************************************************
  public String render(Individual instance,String extraPrefix) {

    // Auxiliar
    StmtIterator properties = instance.listProperties();
    Statement    st         = null;
    Property     property   = null;
    RDFNode      value      = null;
    OntClass     concept    = null;

    // Renderizando super classes
    buffer.setLength(0);
    while (properties.hasNext()) {
      // 1. Proxima propriedade
      st       = (Statement) properties.next();
      property = st.getPredicate();
      value    = st.getObject();

      // 2. Ignorar propriedades que nao sao tipo
      if (  value==null                               ) continue;
      if ( !property.getLocalName().equals("type")    ) continue;

      // 3. Renderizando propriedade
      concept = (OntClass) value.as( OntClass.class );
      selectSuperClasses(concept);
      directClasses.add(concept);
    }
    properties.close();

    // Renderizar super classes
    for (OntClass superClass: superClasses) {
      //buffer.append( vocabulary.format(extraPrefix+Prefix.SUPER_CLASS,superClass.getLocalName()) + " "  );
      buffer.append( vocabulary.format(extraPrefix+Prefix.CLASS_NAME ,superClass.getLocalName()) + "\n" );
    }
    buffer.append("\n");
    
    // Renderizar super classes dadas por mapeamento inicial
    MapList mapList;
    
    for (OntClass directClass : directClasses) {
      mapList = mapTable.get(directClass.getLocalName());
      if (mapList==null) continue;
      for (String moreGeneral : mapList.getMoreGeneral()) {
      	buffer.append( vocabulary.format(extraPrefix+Prefix.CLASS_NAME,moreGeneral) + "\n" );
      }      
    }   
     
    // Renderizar classes diretas
    for (OntClass directClass: directClasses) {
      buffer.append( vocabulary.format(extraPrefix+Prefix.DIRECT_CLASS,directClass.getLocalName()) + " "  );
      buffer.append( vocabulary.format(extraPrefix+Prefix.CLASS_NAME  ,directClass.getLocalName()) + "\n" );
      /*
      mapList = mapTable.get(directClass.getLocalName());
      if (mapList==null) continue;
      for (String equivalent : mapList.getEquivalent()) {
      	buffer.append( vocabulary.format(extraPrefix+Prefix.DIRECT_CLASS,equivalent) + "\n" );
      	buffer.append( vocabulary.format(extraPrefix+Prefix.CLASS_NAME  ,equivalent) + "\n" );
      }  
      */    
    }
    buffer.append("\n\n");

    // Retornar propriedades renderizadas e identadas a partir da tabela
    superClasses.clear();
    directClasses.clear();
    return buffer.toString();

  }
  //****************************************************************************


}
