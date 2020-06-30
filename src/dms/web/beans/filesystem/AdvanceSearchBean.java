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
 * $Id: AdvanceSearchBean.java,v 20040220.25 2006/03/13 14:18:20 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.filesystem;
/* dms package references */
import dms.beans.DbsAttributeQualification;
import dms.beans.DbsAttributeSearchSpecification;
import dms.beans.DbsAttributeValue;
import dms.beans.DbsContentObject;
import dms.beans.DbsContextQualification;
import dms.beans.DbsContextSearchSpecification;
import dms.beans.DbsDocument;
import dms.beans.DbsException;
import dms.beans.DbsFileSystem;
import dms.beans.DbsFolder;
import dms.beans.DbsFolderRestrictQualification;
import dms.beans.DbsFormat;
import dms.beans.DbsJoinQualification;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPublicObject;
import dms.beans.DbsSearch;
import dms.beans.DbsSearchClassSpecification;
import dms.beans.DbsSearchClause;
import dms.beans.DbsSearchQualification;
import dms.beans.DbsSearchSortSpecification;
import dms.web.actionforms.filesystem.AdvanceSearchForm;
import dms.web.actions.filesystem.AdvanceSearchAction;
import dms.web.beans.exception.ExceptionBean;
import dms.web.beans.filesystem.DateHelperForFileSystem;
/* Java API */
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
/* Logger API */
import org.apache.log4j.Logger;
/**
 *	Purpose:             Bean to perform advanced search .
 *  @author              Jeetendra Prasad 
 *  @version             1.0
 * 	Date of creation:    10-04-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  13-03-2006 
 */
public class AdvanceSearchBean {   
    private static String DATE_FORMAT = "MM/dd/yyyy";
    private DbsLibrarySession dbsLibrarySession;
    private DbsPublicObject dbsPublicObject;
    private Logger logger;
    private Locale locale;
    
    public AdvanceSearchBean(DbsLibrarySession dbsLibrarySession){
        this.dbsLibrarySession = dbsLibrarySession;
        //Initialize logger
        logger = Logger.getLogger("DbsLogger");
        //use default if locale not set
        locale = Locale.getDefault(); //
    }

    public DbsSearch getSearchObject(AdvanceSearchForm advanceSearchForm) throws DbsException, ParseException, ExceptionBean{
        ArrayList folderDocCompleteLists= null;   
        DbsSearch dbsSearch = null;
        List publicObjects = null;
        List searchQualificationList = new ArrayList();
        try{
            // To restrict the search to the particular folder
            DbsFolderRestrictQualification folderRestrictQual = null;
            DbsFileSystem dbsFileSystem = new DbsFileSystem(dbsLibrarySession);            
            if (advanceSearchForm.getTxtLookinFolderPath() != null){
                folderRestrictQual = new DbsFolderRestrictQualification();
                DbsFolder dbsSearchFolder = (DbsFolder)dbsFileSystem.findPublicObjectByPath(advanceSearchForm.getTxtLookinFolderPath());
                folderRestrictQual.setStartFolder(dbsSearchFolder);
                folderRestrictQual.setMultiLevel(true);
                folderRestrictQual.setSearchClassname(DbsPublicObject.CLASS_NAME);
                searchQualificationList.add(folderRestrictQual);
            }else{
                ExceptionBean exceptionBean = new ExceptionBean();
                exceptionBean.setMessage("No folder selected for searching");
                exceptionBean.setMessageKey("errors.lookin.folder.not.selected");
                throw exceptionBean;
            }

            DbsAttributeQualification folderDocIdAttrbQual = new DbsAttributeQualification();
            String searchColumnId = "ID";
            folderDocIdAttrbQual.setAttribute(searchColumnId);
            folderDocIdAttrbQual.setOperatorType(DbsAttributeQualification.NOT_EQUAL);
            folderDocIdAttrbQual.setValue(DbsAttributeValue.newAttributeValue(advanceSearchForm.getCurrentFolderId()));
            searchQualificationList.add(folderDocIdAttrbQual);

            // To build search based on the Attribute like name of the document
            if (advanceSearchForm.getTxtFolderOrDocName() != null && advanceSearchForm.getTxtFolderOrDocName().trim().length() != 0 )	{
                DbsAttributeQualification folderDocNameAttrbQual = new DbsAttributeQualification();
                String searchColumn = dbsPublicObject.NAME_ATTRIBUTE;
                folderDocNameAttrbQual.setCaseIgnored(true);
                folderDocNameAttrbQual.setAttribute(searchColumn);
                folderDocNameAttrbQual.setOperatorType(DbsAttributeQualification.LIKE);
                String searchValue = advanceSearchForm.getTxtFolderOrDocName().trim();
                searchValue = searchValue.replace('*' , '%');
                searchValue = searchValue.replace('?' , '_');
                folderDocNameAttrbQual.setValue(searchValue);
                searchQualificationList.add(folderDocNameAttrbQual);
            }

            // To build search based on the Attribute like description of the document
            if (advanceSearchForm.getTxtDocDescription() != null && advanceSearchForm.getTxtDocDescription().trim().length() != 0 )	{
                DbsAttributeQualification folderDocNameAttrbQual = null;
                DbsSearchQualification combinedDescAttrQual = null;
                //DbsAttributeQualification versionDescAttrQual = new DbsAttributeQualification();
                String searchDescription="%";
                StringTokenizer tokenizer = new StringTokenizer(advanceSearchForm.getTxtDocDescription().trim());
                while (tokenizer.hasMoreTokens()) {
                  folderDocNameAttrbQual = new DbsAttributeQualification();
                  String currentToken = tokenizer.nextToken();
                  String searchColumn = dbsPublicObject.DESCRIPTION_ATTRIBUTE;
                  //String searchColForFam = DbsFamily.DESCRIPTION_ATTRIBUTE;
                  folderDocNameAttrbQual.setCaseIgnored(true);
                  //versionDescAttrQual.setCaseIgnored(true);
                  folderDocNameAttrbQual.setAttribute(searchColumn);
                  //versionDescAttrQual.setAttribute(searchColForFam);
                  folderDocNameAttrbQual.setOperatorType(DbsAttributeQualification.LIKE);
                  //versionDescAttrQual.setOperatorType(DbsAttributeQualification.LIKE);
                  currentToken = currentToken.replace('*','%');
                  currentToken = currentToken.replace('?','_');
                  
                  searchDescription = ('%') + currentToken + ('%');
                  logger.debug("searchDescription :" + searchDescription);
                  folderDocNameAttrbQual.setValue(searchDescription);
                  if (combinedDescAttrQual == null) {
                      combinedDescAttrQual = folderDocNameAttrbQual;
                  }else{
                      combinedDescAttrQual = new DbsSearchClause(combinedDescAttrQual, folderDocNameAttrbQual, DbsSearchClause.OR);
                  }
                  folderDocNameAttrbQual = null;
                }  
                //folderDocNameAttrbQual.setValue(searchDescription);
                //versionDescAttrQual.setValue(searchDescription);
                searchQualificationList.add(combinedDescAttrQual);
                //searchQualificationList.add(versionDescAttrQual);
                
            }

//--------------------------------------------------------------------------------------------------------------------
            
            // Search Specification
            DbsAttributeSearchSpecification attributeSearchSpec = null;
            DbsContextSearchSpecification contextSearchSpec = null;
            if (advanceSearchForm.getTxtContainingText() != null && advanceSearchForm.getTxtContainingText().trim().length() != 0)	{
                // search on content
                DbsContextQualification contextQualification = new DbsContextQualification();
                contextQualification.setQuery(advanceSearchForm.getTxtContainingText().trim());
                searchQualificationList.add(contextQualification);
			
                // build ContextSearchSpecification
                contextSearchSpec = new DbsContextSearchSpecification();
                contextSearchSpec.setContextClassname(DbsContentObject.CLASS_NAME);
                //attributeSearchSpec = contextSearchSpec;
                String [] classes = new String[] {DbsDocument.CLASS_NAME,DbsContentObject.CLASS_NAME,DbsPublicObject.CLASS_NAME};
                if (folderRestrictQual != null) {
                    folderRestrictQual.setSearchClassname(DbsPublicObject.CLASS_NAME);
                }
                DbsSearchClassSpecification searchClassSpec = 	new DbsSearchClassSpecification(classes);
                searchClassSpec.addResultClass(DbsPublicObject.CLASS_NAME);
                //attributeSearchSpec.setSearchClassSpecification(searchClassSpec);
                contextSearchSpec.setSearchClassSpecification(searchClassSpec);

                // join DbsDocument and DbsContentObject
                DbsJoinQualification documentContentJoinQuali = new DbsJoinQualification();
                documentContentJoinQuali.setLeftAttribute(DbsDocument.CLASS_NAME, DbsDocument.CONTENTOBJECT_ATTRIBUTE);
                documentContentJoinQuali.setRightAttribute(DbsContentObject.CLASS_NAME, null);
                searchQualificationList.add(documentContentJoinQuali);

                DbsJoinQualification versionDocJoinQuali = new DbsJoinQualification();
                versionDocJoinQuali.setLeftAttribute(DbsPublicObject.CLASS_NAME, DbsPublicObject.RESOLVEDPUBLICOBJECT_ATTRIBUTE);
                versionDocJoinQuali.setRightAttribute(DbsDocument.CLASS_NAME, null);
                searchQualificationList.add(versionDocJoinQuali);

            }else{
                attributeSearchSpec = new DbsAttributeSearchSpecification();
                DbsSearchClassSpecification searchClassSpec;
                if((advanceSearchForm.isAdvancedOptionEnabled()) && (advanceSearchForm.isChkDateSelected() || advanceSearchForm.isChkDocTypeSelected() || advanceSearchForm.isChkSizeSelected())){
                    String [] classes = new String[] {DbsDocument.CLASS_NAME,DbsContentObject.CLASS_NAME,DbsPublicObject.CLASS_NAME};
                    if (folderRestrictQual != null) {
                        folderRestrictQual.setSearchClassname(DbsPublicObject.CLASS_NAME);
                    }
                    searchClassSpec = 	new DbsSearchClassSpecification(classes);
                    searchClassSpec.addResultClass(DbsPublicObject.CLASS_NAME);
                    // join DbsDocument and DbsContentObject
                    DbsJoinQualification documentContentJoinQuali = new DbsJoinQualification();
                    documentContentJoinQuali.setLeftAttribute(DbsDocument.CLASS_NAME, DbsDocument.CONTENTOBJECT_ATTRIBUTE);
                    documentContentJoinQuali.setRightAttribute(DbsContentObject.CLASS_NAME, null);
                    searchQualificationList.add(documentContentJoinQuali);

                    DbsJoinQualification versionDocJoinQuali = new DbsJoinQualification();
                    versionDocJoinQuali.setLeftAttribute(DbsPublicObject.CLASS_NAME, DbsPublicObject.RESOLVEDPUBLICOBJECT_ATTRIBUTE);
                    versionDocJoinQuali.setRightAttribute(DbsDocument.CLASS_NAME, null);
                    searchQualificationList.add(versionDocJoinQuali);

                }else{
                    // build AttributeSearchSpecification
                    String [] classes = new String[] {DbsPublicObject.CLASS_NAME};
                    searchClassSpec = new DbsSearchClassSpecification(classes);
                }
                attributeSearchSpec.setSearchClassSpecification(searchClassSpec);                
            }

//***************************************************************************************
//**If Advanced search option is selected then apply the filter and restrict the search**
//***************************************************************************************
        if(advanceSearchForm.isAdvancedOptionEnabled()){
            // To build search based on the Attribute From and To Date 
            if (advanceSearchForm.isChkDateSelected()){
                if(advanceSearchForm.getTxtFromDate() != null && advanceSearchForm.getTxtFromDate().trim().length() != 0 ){
                    DbsAttributeQualification fromDateAttrbQual = new DbsAttributeQualification();
                    if (advanceSearchForm.getCboDateOption() == AdvanceSearchAction.CREATEDATE){
                      fromDateAttrbQual.setAttribute(DbsPublicObject.CREATEDATE_ATTRIBUTE);
                    }else{
                      fromDateAttrbQual.setAttribute(DbsPublicObject.LASTMODIFYDATE_ATTRIBUTE);
                    }
                    fromDateAttrbQual.setOperatorType(DbsAttributeQualification.GREATER_THAN_EQUAL);

                    Date dtFromDate=DateHelperForFileSystem.parse(advanceSearchForm.getTxtFromDate(),DATE_FORMAT);

                    DbsAttributeValue dbsFromDateAttributeValue = DbsAttributeValue.newAttributeValue(dtFromDate);
                    fromDateAttrbQual.setValue(dbsFromDateAttributeValue, dbsLibrarySession);
                    fromDateAttrbQual.setDateComparisonLevel(DbsAttributeQualification.DATE_COMP_DAY);
                    searchQualificationList.add(fromDateAttrbQual);
                }
                if(advanceSearchForm.getTxtToDate() != null && advanceSearchForm.getTxtToDate().trim().length() != 0){
                    DbsAttributeQualification toDateAttrbQual = new DbsAttributeQualification();
                    if (advanceSearchForm.getCboDateOption() == AdvanceSearchAction.CREATEDATE){
                        toDateAttrbQual.setAttribute(DbsPublicObject.CREATEDATE_ATTRIBUTE);
                    }else{
                        toDateAttrbQual.setAttribute(DbsPublicObject.LASTMODIFYDATE_ATTRIBUTE);
                    }
                    toDateAttrbQual.setOperatorType(DbsAttributeQualification.LESS_THAN_EQUAL);
                    Date dtToDate=DateHelperForFileSystem.parse(advanceSearchForm.getTxtToDate(),DATE_FORMAT);
                      
                    DbsAttributeValue dbsToDateAttributeValue = DbsAttributeValue.newAttributeValue(dtToDate);
                    toDateAttrbQual.setValue(dbsToDateAttributeValue, dbsLibrarySession);
                    toDateAttrbQual.setDateComparisonLevel(DbsAttributeQualification.DATE_COMP_DAY);
                    searchQualificationList.add(toDateAttrbQual);
                }
            }
//--------------------------------------------------------------------------------------------------------------------
            if (advanceSearchForm.isChkDocTypeSelected()){
                DbsAttributeQualification formatAttrQual = new DbsAttributeQualification();
                formatAttrQual.setAttribute(DbsContentObject.CLASS_NAME ,DbsContentObject.FORMAT_ATTRIBUTE);
                formatAttrQual.setOperatorType(DbsAttributeQualification.EQUAL);
                DbsFormat format = (DbsFormat)dbsLibrarySession.getSystemObject(advanceSearchForm.getCboDocType());
                formatAttrQual.setValue(DbsAttributeValue.newAttributeValue(format));
                searchQualificationList.add(formatAttrQual);
            }


            // To build search based on the Attribute docSize of the document.
            if (advanceSearchForm.isChkSizeSelected()){
                if(advanceSearchForm.getTxtDocSize() > 0){
                
                    DbsAttributeQualification docSizeAttrbQual = new DbsAttributeQualification();
                    docSizeAttrbQual.setAttribute(DbsContentObject.CLASS_NAME ,DbsContentObject.CONTENTSIZE_ATTRIBUTE);
                    if (advanceSearchForm.getCboSizeOption() == AdvanceSearchAction.ATLEAST){
                      docSizeAttrbQual.setOperatorType(DbsAttributeQualification.GREATER_THAN_EQUAL);
                    }else{
                      docSizeAttrbQual.setOperatorType(DbsAttributeQualification.LESS_THAN_EQUAL);
                    }
                    long docSize=advanceSearchForm.getTxtDocSize() * 1024;
                    DbsAttributeValue dbsSizeAttributeValue = DbsAttributeValue.newAttributeValue(docSize);
                    docSizeAttrbQual.setValue(dbsSizeAttributeValue, dbsLibrarySession);
                    searchQualificationList.add(docSizeAttrbQual);
                }
            }
            if(advanceSearchForm.isChkAdvanceFeatureSelected()){
                if(!advanceSearchForm.isChkSubFoldersSearch()){
                    folderRestrictQual.setMultiLevel(false);
                }

                if(!advanceSearchForm.isChkCaseSensitiveSearch()){

                }
            }
            
        }
            //And together all the search qualifications
            DbsSearchQualification searchQual = null;
            Iterator iterator = searchQualificationList.iterator();
            while (iterator.hasNext()){
                DbsSearchQualification nextSearchQualification = (DbsSearchQualification) iterator.next();
                if (searchQual == null) {
                    searchQual = nextSearchQualification;
                }else{
                    searchQual = new DbsSearchClause(searchQual, nextSearchQualification, DbsSearchClause.AND);
                }
            }

            if(contextSearchSpec!=null){
                contextSearchSpec.setSearchQualification((DbsSearchClause)searchQual);
                DbsSearchSortSpecification searchSortSpec = new DbsSearchSortSpecification();
                searchSortSpec.add(DbsPublicObject.NAME_ATTRIBUTE, true);
                contextSearchSpec.setSearchSortSpecification(searchSortSpec);
		
                // create and run the search
                dbsSearch = new DbsSearch(dbsLibrarySession, contextSearchSpec);
            }else{
                attributeSearchSpec.setSearchQualification(searchQual);
                DbsSearchSortSpecification searchSortSpec = new DbsSearchSortSpecification();
                searchSortSpec.add(DbsPublicObject.NAME_ATTRIBUTE, true);
                attributeSearchSpec.setSearchSortSpecification(searchSortSpec);
                
                // create and run the search
                dbsSearch = new DbsSearch(dbsLibrarySession, attributeSearchSpec);
            }
        }catch(DbsException dbsException){
            throw dbsException;
        }
        //logger.debug(dbsSearch.getSQL());
        return dbsSearch;
      }
}