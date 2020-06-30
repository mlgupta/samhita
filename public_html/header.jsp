<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<% if(session.getAttribute("UserInfo")==null){ %>
  <jsp:forward page="login.jsp" >
       <jsp:param name='sessionExpired' value='true' />
  </jsp:forward>
<% }; %>

<bean:define id="userInfo" name="UserInfo" type="dms.web.beans.user.UserInfo"/>
<jsp:include page="/style_sheet_include.jsp" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="7px"></td>
    <td width="182px">
      <table width="182px"  border="0" cellpadding="0" cellspacing="0">
        <tr > 
          <td height="11px"></td>
        </tr>  
        <tr>
          <td height="53px"><html:img src="themes/images/logo_samhita.gif" width="182" height="53" styleClass="borderClrLvl_2"  /></td>
        </tr>  
      </table>
    </td>
    <td>&nbsp;</td>
    <td width="182px">
      <table width="182px"  border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td height="20px">
            <table width="226px" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="10px" height="20px" class="imgTopMenuCorner" ></td>
                <td width="44px" class="imgTopMenuTile"><div align="center"><html:link href="webtop.jsp"><bean:message key="mnu.Home" /></html:link></div></td>                
                <td width="74px" class="imgTopMenuTile"><div align="center"><html:link href="userPreferenceProfileB4Action.do"><bean:message key="mnu.Preferences" /></html:link></div></td>
                <td width="44px" class="imgTopMenuTile"><div align="center"><a class="dropMenuover" onclick="openWindow('help?topic=<%=(String)request.getAttribute("topic")%>','Help',650,800,0,0,true);"><bean:message key="mnu.Help" /></a></div></td>
                <td width="54px" class="imgTopMenuTile"><div align="center"><html:link href="logoutAction.do"><bean:message key="mnu.Logout" /></html:link></div></td>
              </tr>  
            </table>
          </td>
        </tr>
        <tr> 
          <td height="46px" align="right">
            <span class="tabText"><bean:message key="lbl.WelcomeMsg" /></span> &nbsp;<span class="tabText"><bean:write name="UserInfo" property="userID" /></span>&nbsp;
          </td>
        </tr>
      </table>
    </td>
  </tr>  
</table>
<table width="100%"  border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="215px"class="imgMenuBar29">&nbsp;</td>
    <td width="31px" class="imgMenuBarMotif"></td>
    <td>
      <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td height="8px" class="bgClrLvl_2"></td>
        </tr>
        <tr>
          <td  class="imgMenuBar21" height="21px" align="right" >
            <%if(userInfo.isSystemAdmin() || userInfo.isAdmin()){%>
            <html:link href="relayAction.do?operation=user_list" styleClass="menu"><bean:message key="mnu.User" /></html:link>&nbsp;|
            <html:link href="relayAction.do?operation=group_list" styleClass="menu"><bean:message key="mnu.Group" /></html:link>&nbsp;|
            <html:link href="relayAction.do?operation=theme_list" styleClass="menu"><bean:message key="mnu.Theme" /></html:link>&nbsp;|
            <%}%>
            <%if(((String)application.getAttribute("nonAdminSecurity")).equals("1") || (userInfo.isSystemAdmin() || userInfo.isAdmin())) {%>
            <html:link href="relayAction.do?operation=acl_list" styleClass="menu"><bean:message key="mnu.Security" /></html:link>&nbsp;|
            <%}%>
            <html:link href="folderDocMenuClickAction.do" styleClass="menu"><bean:message key="mnu.Document" /></html:link>&nbsp;|
            <html:link href="schedulerListAction.do" styleClass="menu" ><bean:message key="mnu.Scheduler" /></html:link>&nbsp;|
            <html:link href="mailB4Action.do" styleClass="menu" ><bean:message key="mnu.Mail" /></html:link>&nbsp;|
            <html:link href="faxB4Action.do" styleClass="menu" ><bean:message key="mnu.Fax" /></html:link>&nbsp;|            
            <html:link href="myNotificationB4Action.do" styleClass="menu"><bean:message key="mnu.WorkFlow" /></html:link>&nbsp;|
            <!--<html:link href="http://www.dbsentry.com/bugs" styleClass="menu"><bean:message key="mnu.ReportBug"  /></html:link>&nbsp;-->
            <a href='<bean:message key="lnk.BugReport"  />' target='_blank' class="menu" style="cursor:pointer;cursor:hand" ><b><bean:message key="mnu.ReportBug"  /></b></a>&nbsp;
          </td>
        </tr>    
      </table>
    </td>
  </tr>
</table>