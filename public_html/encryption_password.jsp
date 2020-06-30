<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="title.EncryptionPassword" /> </title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<jsp:include page="/style_sheet_include.jsp" />
<script src="general.js"></script>
<script language="JavaScript" type="text/JavaScript">
//This is to submit the request for encrypting
function docEncrypt(thisForm){
    if(thisForm.txtEncryptionPassword.value==null || trim(thisForm.txtEncryptionPassword.value)==""){
      alert("<bean:message key="msg.document.encryptionPassword.required"/>");
      thisForm.txtEncryptionPassword.focus();
    } else {
      document.forms[0].target = opener.name;
      document.forms[0].submit();
      window.location.replace("blank.html");
    }
}

function enter(thisField,e){
  var i;
  i=handleEnter(thisField,e);
 	if (i==1) {
    return docEncrypt(thisField.form);
  }
}

</script>
</head>
<body >
<html:form name="docEncryptForm" action="docEncryptAction"  type="dms.web.actionforms.filesystem.DocEncryptForm" focus="txtEncryptionPassword">
  <logic:iterate id="chkFolderDocId" property="chkFolderDocIds" name="docEncryptForm" >    
     <html:hidden property="chkFolderDocIds" value="<%=chkFolderDocId%>" />
  </logic:iterate>

<table id="tabContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">
<!-- This table contains 2 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->
<tr>
  <td height="7px" colspan="2" ></td>
</tr>      

<tr>
<td >
	<table id="tabParent" width="500px" align="center" border="0" cellpadding="0" cellspacing="0">
		<tr>
    	<td>
			<table id="tab" width="155px" border="0" cellpadding="0" cellspacing="0">
      	<tr>
            <td width="5px" class="imgTabLeft"></td>
            <td width="145px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.encryption.password" /></div></td>
            <td width="5px" class="imgTabRight"></td>   
      	</tr>
    	</table>
		</td>
  		</tr>
	</table>
<table id="borderClrLvl_2" align="center" width="500px" border="0" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2" >
  <tr>
    <td >
      <table width="97%" border="0" cellpadding="0" cellspacing="1" align="center" >
      <tr>
        <td height="10px" colspan="2" ></td>
      </tr>      
      <tr>
        <td height="5px" colspan="2" ></td>
      </tr>      
      <tr>
        <td valign="top">
        <div align="right"><bean:message key="lbl.encryption.password" />:&nbsp;
        </div>
        </td>
        <td> 
        <html:password property="txtEncryptionPassword" style="width:350;left-margin:4px" styleClass="borderClrLvl_2 componentStyle" onkeypress="return enter(this,event);"></html:password>
        <input type="text" name="txtDummy" id="txtDummy" style="display:none">        
        </td>
      </tr>
      <tr>
        <td colspan="2" height="30px" align="right">
            <html:button property="btnOk" onclick="docEncrypt(this.form)" styleClass="buttons" style="width:70px"><bean:message key="btn.Ok" /></html:button>
            <html:button property="btnCancel" styleClass="buttons"  onclick='window.close()' style="width:70px"><bean:message key="btn.Cancel" /></html:button>
            <html:button property="btnHelp" styleClass="buttons" style="width:70px" onclick="openWindow('help?topic=document_encrypt_decrypt_html','Help',650,800,0,0,true);" tabindex="5"><bean:message key="btn.Help" /></html:button>                                        
        </td>
      </tr>
      </table>
    </td>
  </tr>
</table>
<!-- borderClrLvl_2 table ends above-->
</td>
</tr>
</table>
<!-- tabContainer table ends above-->      
</html:form>
</body>
</html>
