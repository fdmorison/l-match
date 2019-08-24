// Package
///////////////
package com.merak.ai.classification.rainbow;

// Imports
///////////////
import java.util.*;
import java.io.*;
import java.net.*;
import com.jscape.inet.telnet.TelnetException;
import com.merak.ai.classification.*;

// Class
///////////////
public class RainbowServer extends Classifier {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private Process       server = null;
  private RainbowClient client = null;
  private int           port   = 11000;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public RainbowServer() {

    // Supper Attribute Initialization
    super("Rainbow",ClassificationMethod.SVM);

  }
  //****************************************************************************
  public RainbowServer(int port) {

    // Supper Attribute Initialization
    super("Rainbow",ClassificationMethod.SVM);

    // Attributes Initialization
    this.port = port;

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  /* @return a porta usada pelo servidor
   */
  private int getNextPort() {

     if (port>15000) return port = 10000;
     return port++;

  }
  //****************************************************************************
  /* @return a porta usada pelo servidorNewClass
   */
  private boolean startServer(String command,int port)
    throws IOException,SecurityException,IllegalArgumentException,NullPointerException,TelnetException
  {

    // Auxiliar
    BufferedReader reader   = null;
    String         line     = null;
    String         lastline = "";

    // Try to execute a rainbow server at local system
    command += " --query-server="+port;
    server   = Runtime.getRuntime().exec(command);
    reader   = new BufferedReader(new InputStreamReader(server.getErrorStream()));

    // Wait until the server is ready to receive connections
    while ( (line = reader.readLine()) != null ) {
       lastline = line;
       if ( line.equals("Waiting for connection...") ) return true;
    }

    System.err.println(command);
    System.err.println(lastline);
    return false;

  }
  //****************************************************************************
  /* (Re)Inicia o servidor com novas configuracoes e em seguida cria um cliente.
   */
  private void init(String command)
    throws ClassificationException
  {

    try {
  	  // Auxiliar
  	  int port = 0;
      // Fechar servidor e cliente atuais
      close();
      // Tentando executar o servidor e o cliente
      for (int i=0; i<10; i++) {
        // Try to create a rainbow server at local system
   	    port = getNextPort();
   	    if ( !startServer(command,port) ) continue;
   	    // Try to connect to the recently initialized server
   	    client = new RainbowClient(port);
   	    return;
   	  }
   	  throw new ClassificationException("Todas as tentativas de inicializar o servidor Rainbow falharam");
    }
    catch (TelnetException cause) {
      throw new ClassificationException("A Rainbow nao pode ser executada devido a erro interno da propria",cause);
    }
    catch (IOException cause) {
      throw new ClassificationException("A Rainbow nao pode ser executada devido a nao estar instalada ou nao estar na variavel PATH",cause);
    }
    catch (SecurityException cause) {
      throw new ClassificationException("A Rainbow nao pode ser executada devido a uma restricao de seguranca",cause);
    }
    catch (NullPointerException cause) {
      throw new ClassificationException("A Rainbow nao pode ser executada",cause);
    }
    catch (IllegalArgumentException cause) {
      throw new ClassificationException("A Rainbow nao pode ser executada",cause);
    }

  }
  //****************************************************************************
  /* Faz uma consulta de texto ao classificador e retorna as classes de treino
   * rankeadas por similaridade.
   * @param text conteudo textual da instancia.
   * @return um rank de classes
   */
  public synchronized Rank query(String input)
    throws ClassificationException
  {

    // Check Connection
    if (server==null || client==null) {
      throw new ClassificationException("RainbowServer nao foi inicializado");
    }

    // Request rank from server
    return client.send(input);

  }
  //****************************************************************************
  /* Altera parametros sobre o conjunto de treinamento.
   * @param method         Altera o algoritmo usado para treinar
   * @param model          Caminho para o diret�rio que representa modelo indexado pela rainbow
   * @param transientVars  Par�metros extras. Por exemplo, se method for svm, passe os parametros do svm.
   */
  public void setTrainParams(ClassificationMethod method,URL model,Map<String,Object> args)
    throws ClassificationException
  {

    init( "rainbow -m "+method.toString().toLowerCase()+" -d "+model.getFile() );

  }
  //****************************************************************************
  /* Verifica se a Rainbow suporta um metodo em particular. A Rainbow suporta uma
   * ampla gama de metodos.
   */
  public boolean supports(ClassificationMethod method) {

    switch (method) {
      case KNN: case NAIVEBAYES: case SVM: case MAXENT: case NBSHRINKAGE: case TFIDF: case PRIND: case EM_SIMPLE:
      return true;
    }
    return false;

  }
  //****************************************************************************
  /* Fecha o classificador, liberando o servidor, o cliente e, portanto, a
   * conexao telnet da rainbow.
   */
  public void close() {

    if (client!=null) { client.disconnect(); client = null; }
    if (server!=null) { server.destroy()   ; server = null; }

  }
  //****************************************************************************

}
