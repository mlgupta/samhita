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
 * $Id: FindSubscribers.java,v 1.2 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.wf.watch;
/* dms package references */
import dms.web.beans.utility.ConnectionBean;
import dms.web.beans.wf.watch.WatchMailer;
/* Java API */
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
/* Oracle API */
import oracle.apps.fnd.common.ErrorStack;
import oracle.apps.fnd.wf.WFAttribute;
import oracle.apps.fnd.wf.WFContext;
import oracle.apps.fnd.wf.WFFunctionAPI;
/**
 *	Purpose:             To find out subscribers for watch functionality.
 *  @author              Suved Mishra 
 *  @version             1.0
 * 	Date of creation:    04-01-2006
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class FindSubscribers extends WFFunctionAPI{
  ConnectionBean connBean = null;
  Statement stmt = null;
  ArrayList subscribers = null;
  String [] subscribersNames = null;
  WatchMailer watchMailer = null;
  
  public boolean execute( WFContext pWfCtx ){
    ErrorStack es = pWfCtx.getErrorStack();
    try{
      /* wfattributes */
      WFAttribute lAAttr = new WFAttribute(); 
      WFAttribute relativePath = new WFAttribute(); 
      WFAttribute poId = new WFAttribute();
      WFAttribute userName = new WFAttribute();
      WFAttribute actionMsg = new WFAttribute();
      /* load activity and item attrs */
      loadActivityAttributes(pWfCtx, itemType, itemKey, actID); 
      loadItemAttributes(pWfCtx);
      /* set relativePath and poId from itemAttrs */
      relativePath = getItemAttr("REL_PATH"); 
      poId = getItemAttr("PO_ID");
      userName = getItemAttr("USER_NAME");
      actionMsg = getItemAttr("ACT_MESSAGE");
      /* obtain db connection,execute query,fetch results */
      connBean = new ConnectionBean((String)relativePath.getValue());
      stmt = connBean.getStatement(true);
      Long po_Id = new Long((String)poId.getValue());
      //System.out.println("po_Id value in FindSubscribers : "+po_Id.longValue());
      String query = "SELECT USER_ID FROM watch_pos WHERE PO_ID="+po_Id+"ORDER BY USER_ID";
      ResultSet rSet = null;
      try{
        rSet = stmt.executeQuery(query);
        if( rSet != null ){
          subscribers = new ArrayList();
          while( rSet.next() ){
            subscribers.add(rSet.getString("USER_ID"));
          }
        }else{
          return false;
        }
        subscribersNames = getSubscribers();
        int length = (subscribersNames == null)?0:subscribersNames.length;
        System.out.println("subscribersNames Length : "+length);
        for( int index =0; index < length; index++ ){
          System.out.println("subscribersNames["+index+"]: "+subscribersNames[index]);
        }
        if( length !=0 ){
          watchMailer = new WatchMailer(subscribersNames,po_Id,(String)relativePath.getValue(),(String)userName.getValue(),(String)actionMsg.getValue());
          System.out.println("WatchMailer initialized in FindSubscribers");
        }else{
          System.out.println("No subcribers found...");
          return false;
        }
        if(watchMailer.mailForWatch()){
          System.out.println("Mails sent successfully");
          String mailMsg= ((String)actionMsg.getValue());
          if( mailMsg.endsWith("has been deleted") ){
            watchMailer.deleteEntries();
          }
          return true;
        }else{
          System.out.println("Error in sending mails");
        }
      }catch (Exception e) {
        es.addMessage("WF","WF_FN_ERROR"); 
        es.addToken("MODULE",this.getClass().getName());
        es.addToken("ITEMTYPE",itemType); 
        es.addToken("ITEMKEY",itemKey);
        es.addToken("ACTID",actID.toString());
        es.addToken("FUNCMODE",funcMode); 
        es.addToken("ERRMESSAGE",e.getMessage());
        return false;
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
    }finally{
      connBean.closeStatement();
      connBean.closeConnection();
    }
    return false; 
  }
 
 /* function to fetch names of subscribers for the PO watch */   
  public String[] getSubscribers(){
    if( subscribers!= null ){
      String [] subscribersArray = new String[subscribers.size()];
      for( int index = 0; index < subscribers.size(); index++ ){
        subscribersArray[index] = (String)subscribers.get(index);
      }
      return subscribersArray;
    }
    return null;
  }

}
