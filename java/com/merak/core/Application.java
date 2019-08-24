// Package
/////////////////
package com.merak.core;

// Imports
/////////////////
import java.util.*;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.*;

// Implementation
/////////////////
public class Application extends NamedObject {

 //~ Attributes ////////////////////////////////////////////////////////////////
 //*****************************************************************************
 public  static MsgLogger   debug           = new MsgLogger();
 private static Application instance        = new Application();
 private        File        homePath        = new File( new File("").getAbsolutePath() );
 private        File        dataPath        = new File("ApplicationData");
 private        File        tempPath        = new File("ApplicationData/TemporaryFiles");
 private        File        pluginPath      = new File("ApplicationData/Plugins");
 private        Document    application_xml = null;
 private        HashMap     propertySet     = new HashMap();

 //~ Constructors //////////////////////////////////////////////////////////////
 //*****************************************************************************
 private Application() {

   super("Ontomap");

   // Criando diretorios se nao existem
   if (!dataPath.isDirectory()  ) dataPath.mkdir();
   if (!tempPath.isDirectory()  ) tempPath.mkdir();
   if (!pluginPath.isDirectory()) pluginPath.mkdir();

   // Lendo arquivo com configuracoes
   loadConfiguration();

 }

 //~ Methods ///////////////////////////////////////////////////////////////////
 //*****************************************************************************
 private void loadConfiguration() {

    // Auxiliar
    Element root = null;
    Element tag  = null;
    File    file = new File(getDataPath("application.xml"));

    try {
      // Se application.xml nao existe, entao crie
      if (!file.exists()) {
        // Criando um application_xml basico
        application_xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        // Criando um elemento root
        root = application_xml.createElement("application");
        application_xml.appendChild(root);
        // Criando elemento "name"
        tag = application_xml.createElement("name");
        tag.setTextContent(getHomePath().getName());
        root.appendChild(tag);
        // Criando elemento "description"
        tag = application_xml.createElement("description");
        tag.setTextContent("Resumidamente, descreva sua aplicacao aqui :)");
        root.appendChild(tag);
        // Salvando application_xml
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(root),new StreamResult(file));
      }
      // Senao, carregue os dados de application.xml
      else {
        // Preparando documento para carga
        application_xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        root            = application_xml.getDocumentElement();
        // Lendo atributos
        setName(root.getElementsByTagName("name").item(0).getTextContent());
      }
    }
    catch (Exception e) {
      System.out.println("* Appication.loadConfiguration(): "+e);
      e.printStackTrace();
    }

 }
 //*****************************************************************************
 public static Application getInstance() {

   return instance;

 }
 //*****************************************************************************
 public File getHomePath() {

   return homePath;

 }
 //*****************************************************************************
 public String getHomePath(String path) {

   return homePath + "/" + path;

 }
 //*****************************************************************************
 public File getDataPath() {

   return dataPath;

 }
 //*****************************************************************************
 public String getDataPath(String path) {

   return dataPath + "/" + path;

 }
 //*****************************************************************************
 public File getPluginPath() {

   return pluginPath;

 }
 //*****************************************************************************
 public String getPluginPath(String path) {

   return pluginPath + "/" + path;

 }
 //*****************************************************************************
 public String getProperty(String name) {

   return (String)propertySet.get(name);

 }
 //*****************************************************************************
 public boolean hasProperty(String name) {

   return propertySet.get(name) != null;

 }
 //*****************************************************************************
 public void setProperty(String name,String value) {

   propertySet.put(name,value);

 }
 //*****************************************************************************
 public File createTemporaryFile(String name)
   throws IOException
 {

    return File.createTempFile("~"+name,null,tempPath);

 }
 //*****************************************************************************

}