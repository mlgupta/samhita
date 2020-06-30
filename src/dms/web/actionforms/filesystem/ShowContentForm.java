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
 * $Id: ShowContentForm.java,v 1.6 2006/03/16 07:07:36 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.filesystem;
/*java API */
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
/*Struts API */
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
/**
 *	Purpose: To Store control values of doc_generate_url.jsp
 *  @author              Suved Mishra
 *  @version             1.0
 * 	Date of creation:    13-01-2005
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class ShowContentForm extends ActionForm{ 

  private boolean showContent=true; // dummy variable
  private String[] txtLinkGenerated;
  private Map linkMap = new HashMap();
  private Map idMap = new HashMap();
  private String[] txtDocId;
  
  public void setShowContent(boolean showContent){
    this.showContent=showContent;
  }

  public boolean isShowContent(){
    return showContent;
  }

  public String[] getTxtLinkGenerated() {
      return txtLinkGenerated;
  }

  public void setTxtLinkGenerated(String[] newTxtLinkGenerated) {
      txtLinkGenerated = newTxtLinkGenerated;
  }
  
  public String getTxtLinkGenerated(int index) {
    return (String) linkMap.get( new Integer( index ) );
  }

  public void setTxtLinkGenerated(int index ,String newTxtLinkGenerated) {
      linkMap.put( new Integer( index ), newTxtLinkGenerated );
  }

  public String[] getTxtLinkGenerateds(){
    return (String[]) linkMap.values().toArray( new String[linkMap.size()] );
  }
  
  public String toString(){
    String strTemp=new String();
    strTemp+=new Boolean(showContent).toString();
    return strTemp;
  }
  /**
   * Reset all properties to their default values.
   * @param mapping The ActionMapping used to select this instance.
   * @param request The HTTP Request we are processing.
   */
  public void reset(ActionMapping mapping, HttpServletRequest request)
  {
    super.reset(mapping, request);
  }

  /**
   * Validate all properties to their default values.
   * @param mapping The ActionMapping used to select this instance.
   * @param request The HTTP Request we are processing.
   * @return ActionErrors A list of all errors found.
   */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
    return super.validate(mapping, request);
  }

  public String[] getTxtDocId() {
    return txtDocId;
  }

  public void setTxtDocId(String[] txtDocId) {
    this.txtDocId = txtDocId;
  }
  
  public String getTxtDocId(int index) {
    return (String) idMap.get( new Integer( index ) );
  }

  public void setTxtDocId(int index ,String newTxtDocId) {
      idMap.put( new Integer( index ), newTxtDocId );
  }

  public String[] getTxtDocIds(){
    return (String[]) idMap.values().toArray( new String[idMap.size()] );
  }
  
}
