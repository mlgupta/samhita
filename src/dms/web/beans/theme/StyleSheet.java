/*
 *****************************************************************************
 *                       Confidentiality Information                         *
 *                                                                           *
 * This module is the confidential and proprietary information of            *
 * DBSentry Corp.; it is not to be copied, reproduced, or transmitted in any *
 * form, by any means, in whole or in part, nor is it to be used for any     *
 * purpose other than that for which it is expressly provided without the    *
 * written permission of DBSentry Corp.                                      *
 *                                                                           *
 * Copyright (c) 2004-2005 DBSentry Corp.  All Rights Reserved.              *
 *                                                                           *
 *****************************************************************************
 * $Id: StyleSheet.java,v 1.8 2006/03/14 05:44:57 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.theme;
/**
 *	Purpose: To Create A Style Sheet file of a given File Name & Path and Array of StyleClass
 *  @author              Sudheer Pujar
 *  @version             1.0
 * 	Date of creation:   09-03-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
//Java  Import
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
//Logger Import
import org.apache.log4j.Logger;

public class StyleSheet  {

  private String fileName;
  private String physicalPath;
  private ArrayList elements;
  private static Logger logger = Logger.getLogger("DbsLogger");  

  /**
   * Purpose : Contructs a Style Sheel Object for Given File Name, Physical Path and StyleElements
   * @param fileName - A String object 
   * @param physicalPath - A String object 
   * @param elements - An ArrayList  object 
   */
  public StyleSheet(String fileName, String physicalPath, ArrayList elements ) {
      this.fileName = fileName.toLowerCase();
      this.physicalPath = physicalPath;
      this.elements =elements;
      logger.debug("Style Sheet Object Created");
  }

  /**
   * Purpose : To Creates Style Sheet File 
   * @return boolean value true or false 
   */
  public boolean create()throws IOException,Exception{
    String filePath = this.physicalPath + this.fileName;
    StringBuffer elementsBuffer = new StringBuffer();
  
    boolean created=false;
    try{
      logger.info("Entering" + this.getClass().getName() + ".create()" );
      for(int i=0;i< this.elements.size();i++){
        elementsBuffer.append(this.elements.get(i).toString());
      }

      this.delete();
      logger.debug("Call Made to " + this.getClass().getName() + ".delete()" );
      
      created= writeToFile(elementsBuffer,filePath,false);
      
      logger.debug("Exiting" + this.getClass().getName() + ".create()" );
    }catch(IOException e){
      logger.error("IOException Caught @ " + this.getClass().getName() + ".create()" );
      throw e;
    }catch(Exception e){
      logger.error("Exception Caught @ " + this.getClass().getName() + ".create()" );
      throw e;
    } 
    return created;
  }

  /**
   * Purpose : To Deletes Style Sheet File 
   * @return boolean value true or false 
   */
  public boolean delete() throws Exception{
    String filePath = this.physicalPath + this.fileName;
    File file = new File(filePath);
    boolean deleted=false;
    try{
      logger.info("Entering" + this.getClass().getName() + ".delete()" );
      if(file.exists()){
        logger.debug(filePath + " Exists");
        deleted=file.delete();
        if(deleted){
          logger.debug(filePath + " Deleted");
        }else{
          logger.debug("Unable to Delete " + filePath );
        }
      }else{
        logger.debug(filePath + " does not Exists");
      }
      logger.info("Exiting" + this.getClass().getName() + ".delete()" );  
    }catch(Exception e){
      logger.error("Exception Caught @ " + this.getClass().getName() + ".delete()" );
      throw e;
    } 
    return deleted;    
  }


  /**
  * Purpose : Creates given file with a given String
  * @param singleBuffer - A StringBuffer object defines the content for the file 
  * @param fileName - A String object defines fullpath with name of file
  * @param append - A boolean data defines whether to append the file or overwrite  
  * @return A booean data results file created or not
  */
  private boolean  writeToFile(StringBuffer singleBuffer, String fileName, boolean append)throws IOException,Exception{
    FileOutputStream out; 
    PrintStream p; 
    String toFile = singleBuffer.toString();
    try{
      logger.info("Entering" + this.getClass().getName() + ".writeToFile()" );  
      out = new FileOutputStream(fileName,append);
      p = new PrintStream( out );
      if(toFile.endsWith("\n"))
        p.println (toFile.substring(0,toFile.lastIndexOf("\n")));   
      else
        p.println (toFile);   
      p.close();
      logger.info("Exiting" + this.getClass().getName() + ".writeToFile()" );  
    }catch(IOException e){
      logger.error("IOException Caught @ " + this.getClass().getName() + ".writeToFile()" );
      throw e;
    }catch(Exception e){
      logger.error("Exception Caught @ " + this.getClass().getName() + ".writeToFile()" );
      throw e;
    } 
      return true;
          
  } 
  
}
