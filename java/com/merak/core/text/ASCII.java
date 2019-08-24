package com.merak.core.text;

public class ASCII {

 //~ Inner Classes /////////////////////////////////////////////////////////////
 //*****************************************************************************
 public static class NewLine {

   public static char   EOL0 = '\n';
   public static char   EOL1 = '\r';
   public static String EOL2 = "\r\n";

 }
 //*****************************************************************************
 public static class Special {

   public static char NUL   =  0;  // null
   public static char SOH   =  1;  // start of heading
   public static char STX   =  2;  // start of text
   public static char ETX   =  3;  // end of text
   public static char EOT   =  4;  // end of transmission
   public static char ENQ   =  5;  // enquiry
   public static char ACK   =  6;  // acknowledge
   public static char BEL   =  7;  // bell
   public static char BS    =  8;  // backspace
   public static char TAB   =  9;  // horizontal tab
   public static char LF    = 10;  // NL line feed, new line
   public static char VT    = 11;  // vertical tab
   public static char FF    = 12;  // NP form feed, new page
   public static char CR    = 13;  // carriage return
   public static char SO    = 14;  // shift out
   public static char SI    = 15;  // shift in
   public static char DLE   = 16;  // data link escape
   public static char DC1   = 17;  // device control 1
   public static char DC2   = 18;  // device control 2
   public static char DC3   = 19;  // device control 3
   public static char DC4   = 20;  // device control 4
   public static char NAK   = 21;  // negative acknowledge
   public static char SYN   = 22;  // synchronous idle
   public static char ETB   = 23;  // end of transmission block
   public static char CAN   = 24;  // cancel
   public static char EM    = 25;  // end of medium
   public static char SUB   = 26;  // substitute
   public static char ESC   = 27;  // escape
   public static char FS    = 28;  // file separator
   public static char GS    = 29;  // group separator
   public static char RS    = 30;  // record separator
   public static char US    = 31;  // unit separator
   public static char SPACE = 32;  // blank space
   public static char DEL   = 127; // delete

 }

 //~ Methods ///////////////////////////////////////////////////////////////////
 //*****************************************************************************
 public static boolean isUpperCase(char c) {

   return c>='A' && c<='Z';

 }
 //*****************************************************************************
 public static boolean isLowerCase(char c) {

   return c>='a' && c<='z';

 }
 //*****************************************************************************
 public static boolean isLetter(char c) {

   return (c>='A' && c<='Z') || (c>='a' && c<='z');

 }
 //*****************************************************************************
 public static boolean isDigit(char c) {

   return c>='0' && c<='9';

 }
 //*****************************************************************************
 public static boolean isLetterOrDigit(char c) {

   return (c>='A' && c<='Z') || (c>='a' && c<='z') || (c>='0' && c<='9');

 }
 //*****************************************************************************


}