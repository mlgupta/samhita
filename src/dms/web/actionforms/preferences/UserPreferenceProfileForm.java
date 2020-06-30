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
 * $Id: UserPreferenceProfileForm.java,v 20040220.11 2006/03/16 07:41:31 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.preferences;
//Struts API
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;
/**
 *	Purpose: To store the values of the html controls of
 *  userPreferenceProfileForm in user_preference_profile.jsp
 * 
 *  @author              Rajan Kamal Gupta
 *  @version             1.0
 * 	Date of creation:    16-02-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  27-12-2004. 
 */ 
public class UserPreferenceProfileForm extends ValidatorForm {
    private int txtPermittedTreeAccessLevel;
    private int txtItemsToBeDisplayedPerPage;
    private int cboNavigationType;
    private String[] cboGroup; 
    private String cboSelectTheme;     
    private boolean boolChkOpenDocInNewWin; /* checkbox control variable */
    private String[] lstWatchDocumentName;
    private String[] lstWatchDocumentID;
    private String hdnSelectedDocID;
    
    /**
     * Purpose   : Returns the cboSelectTheme.
     * @return String
     */
    public String getCboSelectTheme() {
        return cboSelectTheme;
    }

   /**
    * Purpose   : Sets the name of cboSelectTheme.
    * @param newcboSelectTheme Value of cboSelectTheme
    */
    public void setCboSelectTheme(String newCboSelectTheme) {
        cboSelectTheme = newCboSelectTheme;
    }

    /**
     * Purpose   : Returns the permittedTreeAccessLevel.
     * @return   : String
     */
    public int getTxtPermittedTreeAccessLevel() {
        return txtPermittedTreeAccessLevel;
    }

   /**
    * Purpose   : Sets the name of permittedTreeAccessLevel.
    * @param    : newPermittedTreeAccessLevel Value of permittedTreeAccessLevel
    */
    public void setTxtPermittedTreeAccessLevel(int newTxtPermittedTreeAccessLevel) {
        txtPermittedTreeAccessLevel = newTxtPermittedTreeAccessLevel;
    }

    /**
     * Purpose   : Returns the itemsToBeDisplayedPerPage.
     * @return   : String
     */
    public int getTxtItemsToBeDisplayedPerPage() {
        return txtItemsToBeDisplayedPerPage;
    }

   /**
    * Purpose   : Sets the name of itemsToBeDisplayedPerPage.
    * @param    : newItemsToBeDisplayedPerPage Value of itemsToBeDisplayedPerPage
    */
    public void setTxtItemsToBeDisplayedPerPage(int newTxtItemsToBeDisplayedPerPage) {
        txtItemsToBeDisplayedPerPage = newTxtItemsToBeDisplayedPerPage;
    }

    /**
     * Purpose   : Returns the NavigationType.
     * @return   : String
     */
    public int getCboNavigationType() {
        return cboNavigationType;
    }

   /**
    * Purpose   : Sets the name of NavigationType.
    * @param    : newCboNavigationType Value of NavigationType
    */
    public void setCboNavigationType(int newCboNavigationType) {
        cboNavigationType = newCboNavigationType;
    }

    /**
     * Purpose   : Returns the group.
     * @return   : String Array
     */
    public String[] getCboGroup() {
        return cboGroup;
    }

   /**
    * Purpose   : Sets the name of group.
    * @param    : newGroup Value of group
    */
    public void setCboGroup(String[] newCboGroup) {
        cboGroup = newCboGroup;
    }
    

    /**
     * Purpose  : Returns boolean Open Document Option.
     * @return  : boolean checkbox status.
     */
    public boolean getBoolChkOpenDocInNewWin(){
        return boolChkOpenDocInNewWin;      
    }

    /**
     * Purpose  : Sets boolean Open Document Option
     * @param   : boolean checkbox status.
     */
    public void setBoolChkOpenDocInNewWin(boolean boolChkOpenDocInNewWin){
        this.boolChkOpenDocInNewWin=boolChkOpenDocInNewWin;
        
    }

    public String[] getLstWatchDocumentName() {
      return lstWatchDocumentName;
    }
  
    public void setLstWatchDocumentName(String[] lstWatchDocumentName) {
      this.lstWatchDocumentName = lstWatchDocumentName;
    }

    public String[] getLstWatchDocumentID() {
      return lstWatchDocumentID;
    }

    public void setLstWatchDocumentID(String[] lstWatchDocumentID) {
      this.lstWatchDocumentID = lstWatchDocumentID;
    }   

    public String getHdnSelectedDocID()  {
      return hdnSelectedDocID;
    }
  
    public void setHdnSelectedDocID(String hdnSelectedDocID)  {
      this.hdnSelectedDocID = hdnSelectedDocID;
    }
    
    public String toString(){
        String strTemp = "";
        Logger logger = Logger.getLogger("DbsLogger");
        if(logger.getLevel() == Level.DEBUG){

            String strArrayValues = "";
            strTemp += "\n\ttxtPermittedTreeAccessLevel : " + txtPermittedTreeAccessLevel;
            strTemp += "\n\ttxtItemsToBeDisplayedPerPage : " + txtItemsToBeDisplayedPerPage;            

            if(cboGroup != null){
                strArrayValues = "{";
                for(int index = 0; index < cboGroup.length; index++){
                    strArrayValues += " " + cboGroup[index];
                }
                strArrayValues += "}";
                strTemp += "\n\tcboGroup : " + strArrayValues;
            }else{
                strTemp += "\n\tcboGroup : " + cboGroup;
            }
            strTemp += "\n\tboolChkOpenDocInNewWin : "+boolChkOpenDocInNewWin;
        }
        return strTemp;
    }
}
