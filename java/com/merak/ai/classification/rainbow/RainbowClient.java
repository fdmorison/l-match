// Package
///////////////
package com.merak.ai.classification.rainbow;

// Imports
///////////////
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.LockSupport;
import com.jscape.inet.telnet.*;
import com.merak.core.*;
import com.merak.ai.classification.*;

public class RainbowClient extends TelnetAdapter {

  //~ Class Attributes /////////////////////////////////////////////////////////
  //****************************************************************************
  private static final String SHELL_PROMPT       = ".\n";
  private static final String COMMAND_TERMINATOR = "\r\n.\r\n";

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private OutputParser       parser    = new OutputParser();  
  private Telnet             telnet    = null;
  private TelnetOutputStream out       = null;
  private String             response  = null;
  private Thread             caller    = null;
  private char[]             bufferL1  = {0,0};                  // Recebe dados e identifica shell prompt
  private StringBuilder      bufferL2  = new StringBuilder(100); // Acumula dados recebidos em bufferL1, exceto shell prompt
  private boolean            connected = false;

  //~ Cosntructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public RainbowClient(int port)
    throws TelnetException
  {

    this("localhost",port);

  }  
  //****************************************************************************
  public RainbowClient(String hostname,int port)
    throws TelnetException
  {

	  // create new Telnet instance
	  telnet = new Telnet(hostname,port);

	  // register this class as TelnetRainbowConnection
	  telnet.addTelnetListener(this);

	  // establish Telnet connection
	  telnet.connect();

	  // get output stream to write in
	  out = (TelnetOutputStream) telnet.getOutputStream();

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  /****************************************************************************/
  public synchronized Rank send(String text)
    throws ClassificationException
  {

    // Check connection
    if (!connected) throw new ClassificationException("O cliente nao esta conectado a um Servidor Rainbow!");

    // Resetando buffers; marcando thread atual, pois ela tera que esperar ate que toda a resposta chegue
    bufferL2.setLength(0);
    bufferL1[0] = bufferL1[1] = 0;
    response    = null;
    caller      = Thread.currentThread();

    // Comunicando com o servidor
    try {
      out.println(text,COMMAND_TERMINATOR);          // send text to Rainbow Telnet server
      while (response==null) LockSupport.park(this); // wait for server's response ( synchronized connection )
    }
    catch (Exception ex) {
      throw new ClassificationException("Erro ao comunicar com Servidor Rainbow em "+telnet.getHostname()+":"+telnet.getPort(),ex);
    }

    // No data received means that the instance's text couldnt be classified
    if (response.isEmpty()) return null;
    
    // Parse and return the received rank
    return parser.parseQueryOutput(null,response); 

  }
  /****************************************************************************/
  public synchronized void disconnect() {

    telnet.disconnect();

  }
  /****************************************************************************/
  public void dataReceived(TelnetDataReceivedEvent event) {

    // read data received from Telnet server into buffer
    bufferL1[0] = bufferL1[1];
    bufferL1[1] = event.getData().charAt(0);

    // if the shell prompt '.\n' is received,
    if (bufferL1[0]=='.' && bufferL1[1]=='\n') {
      // then notify rainbow client that all data have been received
      response = bufferL2.toString();
      LockSupport.unpark(caller);
    }
    else if (bufferL1[0]!=0) {
      // else append new data to previous received data
      bufferL2.append(bufferL1[0]);
    }

  }
  /****************************************************************************/
  public void connected(TelnetConnectedEvent event) {

  	connected = true;

  }
  /****************************************************************************/
  public void disconnected(TelnetDisconnectedEvent event) {

    connected = false;
    response  = "";
    if (caller!=null) LockSupport.unpark(caller);

  }
  /****************************************************************************/
  public void doOption(DoOptionEvent event) {

    // refuse any options requested by Telnet server
    telnet.sendWontOption(event.getOption());

  }
  /****************************************************************************/
  public void willOption(WillOptionEvent event) {

    // refuse any options offered by Telnet server
    telnet.sendDontOption(event.getOption());

  }
  /****************************************************************************/

}
