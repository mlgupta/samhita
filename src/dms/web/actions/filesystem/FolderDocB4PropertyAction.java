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
 * $Id: FolderDocB4PropertyAction.java,v 20040220.20 2006/03/13 14:17:34 suved Exp $
 *****************************************************************************
 */
package dms.web.actions.filesystem;
/* dms package references */
import dms.beans.DbsAttributeValue;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsFolder;
import dms.beans.DbsFormat;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.web.actionforms.filesystem.FolderDocPropertyForm;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.FolderDoc;
import dms.web.beans.filesystem.FolderDocPropertyBean;
import dms.web.beans.filesystem.TotalSizeFoldersDocs;
import dms.web.beans.user.UserInfo;
import dms.web.beans.utility.GeneralUtil;
/* Java API */
import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/* Struts API */
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 *	Purpose: Action called to prepopulate folder_doc_property.jsp with selected
 *           item's property values
 *  @author             Jeetendra Prasad
 *  @version            1.0
 * 	Date of creation:   09-02-2004
 * 	Last Modified by :  Suved Mishra 
 * 	Last Modified Date: 01-03-2006  
 */
public class FolderDocB4PropertyAction extends Action {
  /**
  * This is the main action called from the Struts framework.
  * @param mapping The ActionMapping used to select this instance.
  * @param form The optional ActionForm bean for this request.
  * @param request The HTTP Request we are processing.
  * @param response The HTTP Response we are processing.
  */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    //Initialize logger
    Logger logger = Logger.getLogger("DbsLogger");
    logger.info("Entering FolderDocB4PropertyAction now...");
    logger.info("Building Folder and Doc property...");

    String success_one_document = "success_one_document";
    String success_only_document = "success_only_document";
    String success_one_folder = "success_one_folder";
    String success_only_folder = "success_only_folder";
    String success_folder_and_document = "success_folder_and_document";
    
    //variable declaration
    ExceptionBean exceptionBean;
    String forward = "success";
    Locale locale = getLocale(request);

    int propertyType;
    DbsAttributeValue docWfStatusAttrValue = null;
    DbsDocument dbsDocument;
    DbsFolder dbsFolder;
    DbsPublicObject dbsPublicObject;
    DbsFormat format;        
    try{
        FolderDocPropertyBean folderDocPropertyBean=new FolderDocPropertyBean();
        FolderDocPropertyForm folderDocPropertyForm = (FolderDocPropertyForm)form;
        logger.debug("folderDocPropertyForm : " + folderDocPropertyForm);
        HttpSession httpSession = request.getSession(false);
        UserInfo userInfo = (UserInfo) httpSession.getAttribute("UserInfo");

        DbsLibrarySession dbsLibrarySession = userInfo.getDbsLibrarySession();
        Long[] selectedFolderDocIds = folderDocPropertyForm.getChkFolderDocIds();
        logger.debug("selectedFolderDocIds.length : " + selectedFolderDocIds.length);
        
        propertyType = folderDocPropertyForm.getHdnPropertyType();
        if(propertyType ==  FolderDocPropertyForm.ONE_DOCUMENT){
            
            logger.debug("Property Type : FolderDocPropertyForm.ONE_DOCUMENT");            
            folderDocPropertyBean.setFolderDocIds(selectedFolderDocIds);
            folderDocPropertyBean.setDocumentId(selectedFolderDocIds[0].toString());
            dbsPublicObject = dbsLibrarySession.getPublicObject(selectedFolderDocIds[0]);
            
            String createDate = GeneralUtil.getDateForDisplay(dbsPublicObject.getCreateDate(),locale);
            logger.debug("createDate : " + createDate);
            folderDocPropertyBean.setCreatedDate(createDate);
            
            String createrName = dbsPublicObject.getCreator().getName();
            logger.debug("createrName : " + createrName);
            folderDocPropertyBean.setCreatedBy(createrName);

            String modifyDate = GeneralUtil.getDateForDisplay(dbsPublicObject.getLastModifyDate(),locale);
            logger.debug("modifyDate : " + modifyDate);
            folderDocPropertyBean.setModifiedDate(modifyDate);
            
            String modifierName = dbsPublicObject.getLastModifier().getName();
            logger.debug("modifierName : " + modifierName);
            folderDocPropertyBean.setModifiedBy(modifierName);

            String folderDocPath = dbsPublicObject.getFolderReferences(0).getAnyFolderPath();
            logger.debug("folderDocPath : " + folderDocPath);
            folderDocPropertyBean.setFolderDocPath(folderDocPath);
            
            String folderDocName = dbsPublicObject.getName();
            logger.debug("folderDocName : " + folderDocName);
            folderDocPropertyBean.setFolderDocName(folderDocName);

            String folderDocDescription = dbsPublicObject.getDescription();
            logger.debug("folderDocDescription : " +folderDocDescription);
            folderDocPropertyBean.setFolderDocDescription(folderDocDescription);

            /* code added to account for WORKFLOW_STATUS of a doc */
            docWfStatusAttrValue = dbsPublicObject.getResolvedPublicObject().getAttribute(DbsDocument.WORKFLOW_STATUS);
            if(docWfStatusAttrValue == null || docWfStatusAttrValue.getString(dbsLibrarySession)== null){
              folderDocPropertyBean.setDocWorkFlowStatus("draft");
            }else{
              folderDocPropertyBean.setDocWorkFlowStatus(docWfStatusAttrValue.getString(dbsLibrarySession));
            }
            logger.debug("docWorkflowStatus: "+folderDocPropertyBean.getDocWorkFlowStatus());
            
            String aclName;
            //if the acl applied on the user is private then getAcl() will throw error
            try{
                if(dbsPublicObject.getAcl() != null){
                    aclName = dbsPublicObject.getAcl().getName();
                }else{
                    aclName = "";
                }
                logger.debug("aclName : " + aclName);
                folderDocPropertyBean.setAclName(aclName);
                folderDocPropertyBean.setAclError(new Boolean(false));
                
            }catch(DbsException dex){
                exceptionBean = new ExceptionBean(dex);
                logger.error(exceptionBean.getMessage());
                logger.debug(exceptionBean.getErrorTrace());
                folderDocPropertyBean.setAclError(new Boolean(true));
            }
            
            
            dbsDocument = (DbsDocument)dbsPublicObject.getResolvedPublicObject();
            
            if(dbsPublicObject.getClassname().equals("FAMILY")){
                String versioned = String.valueOf(true);
                logger.debug("versioned : " + versioned);
                folderDocPropertyBean.setVersioned(versioned);                    
            }else{
                String versioned = String.valueOf(false);
                logger.debug("versioned : " + versioned);
                folderDocPropertyBean.setVersioned(versioned);                    
            }
            format = dbsDocument.getFormat();
            if(format != null){
                String mimeType = format.getMimeType();
                logger.debug("mimeType : " + mimeType);
                folderDocPropertyBean.setFolderDocType(mimeType);
            }else{
                String mimeType = "";
                logger.debug("mimeType : " + mimeType);
                folderDocPropertyBean.setFolderDocType(mimeType);
            }

            String folderDocSize = GeneralUtil.getDocSizeForDisplay(dbsDocument.getContentSize(),locale);
            logger.debug("folderDocSize : " + folderDocSize);
            folderDocPropertyBean.setFolderDocSize(folderDocSize);
            
            logger.info("Property Bean Created");                
            forward = success_one_document;
        }

        if(propertyType ==  FolderDocPropertyForm.ONLY_DOCUMENT){
            logger.debug("Property Type : FolderDocPropertyForm.ONLY_DOCUMENT");
            FolderDocPropertyBean propertyBean =new FolderDocPropertyBean();
            long size = 0;
            logger.info("Creating Property Bean...");
            
            //don't send selectedFolderDocIds because it is not required. no editing will be done on this page
            propertyBean.setFolderDocName("Document");  
            propertyBean.setFolderDocDescription("");
            /* code added to account for WORKFLOW_STATUS of a doc */
            propertyBean.setDocWorkFlowStatus("");
            propertyBean.setCreatedDate("");                 
            propertyBean.setModifiedDate("");                

            dbsPublicObject = dbsLibrarySession.getPublicObject(selectedFolderDocIds[0]);
            propertyBean.setFolderDocIds(selectedFolderDocIds);
            propertyBean.setDocumentCount(selectedFolderDocIds.length);

            String folderDocPath = dbsPublicObject.getFolderReferences(0).getAnyFolderPath();
            logger.debug("folderDocPath : " + folderDocPath);
            propertyBean.setFolderDocPath(folderDocPath);

            String createrName = dbsPublicObject.getCreator().getName();
            logger.debug("createrName : "+ createrName);
            propertyBean.setCreatedBy(createrName);

            String modifierName = dbsPublicObject.getLastModifier().getName();
            logger.debug("modifierName : " + modifierName);
            propertyBean.setModifiedBy(modifierName);

            String aclName;
            //if the acl applied on the user is private then getAcl() will through error
            try{
                if(dbsPublicObject.getAcl() != null){
                    aclName = dbsPublicObject.getAcl().getName();
                }else{
                    aclName = "";
                }
                logger.debug("aclName : " + aclName);
                propertyBean.setAclName(aclName);
                propertyBean.setAclError(new Boolean(false));
                
            }catch(DbsException dex){
                exceptionBean = new ExceptionBean(dex);
                logger.error(exceptionBean.getMessage());
                logger.debug(exceptionBean.getErrorTrace());
                propertyBean.setAclError(new Boolean(true));
            }
            
            dbsDocument = (DbsDocument)dbsPublicObject.getResolvedPublicObject();
            if(dbsPublicObject.getClassname().equals("FAMILY")){
                String versioned = String.valueOf(true);
                logger.debug("versioned : " + versioned);
                propertyBean.setVersioned(versioned);
            }else{
                String versioned = String.valueOf(false);
                logger.debug("versioned : " + versioned);
                propertyBean.setVersioned(versioned);
            }
            format = dbsDocument.getFormat();
            if(format != null){
                String mimeType = format.getMimeType();
                logger.debug("mimeType : " + mimeType);
                propertyBean.setFolderDocType(mimeType);
            }else{
                String mimeType = "";
                logger.debug("mimeType : " + mimeType);
                propertyBean.setFolderDocType(mimeType);
            }
            
            propertyBean.setFolderCount(0);
            propertyBean.setFolderDocCount(0);
            size = dbsDocument.getContentSize();
            //Now loop through the list and see if the value of the bean element is equal to the 
            //previous bean element. if so make the value of bean element a blank string
            
            for(int counter = 1; counter < selectedFolderDocIds.length ; counter++){
                dbsPublicObject = dbsLibrarySession.getPublicObject(selectedFolderDocIds[counter]);
                dbsDocument = (DbsDocument)dbsPublicObject.getResolvedPublicObject();                    
                if(dbsPublicObject instanceof DbsDocument){
                    String versioned = String.valueOf(false);
                    logger.debug("versioned : " + versioned);
                    propertyBean.setVersioned(versioned);
                }
                
                String prevMimeType = propertyBean.getFolderDocType();
                logger.debug("prevMimeType : " + prevMimeType);
                if(!prevMimeType.equals("")){ 
                    format = dbsDocument.getFormat();
                    if(format != null){
                        if(! prevMimeType.equals(format.getMimeType())){
                            String mimeType = "";
                            logger.debug("mimeType : " + mimeType);
                            propertyBean.setFolderDocType(mimeType);
                        }
                    }
                }
                
                if(!dbsPublicObject.getCreator().getName().equals(propertyBean.getCreatedBy())){
                    createrName = "" ;
                    logger.debug("createrName : " + createrName);
                    propertyBean.setCreatedBy(createrName);                        
                }
                if(!dbsPublicObject.getLastModifier().getName().equals(propertyBean.getModifiedBy())){
                    modifierName = "";
                    logger.debug("modifierName : " + modifierName);
                    propertyBean.setModifiedBy(modifierName);                        
                }
                if(!propertyBean.isAclError().booleanValue()){
                    if(!propertyBean.getAclName().equals("")){
                        if(dbsPublicObject.getAcl() != null){
                            if(!dbsPublicObject.getAcl().getName().equals(propertyBean.getAclName())){
                                aclName = "";
                                logger.debug("aclName : " + aclName);
                                propertyBean.setAclName(aclName);
                            }
                        }
                    }
                }
                long contentSize = dbsDocument.getContentSize();
                logger.debug("contentSize : " + contentSize);
                size = size + contentSize;
                logger.debug("size : " + size);
            }
            
            String displaySize = GeneralUtil.getDocSizeForDisplay(size,locale);
            logger.debug("displaySize : " + displaySize);
            propertyBean.setFolderDocSize(displaySize);
            
            folderDocPropertyBean = propertyBean;
            logger.info("Property Bean Created");                
            forward = success_only_document;
        }

        if(propertyType ==  FolderDocPropertyForm.ONE_FOLDER){
            logger.debug("Property Type : FolderDocPropertyForm.ONE_FOLDER");
            dbsFolder = (DbsFolder)dbsLibrarySession.getPublicObject(selectedFolderDocIds[0]);
            logger.info("Creating Property Bean...");
            folderDocPropertyBean=new FolderDocPropertyBean();
            folderDocPropertyBean.setFolderDocIds(selectedFolderDocIds);

            String folderDocName = dbsFolder.getName();
            logger.debug("folderDocName : " + folderDocName);
            folderDocPropertyBean.setFolderDocName(folderDocName);
            
            folderDocPropertyBean.setFolderDocType("");

            String folderDocDescription = dbsFolder.getDescription();
            logger.debug("folderDocDescription : " + folderDocDescription);
            folderDocPropertyBean.setFolderDocDescription(folderDocDescription);
            /* code added to account for WORKFLOW_STATUS of a doc */
            folderDocPropertyBean.setDocWorkFlowStatus("");


            String folderDocPath = dbsFolder.getAnyFolderPath();
            logger.debug("folderDocPath : " + folderDocPath);
            folderDocPropertyBean.setFolderDocPath(folderDocPath);
            
            FolderDoc folderDoc = new FolderDoc(dbsLibrarySession);

            TotalSizeFoldersDocs total = folderDoc.findTotalSizeFoldersDocs(selectedFolderDocIds[0]);
            String folderDocSize = GeneralUtil.getDocSizeForDisplay(total.getSize(),locale);
            logger.debug("folderDocSize : " + folderDocSize);
            folderDocPropertyBean.setFolderDocSize(folderDocSize);

            int folderCount = total.getFolderCount();
            logger.debug("folderCount : " + folderCount);
            folderDocPropertyBean.setFolderCount(folderCount);

            int documentCount = total.getDocumentCount();
            logger.debug("documentCount : " + documentCount);
            folderDocPropertyBean.setDocumentCount(documentCount);

            int folderDocCount = total.getFolderDocCount();
            logger.debug("folderDocCount : " + folderDocCount);
            folderDocPropertyBean.setFolderDocCount(folderDocCount);

            String createdDate = GeneralUtil.getDateForDisplay(dbsFolder.getCreateDate(),locale);
            logger.debug("createdDate : " + createdDate);
            folderDocPropertyBean.setCreatedDate(createdDate);

            String createrName = dbsFolder.getCreator().getName();
            logger.debug("createrName : " + createrName);
            folderDocPropertyBean.setCreatedBy(createrName);

            String modifiedDate = GeneralUtil.getDateForDisplay(dbsFolder.getLastModifyDate(),locale);
            logger.debug("modifiedDate : " + modifiedDate);
            folderDocPropertyBean.setModifiedDate(modifiedDate);

            String modifierName = dbsFolder.getLastModifier().getName();
            logger.debug("modifierName : " + modifierName);
            folderDocPropertyBean.setModifiedBy(modifierName);

            String aclName;
            //if the acl applied on the user is private then getAcl() will through error
            try{
                if(dbsFolder.getAcl() != null){
                    aclName = dbsFolder.getAcl().getName();
                }else{
                    aclName = "";
                }
                logger.debug("aclName : " + aclName);
                folderDocPropertyBean.setAclName(aclName);
                folderDocPropertyBean.setAclError(new Boolean(false));
                
            }catch(DbsException dex){
                exceptionBean = new ExceptionBean(dex);
                logger.error(exceptionBean.getMessage());
                logger.debug(exceptionBean.getErrorTrace());
                folderDocPropertyBean.setAclError(new Boolean(true));
            }
            
            folderDocPropertyBean.setShared(false);
            
            logger.info("Property Bean Created");                
            forward = success_one_folder;
        }

        if(propertyType ==  FolderDocPropertyForm.ONLY_FOLDER){
            logger.debug("Property Type : FolderDocPropertyForm.ONLY_FOLDER");

            FolderDoc folderDoc = new FolderDoc(dbsLibrarySession);
            TotalSizeFoldersDocs total;
            long size = 0;
            int folderCount = 0;
            int documentCount = 0;
            int folderDocCount = 0;

            //Create an instance of FolderDocPropertyBean
            FolderDocPropertyBean propertyBean=new FolderDocPropertyBean();

            //initialize the propertyBean wcich are fixed
            propertyBean.setFolderDocIds(selectedFolderDocIds);  
            propertyBean.setFolderDocName("");  
            propertyBean.setFolderDocType("");
            propertyBean.setFolderDocDescription("");
            /* code added to account for WORKFLOW_STATUS of a doc */
            propertyBean.setDocWorkFlowStatus("");
            propertyBean.setCreatedDate(""); 
            propertyBean.setModifiedDate("");
            propertyBean.setShared(false);

            //get first folder object from the list
            dbsFolder = (DbsFolder)dbsLibrarySession.getPublicObject(selectedFolderDocIds[0]);
            //set the propertyBean from the folder object
            String folderDocPath = dbsFolder.getFolderReferences(0).getAnyFolderPath();
            logger.debug("folderDocPath : " + folderDocPath);
            propertyBean.setFolderDocPath(folderDocPath);

            String createrName = dbsFolder.getCreator().getName();
            logger.debug("createrName : " + createrName);
            propertyBean.setCreatedBy(createrName);

            String modifierName = dbsFolder.getLastModifier().getName();
            logger.debug("modifierName : " + modifierName);
            propertyBean.setModifiedBy(modifierName);

            String aclName;
            //if the acl applied on the user is private then getAcl() will through error
            try{
                if(dbsFolder.getAcl() != null){
                    aclName = dbsFolder.getAcl().getName();
                }else{
                    aclName = "";
                }
                logger.debug("aclName : " + aclName);
                propertyBean.setAclName(aclName);
                propertyBean.setAclError(new Boolean(false));
                
            }catch(DbsException dex){
                exceptionBean = new ExceptionBean(dex);
                logger.error(exceptionBean.getMessage());
                logger.debug(exceptionBean.getErrorTrace());
                propertyBean.setAclError(new Boolean(true));
            }

            
            //get size folder count and document count for the first folder object
            total = folderDoc.findTotalSizeFoldersDocs(selectedFolderDocIds[0]);
            size = total.getSize();
            logger.debug("size : " + size);
            folderCount = total.getFolderCount();
            logger.debug("folderCount : " + folderCount);
            documentCount = total.getDocumentCount();
            logger.debug("documentCount : " + documentCount);
            folderDocCount = total.getFolderDocCount();
            logger.debug("folderDocCount : " + folderDocCount);
            
            for(int counter = 1; counter < selectedFolderDocIds.length ; counter++){
                dbsFolder = (DbsFolder)dbsLibrarySession.getPublicObject(selectedFolderDocIds[counter]);                
                total = folderDoc.findTotalSizeFoldersDocs(selectedFolderDocIds[counter]);
                size += total.getSize();
                logger.debug("size : " + size);
                folderCount += total.getFolderCount();
                logger.debug("folderCount : " + folderCount);
                documentCount += total.getDocumentCount();
                logger.debug("documentCount : " + documentCount);
                folderDocCount += total.getFolderDocCount();
                logger.debug("folderDocCount : " + folderDocCount);
                
                if(!dbsFolder.getCreator().getName().equals(propertyBean.getCreatedBy())){
                    createrName = "";
                    logger.debug("createrName : " + createrName);
                    propertyBean.setCreatedBy(createrName); 
                }
                if(!dbsFolder.getLastModifier().getName().equals(propertyBean.getModifiedBy())){
                    modifierName = "";
                    logger.debug("modifierName : " + modifierName);
                    propertyBean.setModifiedBy(modifierName);                        
                }
                if(!propertyBean.isAclError().booleanValue()){
                    if(!propertyBean.getAclName().equals("")){
                        if(dbsFolder.getAcl() != null){
                            if(!dbsFolder.getAcl().getName().equals(propertyBean.getAclName())){
                                aclName = "";
                                logger.debug("aclName : " + aclName);
                                propertyBean.setAclName(aclName);
                            }
                        }
                    }
                }
            }
            String displaySize = GeneralUtil.getDocSizeForDisplay(size,locale);
            logger.debug("displaySize : " + displaySize);
            propertyBean.setFolderDocSize(displaySize);
            logger.debug("folderCount : " + folderCount);
            propertyBean.setFolderCount(folderCount);
            logger.debug("documentCount : " + documentCount);
            propertyBean.setDocumentCount(documentCount);
            logger.debug("folderDocCount : " + folderDocCount);
            propertyBean.setFolderDocCount(folderDocCount);
            
            folderDocPropertyBean = propertyBean;
            logger.info("Property Bean Created");                
            forward = success_one_folder;
        }

        //to show the property when folder and document are selected 

        if(propertyType ==  FolderDocPropertyForm.FOLDER_AND_DOCUMENT){
            logger.debug("Property Type : FolderDocPropertyForm.FOLDER_AND_DOCUMENT");

            FolderDoc folderDoc = new FolderDoc(dbsLibrarySession);
            TotalSizeFoldersDocs total;
            long size = 0;
            int folderCount = 0;
            int documentCount = 0;
            int folderDocCount = 0;

            //Create an instance of FolderDocPropertyBean
            FolderDocPropertyBean propertyBean=new FolderDocPropertyBean();

            //initialize the propertyBean wcich are fixed
            propertyBean.setFolderDocIds(selectedFolderDocIds);  
            propertyBean.setFolderDocName("");  
            propertyBean.setFolderDocType("");
            propertyBean.setFolderDocDescription("");
            propertyBean.setDocWorkFlowStatus("");
            propertyBean.setCreatedDate(""); 
            propertyBean.setModifiedDate("");
            propertyBean.setShared(false);
            propertyBean.setCreatedBy("");
            propertyBean.setModifiedBy("");
            
            //get first folder object from the list
            dbsPublicObject = dbsLibrarySession.getPublicObject(selectedFolderDocIds[0]);
            //set the propertyBean from the folder object
            String folderDocPath = dbsPublicObject.getFolderReferences(0).getAnyFolderPath();
            logger.debug("folderDocPath : " + folderDocPath);
            propertyBean.setFolderDocPath(folderDocPath);

             String aclName;
            //if the acl applied on the user is private then getAcl() will through error
            try{
                if(dbsPublicObject.getAcl() != null){
                    aclName = dbsPublicObject.getAcl().getName();
                }else{
                    aclName = "";
                }
                logger.debug("aclName : " + aclName);
                propertyBean.setAclName(aclName);
                propertyBean.setAclError(new Boolean(false));
                
            }catch(DbsException dex){
                exceptionBean = new ExceptionBean(dex);
                logger.error(exceptionBean.getMessage());
                logger.debug(exceptionBean.getErrorTrace());
                propertyBean.setAclError(new Boolean(true));
            }
           
            //get size folder count and document count for the first folder object
            total = folderDoc.findTotalSizeFoldersDocs(selectedFolderDocIds[0]);
            size = total.getSize();
            logger.debug("size : " + size);
            folderCount = total.getFolderCount();
            logger.debug("folderCount : " + folderCount);
            documentCount = total.getDocumentCount();
            logger.debug("documentCount : " + documentCount);
            folderDocCount = total.getFolderDocCount();
            logger.debug("folderDocCount : " + folderDocCount);
            
            for(int counter = 1; counter < selectedFolderDocIds.length ; counter++){
                dbsPublicObject = dbsLibrarySession.getPublicObject(selectedFolderDocIds[counter]);                
                total = folderDoc.findTotalSizeFoldersDocs(selectedFolderDocIds[counter]);
                size += total.getSize();
                logger.debug("size : " + size);
                folderCount += total.getFolderCount();
                logger.debug("folderCount : " + folderCount);
                documentCount += total.getDocumentCount();
                logger.debug("documentCount : " + documentCount);
                folderDocCount += total.getFolderDocCount();
                logger.debug("folderDocCount : " + folderDocCount);

                if(!propertyBean.isAclError().booleanValue()){
                    if(!propertyBean.getAclName().equals("")){
                        if(dbsPublicObject.getAcl() != null){
                            if(!dbsPublicObject.getAcl().getName().equals(propertyBean.getAclName())){
                                aclName = "";
                                logger.debug("aclName : " + aclName);
                                propertyBean.setAclName(aclName);
                            }
                        }
                    }
                }
            }
            String displaySize = GeneralUtil.getDocSizeForDisplay(size,locale);
            logger.debug("displaySize : " + displaySize);
            propertyBean.setFolderDocSize(displaySize);
            propertyBean.setFolderCount(folderCount);
            logger.debug("folderCount : " + folderCount);
            propertyBean.setDocumentCount(documentCount);
            logger.debug("documentCount : " + documentCount);
            propertyBean.setFolderDocCount(folderDocCount );
            logger.debug("folderDocCount : " + folderDocCount);
            
            folderDocPropertyBean = propertyBean;
            logger.info("Property Bean Created");                
            forward = success_folder_and_document;
        }

        logger.debug("folderDocPropertyBean : " + folderDocPropertyBean);            
        request.setAttribute("folderDocPropertyBean",folderDocPropertyBean);
        
    }catch(DbsException dex){
        exceptionBean = new ExceptionBean(dex);
        logger.error("An Exception occurred in FolderDocB4PropertyAction... ");
        logger.error(exceptionBean.getErrorTrace());
        saveErrors(request,exceptionBean.getActionErrors());
    }catch(Exception ex){
        exceptionBean = new ExceptionBean(ex);
        logger.error("An Exception occurred in FolderDocB4PropertyAction... ");
        logger.error(exceptionBean.getErrorTrace());
        saveErrors(request,exceptionBean.getActionErrors());
    }
    logger.info("Building Folder and Doc property complete");
    logger.info("Exiting FolderDocB4PropertyAction now...");
    return mapping.findForward(forward);
  }

}
