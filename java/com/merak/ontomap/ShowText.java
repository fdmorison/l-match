// Package
///////////////
package com.merak.ontomap;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.merak.ontomap.model.*;

public class ShowText {

  public static void main(String[] args) throws Exception {

    // Sheel parameters
    File path  = new File(args[0]);
    int  docId = Integer.parseInt(args[1]);

    // Auxiliar
    MapModel model    = new MapModel(path);
    Instance instance = model.getInstance(docId);

    // Print text of the instance
    System.out.println(model.getTextOf(instance));

  }

}
