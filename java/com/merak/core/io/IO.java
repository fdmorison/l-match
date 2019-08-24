// Package
///////////////
package com.merak.core.io;

// Imports
///////////////
import java.io.*;
import com.merak.core.MsgLogger;

// Class
///////////////
public class IO {

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public static void pause() {

    try {
      System.in.read();
    }
    catch(IOException ex) {
      MsgLogger.print("IO.pause()",ex);
    }

  }
  /****************************************************************************/

}
