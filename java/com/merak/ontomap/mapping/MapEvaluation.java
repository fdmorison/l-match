// Package
/////////////////
package com.merak.ontomap.mapping;

// Imports
/////////////////
import java.util.*;
import java.text.NumberFormat;
import com.merak.core.text.*;
import com.merak.ontomap.Evaluation;
import com.merak.ontomap.model.Category;
import com.merak.ontomap.classification.StatTools;

// Class
/////////////////
public class MapEvaluation {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  private MapTable     relevantSet;            // The relevant set of mappings
  private MapTable     computedSet;            // The computed set of mappings
  private Evaluation   globalEvaluation;       // The evaluation for the matcher
  private Evaluation[] evaluationOfRelation;   // The evaluation for each relation type
  private double       accuracy;               // The accuracy of the matcher  
  private double       precision;              // The accuracy of the matcher  
  private double       recall;                 // The accuracy of the matcher  

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public MapEvaluation(MapTable relevantSet,MapTable computedSet) {

    // Attribute Initialization
    this.relevantSet         = relevantSet;
    this.computedSet         = computedSet;
    this.evaluationOfRelation = new Evaluation[ Relation.values().length ];

    // Start Evaluation
    evaluate();

  }

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  private void evaluate() {

    // Auxiliar
    Relation   relevant;
    Relation   computed;
    Evaluation evaluation;

    // 0. Zerar contadores parciais
    for (int i=0; i<evaluationOfRelation.length; i++) {
      evaluationOfRelation[i] = new Evaluation(0,0,0);
    }

    // 1. Contar mapeamentos corretos, mapeamentos computados e mapeamentos corretamente computados por relacao
    for ( Category source : relevantSet.source ) {
      for ( Category target : relevantSet.target ) {
        // Comparar mapeamento ideal com o mapeamento computado
        relevant = relevantSet.get(source,target);  // Mapeamento ideal entre as categorias (desejavel)
        computed = computedSet.get(source,target);  // Mapeamento computado entre as categorias (sugerido)
        // Se "mapeamento ideal" == "mapeamento computado""
        if (relevant == computed) {
          evaluationOfRelation[relevant.ordinal()].idealComputed++; // Oba, mais um mapeamento corretamente computado!
        }
        // Contabilizar mapeamentos por relacao
        evaluationOfRelation[relevant.ordinal()].ideal++;    // Contar mapeamento correto para a relacao
        evaluationOfRelation[computed.ordinal()].computed++; // Contar mapeamento computado para a relacao ( corretamente ou incorretamente )     
      }
    }

    // 2. Preparar avaliacao global
    globalEvaluation = new Evaluation();  
    precision        = 0;
    recall           = 0;
    for ( Relation r : Relation.values() ) {
      // Ignorar diferenca
      if (r.isMismatching()) continue;
      // Avaliacao da proxima relacao
      evaluation = evaluationOfRelation[r.ordinal()];      
      // Totalizar parametros de avaliacao: somar tamanho dos conjuntos
      globalEvaluation.sum(evaluation);
      // Calcular media das metricas de avaliacao
      precision += evaluation.getPrecision();
      recall    += evaluation.getRecall();
    }
    // Total Accuracy
    precision /= evaluationOfRelation.length-1;
    recall    /= evaluationOfRelation.length-1;   
    accuracy   = globalEvaluation.idealComputed / (double) relevantSet.length();
    // Overlap Accuracy
    // evaluation = evaluationOfRelation[Relation.UNKNOWN.ordinal()];
    //accuracy   = (globalEvaluation.idealComputed - evaluation.idealComputed) / (globalEvaluation.ideal - evaluation.ideal) ;
    
  }  
  //****************************************************************************
  public double getAccuracy() {

    return accuracy;

  }
  //****************************************************************************
  public double getPrecision() {

    return precision;

  }
  //****************************************************************************
  public double getPrecision(Relation relation) { 
      
    return evaluationOfRelation[relation.ordinal()].getPrecision();

  } 
  //****************************************************************************
  public double getRecall() {

    return recall;

  }
  //****************************************************************************
  public double getRecall(Relation relation) {

    return evaluationOfRelation[relation.ordinal()].getRecall();

  } 
  //****************************************************************************
  public double getFMeasure() {

    return Evaluation.getFMeasure( getPrecision() , getRecall() );

  }
  //****************************************************************************
  public double getFMeasure(Relation relation) {

    return evaluationOfRelation[relation.ordinal()].getFMeasure();

  }  
  //****************************************************************************
  public String toString() {

    // Auxiliar
    RenderTable  render = new RenderTable();
    Row          row    = null;
    NumberFormat nf     = NumberFormat.getInstance();
    Evaluation   eval   = null;
   
    // Configure Number Format
    nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(2);
    
    // Create horizontal delimiter
    row = render.createRow(); 
    row.createCell("------------" ); row.createCell("+"); 
    row.createCell("-----" ); row.createCell("--------" ); row.createCell("--------------"); row.createCell("+"); 
    row.createCell("---------" ); row.createCell("------" ); row.createCell("---------" );
    
    // Create the Title Row
    row = render.createRow();     
    row.createCell("RELATION" );   
    row.createCell("|");    
    row.createCell("IDEAL" ); row.createCell("COMPUTED" ); row.createCell("IDEAL_COMPUTED" );      
    row.createCell("|");
    row.createCell("PRECISION"); row.createCell("RECALL"   ); row.createCell("F-MEASURE"); 
    
    // Create horizontal delimiter
    row = render.createRow(); 
    row.createCell("------------" ); row.createCell("+"); 
    row.createCell("-----" ); row.createCell("--------" ); row.createCell("--------------"); row.createCell("+"); 
    row.createCell("---------" ); row.createCell("------" ); row.createCell("---------" );
    
    // Create one row of evaluation for each relation type
    for (Relation relation : Relation.values()) {    
      if (relation.isMismatching()) continue;        
      eval = evaluationOfRelation[relation.ordinal()];
      row  = render.createRow(); 
      row.createCell( String.valueOf(relation).toLowerCase());
      row.createCell('|');  
      // Show size of each set
      row.createCell( (int)eval.ideal         );
      row.createCell( (int)eval.computed      );
      row.createCell( (int)eval.idealComputed );
      row.createCell('|');
      // Evaluation Metrics
      row.createCell( nf.format( eval.getPrecision() ) );
      row.createCell( nf.format( eval.getRecall()    ) );     
      row.createCell( nf.format( eval.getFMeasure()  ) );         
    }

    // Create horizontal delimiter
    row = render.createRow(); 
    row.createCell("------------" ); row.createCell("+"); 
    row.createCell("-----" ); row.createCell("--------" ); row.createCell("--------------"); row.createCell("+"); 
    row.createCell("---------" ); row.createCell("------" ); row.createCell("---------" );
    
    // Create one row for global evaluation
    row = render.createRow(); 
    row.createCell( "total" );
    row.createCell('|');
    row.createCell( (int)globalEvaluation.ideal         );
    row.createCell( (int)globalEvaluation.computed      );
    row.createCell( (int)globalEvaluation.idealComputed );
    row.createCell('|');
    row.createCell( nf.format( getPrecision() ) );
    row.createCell( nf.format( getRecall()    ) );  
    row.createCell( nf.format( getFMeasure()  ) );  
    
    // Create horizontal delimiter
    row = render.createRow(); 
    row.createCell("------------" ); row.createCell("+"); 
    row.createCell("-----" ); row.createCell("--------" ); row.createCell("--------------"); row.createCell("+"); 
    row.createCell("---------" ); row.createCell("------" ); row.createCell("---------" );
    
    // Render the table
    return "* Accuracy is "+getAccuracy()+"\n"+render.toString();  

  }  
  //****************************************************************************

}