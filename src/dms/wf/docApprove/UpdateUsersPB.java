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
 * $Id: UpdateUsersPB.java,v 1.3 2006/03/13 16:16:12 suved Exp $
 *****************************************************************************
 */
package dms.wf.docApprove;
/* dms package references */
import dms.beans.DbsAccessControlList;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsCleartextCredential;
import dms.beans.DbsDirectoryUser;
import dms.beans.DbsException;
import dms.beans.DbsLibraryService;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPrimaryUserProfile;
import dms.beans.DbsProperty;
import dms.beans.DbsPropertyBundle;
import dms.beans.DbsSelector;
import dms.beans.DbsSortSpecification;
import dms.web.beans.utility.ParseXMLTagUtil;
/* Java API */
import java.io.File;
/**
 *	Purpose:             To update user's workflow acl permission bundle to 
 *                       store multiple acls .
 *  @author              Suved Mishra 
 *  @version             1.0
 * 	Date of creation:    04-01-2006
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */

public class UpdateUsersPB  {
  private String domain;        /* parameters for starting a dbsLibraryService*/
  private String ifsService;
  private String ifsSchemaPassword;
  private String serviceConfiguration;
  private String sysAdminUser;
  private String sysAdminPwd;
  DbsLibraryService dbsLibraryService;                /* dbsLibraryService */
  private DbsLibrarySession dbsLibrarySession=null;           /* dbsLibrarySession */
  DbsCleartextCredential dbsCleartextCredential=null; /* cleartextCredentials */
  DbsDirectoryUser directoryUser = null;        /* system admin DU */
  
  public UpdateUsersPB() {
    this.domain = "ifs://user1.dbsentry.com:1521:cmsdk:CMSDK";
    this.ifsService = "IfsDefaultService";
    this.ifsSchemaPassword = "cmsdk";
    this.serviceConfiguration = "SmallServiceConfiguration";
    this.sysAdminUser = "system";
    this.sysAdminPwd = "system";
    System.out.println("Constructor successfully initialized!!!");
  }

  public UpdateUsersPB( String domain,String ifsService,String ifsSchemaPassword,String serviceConfiguration,String sysAdminUser,String sysAdminPwd ) {
    this.domain = domain;
    this.ifsService = ifsService;
    this.ifsSchemaPassword = ifsSchemaPassword;
    this.serviceConfiguration = serviceConfiguration;
    this.sysAdminUser = sysAdminUser;
    this.sysAdminPwd = sysAdminPwd;
    System.out.println("Constructor successfully initialized!!!");
  }

  /**
   * Purpose: to establish credentials inorder to fetch WfAcl
   * @return: true,if credentials established,else false.
   */
  private boolean establishCredentials(){
    try{        
      dbsCleartextCredential = new DbsCleartextCredential(sysAdminUser,sysAdminPwd);
      if(DbsLibraryService.isServiceStarted(ifsService)){
          System.out.println(ifsService + " is running");
          dbsLibraryService = DbsLibraryService.findService(ifsService);
      }else{
        System.out.println("Starting Library Service...");            
    
        dbsLibraryService = DbsLibraryService.startService(ifsService,ifsSchemaPassword,serviceConfiguration,domain);
        System.out.println("Library Service Started ");
      }
        
      try{
        dbsLibrarySession = dbsLibraryService.connect(dbsCleartextCredential,null);
      }catch(DbsException dex){
        System.err.println(dex.getMessage());
        dex.getErrorMessage();
        dex.printStackTrace();                
        if(dex.containsErrorCode(10170)){
         throw dex;
        }
        else if(dex.getErrorCode() == 21008){
          System.err.println("This exception is thrown when library service started successfully");
          System.err.println("and then database went down");
          System.err.println("In this case Library Service need to be restarted");
  
          System.out.println("Disposing Library Service...");
          dbsLibraryService.dispose(ifsSchemaPassword);
          System.out.println("Disposing Library Service Complete");
          
          System.out.println("ReStarting Library Service...");            
          dbsLibraryService = DbsLibraryService.startService(ifsService,ifsSchemaPassword,serviceConfiguration,domain);
          System.out.println("ReStarting Library Service Complete");
          dbsLibrarySession = dbsLibraryService.connect(dbsCleartextCredential,null);
        }
      }
      System.out.println("User logged in successfully");    
    }catch(DbsException dex){
        System.err.println("User login unsuccessful");
        System.err.println("DbsException: "+dex.getMessage());
        dex.printStackTrace();
        return false;
    }catch(Exception ex)  {
        System.err.println("User login unsuccessful");
        System.err.println("Exception: "+ex.getMessage());
        ex.printStackTrace();
        return false;
    }
    return true;
  }
  
  /**
   * Purpose : To Search Directory Users.
   * @param  : userName - String.
   * @param  : pageNumber - int.
   * @param  : numRecords - int.
   * @return : users - An Array of directory User.
   */
  private DbsDirectoryUser[] listUsers(String userName,int pageNumber,int numRecords) throws DbsException{
    DbsDirectoryUser users[]=new DbsDirectoryUser[0];
    if( dbsLibrarySession != null ){
      try{
        dbsLibrarySession.setAdministrationMode(true);
        DbsSortSpecification dbsSortSpecification = new DbsSortSpecification();
        DbsSelector dbsSelector = new DbsSelector(dbsLibrarySession);
        dbsSelector.setSearchClassname(DbsDirectoryUser.CLASS_NAME);
        dbsSortSpecification.addSortQualifier(DbsDirectoryUser.NAME_ATTRIBUTE, true);
        //dbsSelector.setSortSpecification(dbsSortSpecification);
        if(userName==null){
            userName="%";
        }else{
            userName=userName+"%";
        }
        userName = userName.trim();
        userName=userName.toUpperCase();
        userName = userName.replace('*','%');
        userName = userName.replace('?','_');
        
        dbsSelector.setSearchSelection("upper(name) Like"+"'"+userName+"' order by upper(name)");
    
        int length = dbsSelector.getItemCount();
        if(length!=0){
          if( pageNumber!= -1 && numRecords!=-1 ){
            int startIndex=(pageNumber*numRecords) - numRecords;
            int endIndex=(startIndex + numRecords) - 1;
            if(endIndex >= length){
                endIndex=length-1;
            }
            users=new DbsDirectoryUser[(endIndex-startIndex)+1];
            for (int index=0; startIndex <= endIndex; ){
                users[index++]=(DbsDirectoryUser)dbsSelector.getItems(startIndex);
                startIndex++;
            }
          }else{
            users=new DbsDirectoryUser[length];
            for (int index=0; index < length; index++ ){
                users[index]=(DbsDirectoryUser)dbsSelector.getItems(index);
            }
          }
        }
      }catch(DbsException dbsException) {
        System.err.println("listUsers : " + dbsException.getErrorMessage());
      }
    }
    return users;
  }

  /**
   * Purpose : To Search Access Control List.
   * @param  : userName - String.
   * @param  : pageNumber - int.
   * @param  : numRecords - int.
   * @param  : forUser - boolean.
   * @return : acls - An Array of Access Control List.
   */
  private DbsAccessControlList[] listAcls(String aclName,int pageNumber,int numRecords,boolean forUser) throws DbsException{
    DbsAccessControlList acls[]=new DbsAccessControlList[0];
    try{   
      DbsSelector dbsSelector = new DbsSelector(dbsLibrarySession);
      DbsSortSpecification dbsSortSpecification = new DbsSortSpecification();                                                 
      if(forUser){
          dbsSelector.setSearchClassname("SYSTEMACCESSCONTROLLIST");
      }else{
          dbsSelector.setSearchClassname(DbsAccessControlList.CLASS_NAME);   
      }
      dbsSortSpecification.addSortQualifier(DbsAccessControlList.NAME_ATTRIBUTE, true); 
//            dbsSelector.setSortSpecification(dbsSortSpecification);
      if(aclName==null){
          aclName="%";
      }else{
          aclName=aclName + "%";
      }
      aclName = aclName.trim();
      aclName = aclName.toUpperCase();                
      aclName = aclName.replace('*','%');
      aclName = aclName.replace('?','_');                
//                dbsSelector.setSearchSelection("name LIKE "+"'"+aclName+"%'");
      dbsSelector.setSearchSelection("upper(name) LIKE "+"'"+aclName+"' order by upper(name)");

      int length = dbsSelector.getItemCount();   
      if(length!=0){
        if( pageNumber!=-1 && numRecords!=-1 ){
          int startIndex=(pageNumber*numRecords) - numRecords;
          int endIndex=(startIndex + numRecords) - 1;
          if(endIndex >= length){
              endIndex=length-1;
          }
          acls=new DbsAccessControlList[(endIndex-startIndex)+1];
          for (int index=0; startIndex <= endIndex; ){
              acls[index++]=(DbsAccessControlList)dbsSelector.getItems(startIndex);
              startIndex++;      
          }
        }else{
          acls=new DbsAccessControlList[1];
          acls[0]=(DbsAccessControlList)dbsSelector.getItems(0);
        }
      }  
    }catch(DbsException dbsException){
      System.err.println("listAcls : " + dbsException.getErrorMessage());
    }
    return acls;
  }

  public void setUsersWfProperty(){

    DbsDirectoryUser users[]=null;
    DbsPrimaryUserProfile userProfile = null;
    DbsPropertyBundle defaultAcls = null;
    DbsProperty wfAclProperty = null;
    DbsAccessControlList [] dbsAcls = null;
    DbsAttributeValue dbsAttrVal = null;
    DbsPropertyBundle defPB = null;
    
    try{
      if( establishCredentials() ){
        users = listUsers(null,-1,-1);
        if( users!=null && users.length!=0 ){
          for( int index = 0; index < users.length; index++ ){
            try{
              System.out.println("userName : "+users[index].getDistinguishedName());
              userProfile = users[index].getPrimaryUserProfile();
              defaultAcls = userProfile.getDefaultAcls();
              if( defaultAcls != null ){
                wfAclProperty = defaultAcls.getProperty("WORKFLOWACL");
                dbsAcls = listAcls("wf_approvers",-1,-1,false);
                users[index].putProperty("WORKFLOWACL",DbsAttributeValue.newAttributeValue(dbsAcls));                
              }else{
                dbsAcls = listAcls("wf_approvers",-1,-1,false);
                users[index].putProperty("WORKFLOWACL",DbsAttributeValue.newAttributeValue(dbsAcls));                
                defPB = userProfile.getPropertyBundle();
                userProfile.setDefaultAcls(defPB);
              }
            }catch(DbsException dbsEx) {
              dbsEx.printStackTrace();
            }
          }
        }
      }
    }catch( DbsException dbsEx ){
      System.err.println("DbsError : "+dbsEx.toString());
      dbsEx.printStackTrace();
    }catch( Exception ex ){
      System.err.println("DbsError : "+ex.toString());
      ex.printStackTrace();
    }finally{
        if( dbsLibrarySession!= null ){
          closeThisWfSession();
        }
    }
  }


  /**
   * Purpose: to close the Wf oriented session
   * @param : none
   * @return: none 
   */
  
  private void closeThisWfSession(){
    System.out.println("Disconnecting librarySession...");
    try{    
      if( dbsLibrarySession!=null && dbsLibrarySession.isConnected()){
        System.out.println("dbsLibrarySession id: "+dbsLibrarySession.getId()+" will now be disconnected");
        dbsLibrarySession.disconnect();  /* disconnect this Wf library session */
      }
    }catch( DbsException dex){
      System.out.println("DbsException: "+dex.getMessage());
      dex.printStackTrace();
    }
  }
  
  /*public static void main( String [] args ){
    String relativePath= (String)context.getAttribute("contextPath")+"WEB-INF"+File.separator+"params_xmls"+File.separator+"GeneralActionParam.xml";
    ParseXMLTagUtil parseUtil= new ParseXMLTagUtil(relativePath);            
    String ifsService;
    String ifsSchemaPassword;
    String serviceConfiguration;
    String domain;
    ifsService = parseUtil.getValue("IfsService","Configuration"); 
    ifsSchemaPassword = parseUtil.getValue("IfsSchemaPassword","Configuration");
    serviceConfiguration = parseUtil.getValue("ServiceConfiguration","Configuration");
    domain = parseUtil.getValue("Domain","Configuration");
    String sysAdminUser = parseUtil.getValue("sysaduser","Configuration");
    String sysAdminPwd = parseUtil.getValue("sysadpwd","Configuration");
    UpdateUsersPB updatePb = new UpdateUsersPB(domain,ifsService,ifsSchemaPassword,serviceConfiguration,sysAdminUser,sysAdminPwd);
    updatePb.setUsersWfProperty();
    
  }*/
}