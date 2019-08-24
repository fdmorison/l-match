// Package
///////////////
package com.merak.ontomap.mapping;

// Imports
///////////////
import java.io.*;
import java.util.*;
import java.text.ParseException;
import com.merak.core.*;
import com.merak.core.text.*;
import com.merak.ontomap.model.*;

// Class
///////////////
public class MapTable {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  public  final Ontology     source;
  public  final Ontology     target;
  private final Relation[][] table;

  //~ Constructors /////////////////////////////////////////////////////////////
  //****************************************************************************
  public MapTable(MapModel model) {

    // Attribute Initialization
    this(model.ontologyA,model.ontologyB,Relation.UNKNOWN);

  }
  //****************************************************************************
  public MapTable(MapModel model,Relation initialValue) {

    // Attribute Initialization
    this(model.ontologyA,model.ontologyB,initialValue);

  }
  //****************************************************************************
  public MapTable(Ontology source,Ontology target) {

    // Attribute Initialization
    this(source,target,Relation.UNKNOWN);

  }
  //****************************************************************************
  public MapTable(Ontology source,Ontology target,Relation initialValue) {

    // Attribute Initialization
    this.source  = source;
    this.target  = target;
    this.table   = new Relation[ source.getNumberOfCategories() ][ target.getNumberOfCategories() ];

    // Relation Table Initialization
    for (int row=0; row<source.getNumberOfCategories(); row++) {
      for (int col=0; col<target.getNumberOfCategories(); col++) {
        table[row][col] = initialValue;
      }
    }

  }
  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  /* se A 'equivalente' B, entao A e B compartilham toda ancestral/descendente
   * se A 'maisgeral'   B, entao A contem todo descendente de B e B esta contido em todo ancestral de A
   * se A 'menosgeral'  B, A esta contido em todo ancestral de B E B contem todo descendente de A
   * se A 'sobreposto'  B, A e B possuem ao menos 1 descendente ou ancestral em comum
   * se A 'diferente'   B, A e B nao possuem ancestrais nem descendentes em comum
   */
  private void setEquivalence(Category a,Category b) {

    // Parar se a relacao atual for tao ou mais forte que a Equivalencia
    switch ( table[ a.getOntologicalId() ][ b.getOntologicalId() ] ) {
      case EQUIVALENCE  : return;
      // Default: sobrescrever relacoes mais fracas UNKNOWN, MISMATCHING, OVERLAPPING, LESS_GENERAL e MORE_GENERAL
    }

    // A e B sao equivalentes
    table[ a.getOntologicalId() ][ b.getOntologicalId() ] = Relation.EQUIVALENCE;

    // Logo, todo ancestral de A eh mais geral que B
    for (Category parentA  : a.getParents() ) setMoreGeneral(parentA,b);

    // Logo, A eh menos geral que todo ancestral de B
    for (Category parentB  : b.getParents() ) setLessGeneral(a,parentB);

    // Logo, Todo descendente de A eh menos geral que B
    for (Category childA   : a.getChildren()) setLessGeneral(childA,b );

    // Logo, A eh mais geral que todo descendente de B
    for (Category childB   : b.getChildren()) setMoreGeneral(a,childB );
    
  }
  //****************************************************************************
  private void setMoreGeneral(Category a,Category b) {

    // Parar se a relacao atual for tao ou mais forte que a Mais Geral
    switch ( table[ a.getOntologicalId() ][ b.getOntologicalId() ] ) {
      case EQUIVALENCE  : return;
      case MORE_GENERAL : return;
      // Default: sobrescrever relacoes mais fracas UNKNOWN, MISMATCHING, OVERLAPPING e LESS_GENERAL
    }

    // 1) A eh mais geral que B --- B eh menos geral que A
    table[ a.getOntologicalId() ][ b.getOntologicalId() ] = Relation.MORE_GENERAL;

    // 2) Todo ancestral de A eh mais geral que B
    for (Category parentA : a.getParents()  ) setMoreGeneral(parentA,b);

    // 3) A eh mais geral que todo descendente de B
    for (Category childB  : b.getChildren() ) setMoreGeneral(a,childB);

    // 4) A eh sobreposto a todo ancestral de B
    for (Category parentB : b.getParents()  ) setOverlapping(a,parentB);

  }
  //****************************************************************************
  private void setLessGeneral(Category a,Category b) {

    // Parar se a relacao atual for tao ou mais forte que a Menos Geral
    switch ( table[ a.getOntologicalId() ][ b.getOntologicalId() ] ) {
      case EQUIVALENCE  : return;
      case MORE_GENERAL : return;
      case LESS_GENERAL : return;
      // Default: sobrescrever relacoes mais fracas UNKNOWN, MISMATCHING e OVERLAPPING
    }

    // 1) A eh menos geral que B
    table[ a.getOntologicalId() ][ b.getOntologicalId() ] = Relation.LESS_GENERAL;

    // 2) Todo descendente de A eh menos geral que B
    for (Category childA : a.getChildren() ) setLessGeneral(childA,b);

    // 3) A eh menos geral que todo ancestral de B
    for (Category parentB: b.getParents()  ) setLessGeneral(a,parentB);

    // 4) Todo ancestral de A eh sobreposto a B e aos ancestrais de B
    for (Category parentA: a.getParents()  ) setOverlapping(parentA,b);

  }
  //****************************************************************************
  private void setOverlapping(Category a,Category b) {

    // Parar se a relacao atual for tao ou mais forte que a Sobreposicao
    switch ( table[ a.getOntologicalId() ][ b.getOntologicalId() ] ) {
      case EQUIVALENCE  : return;
      case MORE_GENERAL : return;
      case LESS_GENERAL : return;
      case OVERLAPPING  : return;
      // Default: sobrescrever relacoes mais fracas UNKNOWN e MISMATCHING
    }

    // 1) A e B sao sobrepostos
    table[ a.getOntologicalId() ][ b.getOntologicalId() ] = Relation.OVERLAPPING;

    // 2) Todo ancestral de A eh sobreposto a B e a todo ancestral de B
    for (Category parentA: a.getParents()  ) setOverlapping(parentA,b);

    // 3) Todo ancestral de B eh sobreposto a A e a todo ancestral de A
    for (Category parentB: b.getParents()  ) setOverlapping(a,parentB);

  }
  //****************************************************************************
  private void setMismatching(Category a,Category b) {

    // 1) A e B sao sobrepostos
    table[ a.getOntologicalId() ][ b.getOntologicalId() ] = Relation.MISMATCHING;

  }
  //****************************************************************************
  private void setRelation(Category a,Category b,Relation mapping) {

    switch (mapping) {
      case EQUIVALENCE : setEquivalence(a,b); return;
      case MORE_GENERAL: setMoreGeneral(a,b); return;
      case LESS_GENERAL: setLessGeneral(a,b); return;
      case OVERLAPPING : setOverlapping(a,b); return;
      case MISMATCHING : setMismatching(a,b); return;
      default          : table[ a.getOntologicalId() ][ b.getOntologicalId() ] = Relation.UNKNOWN;
    }

  }
  //****************************************************************************
  public Relation get(Category a,Category b) {

    // Para o caso de 'a' oriundo de source e 'b' oriundo de target
    if ( source.contains(a) && target.contains(b) ) {
      return table[ a.getOntologicalId() ][ b.getOntologicalId() ];
    }
    // Para o caso de 'b' oriundo de source e 'a' oriundo de target
    if ( source.contains(b) && target.contains(a) ) {
      if (a.getOntology()==target) return table[ b.getOntologicalId() ][ a.getOntologicalId() ].reverse();
    }
    // Para o caso de 'a' ou 'b' nao serem oriundos de source ou de target
    throw new RuntimeException("A categoria '"+a.getName()+"' ou '"+b.getName()+"' nao pertence as ontologias deste MappingSet.");

  }
  //****************************************************************************
  public Relation getStronger(Category a) {

    // Auxiliar
    Relation stronger = Relation.UNKNOWN;

    // Para o caso de 'a' oriundo de source
    if ( source.contains(a) ) {
      for (Relation r : table[a.getOntologicalId()]) {
        if (r.isEquivalence()         ) return r;
        if (r.isStrongerThan(stronger)) stronger = r;
      }
    }
    // Para o caso de 'a' oriundo de target
    else if ( target.contains(a) ) {
      Relation r;
      for (int i=0; i<table.length; i++) {
        r = table[i][a.getOntologicalId()];
        if (r.isEquivalence()         ) return r;
        if (r.isStrongerThan(stronger)) stronger = r;
      }
    }
    else {
      throw new RuntimeException("Categoria '"+a.getName()+"' nao pertence as ontologias desta MapTable.");
    }
    return stronger;

  }
  //****************************************************************************
  public void set(Category a,Category b,Relation mapping) {

    // Para o caso de 'a' oriundo de source e 'b' oriundo de target
    if ( source.contains(a) && target.contains(b)) {
      setRelation(a,b,mapping);
      return;
    }
    // Para o caso de 'b' oriundo de source e 'a' oriundo de target
    if ( source.contains(b) && target.contains(a)) {
      setRelation(b,a,mapping.reverse());
      return;
    }
    // Para o caso de 'a' ou 'b' nao serem oriundos de source ou de target
    throw new RuntimeException("Categoria '"+a.getName()+"' ou '"+b.getName()+"' nao pertence as ontologias desta MapTable.");

  }
  //****************************************************************************
  public int count(Category a,Relation weaker,Relation stronger) {

    // Auxiliar
    int counter = 0;

    // Para o caso de 'a' oriundo de source
    if ( source.contains(a) ) {
      for (Relation r : table[a.getOntologicalId()] ) {
        if ( r.isWeakerThan(weaker)     ) continue;
        if ( r.isStrongerThan(stronger) ) continue;
        counter++;
      }
    }
    // Para o caso de 'a' oriundo de target
    else if ( target.contains(a) ) {
      Relation r;
      for (int i=0; i<table.length; i++) {
        r = table[i][a.getOntologicalId()].reverse();
        if ( r.isWeakerThan(weaker)     ) continue;
        if ( r.isStrongerThan(stronger) ) continue;
        counter++;
      }
    }
    else {
      // Para o caso de 'a' ou 'b' nao serem oriundos de source ou de target
      throw new RuntimeException("Categoria '"+a.getName()+"' nao pertence as ontologias desta MapTable.");
    }
    return counter;

  }
  //****************************************************************************
  public int count(Category a,Relation relation) {

    // Auxiliar
    int counter = 0;

    // Para o caso de 'a' oriundo de source
    if ( source.contains(a) ) {
      for (Relation r : table[a.getOntologicalId()] ) {
        if ( r==relation ) counter++;
      }
    }
    // Para o caso de 'a' oriundo de target
    else if ( target.contains(a) ) {
      Relation r;
      for (int i=0; i<table.length; i++) {
        r = table[i][a.getOntologicalId()].reverse();
        if ( r==relation ) counter++;
      }
    }
    else {
      // Para o caso de 'a' ou 'b' nao serem oriundos de source ou de target
      throw new RuntimeException("Categoria '"+a.getName()+"' nao pertence as ontologias desta MapTable.");
    }
    return counter;

  }
  //****************************************************************************
  public int[] countEachRelation(Category a) {

    // Auxiliar
    int[] counters = new int[ Relation.values().length ];
    
    // Reset counters
    for (int i=0; i<counters.length; i++) counters[i] = 0;

    // Para o caso de 'a' oriundo de source
    if ( source.contains(a) ) {
      for (Relation r : table[a.getOntologicalId()] ) {
        counters[ r.ordinal() ]++;
      }
    }
    // Para o caso de 'a' oriundo de target
    else if ( target.contains(a) ) {
      Relation r;
      for (int i=0; i<table.length; i++) {
        r = table[i][a.getOntologicalId()].reverse();
        counters[ r.ordinal() ]++;
      }
    }
    else {
      // Para o caso de 'a' ou 'b' nao serem oriundos de source ou de target
      throw new RuntimeException("Categoria '"+a.getName()+"' nao pertence as ontologias desta MapTable.");
    }
    return counters;

  }  
  //****************************************************************************
  public void load(File file)
    throws ParseException
  {

    // Auxiliar
    TextDocument document       = new Tokenizer().lexWhite(file);
    List<String> line           = null;
    Category     sourceCategory = null;
    Category     targetCategory = null;
    boolean      hasDirection   = false;
    int          i;

    // Parse header lines
    for (i=0; i<document.getNumberOfLines(); i++) {
      // Agir conforme o tipo de linha
      line = document.getLine(i);
      if ( line.isEmpty()             ) continue; // Ignorar linha vazia
      if ( line.get(0).startsWith("#")) continue; // Ignorar linha de comentario
      if (!line.get(0).startsWith("@")) break;    // Finalizar a leitura do header ao achar a primeira linha de mapeamento
      // Variavel encontrada
      // Se a variavel for @direction, entao checar se as ontologias no arquivo seriam as mesmas ontologias deste MappingSet
      if ( line.get(0).equals("@direction") ) {
        if ( !source.hasName(line.get(1)) ) throw new ParseException("Linha "+(i-1)+": Ontologia '"+line.get(1)+"' encontrada enquanto se esperava por '"+source.getName()+"'",i-1);
        if ( !target.hasName(line.get(3)) ) throw new ParseException("Linha "+(i-1)+": Ontologia '"+line.get(2)+"' encontrada enquanto se esperava por '"+target.getName()+"'",i-1);
        hasDirection = true;
      }
    }

    // Verificar se a variavel @direction foi setada
    if (!hasDirection) throw new RuntimeException("Variavel @direction nao encontrada em "+file);

    // Parse mapping lines
    while (i<document.getNumberOfLines()) {
      // Next document line
      line = document.getLine(i++);
      // Skip comment line
      if ( line.isEmpty()             ) continue; // Ignorar linha vazia
      if ( line.get(0).startsWith("#")) continue; // Ignorar linha de comentario
      // Parse next mapping attributes
      sourceCategory = source.getCategory( line.get(1) );
      targetCategory = target.getCategory( line.get(2) );
      if (sourceCategory==null) throw new ParseException("Linha "+(i-1)+": Categoria '"+line.get(1)+"' nao encontrada na ontologia '"+source.getName()+"'",i-1);
      if (targetCategory==null) throw new ParseException("Linha "+(i-1)+": Categoria '"+line.get(2)+"' nao encontrada na ontologia '"+target.getName()+"'",i-1);
      // Fill the mapping table
      setRelation(sourceCategory,targetCategory,Relation.parseValue(line.get(0)));
    }

  }
  //****************************************************************************
  public void save(File file)
    throws IOException
  {

    FileWriter writer = new FileWriter(file);
    writer.write(toString());
    writer.close();

  }
  //****************************************************************************
  private void render(StringBuilder builder,Relation relation) {

    // Render the Relation Table
    for (int row=0; row<source.getNumberOfCategories(); row++) {
      for (int col=0; col<target.getNumberOfCategories(); col++) {
        if (!table[row][col].equals(relation)) continue;
        builder.append( table[row][col].toString().toLowerCase()); builder.append(' ' );
        builder.append( source.getCategory(row).getName()       ); builder.append(' ' );
        builder.append( target.getCategory(col).getName()       ); builder.append('\n');
      }
    }

  }  
  //****************************************************************************
  public String toString() {

    // Auxiliar
    StringBuilder builder = new StringBuilder( 40 * source.getNumberOfCategories() * target.getNumberOfCategories() );

    // Create header
    builder.append( "@direction "+source.getName()+" -> "+target.getName()+"\n" );

    // Create body
    // Render the Relation Table
    builder.append("\n# Equivalence list\n"); render(builder,Relation.EQUIVALENCE );
    builder.append("\n# MoreGeneral list\n"); render(builder,Relation.MORE_GENERAL);
    builder.append("\n# LessGeneral list\n"); render(builder,Relation.LESS_GENERAL);
    builder.append("\n# Overlapping list\n"); render(builder,Relation.OVERLAPPING );
    builder.append("\n# Mismatching list\n"); render(builder,Relation.MISMATCHING );
    return builder.toString();

  }
  //****************************************************************************
  public int length() {

    return source.getNumberOfCategories() * target.getNumberOfCategories();

  }
  //****************************************************************************

}