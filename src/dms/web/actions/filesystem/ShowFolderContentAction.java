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
 * $Id: ShowFolderContentAction.java,v 1.0 2006/06/05 14:06:57 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import adapters.beans.LoginBean;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsException;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.ShowContentForm;
import dms.web.beans.crypto.CryptographicUtil;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.DateHelperForFileSystem;
import dms.web.beans.filesystem.FolderDoc;
import dms.web.beans.filesystem.FolderDocInfo;
import dms.web.beans.filesystem.FolderDocList;
import dms.web.beans.filesystem.UserDetailsBean;
import dms.web.beans.user.UserInfo;
import dms.web.beans.user.UserPreferences;
import dms.web.beans.utility.FetchPOIds;
import dms.web.beans.utility.GeneralUtil;
import dms.web.beans.utility.ParseXMLTagUtil;
/* Java API */
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * Purpose: Action called to display doc(s) within the folder from link(s) 
 *          generated in doc_generate_url.jsp
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 05-06-06
 * Last Modified Date : 
 * Last Modified By   : 
 */
/** The link(s) would be decrypted and after appropriate validation, the docs 
 *  would be displayed */
public class ShowFolderContentAction extends Action  {
private static String DATE_FORMAT="MM/dd/yyyy HH:mm:ss"; 
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
  boolean validSession=false;
  Logger logger= Logger.getLogger("DbsLogger");
  UserInfo userInfo=null;
  UserPreferences userPreferences = null; 
  FolderDocInfo folderDocInfo = null; 
  ServletContext context;
  HttpSession httpSession = null;  
  DbsLibrarySession dbsLibrarySession= null;
  DbsPublicObject dbsPublicObject=null;
  DbsFolder dbsFolder= null;
  ExceptionBean exceptionBean;
  logger.debug("Entering ShowFolderContentAction now...");
  CryptographicUtil cryptoUtil=new CryptographicUtil(); 
  String contextPath=new String();
  String encodedString=new String();
  String decryptedString=new String();
  String userId=null;
  String userPassword=null;
  String davPath = null;
  Long folderId=null;
  Integer linkId = null;
  int [] linkCount = null;
  String [] linkExpDate = null;
  ActionErrors errors=new ActionErrors();
  boolean showFolder = false;
  ArrayList folderDocLists = new ArrayList();
  Locale locale = null;
  ActionMessages actionMessages = null;
  boolean isNewSession = true;
  
  try{
    logger.info("Entering ShowFolderContentAction...");
    locale = Locale.getDefault();
    ShowContentForm showContentForm=(ShowContentForm)form;
    logger.debug("ShowContentForm: "+showContentForm);
    context=getServlet().getServletContext();
    contextPath=(String)context.getAttribute("contextPath");
    logger.debug("contextPath: "+contextPath);
    /*before decrypting check for the existence of .password & .keyStore*/
    httpSession = request.getSession(false);
    if( httpSession.getAttribute("UserInfo")!= null ){
      isNewSession = false;
    }
    /*if they exist, go ahead with decryption */
    if(cryptoUtil.getSystemKeyStoreFile(contextPath)){
      String relativePath= contextPath+"WEB-INF"+File.separator+"params_xmls"+
                           File.separator+"GeneralActionParam.xml";
      ParseXMLTagUtil parseUtil= new ParseXMLTagUtil(relativePath);            
      davPath = parseUtil.getValue("DavPath","Configuration");
      /*if .password exists obtain cryptoPassword for decryption */
      if(cryptoUtil.getSystemKeyStoreFile(contextPath)){
        String pwdFilePath=cryptoUtil.getPwdFilePath(contextPath);
        String cryptoPassword=cryptoUtil.getCryptoPassword(pwdFilePath);
  
        /*while obtaining encodedString, URLDecoder will automatically perform 
         * it's function and the encodedString is now Base64Decoded to obtain a 
         * byte[]buf */
        encodedString=request.getParameter("auth");
  
        byte[] buf = new sun.misc.BASE64Decoder().decodeBuffer(encodedString);
        String decodedStr=URLDecoder.decode(encodedString,"UTF-8");
        logger.debug("URLDECODE: "+decodedStr);
      
        logger.debug("byteArray2 (buf)in showContentAction: "+new String(buf));
        logger.debug("byteArray2(buf).length: "+buf.length);
  
        /* buf is assigned to byte[]ToDecrypt*/
        byte[] byteArrayToDecrypt=buf;
        logger.debug("byteArrayToDecrypt.length: "+byteArrayToDecrypt.length);
  
        /*obtain byte[]Input Stream from byte[]ToDecrypt and byte[]Decrypted 
         * using crypto.decryptString()*/
        ByteArrayInputStream isStream= new ByteArrayInputStream(byteArrayToDecrypt);
        logger.debug("isStream.class: "+isStream.getClass());
        ByteArrayInputStream deciStream=(ByteArrayInputStream)
                                        cryptoUtil.decryptString(isStream,
                                        cryptoPassword,contextPath);
  
        int destavailable= deciStream.available();
        logger.debug("destavailable: "+destavailable);
        byte[] byteArrayDecrypted=new byte[destavailable];
        int decbytecount= deciStream.read(byteArrayDecrypted,0,destavailable);
        logger.debug("decbytecount: "+decbytecount);
        
        /*if streamreading has been successful, obtain decryptedString */
        if(decbytecount == destavailable){
          decryptedString=new String(byteArrayDecrypted);
          /* split decryptedString to obtain userId,userPassword,folderId,
           * linkId(optional) */
          String [] splitVals = decryptedString.split("&");
          int length = (splitVals == null)?0:splitVals.length;
          /* for backward compatibility , where linkId wasn't specified,viz: Wf */
          if( length == 3 ){
            String [] userIdArr = splitVals[0].split("=");
            userId = userIdArr[1];                        /* parameter 1 */
            String [] passwordArr = splitVals[1].split("=");
            userPassword = passwordArr[1];                /* parameter 2 */
            String [] folderIdArr = splitVals[2].split("=");
            folderId = new Long(folderIdArr[1]);           /* parameter 3 */
          }else{
          /* for modified link generation,there will be 4 parameters to obtain */
            String [] userIdArr = splitVals[0].split("=");
            userId = userIdArr[1];                        /* parameter 1 */
            String [] passwordArr = splitVals[1].split("=");
            userPassword = passwordArr[1];                /* parameter 2 */
            String [] folderIdArr = splitVals[2].split("=");
            folderId = new Long(folderIdArr[1]);           /* parameter 3 */
            String [] linkIdArr = splitVals[3].split("=");
            linkId = new Integer(linkIdArr[1]);           /* parameter 4 */
            logger.debug("linkId :"+linkId.intValue());
          }
          LoginBean loginBean = new LoginBean(userId,userPassword,relativePath);
          dbsLibrarySession = loginBean.getUserSession();
          /*obtain folder as dbsPublicObject using folderId from URL */
          dbsPublicObject=dbsLibrarySession.getPublicObject(folderId).getResolvedPublicObject();
          if( isNewSession ){
            UserDetailsBean userDetails = new UserDetailsBean(dbsLibrarySession);
            userDetails.setDetails(folderId);
            userPreferences = userDetails.getUserPreferences();
            userInfo = userDetails.getUserInfo();
            userInfo.setDavPath(davPath);
            userInfo.setLocale(Locale.getDefault());
            userInfo.setDbsLibrarySession(dbsLibrarySession);
            userInfo.setSystemAdmin(dbsLibrarySession.isAdministrationMode());
            folderDocInfo = userDetails.getFolderDocInfo();
            httpSession.setAttribute("UserPreferences",userPreferences);
            httpSession.setAttribute("UserInfo",userInfo);
            httpSession.setAttribute("FolderDocInfo",folderDocInfo);
          }else{
            userInfo = (UserInfo)httpSession.getAttribute("UserInfo"); 
            userPreferences = (UserPreferences)httpSession.getAttribute("UserPreferences");
            folderDocInfo = (FolderDocInfo)httpSession.getAttribute("FolderDocInfo");
            davPath = userInfo.getDavPath();
          }
          /* for backward compatibility */
          if( linkId == null ){
            showFolder = true;
            /*if dbsPublicObject is an instance of DbsFolder show it's docs */
            if(dbsPublicObject instanceof DbsFolder){
              dbsFolder=(DbsFolder)dbsPublicObject;
              logger.debug("Folder name: "+dbsFolder.getName());
            }
          }else{
            /* new logic for link limits by time and trials begins here */
            if( dbsPublicObject instanceof DbsFolder ){
              dbsFolder=(DbsFolder)dbsPublicObject;
              /* obtain folder attrs. LINK_COUNT and LINK_EXP_DATE */
              linkCount = dbsFolder.getAttribute("LINK_COUNT").getIntegerArray(
                                                                 dbsLibrarySession);
              linkExpDate = dbsFolder.getAttribute("LINK_EXP_DATE").getStringArray(
                                                                  dbsLibrarySession);
              /* fetch the linkCountValue and linkExpiryDateValue corr. to index
               * linkId obtained from the decrypted String */
              int linkCountVal = linkCount[linkId.intValue()];
              logger.debug("linkCountVal: "+linkCountVal);
              String linkExpDateVal = linkExpDate[linkId.intValue()];
              logger.debug("linkExpDateVal : "+linkExpDateVal);
              /* if there is a link expiry date */
              if( !linkExpDateVal.equals("-1") ){
                Date expiryDate = DateHelperForFileSystem.parse(linkExpDateVal,
                                                                DATE_FORMAT);
                Date dateObj = new Date();
                /* compare current date with the expiry date */
                if( dateObj.compareTo(expiryDate) <= 0 ){
                  logger.debug("link not expired...");
                  /* check for link count value */
                  /* if -1, then there is no limit on viewing folder thru link */
                  /* for non-negative value, update count and view link */
                  if( linkCountVal == -1 ){
                    showFolder = true;
                  }else if( linkCountVal > 0 ){
                    linkCount[linkId.intValue()] = --linkCountVal;
                    Integer [] link_Count = new Integer[linkCount.length];
                    for( int index = 0; index < linkCount.length; index++ ){
                      link_Count[index] = new Integer(linkCount[index]);
                    }
                    try{
                      /* Please note that when we have set the link for a version
                       * and then a new version is uploaded, our folder becomes
                       * a previous version and hence acquires a soft lock .
                       * Inorder to view it's content, we have to unlock it first
                       * because on a soft lock , one can delete an object but 
                       * cannot modify it and we are about to do the latter
                       * part by modifying it's attribute and hence need to unlock
                       * it first.*/
                      if( dbsFolder.getResolvedPublicObject().isLocked() ){
                        dbsFolder.getResolvedPublicObject().unlock();
                      }
                      /* set "LINK_COUNT" attr for the folder */
                      dbsFolder.setAttribute("LINK_COUNT",
                                               DbsAttributeValue.newAttributeValue(
                                               link_Count));
                    }catch (DbsException dbsEx) {
                      logger.debug(dbsEx.toString()); 
                    }
                    showFolder = true;
                  }else{            /* for zero link count donot show folder */
                    showFolder = false;
                  }
                }else{              /* if link has expired, donot show folder */
                  showFolder = false;
                }
                
              }else{ /* if no expiry date set, check only for link count */
                if( linkCountVal == -1 ){
                  showFolder = true;
                }else if( linkCountVal > 0 ){
                  linkCount[linkId.intValue()] = --linkCountVal;
                  Integer [] link_Count = new Integer[linkCount.length];
                  for( int index = 0; index < linkCount.length; index++ ){
                    link_Count[index] = new Integer(linkCount[index]);
                  }
                  try{
                    /* Please note that when we have set the link for a version
                     * and then a new version is uploaded, our folder becomes
                     * a previous version and hence acquires a soft lock .
                     * Inorder to view it's content, we have to unlock it first
                     * because on a soft lock , one can delete an object but 
                     * cannot modify it and we are about to do the latter
                     * part by modifying it's attribute and hence need to unlock
                     * it first.*/
                    if( dbsFolder.getResolvedPublicObject().isLocked() ){
                      dbsFolder.getResolvedPublicObject().unlock();
                    }
                    dbsFolder.setAttribute("LINK_COUNT",
                                            DbsAttributeValue.newAttributeValue(
                                            link_Count));
                  }catch (DbsException dbsEx) {
                    logger.debug(dbsEx.toString()); 
                  }
                  showFolder = true;
                }else{
                  showFolder = false;
                }
              }
            }
          }
          /* if showFolder then display folder content through link */
          if(showFolder){
            FetchPOIds fetchIds = new FetchPOIds(dbsLibrarySession);
            /* fetch documentIds from selected folder */
            Long [] selectedDocIds = fetchIds.getDocIds(new Long [] {dbsFolder.getId()},false);
            FolderDoc folderDoc = new FolderDoc(dbsLibrarySession);
            folderDocLists = folderDoc.getDocumentList(selectedDocIds,(byte)folderDocInfo.getListingType(),folderDocInfo,userPreferences,davPath);
            actionMessages = new ActionMessages();
            ActionMessage actionMessage = new ActionMessage("msg.folderdoc.number_of_item_found",String.valueOf(folderDocLists.size()));
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE,actionMessage);
            saveMessages(request,actionMessages);
          }else{
            /* unable to display folder as no criteria satisfied.
             * display appropriate error message */
            ActionError actionError=new ActionError("errors.link.limits.exceeded");
            errors.add(ActionErrors.GLOBAL_ERROR,actionError);
            saveErrors(request,errors);
            try{
              if(userInfo==null)
                target=new String("failureuserInfoNull");
              if(userInfo!=null)
                target=new String("failureInSession");
            }catch(Exception e){
              target=new String("failureuserInfoNull");
            }
          }
        }else{
          logger.error("error in reading stream...");
        }
      }else{/*if .password & .keyStore donot exist setup an ActionError */
        ActionError actionError=new ActionError("errors.in.displaying.content");
        errors.add(ActionErrors.GLOBAL_ERROR,actionError);
        saveErrors(request,errors);
        try{
          if(userInfo==null)
            target=new String("failureuserInfoNull");
          if(userInfo!=null)
            target=new String("failureInSession");
        }catch(Exception e){
          target=new String("failureuserInfoNull");
        }
      }
    }else{
      /* if .SystemKeyStore dir doesn't exist , display an ActionError */
      ActionError actionError=new ActionError("errors.in.displaying.content");
      errors.add(ActionErrors.GLOBAL_ERROR,actionError);
      saveErrors(request,errors);
      try{
        if(userInfo==null)
          target=new String("failureuserInfoNull");
        if(userInfo!=null)
          target=new String("failureInSession");
      }catch(Exception e){
        target=new String("failureuserInfoNull");
      }
    }
  }catch(DbsException dex){
    exceptionBean = new ExceptionBean(dex);
    dex.printStackTrace();
    logger.debug(dex.toString());
    if(dex.containsErrorCode(10201)){
      exceptionBean.setMessageKey("errors.unable.to.access.file");
    }
    logger.error(exceptionBean.getMessage());
    saveErrors(request,exceptionBean.getActionErrors());
    try{
      if(userInfo==null)
        target=new String("failureuserInfoNull");
      if(userInfo!=null)
        target=new String("failureInSession");
    }catch(Exception e){
      target=new String("failureuserInfoNull");
    }
  }catch(Exception ex){
    ActionError actionError = new ActionError("errors.unable.to.display.file");
    errors.add(ActionErrors.GLOBAL_ERROR,actionError);            
    saveErrors(request,errors);
    try{
      if(userInfo==null)
        target=new String("failureuserInfoNull");
      if(userInfo!=null)
        target=new String("failureInSession");
    }catch(Exception e){
      target=new String("failureuserInfoNull");
    }
  }
  request.setAttribute("folderDocLists",folderDocLists);
  logger.info("Exiting ShowFolderContentAction...");
  logger.debug("target : "+target);
  logger.debug("userInfo : " + userInfo);
  logger.debug("userPreferences : " + userPreferences);
  logger.debug("folderDocInfo : " + folderDocInfo);
  
  return mapping.findForward(target);
}   
}