// Package
///////////////
package com.merak.ai.classification;

// Imports
///////////////
import java.net.URL;
import java.util.*;
import com.merak.core.*;

// Class
///////////////
public abstract class Classifier extends NamedObject {

  //~ Attributes ///////////////////////////////////////////////////////////////
  /****************************************************************************/
  private final ClassificationMethod defaultMethod;

  //~ Constructors /////////////////////////////////////////////////////////////
  /****************************************************************************/
  public Classifier(String name,ClassificationMethod defaultMethod) {

    // Super Attribute Initialization
    super(name);

    // Attribute initialization
    this.defaultMethod = defaultMethod;

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  /****************************************************************************/
  /* Retorna o metodo padrao usado pelo classificador
   */
  public ClassificationMethod getDefaultMethod() {

    return defaultMethod;

  }
  /****************************************************************************/
  /* Altera parametros sobre o conjunto de treinamento.
   * @param method Altera o algoritmo usado para treinar. Ex: KNN, SVM, MAXENT, etc.
   * @param model  Referencia para a base de treino. Ex: um diretorio.
   * @param args   Parametros especificos. Ex: se method=KNN, pode-se querer setar o parametro 'knn-k', o que nao acontece se method=SVM.
   */
  public abstract void setTrainParams(ClassificationMethod method,URL model,Map<String,Object> args) throws ClassificationException;

  /****************************************************************************/
  /* Faz uma consulta de texto ao classificador e retorna as classes de treino
   * rankeadas por similaridade.
   * @param input conteudo textual da instancia.
   * @return um rank de classes
   */
  public abstract Rank<String,String> query(String input) throws ClassificationException;

  /****************************************************************************/
  /* Verifica se o classificador suporta um dado metodo
   */
  public abstract boolean supports(ClassificationMethod method);

  /****************************************************************************/
  /* Fecha o classificador, liberando quaisquer recursos associados, como
   * conexoes de rede e de banco de dados
   */
  public abstract void close();

  /****************************************************************************/

}





