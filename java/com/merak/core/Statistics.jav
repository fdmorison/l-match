// Package
///////////////
package com.merak.ontomap.classification;

// Imports
///////////////
import java.util.*;

public class Statistics {

  //~ Class Attributes /////////////////////////////////////////////////////////
  /****************************************************************************/
  private static final double LN_2 = Math.log( 2.0d);

  //~ Methods //////////////////////////////////////////////////////////////////
  /****************************************************************************/
  public static double getEntropy(double[] array) {
    /*
                   |                                                         |
                   |   probabilidades[i]+1         |  probabilidades[i]+1  | |
      -1* sum(0,N) |  ---------------------  * log | --------------------- | |
                   |  NormalizingConstant+N        | NormalizingConstant+N | |
                   |                                                         |
          ---------------------------------------------------------------------
                                        log( N )
    */
    if (array.length<2) return 1.0d;

    double norm = array.length + getSum(array);
    double pi   = 0.0d;
    double sum  = 0.0d;

    for (int i=0; i<array.length; i++) {
       pi   = (1.0d + array[i]) / norm;
       sum += pi * log2(pi);
    }

    return -1 * ( sum / log2(array.length) );

  }
  /****************************************************************************/
  public static double getEntropy(double[] array,int offset) {

    // Auxiliar
    int length = array.length - offset;
    if (length<2) return 1.0d;

    double normalizingConstant = length + getSum(array,offset);
    double pi                  = 0.0d;
    double sum                 = 0.0d;

    for (int i=offset; i<array.length; i++) {
       pi   = (1.0d + array[i])/normalizingConstant;
       sum += pi * log2(pi);
    }

    return -1 * ( sum / log2(length) );

  }
  /****************************************************************************/
  public static double getNorm(double[] array) {

    double sum = 0.0d;
    for (int i=0; i<array.length; i++) {
       sum += array[i] * array[i];
    }
    return Math.sqrt(sum);

  }
  /****************************************************************************/
  public static double getNoisyOr(double[] array) {

    double product = 1.0d;
    for (int i=0; i<array.length; i++) product *= 1.0d - array[i];
    return 1.0d - product;

  }
  /****************************************************************************/
  public static double getNoisyAnd(double[] array) {

    double product = 1.0d;
    for (int i=0; i<array.length; i++) product *= array[i];
    return product;

  }
  /****************************************************************************/
  public static double getHarmonicMean(double[] array) {

    // Auxiliar
    double sum     = 0;
    double product = 1;

    // Measure harmonic mean
    for (int i=0; i<array.length; i++) {
      product *= array[i];
      sum     += array[i];
    }
    return ( array.length * product) / (sum+1);

  }
  /****************************************************************************/
  public static double getSigmoidFunction(double linearCombination) {

    if (linearCombination==0) return 0.5;

    return 1 / (1 + 1 * ( 1 / Math.pow(Math.E,linearCombination) ));

  }
  /****************************************************************************/
  public static int getSum(int[] array) {

    int sum = 0;
    for (int i=0; i<array.length; i++) sum += array[i];
    return sum;

  }
  /****************************************************************************/
  public static double getSum(double[] array) {

    double sum = 0.0d;
    for (int i=0; i<array.length; i++) {
       sum += array[i];
    }
    return sum;

  }
  /****************************************************************************/
  public static double getSum(double[] array,int offset) {

    double sum = 0.0d;
    for (int i=offset; i<array.length; i++) {
       sum += array[i];
    }
    return sum;

  }
  /****************************************************************************
  public static double getSum(double[] array,double weight) {

    double sum = 0.0d;
    for (int i=0; i<array.length; i++) {
       sum += weight * array[i];
    }
    return sum;

  }
  /****************************************************************************
  public static double getSum(double[] array,double[] weight) {

    double sum = 0.0d;
    for (int i=0; i<array.length; i++) {
       sum += weight[i] * array[i];
    }
    return sum;

  }
  /****************************************************************************/
  public static double getAverage(double[] array) {

    return getSum(array) / array.length;

  }
  /****************************************************************************/
  public static double ln(double n) {

    return Math.log(n);

  }
  /****************************************************************************/
  public static double log(double n,double base) {

    return Math.log(n) / Math.log(base) ;

  }
  /****************************************************************************/
  public static double log2(double n) {

    return Math.log(n) / LOG_2 ;

  }
  /****************************************************************************/
  public static double log10(double n) {

    return Math.log10(n);

  }
  /****************************************************************************/

}
