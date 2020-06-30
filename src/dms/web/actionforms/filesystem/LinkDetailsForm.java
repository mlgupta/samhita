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
 * $Id: LinkDetailsForm.java,v 20040220.5 2006/03/17 12:23:13 suved Exp $
 *****************************************************************************
 */
package dms.web.actionforms.filesystem;
/* Java API */
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
/**
 *	Purpose: To store the values of the html controls of obtain_link_limits.jsp.
 *  @author             Suved Mishra
 *  @version            1.0 
 * 	Date of creation:   12-12-2005
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */
public class LinkDetailsForm extends ActionForm  {
  private String[] txtLinkLimit;              /* numeric limit [] for links */
  private String[] txtDateTimeName;           /* date limit [] for links */
  private String[] combotext;                 /* store selected download limits*/
  private String [] documentName;             /* store document names */
  private String hdnTimeZone;                 /* timezone as hidden value */
  private Long[] txtDocId;                    /* docs whose links be generated */
  private Long [] chkSelect;                  /* docs which are chkd for no limits */ 
  private Map dateTimeMap = new HashMap();    /* hashmap to store date limits */
  private Map linkLimitMap = new HashMap();   /* hashmap to store numeric limits */
  private Map docIdsMap = new HashMap();      /* hashmap to store docIds */
  private Map chkDocIdsMap = new HashMap();   /* hashmap to store chkd docIds */
  private Map editLinkMap = new HashMap();    /* hashmap to store selected download limits */
  private Map documentNameMap = new HashMap();/* hashmap to store document names */
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

  public String getTxtDateTimeName(int index) {
    return (String) dateTimeMap.get( new Integer( index ) );
  }

  public void setTxtDateTimeName(int index ,String newTxtDateTimeName) {
      dateTimeMap.put( new Integer( index ), newTxtDateTimeName );
  }

  public String[] getTxtDateTimeNames(){
    return (String[]) dateTimeMap.values().toArray( new String[dateTimeMap.size()] );
  }

  public String getTxtLinkLimit(int index) {
    return (String) linkLimitMap.get( new Integer( index ) );
  }

  public void setTxtLinkLimit(int index ,String newTxtLinkLimit) {
      linkLimitMap.put( new Integer( index ), newTxtLinkLimit );
  }

  public String[] getTxtLinkLimits(){
    return (String[]) linkLimitMap.values().toArray( new String[linkLimitMap.size()] );
  }

  public Long getTxtDocId(int index) {
    return (Long) docIdsMap.get( new Integer( index ) );
  }

  public void setTxtDocId(int index ,Long newDocId) {
      docIdsMap.put( new Integer( index ), newDocId );
  }

  public Long[] getTxtDocIds(){
    return (Long[]) docIdsMap.values().toArray( new Long[docIdsMap.size()] );
  }

  public Long getChkSelect(int index) {
    return (Long) chkDocIdsMap.get( new Integer( index ) );
  }

  public void setChkSelect(int index ,Long newDocId) {
      chkDocIdsMap.put( new Integer( index ), newDocId );
  }

  public Long[] getChkSelects(){
    return (Long[]) chkDocIdsMap.values().toArray( new Long[chkDocIdsMap.size()] );
  }

  public String getHdnTimeZone() {
    return hdnTimeZone;
  }

  public void setHdnTimeZone(String timeZone) {
    this.hdnTimeZone = timeZone;
  }
  
  public String toString(){
    int index = (txtLinkLimit == null)?0:txtLinkLimit.length;
    Logger logger = Logger.getLogger("DbsLogger");
    String strTemp = null;
    strTemp += "\n\tTimeZone : "+this.getHdnTimeZone();
    return strTemp;
  }

  public String getCombotext(int index) {
    return (String) editLinkMap.get( new Integer( index ) );
  }

  public void setCombotext(int index ,String newCombotext) {
      editLinkMap.put( new Integer( index ), newCombotext );
  }

  public String[] getCombotexts(){
    return (String[]) editLinkMap.values().toArray( new String[editLinkMap.size()] );
  }

  public String getDocumentName( int index ){
    return (String)documentNameMap.get(new Integer(index));
  }

  public String[] getDocumentNames() {
    return (String[]) documentNameMap.values().toArray(new String[documentNameMap.size()]);
  }

  public void setDocumentName(int index , String documentName) {
    this.documentNameMap.put(new Integer(index),documentName);
  }

}