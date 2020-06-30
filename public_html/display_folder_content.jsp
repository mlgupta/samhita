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
int rowsInList = 0;
ArrayList folderDocLists = (ArrayList)request.getAttribute("folderDocLists");
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
<title><bean:message key="title.Folder.Contents" /></title>
<script src="general.js"></script>
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
<script language="JavaScript" type="text/JavaScript">
  function docDownload(docId){
    openWindow("docDownloadAction.do?blnDownload=true&documentId=" + docId,"DownloadDoc",650,800,0,0,true,"scrollbars=1,menubar=1");
  }
  //Called when Download button is clicked
  /*function docDownload(){
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
      openWindow("docDownloadAction.do?blnDownload=true&documentId=" + selectedItemValue,"ViewHtml",650,800,0,0,true,"scrollbars=1,menubar=1");
    }else{
      if(itemCount > 1){
        alert("<bean:message key="msg.folderdoc.select.one.document" />");
      }else{
        alert("<bean:message key="msg.folderdoc.no_doc_selected" />");
      }
    }
  }*/
  //Select All
  function checkAll(val){
    var thisForm = document.forms[0];
    var checkValue =val;
    thisForm.chkAll.checked = val;
    thisForm.chkAllBtm.checked = val;
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
  //UnSelect All
  function unCheckChkAll(val){
    var thisForm=document.forms[0];
    var totalCount =thisForm.chkFolderDocIds.length;
    var checkValue = val;
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
    thisForm.chkAllBtm.checked=isAllSelected;
  }

</script>
</head>
<body style="margin:0">
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
  <td width="100%" valign="top" >
    <table id="dataParent" width="100%"  border="0" cellspacing="0" cellpadding="0">
    <!-- this table is having 2 tr, 1st tr contains the 'data' table, 2nd tr contains the 'navBar' table-->
      <tr>
        <td>
          <table id="parentTab" width="100%" class="imgData bgClrLvl_4" style="height:450px;" >          
            <table id="data" width="100%" border="0" cellpadding="1" cellspacing="1" class="bgClrLvl_F">
              <tr>
                <th width="26%" height="18px;"><bean:message key="tbl.head.Name" /></th>
                <th width="8%"><bean:message key="tbl.head.Size" /></th>
                <th width="23%"><bean:message key="tbl.head.Type" /></th>
                <th width="18%"><bean:message key="tbl.head.ModifiedDate" /></th>
                <th width="26%"><bean:message key="tbl.head.Description" /></th>
              </tr>
              <%if(folderDocLists.size() > 0 ){%>
              <logic:iterate id="folderDocList" name="folderDocLists" type="dms.web.beans.filesystem.FolderDocList" >
              <%if (firstTableRow == true){ firstTableRow = false; %>
                <tr class="bgClrLvl_4">
              <%}else{ firstTableRow = true; %>
                <tr class="bgClrLvl_3">
              <%}%>
                <td height="18px;" title="<bean:write name="folderDocList" property="name" />">
                  <html:hidden property="name" value="<%=folderDocList.getName()%>" />
                    <%if(folderDocList.getClassName().equals("FAMILY")){
                      if(folderDocList.isCheckedOut()){
                      %>
                        <a class="imgDocumentChecked"></a>
                        <span style="cursor:pointer;cursor:hand;" title="Cannot Download Checked Out <bean:write name="folderDocList" property="name" /> Document" class="dataLink">
                          <bean:write name="folderDocList" property="pathOrTrimmedName" />
                        </span>
                      <%}else{ %>
                          <a class="imgDocuments"></a>
                          <span style="cursor:pointer;cursor:hand;" onclick="docDownload(<bean:write name='folderDocList' property='id' />)" title="Download <bean:write name="folderDocList" property="name" /> Now" class="dataLink">
                            <bean:write name="folderDocList" property="pathOrTrimmedName" />
                          </span>
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
                        <span style="cursor:pointer;cursor:hand;" onclick="docDownload(<bean:write name='folderDocList' property='id' />)" title="Download <bean:write name="folderDocList" property="name" /> Now" class="dataLink">
                          <bean:write name="folderDocList" property="pathOrTrimmedName" />
                        </span>
                    <%}%>
                </td>
                <td align="right"><bean:write name="folderDocList" property="size"  />&nbsp;</td>
                <td>
                  <%if(folderDocList.getType().equals("")){%>
                    <bean:message key="lbl.type.unknown" />
                    <html:hidden property="hdnTypes" value="" />
                  <%}else{%>
                    <bean:write name="folderDocList" property="type" />
                    <html:hidden property="hdnTypes" value="<%=folderDocList.getType()%>" />
                  <%}%>
                </td>
                <td><bean:write name="folderDocList" property="modifiedDate" /> </td>
                <td title="<bean:write name="folderDocList" property="description" />"><bean:write name="folderDocList" property="trimDescription" /></td>
                </tr>
              </logic:iterate>
              <tr>
                <th height="18px;" width="26%"><bean:message key="tbl.head.Name" /></th>
                <th width="8%"><bean:message key="tbl.head.Size" /></th>
                <th width="23%"><bean:message key="tbl.head.Type" /></th>
                <th width="18%"><bean:message key="tbl.head.ModifiedDate" /></th>
                <th width="26%"><bean:message key="tbl.head.Description" /></th>
              </tr>
              <%}%>
            </table>
            <!-- data table ends above-->
          </table>
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
  <table class="borderClrLvl_2 imgStatusBar bgClrLvl_4 " width="100%" border="0" cellpadding="0" cellspacing="0" id="statusBar">
    <tr>
      <td height="20px" width="30px"><div class="imgStatusMsg"></div></td>
      <td width="955px">
        <html:messages id="actionMessage" message="true">
          <bean:write  name="actionMessage"/>
        </html:messages>
        <html:messages id="actionError">
          <font color="red"> <bean:write  name="actionError"/></font>
        </html:messages>
      </td>
    </tr>
  </table>
   <!-- statusBar table ends above-->
</html:form>
</body>
</html:html>
