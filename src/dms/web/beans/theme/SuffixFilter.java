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
 * $Id: SuffixFilter.java,v 20040220.6 2006/03/14 05:44:57 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.theme;
/* Java API */
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
/* Logger API */
import org.apache.log4j.Logger;

/**
 *	Purpose: To get the File Names from a given Physical Directory and Array of file extensions.
 * 
 *  @author              Mishra Maneesh
 *  @version             1.0
 * 	Date of creation:   23-12-2003
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */

public class SuffixFilter implements FilenameFilter {

    private String suffix;

    /**
     * Purpose   : To create object of  SuffixFilter
     * @param    : suffix - String object represents File Extension
     */
    public SuffixFilter(String suffix) {        
        this.suffix = suffix;
    }
    /**
     * Purpose   : To override the interface method
     * @param    : dir - File Object represents directory
     * @param    : name - String object represents File name
     * @return   : boolean True or False
     */

    public boolean accept(File dir, String name) {
        return name.endsWith("." + suffix);
        
    }

    /**
     * Purpose   : To List the File names of given Physical Directory and Array of file extensions
     * @param    : suffix  - An Array of String Object represents Array of file extensions
     * @param    : folderPath - String object represents Physical Directory
     * @return   : ArrayList Object containing list of files in the form of String Objects
     */

    public static ArrayList listFiles(String suffix[],String folderPath) throws Exception{    
        ArrayList fileList = new ArrayList();
        Logger logger = null;
        logger = Logger.getLogger("DbsLogger");
        try{

            logger.info("Entering dms.web.beans.theme.SuffixFilter.listFiles()");
            File dir = new File(folderPath);
            int noOfFiles=0;
            if(dir.exists()){
                logger.debug("Files are : ");
                for(int suffixIndex=0;suffixIndex< suffix.length;suffixIndex++){
                    String[] files = dir.list(new SuffixFilter(suffix[suffixIndex]));        
                    noOfFiles=noOfFiles+files.length;
                    for (int i = 0; i < files.length; i++){            
                        logger.debug(files[i]);
                        fileList.add(files[i]);
                    }
                }
                logger.debug("Total " + noOfFiles + " Files Listed ");
            }else{
              logger.debug(folderPath + "No Such Directory Exist ");
            }
            
        }catch(Exception e){
          logger.error("Exception Caught @ dms.web.beans.theme.SuffixFilter.listFiles()");
          throw e;
        }
        logger.info("Exiting dms.web.beans.theme.SuffixFilter.listFiles()");
        return fileList;
    }
}
