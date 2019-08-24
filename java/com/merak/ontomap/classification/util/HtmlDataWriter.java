// Package
///////////////
package com.merak.ontomap.classification.util;

// Imports
///////////////
import java.io.*;
import java.text.*;
import java.util.*;
import com.merak.core.*;
import com.merak.core.text.*;
import com.merak.ai.classification.*;
import com.merak.ontomap.*;
import com.merak.ontomap.model.*;
import com.merak.ontomap.classification.*;
import com.merak.ontomap.mapping.Relation;
import com.merak.ontomap.mapping.MapTable;

// Class
///////////////
public class HtmlDataWriter extends DataWriter {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private final MapTable     idealSet;
  private final Evaluator    evaluator;
  private final Evaluation   globalEvaluation  = new Evaluation();
  private final NumberFormat evalFormat        = NumberFormat.getInstance();
  private final String       styleTag          = createStyleTag();
  private final String       tableTag          = "<table border='1' cellpadding='1' cellspacing='0' bordercolor='#EAEAEA'>\n";
  private final String       trTag1            = "  <TR bgcolor='#f9f9f9'>\n";
  private final String       trTag2            = "  <TR bgcolor='#ebf0f3'>\n";
  private final String       equivalenceSymbol = "<span class='equivalence'>&ordm;  </span>";
  private final String       moregeneralSymbol = "<span class='moregeneral'>&Eacute;</span>";
  private final String       lessgeneralSymbol = "<span class='lessgeneral'>&Igrave;</span>";
  private final String       overlappingSymbol = "<span class='overlapping'>&Ccedil;</span>";
  private final String       mismatchingSymbol = "<span class='mismatching'>&sup1;  </span>";
  private final String       unknownSymbol     = "<span class='unknown'    >?</span>";
  private       boolean      tagColor          = false;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public HtmlDataWriter(MapModel model,MapTable idealSet,int fractionDigits) {

    // Super Attibute Initialization
    super(model,fractionDigits);

    // Attribute Initialization
    this.idealSet  = idealSet;
    this.evaluator = new Evaluator(idealSet);

    // Configure evaluation format
    evalFormat.setMaximumFractionDigits(2);
    evalFormat.setMinimumFractionDigits(2);

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private String createStyleTag() {

    String styleTag = "<style>\n";

    // Estilo das celulas contendo as classes de teste
    styleTag += "th {\n";
    styleTag += "   font-family:Arial;\n";
    styleTag += "   font-size:11px;\n";
    styleTag += "   font-weight:bold;\n";
    styleTag += "   color:#003399;\n";
    styleTag += "   text-align:left;\n";
    styleTag += "   padding: 0 1 0 4;\n";
    styleTag += "   white-space:nowrap;\n";
    styleTag += "}\n";

    // Estilo das celulas contendo as classes de treino rankeadas
    styleTag += "td {\n";
    styleTag += "   font-family:Arial;\n";
    styleTag += "   font-size:11px;\n";
    styleTag += "   color:#333333;\n";
    styleTag += "   padding: 0 1 0 4;\n";
    styleTag += "   white-space:nowrap;\n";
    styleTag += "}\n";

    // Estilo das probabilidades numericas
    styleTag += "span {\n";
    styleTag += "   font-family:Arial;\n";
    styleTag += "   font-size:11px;\n";
    styleTag += "   font-weight:bold;\n";
    styleTag += "   color:red;\n";
    styleTag += "}\n";

    // Estilo dos operadores semanticos
    styleTag += ".equivalence { font-size: 14px; font-family:Symbol; font-weight:bolder; color:#0000FF; padding: 0 5; width:20; text-align:center; }\n";
    styleTag += ".moregeneral { font-size: 14px; font-family:Symbol; font-weight:bolder; color:#00CCFF; padding: 0 5; width:20; text-align:center; }\n";
    styleTag += ".lessgeneral { font-size: 14px; font-family:Symbol; font-weight:bolder; color:#33CC00; padding: 0 5; width:20; text-align:center; }\n";
    styleTag += ".overlapping { font-size: 14px; font-family:Symbol; font-weight:bolder; color:#FF9900; padding: 0 5; width:20; text-align:center; }\n";
    styleTag += ".mismatching { font-size: 14px; font-family:Symbol; font-weight:bolder; color:black  ; padding: 0 5; width:20; text-align:center; }\n";
    styleTag += ".unknown     { font-size: 14px; font-family:Symbol; font-weight:bolder; color:black  ; padding: 0 5; width:20; text-align:center; }\n";

    // Demais estilos
    styleTag += ".eval    { color:#666666  ; padding:0 3; }";
    styleTag += ".maxeval { color:black    ; padding:0 3; }";
    styleTag += ".noeval  { color:lightgray; padding:0 3; }";
    styleTag += ".title   { color:white    ; background-color:#000000; }";

    return styleTag + "</style>\n";

  }
  //****************************************************************************
  private Relation getRelation(Category source,Category target) {

    return (idealSet==null) ? Relation.UNKNOWN : idealSet.get(source,target);

  }
  //****************************************************************************
  private String formatCell(String priority,Relation symbol,Category category) {

    String renderedSymbol = unknownSymbol;
    if (symbol!=null) switch (symbol) {
      case EQUIVALENCE  : renderedSymbol = equivalenceSymbol; break;
      case MORE_GENERAL : renderedSymbol = moregeneralSymbol; break;
      case LESS_GENERAL : renderedSymbol = lessgeneralSymbol; break;
      case OVERLAPPING  : renderedSymbol = overlappingSymbol; break;
      case MISMATCHING  : renderedSymbol = mismatchingSymbol; break;
    }
    return "    <TD><span>"+priority+"</span> "+renderedSymbol+" "+category.getName()+"</TD>\n";

  }
  //****************************************************************************
  private String formatCell(Category category,Evaluation eval,int level) {

    // Auxiliar
    double precision = eval.getPrecision();
    double recall    = eval.getRecall();
    double fmeasure  = eval.getFMeasure();
    String evalClass = ( precision==1 || recall==1 || fmeasure==1 ) ? "maxeval" : "eval";
    String html;

    // Render header cell
    html  = "    <TH style='padding-left:"+(28*level)+";'>|"+level+"|--"+category.getName()+"("+category.getNumberOfDirectInstances()+")</TH>\n";

    // Render evaluation cells
    if ( !eval.hasComputedElements() ) {
      html += "    <TD class='noeval'>?.??</TD>\n";
      html += "    <TD class='noeval'>?.??</TD>\n";
      html += "    <TD class='noeval'>?.??</TD>\n";
    }
    else {
      html += "    <TD class='"+evalClass+"'>"+evalFormat.format(precision)+"</TD>\n";
      html += "    <TD class='"+evalClass+"'>"+evalFormat.format(recall   )+"</TD>\n";
      html += "    <TD class='"+evalClass+"'>"+evalFormat.format(fmeasure )+"</TD>\n";
    }
    return html;

  }
  //****************************************************************************
  private String formatTitle(Category root) {

    String html;
    html  = "  <TR>\n";
    html += "    <TD class='title' align='left'  >Query: Taxonomy "+root.getName()+"</TD>\n";
    html += "    <TD class='title' align='center'>P</TD>\n";
    html += "    <TD class='title' align='center'>R</TD>\n";
    html += "    <TD class='title' align='center'>F</TD>\n";
    html += "    <TD class='title' align='left'  >Rank</TD>\n";
    html += "  </TR>\n";
    return html;

  }
  //****************************************************************************
  private String formatGlobalEvaluation() {

    String html;
    html  = "  <TR>\n";
    html += "    <TD class='title' align='left'  >Global Evaluation</TD>\n";
    html += "    <TD class='title' align='center'>"+evalFormat.format( globalEvaluation.getPrecision() )+"</TD>\n";
    html += "    <TD class='title' align='center'>"+evalFormat.format( globalEvaluation.getRecall()    )+"</TD>\n";
    html += "    <TD class='title' align='center'>"+evalFormat.format( globalEvaluation.getFMeasure()  )+"</TD>\n";
    html += "  </TR>\n";
    return html;

  }
  //****************************************************************************
  private String renderRank(CategoryRank rank,int level,NumberFormat nf) {

    // Auxiliar
    String     html     = null;
    Relation   relation = null;
    Category   target   = null;
    Category   source   = rank.getQuery();
    Evaluation eval     = evaluator.evaluate(rank);

    // Add partial evaluation to global
    if ( source.getNumberOfAllInstances() > 0 ) {
      globalEvaluation.sum(eval);
    }

    // Render rank's descriptor
    html  = (tagColor) ? trTag1 : trTag2 ;
    html += formatCell(source,eval,level);

    // Render rank's body
    if (!rank.isEmpty() && rank.getPriority(0)>0) {
      for (int j=0; j<rank.length(); j++) {
        target   = rank.getEntity(j);
        relation = getRelation(source,target);
        html    += formatCell(nf.format(rank.getPriority(j)),relation,target);
      }
    }

    // Finalize
    tagColor = !tagColor;
    return html + "  </TR>\n";

  }
  //****************************************************************************
  private void renderCategory(Category category,IntegerTable table,FileWriter writer,int level)
    throws IOException
  {

    // Render the class
    String html = renderRank(table.toRank(category),level,integerFormat);
    writer.write(html);

    // Render the subclasses
    for (Category child : category.getChildren()) {
      renderCategory(child,table,writer,level+1);
    }

  }
  //****************************************************************************
  private void renderCategory(Category category,DoubleTable table,FileWriter writer,int level)
    throws IOException
  {

    // Render the class
    String html = renderRank(table.toRank(category),level,doubleFormat);
    writer.write(html);

    // Render the subclasses
    for (Category child : category.getChildren()) {
      renderCategory(child,table,writer,level+1);
    }

  }
  //****************************************************************************
  private void renderTable(DoubleTable table,String filename,String title) {

    try {
      // Auxiliar
      FileWriter writer = new FileWriter(outputDirectory + "/" + filename + ".html" );
      globalEvaluation.reset();

      // Escrever cabecalho do arquivo
      writer.write("<html>\n");
      writer.write("<head><title>"+title+"</title>\n"+styleTag+"</head>\n");
      writer.write("<body>\n");

      // Escrever corpo do arquivo
      // Para cada categoria de teste, rankear as classes de treino por similaridade
      writer.write(tableTag);
      for (Category category : table.rowOntology.getRootCategories()) {
        writer.write( formatTitle(category) );
        renderCategory(category,table,writer,0);
      }
      writer.write(formatGlobalEvaluation());
      writer.write("</table>\n");

      // Escrever final do arquivo
      writer.write("</body>\n</html>");
      writer.close();
    }
    catch (Exception ex) {
      Application.debug.print("DataWriter.renderTable(DoubleTable,String,String)",ex);
    }

  }
  //****************************************************************************
  private void renderTable(IntegerTable table,String filename,String title) {

    try {
      // Auxiliar
      FileWriter writer = new FileWriter(outputDirectory + "/" + filename + ".html" );
      globalEvaluation.reset();

      // Escrever cabecalho do arquivo
      writer.write("<html>\n");
      writer.write("<head><title>"+title+"</title>\n"+styleTag+"</head>\n");
      writer.write("<body>\n");

      // Escrever corpo do arquivo
      // Para cada categoria de teste, rankear as classes de treino por similaridade
      writer.write(tableTag);
      for (Category category : table.rowOntology.getRootCategories()) {
        writer.write("\n   <TR><TD style='background:black;color:white;'><font face='arial'>Root: "+category.getName()+"</font></TD></TR>\n\n");
        renderCategory(category,table,writer,0);
      }
      writer.write(formatGlobalEvaluation());
      writer.write("</table>\n");

      // Escrever final do arquivo
      writer.write("</body>\n</html>");
      writer.close();
    }
    catch (Exception ex) {
      Application.debug.print("DataWriter.renderTable(IntegerTable,String,String)",ex);
    }

  }
  //****************************************************************************
  public void save(OneWayCategoryPrediction prediction) {

    // Auxiliar
    String sourceName = prediction.sourceOntology.getName();
    String targetName = prediction.targetOntology.getName();

    // 1. SALVAR RANKS DE SIMILARIDADE
    renderTable(
      prediction.similarityTable,
      "oneway.similarity_"+sourceName+".to."+targetName,
      "One Way Similarity: "+sourceName+" --> "+targetName
    );

    // 2. SALVAR RANKS DE INTERSECAO
    renderTable(
      prediction.overlapTable,
      "oneway.overlap_"+sourceName+".to."+targetName,
      "One Way Overlap: "+sourceName+" --> "+targetName
    );

  }
  //****************************************************************************/
  public void save(TwoWayCategoryPrediction prediction) {

    // Auxiliar
    String sourceName = prediction.ontologyA.getName();
    String targetName = prediction.ontologyB.getName();

    // 1. SALVAR DADOS POR PONTO DE VISTA
    save( prediction.predictionFromA ); // PONTO DE VISTA DA ONTOLOGIA A
    save( prediction.predictionFromB ); // PONTO DE VISTA DA ONTOLOGIA B

    // 2. SALVAR TABELA DE SIMILARIDADE COMBINADA
    renderTable(
      prediction.similarityTable,
      "twoway.similarity_"+sourceName+".and."+targetName,
      "Two Way Similarity: "+sourceName+" <--> "+targetName
    );

    // 2. SALVAR TABELA DE SOBREPOSICAO COMBINADA
    renderTable(
      prediction.overlapTable,
      "twoway.overlap_"+sourceName+".and."+targetName,
      "Two Way Overlap: "+sourceName+" <--> "+targetName
    );
    
    
  }
  //****************************************************************************
}