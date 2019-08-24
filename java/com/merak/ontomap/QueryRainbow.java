// Package
///////////////
package com.merak.ontomap;

// Imports
///////////////
import java.io.*;
import com.merak.core.io.FileLoader;
import com.merak.ai.classification.rainbow.RainbowClient;

public class QueryRainbow {

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public static void main (String args[]) throws Exception {

    if (args.length!=2) { printHelp(); return; }

    // Shell Params / Auxiliar Params
    File       test   = new File(args[0]);
    int        port   = Integer.parseInt(args[1]);
    FileLoader loader = new FileLoader();

    // Connect to server
    RainbowClient client = new RainbowClient("localhost",port);

    // Classify the test file
    System.out.println(
      client.send( loader.loadString(test) ).toStringAsColumn()
    );
    client.disconnect();

  }
  //****************************************************************************
  public static void printHelp() {

    System.err.println( "USAGE: QueryRainbow FILE PORT" );

  }
  //****************************************************************************
}
