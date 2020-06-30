<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.ArrayList" %>
<bean:define id="folderDocInfo" name="FolderDocInfo" type="dms.web.beans.filesystem.FolderDocInfo" />
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Cache-Control","no-store"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<%
boolean isFirstSet = ((Boolean)request.getAttribute("isFirstSet")).booleanValue();
boolean isLastSet = ((Boolean)request.getAttribute("isLastSet") ).booleanValue();
%>
<html:html>
<head>
<jsp:include page="/style_sheet_include.jsp" />
<title><bean:message key="title.FolderDocList" /></title>
<script src="general.js"></script>
<script>
  function checkText(folderId){
    var spanObj = document.getElementById(folderId);
    spanObj.style.cursor="pointer";
    spanObj.style.textDecoration="underline";
  }

  function normalText(folderId){
    var spanObj = document.getElementById(folderId);
    spanObj.style.cursor="pointer";
    spanObj.style.textDecoration="none";
  }

  function clickOnFolder(folderId){
    window.top.frames[1].location.replace("folderDocFolderClickAction.do?currentFolderId="+ folderId);
  }
</script>
</head>
<body style="margin:0; width:100%" >
<table width="100%"  border="0" cellpadding="0" cellspacing="0" class="imgToolBar bgClrLvl_3 ">
  <tr><td height="1px" colspan="3" class="bgClrLvl_F"></td></tr>
  <tr><td height="1px" colspan="3" class="imgToolBar bgClrLvl_3"></td></tr>
  <tr>
    <td width="30px" height="20px" align="left">
      <% if( !isFirstSet ){%>
        <a href="folderNextAction.do?oper=prev" target="_self" class="imgMoreLeftFolders" onMouseOut="this.className='imgMoreLeftFolders'" onMouseOver="this.className='imgMoreLeftFolders'" title="Previous"></a>
      <% } %>
    </td>
  
    <td height="20px" width="920px" align="left">
      <%
        String folderId;
        String folderName;
        ArrayList listOfParents = folderDocInfo.getListOfParents();
        ArrayList listOfParentsId = folderDocInfo.getListOfParentsId();
        int listOfParentsLength = listOfParents.size();
        folderId = (String)listOfParentsId.get(0);
        folderName = (String)listOfParents.get(0);
      %>
        <% if( folderDocInfo.getHierarchySetNo() == 1 ){ %>
          <span id="<%=folderId%>" class="menuFldr" style="cursor:pointer;text-decoration:none;font-weight:bold;color:#333333;" onMouseOver="return checkText(<%=folderId%>);" onMouseOut="return normalText(<%=folderId%>);" onClick="clickOnFolder(<%=folderId%>);"><%=folderName%></span>
          <span style="margin-right:5px; margin-left:5px">/</span>
        <%}else{%>
          <span id="<%=folderId%>" class="menuFldr" style="cursor:pointer;text-decoration:none;font-weight:bold;color:#333333;" onMouseOver="return checkText(<%=folderId%>);" onMouseOut="return normalText(<%=folderId%>);" onClick="clickOnFolder(<%=folderId%>);"><%=folderName%></span>
        <%}%>
      <%
        if(listOfParentsLength > 1){
          folderId = (String)listOfParentsId.get(1);
          folderName = (String)listOfParents.get(1);
      %>
        <% if( folderDocInfo.getHierarchySetNo() == 1 ) {%>
          <span id="<%=folderId%>" class="menuFldr" style="cursor:pointer;text-decoration:none;font-weight:bold;color:#333333;" onMouseOver="return checkText(<%=folderId%>);" onMouseOut="return normalText(<%=folderId%>);" onClick="clickOnFolder(<%=folderId%>);"><%=folderName%></span>
        <%}else{%>
          <span style="margin-right:5px; margin-left:5px">/</span>
          <span id="<%=folderId%>" class="menuFldr" style="cursor:pointer;text-decoration:none;font-weight:bold;color:#333333;" onMouseOver="return checkText(<%=folderId%>);" onMouseOut="return normalText(<%=folderId%>);" onClick="clickOnFolder(<%=folderId%>);"><%=folderName%></span>
        <%}%>
        
      <%}%>
      <%
        for(int index = 2; index < listOfParentsLength; index++){
          folderId = (String)listOfParentsId.get(index);
          folderName = (String)listOfParents.get(index);
      %>
          <span style="margin-right:5px; margin-left:5px">/</span>
          <span id="<%=folderId%>" class="menuFldr" style="cursor:pointer;text-decoration:none;font-weight:bold;color:#333333;" onMouseOver="return checkText(<%=folderId%>);" onMouseOut="return normalText(<%=folderId%>);" onClick="clickOnFolder(<%=folderId%>);"><%=folderName%></span>
      <%}%>
    </td>

    <td width="30px" height="20px" align="right">
      <% if( !isLastSet ){%>
        <a href="folderNextAction.do?oper=next" target="_self" class="imgMoreRightFolders" onMouseOut="this.className='imgMoreRightFolders'" onMouseOver="this.className='imgMoreRightFolders'" title="Next"></a>
      <% } %>
    </td>
  </tr>
  <tr><td height="2px" colspan="3" ></td></tr>
  <tr><td height="1px" class="bgClrLvl_2" colspan="3"></td></tr>
</table>
</body>
</html:html>
