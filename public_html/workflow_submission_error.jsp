<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
<head>
<title>
 <bean:message key="title.Problem.in.workFlowSubmission"/>
</title>
<script src="general.js"></script>
<jsp:include page="style_sheet_include.jsp" />
<script>
    
  function closewindow(){
    window.location.replace("blank.html");
  }
</script>
</head>
<body>
<table id="tabContainer" width="100%"  height="92%" border="0" cellspacing="0" cellpadding="0">
<!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->
<br>
<tr>
<td align="center">
	<table id="tabParent" width="400px"  border="0" cellpadding="0" cellspacing="0">
		<tr>
    	<td>
			<table id="tab" width="150px" border="0" cellpadding="0" cellspacing="0">
      		<tr>
            <td width="5px" class="imgTabLeft"></td>
            <td width="140px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.Problem.in.workflowsubmission" /></div></td>
            <td width="5px" class="imgTabRight"></td>
      		</tr>
    	</table>
		  </td>
  	</tr>
	</table>
	<table width="400px" border="0" cellpadding="0" cellspacing="0" class="imgData borderClrLvl_2 bgClrLvl_4" id="borderClrLvl_2">
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
</body>
</html>
