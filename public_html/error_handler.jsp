<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page  isErrorPage="true" session="true" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><bean:message key="title.ErrorPage" /></title>
</head> 
<body style="margin:0px">
<table id="headerIncluder" width="100%"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="96px">
		<!--Header Starts-->
		<jsp:include page="/header.jsp" />
		<!--Header Ends-->
	</td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4" height="498px" width="100%" >
<tr>
<td  align="center" >
<div style="width:90%">
<table border="0" cellpadding="1" cellspacing="1" >
  <tr>
    <td align="center" width="45px"><div class="imgWarning"></div></td>
    <td class="tabText"> 
    <logic:messagesPresent>
            <html:messages id="actionErrors">
                 <bean:write  name="actionErrors"/>
            </html:messages>
     </logic:messagesPresent>
   </td>
  </tr>
</table>
</div>
</td>
</tr>
</table>

</body>
</html>
