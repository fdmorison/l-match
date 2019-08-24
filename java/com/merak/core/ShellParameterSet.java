// Package
///////////////
package com.merak.core;

// Imports
///////////////
import java.util.*;
import com.merak.core.text.*;

// Class
///////////////
public class ShellParameterSet extends NamedObject {

 //~ Attributes ////////////////////////////////////////////////////////////////
 //*****************************************************************************
 private String                     description  = null;
 private String                     usage        = null;
 private String                     comment      = null;
 private Map<String,ShellParameter> parameterSet = new HashMap<String,ShellParameter>();
 private List<String>               anonymousSet = new ArrayList<String>();

 //~ Constructors //////////////////////////////////////////////////////////////
 //*****************************************************************************
 public ShellParameterSet(String name) {

   // Super Attribute Initialization
   super(name);

   // Add generic parameters
   ShellParameter parameter = createParameter("help",true,"Prints this help screen.");
   parameter.addShortcut('h');
   parameter.addShortcut('?');

 }
 //~ Methods ///////////////////////////////////////////////////////////////////
 //*****************************************************************************
 private int parseArgs(String[] args)
   throws Exception
 {
    // MyProg --opt1=value1 ANONIMOUS0 --opt2=v1+v2+v3 --opt3 ANONIMOUS1 ANONIMOUS2 .. ANONIMOUSN
    // Auxiliar
    ShellParameter param             = null;
    String         argument          = null;
    String         value             = null;
    int            assignmentCounter = 0;

    // Read arguments
    for (int i=0; i<args.length; i++) {
      // Next argument
      argument = args[i];

      // Se argumento inicia com "--" ou "-" seguido de um nome, entao argumento eh um parametro nomeado
      if      (argument.startsWith("--")) argument=argument.substring(2);
      else if (argument.startsWith("-" )) argument=argument.substring(1);
      // Senao, argumento eh um parametro anonimo
      else {
        anonymousSet.add(argument); continue;
      };
      if (argument.isEmpty()) continue;

      // Verificar se o parametro existe
      value = "true";
      param = parameterSet.get(argument);
      if (param==null) throw new Exception("Parametro desconhecido: '"+argument+"'");

      // Se o parametro nao eh booleano, entao o esta na proxima posicao
      if ( !param.isBoolean() ) {
        i++;
        if (i>=args.length || args[i].startsWith("-")) {
          throw new Exception("Parametro '"+argument+"' precisa receber um valor.");
        }
        value = args[i];
      }
      // Contando a atribuicao de valor
      param.setValue(value);
      assignmentCounter++;
    }
    return assignmentCounter;

 }
 //*****************************************************************************
 private String[] breakInLines(String text,int lineLength) {

   // Auxiliar
   int      textLength    = text.length();
   int      numberOfLines = (textLength<=0) ? 0 : (textLength-1)/lineLength + 1;
   String[] lines         = new String[numberOfLines];
   int      offset        = 0;
   int      endset        = 0;

   // Break it
   for (int i=0; i<numberOfLines; i++) {
     offset   = i*lineLength;
     endset   = (offset+lineLength>textLength) ? textLength : offset+lineLength;
     lines[i] = text.substring(offset,endset).trim();
   }
   return lines;

 }
 //*****************************************************************************
 private void renderOption(String header,String body,RenderTable render,String leadingSpaces) {

   // Auxiliar
   Row      row   = render.createRow();
   String[] lines = breakInLines(body,57-leadingSpaces.length());

   // Render the line header and the first body line
   row.createCell(header);
   row.createCell(leadingSpaces+lines[0]);

   // Render remaining body lines
   for (int i=1; i<lines.length; i++) {
     row = render.createRow();
     row.createCell();
     row.createCell(leadingSpaces+lines[i]);
   }

 }
 //*****************************************************************************
 protected void addShortcut(String shortcut,ShellParameter param) {

   parameterSet.put(shortcut,param);

 }
 //*****************************************************************************
 public ShellParameter createParameter(String name,boolean isBoolean,String description) {

   ShellParameter param = new ShellParameter(name,isBoolean,description,this);
   parameterSet.put(name,param);
   return param;

 }
 //*****************************************************************************
 public void setDescription(String description) {

   this.description = description;

 }
 //*****************************************************************************
 public void setUsage(String usage) {

   this.usage = usage;

 }
 //*****************************************************************************
 public void setComment(String comment) {

   this.comment = comment;

 }
 //*****************************************************************************
 public String getDescription() {

   return description;

 }
 //*****************************************************************************
 public String getUsage() {

   return usage;

 }
 //*****************************************************************************
 public String getComment() {

   return comment;

 }
 //*****************************************************************************
 public String getParameter(String name) {

   ShellParameter param = parameterSet.get(name);
   if (param==null) throw new RuntimeException("Parametro '"+name+"' nao pertence a este ShellParameterSet");

   if (!param.isBoolean() && param.isNull()) {
     System.err.println("Parametro '"+name+"' eh esperado mas nao foi fornecido.\n");
     printHelp();
     System.exit(1);
   }

   return param.getValue();

 }
 //*****************************************************************************
 public String getAnonymousParameter(int i) {

   if (i >= anonymousSet.size()) {
     System.err.println("Insuficiencia de Parametros Anonimos: esperava-se encontrar "+(i+1)+" parametros.\n");
     printHelp();
     System.exit(1);
   }
   return anonymousSet.get(i);

 }
 //*****************************************************************************
 public List<String> getAnonymousParameters() {

   return anonymousSet;

 }
 //*****************************************************************************
 public int getNumberOfAnonymousParameters() {

   return anonymousSet.size();

 }
 //*****************************************************************************
 public boolean hasParameter(String name) {

   ShellParameter param = parameterSet.get(name);
   if (param==null) throw new RuntimeException("Parametro '"+name+"' nao pertence a este ShellParameterSet");
   return param.isNull();

 }
 //*****************************************************************************
 public boolean hasAnonymousParameters() {

   return !anonymousSet.isEmpty();

 }
 //*****************************************************************************
 public void parse(String[] args) {

   try {
     // Extrair argumentos
     int assignmentCounter = parseArgs(args);
     // Se --help foi setado ou nenhum parametro foi setado, exibir ajuda e abortar
     if (parameterSet.get("help").hasValue("true")      ) { printHelp(); System.exit(0); }
     if (assignmentCounter==0 && anonymousSet.isEmpty() ) { printHelp(); System.exit(0); }

   }
   catch (Exception ex) {
     System.err.println("\n"+ex.getMessage());
     printHelp();
     System.exit(1);
   }

 }
 //*****************************************************************************
 public void printHelp() {

   // Auxiliar
   RenderTable      render        = new RenderTable();
   String           header        = null;
   String           leadingSpaces = "    ";
   ShellParameter[] params        = parameterSet.values().toArray( new ShellParameter[parameterSet.size()] );
   ShellParameter   lastParam     = null;

   // Prepare
   Arrays.sort(params);

   // Print program's description
   System.out.println("\n"+getName());
   if (description!=null) System.out.println("About: "+description);
   if (usage!=null      ) System.out.println("Usage: "+usage+"\n");

   // Print each parameter's description
   System.out.println("Where possible options include:\n");
   for (ShellParameter param : params) {
     // Skip repeated objects
     if (lastParam==param) continue;
     lastParam = param;

     // Render parameter's name and shortcuts
     header = leadingSpaces + "--" + param.getName();
     for (String shortcut : param.getShortcuts()) {
       header += ", -"+shortcut;
     }
     // Create a line for parameter's header and description
     renderOption(header,param.getDescription(),render,"");
     // Create a line for each parameter's extra description
     if (param.getPossibleValues() != null) renderOption(""," * Possible: "+param.getPossibleValues(),render,"  ");
     if (param.getDefaultValue()   != null) renderOption(""," * Default : "+param.getDefaultValue(),render,"  ");
     if (param.getExample()        != null) renderOption(""," * Example : "+param.getExample(),render,"  ");
   }
   System.out.println( render.toString() );

   // Print final comment
   if (comment!=null) System.out.println( comment + "\n" );

 }
 //*****************************************************************************

}
