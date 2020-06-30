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
 * $Id: GetNotifications.java,v 1.3 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.wf.docApprove;
/* dms package references */
import dms.web.beans.utility.ConnectionBean;
/* Java API */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
/* Struts API */
import org.apache.log4j.Logger;
/**
 *	Purpose:             Bean to obtain notifications .
 *  @author              Maneesh Mishra 
 *  @version             1.0
 * 	Date of creation:    04-01-2005
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class GetNotifications  {

  ConnectionBean conn=null;
  Statement notificationSt=null;
  Logger logger = Logger.getLogger("DbsLogger");
  private static String DATE_FORMAT="MM/DD/YYYY";//"MM/dd/yyyy HH:mm:ss z"; //also present in JobListBean
  public int pageCount=1;
  
  
  public GetNotifications(String relativePath) {
     conn= new ConnectionBean(relativePath) ;
     //logger.debug("relativePath: "+relativePath);
     notificationSt=conn.getStatement();
  }
  
  public ArrayList getNotificationData(String user,String status,String fromBeginDate,String toBeginDate,String fromEndDate,String toEndDate,int pageNumber,int numRecords){
    ArrayList notificationList=null;
    ArrayList notificationListToReturn=new ArrayList();
    ResultSet notificationRset=null;
    try{
      String dateString="";
      String statusString="";
        if(fromBeginDate!=null && !fromBeginDate.equals("")){
           dateString+=" AND BEGIN_DATE > to_date('"+fromBeginDate+"','"+DATE_FORMAT+"' )";
        }
       if(toBeginDate!=null && !toBeginDate.equals("")){
           dateString+=" AND BEGIN_DATE < to_date('"+toBeginDate+"','"+DATE_FORMAT+"' )";
        }
        if(fromEndDate!=null && !fromEndDate.equals("")){
           dateString+=" AND END_DATE > to_date('"+fromEndDate+"','"+DATE_FORMAT+"' )";
        }
        if(toEndDate!=null && !toEndDate.equals("")){
           dateString+=" AND BEGIN_DATE < to_date('"+toEndDate+"','"+DATE_FORMAT+"' )";
        }
        
        if(status != null && !status.equals("ALL")){
              statusString=" AND WfNotifications.STATUS = '"+status+"'";  
        }
        
        
              
        // Query distinct Flight Codes from the Flight_Departures Table
        /*NOTIFICATION_ID STATUS CONTEXT BEGIN_DATE END_DATE SUBJECT
         * 
         * 
         * 
         * 
         */
        String sql="Select MESSAGE_NAME, NOTIFICATION_ID, STATUS ,CONTEXT, BEGIN_DATE, END_DATE, SUBJECT"
                    +" From OWF_MGR.WF_NOTIFICATIONS WfNotifications, "
                    +" OWF_MGR.WF_ITEM_TYPES_TL WfItemTypes Where "
                    +" WfNotifications.MESSAGE_TYPE = WfItemTypes.NAME AND"
                    +" WfNotifications.TO_USER ='"+user+"' "
                    + statusString
                    + dateString
                    +" Order By WfNotifications.BEGIN_DATE DESC ";
        logger.debug(sql);        
       /*             String sql="select * from tab";*/
        notificationRset = notificationSt.executeQuery(sql);
        if(notificationRset == null){
          logger.debug("notificationRset is set to null...");
        }
      
        // Loop through all the records fetched in the result Set
        // and store in the flightcodes array
        logger.debug("Before List");
       
        notificationList=new ArrayList();
        
        while(notificationRset.next()) {
           Hashtable notificationData = new Hashtable();
           
           if(notificationRset.getString("MESSAGE_NAME")!=null){
              notificationData.put("MESSAGE_NAME",notificationRset.getString("MESSAGE_NAME"));
           }
           
           if(notificationRset.getBigDecimal("NOTIFICATION_ID")!=null){
              notificationData.put("NOTIFICATION_ID",notificationRset.getBigDecimal("NOTIFICATION_ID"));
           }
           if(notificationRset.getString("CONTEXT")!=null){
              notificationData.put("CONTEXT",notificationRset.getString("CONTEXT"));
           }
           if(notificationRset.getString("SUBJECT")!=null){
              notificationData.put("SUBJECT",notificationRset.getString("SUBJECT"));
           }
           if(notificationRset.getString("STATUS")!=null){
              notificationData.put("STATUS",notificationRset.getString("STATUS"));
           }
           if(notificationRset.getDate("BEGIN_DATE")!=null){
              notificationData.put("BEGIN_DATE",notificationRset.getDate("BEGIN_DATE"));
           }
           if(notificationRset.getDate("END_DATE")!=null){
               notificationData.put("END_DATE",notificationRset.getDate("END_DATE"));
           }
           notificationList.add(notificationData);          
          
        }
        logger.debug("After List");
        
        int length = notificationList.size();
        
            if(length!=0){
                int startIndex=(pageNumber*numRecords) - numRecords;
                int endIndex=(startIndex + numRecords) - 1;
                if(endIndex >= length){
                    endIndex=length-1;
                }                
                for (int index=0; startIndex <= endIndex; ){
                    notificationListToReturn.add(notificationList.get(startIndex));                    
                    startIndex++;
                }
            }
            int itemCount=length;
            if(itemCount!=0){
                int pageMod=itemCount%numRecords;
                if(pageMod==0){
                    this.pageCount=itemCount/numRecords;
                   // logger.debug("form sj page count0="+pageCount);
                }else{
                    this.pageCount=(itemCount/numRecords) + 1 ;
                   // logger.debug("form sj page count1="+pageCount);
                }
            }
      
        // Initialize the Combo Box which holds flight codes
        
      } catch(SQLException ex) { // Trap SQL errors
        ex.printStackTrace();
      } finally {
        try {
          notificationRset.close();
          logger.debug("notificationRset closed in getNotifications.");
          if(conn != null) {
            conn.closeStatement();
            logger.debug("Statement closed in getNotifications.");
            conn.closeConnection();
            logger.debug("Connection closed in getNotifications.");
          }
        } catch(Exception ex) {
            logger.error("Exception thrown in finally..."+ex.getMessage());
            ex.printStackTrace();
        }  
    
  }
  return notificationListToReturn;
}
/*public static void main(String args[]){
  GetNotifications gc=new GetNotifications("/home/ias/dbsentry/dms/public_html/WEB-INF/params_xmls/GeneralActionParam.xml");
 // gc.getNotificationData("BLEWIS",null,null,null,null,null);
}*/
}
