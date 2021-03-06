<%@ page import="dms.web.actions.filesystem.*" %>
<%@ page import="dms.web.actionforms.filesystem.*" %>
<%@ page import="org.apache.struts.action.*" %>
<%@ page import="dms.web.beans.filesystem.*" %>
<%@ page import="dms.web.beans.exception.* "%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<% ActionErrors actionErrors =(ActionErrors)request.getAttribute("actionErrors"); 
    if(actionErrors!=null){
      
    }
%>
<html:html>
<head>
<title><bean:message key="title.UploadDocument" /></title>
<jsp:include page="/style_sheet_include.jsp" />
<script src="general.js"></script>
<script>
    
  function closewindow(){
    window.close();
  }

  
</script>
</head>
<body>
<html:form action="folderDocListAction.do">
<table id="tabContainer" align="center" height="100%" width="100%"  border="0" cellspacing="0" cellpadding="0">
<!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->
<tr>
  <td >
    <table id="tabParent" align="center" width="450px"  border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td>
          <table id="tab" width="150px" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="5px" class="imgTabLeft"></td>
              <td width="140px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.UploadDocuments" /></div></td>
              <td width="5px" class="imgTabRight"></td>            
            </tr>
          </table>
        </td>
  		</tr>
    </table>
<table id="borderClrLvl_2" align="center" width="450px"  border="0" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2"  >
  <tr>
    <td >
        <table width="100%"  align="left" border="0"  cellpadding="0" cellspacing="2" align="center">
          <tr><td height="25px" ></td></tr>
          <tr>
              <td align="center" class="tabText" style="color:ff0000" >
                  <html:messages id="actionError">
                    <bean:write  name="actionError"/>
                  </html:messages>             
              </td>
          </tr>
          <tr><td height="15px" ></td></tr>
          <tr>
            <td align="center">
            <html:button property="btnOK" onclick="closewindow()" styleClass="buttons" style="width:70px"><bean:message key="btn.Ok" /></html:button>
            </td>
          </tr>
          <tr><td height="25px" ></td></tr>
      </table>
    </td>
  </tr>
</table>
</td>
</tr>
</table>
</html:form>
</body>
</html:html>