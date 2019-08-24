// Package
///////////////
package com.merak.core.text;

// Imports
///////////////
import java.io.*;
import java.util.*;
import com.merak.core.*;
import com.merak.core.io.*;

public class Tokenizer {

  //~ Attributes ///////////////////////////////////////////////////////////////
  private FileLoader loader = new FileLoader();

  //~ Cosntructors /////////////////////////////////////////////////////////////
  /****************************************************************************/

  //~ Methods //////////////////////////////////////////////////////////////////
  /****************************************************************************/
  /* Cria um token para cada substrings delimitada por espaco e agrupa os tokens
   * em linhas. Não se considera que o texto a ser tokenizado ocupa o buffer por
   * completo, por isso o final do texto precisa ser marcado com '\0'. Então,
   * parauma string com 1 caracter, buffer deve ter tamanho igual a 2. Por via
   * das dúvidas, buffer[buffer.length-1] é marcado com zero, eliminando
   * qualquer caracter previamente setado nesta posição, portanto cuidado.
   *
   * @param buffer bloco de memória contendo texto terminada por '\0'
   * @return um grupo de tokens agrupados por linha, como no texto original.
   */
  public TextDocument lexWhite(byte[] buffer) {

  	// Se não há texto, retornar um documento vazio
  	TextDocument document = new TextDocument();
  	if (buffer.length<2) return document;
  	buffer[buffer.length-1]=0;

  	// Variaveis
  	List<String> line   = document.createLine();
  	String       token  = null;
  	int          i      = 0;
  	int          offset = 0;

  	// Ignorando espacos iniciais
  	while (Character.isSpace((char)buffer[i])) i++;

  	// Extraindo e contando tokens
  	while (buffer[i]!=0) {
      // Lendo corpo do token
 	    for (offset=i; buffer[i]!=0 && !Character.isSpace((char)buffer[i]); i++);
 	    token = new String(buffer,offset,i-offset);
 	    line.add(token);

      // Ignorando espacos seguintes e finalizando linhas
      while (Character.isSpace((char)buffer[i])) {
      	if (buffer[i]=='\n' || buffer[i]=='\r') {
      	  line = document.createLine();
      	  for (i++; Character.isSpace((char)buffer[i]); i++);
      	  break;
      	}
      	i++;
      }
  	}
  	if (line.isEmpty()) document.removeLine(document.getNumberOfLines()-1);

  	return document;

  }
  /****************************************************************************/
  public TextDocument lexWhite(File file) {

  	return lexWhite( loader.load(file) );

  }
  /****************************************************************************/
  public TextDocument lexWhite(String string) {

  	return lexWhite(string.getBytes());

  }
  /****************************************************************************/


}
