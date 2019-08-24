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
import com.merak.ontomap.model.ModelStore;
import com.merak.ontomap.model.extraction.util.DirectoryFilter;
import com.merak.ontomap.model.extraction.render.InstanceRender;
import com.merak.ontomap.model.extraction.vocabulary.Vocabulary;

// Public Class
///////////////
public class Indexer {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private List<NewCategory> categoryList    = new LinkedList<NewCategory>();
  private ModelStore        persistence     = null;
  private InstanceRender    render          = null;
  private Filter            anonimousFilter = null;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public Indexer(ModelStore persistence,Vocabulary vocabulary) {

    // Attribute Initialization
    this.persistence = persistence;
    this.render      = new InstanceRender(vocabulary);

    // Criando um filtro que reconhece classes anonimas
    this.anonimousFilter = new Filter() {
                                 public boolean accept( Object o ) {
                                   return ((Resource) o).isAnon();
                                 }
                               };

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private void indexInstances(NewCategory newCategory,File directory)
    throws IOException
  {

    // Auxiliar
    Iterator    iter        = newCategory.getSource().listInstances(true);
    NewOntology newOntology = newCategory.getOntology();
    NewInstance newInstance = null;
    Individual  oldInstance = null;
    FileWriter  writer      = null;
    int         id          = persistence.pertinenceTable.insert(null);
    String      name        = null;
    String      text        = null;

    // Check id
    if ( id != newCategory.getId() ) throw new IOException("Os ids da categoria e da lista nao sao iguais");

    // For each instance
    while (iter.hasNext()) {
      // Colhendo dados da proxima instancia
      oldInstance = (Individual) iter.next();
      name        = oldInstance.getLocalName();
      text        = render.render(oldInstance);
      // Salvando dados da instancia, se isso ainda nao foi feito
      newInstance = newOntology.getInstance(name);
      if ( newInstance == null ) {
        newInstance = newOntology.createInstance(name,text);
      }
      // Notificando pertinencia
      newCategory.notifyInstance(newInstance);
      persistence.pertinenceTable.append(newInstance.getId());
      // Convertendo instancia em arquivo na entrada da Rainbow
      writer = new FileWriter( directory + "/" + name + ".txt" );
      writer.write(text);
      writer.close();
    }

  }
  //****************************************************************************
  private NewCategory index(NewCategory newCategory,File parentDirectory)
    throws IOException
  {

    // Auxiliar
    Iterator    iter        = null;
    NewOntology newOntology = newCategory.getOntology();
    NewCategory newChild    = null;
    OntClass    oldChild    = null;
    File        directory   = new File(parentDirectory + "/" + newCategory.getId() + "_" + newCategory.getName());

    // 0) Preparando
    directory.mkdir();
    categoryList.add(newCategory);

    // 1) Descobrindo Pertinencia e Taxonomia
    indexInstances(newCategory,directory);

    // 2) Descobrindo Taxonomias
    iter = newCategory.getSource().listSubClasses(true);
    while (iter.hasNext()) {
      oldChild = (OntClass)iter.next();                // Categoria encontrada, sera se eh nova?
      newChild = newOntology.notifyCategory(oldChild); // Verificando se a categoria ja foi indexada anteriormente
      if (newChild.isRecent()) {                       // Se a categoria acabou de ser criada, ent�o
        index(newChild,parentDirectory);               //   marcar categoria para posterior indexacao de suas subcategorias
      }
      newCategory.notifyChild(newChild);
    }

    // 4) Deletando diretorio se vazio
    if (!newCategory.hasInstance()) directory.delete();

    // 5) Return the category descriptor
    return newCategory;

  }
  //****************************************************************************
  public void indexForRainbow(NewOntology ontology) {

    try {
      // Auxiliar
      File[] classes = ontology.getRainbowInput().listFiles( new DirectoryFilter() );
      String command = "rainbow -i -d "+ontology.getRainbowModel();

      // Formatar comando para executar o indexador da Rainbow
      for (File directory : classes) {
        command += " "+directory;
      }

      // Executar o indexador da Rainbow como subprocesso
      Process rainbow = Runtime.getRuntime().exec(command);
      rainbow.waitFor();
    }
    catch (Exception ex) {
      MsgLogger.print("ModelFactory.indexForRainbow(NewOntology)",ex.getMessage());
    }

  }
  //****************************************************************************
  private void show(OntClass c,String space) {
  
    System.out.println( "::" + space + c.getLocalName() );
    
    Iterator iter = c.listSubClasses(true); 
    while (iter.hasNext()) {
      c = (OntClass)iter.next();
      show(c,space+"  ");
    }    
      
  }
  
  //****************************************************************************
  /* Para cada ontolgia, chame este m�todo. � por este m�todo que se inicia a
   * indexacao de uma ontologia por vez.
   *
   * @param ontology_owl A ontologia a indexar
   * @param mappingModel Vocabulario para usar
   * @param ontModel     Modelo que representa a ontologia original
   */
  public void start(File ontology_owl,OntModel ontModel,Map<String,MapList> mapTable)
    throws IOException
  {

    // Auxiliar
    String      name               = ontology_owl.getName();
    NewOntology newOntology        = null;
    OntClass    oldCategory        = null;
    NewCategory newCategory        = null;
    Interval    instanceIdInterval = new Interval( persistence.instanceTable.getNextId() );
    Interval    categoryIdInterval = new Interval( persistence.categoryTable.getNextId() );

    render.setMapTable(mapTable);

    // Criando nova ontologia
    name        = name.substring(0,name.lastIndexOf('.')).toLowerCase();
    newOntology = persistence.createOntology(name);

    // Indexando conceitos recursivamente
    Iterator iter = ontModel.listHierarchyRootClasses().filterDrop(anonimousFilter);    
    while (iter.hasNext()) {
      oldCategory = (OntClass)iter.next();
      newCategory = newOntology.notifyCategory(oldCategory);
      if (newCategory.isRecent()) {
        index(newCategory,newOntology.getRainbowInput());
      }
    }
    
    iter = ontModel.listHierarchyRootClasses();
    while (iter.hasNext()) {
      oldCategory = (OntClass)iter.next();
     // show(oldCategory," ");
    }    
    
    
    // Salvar entradas na tabela de taxonomia
    while (!categoryList.isEmpty()) {
      newCategory = categoryList.remove(0);
      persistence.createChildrenList(newCategory);
    }

    // Salvar descritor da ontologia
    instanceIdInterval.endset = persistence.instanceTable.getNextId()-1;
    categoryIdInterval.endset = persistence.categoryTable.getNextId()-1;
    persistence.ontologyTable.append(" "+categoryIdInterval+" "+instanceIdInterval);

    // Criar modelo da Rainbow
    indexForRainbow(newOntology);

  }
  //****************************************************************************

}
