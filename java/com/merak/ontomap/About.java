// Package
///////////////
package com.merak.ontomap;

// Imports
///////////////
import java.io.*;
import com.merak.core.*;
import com.merak.ontomap.model.*;

public class About {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public static void main (String args[]) throws Exception {

    // Get Shell Params
    ShellParameterSet parameters = parse(args);
    String            pModel     = parameters.getAnonymousParameter(0);

    // Auxiliar
    MapModel model = new MapModel( new File(pModel) );
    int      empty;
    
    // Show information about the model
    System.out.println();    
    System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::");
    System.out.println(":: ABOUT MODEL ");        
    System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::");
    System.out.println(":: Classes    : "+model.getNumberOfCategories()   );
    System.out.println(":: Instances  : "+model.getNumberOfInstances()  );
    System.out.println("::");
    System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::: ");
    
    // Show information about ontologies   
    showAboutOntology(model.ontologyA);
    showAboutOntology(model.ontologyB);
    System.out.println();
    
    
    showInstanceDistribution(model.ontologyA);
    showInstanceDistribution(model.ontologyB);
    
    System.out.println();    
    	
  }
  //****************************************************************************
  public static void showAboutOntology(Ontology ontology) throws Exception {
  	   
    // Count empty classes
    int empty = 0;    
    for (Category c : ontology) {
    	if ( c.getNumberOfAllInstances() == 0 ) empty++;
    }
    
    // Show information about ontology    
    System.out.println(":: ABOUT ONTOLOGY "+ontology.getName());        
    System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::: ");    	
    System.out.println(":: Classes                 : "+ontology.getNumberOfCategories()+" total, "+ontology.getNumberOfConcreteCategories()+" concrete, "+ontology.getNumberOfAbstractCategories()+ " abstract, "+empty+" empty");  
    System.out.println(":: Instances               : "+ontology.getNumberOfInstances() );    	 	 	    	
    System.out.println(":: Instances/Class         : "+ontology.getNumberOfInstances()/(float)ontology.getNumberOfCategories() );
    System.out.println(":: Instances/Concrete Class: "+ontology.getNumberOfInstances()/(float)ontology.getNumberOfConcreteCategories() );
    System.out.println("::");     
    System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::: ");    

  }
  //****************************************************************************
  public static void showInstanceDistribution(Ontology ontology) throws Exception {

    System.out.println(":: Instance Distribution per Class in Ontology "+ontology.getName()); 
    for (Category c : ontology.getConcreteCategories()) {
    	System.out.println(c.getName()+" "+c.getNumberOfDirectInstances());
    }
    System.out.println();
    
  }  
  //****************************************************************************
  public static ShellParameterSet parse(String[] args) {

    // Auxiliar
    ShellParameter    parameter    = null;
    ShellParameterSet parameterSet = new ShellParameterSet("About Model");

    // About this program
    parameterSet.setDescription("Shows information about the model");
    parameterSet.setUsage("About <model>");
    /*
    // Specify the Action Parameter
    parameter = parameterSet.createParameter("show",false,"What to show");
    parameter.setPossibleValues("normal","normal, instance_distribution");
    parameter.setExample("--show=normal");
    parameter.addShortcut('s');
    */
    // Set final comment
    parameterSet.setComment("Report bugs to <fabricio.dmorison@gmail.com>. Thank you :)");

    // Parse command line arguments
    parameterSet.parse(args);
    return parameterSet;

  }
  //****************************************************************************
}
