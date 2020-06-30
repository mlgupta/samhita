<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="dms.web.beans.filesystem.FolderDocInfo" %>
<%@ page import="dms.web.beans.filesystem.FolderOperation" %>
<%@ page import="java.util.ArrayList" %>

<bean:define id="folderDocInfo" name="FolderDocInfo" type="dms.web.beans.filesystem.FolderDocInfo" />
<bean:define id="userPreferences" name="UserPreferences" type="dms.web.beans.user.UserPreferences" />
<bean:define id="currentFolderPath" name="FolderDocInfo" property="currentFolderPath" type="String" />
<bean:define id="currentFolderId" name="FolderDocInfo" property="currentFolderId" />
<bean:define id="imagepath" name="UserPreferences" property="treeIconPath" />
<bean:define id="userInfo" name="UserInfo" type="dms.web.beans.user.UserInfo" />

<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>

<% 
request.setAttribute("topic","folder_doc_introduction_html");
//Variable declaration
int rowsInList = 0;
ArrayList folderDocLists = (ArrayList)request.getAttribute("folderDocLists");
if(folderDocLists != null){
    rowsInList = folderDocLists.size();
}
boolean upButtonDisabled = false;
if(currentFolderPath.equals("/")){
    upButtonDisabled = true;
}
%>
<html:html>
<head>
<title><bean:message key="title.FolderDocList" /></title>
<script src="general.js"></script>
<script src="menu_functionality.js"></script>
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'':(v=='hide')?'none':v; }
    obj.display=v; }
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
<script language="JavaScript" type="text/JavaScript">
  //Called when Search Lookup button is clicked
  function lookuponclick(){
    openWindow("folderDocSelectB4Action.do?heading=<bean:message key="lbl.ChoosePath" />&foldersOnly=true&openerControl=txtLookinFolderPath","searchLookUp",495,390,0,0,true);
  }
  //Called when new Document button is clicked
  var theNewDocLayerTimer;
  function newDocument_onclick() {
    MM_showHideLayers('lyrNewDocument','','show');
  }

  function newdoclayer_onmouseover() {
    clearTimeout(theNewDocLayerTimer);
  }

  function newdoclayer_onmouseout() {
    theNewDocLayerTimer=setTimeout("MM_showHideLayers('lyrNewDocument','','hide')",500);
  }
  //Called when new button is clicked
  function folderNew() { 
    window.frames[1].folderNew();
  }
  //Called when up button is clicked
  function folderUp() {
    window.frames[1].folderUp();
  }
  //Called when back button is clicked
  function folderBack() {
    window.frames[1].folderBack();
  }
  //Called when forward button is clicked
  function folderForward() { 
    window.frames[1].folderForward();
  }
  //Called when go button is clicked , check out the modified target for iframe
  function gotoFolder(){
    if (document.forms[0].txtAddress.value != "" ){
      document.forms[0].hdnActionType.value=<%=FolderOperation.GOTO%>;
      //document.forms[0].target = "_self";
      document.forms[0].target = window.frames[1].name;
      document.forms[0].action = "folderDocGoToFolderAction.do";
      document.forms[0].submit();
   }
  }
  //Called when refresh button is clicked
  function folderRefresh(){
    window.frames[1].folderRefresh();
  }
  //Called when delete button is clicked
  function folderDelete(){
    window.frames[1].folderDelete();
  }
  //Called when rename button is clicked
  function folderDocRename(){
    window.frames[1].folderDocRename();    
  }
  //Called when property button is clicked
  function folderDocProperty(){
    window.frames[1].folderDocProperty();
  }
  //Called when upload button is clicked
  function docUpload(){
    window.frames[1].docUpload();
  }
  //Called when Apply ACL button is clicked
  function folderDocApplyAcl(){
    window.frames[1].folderDocApplyAcl();
  }
  //Called when copyTo or moveTo button is clicked
  function b4CopyMoveFolderDocTo(actionType){
    window.frames[1].b4CopyMoveFolderDocTo(actionType);
  }
  //Called when copy button is clicked
  function folderDocCopy(){
    window.frames[1].folderDocCopy();
  }
  //Called when cut button is clicked
  function folderDocCut(){
    window.frames[1].folderDocCut();
  }
  //Called when paste button is clicked
  function folderDocPaste(){
    window.frames[1].folderDocPaste();
  }
  //Called when Make Versionable  button is clicked
  function docMakeVersionable(){
    window.frames[1].docMakeVersionable();
  }
  //Called when Checkout button is clicked
  function folderDocCheckOut(){
    window.frames[1].folderDocCheckOut();
  }
  //Called when CheckIn button is clicked
  function folderDocCheckIn(){
    window.frames[1].folderDocCheckIn();
  }
  //Called when Cancel CheckOut button is clicked
  function folderDocCancelCheckout(){
    window.frames[1].folderDocCancelCheckout();
  }
  //Called when History button is clicked
  function docHistory(){
    window.frames[1].docHistory();
  }
  //Called when Download button is clicked
  function docDownload(){
    window.frames[1].docDownload();
  }
  //Called when Encrypt button is clicked
  function docEncryptAction(){
    window.frames[1].docEncryptAction();
  }
  //Called when Decrypt button is clicked
  function docDecryptAction(){
    window.frames[1].docDecryptAction();
  }
  //Called when zip button is clicked
  function docZipAction(){
    window.frames[1].docZipAction();
  }
  //Called when Unzip button is clicked
  function docUnZipAction(){
    window.frames[1].docUnZipAction();
  }
////////////// Added By Maneesh Start //////////////////////////////////////////
  //Called when mail button is clicked
  function docMailAction(){
    window.frames[1].docMailAction();
  }
  //Called when fax button is clicked
  function docFaxAction(){
    window.frames[1].docFaxAction();
  }
/////////////////////////////Added by Maneesh End///////////////////////////////
  function generateLinks(){
    window.frames[1].generateLinks();
  }
  function generateLinksForFolder(){
    window.frames[1].generateLinksForFolder();
  }
  // Submit Document to WorkFlow
  function submitDocToWf(){
    window.frames[1].submitDocToWf();
  }
  // Add watch to POs
  function addWatch(){
    window.frames[1].addWatch();
  }
//Called When Search / Folder Button is Clicked
  function folderSearchFolders(me){
    window.frames[1].folderSearchFolders();
  }
//Select All
  function checkAll(menu){
    window.frames[1].checkAll(menu);
  }
//Invert Selection    
  function invertSelection(){
    window.frames[1].invertSelection();
  }
//UnSelect All
  function unCheckChkAll(me){
    window.frames[1].unCheckChkAll(me);
  }
// Function to view non-versioned document log
  function auditLog(){
    window.frames[1].auditLog();  
  }
// Called to search for documents and folders    
  function folderDocSearch(){
    document.forms[0].target = "_self";
    document.forms[0].action = "advanceSearchAction.do";
    document.forms[0].submit();
  }
// Called to reset search    
  function SearchReset(){    
    document.forms[0].target="_self";
    document.forms[0].action="b4ToggleFolderSearchAction.do";
    document.forms[0].submit();    
  }
// Called when home button is clicked
  function goHome(){
    window.frames[1].goHome();
  }
//Called when view as html button is clicked
  function viewAsHtml(){
    window.frames[1].viewAsHtml();
  }
  
  function window_onload(){   
    MM_preloadImages('<%=imagepath%>/butt_dwnld_over.gif','<%=imagepath%>/butt_back_over.gif',
                     '<%=imagepath%>/butt_forward_over.gif','<%=imagepath%>/butt_go_up_over.gif',
                     '<%=imagepath%>/butt_search_over.gif','<%=imagepath%>/butt_refresh_over.gif',
                     '<%=imagepath%>/butt_user_home_over.gif','<%=imagepath%>/butt_view_html_over.gif',
                     '<%=imagepath%>/butt_copy_over.gif','<%=imagepath%>/butt_cut_over.gif',
                     '<%=imagepath%>/butt_paste_over.gif','<%=imagepath%>/butt_copy_to_over.gif',
                     '<%=imagepath%>/butt_move_to_over.gif','<%=imagepath%>/butt_rename_over.gif',
                     '<%=imagepath%>/butt_delete_folder_over.gif','<%=imagepath%>/butt_new_folder_over.gif',
                     '<%=imagepath%>/butt_chkin_over.gif','<%=imagepath%>/butt_chk_out_over.gif',
                     '<%=imagepath%>/butt_undo_chkout_over.gif','<%=imagepath%>/butt_version_over.gif',
                     '<%=imagepath%>/butt_version_delete_over.gif','<%=imagepath%>/butt_version_detail_over.gif',
                     '<%=imagepath%>/butt_upload_over.gif','<%=imagepath%>/butt_apply_acl_over.gif',
                     '<%=imagepath%>/butt_property_over.gif','<%=imagepath%>/butt_history_over.gif',
                     '<%=imagepath%>/butt_encrypt_over.gif','<%=imagepath%>/butt_decrypt_over.gif',
                     '<%=imagepath%>/butt_zip_over.gif','<%=imagepath%>/butt_unzip_over.gif',
                     '<%=imagepath%>/butt_mail_to_over.gif','<%=imagepath%>/butt_fax_to_over.gif',
                     '<%=imagepath%>/butt_view_log_over.gif','<%=imagepath%>/butt_generate_link_over.gif',
                     '<%=imagepath%>/butt_new_doc_over.gif','<%=imagepath%>/butt_workflow_submit_over.gif',
                     '<%=imagepath%>/icon_word.gif','<%=imagepath%>/icon_excel.gif',
                     '<%=imagepath%>/icon_ppt.gif','<%=imagepath%>/icon_txt.gif');
    
    document.forms[0].txtAddress.value="<%=folderDocInfo.getCurrentFolderPath()%>";
  }
</script>
<script>
//for addressbar
  function enter(thisField,e){
    var i;
    i=handleEnter(thisField,e);
    if (i==1) {
      return gotoFolder();
    }
  }
</script>
<script>
  // for webdav
  function editDoc(path,type){
    window.frames[1].editDoc(path,type);
  }

  function openNewDoc(davpath){
    window.frames[1].openNewDoc(davpath);
  }

  function openNewExcel(davpath){
    window.frames[1].openNewExcel(davpath);
  }
      
  function openNewPowerPoint(davpath){
    window.frames[1].openNewPowerPoint(davpath);
  }
  
  function openNewText(davpath){
    window.frames[1].openNewText(davpath);
  }

  function evalMenuObj(menuId){
    return eval(menuId); 
  }
  
  function doNothing(){
    return null;
  }
  
</script>
<!-- -->
<jsp:include page="/menu_structure_document.jsp" />
</head>
<body style="margin:0" onload="window_onload();">
<!-- This page contains ? outermost tables, named 'headerIncluder' 'spacer'  'toolBar' 'addressBar' 'errorContainer' 'tabContainer'.
And 2 divs named 'treeMenuContainer' and 'SearchContainer'.
-->
<table id="headerIncluder" width="100%"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="95px">
      <!--Header Starts-->
      <jsp:include page="/header.jsp" />
      <!--Header Ends-->
  	</td>
  </tr>
</table>
<table id="spacer" width="100%"  border="0" cellspacing="0" cellpadding="0" >
  <!-- this table is for giving 14 px height bar behind the drop-down menu-->
  <tr><td height="14px"></td></tr>
</table>
<!-- Begin Menu Generation-->
<script>
  folderDoc.renderMenu();
</script>
<!-- End Menu  Generation-->
<table id="toolBar" width="100%"  border="0" cellpadding="0" cellspacing="0" class="imgToolBar bgClrLvl_3">
	<tr><td height="1px" class="bgClrLvl_F"></td></tr>
  <tr><td height="1px" class="imgToolBar bgClrLvl_3"></td></tr>
	<tr>
		<td height="25px" id="iconsTd">
      <logic:equal name="folderDocInfo" property="listingType" value="<%=FolderDocInfo.SIMPLE_LISTING%>" >
        <div style="float:left" >
          <table border="0" cellpadding="0" cellspacing="0" >
            <tr>
              <td>
                <a id="newDoc" onclick="return newDocument_onclick();" class="imgNewDoc" onmouseout="this.className='imgNewDoc';return newdoclayer_onmouseout();" onmouseover="this.className='imgNewDocOver';return newdoclayer_onmouseover();" style="margin-left:5px;" title="<bean:message key="tooltips.CreateNewDoc" />"></a>
              </td>
            </tr>
            <tr>
              <td>
                <div>
                  <div id="lyrNewDocument" class="imgToolBar bgClrLvl_3 borderClrLvl_2" style="background-repeat:repeat-y;margin-top:4px;margin-left:4px;display:none;position:absolute;width:100px;z-index:2">
                    <table align="center" width="100%" border="0" cellpadding="1" cellspacing="2"  >
                      <tr>
                        <td>
                          <a class="iconWord"  onclick="openNewDoc('<%=userInfo.getDavPath()%>' + '<%=folderDocInfo.getCurrentFolderPath()%>' + '/')" onmouseout="this.className='iconWord'; return newdoclayer_onmouseout();" onmouseover="this.className='iconWordOver'; return newdoclayer_onmouseover();" title="<bean:message key="tooltips.NewWordDoc" />"></a>
                        </td>
                        <td>
                          <a class="iconExcel"  onclick="openNewExcel('<%=userInfo.getDavPath()%>' + '<%=folderDocInfo.getCurrentFolderPath()%>' + '/')" onmouseout="this.className='iconExcel'; return newdoclayer_onmouseout();" onmouseover="this.className='iconExcelOver'; return newdoclayer_onmouseover();" title="<bean:message key="tooltips.NewExcelDoc" />"></a>
                        </td>
                        <td>
                          <a class="iconPpt"  onclick="openNewPowerPoint('<%=userInfo.getDavPath()%>' + '<%=folderDocInfo.getCurrentFolderPath()%>' + '/')" onmouseout="this.className='iconPpt'; return newdoclayer_onmouseout();" onmouseover="this.className='iconPptOver'; return newdoclayer_onmouseover();" title="<bean:message key="tooltips.NewPowerPointDoc" />"></a>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <a class="iconTxt"  onclick="openNewText('<%=userInfo.getDavPath()%>' + '<%=folderDocInfo.getCurrentFolderPath()%>' + '/')" onmouseout="this.className='iconTxt'; return newdoclayer_onmouseout();" onmouseover="this.className='iconTxtOver'; return newdoclayer_onmouseover();" title="<bean:message key="tooltips.NewTextDoc" />"></a>
                        </td>		
                        <td>
                        </td>
                        <td>&nbsp;</td>
                      </tr>
                    </table>
                  </div>
                </div>
              </td>
            </tr>
          </table>
        </div>
        <a onClick="folderNew();" class='imgNewFolder' onMouseOut="this.className='imgNewFolder'" onMouseOver="this.className='imgNewFolderOver'" style="margin-left:3px;" title="<bean:message key="tooltips.FolderNew" />"></a>
        <a onClick="folderDocRename();" class="imgRename" onMouseOut="this.className='imgRename'" onMouseOver="this.className='imgRenameOver'" title="<bean:message key="tooltips.FolderRename" />" ></a>
        <a onClick="docUpload();" class="imgUpload" onMouseOut="this.className='imgUpload'" onMouseOver="this.className='imgUploadOver'" title="<bean:message key="tooltips.DocumentUpload" />" ></a>
        <a onClick="docDownload();" class="imgDownload" onMouseOut="this.className='imgDownload'" onMouseOver="this.className='imgDownloadOver'" title="<bean:message key="tooltips.DocumentDownload" />" ></a>
        <a onClick="folderDocApplyAcl();" class="imgApplyACL" onMouseOut="this.className='imgApplyACL'" onMouseOver="this.className='imgApplyACLOver'" title="<bean:message key="tooltips.FolderDocApplyAcl" />" ></a>
        <a onClick="folderDocProperty();" class="imgProperty" onMouseOut="this.className='imgProperty'" onMouseOver="this.className='imgPropertyOver'" title="<bean:message key="tooltips.FolderDocProperty" />" ></a>
        <a onClick="generateLinks();" class="imgGenerateLink" onMouseOut="this.className='imgGenerateLink'" onMouseOver="this.className='imgGenerateLinkOver'" title="<bean:message key="tooltips.DocumentGenerateLink" />" ></a>
        <a onClick="auditLog();" class="imgViewLog" onMouseOut="this.className='imgViewLog'" onMouseOver="this.className='imgViewLogOver'" title="<bean:message key="tooltips.DocumentViewLog" />" ></a>          
        <a onClick="addWatch();" class="imgAddWatch" onMouseOut="this.className='imgAddWatch'" onMouseOver="this.className='imgAddWatchOver'" title="<bean:message key="tooltips.addWatch" />" ></a>          
        <a onClick="viewAsHtml();" class="imgViewHtml" onMouseOut="this.className='imgViewHtml'" onMouseOver="this.className='imgViewHtmlOver'" style='margin-right:3px;' title="<bean:message key="tooltips.ViewAsHtml" />" ></a>          
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <a onClick="folderDocCut()" class="imgCut" onMouseOut="this.className='imgCut'" onMouseOver="this.className='imgCutOver'" style="margin-left:3px;" title="<bean:message key="tooltips.FolderDocCut" />"></a>
        <a onClick="folderDocCopy()" class="imgCopy" onMouseOut="this.className='imgCopy'" onMouseOver="this.className='imgCopyOver'" title="<bean:message key="tooltips.FolderDocCopy" />"></a>
        <a onClick="folderDocPaste()" class="imgPaste" onMouseOut="this.className='imgPaste'" onMouseOver="this.className='imgPasteOver'" title="<bean:message key="tooltips.FolderDocPaste" />"></a>
        <a onClick="folderDelete();" class="imgDeleteFolder" onMouseOut="this.className='imgDeleteFolder'" onMouseOver="this.className='imgDeleteFolderOver'" style='margin-right:3px;' title="<bean:message key="tooltips.FolderDocDelete" />"></a>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <a onClick="b4CopyMoveFolderDocTo('COPY')" class="imgCopyTo" onMouseOut="this.className='imgCopyTo'" onMouseOver="this.className='imgCopyToOver'" style="margin-left:3px;" title="<bean:message key="tooltips.FolderDocCopyTo" />"></a>
        <a onClick="b4CopyMoveFolderDocTo('MOVE')" class="imgMoveTo" onMouseOut="this.className='imgMoveTo'" onMouseOver="this.className='imgMoveToOver'" style='margin-right:3px;' title="<bean:message key="tooltips.FolderDocMoveTo" />"></a>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <a onClick="docMakeVersionable();" class="imgVersionable" onMouseOut="this.className='imgVersionable'" onMouseOver="this.className='imgVersionableOver'" style="margin-left:3px;" title="<bean:message key="tooltips.DocumentMakeVersionable" />" ></a>
        <a onClick="folderDocCheckOut();" class="imgChkOut" onMouseOut="this.className='imgChkOut'" onMouseOver="this.className='imgChkOutOver'" title="<bean:message key="tooltips.DocumentCheckOut" />" ></a>
        <a onClick="folderDocCheckIn();" class="imgChkIn" onMouseOut="this.className='imgChkIn'" onMouseOver="this.className='imgChkInOver'" title="<bean:message key="tooltips.DocumentCheckIn" />" ></a>
        <a onClick="folderDocCancelCheckout();" class="imgUndoChkOut" onMouseOut="this.className='imgUndoChkOut'" onMouseOver="this.className='imgUndoChkOutOver'" title="<bean:message key="tooltips.DocumentUndoCheckOut" />" ></a>
        <a onClick="docHistory();" class="imgHistory" onMouseOut="this.className='imgHistory'" onMouseOver="this.className='imgHistoryOver'" style='margin-right:3px;' title="<bean:message key="tooltips.DocumentHistory" />" ></a>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <a onClick="docEncryptAction();" style="margin-left:3px" class="imgEncrypt" onMouseOut="this.className='imgEncrypt'" onMouseOver="this.className='imgEncryptOver'" title="<bean:message key="tooltips.EncryptDoc" />" ></a>
        <a onClick="docDecryptAction();" class="imgDecrypt" onMouseOut="this.className='imgDecrypt'" onMouseOver="this.className='imgDecryptOver'" style='margin-right:3px;' title="<bean:message key="tooltips.DecryptDoc" />" ></a>          
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <a onClick="docZipAction();" style="margin-left:3px" class="imgZip" onMouseOut="this.className='imgZip'" onMouseOver="this.className='imgZipOver'" title="<bean:message key="tooltips.Zip" />" ></a>
        <a onClick="docUnZipAction();" class="imgUnZip" onMouseOut="this.className='imgUnZip'" onMouseOver="this.className='imgUnZipOver'" style='margin-right:3px;' title="<bean:message key="tooltips.UnZip" />" ></a>          
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>          
        <a onClick="docMailAction();" style="margin-left:3px" class="imgMailTo" onMouseOut="this.className='imgMailTo'" onMouseOver="this.className='imgMailToOver'" title="<bean:message key="tooltips.MailTo" />" ></a>
        <a onClick="docFaxAction();" class="imgFaxTo" onMouseOut="this.className='imgFaxTo'" onMouseOver="this.className='imgFaxToOver'" style='margin-right:3px;' title="<bean:message key="tooltips.FaxTo" />" ></a>          
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>          
        <a onClick="submitDocToWf();" style="margin-left:3px" class="imgWorkflowSubmit" onMouseOut="this.className='imgWorkflowSubmit'" onMouseOver="this.className='imgWorkflowSubmitOver'" title="<bean:message key="tooltips.SubmitWorkFlow" />" ></a>
      </logic:equal>          

      <logic:equal name="folderDocInfo" property="listingType" value="<%=FolderDocInfo.SEARCH_LISTING%>" >
        <div class="imgNewDocDisable" style="float:left"></div>  
        <div class="imgNewFolderDisable" style="margin-left:3px;"></div>  
        <a onClick="folderDocRename();" class="imgRename" onMouseOut="this.className='imgRename'" onMouseOver="this.className='imgRenameOver'" title="<bean:message key="tooltips.FolderRename" />" ></a>
        <div class="imgUploadDisable"></div>  
        <div class="imgDownloadDisable"></div>  
        <a onClick="folderDocApplyAcl();" class="imgApplyACL" onMouseOut="this.className='imgApplyACL'" onMouseOver="this.className='imgApplyACLOver'" title="<bean:message key="tooltips.FolderDocApplyAcl" />" ></a>
        <a onClick="folderDocProperty();" class="imgProperty" onMouseOut="this.className='imgProperty'" onMouseOver="this.className='imgPropertyOver'" title="<bean:message key="tooltips.FolderDocProperty" />" ></a>
        <a onClick="generateLinks();" class="imgGenerateLink" onMouseOut="this.className='imgGenerateLink'" onMouseOver="this.className='imgGenerateLinkOver'" title="<bean:message key="tooltips.DocumentGenerateLink" />" ></a>
        <a onClick="auditLog();" class="imgViewLog" onMouseOut="this.className='imgViewLog'" onMouseOver="this.className='imgViewLogOver'" title="<bean:message key="tooltips.DocumentViewLog" />" ></a>          
        <a onClick="addWatch();" class="imgAddWatch" onMouseOut="this.className='imgAddWatch'" onMouseOver="this.className='imgAddWatchOver'" title="<bean:message key="tooltips.addWatch" />" ></a>          
        <a onClick="viewAsHtml();" class="imgViewHtml" onMouseOut="this.className='imgViewHtml'" onMouseOver="this.className='imgViewHtmlOver'" style='margin-right:3px;' title="<bean:message key="tooltips.ViewAsHtml" />" ></a>          
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <a onClick="folderDocCut()" class="imgCut" onMouseOut="this.className='imgCut'" onMouseOver="this.className='imgCutOver'" style="margin-left:3px;" title="<bean:message key="tooltips.FolderDocCut" />"></a>
        <a onClick="folderDocCopy()" class="imgCopy" onMouseOut="this.className='imgCopy'" onMouseOver="this.className='imgCopyOver'" title="<bean:message key="tooltips.FolderDocCopy" />"></a>
        <div class="imgPasteDisable"></div>  
        <a onClick="folderDelete();" class="imgDeleteFolder" onMouseOut="this.className='imgDeleteFolder'" onMouseOver="this.className='imgDeleteFolderOver'" style='margin-right:3px;' title="<bean:message key="tooltips.FolderDocDelete" />"></a>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <a onClick="b4CopyMoveFolderDocTo('COPY')" class="imgCopyTo" onMouseOut="this.className='imgCopyTo'" onMouseOver="this.className='imgCopyToOver'" style="margin-left:3px;" title="<bean:message key="tooltips.FolderDocCopyTo" />"></a>
        <a onClick="b4CopyMoveFolderDocTo('MOVE')" class="imgMoveTo" onMouseOut="this.className='imgMoveTo'" onMouseOver="this.className='imgMoveToOver'" style='margin-right:3px;' title="<bean:message key="tooltips.FolderDocMoveTo" />"></a>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <a onClick="docMakeVersionable();" class="imgVersionable" onMouseOut="this.className='imgVersionable'" onMouseOver="this.className='imgVersionableOver'" style="margin-left:3px;" title="<bean:message key="tooltips.DocumentMakeVersionable" />" ></a>
        <a onClick="folderDocCheckOut();" class="imgChkOut" onMouseOut="this.className='imgChkOut'" onMouseOver="this.className='imgChkOutOver'" title="<bean:message key="tooltips.DocumentCheckOut" />" ></a>
        <a onClick="folderDocCheckIn();" class="imgChkIn" onMouseOut="this.className='imgChkIn'" onMouseOver="this.className='imgChkInOver'" title="<bean:message key="tooltips.DocumentCheckIn" />" ></a>
        <a onClick="folderDocCancelCheckout();" class="imgUndoChkOut" onMouseOut="this.className='imgUndoChkOut'" onMouseOver="this.className='imgUndoChkOutOver'" title="<bean:message key="tooltips.DocumentUndoCheckOut" />" ></a>
        <a onClick="docHistory();" class="imgHistory" onMouseOut="this.className='imgHistory'" onMouseOver="this.className='imgHistoryOver'" style='margin-right:3px;' title="<bean:message key="tooltips.DocumentHistory" />" ></a>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <a onClick="docEncryptAction();" style="margin-left:3px" class="imgEncrypt" onMouseOut="this.className='imgEncrypt'" onMouseOver="this.className='imgEncryptOver'" title="<bean:message key="tooltips.EncryptDoc" />" ></a>
        <a onClick="docDecryptAction();" class="imgDecrypt" onMouseOut="this.className='imgDecrypt'" onMouseOver="this.className='imgDecryptOver'" style='margin-right:3px;' title="<bean:message key="tooltips.DecryptDoc" />" ></a>          
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <a onClick="docZipAction();" style="margin-left:3px" class="imgZip" onMouseOut="this.className='imgZip'" onMouseOver="this.className='imgZipOver'" title="<bean:message key="tooltips.Zip" />" ></a>
        <a onClick="docUnZipAction();" class="imgUnZip" onMouseOut="this.className='imgUnZip'" onMouseOver="this.className='imgUnZipOver'" style='margin-right:3px;' title="<bean:message key="tooltips.UnZip" />" ></a>          
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <a onClick="docMailAction();" style="margin-left:3px" class="imgMailTo" onMouseOut="this.className='imgMailTo'" onMouseOver="this.className='imgMailToOver'" title="<bean:message key="tooltips.MailTo" />" ></a>
        <a onClick="docFaxAction();" class="imgFaxTo" onMouseOut="this.className='imgFaxTo'" onMouseOver="this.className='imgFaxToOver'" style='margin-right:3px;' title="<bean:message key="tooltips.FaxTo" />" ></a>          
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>          
        <a onClick="submitDocToWf();" style="margin-left:3px" class="imgWorkflowSubmit" onMouseOut="this.className='imgWorkflowSubmit'" onMouseOver="this.className='imgWorkflowSubmitOver'" title="<bean:message key="tooltips.SubmitWorkFlow" />" ></a>          
      </logic:equal>          

      <logic:equal name="folderDocInfo" property="listingType" value="<%=FolderDocInfo.DISPLAY_PAGE%>" >
        <div class="imgNewDocDisable" style="float-left;"></div>  
        <div class="imgNewFolderDisable" style="margin-left:3px;"></div>  
        <div class="imgRenameDisable"></div>  
        <div class="imgUploadDisable"></div>  
        <div class="imgDownloadDisable"></div>  
        <div class="imgApplyACLDisable"></div>  
        <div class="imgPropertyDisable"></div>
        <div class="imgGenerateLinkDisable"></div> 
        <div class="imgViewLogDisable"></div> 
        <div class="imgAddWatchDisable"></div> 
        <div class="imgViewHtmlDisable" style='margin-right:3px;' ></div> 
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <div class="imgCutDisable" style="margin-left:3px;"></div>  
        <div class="imgCopyDisable"></div>  
        <div class="imgPasteDisable"></div>  
        <div class="imgDeleteFolderDisable" style='margin-right:3px;' ></div>  
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <div class="imgCopyToDisable" style="margin-left:3px;"></div>  
        <div class="imgMoveToDisable" style='margin-right:3px;' ></div>  
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <div class="imgVersionableDisable" style="margin-left:3px;"></div>            
        <div class="imgChkOutDisable"></div>            
        <div class="imgChkInDisable"></div>            
        <div class="imgUndoChkOutDisable"></div>                      
        <div class="imgHistoryDisable" style='margin-right:3px;' ></div>                      
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <div class="imgEncryptDisable" style="margin-left:3px;"></div>                      
        <div class="imgDecryptDisable" style='margin-right:3px;' ></div>                      
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <div class="imgZipDisable" style="margin-left:3px;"></div>                      
        <div class="imgUnZipDisable" style='margin-right:3px;' ></div> 
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
        <div class="imgMailToDisable" style="margin-left:3px;"></div>                      
        <div class="imgFaxToDisable" style='margin-right:3px;' ></div>    
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>          
        <div class="imgWorkflowSubmitDisable" style="margin-left:3px;"></div>
      </logic:equal>        
    </td>
	</tr>
	<tr><td height="1px" ></td></tr>
	<tr><td height="1px" class="bgCLrLvl_2"></td></tr>
</table>
<!--toolBar table ends above-->
<table id="formTab" width="100%"  border="0" cellpadding="0" cellspacing="0" class="imgToolBar bgClrLvl_3" >
<html:form name="folderDocListForm" action="/folderDocListAction" type="dms.web.actionforms.filesystem.FolderDocListForm" >
<!-- hidden values to be passed for all operation -->
<html:hidden property="hdnActionType" value="" />
<!-- hidden values to be passed for following operation
    1. UP_FOLDER
    2. REFRESH
-->
<html:hidden property="currentFolderId" value="<%=currentFolderId %>" />
<!-- hidden values to be passed to the copy or move operation -->
<html:hidden property="hdnTargetFolderPath" value="<%=currentFolderPath%>" />
<html:hidden property="hdnPropertyType" value="" />
<html:hidden property="hdnWfName" value="" />
<!-- hidden values to be passed to the folder copyTo moveTo and paste operation -->
<html:hidden property="hdnOverWrite" value="true" />
<!-- hidden values to be passed to encrypt operation -->
<html:hidden property="hdnEncryptionPassword" value="" />
<html:hidden property="txtPageNo" value="<%=folderDocInfo.getPageNumber()%>" /> 
<table id="addressBar" width="100%"  border="0" cellpadding="0" cellspacing="0" class="imgToolBar bgClrLvl_3">
  <tr><td colspan="3" height="1px" class="bgClrLvl_F"></td></tr>
  <tr>
    <td width="83px" height="25px">
      <div id="navDiv" style="width:100%">
        <% if(!folderDocInfo.isBackButtonDisabled()){ %>
          <a onclick="folderBack();" class="imgBack" onmouseout="this.className='imgBack'" onmouseover="this.className='imgBackOver'" style="margin-left:5px;" title="<bean:message key="tooltips.Back" />" ></a>
        <%}else{%>
          <div class="imgBackDisable"></div>		
        <%}%>
        <% if(!folderDocInfo.isForwardButtonDisabled()){ %>
          <a onclick="folderForward();" class="imgForward" onmouseout="this.className='imgForward'" onmouseover="this.className='imgForwardOver'" title="<bean:message key="tooltips.Forward" />" ></a>
        <%}else{%>
          <div class="imgForwardDisable"></div>  
        <%}%>
        <% if(!upButtonDisabled){ %>
          <a onclick="folderUp();" class="imgGoUp" onmouseout="this.className='imgGoUp'" onmouseover="this.className='imgGoUpOver'" title="<bean:message key="tooltips.Up" />" ></a>
        <%}else{%>
          <div class="imgGoUpDisable"></div>
        <%}%>
      </div>
    </td>
    <td height="25px" width="100px">
      <a onClick="folderSearchFolders(this);" class="imgSearch" onMouseOut="this.className='imgSearch'" onMouseOver="this.className='imgSearchOver'" title="<bean:message key="tooltips.FolderDocSearch" />" ></a>
      <a onClick="folderRefresh();" class="imgRefresh" onMouseOut="this.className='imgRefresh'" onMouseOver="this.className='imgRefreshOver'" title="<bean:message key="tooltips.Refresh" />" ></a>
      <a onClick="goHome();" class="imgUserHome" onMouseOut="this.className='imgUserHome'" onMouseOver="this.className='imgUserHomeOver'" title="<bean:message key="tooltips.UserHome" />" ></a>
      <div style="float:left; width:1px;height:100%;" ></div>
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
      <div style="float:left; width:3px;height:100%;" ></div>
    </td>
    <td>
      <html:text property="txtAddress" value="<%=folderDocInfo.getCurrentFolderPath()%>" styleClass="borderClrLvl_2 componentStyle" style="width:780px;float:left;" onkeypress="return enter(this,event);" /> 
      <a onClick="gotoFolder();" class="imgGo" title="<bean:message key="btn.Go" /> "></a>
    </td>
  </tr>
  <tr><td colspan="3" height="1px" class="bgClrLvl_2"></td></tr>
</table>
<!--addressBar table ends above-->
<table id="tabContainer" width="990px"  border="0" cellspacing="0" cellpadding="0">
<!-- this table is having 1 tr and 2 td. 1st td contains the divs and the 2nd one contains tabParent-->
  <tr><td height="5px" colspan="2"></td></tr>
  <tr>
    <td rowspan="2" width="240px" valign="top">
      <table id="tabParentTree" width="240px" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="240px">
            <table id="foldersTab" width="240px" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="5px" class="imgTabLeft"></td>
                <td width="230px" class="imgTabTile"><div align="left" class="tabText"><bean:message key="lbl.Folders" /></div></td>
                <td width="5px" class="imgTabRight"></td>
              </tr>
            </table>
          </td>
        </tr>
        <%if(folderDocInfo.isTreeVisible()){%>
          <tr>
            <td class="borderClrLvl_2 imgData bgClrLvl_4">
              <!--<div id="treeDiv" style="top:0px;" >-->
                <iframe id="iframeTreeview" name="iframeTreeview" src="treeview.jsp" class="imgData bgClrLvl_4" style="top:0px;" height="385px" width="238px" frameborder="0" scrolling="auto"></iframe>
              <!--</div>-->
            </td>
          </tr>
          <!--treeDiv ends above-->
        <%}else{%> 
          <tr>
            <td class="imgData bgClrLvl_4">
              <!--<div id="divSearchShowHide">-->
                <iframe id="iframeSearch" name="iframeSearch" src="b4SearchAction.do" class="imgData bgClrLvl_4" style="display:'';"  height="385px" width="238px" frameborder="0" scrolling="auto"></iframe>
              <!--</div>-->
            </td>
          </tr>
        <!--searchContainer div ends above-->
        <%}%>
      </table>
    </td>
    <!-- Data Parent table inspects for doc opening option and proceeds accordingly -->
    <td width="750px" valign="top" >
      <table id="tabParentData" width="100%"  border="0" cellpadding="0" cellspacing="0" style="margin-left:5px;">
        <!-- this table is having 2 tr, 1st tr contains the 'folderDocTab' table, 2nd tr contains the 'dataParent' table-->
        <tr>
          <td>
            <table id="folderDocTab" width="220px" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="5px" class="imgTabLeft"></td>
                <td width="210px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.FolderDocumentList" /></div></td>
                <td width="5px" class="imgTabRight"></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td>
            <iframe id="iframeFolderDocList" name="iframeFolderDocList" src="folderDocListAction.do?reloadTree=false" style="top:0px;" height="385px" width="750px" frameborder="0" scrolling="no"></iframe>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<!-- tabContainer table ends here-->
</html:form>
</table>
</body>
</html:html>
