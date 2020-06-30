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
 * $Id: DocGenerateLinkAction.java,v 1.20 2006/06/08 12:54:41 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/*dms package references*/
import dms.beans.DbsAttributeValue;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.LinkDetailsForm;
import dms.web.beans.crypto.CryptographicUtil;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.DateHelperForFileSystem;
import dms.web.beans.filesystem.FolderDocList;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.ParseXMLTagUtil;
/*java API */
import dms.web.beans.wf.watch.InitiateWatch;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/*Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * Purpose: Action called to generate link(s) for doc(s)(only) selected from 
 *          folder-doc list
 * @author            : Suved Mishra
 * @version           : 1.0
 * Date of Creation   : 13-01-05
 * Last Modified Date : 12-12-05
 * Last Modified By   : Suved Mishra
 */
/** The link(s) generated contain userId,userpassword,docId & linkId(optional) 
 * in encrypted form */
public class DocGenerateLinkAction extends Action 
{
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
    String UserId=new String();
    DbsLibrarySession dbsLibrarySession= null;
    HttpSession httpSession= null;    
    ExceptionBean exceptionBean;
    ActionErrors errors=new ActionErrors();
    Locale locale = getLocale(request);
    CryptographicUtil crypto=new CryptographicUtil();
    String cryptoPassword=new String();
    String encryptedString=new String();
    ArrayList DocLinkLists= new ArrayList();
    DbsDocument dbsDoc = null;
    DbsFolder dbsFolder = null;
    int [] linkCount = null;
    String [] linkExpDate = null;
    Integer [] link_Count = null;
    String [] link_ExpDate = null;
    String [] dateTimeNames = null;
    String [] link_Limits = null;
    Long [] docIds = null;
    Long [] chkDocIds = null;
    int length = 0;
    int selectedIdsLength = 0;
    Logger logger= Logger.getLogger("DbsLogger");
    logger.info("Entering DocGenerateLinkAction now... ");

    try{
      httpSession= request.getSession(false);
      LinkDetailsForm linkDetailsForm=(LinkDetailsForm)form;
      logger.debug("LinkDetailsForm: "+linkDetailsForm);

      /*obtain userId,userPassword */
      UserInfo userInfo= (UserInfo)httpSession.getAttribute("UserInfo");
      logger.debug("UserInfo: "+userInfo);
      dbsLibrarySession= userInfo.getDbsLibrarySession();
      String userID= dbsLibrarySession.getUser().getName();
      String userPassword=(String)httpSession.getAttribute("password");

      if( linkDetailsForm!=null ){
        dateTimeNames = linkDetailsForm.getTxtDateTimeNames();
        link_Limits = linkDetailsForm.getCombotexts();
        docIds = linkDetailsForm.getTxtDocIds();
        chkDocIds = linkDetailsForm.getChkSelects();
        // selectedChkDocIdsLength is to check those pos that had been selected
        // for "Never" option.
        int selectedChkDocIdsLength = ( chkDocIds == null )?0:chkDocIds.length;
        // selectedIdsLength is for those pos whose date and/or numeric limits
        // were specified.
        selectedIdsLength = ((dateTimeNames == null) && (link_Limits == null))?
                              0:dateTimeNames.length;
        String nameOfDoc = null;
        
        for( int index = 0; index < selectedIdsLength; index++ ){
          logger.debug("linkExpDate ["+index+"]:"+dateTimeNames[index]);
          logger.debug("link_Limits ["+index+"]:"+link_Limits[index]);
          logger.debug("docIds ["+index+"]:"+docIds[index].longValue());
        }
        for( int index = 0; index < selectedChkDocIdsLength; index++ ){
          logger.debug("chkDocIds ["+index+"]:"+chkDocIds[index]);
        }
        /* for "Never" option set the corr dateTimeNames and link_Limits
         * values to "" and -1 resp, which implies no expiry limit. */
        for( int i = 0; i < selectedChkDocIdsLength; i++ ){
          for( int j =0; j < selectedIdsLength; j++ ){
            if( chkDocIds[i].longValue() == docIds[j].longValue() ){
              link_Limits[j] ="-1";
              dateTimeNames[j] = "";
            }
          }
        }
        
        /* if unlimited downloads , then set corr link_Limits to -1.*/
        for( int index = 0; index < selectedIdsLength; index++ ){
          if( link_Limits[index].equalsIgnoreCase("unlimited") ){
            link_Limits[index] = "-1";
          }
        }
      }

      /*obtain contextPath,SystemKeyStorePath,passwordFilePath */
      String contextPath=(String)
                    httpSession.getServletContext().getAttribute("contextPath");
      String keyPath=crypto.getSystemKeyStorePath(contextPath);
      //logger.debug("keyPath: "+keyPath);
      String pwdFilePath=crypto.getPwdFilePath(contextPath);
      //logger.debug("pwdFilePath: "+pwdFilePath);
      logger.debug("keyPath and password file paths obtained.");

      String relativePath= contextPath+"WEB-INF"+File.separator+"params_xmls"+
                           File.separator+"GeneralActionParam.xml";
      ParseXMLTagUtil parseUtil= new ParseXMLTagUtil(relativePath);

      /*before link generation, check for the existence of .password & .keyStore */
      /*failure, setup an ActionError */
      if(!crypto.getSystemKeyStoreFile(contextPath)){
        target=new String("failure");
        ActionError actionError=new ActionError("errors.problem.in.linkgeneration");
        errors.add(ActionErrors.GLOBAL_ERROR,actionError);
        saveErrors(request,errors);
      }else{
      /*else go ahead for encryption */
      /*obtain cryptoPassword from .password, a byte[]Stream from userId,
       * userpassword,docId,linkId and encrypt using crypto.encryptString()*/

      /*obtain an arraylist of doc(s) selected in the form: name,id,txtLinkGenerated */
        for(int counter=0;counter<selectedIdsLength;counter++){
          cryptoPassword=crypto.getCryptoPassword(pwdFilePath);
          DbsPublicObject dbsPO = dbsLibrarySession.getPublicObject(
                                  docIds[counter]).getResolvedPublicObject();


          if( dbsPO instanceof DbsDocument ){
            dbsDoc = (DbsDocument)dbsPO;

            linkCount = dbsDoc.getAttribute("LINK_COUNT").getIntegerArray(
                        dbsLibrarySession);
            linkExpDate = dbsDoc.getAttribute("LINK_EXP_DATE").getStringArray(
                          dbsLibrarySession);
            length = ( (linkCount == null) && (linkExpDate == null) )?
                            0:linkCount.length;
            logger.debug("length : "+length);

            /* encrypt userId, password, documentId, linkId */
            String toBeEncrypted="userId="+userID+
                                 "&password="+userPassword+
                                 "&documentId="+dbsLibrarySession.getPublicObject(
                                                docIds[counter]).getResolvedPublicObject().getId()+
                                 "&linkId="+new Integer(length);
            //logger.debug("cryptoPassword in DocGenerateLinkAction : "+cryptoPassword);
            byte[] byteArrayToEncrypt= toBeEncrypted.getBytes();
            //logger.debug("byteArrayToEncrypt: "+new String(byteArrayToEncrypt));
            ByteArrayInputStream inStream= new ByteArrayInputStream(byteArrayToEncrypt);
            //logger.debug("ByteArray To Encrypt "+new String(byteArrayToEncrypt));
            logger.debug("inStream: "+inStream.getClass());
            ByteArrayInputStream enciStream=(ByteArrayInputStream)
                  crypto.encryptString(inStream,userID,cryptoPassword,contextPath);
            int available=enciStream.available();
            //logger.debug("available: "+available);
            byte[] byteArrayEncrypted=new byte[available];
            int byteCount=enciStream.read(byteArrayEncrypted,0,available);
            //logger.debug("byteArray1 "+new String(byteArrayEncrypted));

            //logger.debug("byteCount: "+byteCount);
            enciStream.close();

            /*if streamreading has been successful, encode the byte[]Encrypted to
             * encryptedString using Base64 Encoder and then URLEncode the latter */
            if(byteCount == available){
              encryptedString = new sun.misc.BASE64Encoder().encode(
                                                              byteArrayEncrypted);
              logger.debug("Base64Enc "+ encryptedString);
              String encodedStr=URLEncoder.encode(encryptedString,"UTF-8");
              //request.setAttribute("EncodedString",encodedStr);

              /*obtain IP:Port reference from GeneralActionParams.xml */
              //logger.debug("request.getRequestURI(): "+request.getRequestURI());

              /*String linkString= parseUtil.getValue("editiporport","Configuration")+request.getRequestURI().replaceAll("docGenerateLinkAction.do","showContentAction.do");
              logger.debug("linkString: "+linkString);*/

              /* construct linkString using request.getRequestURL().toString() */
              //String linkString= request.getRequestURL().toString().replaceAll("docGenerateLinkAction.do","showContentAction.do");
              String linkString= parseUtil.getValue("redirector","Configuration")+
                                 request.getRequestURI().replaceAll(
                                "docGenerateLinkAction.do","showContentAction.do");

              FolderDocList folderDocList= new FolderDocList();

              logger.debug("dbsPublicObject.getId for folderocList: "+
                            dbsLibrarySession.getPublicObject(docIds[counter]).getId());
              folderDocList.setId(dbsLibrarySession.getPublicObject(docIds[counter]).getResolvedPublicObject().getId());

              logger.debug("dbsPublicObject.getName for folderocList: "+
                            dbsDoc.getName());
              folderDocList.setName(dbsDoc.getName());

              folderDocList.setClassName("Document");
              String linkGenerated =linkString+"?auth="+encodedStr;
              logger.debug("linkGenerated: "+linkGenerated);

              folderDocList.setTxtLinkGenerated(linkGenerated);
              DocLinkLists.add(folderDocList);
              logger.debug("DocLinkLists.size: "+DocLinkLists.size());

              /* Update the 2 document attrs namely : LINK_COUNT,LINK_EXP_DATE */
              link_Count = new Integer[length+1];
              link_ExpDate = new String[length+1];
              for( int index = 0; index < length; index++ ){
                link_Count[index] = new Integer(linkCount[index]);
                link_ExpDate[index] = new String(linkExpDate[index]);
              }

              link_Count[length] = (link_Limits[counter].trim().length() == 0)?
                                  new Integer(-1):new Integer(link_Limits[counter]);

              if( dateTimeNames[counter]==null ||
                  dateTimeNames[counter].length() == 0 ||
                  dateTimeNames[counter].trim().length() == 0 ){
                link_ExpDate[length] = new String("-1");
              }else{
                String hdnTimeZone = linkDetailsForm.getHdnTimeZone();
                TimeZone userTZ = TimeZone.getTimeZone(hdnTimeZone);
                Date dateObj = DateHelperForFileSystem.parse(
                                            dateTimeNames[counter],DATE_FORMAT);
                int year = DateHelperForFileSystem.getYear(dateObj);
                int month = DateHelperForFileSystem.getMonth(dateObj);
                int day = DateHelperForFileSystem.getDay(dateObj);
                int hours = DateHelperForFileSystem.getHour(dateObj);
                int minutes = DateHelperForFileSystem.getMinute(dateObj);
                int seconds = DateHelperForFileSystem.getSecond(dateObj);
                Date expiryDate = DateHelperForFileSystem.getDate(
                                    year,month,day,hours,minutes,seconds,userTZ);
                link_ExpDate[length] = DateHelperForFileSystem.format(expiryDate,
                                                                  DATE_FORMAT);
                logger.debug("Formatted Date : "+link_ExpDate[length]);
              }
              dbsDoc.setAttribute("LINK_COUNT",DbsAttributeValue.newAttributeValue(
                                  link_Count));
              dbsDoc.setAttribute("LINK_EXP_DATE",DbsAttributeValue.newAttributeValue(
                                  link_ExpDate));
              // code for wf submission goes here
              if( relativePath!=null && userID!=null ){
                String actionForWatch = "You are receiving this mail because you have applied watch on "+dbsDoc.getName()+" and a new operation has been performed on it.\n\t\tOperation Performed : Link Generated ";
                InitiateWatch iniWatch = new InitiateWatch(relativePath,userID,
                                                actionForWatch,dbsDoc.getId());
                iniWatch.startWatchProcess();
                actionForWatch = null;
              }
            }else{
              /*else set up an ActionError */
              target=new String("failure");
              ActionError actionError=new ActionError("errors.problem.in.streamread");
              errors.add(ActionErrors.GLOBAL_ERROR,actionError);
              saveErrors(request,errors);
            }
          }else if( dbsPO instanceof DbsFolder  ){
            dbsFolder = (DbsFolder)dbsPO;
            linkCount = dbsFolder.getAttribute("LINK_COUNT").getIntegerArray(
                        dbsLibrarySession);
            linkExpDate = dbsFolder.getAttribute("LINK_EXP_DATE").getStringArray(
                          dbsLibrarySession);
            length = ( (linkCount == null) && (linkExpDate == null) )?
                            0:linkCount.length;
            logger.debug("length : "+length);

            /* encrypt userId, password, documentId, linkId */
            String toBeEncrypted="userId="+userID+
                                 "&password="+userPassword+
                                 "&folderId="+dbsLibrarySession.getPublicObject(
                                                docIds[counter]).getResolvedPublicObject().getId()+
                                 "&linkId="+new Integer(length);
            //logger.debug("cryptoPassword in DocGenerateLinkAction : "+cryptoPassword);
            byte[] byteArrayToEncrypt= toBeEncrypted.getBytes();
            //logger.debug("byteArrayToEncrypt: "+new String(byteArrayToEncrypt));
            ByteArrayInputStream inStream= new ByteArrayInputStream(byteArrayToEncrypt);
            //logger.debug("ByteArray To Encrypt "+new String(byteArrayToEncrypt));
            logger.debug("inStream: "+inStream.getClass());
            ByteArrayInputStream enciStream=(ByteArrayInputStream)
                  crypto.encryptString(inStream,userID,cryptoPassword,contextPath);
            int available=enciStream.available();
            //logger.debug("available: "+available);
            byte[] byteArrayEncrypted=new byte[available];
            int byteCount=enciStream.read(byteArrayEncrypted,0,available);
            //logger.debug("byteArray1 "+new String(byteArrayEncrypted));

            //logger.debug("byteCount: "+byteCount);
            enciStream.close();

            /*if streamreading has been successful, encode the byte[]Encrypted to
             * encryptedString using Base64 Encoder and then URLEncode the latter */
            if(byteCount == available){
              encryptedString = new sun.misc.BASE64Encoder().encode(
                                                              byteArrayEncrypted);
              logger.debug("Base64Enc "+ encryptedString);
              String encodedStr=URLEncoder.encode(encryptedString,"UTF-8");
              //request.setAttribute("EncodedString",encodedStr);

              /*obtain IP:Port reference from GeneralActionParams.xml */
              //logger.debug("request.getRequestURI(): "+request.getRequestURI());

              /*String linkString= parseUtil.getValue("editiporport","Configuration")+request.getRequestURI().replaceAll("docGenerateLinkAction.do","showContentAction.do");
              logger.debug("linkString: "+linkString);*/

              /* construct linkString using request.getRequestURL().toString() */
              //String linkString= request.getRequestURL().toString().replaceAll("docGenerateLinkAction.do","showContentAction.do");
              String linkString= parseUtil.getValue("redirector","Configuration")+
                                 request.getRequestURI().replaceAll(
                                "docGenerateLinkAction.do","showFolderContentAction.do");

              FolderDocList folderDocList= new FolderDocList();

              logger.debug("dbsPublicObject.getId for folderocList: "+
                            dbsLibrarySession.getPublicObject(docIds[counter]).getId());
              folderDocList.setId(dbsLibrarySession.getPublicObject(docIds[counter]).getResolvedPublicObject().getId());

              logger.debug("dbsPublicObject.getName for folderocList: "+
                            dbsFolder.getName());
              folderDocList.setName(dbsFolder.getName());

              folderDocList.setClassName("Folder");
              String linkGenerated =linkString+"?auth="+encodedStr;
              logger.debug("linkGenerated: "+linkGenerated);

              folderDocList.setTxtLinkGenerated(linkGenerated);
              DocLinkLists.add(folderDocList);
              logger.debug("DocLinkLists.size: "+DocLinkLists.size());

              /* Update the 2 document attrs namely : LINK_COUNT,LINK_EXP_DATE */
              link_Count = new Integer[length+1];
              link_ExpDate = new String[length+1];
              for( int index = 0; index < length; index++ ){
                link_Count[index] = new Integer(linkCount[index]);
                link_ExpDate[index] = new String(linkExpDate[index]);
              }

              link_Count[length] = (link_Limits[counter].trim().length() == 0)?
                                  new Integer(-1):new Integer(link_Limits[counter]);

              if( dateTimeNames[counter]==null ||
                  dateTimeNames[counter].length() == 0 ||
                  dateTimeNames[counter].trim().length() == 0 ){
                link_ExpDate[length] = new String("-1");
              }else{
                String hdnTimeZone = linkDetailsForm.getHdnTimeZone();
                TimeZone userTZ = TimeZone.getTimeZone(hdnTimeZone);
                Date dateObj = DateHelperForFileSystem.parse(
                                            dateTimeNames[counter],DATE_FORMAT);
                int year = DateHelperForFileSystem.getYear(dateObj);
                int month = DateHelperForFileSystem.getMonth(dateObj);
                int day = DateHelperForFileSystem.getDay(dateObj);
                int hours = DateHelperForFileSystem.getHour(dateObj);
                int minutes = DateHelperForFileSystem.getMinute(dateObj);
                int seconds = DateHelperForFileSystem.getSecond(dateObj);
                Date expiryDate = DateHelperForFileSystem.getDate(
                                    year,month,day,hours,minutes,seconds,userTZ);
                link_ExpDate[length] = DateHelperForFileSystem.format(expiryDate,
                                                                  DATE_FORMAT);
                logger.debug("Formatted Date : "+link_ExpDate[length]);
              }
              dbsFolder.setAttribute("LINK_COUNT",DbsAttributeValue.newAttributeValue(
                                  link_Count));
              dbsFolder.setAttribute("LINK_EXP_DATE",DbsAttributeValue.newAttributeValue(
                                  link_ExpDate));
              request.setAttribute("folders","true");
            }else{
              /*else set up an ActionError */
              target=new String("failure");
              ActionError actionError=new ActionError("errors.problem.in.streamread");
              errors.add(ActionErrors.GLOBAL_ERROR,actionError);
              saveErrors(request,errors);
            }
          }
          length = 0;
        }
        request.setAttribute("DocLinkLists",DocLinkLists);
      }

    }catch(DbsException dex){
      exceptionBean= new ExceptionBean(dex);
      logger.error("An Exception occurred in DocGenerateLinkAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }catch(Exception ex){
      exceptionBean= new ExceptionBean(ex);
      logger.error("An Exception occurred in DocGenerateLinkAction... ");
      logger.error(exceptionBean.getErrorTrace());
      saveErrors(request,exceptionBean.getActionErrors());
    }
    logger.info("Exiting DocGenerateLinkAction now... ");
    return mapping.findForward(target);
  }
}
