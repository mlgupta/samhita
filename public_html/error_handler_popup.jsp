<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page  isErrorPage="true" session="true" %>
<html>
<head>
<title><bean:message key="title.ErrorPage" /></title>
<jsp:include page="/style_sheet_include.jsp" />
</head> 
<body style="margin:0px" >
<table border="0" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4" height="100%" width="100%" >
<tr>
<td  align="center" >
<div style="width:90%">
<table border="0" cellpadding="1" cellspacing="1" >
  <tr>
    <td align="center" width="45px"><div class="imgWarning"></div></td>
    <td class="tabText">
    <html:errors/>
    </td>
  </tr>
</table>
</div>
</td>
</tr>
</table>
</body>
</html>
