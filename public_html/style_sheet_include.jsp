<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<bean:define id="userPreferences" name="UserPreferences" type="dms.web.beans.user.UserPreferences"/>
<link href="themes/general.css" rel="stylesheet" type="text/css">
<%
out.println(userPreferences.getStylePlaceHolder());
%>