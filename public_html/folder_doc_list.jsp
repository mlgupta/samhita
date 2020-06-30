<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="dms.web.beans.user.UserPreferences" %>
<%@ page import="dms.web.beans.filesystem.FolderDocInfo" %>
<%@ page import="dms.web.beans.filesystem.FolderOperation" %>
<%@ page import="dms.web.actionforms.filesystem.FolderDocPropertyForm" %>
<%@ page import="org.apache.struts.action.ActionMessages" %>
<%@ page import="java.util.ArrayList" %>

<bean:define id="folderDocInfo" name="FolderDocInfo" type="dms.web.beans.filesystem.FolderDocInfo" />
<bean:define id="userPreferences" name="UserPreferences" type="dms.web.beans.user.UserPreferences" />
<bean:define id="currentFolderPath" name="FolderDocInfo" property="currentFolderPath" type="String" />
<bean:define id="currentFolderId" name="FolderDocInfo" property="currentFolderId" />

<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>

<% 
request.setAttribute("topic","folder_doc_introduction_html");
int rowsInList = 0;
ArrayList folderDocLists = (ArrayList)request.getAttribute("folderDocLists");
//ArrayList folderDocLists = (ArrayList)session.getAttribute("folderDocLists");
if(folderDocLists != null){
    rowsInList = folderDocLists.size();
}
//Variable declaration
boolean firstTableRow = true;
boolean upButtonDisabled = false;
if(currentFolderPath.equals("/")){
    upButtonDisabled = true;
}
ActionMessages actionMessages = (ActionMessages)request.getAttribute("actionMessages");
if(actionMessages != null)
    out.print(actionMessages.size());
%>

<html:html>
<head>
<jsp:include page="/style_sheet_include.jsp" />
<title><bean:message key="title.FolderDocList" /></title>
<script src="general.js"></script>
<script src="navigationbar.js" ></script>
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
<script>
  var xmlHttpReq = false;
  //var xmlHttpReq1 = false;
  //var documentId = 0;
  function getXMLHTTPReq(){
    try{
      xmlHttpReq = new XMLHttpRequest();
    }catch(e){
      try{
        xmlHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
      }catch(e1){
        try{
          xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
        }catch(allefailed){
          xmlHttpReq = false;
        }  
        if(!xmlHttpReq){
          alert("Error initializing XMLHttpRequest!");
        }
      }
    }
  }
  /*function getXMLHTTPReq1(){
    try{
      xmlHttpReq1 = new XMLHttpRequest();
    }catch(e){
      try{
        xmlHttpReq1 = new ActiveXObject("Msxml2.XMLHTTP");
      }catch(e1){
        try{
          xmlHttpReq1 = new ActiveXObject("Microsoft.XMLHTTP");
        }catch(allefailed){
          xmlHttpReq1 = false;
        }  
        if(!xmlHttpReq1){
          alert("Error initializing XMLHttpRequest for view as html!");
        }
      }
    }
  }*/
  function enableDisable(menuId,funcVal){
    var menuObj = window.top.evalMenuObj(menuId);
    menuObj.onclick= funcVal;
    return ;
  }

  function changeNavDiv() {
    getXMLHTTPReq();
    var url = "check_criteria.jsp";
    xmlHttpReq.open("GET", url, true);
    xmlHttpReq.onreadystatechange = updatePage;
    xmlHttpReq.send(null);     
  }
  
  function updatePage(){
    var menuIdsArr = null;
    var funcArr = null;
    if (xmlHttpReq.readyState == 4) {
      if (xmlHttpReq.status == 200) {
        var responseArr = xmlHttpReq.responseText.split("|");
        window.top.document.getElementById('navDiv').innerHTML = responseArr[0];
        window.top.document.getElementById('iconsTd').innerHTML = responseArr[1];
        menuIdsArr = responseArr[2].split(",");
        funcArr = responseArr[3].split(",");
        for(var index = 0; index < menuIdsArr.length; index++){
          enableDisable(menuIdsArr[index],funcArr[index]);
        }
      }else{
        alert("status is " + xmlHttpReq.status);
      }
    }  
  }
  /*function viewDoc(docId){
    documentId = docId; 
    getXMLHTTPReq1();
    var url="blank.html";
    xmlHttpReq1.open("GET", url, true);
    xmlHttpReq1.onreadystatechange = showDoc;
    xmlHttpReq1.send(null);     
  }
  
  function showDoc(){
    if (xmlHttpReq1.readyState == 4) {
      if (xmlHttpReq1.status == 200) {
        //var responseXml = xmlHttpReq1.responseText;
        openWindow("docViewAsHtmlAction.do?documentId="+documentId,'ViewHtml',650,800,0,0,true,"scrollbars=1");
      }else{
        alert("status is " + xmlHttpReq1.status);
      }
    }  
  }*/
</script>
<script language="JavaScript" type="text/JavaScript">
  window.name="<%=session.getId()%>";
  //Called when Search Lookup button is clicked
  function lookuponclick(){
    openWindow("folderDocSelectB4Action.do?heading=<bean:message key="lbl.ChoosePath" />&foldersOnly=true&openerControl=txtLookinFolderPath","searchLookUp",495,390,0,0,true);
  }
  //Called when new button is clicked
  function folderNew() { 
      openWindow("","newFolder",150,530,0,0,true);
      document.forms[0].target = "newFolder";
      document.forms[0].action = "b4FolderNewAction.do";
      document.forms[0].submit();
  }
  //Called when up button is clicked
  function folderUp() { 
      document.forms[0].hdnActionType.value=<%=FolderOperation.UP_FOLDER%>;
      document.forms[0].target = "_self";
      document.forms[0].action = "folderDocParentAction.do";
      document.forms[0].submit();
  }
  //Called when back button is clicked
  function folderBack() {
      document.forms[0].hdnActionType.value=<%=FolderOperation.BACK%>;
      document.forms[0].target = "_self";    
      document.forms[0].action = "folderDocBackAction.do";
      document.forms[0].submit();
  }
  //Called when forward button is clicked
  function folderForward() { 
      document.forms[0].hdnActionType.value=<%=FolderOperation.FORWARD%>;
      document.forms[0].target = "_self";
      document.forms[0].action = "folderDocForwardAction.do";
      document.forms[0].submit();
  }
  //Called when refresh button is clicked
  function folderRefresh(){
      document.forms[0].hdnActionType.value=<%=FolderOperation.REFRESH%>;
      document.forms[0].target = "_self";    
      document.forms[0].action = "folderDocListAction.do";
      document.forms[0].submit();
  }
  //Called when delete button is clicked
  function folderDelete(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter;
    var itemsToBeDeleted = 0;
    var itemName;
    if (rowsInList == 1){
      itemSelected = document.forms[0].chkFolderDocIds.checked
      itemsToBeDeleted = 1;
      itemName = document.forms[0].name.value;
    }else if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(itemSelected = document.forms[0].chkFolderDocIds[counter].checked){
          itemsToBeDeleted++;
          itemName = document.forms[0].name[counter].value;
        }
     }
    }
    if (itemsToBeDeleted >= 1){
      if (itemsToBeDeleted == 1){
        if (confirm("<bean:message key="msg.delete.confirm" /> \n\"" + itemName + "\" ?" )){
          document.forms[0].target = "_self";
          document.forms[0].action = "folderDocDeleteAction.do";
          document.forms[0].submit();
        }
      }else if (itemsToBeDeleted > 1){
        if (confirm("<bean:message key="msg.delete.confirm" /> " + " <bean:message key="lbl.these" /> " + itemsToBeDeleted + " <bean:message key="lbl.items" />" + " ?" )){
          document.forms[0].target = "_self";
          document.forms[0].action = "folderDocDeleteAction.do";
          document.forms[0].submit();
        }
      }
    }else{
      alert("<bean:message key="msg.folderdoc.notselected" />");
    }
  }
  //Called when rename button is clicked
  function folderDocRename(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter
    if (rowsInList == 1){
        itemSelected = document.forms[0].chkFolderDocIds.checked
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length; counter++){
        if(itemSelected = document.forms[0].chkFolderDocIds[counter].checked){
            break;
        }    
      }
    }
    if (itemSelected){
      openWindow("","Rename",220,530,0,0,true);
      document.forms[0].target = "Rename";
      document.forms[0].action = "folderDocB4RenameAction.do";
      document.forms[0].submit();
    }else{
      alert("<bean:message key="msg.folderdoc.notselected" />");
    }
  }
  //Called when property button is clicked
  function folderDocProperty(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter;
    var folderCount;
    var documentCount;
    var propertyTypeToSet;
    if (rowsInList == 1){
        itemSelected = document.forms[0].chkFolderDocIds.checked
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(itemSelected = document.forms[0].chkFolderDocIds[counter].checked){
            break;
        }    
      }
    }
    if (!itemSelected){
        alert("<bean:message key="msg.folderdoc.notselected" />");        
    }
    //initialise variable to zero
    folderCount = 0;  documentCount = 0;
    //check if only one document or folder is selected     
    if (rowsInList == 1){
      itemSelected = document.forms[0].chkFolderDocIds.checked
      if (itemSelected){
        folderCount = 0; documentCount = 0;
        if(document.forms[0].hdnTypes.value == "FOLDER" ){
            folderCount = 1;
        }else{
            documentCount = 1;
        }
      }
    }    
    //check if only many document and or many folder is selected         
    if (rowsInList > 1){
      folderCount = 0; documentCount = 0;
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        itemSelected = document.forms[0].chkFolderDocIds[counter].checked;
        if(itemSelected){
          if(document.forms[0].hdnTypes[counter].value == "FOLDER" ){
            folderCount = folderCount + 1;
          }else{
            documentCount = documentCount + 1;
          }
        }    
      }
    }
    if (folderCount > 0 || documentCount > 0){
      //So depending on the number of document and folder selected set propertyType
      //for one folder only        
      if(folderCount == 1 && documentCount == 0){
        openWindow("","Property",413,385,0,0,true);                
        document.forms[0].hdnPropertyType.value = <%=FolderDocPropertyForm.ONE_FOLDER%>;        
      }
      //for one document only
      if(folderCount == 0 && documentCount == 1){
        openWindow("","Property",453,385,0,0,true);                
        document.forms[0].hdnPropertyType.value = <%=FolderDocPropertyForm.ONE_DOCUMENT%>;        
      }
      //for more than one folders
      if(folderCount > 1 && documentCount == 0){
        openWindow("","Property",383,385,0,0,true);                
        document.forms[0].hdnPropertyType.value = <%=FolderDocPropertyForm.ONLY_FOLDER%>;        
      }
      //for more than one documents
      if(folderCount == 0 && documentCount > 1){
        openWindow("","Property",438,385,0,0,true);
        document.forms[0].hdnPropertyType.value = <%=FolderDocPropertyForm.ONLY_DOCUMENT%>;        
      }
      //for both documents and folders
      if(folderCount > 0  && documentCount > 0){
        openWindow("","Property",235,385,0,0,true); 
        document.forms[0].hdnPropertyType.value = <%=FolderDocPropertyForm.FOLDER_AND_DOCUMENT%>;        
      }
      document.forms[0].target = "Property";
      document.forms[0].action = "folderDocB4PropertyAction.do";
      document.forms[0].submit();
    }
  }
  //Called when upload button is clicked
  function docUpload(){
    openWindow("","Upload",410,520,0,0,true);
    document.forms[0].target = "Upload";
    document.forms[0].action = "b4DocUploadAction.do";
    document.forms[0].submit();
  }
  //Called when Apply ACL button is clicked
  function folderDocApplyAcl(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter
    if (rowsInList == 1){
        itemSelected = document.forms[0].chkFolderDocIds.checked
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(itemSelected = document.forms[0].chkFolderDocIds[counter].checked){
            break;
        }    
      }
    }
    if (itemSelected){
      openWindow("","ApplyAcl",500,620,0,0,true);
      document.forms[0].target = "ApplyAcl";
      document.forms[0].action = "folderDocB4ApplyAclAction.do";
      document.forms[0].submit();
    }else{
      alert("<bean:message key="msg.folderdoc.notselected" />");
    }
  }
  //Called when copyTo or moveTo button is clicked
  function b4CopyMoveFolderDocTo(actionType){
    var itemSelected = false;
    var counter
    if (typeof document.forms[0].chkFolderDocIds!="undefined"){
      if (typeof document.forms[0].chkFolderDocIds.length!="undefined"){
        for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
          if(itemSelected = document.forms[0].chkFolderDocIds[counter].checked){
              break;
          }    
        }
      }else{
        itemSelected = document.forms[0].chkFolderDocIds.checked
      }
    }    
    if (itemSelected){
      if(actionType == 'COPY'){
        document.forms[0].hdnActionType.value = <%=FolderOperation.COPY%>;
        openWindow('folderDocSelectB4Action.do?heading=<bean:message key="lbl.CopyFolderDoc" />&foldersOnly=true&openerControl=hdnTargetFolderPath&recreate=true',"copyToLookup",495,390,0,0,true);
      }else{
        document.forms[0].hdnActionType.value = <%=FolderOperation.MOVE%>;
        openWindow('folderDocSelectB4Action.do?heading=<bean:message key="lbl.MoveFolderDoc" />&foldersOnly=true&openerControl=hdnTargetFolderPath&recreate=true',"moveToLookup",495,390,0,0,true);
      }
    }else{
      alert("<bean:message key="msg.folderdoc.notselected" />");
    }
  }
  //Called when copyTo or moveTo ok button is clicked
  function copyMoveFolderDocTo(){
    var itemSelected = false;
    var counter
    if (typeof document.forms[0].chkFolderDocIds!="undefined"){
      if (typeof document.forms[0].chkFolderDocIds.length!="undefined"){
        for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
          if(itemSelected = document.forms[0].chkFolderDocIds[counter].checked){
            break;
          }    
        }
      }else{
        itemSelected = document.forms[0].chkFolderDocIds.checked
      }
    }
    if (itemSelected){
      document.forms[0].target = "_self";
      var conf = confirm("<bean:message key="msg.overwrite.confirm" />");
      document.forms[0].hdnOverWrite.value=conf;
      document.forms[0].action = "folderDocCopyMoveAction.do";
      document.forms[0].submit();
    }else{
      alert("<bean:message key="msg.folderdoc.notselected" />");
    }
  }
  //Called when copy button is clicked
  function folderDocCopy(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter
    if (rowsInList == 1){
      itemSelected = document.forms[0].chkFolderDocIds.checked
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(itemSelected = document.forms[0].chkFolderDocIds[counter].checked){
            break;
        }    
      }
    }
    if (itemSelected){
      document.forms[0].target="_self";
      document.forms[0].action = "folderDocCopyAction.do" ;
      document.forms[0].submit();
    }else{
      alert("<bean:message key="msg.folderdoc.notselected" />");
    }
  }
  //Called when cut button is clicked
  function folderDocCut(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter
    if (rowsInList == 1){
      itemSelected = document.forms[0].chkFolderDocIds.checked
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(itemSelected = document.forms[0].chkFolderDocIds[counter].checked){
            break;
        }    
      }
    }
    if (itemSelected){
      document.forms[0].target="_self";
      document.forms[0].action = "folderDocCutAction.do" ;
      document.forms[0].submit();
    }else{
      alert("<bean:message key="msg.folderdoc.notselected" />");
    }
  }
  //Called when paste button is clicked
  function folderDocPaste(){
    document.forms[0].target="_self";
    document.forms[0].action = "folderDocB4PasteAction.do" ;
    document.forms[0].submit();
  }
  //Called when Make Versionable  button is clicked
  function docMakeVersionable(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter
    if (rowsInList == 1){
      if(document.forms[0].hdnClassName.value == "FAMILY"){
        alert("<bean:message key="msg.folderdoc.select.nonversioned.document" />");
        return false;
      }    
      itemSelected = document.forms[0].chkFolderDocIds.checked
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
       if(document.forms[0].chkFolderDocIds[counter].checked){
          if(document.forms[0].hdnClassName[counter].value == "FAMILY"){
            alert("<bean:message key="msg.folderdoc.select.nonversioned.document" />");
            return false;
          }
          itemSelected = true;
        }
      }
    }
    if (itemSelected){
      if (confirm("<bean:message key="msg.document.makeversionable" />")){
        document.forms[0].target = "_self"
        document.forms[0].action = "folderDocMakeVersionableAction.do";
        document.forms[0].submit();
      }
    }else{
      alert("<bean:message key="msg.folderdoc.notselected" />");
    }
  }
  //Called when Checkout button is clicked
  function folderDocCheckOut(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter
    if (rowsInList == 1){
      if(document.forms[0].hdnClassName.value != "FAMILY"){
        alert("<bean:message key="msg.folderdoc.select.one.versioned.document" />");
        return false;
      }
      itemSelected = document.forms[0].chkFolderDocIds.checked
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(itemSelected = document.forms[0].chkFolderDocIds[counter].checked){
          if(document.forms[0].hdnClassName[counter].value != "FAMILY"){
            alert("<bean:message key="msg.folderdoc.select.one.versioned.document" />");
            return false;
          }
          break;
        }    
      }
    }
    if (itemSelected){
      openWindow("","CheckOut",190,525,0,0,true);
      document.forms[0].target = "CheckOut"
      document.forms[0].action = "folderDocB4CheckOutAction.do";
      document.forms[0].submit();
    }else{
      alert("<bean:message key="msg.folderdoc.notselected" />");
    }
  }
  //Called when CheckIn button is clicked
  function folderDocCheckIn(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter
    if (rowsInList == 1){
      if(document.forms[0].hdnClassName.value != "FAMILY"){
        alert("<bean:message key="msg.folderdoc.select.one.versioned.document" />");
        return false;
      }
      itemSelected = document.forms[0].chkFolderDocIds.checked
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(itemSelected = document.forms[0].chkFolderDocIds[counter].checked){
          if(document.forms[0].hdnClassName[counter].value != "FAMILY"){
            alert("<bean:message key="msg.folderdoc.select.one.versioned.document" />");
            return false;
          }
          break;
        }    
      }
    }
    if (itemSelected){
      openWindow("","CheckIn",210,525,0,0,true);
      document.forms[0].target = "CheckIn"
      document.forms[0].action = "folderDocB4CheckInAction.do";
      document.forms[0].submit();
    }else{
      alert("<bean:message key="msg.folderdoc.notselected" />");
    }
  }
  //Called when Cancel CheckOut button is clicked
  function folderDocCancelCheckout(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter
    if (rowsInList == 1){
      if(document.forms[0].hdnClassName.value != "FAMILY"){
        alert("<bean:message key="msg.folderdoc.select.one.versioned.document" />");
        return false;
      }
      itemSelected = document.forms[0].chkFolderDocIds.checked
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(itemSelected = document.forms[0].chkFolderDocIds[counter].checked){
          if(document.forms[0].hdnClassName[counter].value != "FAMILY"){
            alert("<bean:message key="msg.folderdoc.select.one.versioned.document" />");
            return false;
          }
          break;
        }    
      }
    }
    if (itemSelected){
      if (confirm("<bean:message key="msg.cancelcheckout.confirm" />")){
        document.forms[0].target = "_self"
        document.forms[0].action = "folderDocCancleCheckoutAction.do";
        document.forms[0].submit();
      }
    }else{
      alert("<bean:message key="msg.folderdoc.notselected" />");
    }
  }
  //Called when History button is clicked
  function docHistory(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter;
    var itemCount = 0;
    thisForm = document.forms[0];
    if (rowsInList == 1){
      if(document.forms[0].chkFolderDocIds.checked){
        if(thisForm.hdnClassName.value == "FAMILY"){
          itemSelected = true;
        }
      itemCount = 1;
      }
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(document.forms[0].chkFolderDocIds[counter].checked){
          if(thisForm.hdnClassName[counter].value == "FAMILY"){                
            itemSelected = true;
          }
          itemCount = itemCount + 1;
        }
      }
    }
    if (itemSelected && itemCount == 1){
      openWindow("","History",440,525,0,0,true);
      document.forms[0].target = "History"
      document.forms[0].action = "docHistoryListAction.do";
      document.forms[0].submit();
    }else{
      if(itemCount > 1){
        alert("<bean:message key="msg.folderdoc.select.one.versioned.document" />");
      }else if(itemCount == 1){
        alert("<bean:message key="msg.SelectDocNotVersioned" />");
      }else if(itemCount == 0){
        alert("<bean:message key="msg.folderdoc.no_doc_selected" />");
      }
    }
  }
  //Called when Download button is clicked
  function docDownload(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter;
    var itemCount = 0;
    var selectedItemValue=0
    thisForm = document.forms[0];
    if (rowsInList == 1){
      if(document.forms[0].chkFolderDocIds.checked){
        if(thisForm.hdnClassName.value != "FOLDER"){
          itemSelected = true;
          selectedItemValue = document.forms[0].chkFolderDocIds.value;
        }
      itemCount = 1;
      }
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(document.forms[0].chkFolderDocIds[counter].checked){
          if(thisForm.hdnClassName[counter].value != "FOLDER"){                
            itemSelected = true;
            selectedItemValue = document.forms[0].chkFolderDocIds[counter].value;
          }
          itemCount = itemCount + 1;
        }
      }
    }
    if (itemSelected && itemCount == 1){
      window.location.replace("docDownloadAction.do?blnDownload=true&documentId=" + selectedItemValue);
    }else{
      if(itemCount > 1){
        alert("<bean:message key="msg.folderdoc.select.one.document" />");
      }else{
        alert("<bean:message key="msg.folderdoc.no_doc_selected" />");
      }
    }
  }

  //Called when view as html button is clicked
  function viewAsHtml(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter;
    var itemCount = 0;
    var selectedItemValue=0
    thisForm = document.forms[0];
    if (rowsInList == 1){
      if(document.forms[0].chkFolderDocIds.checked){
        if(thisForm.hdnClassName.value != "FOLDER"){
          itemSelected = true;
          selectedItemValue = document.forms[0].chkFolderDocIds.value;
        }
      itemCount = 1;
      }
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(document.forms[0].chkFolderDocIds[counter].checked){
          if(thisForm.hdnClassName[counter].value != "FOLDER"){                
            itemSelected = true;
            selectedItemValue = document.forms[0].chkFolderDocIds[counter].value;
          }
          itemCount += 1;
        }
      }
    }
    if (itemSelected && itemCount == 1){
      openWindow('docViewAsHtmlAction.do?documentId='+selectedItemValue,'ViewHtml',650,800,0,0,true,"scrollbars=1");
    }else{
      if(itemCount > 1){
        alert("<bean:message key="msg.folderdoc.select.one.document" />");
      }else{
        alert("<bean:message key="msg.folderdoc.no_doc_selected" />");
      }
    }
  }
  
  //Called when Encrypt button is clicked
  function docEncryptAction(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter;
    var itemCount = 0;
    thisForm = document.forms[0];
    if (rowsInList == 1){
      if(document.forms[0].chkFolderDocIds.checked){
        if(thisForm.hdnClassName.value != "FOLDER"){
          itemSelected = true;
          if(thisForm.hdnClassName.value == "FAMILY"){
            alert("<bean:message key="msg.folderdoc.cannot.encrypt.versiondoc" />");
            return;
          }
        }
        itemCount = 1;
      }
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(document.forms[0].chkFolderDocIds[counter].checked){
          if(thisForm.hdnClassName[counter].value != "FOLDER"){                
            itemSelected = true;
            if(thisForm.hdnClassName[counter].value == "FAMILY"){
              alert("<bean:message key="msg.folderdoc.cannot.encrypt.versiondoc" />");
              return;
            }
          }
          itemCount = itemCount + 1;
        }
      }
    }
    if (itemSelected && itemCount == 1){
      openWindow("","Encryption",120,530,0,0,true);
      document.forms[0].target = "Encryption";
      document.forms[0].action = "b4DocEncryptAction.do";
      document.forms[0].submit();        
    }else{
      if(itemCount > 1){
        alert("<bean:message key="msg.folderdoc.select.one.document" />");
      }else{
        alert("<bean:message key="msg.folderdoc.no_doc_selected" />");
      }
    }
  }
  //Called when Decrypt button is clicked
  function docDecryptAction(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter;
    var itemCount = 0;
    thisForm = document.forms[0];
    if (rowsInList == 1){
      if(document.forms[0].chkFolderDocIds.checked){
        if(thisForm.hdnClassName.value != "FOLDER"){
          itemSelected = true;
        }
        itemCount = 1;
      }
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(document.forms[0].chkFolderDocIds[counter].checked){
          if(thisForm.hdnClassName[counter].value != "FOLDER"){                
              itemSelected = true;
          }
          itemCount = itemCount + 1;
        }
      }
    }
    if (itemSelected && itemCount == 1){
      openWindow("","Decryption",120,530,0,0,true);
      document.forms[0].target = "Decryption";
      document.forms[0].action = "b4DocDecryptAction.do";
      document.forms[0].submit();        
    }else{
        if(itemCount > 1){
          alert("<bean:message key="msg.folderdoc.select.one.document" />");
        }else{
          alert("<bean:message key="msg.folderdoc.no_doc_selected" />");
        }
    }
  }
  //Called when zip button is clicked
  function docZipAction(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter;
    thisForm = document.forms[0];
    if (rowsInList == 1){
      if(document.forms[0].chkFolderDocIds.checked){
          itemSelected = true;
      }
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(document.forms[0].chkFolderDocIds[counter].checked){
          itemSelected = true;
          break;
        }
      }
    }
    if (itemSelected ){
      openWindow("","docZip",120,530,0,0,true);    
      document.forms[0].target = "docZip"
      document.forms[0].action = "b4DocZipAction.do";
      document.forms[0].submit();
    }else{
      alert("<bean:message key="msg.folderdoc.no_doc_selected" />");
    }
  }
  //Called when Unzip button is clicked
  function docUnZipAction(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter;
    var itemCount = 0;
    thisForm = document.forms[0];
    if (rowsInList == 1){
      if(document.forms[0].chkFolderDocIds.checked){
        if(thisForm.hdnClassName.value != "FOLDER" && thisForm.hdnClassName.value != "FAMILY"){
            itemSelected = true;
        }
        itemCount = 1;
      }
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(document.forms[0].chkFolderDocIds[counter].checked){
            if(thisForm.hdnClassName[counter].value != "FOLDER" && thisForm.hdnClassName[counter].value != "FAMILY"){                
              itemSelected = true;
            }
            itemCount = itemCount + 1;
        }
      }
    }
    if (itemSelected && itemCount == 1){
      openWindow("","docUnZip",120,530,0,0,true);    
      document.forms[0].target = "docUnZip"
      document.forms[0].action = "b4DocUnZipAction.do";
      document.forms[0].submit();
    }else{
      alert("<bean:message key="msg.folderdoc.select.one.zipdocument" />");
    }
  }
////////////// Added By Maneesh Start //////////////////////////////////////////
  //Called when mail button is clicked , remember that the target is "_top" now
  // for this iframe and it was "_self" for the original jsp
  function docMailAction(){    
    var itemSelected = false;
    var folderSelected =false;
    var rowsInList = <%=rowsInList%>;
    var counter;
    var itemCount = 0;
    thisForm = document.forms[0];
    if (rowsInList == 1){
      if(document.forms[0].chkFolderDocIds.checked){
        if(thisForm.hdnClassName.value != "FOLDER"){
          itemSelected = true;
        }else{
          folderSelected=true;
        }
        itemCount = 1;
      }
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(document.forms[0].chkFolderDocIds[counter].checked){
          if(thisForm.hdnClassName[counter].value != "FOLDER"){                
              itemSelected = true;
          }else{
              folderSelected=true;
          }
          itemCount = itemCount + 1;
        }
      }
    }
    if (itemSelected && (itemCount > 0) ){
      if (!folderSelected){
        //document.forms[0].target = "_self"
        document.forms[0].target = "_top"
        document.forms[0].action = "mailB4Action.do";
        document.forms[0].submit();
      }else{
        alert("<bean:message key="msg.folderdoc.select_doc_only" />");
      }
    }else{        
      alert("<bean:message key="msg.folderdoc.no_doc_selected" />");
    }
  }
  //Called when fax button is clicked, remember that the target is "_top" now
  // for this iframe and it was "_self" for the original jsp
  function docFaxAction(){
    var itemSelected = false;
    var folderSelected =false;
    var rowsInList = <%=rowsInList%>;
    var counter;
    var itemCount = 0;
    thisForm = document.forms[0];
    if (rowsInList == 1){
      if(document.forms[0].chkFolderDocIds.checked){
        if(thisForm.hdnClassName.value != "FOLDER"){
          itemSelected = true;
        }else{
          folderSelected=true;
        }
        itemCount = 1;
      }
    }        
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(document.forms[0].chkFolderDocIds[counter].checked){
          if(thisForm.hdnClassName[counter].value != "FOLDER"){                
            itemSelected = true;
          }else{
            folderSelected=true;
          }
          itemCount = itemCount + 1;
        }
      }
    }
    if (itemSelected && (itemCount > 0) ){
      if (!folderSelected){
        //document.forms[0].target = "_self"
        document.forms[0].target = "_top"
        document.forms[0].action = "faxB4Action.do";
        document.forms[0].submit();
      }else{
        alert("<bean:message key="msg.folderdoc.select_doc_only" />");
      }
    }else{        
      alert("<bean:message key="msg.folderdoc.no_doc_selected" />");
    }
  }
/////////////////////////////Added by Maneesh End///////////////////////////////
  function generateLinks(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter;
    var itemCount=0;
    if (rowsInList == 1){
      if(document.forms[0].chkFolderDocIds.checked){
        itemSelected=true;
        itemCount=1;
      }
    }
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length; counter++){
        if( document.forms[0].chkFolderDocIds[counter].checked ){
          itemSelected = true;
          itemCount=itemCount+1;
        }
      }
    }
    if (itemSelected){
      openWindow("","GenerateLink",415,700,0,0,true);
      document.forms[0].target = "GenerateLink";
      document.forms[0].action = "docB4GenerateLinkAction.do";
      document.forms[0].submit();
    }else{
      alert("<bean:message key="msg.folderdoc.no_doc4link" />");
    }
  }
  // generate link for folder
  /*function generateLinksForFolder(){
    var itemSelected = false;
    var docSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter = 0;
   
    if( rowsInList == 1 ){
      if( document.forms[0].chkFolderDocIds.checked ){
        if( document.forms[0].hdnClassName.value !="FOLDER" ){
          docSelected = true;
        }
        itemSelected = true;
      }
    }
    
    if( rowsInList > 1 ){
      for( counter = 0; counter < document.forms[0].chkFolderDocIds.length; counter++ ){
        if( document.forms[0].chkFolderDocIds[counter].checked ){
          if( document.forms[0].hdnClassName[counter].value != "FOLDER" ){
            docSelected = true;
            itemSelected = true;
            break;
          }else{
            itemSelected = true;
          }
        }
      }
    }
    
    if (itemSelected && !(docSelected)){
      openWindow("","GenerateLink",415,700,0,0,true);
      document.forms[0].target = "GenerateLink";
      document.forms[0].action = "docB4GenerateLinkAction.do?folders=true";
      document.forms[0].submit();
    }
    if(itemSelected && docSelected){
      alert("<bean:message key="msg.folderdoc.select_folder_only" />");
    }
    if(!itemSelected && !docSelected){
      alert("<bean:message key="msg.folderdoc.no_fldr4link" />");
    }
  }*/
  // Submit Document to WorkFlow
  function submitDocToWf(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter;
    var itemCount = 0;
    thisForm = document.forms[0];
    if (rowsInList == 1){
      if(document.forms[0].chkFolderDocIds.checked){
        itemSelected = true;
        itemCount = 1;
      }
    }
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(document.forms[0].chkFolderDocIds[counter].checked){
          itemSelected = true;
          itemCount = itemCount + 1;
        }
      }
    }
    if (itemSelected && itemCount >= 1){
      openWindow("","ApplyAcl",400,620,0,0,true);
      document.forms[0].target = "ApplyAcl";
      document.forms[0].action = "displayWfAclsAction.do";
      document.forms[0].submit();
    }else{
      if(itemCount > 1){
        alert("<bean:message key="msg.folderdoc.select.one.document" />");
      }else{
        alert("<bean:message key="msg.folderdoc.no_doc_selected" />");
      }
    }
  }
  // Submit Document to WorkFlow
  function submitToWorkFlow(wfAclName,approverWfPrefix){
    document.forms[0].hdnWfName.value = wfAclName;
    if( (approverWfPrefix != null) && (wfAclName.search(approverWfPrefix) == 0) ){
      var itemSelected = false;
      var rowsInList = <%=rowsInList%>;
      var counter;
      var itemCount = 0;
      thisForm = document.forms[0];
      if (rowsInList == 1){
        if(document.forms[0].chkFolderDocIds.checked){
          if(thisForm.hdnClassName.value != "FOLDER"){
            itemSelected = true;
            if(thisForm.hdnClassName.value == "FAMILY"){
                alert("<bean:message key="msg.folderdoc.cannot.submit.versiondoc.toWf" />");
                return;
            }
          }
          itemCount = 1;
        }
      }
      if (rowsInList > 1){
        for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
          if(document.forms[0].chkFolderDocIds[counter].checked){
            if(thisForm.hdnClassName[counter].value != "FOLDER"){
              itemSelected = true;
            }
            itemCount = itemCount + 1;
          }
        }
      }
      if (itemSelected && itemCount == 1){
        document.forms[0].target = "_self";
        document.forms[0].action = "docLinkForWfAction.do";
        document.forms[0].submit();
      }else{
        if(itemCount > 1){
          alert("<bean:message key="msg.folderdoc.select.one.document" />");
        }else{
          alert("<bean:message key="msg.folderdoc.no_doc_selected" />");
        }
      }
    }else{
      document.forms[0].target = "_self";
      document.forms[0].action = "docLinkForWfAction.do";
      document.forms[0].submit();
    }
  }
  // Add watch to POs
  function addWatch(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var counter;
    var itemCount = 0;
    thisForm = document.forms[0];
    if (rowsInList == 1){
      if(document.forms[0].chkFolderDocIds.checked){
        if(thisForm.hdnClassName.value != "FOLDER"){
          itemSelected = true;
        }else{
          alert("<bean:message key="msg.folderdoc.select_doc_only" />");
          return;
        }
        itemCount = 1;
      }
    }
    if (rowsInList > 1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(document.forms[0].chkFolderDocIds[counter].checked){
          if(thisForm.hdnClassName[counter].value != "FOLDER"){
            itemSelected = true;
          }else{
            alert("<bean:message key="msg.folderdoc.select_doc_only" />");
            return;
          }
          itemCount = itemCount + 1;
        }
      }
    }
    if (itemSelected && itemCount >= 1){
      document.forms[0].target = "_self";
      document.forms[0].action = "folderDocAddWatchAction.do";
      document.forms[0].submit();
    }else{
      alert("<bean:message key="msg.folderdoc.no_doc_selected" />");
    }
  }
  //Called When Navigation bar buttons Clicked
  function folderDocNavigate(){
    var thisForm=document.forms[0]
    thisForm.hdnActionType.value=<%=FolderOperation.NAVIGATE%>
    thisForm.txtPageNo.value=navBar.getPageNumber();
    thisForm.target = "_self"
    thisForm.action = "folderDocNavigateAction.do";
    thisForm.submit();
  }
  //Called When Search / Folder Button is Clicked
  function folderSearchFolders(){
      <% if( folderDocInfo.isTreeVisible() ) {%>
        window.top.frames['iframeTreeview'].location.href="b4SearchAction.do";
      <%}else{%>
        window.top.frames['iframeTreeview'].location.href="treeview.jsp"
      <%}%>
      document.forms[0].target="_self";
      document.forms[0].action = "toggleFolderSearchAction.do";
      document.forms[0].submit();
  }
  //Select All
  function checkAll(menu){
    var thisForm = document.forms[0];
    var checkValue =menu?(thisForm.chkAll.checked=true):((thisForm.chkAll.checked)?true:false);
    if(typeof thisForm.chkFolderDocIds != "undefined"){
      var totalRows = thisForm.chkFolderDocIds.length;
      if (typeof totalRows != "undefined") {
        var count=0;                
        for (count=0;count<totalRows;count++) {
          thisForm.chkFolderDocIds[count].checked=checkValue;
        }
      }else{
        thisForm.chkFolderDocIds.checked=checkValue;
      }
    }
  }
  //Invert Selection    
  function invertSelection(){
    var thisForm = document.forms[0];
    var isAllSelected=true;
    if(typeof thisForm.chkFolderDocIds != "undefined"){
      var totalRows = thisForm.chkFolderDocIds.length;
      if (typeof totalRows != "undefined") {
        var count=0;                
        for (count=0;count<totalRows;count++) {
          thisForm.chkFolderDocIds[count].checked=(thisForm.chkFolderDocIds[count].checked?false:true);
        }
        for (count=0;count<totalRows;count++) {
          if (!thisForm.chkFolderDocIds[count].checked){
            isAllSelected=false;
            break;
          }
        }
      }else{
        isAllSelected=(thisForm.chkFolderDocIds.checked?false:true);
        thisForm.chkFolderDocIds.checked=isAllSelected
      }
      thisForm.chkAll.checked=isAllSelected
    }
  }
  //UnSelect All
  function unCheckChkAll(me){
    var thisForm=me.form;
    var totalCount =thisForm.chkFolderDocIds.length;
    var checkValue = (me.checked)?true:false;
    var isAllSelected=false;
    if(checkValue){        
      if (typeof totalCount != "undefined") {
        for (var count=0;count<totalCount;count++) {
          if(thisForm.chkFolderDocIds[count].checked){
            isAllSelected=true;           
          }else{
            isAllSelected=false;
            break;
          }
        }      
      }else{
        if(thisForm.chkFolderDocIds.checked){
          isAllSelected=true;           
        }else{
          isAllSelected=false;
        }
      }        
    }
    thisForm.chkAll.checked=isAllSelected;
  }
  // Function to view non-versioned document log
  function auditLog(){
    var itemSelected = false;
    var rowsInList = <%=rowsInList%>;
    var itemCount = 0;
    var counter;
    thisForm = document.forms[0];
    if(rowsInList == 1){
      if(document.forms[0].chkFolderDocIds.checked){
        if(thisForm.hdnClassName.value!= "Folder"){
          itemSelected = true;
        }
        itemCount = 1;
      }
    }
    if(rowsInList>1){
      for(counter=0; counter < document.forms[0].chkFolderDocIds.length;counter++){
        if(document.forms[0].chkFolderDocIds[counter].checked){
          if(thisForm.hdnClassName[counter].value != "FOLDER"){                
              itemSelected = true;
          }
          itemCount = itemCount + 1;
        }
      } 
    }
    if (itemSelected && itemCount == 1){
      openWindow("","ViewLog",455,700,0,0,true);
      document.forms[0].target = "ViewLog";
      document.forms[0].action = "docViewLogAction.do";
      document.forms[0].submit();        
    }else{
      if(itemCount > 1){
        alert("<bean:message key="msg.folderdoc.select.one.document" />");
      }else{
        alert("<bean:message key="msg.folderdoc.no_doc_selected" />");
      }
    }
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
    document.forms[0].target="_self";
    document.forms[0].action="gotoHomeFolderAction.do";
    document.forms[0].submit();        
  }

  function window_onload(){   

    <%if((request.getAttribute("OverWrite")) != null){%>
        openWindow("folder_doc_b4_paste.jsp","B4Paste",150,330,0,0,true);                
    <%}%> 
  }
  var navBar=new NavigationBar("navBar");
  navBar.setPageNumber(<%=folderDocInfo.getPageNumber()%>);
  navBar.setPageCount(<%=folderDocInfo.getPageCount()%>);
  navBar.onclick="folderDocNavigate()";

  navBar.msgPageNotExist="<bean:message key="page.pageNumberNotExists" />";
	navBar.msgNumberOnly="<bean:message key="page.enterNumbersOnly"/>";    
	navBar.msgEnterPageNo="<bean:message key="page.enterPageNo"/>";
	navBar.msgOnFirst="<bean:message key="page.onFirst"/>";
	navBar.msgOnLast="<bean:message key="page.onLast"/>";

	navBar.lblPage="<bean:message key="lbl.Page"/>";
	navBar.lblOf="<bean:message key="lbl.Of"/>";

	navBar.tooltipPageNo="<bean:message key="tooltips.PageNo"/>";
	navBar.tooltipFirst="<bean:message key="tooltips.First"/>";
	navBar.tooltipNext="<bean:message key="tooltips.Next"/>";
	navBar.tooltipPrev="<bean:message key="tooltips.Previous"/>";
	navBar.tooltipLast="<bean:message key="tooltips.Last"/>";
	navBar.tooltipGo="<bean:message key="btn.Go"/>";
  
  // for webdav
  function editDoc(path,type){
    var browserName=navigator.appName; 
    if (browserName=="Microsoft Internet Explorer"){
      if((type == "application/msword") || (type == "application/rtf") || (type == "text/plain")){
        var WordObj;
        WordObj = new ActiveXObject("Word.Application");
        WordObj.Visible = true;
        WordObj.Documents.Open(path);
      }else if(type=="application/vnd.ms-excel"){
        var ExcelObj;
        ExcelObj = new ActiveXObject("Excel.Application");
        ExcelObj.Visible = true;
        ExcelObj.Workbooks.Open(path);    
      }else if(type=="application/vnd.ms-powerpoint"){
        var WordObj;
        WordObj = new ActiveXObject("PowerPoint.Application");
        WordObj.Visible = true;
        WordObj.Presentations.Open(path);
      }
      folderRefresh();
    }else{
      alert("Edit document feature is only available with Internet Explorer")
    }
  }

  function openNewDoc(davpath){
    var browserName=navigator.appName; 
    if (browserName=="Microsoft Internet Explorer"){
      openWindow("","newDoc",150,530,0,0,true);
      document.forms[0].target = "newDoc";
      document.forms[0].action = "b4DocNewAction.do?docType=doc";
      document.forms[0].submit();
    }else{
      alert("New MSWord document creation is only available with Internet Explorer")
    }
  }

  function openNewExcel(davpath){
    var browserName=navigator.appName; 
    if (browserName=="Microsoft Internet Explorer"){
      openWindow("","newDoc",150,530,0,0,true);
      document.forms[0].target = "newDoc";
      document.forms[0].action = "b4DocNewAction.do?docType=xls";
      document.forms[0].submit();
    }else{
      alert("New Excel document creation is only available with Internet Explorer")
    }
  }
      
  function openNewPowerPoint(davpath){
    var browserName=navigator.appName; 
    if (browserName=="Microsoft Internet Explorer"){
      openWindow("","newDoc",150,530,0,0,true);
      document.forms[0].target = "newDoc";
      document.forms[0].action = "b4DocNewAction.do?docType=ppt";
      document.forms[0].submit();
    }else{
      alert("New Power Point document creation is only available with Internet Explorer")
    }
  }
  
  function openNewText(davpath){
    var browserName=navigator.appName; 
    if (browserName=="Microsoft Internet Explorer"){
      openWindow("","newDoc",150,530,0,0,true);
      document.forms[0].target = "newDoc";
      document.forms[0].action = "b4DocNewAction.do?docType=txt";
      document.forms[0].submit();
    }else{
      alert("New Text document creation is only available with Internet Explorer")
    }
  }

</script>
</head>
<body style="margin:0" onload="window_onload();">
<script>
  <% if((request.getAttribute("fromToggle"))!= null) {
      if(((String)request.getAttribute("fromToggle")).equals("forSearch")){%>
        window.top.frames['iframeTreeview'].location.href="b4SearchAction.do";
      <%}%>
  <%}else{%>
    <%if((!folderDocInfo.isNoReloadTree())){%>
        window.top.frames['iframeTreeview'].location.reload();
    <%}%>
  <%}%>
  window.top.document.forms[0].txtAddress.value="<%=folderDocInfo.getCurrentFolderPath()%>";
  <% folderDocInfo.setNoReloadTree(false); %>
</script>
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
<table id="formTab" width="100%"  border="0" cellpadding="0" cellspacing="0" class="borderClrLvl_2 imgToolBar bgClrLvl_3" >
<tr>
  <td width="750px" valign="top" >
    <table id="dataParent" width="100%"  border="0" cellspacing="0" cellpadding="0">
    <!-- this table is having 2 tr, 1st tr contains the 'data' table, 2nd tr contains the 'navBar' table-->
      <tr>
        <td>
        <%if(folderDocInfo.getListingType() == FolderDocInfo.DISPLAY_PAGE){%>
          <div style="overflow:auto; width:100%; height:353px" class="imgData bgClrLvl_4"  >
            <iframe src="docDownloadAction.do?documentId=<%=folderDocInfo.getDocId()%>" scrolling="auto" height="333px" width="720px" class="imgData blueBorder" style="position:relative;left:10px;top:10px;bottom:10px;" frameborder="0"></iframe>
          </div>
        <%}else{%>
          <div style="overflow:auto; width:100%; height:353px" class="imgData bgClrLvl_4"  >
            <table id="data" width="100%" border="0" cellpadding="1" cellspacing="1" class="bgClrLvl_F">
              <tr>
                <th width="2%" height="18px"></th>
                <th width="3%" height="18px"><input type="checkbox" onclick="checkAll()" name="chkAll" title="<bean:message key="tooltips.SelectAll" />" /></th>
                <th width="27%">
                  <% if(!upButtonDisabled){ %>
                    <a style="margin-left:5px;" onclick="folderUp();" class="imgGoUpDataTbl" title="<bean:message key="tooltips.Up" />" ></a>
                  <%}else{%>
                    <div style="margin-left:5px;" class="imgGoUpDataTblDisable"></div>
                  <%}%>
                  <bean:message key="tbl.head.Name" />
                </th>
                <th width="8%"><bean:message key="tbl.head.Size" /></th>
                <th width="20%"><bean:message key="tbl.head.Type" /></th>
                <th width="18%"><bean:message key="tbl.head.ModifiedDate" /></th>
              </tr>
              <%if(folderDocLists.size() > 0 ){%>
                <logic:iterate id="folderDocList" name="folderDocLists" type="dms.web.beans.filesystem.FolderDocList" >
                <%if (firstTableRow == true){ firstTableRow = false; %>
                  <tr class="bgClrLvl_4">
                <%}else{ firstTableRow = true; %>
                  <tr class="bgClrLvl_3">
                <%}%>
                    <td>
                      <%if((folderDocList.getDavPath()!=null)){%>
                        <html:hidden property="hdnDavPath" value="<%=folderDocList.getDavPath()%>" />
                        <html:hidden property="hdnDavType" value="<%=folderDocList.getType()%>" />
                        <%if (!folderDocList.isCheckedOut()) {%>
                          <a onclick="editDoc('<%=folderDocList.getDavPath()%>', '<%=folderDocList.getType()%>');" class="imgDocEdit" title="<bean:message key="tooltips.EditDoc" />" ></a>
                        <%}else{%>
                          <a onclick="alert('<bean:message key='msg.folderdoc.cannotEdit' />')" class="imgDocEdit" title="<bean:message key="tooltips.EditDoc" />" ></a>
                        <%}%>
                      <%}%>
                    </td>
                    <td>
                      <div align="center">
                        <html:multibox property="chkFolderDocIds" onclick="unCheckChkAll(this);" >
                          <bean:write name="folderDocList"  property="id" />
                        </html:multibox>
                      </div>
                      <bean:define id="className" property="className" name="folderDocList" />
                      <html:hidden property="hdnClassName" value="<%=className%>" />
                    </td>
                    <td title="<bean:write name="folderDocList" property="name" />">
                      <html:hidden property="name" value="<%=folderDocList.getName()%>" />
                      <%if(folderDocList.getClassName().equals("FOLDER")){%>
                        <a class="imgFolder"></a>
                        <a href="folderDocFolderClickAction.do?currentFolderId=<bean:write name="folderDocList" property="id" />" class="dataLink">
                        <bean:write name="folderDocList" property="pathOrTrimmedName" />
                      <%if(folderDocInfo.getListingType()!=folderDocInfo.SEARCH_LISTING){
                        int item=folderDocList.getItem();
                        if(folderDocList.getItem()<=1){%>
                        (<b><%=item%></b>)
                        <%}else{%>
                        (<b><%=item%></b>)
                        <%}%>
                        </a>
                      <%}}else{%>
                        <%if(folderDocList.getClassName().equals("FAMILY")){
                            if(folderDocList.isCheckedOut()){
                        %>
                            <a class="imgDocumentChecked"></a>
                            <%if(userPreferences.getChkOpenDocInNewWin() == UserPreferences.IN_NEW_WINDOW){%>
                              <a target=newwin href="b4DocDownloadAction.do?documentId=<bean:write name="folderDocList" property="id" />" class="dataLink">
                            <%}else{%>
                              <a href="b4DocDownloadAction.do?documentId=<bean:write name="folderDocList" property="id" />" class="dataLink">
                            <%}%>
                              <bean:write name="folderDocList" property="pathOrTrimmedName" />
                            </a>
                          <%}else{ %>
                              <a class="imgDocuments"></a>
                              <%if(userPreferences.getChkOpenDocInNewWin() == UserPreferences.IN_NEW_WINDOW){%>
                                <a target=newwin href="b4DocDownloadAction.do?documentId=<bean:write name="folderDocList" property="id" />" class="dataLink">
                              <%}else{%>
                                <a href="b4DocDownloadAction.do?documentId=<bean:write name="folderDocList" property="id" />" class="dataLink">
                              <%}%>
                                <bean:write name="folderDocList" property="pathOrTrimmedName" />
                              </a>
                          <%}%>
                      <%}else{%>
                          <bean:define id="docName" name="folderDocList" property="name" type="String" />
                          <%if(docName.lastIndexOf(".") != -1 ){%>
                            <%if(folderDocList.isEncripted()){%>
                              <a class="imgDocumentEncrypted"></a>
                            <%}else if(docName.substring(docName.lastIndexOf(".")).toLowerCase().equals(".zip")){%>
                              <a class="imgDocumentZipped"></a>
                            <%}else{%>
                              <a class="imgDocument"></a>
                            <%}%>
                          <%}else{%>
                            <a class="imgDocument"></a>
                          <%}%>
                          <%if(userPreferences.getChkOpenDocInNewWin() == UserPreferences.IN_NEW_WINDOW){%>
                            <a target=newwin href="b4DocDownloadAction.do?documentId=<bean:write name="folderDocList" property="id" />" class="dataLink">
                          <%}else{%>
                            <a href="b4DocDownloadAction.do?documentId=<bean:write name="folderDocList" property="id" />" class="dataLink">
                          <%}%>
                          <bean:write name="folderDocList" property="pathOrTrimmedName" />
                          </a>
                        <%}%>
                      <%}%>
                    </td>
                    <td align="right"><bean:write name="folderDocList" property="size" />&nbsp;</td>
                    <td>
                      <%if(folderDocList.getType().equals("")){%>
                        <bean:message key="lbl.type.unknown" />
                        <html:hidden property="hdnTypes" value="" />
                      <%}else{%>
                        <bean:write name="folderDocList" property="type" />
                        <html:hidden property="hdnTypes" value="<%=folderDocList.getType()%>" />
                      <%}%>
                    </td>
                    <td><bean:write name="folderDocList" property="modifiedDate" /></td>
                  </tr>
                </logic:iterate>
              <%}%>
            </table>
            <!-- data table ends above-->
            <%if(folderDocLists.size() == 0 ){%>
            <div style="position:relative; top:175px; text-align:center;" class="tabText">
              <bean:message key="info.no_item_found.no_item_found" />
            </div>
            <%}%>
          </div>
          <%}%>
        </td>
      </tr>
    </table>
  </td>
  </tr>
  </table>
  <table id="spacerTab" width="100%"  border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td height="2px"></td>
    </tr>
  </table>
  <table class="borderClrLvl_2 imgStatusBar bgClrLvl_4 " width="750px" border="0" cellpadding="0" cellspacing="0" id="statusBar">
    <tr>
      <td height="20px" width="30px"><div class="imgStatusMsg"></div></td>
      <td width="527px">
        <html:messages id="actionMessage" message="true">
          <bean:write  name="actionMessage"/>
        </html:messages>
        <html:messages id="actionError">
          <font color="red"> <bean:write  name="actionError"/></font>
        </html:messages>
      </td>
      <td width="3px">
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
        <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
      </td>
      <td width="200px" align="right">
        <script>navBar.render();</script>
      </td>
    </tr>
  </table>
  <script>changeNavDiv();</script>
   <!-- statusBar table ends above-->
</html:form>
</body>
</html:html>
