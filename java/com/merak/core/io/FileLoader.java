// Package
///////////////
package com.merak.core.io;

// Imports
///////////////
import java.util.*;
import java.io.*;
import com.merak.core.*;

public class FileLoader {

  //~ Attributes ///////////////////////////////////////////////////////////////
  private byte[] buffer   = new byte[0];  
  private int    filesize = 0;
  
  //~ Constructors /////////////////////////////////////////////////////////////
  //~ Methods ////////////////////////////////////////////////////////////////// 
  /****************************************************************************/
  public byte[] load(File file) {
  
  	try {  		
  	  // Abrindo arquivo	  
  	  RandomAccessFile in = new RandomAccessFile(file,"r");
  	  
  	  // Alocando/Realocando memória se necessário
  	  filesize = (int) in.length(); 
  	  if ( buffer.length < filesize+1 ) buffer = new byte[filesize+1];
  	  
  	  // Carregando arquivo
  	  in.read(buffer);
  	  in.close();  	  
  	  buffer[filesize] = 0;
  	}
  	catch(IOException ex) {
  	  MsgLogger.print("FileLoader.load(File,byte[])",ex);
  	  buffer[0] = 0;
  	} 	
  	return buffer;
  	
  }    
  /****************************************************************************/
  public String loadString(File file) {
  
  	try {  		
  	  // Abrindo arquivo	  
  	  RandomAccessFile in = new RandomAccessFile(file,"r");
  	  
  	  // Alocando/Realocando memória se necessário
  	  filesize = (int) in.length(); 
  	  if ( buffer.length < filesize+1 ) buffer = new byte[filesize+1];
  	  
  	  // Carregando arquivo
  	  in.read(buffer);
  	  in.close();
  	  
  	  // Finalizando
  	  return new String(buffer,0,filesize) ;
  	}
  	catch(IOException ex) {
  	  MsgLogger.print("FileLoader.load(File,byte[])",ex);
  	  return "";
  	}
  	
  }   
  /****************************************************************************/
  
}
