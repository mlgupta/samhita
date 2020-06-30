<%@ page import="dms.web.beans.user.*" %>
<%@ page import="dms.web.beans.filesystem.*" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="listBeans" name="listBeans" type="java.util.ArrayList" />
<bean:define id="userPreferences" name="UserPreferences" type="dms.web.beans.user.UserPreferences" />

<%
//Variable declaration
boolean firstTableRow;
firstTableRow = true;
String prefix = (String)request.getAttribute("prefix");
%>

<html:html>
<head>
<title><bean:message key="title.ManageAdapterFolder" /></title>
<jsp:include page="/style_sheet_include.jsp" />
<script src="general.js"></script>
<script language="javascript" type="text/javascript">
<!--
//UnSelect All
function unCheckChkAll(me){
  var thisForm=me.form;
  var totalCount =thisForm.chkManagedFolderKeys.length;
  var checkValue = (me.checked)?true:false;
  var isAllSelected=false;
  if(checkValue){        
    if (typeof totalCount != "undefined") {
      for (var count=0;count<totalCount;count++) {
        if(thisForm.chkManagedFolderKeys[count].checked){
          isAllSelected=true;           
        }else{
          isAllSelected=false;
          break;
        }
      }      
    }else{
        if(thisForm.chkManagedFolderKeys.checked){
          isAllSelected=true;           
        }else{
          isAllSelected=false;
        }
    }        
  }
  thisForm.chkAll.checked=isAllSelected;
}
//Select All
function checkAll(menu){
  var thisForm = document.forms[0];
  var checkValue =menu?(thisForm.chkAll.checked=true):((thisForm.chkAll.checked)?true:false);
  if(typeof thisForm.chkManagedFolderKeys != "undefined"){
    var totalRows = thisForm.chkManagedFolderKeys.length;
    if (typeof totalRows != "undefined") {
      var count=0;                
      for (count=0;count<totalRows;count++) {
        thisForm.chkManagedFolderKeys[count].checked=checkValue;
      }
    }else{
        thisForm.chkManagedFolderKeys.checked=checkValue;
    }
  }
}

// close request window
function closewindow(){
  window.close();
}

// delete entry
function deleteManagedFolders(){
  var thisForm = document.forms[0];
  var totalCount = thisForm.chkManagedFolderKeys.length;
  var itemCount = 0;
  if( typeof totalCount!= "undefined" ){
    for (var count=0;count<totalCount;count++) {
      if(thisForm.chkManagedFolderKeys[count].checked){
        itemCount += 1;           
      }
    }      
  }else{
    var itemName = thisForm.chkManagedFolderKeys.value; 
    if( confirm("<bean:message key="msg.delete.confirm" /> \n\"" + itemName + "\" ?") ){
      thisForm.chkManagedFolderKeys.checked = true;
      itemCount += 1;
    }else{
      return false;
    }
  }
  if( itemCount > 0 ){
    thisForm.target = "_self";
    thisForm.submit();
  }else{
    alert("Select An Item");
    return false;
  }
}
//-->
</script>
</head>

<body style="margin:7px">
<html:form action="/deleteManagedKeyAction" name="listManagedFoldersForm" type="adapters.actionforms.ListManagedFoldersForm" >
<html:hidden name="listManagedFoldersForm" property="hdnPrefix" value="<%=prefix%>" />
<table id="tabContainer" align="center" width="522px"  border="0" cellspacing="0" cellpadding="0">
<!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->
<tr><td height="5px"></td></tr>
<tr>
  <td >
    <table id="tabParent" align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td>
          <table id="tab" width="80px" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="5px" class="imgTabLeft"></td>
              <td width="70px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.Access" /></div></td>
              <td width="5px" class="imgTabRight"></td>            
            </tr>
          </table>
        </td>
  		</tr>
    </table>
    <table id="borderClrLvl_2" align="center" width="100%"  border="0" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2"  >
      <tr><td height="15px"></td></tr>
      <tr>
        <td align="center">
          <div style="overflow:auto;width:97%;height:230px" class="imgData borderClrLvl_2 bgClrLvl_4">
            <table width="99%" class="bgClrLvl_F" border="0"  cellpadding="1" cellspacing="1" id="innerTabContainer">
            <tr>
              <th align="center" width="5%" height="18px"><input type="checkbox" style="height:12px" onclick="checkAll()" name="chkAll" title="Select All"></th>
              <th align="center" width="35%"><bean:message key="title.Folder.Access.Id" /></th>
              <th align="center" width="60%"><bean:message key="title.ManageFolderPath" /></th>
            </tr>
            <% if(listBeans.size() > 0) {%>
            <logic:iterate id="listBean" name="listBeans">
              <%if (firstTableRow == true){ firstTableRow = false; %>
                <tr class="bgClrLvl_4">
              <%}else{ firstTableRow = true; %>
                <tr class="bgClrLvl_3">                  
              <%}%>
                <td height="18px" align="center" >
                  <bean:define id="accessId" name="listBean" property="accessId" />
                  <html:checkbox name="listManagedFoldersForm" property="chkManagedFolderKeys" onclick="unCheckChkAll(this);" value="<%=accessId%>" />
                </td> 
                <td align="left" title="<bean:write name="listBean" property="accessId" />" >
                  <bean:write name="listBean" property="trimAccessId" />
                </td>
                <td align="left" title="<bean:write name="listBean" property="managedFolderPath" />" >
                  <bean:write name="listBean" property="trimManagedFolderPath" />
                </td>
              </tr>
              </logic:iterate>
            <%}%>
            </table>
            <%if( listBeans.size() == 0 ){%>
              <div style="position:relative; top:115px; text-align:center;" class="tabText">
                <bean:message key="info.no_item_found.no_item_found" />
              </div> 
            <%}%>
          </div>
        </td>
      </tr>
      <tr>
        <td align="right" style="margin-right:5px;" height="30px">
          <% if(listBeans.size() > 0) {%>
            <html:button property="btnDelete" onclick="deleteManagedFolders();" styleClass="buttons" style="width:45px;"><bean:message key="btn.Delete" /></html:button>
          <%}%>
          <html:button property="btnOK" onclick="closewindow()" styleClass="buttons" style="width:45px;margin-right:7px;"><bean:message key="btn.Ok" /></html:button>
        </td>
      </tr>
    </table>
  </td>
</tr>
</table>
</html:form>
</body>
</html:html>