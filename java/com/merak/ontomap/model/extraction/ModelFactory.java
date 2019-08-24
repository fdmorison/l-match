// Package
///////////////
package com.merak.ontomap.model.extraction;

// Imports
///////////////
import java.io.*;
import java.util.*;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.util.iterator.Filter;

import com.merak.core.*;
import com.merak.core.io.database.*;
import com.merak.ontomap.model.ModelStore;
import com.merak.ontomap.model.extraction.vocabulary.Vocabulary;
import com.merak.ontomap.model.extraction.vocabulary.VocabularyFactory;

// Public Class
///////////////
public class ModelFactory {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private boolean keepHierarchyFlag = false;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public ModelFactory() {

    // nothing to do here :)

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  /* L� um arquivo owl e o estrutura em um objeto modelo.
   * @param ontology_owl arquivo com uma ontologia no formato owl
   */
  private OntModel load(File ontology_owl)
    throws IOException
  {

    // Auxiliar
    OntModel        ontModel = com.hp.hpl.jena.rdf.model.ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM,null);
    FileInputStream input    = new FileInputStream(ontology_owl);

    // Lendo o arquivo e estruturando no modelo
    ontModel.getDocumentManager().setProcessImports(false);
    ontModel.read(input,"");
    input.close();

    return ontModel;

  }
  //****************************************************************************
  private Vocabulary createVocabulary(OntModel ontology1,OntModel ontology2,File modelPath)
    throws Exception
  {

    // Auxiliar
    VocabularyFactory factory    = new VocabularyFactory(modelPath);
    Vocabulary        vocabulary = null;

    // Configure factory
    factory.useStopwords( new File("stopwords.txt") );

    // Create the vocabulary
    vocabulary = factory.create(ontology1,ontology2);

    // Close factory and return the vocabulary
    factory.close();
    return vocabulary;

  }
  //****************************************************************************
  /**
   * Indexa conceitos, instancias e as ontologias contidas em dois arquivos owl
   *
   * @param o1_owl  primeira ontologia a participar do mapeamento
   * @param o2_owl  segunda ontologia a participar do mapeamento
   * @param model   diretorio no qual salvar os dados indexados
   */
  private void createIndex(File o1_owl,File o2_owl,File mappingModel,File mapFile) {

    try {
      // Criando diretorio do modelo
      mappingModel.mkdir();

      // 0) Carregando mapeamentos iniciais
      MapTable mapTable = new MapTable(mapFile);

      // 1) Lendo ontologias no formato owl
      System.err.println(":: Carregando '"+o1_owl.getName()+"' e '"+o2_owl.getName()+"'");
      OntModel ontModel1 = load(o1_owl);
      OntModel ontModel2 = load(o2_owl);

      // 2) Gerando vocabulario comum entre as duas ontologias
      System.err.println(":: Calculando Vocabulario Comum...");
      Vocabulary vocabulary = createVocabulary(ontModel1,ontModel2,mappingModel);

      // 4) Preparando pra indexar
      ModelStore persistence = new ModelStore(mappingModel,IndexTable.Mode.Insert);
      Indexer    indexer     = new Indexer(persistence,vocabulary);

      // 5) Indexando ontologias
      System.err.println(":: Indexando '"+o1_owl.getName()+"'"); indexer.start(o1_owl,ontModel1,mapTable.ontologyA);
      System.err.println(":: Indexando '"+o2_owl.getName()+"'"); indexer.start(o2_owl,ontModel2,mapTable.ontologyB);

      // 6) Finalizando
      persistence.close();
      System.err.println(":: Processo finalizado com sucesso");
    }
    catch(Exception ex) {
      MsgLogger.print("ModelFactory.create(File,File,File)",ex);
    }

  }
  //****************************************************************************
  /**
   * Prepara os modelo para o mapeador
   * @param o1_owl primeira ontologia a participar do mapeamento
   * @param o2_owl segunda ontologia a participar do mapeamento
   * @param model  diretorio no qual salvar os dados indexados
   */
  public void create(File o1_owl,File o2_owl,File mappingModel,File mapFile) {

    createIndex(o1_owl,o2_owl,mappingModel,mapFile);

  }
  //****************************************************************************


}
