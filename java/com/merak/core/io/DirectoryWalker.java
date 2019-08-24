// Package
///////////////
package com.merak.core.io;

// Imports
///////////////
import java.io.*;

public abstract class DirectoryWalker {

  //~ Constructors /////////////////////////////////////////////////////////////
  //~ Methods //////////////////////////////////////////////////////////////////
  /****************************************************************************/
  protected abstract void directoryFound(File directory,File parent,int level) throws Exception;

  /****************************************************************************/
  protected abstract void directoryClosed(File directory,File parent,int level) throws Exception;

  /****************************************************************************/
  protected abstract void fileFound(File file,File parent,int level) throws Exception;

  /****************************************************************************/
  private void depthFirstWalk(File directory,File parent,int level)
    throws Exception
  {

    // Firing directory found event
    directoryFound(directory,directory,level++);

    // Loading directory entries
    File[] entries = directory.listFiles();

    // Firing file found events
    int i,j;
    for (i=j=0; i<entries.length; i++) {
      if (entries[i].isDirectory()) {
        entries[j++] = entries[i];
      }
      else {
        fileFound(entries[i],directory,level);
      }
    }

    // Walking throught the directory file hierarchy
    for (i=0; i<j; i++) {
      depthFirstWalk(entries[i],directory,level);
    }

    // Firing directory closed event
    directoryClosed(directory,parent,level-1);

  }
  /****************************************************************************/
  public void startDepthFirstWalk(File directory,boolean skipRoot)
    throws Exception
  {

    // Checando se o parametro realmente se refere a um diretorio
    if (!directory.isDirectory()) {
      throw new IOException(directory + " não é um diretório.");
    }

    if (!skipRoot) { depthFirstWalk(directory,null,0); return; }

    File[] entries = directory.listFiles();
    for (int i=0; i<entries.length; i++) {
      if (entries[i].isDirectory()) depthFirstWalk(entries[i],directory,1);
    }

  }
  /****************************************************************************/
  public void startDirectWalk(File directory)
    throws Exception
  {

    // Checando se o parametro realmente se refere a um diretorio
    if (!directory.isDirectory()) {
      throw new IOException(directory + " não é um diretório.");
    }

    File[] entries = directory.listFiles();
    for (int i=0; i<entries.length; i++) {
      if (entries[i].isFile()) fileFound(entries[i],directory,0);
    }

  }
  /****************************************************************************/

}














