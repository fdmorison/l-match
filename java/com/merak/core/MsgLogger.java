package com.merak.core;

public class MsgLogger {

 //~ Methods //////////////////////////////////////////////////////////////////
 /****************************************************************************/
 private static void printRootCauseStackTrace(Throwable cause) {

   if (cause==null) return;
/*
   while (cause.getCause()!=null) {
     cause = cause.getCause();
   }
 */
   cause.printStackTrace();
   
 } 
 /****************************************************************************/
 public static void print(boolean condition,String source,String msg) {

   if (condition) {
     System.out.println("\n * MSG FROM "+source+"\n * "+msg+"\n\n");
   }

 }
/****************************************************************************/
 public static void print(String source,String msg) {

   System.err.println("\n * MSG FROM "+source+"\n * "+msg+"\n\n");

 }
/****************************************************************************/
 public static void print(boolean condition,String source,Exception msg) {

   if (condition) {
     System.out.println("\n * MSG FROM "+source+"\n * "+msg+"\n\n");
     printRootCauseStackTrace(msg);
   }

 }
/****************************************************************************/
 public static void print(String source,Exception msg) {

   System.err.println("\n * MSG FROM "+source+"\n * "+msg+"\n\n");
   printRootCauseStackTrace(msg);

 }
 /****************************************************************************/
 public static void exit(boolean condition,String source,String msg) {

   if (condition) {
     System.out.println("\n * ERROR FROM "+source+"\n * "+msg+"\n\n");
     System.exit(1);
   }

 }
/****************************************************************************/

}
