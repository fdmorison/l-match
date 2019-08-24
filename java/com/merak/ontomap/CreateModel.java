// Package
///////////////
package com.merak.ontomap;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.merak.ontomap.model.extraction.*;

public class CreateModel {

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public static void main(String[] args) throws Exception {

    // Exibir ajuda e abortar se os parametros sao invalidos
  	if (args.length!=4) {
  	  printHelp();
  	  return;
  	}

    // Lendo parametros da linha de comando
    File o1_owl    = new File(args[0]);
    File o2_owl    = new File(args[1]);
    File model_dir = new File(args[2]);
    File maps_file = new File(args[3]);

    // Extraindo dados das ontologias
    ModelFactory factory = new ModelFactory();
    factory.create(o1_owl,o2_owl,model_dir,maps_file);

    System.out.println("Fim");
    System.exit(1);

  }
  //****************************************************************************
  public static void printHelp() {

  	 System.out.println("USAGE: CreateModel [o1.owl] [o2.owl] [model_dir]");
  	 System.out.println("WHERE: - o1.owl   First ontology to be mapped");
  	 System.out.println("WHERE: - o2.owl   Second ontology to be mapped");
  	 System.out.println("       - MODEL    Model directory to output the indexed data");

  }
  //****************************************************************************

}
