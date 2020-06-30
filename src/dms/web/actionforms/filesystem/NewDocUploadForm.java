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
 * $Id: NewDocUploadForm.java,v 1.4 2006/03/16 07:06:42 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.filesystem;
/*java API*/
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
/*Struts API*/
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
/**
 * Purpose: To store the values of the html controls of
 *           doc_upload_new.jsp. 
 * 
 * @author              Suved Mishra
 * @version             1.0 
 * Date of creation:   13-12-2004
 * Last Modified by :     
 * Last Modified Date:    
 */
public class NewDocUploadForm extends ActionForm {
    private String txtPath;
    private Map fileMap = new HashMap();
    private Map fileDescMap = new HashMap();
    private FormFile fleFile;
    private String txaFileDesc;
    
    /**
     * Reset all properties to their default values.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     */
      public void reset(ActionMapping mapping, HttpServletRequest request) {
          super.reset(mapping, request);
      }

      /**
       * Validate all properties to their default values.
       * @param mapping The ActionMapping used to select this instance.
       * @param request The HTTP Request we are processing.
       * @return ActionErrors A list of all errors found.
       */
      public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
          return super.validate(mapping, request);
      }

      public String getTxtPath() {
          return txtPath;
      }

      public void setTxtPath(String newTxtPath) {
          txtPath = newTxtPath;        
      }

      public FormFile getFleFile(int index) {
        return (FormFile) fileMap.get( new Integer( index ) );
      }

      public void setFleFile(int index ,FormFile newFleFile) {
          fileMap.put( new Integer( index ), newFleFile );
      }

      public FormFile[] getFleFiles(){
        return (FormFile[]) fileMap.values().toArray( new FormFile[fileMap.size()] );
      }

      public String getTxaFileDesc(int index) {
        return  (String) fileDescMap.get( new Integer( index ) );
      }
      public String[] getTxaFileDescs() {
        return  (String[]) fileDescMap.values().toArray( new String[fileDescMap.size()] );
      }

      public void setTxaFileDesc(int index ,String newTxaFileDesc) {
        fileDescMap.put( new Integer( index ), newTxaFileDesc );
      }
    
   
      public String toString(){
        String strTemp = "";
        Logger logger = Logger.getLogger("DbsLogger");
        if(logger.getLevel() == Level.DEBUG){
            strTemp += "\n\ttxtPath : " + txtPath;            
        }
        return strTemp;
    }


}
