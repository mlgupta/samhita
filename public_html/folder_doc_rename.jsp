<%@ page errorPage="error_handler.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="java.util.*" %>
<%@ page import="dms.web.beans.user.*" %>
<%@ page import="dms.web.beans.filesystem.*" %>

<html:html>
<head>
<title><bean:message key="title.RenameDocument" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script src="general.js" ></script>
<jsp:include page="/style_sheet_include.jsp" />
<script name = "javascript">
  function rename(){
    var emptyTxtName=validateRename();
    if (emptyTxtName){
      alert("<bean:message key="msg.folderdoc.rename.required"/>" + " " + emptyTxtName.alt);  
      emptyTxtName.focus();
    }else{
    document.forms[0].target = opener.name;
    document.forms[0].submit();
    window.location.replace("blank.html");    
    }
  }
  
  function validateRename(){
    var thisForm=document.forms[0];
    if (typeof thisForm.txtNewName.length != "undefined"){
      for(var i=0;i<thisForm.txtNewName.length;i++){
        if (trim(thisForm.txtNewName[i].value)==""){
          return thisForm.txtNewName[i]
        }
      } 
    }else{
      if (trim(thisForm.txtNewName.value)==""){
        return thisForm.txtNewName
      }
    }
    return false;
  }
  
  function enter(thisField,e){
    var i;
    i=handleEnter(thisField,e);
    if (i==1) {
      return rename();
    }
  }
</script>
</head>
<body style="margin:15px" >
<html:form  action="/folderDocRenameAction" name="folderDocRenameForm" type="dms.web.actionforms.filesystem.FolderDocRenameForm" >
<table id="tabParent" align="center"  width="500px"  border="0" cellpadding="0" cellspacing="0" >
  <tr>
    <td>
      <table width="200px" border="0" cellpadding="0" cellspacing="0" id="tab">
        <tr>
          <td width="5px" class="imgTabLeft"></td>
          <td width="190px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.RenameDocumentFolder" /></div></td>
          <td width="5px" class="imgTabRight"></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table width="500px" border="0" align="center" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2" id="borderClrLvl_2">
  <tr>
    <td>
      <table width="97%" border="0" align="center" cellpadding="0" cellspacing="0" id="innerContainer">
        <tr>
          <td height="10px"></td>
        </tr>
        <tr>
          <td align="center">
            <div style="overflow:auto;width:100%;height:125px" class="bgClrLvl_4 borderClrLvl_2">
              <logic:iterate id="folderDocList" name="folderDocRenameLists" type="dms.web.beans.filesystem.FolderDocList" >
                <bean:define id="name" name="folderDocList" property="name" />
                <bean:define id="description" name="folderDocList" property="description" />
                <bean:define id="id" name="folderDocList" property="id" />
                <table class="bgClrLvl_F" width="100%"  border="0" cellspacing="0" cellpadding="1">
                  <tr class="bgClrLvl_4"><td height="1px" colspan="2"></td></tr>
                  <tr class="bgClrLvl_3">
                    <td width="20%"><div align="right"><bean:message key="lbl.OldName" /></div></td>
                    <td width="80%"><bean:write name="folderDocList" property="name" /></td>
                  </tr>
                  <tr class="bgClrLvl_3">
                    <td><div align="right"><bean:message  key="txt.RenameTo" /></div></td>
                    <td>
                      <html:text property="txtNewName" styleClass="borderClrLvl_2 componentStyle" style="width:350px" value="<%=name%>" tabindex="1" alt="<%=name%>" onkeypress="return enter(this,event);" /> 
                      <html:hidden property="txtId" value="<%=id%>" />
                    </td>
                  </tr>
                  <tr class="bgClrLvl_3">
                    <td><div align="right"><bean:message  key="txt.ItemDesc" /></div></td>
                    <td>
                      <html:text property="txtNewDesc" styleClass="borderClrLvl_2 componentStyle" style="width:350px" value="<%=description%>" tabindex="2" alt="<%=description%>" onkeypress="return enter(this,event);" />
                    </td>
                  </tr>
                  <tr class="bgClrLvl_4"><td height="1px" colspan="2"></td></tr>
                </table>
              </logic:iterate>
            </div>  
          </td>
        </tr>
        <tr>
          <td align="center" height="30px" >
            <div align="right">
              <html:button property="btnOk" onclick="rename()" styleClass="buttons" style="width:70px" tabindex="2" ><bean:message key="btn.Ok" /></html:button>
              <html:button property="btnCancel" styleClass="buttons" onclick="window.close();" style="width:70px" tabindex="3" ><bean:message key="btn.Cancel" /></html:button> 
              <html:button property="btnHelp" styleClass="buttons" style="width:70px" onclick="openWindow('help?topic=folder_doc_rename_html','Help',650,800,0,0,true);" tabindex="5"><bean:message key="btn.Help" /></html:button>
            </div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
  <!-- borderClrLvl_2 table ends above-->
</html:form> 
</body>
</html:html>