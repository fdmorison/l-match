// Package
///////////////
package com.merak.ontomap;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.merak.core.*;
import com.merak.ontomap.model.*;

// Class
///////////////
public class ShowTaxonomy {

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public static void main(String[] args) throws Exception {

    // Parse parameters
    ShellParameterSet params = parse(args);

    // Shell Parameters
    String pModelPath    = params.getParameter("model");
    String pOntologyName = params.getAnonymousParameter(0);

    // Initialize the model
    MapModel model = new MapModel( new File(pModelPath) );

    // Exibindo taxonomia da ontologia selecionada
    if ( model.ontologyA.hasName(pOntologyName) ) showTaxonomy(model.ontologyA); else
    if ( model.ontologyB.hasName(pOntologyName) ) showTaxonomy(model.ontologyB); else
    {
      Application.debug.print("ShowTaxonomy.main(String[])","Ontologia '"+pOntologyName+"' nao existe no modelo "+pModelPath);
    }

  }
  //****************************************************************************
  public static void showTaxonomy(Ontology ontology) {

    // Exibir recursivamente a taxonomia abaixo de cada categoria raiz
    for (Category root : ontology.getRootCategories()) {
      System.out.println();
      showTaxonomy( root, 0, "" );
    }

  }
  //****************************************************************************
  public static void showTaxonomy(Category category, int level, String space) {

    // Exibir categoria
    System.out.println( space + category.getName() + "(" + category.getNumberOfDirectInstances() +")" );

    // Exibir recursivamente descendentes da categoria
    space+= "   ";
    level++;
    for (Category child : category.getChildren()) {
      showTaxonomy( child, level, space );
    }

  }
  //****************************************************************************
  public static ShellParameterSet parse(String[] args) {

    // Auxiliar
    ShellParameter    parameter    = null;
    ShellParameterSet parameterSet = new ShellParameterSet("OntoMap - Taxonomy Viewer");

    // About this program
    parameterSet.setDescription("Shows taxonomic hierarchies from a model created by CreateModel");
    parameterSet.setUsage("ShowTaxonomy <model> <ontology_name>");

    // Specify the Directory Parameter
    parameter = parameterSet.createParameter("model",false,"A directory containing the extracted data");
    parameter.setExample("--model=mymodel, -d=mymodel");
    parameter.addShortcut('d');

    // Set final comment
    parameterSet.setComment("Report bugs to <fabricio.dmorison@gmail.com>. Thank you :)");

    // Parse command line arguments
    parameterSet.parse(args);
    return parameterSet;

  }
  //****************************************************************************

}
