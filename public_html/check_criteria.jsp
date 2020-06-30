<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>

<jsp:useBean id="FolderDocInfo" scope="session" class="dms.web.beans.filesystem.FolderDocInfo" type="dms.web.beans.filesystem.FolderDocInfo"  />
<jsp:useBean id="UserInfo" scope="session" class="dms.web.beans.user.UserInfo" type="dms.web.beans.user.UserInfo" />

<%

  String ajaxResponse = new String();
  String currentFolderPath = FolderDocInfo.getCurrentFolderPath();
  Long currentFolderId = FolderDocInfo.getCurrentFolderId();
  String davPathForFolder = UserInfo.getDavPath()+currentFolderPath+"/";
  
  if( !FolderDocInfo.isBackButtonDisabled() ){
    ajaxResponse += "<a onclick='folderBack();' class='imgBack' title='Back' onmouseover=\"this.className='imgBackOver'\" onmouseout=\"this.className='imgBack'\" ></a>";
  }else{
    ajaxResponse +="<div class='imgBackDisable' ></div>";
  }

  if(!FolderDocInfo.isForwardButtonDisabled()){
    ajaxResponse += "<a onclick='folderForward();' class='imgForward' title='Forward' onmouseover=\"this.className='imgForwardOver'\" onmouseout=\"this.className='imgForward'\" ></a>";
  }else{
    ajaxResponse += "<div class='imgForwardDisable' ></div>";
  }

  if( !currentFolderPath.equals("/") ){
    ajaxResponse += "<a onclick='folderUp();' class='imgGoUp' title='Up' onmouseover=\"this.className='imgGoUpOver'\" onmouseout=\"this.className='imgGoUp'\" ></a>";
  }else{
    ajaxResponse += "<div class='imgGoUpDisable' ></div>";
  }
  
  ajaxResponse += "|";
  
  if( FolderDocInfo.getListingType() == FolderDocInfo.SIMPLE_LISTING ){
    ajaxResponse += "<div style='float:left' >";
    ajaxResponse += "<table border='0' cellpadding='0' cellspacing='0' >" ;
    ajaxResponse += "<tr>";
    ajaxResponse += "<td>";
    ajaxResponse += "<a id='newDoc' onclick='return newDocument_onclick();' class='imgNewDoc' onmouseout=\"this.className='imgNewDoc';return newdoclayer_onmouseout();\" onmouseover=\"this.className='imgNewDocOver';return newdoclayer_onmouseover();\" style='margin-left:5px;' title='Create New Document' ></a>";
    ajaxResponse += "</td>";            
    ajaxResponse += "</tr>";            
    ajaxResponse += "<tr>";
    ajaxResponse += "<td>";
    ajaxResponse += "<div>";
    ajaxResponse += "<div id='lyrNewDocument' class='imgToolBar bgClrLvl_3 borderClrLvl_2' style='background-repeat:repeat-y;margin-top:4px;margin-left:4px;display:none;position:absolute;width:100px;z-index:2'>";
    ajaxResponse += "<table align='center' width='100%' border='0' cellpadding='1' cellspacing='2' >";
    ajaxResponse += "<tr>";
    ajaxResponse += "<td>";
    StringBuffer sb = new StringBuffer();
    sb.append("<a onclick=openNewDoc('"+davPathForFolder+"') ");
    ajaxResponse += sb.toString();
    ajaxResponse += "class='iconWord' onmouseout=\"this.className='iconWord';return newdoclayer_onmouseout();\" onmouseover=\"this.className='iconWordOver';return newdoclayer_onmouseover();\" title='Create New MS Word Document' ></a>";
    ajaxResponse += "</td>";            
    ajaxResponse += "<td>";
    sb = new StringBuffer();
    sb.append("<a onclick=openNewExcel('"+davPathForFolder+"') ");
    ajaxResponse += sb.toString();
    ajaxResponse += "class='iconExcel' onmouseout=\"this.className='iconExcel';return newdoclayer_onmouseout();\" onmouseover=\"this.className='iconExcelOver';return newdoclayer_onmouseover();\" title='Create New MS Excel Document' ></a>";
    ajaxResponse += "</td>";            
    ajaxResponse += "<td>";
    sb = new StringBuffer();
    sb.append("<a onclick=openNewPowerPoint('"+davPathForFolder+"') ");
    ajaxResponse += sb.toString();
    ajaxResponse += "class='iconPpt' onmouseout=\"this.className='iconPpt';return newdoclayer_onmouseout();\" onmouseover=\"this.className='iconPptOver';return newdoclayer_onmouseover();\" title='Create New MS PowerPoint Presentation' ></a>";
    ajaxResponse += "</td>";            
    ajaxResponse += "</tr>";            
    ajaxResponse += "<tr>";
    ajaxResponse += "<td>";
    sb = new StringBuffer();
    sb.append("<a onclick=openNewText('"+davPathForFolder+"') ");
    ajaxResponse += sb.toString();
    ajaxResponse += "class='iconTxt' onmouseout=\"this.className='iconTxt';return newdoclayer_onmouseout();\" onmouseover=\"this.className='iconTxtOver';return newdoclayer_onmouseover();\" title='Create New MS Text Document' ></a>";
    ajaxResponse += "</td>";            
    ajaxResponse += "<td>";
    ajaxResponse += "</td>";            
    ajaxResponse += "<td>&nbsp</td>";
    ajaxResponse += "</tr>";            
    ajaxResponse += "</table>";
    ajaxResponse += "</div>";    
    ajaxResponse += "</div>";    
    ajaxResponse += "</td>";            
    ajaxResponse += "</tr>";            
    ajaxResponse += "</table>";
    ajaxResponse += "</div>";    
    ajaxResponse += "<a onclick='folderNew();' class='imgNewFolder' onmouseout=\"this.className='imgNewFolder'\" onmouseover=\"this.className='imgNewFolderOver'\" style='margin-left:3px;' title='Create a Folder' ></a>";
    ajaxResponse += "<a onclick='folderDocRename();' class='imgRename' onmouseout=\"this.className='imgRename'\" onmouseover=\"this.className='imgRenameOver'\" title='Rename Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<a onclick='docUpload();' class='imgUpload' onmouseout=\"this.className='imgUpload'\" onmouseover=\"this.className='imgUploadOver'\" title='Upload Document(s)' ></a>";
    ajaxResponse += "<a onclick='docDownload();' class='imgDownload' onmouseout=\"this.className='imgDownload'\" onmouseover=\"this.className='imgDownloadOver'\" title='Download Document(s)' ></a>";
    ajaxResponse += "<a onclick='folderDocApplyAcl();' class='imgApplyACL' onmouseout=\"this.className='imgApplyACL'\" onmouseover=\"this.className='imgApplyACLOver'\" title='Apply ACL to Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<a onclick='folderDocProperty();' class='imgProperty' onmouseout=\"this.className='imgProperty'\" onmouseover=\"this.className='imgPropertyOver'\" title='Property of Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<a onclick='generateLinks();' class='imgGenerateLink' onmouseout=\"this.className='imgGenerateLink'\" onmouseover=\"this.className='imgGenerateLinkOver'\" title='Generate Link(s)' ></a>";
    ajaxResponse += "<a onclick='auditLog();' class='imgViewLog' onmouseout=\"this.className='imgViewLog'\" onmouseover=\"this.className='imgViewLogOver'\" title='View Document Log' ></a>";
    ajaxResponse += "<a onclick='addWatch();' class='imgAddWatch' onmouseout=\"this.className='imgAddWatch'\" onmouseover=\"this.className='imgAddWatchOver'\" title='Add Watch' ></a>";
    ajaxResponse += "<a onclick='viewAsHtml();' class='imgViewHtml' onmouseout=\"this.className='imgViewHtml'\" onmouseover=\"this.className='imgViewHtmlOver'\" style='margin-right:3px;' title='View As HTML' ></a>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<a onclick='folderDocCut();' class='imgCut' onmouseout=\"this.className='imgCut'\" onmouseover=\"this.className='imgCutOver'\" style='margin-left:3px;' title='Cut Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<a onclick='folderDocCopy();' class='imgCopy' onmouseout=\"this.className='imgCopy'\" onmouseover=\"this.className='imgCopyOver'\" title='Copy Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<a onclick='folderDocPaste();' class='imgPaste' onmouseout=\"this.className='imgPaste'\" onmouseover=\"this.className='imgPasteOver'\" title='Paste Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<a onclick='folderDelete();' class='imgDeleteFolder' onmouseout=\"this.className='imgDeleteFolder'\" onmouseover=\"this.className='imgDeleteFolderOver'\" style='margin-right:3px;' title='Delete Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<a onclick=\"b4CopyMoveFolderDocTo('COPY');\" class='imgCopyTo' onmouseout=\"this.className='imgCopyTo'\" onmouseover=\"this.className='imgCopyToOver'\" style='margin-left:3px;' title='Copy Folder(s)/Document(s) To...' ></a>";
    ajaxResponse += "<a onclick=\"b4CopyMoveFolderDocTo('MOVE');\" class='imgMoveTo' onmouseout=\"this.className='imgMoveTo'\" onmouseover=\"this.className='imgMoveToOver'\" style='margin-right:3px;' title='Move Folder(s)/Document(s) To...' ></a>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<a onclick='docMakeVersionable();' class='imgVersionable' onmouseout=\"this.className='imgVersionable'\" onmouseover=\"this.className='imgVersionableOver'\" style='margin-left:3px;' title='Make Document(s) Versionable' ></a>";
    ajaxResponse += "<a onclick='folderDocCheckOut();' class='imgChkOut' onmouseout=\"this.className='imgChkOut'\" onmouseover=\"this.className='imgChkOutOver'\" title='Check-Out Document(s)' ></a>";
    ajaxResponse += "<a onclick='folderDocCheckIn();' class='imgChkIn' onmouseout=\"this.className='imgChkIn'\" onmouseover=\"this.className='imgChkInOver'\" title='Check-In Document(s)' ></a>";
    ajaxResponse += "<a onclick='folderDocCancelCheckout();' class='imgUndoChkOut' onmouseout=\"this.className='imgUndoChkOut'\" onmouseover=\"this.className='imgUndoChkOutOver'\" title='Undo Check-Out Document(s)' ></a>";
    ajaxResponse += "<a onclick='docHistory();' class='imgHistory' onmouseout=\"this.className='imgHistory'\" onmouseover=\"this.className='imgHistoryOver'\" style='margin-right:3px;' title='Version History of Document(s)' ></a>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<a onclick='docEncryptAction();' style='margin-left:3px' class='imgEncrypt' onmouseout=\"this.className='imgEncrypt'\" onmouseover=\"this.className='imgEncryptOver'\" style='margin-left:3px;' title='Encrypt Document(s)' ></a>";
    ajaxResponse += "<a onclick='docDecryptAction();' class='imgDecrypt' onmouseout=\"this.className='imgDecrypt'\" onmouseover=\"this.className='imgDecryptOver'\" style='margin-right:3px;' title='Decrypt Document(s)' ></a>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<a onclick='docZipAction();' style='margin-left:3px' class='imgZip' onmouseout=\"this.className='imgZip'\" onmouseover=\"this.className='imgZipOver'\" style='margin-left:3px;' title='Zip Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<a onclick='docUnZipAction();' class='imgUnZip' onmouseout=\"this.className='imgUnZip'\" onmouseover=\"this.className='imgUnZipOver'\" style='margin-right:3px;' title='Un-Zip Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<a onclick='docMailAction();' style='margin-left:3px' class='imgMailTo' onmouseout=\"this.className='imgMailTo'\" onmouseover=\"this.className='imgMailToOver'\" style='margin-left:3px;' title='Mail Document(s)' ></a>";
    ajaxResponse += "<a onclick='docFaxAction();' class='imgFaxTo' onmouseout=\"this.className='imgFaxTo'\" onmouseover=\"this.className='imgFaxToOver'\" style='margin-right:3px;' title='Fax Document(s)' ></a>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<a onclick='submitDocToWf();' style='margin-left:3px' class='imgWorkflowSubmit' onmouseout=\"this.className='imgWorkflowSubmit'\" onmouseover=\"this.className='imgWorkflowSubmitOver'\" style='margin-left:3px;' title='Submit To Workflow' ></a>";
    ajaxResponse += "|";
    ajaxResponse += "menu11,menu12,menu13,menu14,menu15,menu16,menu17,menu18,menu19,menu110,menu111,menu112,menu113,menu114,menu115,menu116,menu21,menu22,menu23,menu24,menu25,menu26,menu27,menu28,menu29,menu31,menu32,menu33,menu34,menu35";
    ajaxResponse += "|";
    ajaxResponse += "folderNew(),folderDocApplyAcl(),docUpload(),docDownload(),folderDocProperty(),docEncryptAction(),docDecryptAction(),docZipAction(),docUnZipAction(),docMailAction(),docFaxAction(),generateLinks(),auditLog(),submitDocToWf(),addWatch(),viewAsHtml(),folderDocRename(),folderDocCut(),folderDocCopy(),folderDocPaste(),folderDelete(),b4CopyMoveFolderDocTo('COPY'),b4CopyMoveFolderDocTo('MOVE'),checkAll(true),invertSelection(),docMakeVersionable(),folderDocCheckOut(),folderDocCheckIn(),folderDocCancelCheckout(),docHistory()";
  }else if( FolderDocInfo.getListingType() == FolderDocInfo.SEARCH_LISTING ){
    ajaxResponse += "<div class='imgNewDocDisable' style='float-left;' ></div>";  
    ajaxResponse += "<div class='imgNewFolderDisable' style='margin-left:3px;' ></div>";  
    ajaxResponse += "<a onclick='folderDocRename();' class='imgRename' onmouseout=\"this.className='imgRename'\" onmouseover=\"this.className='imgRenameOver'\" title='Rename Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<div class='imgUploadDisable' ></div>";  
    ajaxResponse += "<div class='imgDownloadDisable' ></div>";  
    ajaxResponse += "<a onclick='folderDocApplyAcl();' class='imgApplyACL' onmouseout=\"this.className='imgApplyACL'\" onmouseover=\"this.className='imgApplyACLOver'\" title='Apply ACL to Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<a onclick='folderDocProperty();' class='imgProperty' onmouseout=\"this.className='imgProperty'\" onmouseover=\"this.className='imgPropertyOver'\" title='Property of Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<a onclick='generateLinks();' class='imgGenerateLink' onmouseout=\"this.className='imgGenerateLink'\" onmouseover=\"this.className='imgGenerateLinkOver'\" title='Generate Link(s)' ></a>";
    ajaxResponse += "<a onclick='auditLog();' class='imgViewLog' onmouseout=\"this.className='imgViewLog'\" onmouseover=\"this.className='imgViewLogOver'\" title='View Document Log' ></a>";
    ajaxResponse += "<a onclick='addWatch();' class='imgAddWatch' onmouseout=\"this.className='imgAddWatch'\" onmouseover=\"this.className='imgAddWatchOver'\" title='Add Watch' ></a>";
    ajaxResponse += "<a onclick='viewAsHtml();' class='imgViewHtml' onmouseout=\"this.className='imgViewHtml'\" onmouseover=\"this.className='imgViewHtmlOver'\" style='margin-right:3px;' title='View As HTML' ></a>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<a onclick='folderDocCut();' class='imgCut' onmouseout=\"this.className='imgCut'\" onmouseover=\"this.className='imgCutOver'\" style='margin-left:3px;' title='Cut Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<a onclick='folderDocCopy();' class='imgCopy' onmouseout=\"this.className='imgCopy'\" onmouseover=\"this.className='imgCopyOver'\" title='Copy Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<div class='imgPasteDisable' ></div>";  
    ajaxResponse += "<a onclick='folderDelete();' class='imgDeleteFolder' onmouseout=\"this.className='imgDeleteFolder'\" onmouseover=\"this.className='imgDeleteFolderOver'\" style='margin-right:3px;' title='Delete Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<a onclick=\"b4CopyMoveFolderDocTo('COPY');\" class='imgCopyTo' onmouseout=\"this.className='imgCopyTo'\" onmouseover=\"this.className='imgCopyToOver'\" style='margin-left:3px;' title='Copy Folder(s)/Document(s) To...' ></a>";
    ajaxResponse += "<a onclick=\"b4CopyMoveFolderDocTo('MOVE');\" class='imgMoveTo' onmouseout=\"this.className='imgMoveTo'\" onmouseover=\"this.className='imgMoveToOver'\" style='margin-right:3px;' title='Move Folder(s)/Document(s) To...' ></a>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<a onclick='docMakeVersionable();' class='imgVersionable' onmouseout=\"this.className='imgVersionable'\" onmouseover=\"this.className='imgVersionableOver'\" style='margin-left:3px;' title='Make Document(s) Versionable' ></a>";
    ajaxResponse += "<a onclick='folderDocCheckOut();' class='imgChkOut' onmouseout=\"this.className='imgChkOut'\" onmouseover=\"this.className='imgChkOutOver'\" title='Check-Out Document(s)' ></a>";
    ajaxResponse += "<a onclick='folderDocCheckIn();' class='imgChkIn' onmouseout=\"this.className='imgChkIn'\" onmouseover=\"this.className='imgChkInOver'\" title='Check-In Document(s)' ></a>";
    ajaxResponse += "<a onclick='folderDocCancelCheckout();' class='imgUndoChkOut' onmouseout=\"this.className='imgUndoChkOut'\" onmouseover=\"this.className='imgUndoChkOutOver'\" title='Undo Check-Out Document(s)' ></a>";
    ajaxResponse += "<a onclick='docHistory();' class='imgHistory' onmouseout=\"this.className='imgHistory'\" onmouseover=\"this.className='imgHistoryOver'\" style='margin-right:3px;' title='Version History of Document(s)' ></a>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<a onclick='docEncryptAction();' style='margin-left:3px' class='imgEncrypt' onmouseout=\"this.className='imgEncrypt'\" onmouseover=\"this.className='imgEncryptOver'\" style='margin-left:3px;' title='Encrypt Document(s)' ></a>";
    ajaxResponse += "<a onclick='docDecryptAction();' class='imgDecrypt' onmouseout=\"this.className='imgDecrypt'\" onmouseover=\"this.className='imgDecryptOver'\" style='margin-right:3px;' title='Decrypt Document(s)' ></a>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<a onclick='docZipAction();' style='margin-left:3px' class='imgZip' onmouseout=\"this.className='imgZip'\" onmouseover=\"this.className='imgZipOver'\" style='margin-left:3px;' title='Zip Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<a onclick='docUnZipAction();' class='imgUnZip' onmouseout=\"this.className='imgUnZip'\" onmouseover=\"this.className='imgUnZipOver'\" style='margin-right:3px;' title='Un-Zip Folder(s)/Document(s)' ></a>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<a onclick='docMailAction();' style='margin-left:3px' class='imgMailTo' onmouseout=\"this.className='imgMailTo'\" onmouseover=\"this.className='imgMailToOver'\" style='margin-left:3px;' title='Mail Document(s)' ></a>";
    ajaxResponse += "<a onclick='docFaxAction();' class='imgFaxTo' onmouseout=\"this.className='imgFaxTo'\" onmouseover=\"this.className='imgFaxToOver'\" style='margin-right:3px;' title='Fax Document(s)' ></a>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<a onclick='submitDocToWf();' style='margin-left:3px' class='imgWorkflowSubmit' onmouseout=\"this.className='imgWorkflowSubmit'\" onmouseover=\"this.className='imgWorkflowSubmitOver'\" style='margin-left:3px;' title='Submit To Workflow' ></a>";
    ajaxResponse += "|";
    ajaxResponse += "menu11,menu12,menu13,menu14,menu15,menu16,menu17,menu18,menu19,menu110,menu111,menu112,menu113,menu114,menu115,menu116,menu21,menu22,menu23,menu24,menu25,menu26,menu27,menu28,menu29,menu31,menu32,menu33,menu34,menu35";
    ajaxResponse += "|";
    ajaxResponse += "doNothing(),folderDocApplyAcl(),doNothing(),doNothing(),folderDocProperty(),docEncryptAction(),docDecryptAction(),docZipAction(),docUnZipAction(),docMailAction(),docFaxAction(),generateLinks(),auditLog(),submitDocToWf(),addWatch(),viewAsHtml(),folderDocRename(),folderDocCut(),folderDocCopy(),doNothing(),folderDelete(),b4CopyMoveFolderDocTo('COPY'),b4CopyMoveFolderDocTo('MOVE'),checkAll(true),invertSelection(),docMakeVersionable(),folderDocCheckOut(),folderDocCheckIn(),folderDocCancelCheckout(),docHistory()";
  }else{
    ajaxResponse += "<div class='imgNewDocDisable' style='float-left;' ></div>";  
    ajaxResponse += "<div class='imgNewFolderDisable' style='margin-left:3px;' ></div>";  
    ajaxResponse += "<div class='imgRenameDisable' ></div>";  
    ajaxResponse += "<div class='imgUploadDisable' ></div>";  
    ajaxResponse += "<div class='imgDownloadDisable' ></div>";  
    ajaxResponse += "<div class='imgApplyACLDisable' ></div>";  
    ajaxResponse += "<div class='imgPropertyDisable' ></div>";  
    ajaxResponse += "<div class='imgGenerateLinkDisable' ></div>";  
    ajaxResponse += "<div class='imgViewLogDisable' ></div>";  
    ajaxResponse += "<div class='imgAddWatchDisable' ></div>";  
    ajaxResponse += "<div class='imgViewHtmlDisable' style='margin-right:3px;' ></div>";  
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<div class='imgCutDisable' style='margin-left:3px;' ></div>";  
    ajaxResponse += "<div class='imgCopyDisable' ></div>";  
    ajaxResponse += "<div class='imgPasteDisable' ></div>";  
    ajaxResponse += "<div class='imgDeleteFolderDisable' style='margin-right:3px;' ></div>";  
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<div class='imgCopyToDisable' style='margin-left:3px;' ></div>";  
    ajaxResponse += "<div class='imgMoveToDisable' style='margin-right:3px;' ></div>";  
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<div class='imgVersionableDisable' style='margin-left:3px;' ></div>";  
    ajaxResponse += "<div class='imgChkOutDisable' ></div>";  
    ajaxResponse += "<div class='imgChkInDisable' ></div>";  
    ajaxResponse += "<div class='imgUndoChkOutDisable' ></div>";  
    ajaxResponse += "<div class='imgHistoryDisable' style='margin-right:3px;' ></div>";  
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<div class='imgEncryptDisable' style='margin-left:3px;' ></div>";  
    ajaxResponse += "<div class='imgDecryptDisable' style='margin-right:3px;' ></div>";  
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<div class='imgZipDisable' style='margin-left:3px;' ></div>";  
    ajaxResponse += "<div class='imgUnZipDisable' style='margin-right:3px;' ></div>";  
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<div class='imgMailToDisable' style='margin-left:3px;' ></div>";  
    ajaxResponse += "<div class='imgFaxToDisable' style='margin-right:3px;' ></div>";  
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_2'></div>";
    ajaxResponse += "<div style='float:left; width:1px;height:100%;' class='bgClrLvl_F'></div>";
    ajaxResponse += "<div class='imgWorkflowSubmitDisable' style='margin-left:3px;' ></div>";  
    ajaxResponse += "|";
    ajaxResponse += "menu11,menu12,menu13,menu14,menu15,menu16,menu17,menu18,menu19,menu110,menu111,menu112,menu113,menu114,menu115,menu116,menu21,menu22,menu23,menu24,menu25,menu26,menu27,menu28,menu29,menu31,menu32,menu33,menu34,menu35";
    ajaxResponse += "|";
    ajaxResponse += "doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing(),doNothing()";
  }
  
  out.print(ajaxResponse);

%>