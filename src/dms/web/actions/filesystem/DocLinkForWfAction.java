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
 * $Id: DocLinkForWfAction.java,v 20040220.10 2006/03/13 14:19:10 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* adapters package references */
import adapters.beans.SubmitToAdapter;
import adapters.beans.XMLBean;
/* dms package references */
import dms.beans.DbsAttributeValue;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.FolderDocListForm;
import dms.web.beans.crypto.CryptographicUtil;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.DocEventLogBean;
import dms.web.beans.filesystem.FolderDocList;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.FetchPOIds;
import dms.web.beans.utility.ParseXMLTagUtil;
import dms.web.beans.wf.docApprove.InitiateRequisitionWF;
import dms.web.beans.wf.watch.InitiateWatch;
/*Java API */
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;
/* Servlet API */
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Oracle Connection Pool */
import oracle.jdbc.pool.OracleConnectionCacheImpl;
/*Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 * Purpose: Action called to generate link(s) for doc(s)(only) selected from folder-doc list
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 28-04-2005
 * Last Modified Date : 01-03-06
 * Last Modified By   : Suved Mishra
 */

/** The link(s) generated would have userId,userpassword & docId in encrypted form */

public class DocLinkForWfAction extends Action{
  
  /**
   * This is the main action called from the Struts framework.
   * @param mapping The ActionMapping used to select this instance.
   * @param form The optional ActionForm bean for this request.
   * @param request The HTTP Request we are processing.
   * @param response The HTTP Response we are processing.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {

    String target= new String("success");
    //String UserId=new String();
    DbsLibrarySession dbsLibrarySession= null;
    DbsPublicObject dbsPublicObject=null;
    DbsDocument dbsDoc = null;
    HttpSession httpSession= null;    
    ExceptionBean exceptionBean;
    ActionErrors errors=new ActionErrors();
    Locale locale = getLocale(request);
    CryptographicUtil crypto=new CryptographicUtil();
    String cryptoPassword=new String();
    String encryptedString=new String();
    ArrayList DocLinkLists= new ArrayList();
    String userID;
    Logger logger= Logger.getLogger("DbsLogger");
    logger.info("Entering DocLinkForWfAction now... ");
    String relativePath=null;
    String wfAclName = null; 
    try{
        httpSession= request.getSession(false);
        FolderDocListForm folderDocListForm=(FolderDocListForm)form;
        logger.debug("folderDocListForm: "+folderDocListForm);
        wfAclName = folderDocListForm.getHdnWfName();
        logger.debug("wfAclName : "+wfAclName);
        FolderDocInfo folderDocInfo = (FolderDocInfo)
                                      httpSession.getAttribute("FolderDocInfo");
        folderDocInfo.setNoReloadTree(true);
          /*obtain selected doc-ids */
        Long []selectedDocIds= ((Long [])httpSession.getAttribute("selectedDocIds"));
        logger.debug("selectedDocIds length: "+selectedDocIds.length);
        /*obtain userId,userPassword */
        httpSession.removeAttribute("selectedDocIds");
        UserInfo userInfo= (UserInfo)httpSession.getAttribute("UserInfo");
        logger.debug("UserInfo: "+userInfo);
        dbsLibrarySession= userInfo.getDbsLibrarySession();
        userID= dbsLibrarySession.getUser().getName();
        String userPassword=(String)httpSession.getAttribute("password");
        /* obtain contextPath */
        String contextPath = (String)
                    httpSession.getServletContext().getAttribute("contextPath");
        relativePath = contextPath+"WEB-INF"+File.separator+"params_xmls"+
                      File.separator+"Adapters.xml";
        File file = new File(relativePath);
        if( !file.exists() ){
          throw new Exception("Unable to find file Adapters.xml.Kindly contact your System Administrator.");
        }
        XMLBean xmlBean = new XMLBean(relativePath);
        ArrayList allWfNames = xmlBean.getAllNames();
        int allWfsSize = (allWfNames == null)?0:allWfNames.size();
        for( int index = 0 ; index < allWfsSize ; index++ ){
          if( wfAclName.startsWith(xmlBean.getValue((String)allWfNames.get(index),"PrefixName")) ){
            if( xmlBean.getValue((String)allWfNames.get(index),"Enabled").equalsIgnoreCase("False") ){
              throw new Exception("Adapter for selected workflow not enabled. Kindly contact your System Administrator");
            }
          }
        }
        xmlBean = null;
        relativePath = contextPath+"WEB-INF"+File.separator+"params_xmls"+
                      File.separator+"Workflows.xml";
        file = new File(relativePath);
        if( !file.exists() ){
          throw new Exception("Unable to find file Workflows.xml.Kindly contact your System Administrator.");
        }
        xmlBean = new XMLBean(relativePath);
        String approverWfPrefix = xmlBean.getValue("WF_SAM","PrefixName");
        if( wfAclName.startsWith(approverWfPrefix) ){
        
        /*obtain SystemKeyStorePath,passwordFilePath */
        String keyPath=crypto.getSystemKeyStorePath(contextPath);
        //logger.debug("keyPath: "+keyPath);
        String pwdFilePath=crypto.getPwdFilePath(contextPath);
        //logger.debug("pwdFilePath: "+pwdFilePath);
        
        //logger.debug("keyPath and password file paths obtained.");
        
        relativePath= contextPath+"WEB-INF"+File.separator+"params_xmls"+
                      File.separator+"GeneralActionParam.xml";

        ParseXMLTagUtil parseUtil= new ParseXMLTagUtil(relativePath);            

        /*before link generation, check for the existence of .password & .keyStore */
        /*failure, throw an Exception */
        if(!crypto.getSystemKeyStoreFile(contextPath)){
          //target=new String("failure");
          //ActionError actionError=new ActionError("errors.problem.in.linkgeneration");
          //errors.add(ActionErrors.GLOBAL_ERROR,actionError);
          //saveErrors(request,errors);
          Exception fnfEx = new Exception("Problem in link generation.Kindly contact your System Administrator.");
          throw fnfEx;
        }else{
        /*else go ahead to encrypt the userpassword */
        /*obtain cryptoPassword from .password, a byte[]Stream from userId,userpassword,docId and encrypt using 
          crypto.encryptString()*/

          /*obtain an arraylist of doc(s) selected in the form: name,id,txtLinkGenerated */
          for(int counter=0;counter<selectedDocIds.length;counter++){
          
            cryptoPassword=crypto.getCryptoPassword(pwdFilePath);
            String toBeEncrypted="userId="+userID+"&password="+userPassword+"&documentId="+dbsLibrarySession.getPublicObject(selectedDocIds[counter]).getResolvedPublicObject().getId();
            //logger.debug("cryptoPassword in DocGenerateLinkAction : "+cryptoPassword);
            byte[] byteArrayToEncrypt= toBeEncrypted.getBytes();
            //logger.debug("byteArrayToEncrypt: "+new String(byteArrayToEncrypt));
            ByteArrayInputStream inStream= new ByteArrayInputStream(byteArrayToEncrypt);
            //logger.debug("ByteArray To Encrypt "+new String(byteArrayToEncrypt));
            logger.debug("inStream: "+inStream.getClass());
            ByteArrayInputStream enciStream=(ByteArrayInputStream)crypto.encryptString(inStream,userID,cryptoPassword,contextPath);
            int available=enciStream.available();
            logger.debug("available: "+available);
            byte[] byteArrayEncrypted=new byte[available];
            int byteCount=enciStream.read(byteArrayEncrypted,0,available);
            //logger.debug("byteArray1 "+new String(byteArrayEncrypted));
  
            logger.debug("byteCount: "+byteCount);
            enciStream.close();
  
            /*if streamreading has been successful, encode the byte[]Encrypted to encryptedString 
              using Base64 Encoder and then URLEncode the latter */
            if(byteCount == available){
              encryptedString = new sun.misc.BASE64Encoder().encode(byteArrayEncrypted);
              logger.debug("Base64Enc "+ encryptedString);
              String encodedStr=URLEncoder.encode(encryptedString,"UTF-8");
              //request.setAttribute("EncodedString",encodedStr);
  
              /*obtain IP:Port reference from GeneralActionParams.xml */
              //logger.debug("request.getRequestURI(): "+request.getRequestURI());
              
              /*String linkString= parseUtil.getValue("editiporport","Configuration")+request.getRequestURI().replaceAll("docGenerateLinkAction.do","showContentAction.do");
              logger.debug("linkString: "+linkString);*/
              
              /* construct linkString using request.getRequestURL().toString() */
              String linkString= parseUtil.getValue("redirector","Configuration")+
                                 request.getRequestURI().replaceAll(
                                "docLinkForWfAction.do","showContentAction.do");
              logger.debug("linkString: "+linkString);
        
              FolderDocList folderDocList;
              folderDocList= new FolderDocList();          
              dbsPublicObject= dbsLibrarySession.getPublicObject(selectedDocIds[counter]);

              dbsDoc = (DbsDocument)dbsPublicObject.getResolvedPublicObject();
                
              logger.debug("dbsDoc.getId: "+dbsDoc.getId());
              folderDocList.setId(dbsDoc.getId());
    
              logger.debug("dbsDoc.getName: "+dbsDoc.getName());
              folderDocList.setName(dbsDoc.getName());
    
              String linkGenerated =linkString+"?auth="+encodedStr;
              logger.debug("linkGenerated: "+linkGenerated);
              
              folderDocList.setTxtLinkGenerated(linkGenerated);
              DocLinkLists.add(folderDocList);
            
              logger.debug("DocLinkLists.size: "+DocLinkLists.size());
              request.setAttribute("DocLinkLists",DocLinkLists);
            
            for(int i=0;i<DocLinkLists.size();i++){
              logger.debug("Link for "+((FolderDocList)DocLinkLists.get(i)).getName()+" is: "+((FolderDocList)DocLinkLists.get(i)).getTxtLinkGenerated());
            }
            
            
          }else{
            /*if stream reading has been unsuccessful,throw an exception */
            //target=new String("failure");
            Exception srEx = new Exception("Problem in reading stream.");
            throw srEx;
            //saveErrors(request,errors);
          }
        }
        /* submit to wf only if link has been successfully generated */
        if(target.equalsIgnoreCase("success")){
        
          InitiateRequisitionWF initiateWF=new InitiateRequisitionWF(relativePath,wfAclName);
          initiateWF.startReqWF(userID,((FolderDocList)DocLinkLists.get(0)).getTxtLinkGenerated(),dbsPublicObject.getResolvedPublicObject());
          
          if(initiateWF.exBean == null || initiateWF.exBean.getActionErrors() == null){
            DbsAttributeValue docWfStatusAttrValue = dbsPublicObject.getResolvedPublicObject().getAttribute(DbsDocument.WORKFLOW_STATUS);
            if(docWfStatusAttrValue!= null){
              dbsPublicObject.getResolvedPublicObject().setAttribute(DbsDocument.WORKFLOW_STATUS,docWfStatusAttrValue.newAttributeValue("in review"));
              /* logic for logging Wf submission event in doc attribute "AUDIT_LOG" */
              DocEventLogBean logBean = new DocEventLogBean();
              logBean.logEvent(dbsLibrarySession,dbsPublicObject.getResolvedPublicObject().getId(),"Doc Submitted To WorkFlow ");
            
            }else{
              logger.debug("docWfStatusAttrValue is null");
            }
            // code for watch wf submission goes here
            if( relativePath!=null && userID!=null ){
              String actionForWatch = "You are receiving this mail because you have applied watch on "+dbsDoc.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : Document submitted to Workflow for Approval "; 
              InitiateWatch iniWatch = new InitiateWatch(relativePath,userID,
                                              actionForWatch,dbsDoc.getId());
              iniWatch.startWatchProcess();
              actionForWatch = null;
            }
            
            ActionMessages actionMessages = new ActionMessages();
            ActionMessage actionMessage = new ActionMessage("msg.document.submitworkflow.successfully");
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
            httpSession.setAttribute("ActionMessages",actionMessages);
          }else{
            httpSession.setAttribute("ActionErrors",initiateWF.exBean.getActionErrors());
          }
        }
      }
    }else{
      /* for adapter workflows */
      FetchPOIds fetchIds = new FetchPOIds(dbsLibrarySession);
      /* fetch documentIds from selected folders and documents */
      selectedDocIds = fetchIds.getDocIds(selectedDocIds,true);
      /* if there are documents to be submitted, go ahead */
      if( selectedDocIds != null && selectedDocIds.length!=0 ){
        /* obtain path for "Adapters.xml" */
        String adapterFilePath = httpSession.getServletContext().getRealPath("/")+
                                 "WEB-INF"+File.separator+"params_xmls"+
                                 File.separator+"Adapters.xml";
        /* obtain connection pool from context */
        OracleConnectionCacheImpl wfConnCache = (OracleConnectionCacheImpl)
                httpSession.getServletContext().getAttribute("wfConnCache");
        /* submit to SubmitToAdapter with appropriate parameters */
        SubmitToAdapter toAdapter = new SubmitToAdapter(adapterFilePath,wfConnCache);
        toAdapter.submit(dbsLibrarySession.getUser().getName(),wfAclName,selectedDocIds);
        /* set up action message for successful submission */
        logger.debug("Items submitted to corresponding workflow...");
        ActionMessages actionMessages = new ActionMessages();
        ActionMessage actionMessage = new ActionMessage("msg.document.submitworkflow.successfully");
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
        httpSession.setAttribute("ActionMessages",actionMessages);
        
      }else{
        /* else, when there are no documents to submit, set up a message */
        ActionMessages actionMessages = new ActionMessages();
        ActionMessage actionMessage = new ActionMessage("msg.document.not.in.folder");
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
        httpSession.setAttribute("ActionMessages",actionMessages);
      }
      
    }
    }catch(DbsException dex){
      exceptionBean= new ExceptionBean(dex);
      logger.error("An Exception occurred in DocLinkForWfAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean= new ExceptionBean(ex);
      logger.error("An Exception occurred in DocLinkForWfAction... ");
      logger.error(exceptionBean.getErrorTrace());
      httpSession.setAttribute("ActionErrors",exceptionBean.getActionErrors());
    }
    logger.info("Exiting DocLinkForWfAction now... ");
    return mapping.findForward(target);
  }
}
