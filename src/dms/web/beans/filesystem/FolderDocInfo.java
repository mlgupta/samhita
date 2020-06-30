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
 * $Id: FolderDocInfo.java,v 20040220.45 2006/05/19 06:24:33 suved Exp $
 *****************************************************************************
 */

package dms.web.beans.filesystem;
/* dms package references */ 
import dms.beans.DbsException;
import dms.beans.DbsFolder;
import dms.beans.DbsLibrarySession;
/* Java API */
import java.util.ArrayList;
/* Logger API */
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
/**
 *	Purpose           : To hold information regarding folder and document
 *  @author           : Jeetendra Prasad
 *  @version          : 1.0
 * 	Date of creation  : 20-01-2004
 * 	Last Modified by  : Suved Mishra    
 * 	Last Modified Date: 18-04-2006   
 */
public class FolderDocInfo  {
  private Long homeFolderId;
  private Long currentFolderId;
  private String currentFolderPath;
  private String jsFileName;
  private boolean backButtonDisabled;
  private boolean forwardButtonDisabled;
  private DbsArrayList nevigationHistory;
  private int nevigationPointer;
  private int pageNumber;
  private Integer pageCount;
  private Long[] clipBoard;
  private byte clipBoardContent;
  public static final byte CLIPBOARD_CONTENT_CUT = 1;
  public static final byte CLIPBOARD_CONTENT_COPY = 2;
  private int listingType;
  public static final byte SIMPLE_LISTING = 1;
  public static final byte DISPLAY_PAGE = 2;
  public static final byte SEARCH_LISTING = 3;    
  private boolean treeVisible;
  //private boolean boolChkDocOverWrite = false;
  private Long docId;
  private DbsLibrarySession dbsLibrarySession;
  private ArrayList listOfParents;
  private ArrayList listOfParentsId;
  private boolean noReloadTree;
  private int hierarchySetNo;
//    private String davPath;
  public FolderDocInfo() {
    nevigationHistory = new DbsArrayList();
    nevigationPointer = 0;
    hierarchySetNo = 1;
    backButtonDisabled = true;
    forwardButtonDisabled = true;
    listingType = FolderDocInfo.SIMPLE_LISTING;
    treeVisible = true;
    listOfParents = new ArrayList();
    listOfParentsId = new ArrayList();
    noReloadTree = false;
  }

  public Long getCurrentFolderId() {
    return currentFolderId;
  }

  public void setCurrentFolderId(Long newCurrentFolderId) {
    currentFolderId = newCurrentFolderId;
  }

  public Long getHomeFolderId() {
    return homeFolderId;
  }

  public void setHomeFolderId(Long newHomeFolderId) {
    homeFolderId = newHomeFolderId;
  }

  public String getJsFileName() {
    return jsFileName;
  }

  public void setJsFileName(String newJsFileName) {
    jsFileName = newJsFileName;
  }

  public String getCurrentFolderPath() {
    return currentFolderPath;
  }

  public void setCurrentFolderPath(String newCurrentFolderPath) {
    currentFolderPath = newCurrentFolderPath;
  }

  public boolean isBackButtonDisabled() {
    return backButtonDisabled;
  }

  public void setBackButtonDisabled(boolean newBackButtonDisabled) {
    backButtonDisabled = newBackButtonDisabled;
  }

  public boolean isForwardButtonDisabled() {
    return forwardButtonDisabled;
  }

  public void setForwardButtonDisabled(boolean newForwardButtonDisabled) {
    forwardButtonDisabled = newForwardButtonDisabled;
  }

  public void addFolderDocId(Long newFolderDocId){
      //clear the nevigation history from the current position onward if user finishes the 
      //nevigation in the history and starts the nevigation again
/*        
      if (nevigationPointer > 0 && nevigationPointer < nevigationHistory.size() -1 ){
          nevigationHistory.removeRange(nevigationPointer + 1,nevigationHistory.size());
      }
*/        
    nevigationHistory.add(newFolderDocId);
    nevigationPointer = nevigationHistory.size() -1;
    //disable back and forward button when one one path is there in the nevigation history else enable back 
    //button and disable forward button
    if (nevigationHistory.size() == 1){
      backButtonDisabled = true;    
      backButtonDisabled = true;    
    }else{
      backButtonDisabled = false;
      forwardButtonDisabled = true;
    }
  }

/**
 * Purpose   : To get previous folder path
 * @param    : no paramater
 * @logic    : this function returns the previous folder path. One point to note is that it will 
 *             disable the back button if previous to previous folder path is not available.
 */
  public Long getPrevFolderDocId(){
    Long prevFolderDocId = null;
    DbsFolder dbsFolder = null;
    if(nevigationPointer > 0){
      nevigationPointer = nevigationPointer -1;
      prevFolderDocId = (Long)nevigationHistory.get(nevigationPointer);
      forwardButtonDisabled = false;
    }
    if(nevigationPointer == 0){
      backButtonDisabled = true;
    }
    try{
      if(prevFolderDocId != null){
        dbsFolder = (DbsFolder)dbsLibrarySession.getPublicObject(
                                                 prevFolderDocId);
      }
    }catch(DbsException dex){
      nevigationHistory.remove(nevigationPointer);
      prevFolderDocId = getPrevFolderDocId();
    }catch(Exception ex){
      nevigationHistory.remove(nevigationPointer);
      prevFolderDocId = getPrevFolderDocId();
    }
    return prevFolderDocId;
  }

/**
 * Purpose   : To get next folder path
 * @param    : no paramater
 * @logic    : this function returns the previous folder path. One point to note is that it will 
 *             disable the forward button if next to next folder path is not available.
 */
  public Long getNextFolderDocId(){
    Long nextFolderDocId = null;
    DbsFolder dbsFolder = null;
    if(nevigationPointer < nevigationHistory.size() -1){
      nevigationPointer = nevigationPointer + 1;
      nextFolderDocId = (Long)nevigationHistory.get(nevigationPointer);
      backButtonDisabled = false;
    }
    if(nevigationPointer == nevigationHistory.size() -1 ){
      forwardButtonDisabled = true;
    }
    try{
      if(nextFolderDocId != null){
        dbsFolder = (DbsFolder)dbsLibrarySession.getPublicObject(
                                                 nextFolderDocId);
      }else{
        nextFolderDocId = getPrevFolderDocId();
      }
    }catch(DbsException dex){
      nevigationHistory.remove(nevigationPointer);
      nextFolderDocId = getNextFolderDocId();
    }catch(Exception ex){
      nevigationHistory.remove(nevigationPointer);
      nextFolderDocId = getNextFolderDocId();
    }
    return nextFolderDocId;
  }

  public void initializeNevigation(){
    nevigationHistory.clear();
    nevigationPointer = 0;
    backButtonDisabled = true;
    forwardButtonDisabled = true;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(int newPageNumber) {
    pageNumber = newPageNumber;
  }

  public Integer getPageCount() {
    return pageCount;
  }

  public void setPageCount(Integer newPageCount) {
    pageCount = newPageCount;
  }

  public Long[] getClipBoard() {
    return clipBoard;
  }

  public void setClipBoard(Long[] newClipBoard) {
    clipBoard = newClipBoard;
  }

  public byte getClipBoardContent() {
    return clipBoardContent;
  }

  public void setClipBoardContent(byte newClipBoardContent) {
    clipBoardContent = newClipBoardContent;
  }

  public String toString(){
    String strTemp = "";
    Logger logger = Logger.getLogger("DbsLogger");
    if(logger.getLevel() == Level.DEBUG){
      String strArrayValues = "";
      strTemp += "\n\thomeFolderId : " + homeFolderId;
      strTemp += "\n\tcurrentFolderId : " + currentFolderId;
      strTemp += "\n\tcurrentFolderPath : " + currentFolderPath;
      strTemp += "\n\tjsFileName : " + jsFileName;
  
      if(nevigationHistory != null){
        strArrayValues = "{";
        for(int index = 0; index < nevigationHistory.size(); index++){
          strArrayValues += " " + (Long)nevigationHistory.get(index);
        }
        strArrayValues += "}";
        strTemp += "\n\tnavigationHistory : " + strArrayValues;
      }else{
        strTemp += "\n\tnavigationHistory : " + nevigationHistory;
      }

      strTemp += "\n\tnavigationPointer : " + nevigationPointer;
      strTemp += "\n\tbackButtonDisabled : " + backButtonDisabled;
      strTemp += "\n\tforwardButtonDisabled : " + forwardButtonDisabled;
      strTemp += "\n\tpageCount : " + pageCount;
      strTemp += "\n\tpageNumber : " + pageNumber;

      if(clipBoard != null){
        strArrayValues = "{";
        for(int index = 0; index < clipBoard.length; index++){
          strArrayValues += " " + clipBoard[index];
        }
        strArrayValues += "}";
        strTemp += "\n\tclipBoard : " + strArrayValues;            
      }else{
        strTemp += "\n\tclipBoard : " + clipBoard;
      }
  
      switch(clipBoardContent){
        case FolderDocInfo.CLIPBOARD_CONTENT_COPY:
          strTemp += "\n\tclipBoardContent : FolderDocInfo.CLIPBOARD_CONTENT_COPY" ;        
          break;
        case FolderDocInfo.CLIPBOARD_CONTENT_CUT:
          strTemp += "\n\tclipBoardContent : FolderDocInfo.CLIPBOARD_CONTENT_CUT" ;
          break;
        default:
          strTemp += "\n\tclipBoardContent : " + clipBoardContent;
      }

      strTemp += "\n\tlistingType : " + listingType;
      strTemp += "\n\ttreeVisible : " + treeVisible;
      strTemp += "\n\tnoReloadTree  : " +isNoReloadTree();
      strTemp += "\n\thierarchySetNo  : " +getHierarchySetNo();
    }        
    return strTemp;
  }

  public int getListingType() {
    return listingType;
  }

  public void setListingType(int newListingType) {
    listingType = newListingType;
  }

  public boolean isTreeVisible() {
    return treeVisible;
  }

  public void setTreeVisible(boolean newTreeVisible) {
    treeVisible = newTreeVisible;
  }
  
  /*public boolean getBoolChkDocOverWrite(){
    return boolChkDocOverWrite;
  }
  
  public void setBoolChkDocOverWrite(boolean newBoolChkDocOverWrite){
    boolChkDocOverWrite = newBoolChkDocOverWrite;
  }*/

  public Long getDocId() {
    return docId;
  }

  public void setDocId(Long newDocId) {
    docId = newDocId;
  }

  public DbsLibrarySession getDbsLibrarySession() {
    return dbsLibrarySession;
  }

  public void setDbsLibrarySession(DbsLibrarySession newDbsLibrarySession) {
    dbsLibrarySession = newDbsLibrarySession;
  }

  public ArrayList getListOfParents() {
    return listOfParents;
  }

  public void setListOfParents(ArrayList newListOfParents) {
    listOfParents = newListOfParents;
  }

  public ArrayList getListOfParentsId() {
    return listOfParentsId;
  }

  public void setListOfParentsId(ArrayList newListOfParentsId) {
    listOfParentsId = newListOfParentsId;
  }

  public boolean isNoReloadTree() {
    return noReloadTree;
  }

  public void setNoReloadTree(boolean noReloadTree) {
    this.noReloadTree = noReloadTree;
  }

  public int getHierarchySetNo() {
    return hierarchySetNo;
  }

  public void setHierarchySetNo(int hierarchySetNo) {
    this.hierarchySetNo = hierarchySetNo;
  }

/*    public String getDavPath(){
      return davPath;
  }

  public void setDavPath(String davPath) {
      this.davPath = davPath;
  }*/
}


  class DbsArrayList extends ArrayList{
    public DbsArrayList(){
  
  }

  public void removeRange(int fromIndex, int toIndex){
    try{
      super.removeRange(fromIndex,toIndex);            
    }catch(Exception ex){
//            System.out.println(ex);
    }
  }
}
