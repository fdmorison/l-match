// Package
///////////////
package com.merak.ontomap;

// Imports
///////////////
import java.io.*;
import java.util.*;

import com.merak.core.*;
import com.merak.ai.classification.*;

import com.merak.ontomap.*;
import com.merak.ontomap.model.*;
import com.merak.ontomap.classification.*;
import com.merak.ontomap.classification.util.*;
import com.merak.ontomap.mapping.*;

// Class
///////////////
public class InferAllMap {
    
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public static void main(String[] args) throws Exception {

    if (args.length!=2) {
        System.out.println("USAGE: InferAllMap <model> <mapfile>");
    }
     
    // Prepare model and maptable
    MapModel model = new MapModel(new File(args[0]));
    MapTable table = new MapTable(model);

    // Load map file
    table.load( new File(args[1]) );
    
    // Show all mappings (explicity and implicity)
    System.out.println(table);

  }
  //****************************************************************************
    
}
