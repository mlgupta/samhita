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
 * $Id: SetForwardFrom.java,v 1.2 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.wf.docApprove;
/* Java API */
import java.math.BigDecimal;
/* Workflow API */
import oracle.apps.fnd.common.ErrorStack;
import oracle.apps.fnd.wf.WFAttribute;
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFFunctionAPI;
import oracle.apps.fnd.wf.WFTwoDArray;
/**
 *	Purpose:             To set forward from for approval workflow .
 *  @author              Maneesh Mishra 
 *  @version             1.0
 * 	Date of creation:    05-05-2005
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class SetForwardFrom extends WFFunctionAPI {

  WFTwoDArray dataSource = null;
  BigDecimal myNid=null;

  public boolean execute(WFContext pWCtx){
    ErrorStack es = pWCtx.getWFErrorStack(); 
    try{
      loadItemAttributes(pWCtx); 
      loadActivityAttributes(pWCtx, itemType, itemKey, actID); 
      WFAttribute requestor = new WFAttribute(); 
      WFAttribute forward_from = new WFAttribute();
      WFAttribute forward_to = new WFAttribute();
        //lAAttr = getActivityAttr("REQUESTOR"); 
      requestor = getItemAttr("REQUESTOR"); 
      forward_from = getItemAttr("FORWARD_FROM");
      forward_to = getItemAttr("FORWARD_TO");
      
      forward_from.value((Object)forward_to.getValue());
      setItemAttrValue(pWCtx, forward_from); 
    }catch (Exception e)  { 
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