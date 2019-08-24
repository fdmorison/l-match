package com.merak.core;

public class Interval {

 //~ Attributes ////////////////////////////////////////////////////////////////
 //*****************************************************************************
 public int offset = 0;
 public int endset = 0;

 //~ Constructors //////////////////////////////////////////////////////////////
 //*****************************************************************************
 public Interval(int offset) {

   this(offset,offset);

 }
 //*****************************************************************************
 public Interval(int offset,int endset) {

   this.offset = offset ;
   this.endset = endset ;

 }
 //*****************************************************************************
 public Interval(String interval) {

   String[] array = interval.split("-");
   this.offset    = Integer.parseInt(array[0]);
   this.endset    = Integer.parseInt(array[1]);

 }

 //~ Methods ///////////////////////////////////////////////////////////////////
 //*****************************************************************************
 public int length() {

   return endset - offset;

 }
 //*****************************************************************************
 public String toString() {

   return offset+"-"+endset;

 }
 //*****************************************************************************

}