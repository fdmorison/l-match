// Package
///////////////
package com.merak.core;

// Imports
///////////////
import java.util.*;

// Class
///////////////
public class ShellParameter extends NamedObject implements Comparable<ShellParameter> {

 //~ Attributes ////////////////////////////////////////////////////////////////
 //*****************************************************************************
 private ShellParameterSet container;
 private String            description;
 private String            possibleValues;
 private String            defaultValue;
 private String            example;
 private String            value;
 private boolean           isBoolean;
 private List<String>      shortcuts;

 //~ Constructors //////////////////////////////////////////////////////////////
 //*****************************************************************************
 public ShellParameter(String name,boolean isBoolean,String description,ShellParameterSet container) {

   // Super Attribute Initialization
   super(name);

   // Attribute Initialization
   this.container      = container;
   this.isBoolean      = isBoolean;
   this.description    = description;
   this.possibleValues = null;
   this.defaultValue   = null;
   this.example        = null;
   this.value          = null;
   this.shortcuts      = new LinkedList<String>();

 }
 //~ Methods ///////////////////////////////////////////////////////////////////
 //*****************************************************************************
 public String getDescription() {

   return description;

 }
 //*****************************************************************************
 public String getDefaultValue() {

   return defaultValue;

 }
 //*****************************************************************************
 public String getPossibleValues() {

   return possibleValues;

 }
 //*****************************************************************************
 public String getExample() {

   return example;

 }
 //*****************************************************************************
 public String getValue() {

   return (value==null) ? defaultValue : value;

 }
 //*****************************************************************************
 public List<String> getShortcuts() {

   return shortcuts;

 }
 //*****************************************************************************
 public boolean isBoolean() {

   return isBoolean;

 }
 //*****************************************************************************
 public boolean isNull() {

   return value == null;

 }
 //*****************************************************************************
 public boolean hasValue(String value) {

   return (this.value == null) ? false : this.value.equals(value);

 }
 //*****************************************************************************
 public void setPossibleValues(String defaultValue,String possibleValues) {

   this.defaultValue   = defaultValue;
   this.possibleValues = possibleValues;

 }
 //*****************************************************************************
 public void setExample(String example) {

   this.example = example;

 }
 //*****************************************************************************
 public void setValue(String value) {

   this.value = value;

 }
 //*****************************************************************************
 public void addShortcut(char shortcut) {

   String key = String.valueOf(shortcut);
   shortcuts.add(key);
   container.addShortcut(key,this);

 }
 //*****************************************************************************
 public int compareTo(ShellParameter parameter) {

   return getName().compareTo(parameter.getName());

 }
 //*****************************************************************************

}