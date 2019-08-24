// Package
/////////////////
package com.merak.core;

// Imports
/////////////////
import java.io.*;
import java.util.*;

// Class
/////////////////
public class MultiMap <K,T> {

 //~ Attributes ////////////////////////////////////////////////////////////////
 //*****************************************************************************
 private Map<K,List<T>> map = new HashMap<K,List<T>>();

 //~ Constructors //////////////////////////////////////////////////////////////
 //*****************************************************************************
 public MultiMap() {

   this.map = new HashMap<K,List<T>>();

 }
 //*****************************************************************************
 public MultiMap(int capacity) {

   this.map = new HashMap<K,List<T>>(capacity);

 }

 //~ Methods ///////////////////////////////////////////////////////////////////
 //*****************************************************************************
 public List<T> put(K key,T value) {

   // Buscar o conjunto de key
   List<T> set = map.get(key);

   // Se nao existir conjunto, entao crie um para key
   if ( set == null ) {
     set = new ArrayList<T>();
     map.put(key,set);
   }

   // Adicione o valor ao conjunto
   set.add(value);
   return set;

 }
 //*****************************************************************************
 public List<T> get(K key) {

   return map.get(key);

 }
 //*****************************************************************************
 public int getLength(K key) {

   List<T> set = get(key);
   return (set==null) ? 0 : set.size();

 }
 //*****************************************************************************
 public boolean has(K key,T value) {

   List<T> set = map.get(key);
   return set!=null && set.contains(value);

 }
 //*****************************************************************************
 public boolean has(K key) {

   return map.containsKey(key);

 }
 //*****************************************************************************
 public Set<K> keySet() {

   return map.keySet();

 }
 //*****************************************************************************
 public Collection<List<T>> values() {

   return map.values();

 }
 //*****************************************************************************
 public boolean isEmpty() {

   return map.isEmpty();

 }
 //*****************************************************************************
 public void clear() {

   map.clear();

 }
 //*****************************************************************************
 public int length() {

   return map.size();

 }
 //*****************************************************************************
 public String toString() {

   String string = "";

   for (Map.Entry<K,List<T>> entry : map.entrySet() ) {

     if (entry.getValue().size()<2) continue;

     string += entry.getKey()+" -> ";
     for (T value : entry.getValue()) {
       string += value+" ";
     }
     string += "\n";
   }

   return string;

 }
 //*****************************************************************************
}