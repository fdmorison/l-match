// Package
/////////////////
package com.merak.ontomap.mapping;

// Imports
/////////////////
import java.util.*;
import java.text.ParseException;

// Implementation
/////////////////
public enum Relation {

  //~ Attributes ///////////////////////////////////////////////////////////////
  //****************************************************************************
  UNKNOWN,MISMATCHING,OVERLAPPING,LESS_GENERAL,MORE_GENERAL,EQUIVALENCE;

  //~ Methods //////////////////////////////////////////////////////////////////
  //****************************************************************************
  public static Relation parseValue(String relation)
    throws ParseException
  {

    try {
      return valueOf( relation.toUpperCase() );
    }
    catch (Exception ex) {
      throw new ParseException("Relacao nao existe: '"+relation+"'",0);
    }

  }
  //****************************************************************************
  public Relation reverse() {

    if (this==LESS_GENERAL) return MORE_GENERAL;
    if (this==MORE_GENERAL) return LESS_GENERAL;
    return this;

  }
  //****************************************************************************
  public boolean isUnknown() {

    return this == UNKNOWN;

  }
  //****************************************************************************
  public boolean isMismatching() {

    return this == MISMATCHING;

  }
  //****************************************************************************
  public boolean isOverlapping() {

    return this == OVERLAPPING;

  }
  //****************************************************************************
  public boolean isLessGeneral() {

    return this == LESS_GENERAL;

  }
  //****************************************************************************
  public boolean isMoreGeneral() {

    return this == MORE_GENERAL;

  }
  //****************************************************************************
  public boolean isEquivalence() {

    return this == EQUIVALENCE;

  }
  //****************************************************************************
  public boolean isStrongerThan(Relation other) {

    return this.ordinal() > other.ordinal();

  }
  //****************************************************************************
  public boolean isWeakerThan(Relation other) {

    return this.ordinal() < other.ordinal();

  }
  //****************************************************************************
  public boolean isEqualOrStrongerThan(Relation other) {

    return this.ordinal() >= other.ordinal();

  }
  //****************************************************************************
  public boolean isEqualOrWeakerThan(Relation other) {

    return this.ordinal() <= other.ordinal();

  }
  //****************************************************************************

}