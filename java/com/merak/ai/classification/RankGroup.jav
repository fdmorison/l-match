// Package
///////////////
package com.merak.ai.classification;

// Imports
///////////////
import java.util.*;
import com.merak.core.text.*;

public class RankGroup {

  //~ Attributes ///////////////////////////////////////////////////////////////
  /****************************************************************************/
  private String name  = null;
  private Rank[] group = null;

  //~ Constructors /////////////////////////////////////////////////////////////
  /****************************************************************************/
  public RankGroup(String name,Rank[] group) {

    this.name  = name;
    this.group = group;

  }
  /****************************************************************************/
  public RankGroup(String name,List<Rank> group) {

    this.name  = name;
    this.group = group.toArray( new Rank[group.size()] );

  }  

  //~ Methods //////////////////////////////////////////////////////////////////
  /****************************************************************************/
  public String getName() {

    return name;

  }
  /****************************************************************************/
  public Rank get(int i) {

    return group[i];

  }
  /****************************************************************************/
  public void set(int i,Rank rank) {

    group[i] = rank;

  }  
  /****************************************************************************/
  public Rank[] getAll() {

    return group;

  }
  /****************************************************************************/
  public void sortByFirstClassName() {

    // Estabelecendo um comparador para ordenar os ranks do grupo pelo nome da
    // primeira classe de cada rank, isto e, rank[0].identifier
    Comparator<Rank> c = new Comparator<Rank>() {
        public int compare(Rank a,Rank b) {
          String s1 = a.get(0).identifier;
          String s2 = b.get(0).identifier;
          return s1.compareTo(s2);
        }
    };
    
    // Ordenando
    Arrays.sort(group,c);

  }
  /****************************************************************************/
  public void sortByFirstClassPriority() {

    Comparator<Rank> c = new Comparator<Rank>() {
        public int compare(Rank a,Rank b) {
          double p1 = a.get(0).value;
          double p2 = b.get(0).value;
          if (p1>p2) return  1;
          if (p1<p2) return -1;
          return 0;
        }
    };
    Arrays.sort(group,c);

  }
  /****************************************************************************/
  public void sortByRankName() {

    Comparator<Rank> c = new Comparator<Rank>() {
        public int compare(Rank a,Rank b) {
          String s1 = a.getName();
          String s2 = b.getName();
          return s1.compareTo(s2);
        }
    };
    Arrays.sort(group,c);

  }
  /****************************************************************************/
  public String toString() {

    RenderTable table = new RenderTable();
    Row         row   = null;
    Rank        rank  = null;
    int         i,j;

    // 2) Para cada rank no grupo, faça
    for (i=0; i<group.length; i++) {
      // Crie uma linha para o rank: o nome do rank fica na primeira célula e do grupo na segunda
      rank = group[i];
      row  = table.createRow();
      row.createCell( rank.getName() );
      row.createCell( name           );
      // Para cada classe no rank, faça
      for (j=0; j<rank.length(); j++) {
        // Crie uma célula para a classe
        row.createCell( rank.get(j).identifier );
        row.createCell( rank.get(j).value + "" );
      }
    }

    // Renderize a tabela e retorne o texto resultante
    return table.toString();

  }
  /****************************************************************************/
  public boolean isEmpty() {

    return group.length == 0;

  }
  /****************************************************************************/
  public int length() {

    return group.length;

  }
  /****************************************************************************/

}

