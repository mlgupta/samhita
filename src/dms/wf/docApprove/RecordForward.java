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
 * $Id: RejectRequisition.java,v 1.4 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.wf.docApprove;
/* Java API */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
/* Oracle API */
import oracle.apps.fnd.common.ErrorStack;
import oracle.apps.fnd.wf.WFAttribute;
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFFunctionAPI;
/**
 *	Purpose:             To record forward for approval workflow .
 *  @author              Maneesh Mishra 
 *  @version             1.0
 * 	Date of creation:    27-04-2005
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class RecordForward extends WFFunctionAPI {

  public boolean execute(WFContext pWCtx){
  ErrorStack es = pWCtx.getWFErrorStack(); 
  try{
    WFAttribute lAAttr = new WFAttribute(); 
    WFAttribute lIAttr = new WFAttribute(); 
    loadActivityAttributes(pWCtx, itemType, itemKey, actID); 
    loadItemAttributes(pWCtx); 
    lIAttr = getItemAttr("REQUESTOR"); 
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter("c:\\outfilename.txt"));
      out.write((String)lIAttr.getValue());
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }catch (Exception e){ 
    es.addMessage("WF","WF_FN_ERROR"); 
    es.addToken("MODULE",this.getClass().getName()); 
    es.addToken("ITEMTYPE",itemType); 
    es.addToken("ITEMKEY",itemKey); 
    es.addToken("ACTID",actID.toString()); 
    es.addToken("FUNCMODE",funcMode); 
    es.addToken("ERRMESSAGE",e.getMessage()); 
    return false;
  }
  return true; 
  }
}