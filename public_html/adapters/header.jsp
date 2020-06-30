<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<% if(session.getAttribute("UserInfo")==null){ %>
  <jsp:forward page="login.jsp" >
       <jsp:param name='sessionExpired' value='true' />
  </jsp:forward>
<% }; %>

</script>
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
      <table  border="0" cellpadding="0" cellspacing="0" id="menuTblParent">
        <tr>
          <td height="20px" width="182px">
            <table border="0" cellspacing="0" cellpadding="0" id="menuTbl">
              <tr>
                <td width="10px" height="20px" class="imgTopMenuCorner" ></td>
                <td width="57px" class="imgTopMenuTile"><div align="center">
                  <html:link href="adapterWebtopAction.do"><bean:message key="mnu.Home" /></html:link></div>
                </td>                
                <td width="57px" class="imgTopMenuTile"><div align="center">
                  <a class="dropMenuover" onclick="openWindow('help?topic=<%=(String)request.getAttribute("topic")%>','Help',650,800,0,0,true);">
                  <bean:message key="mnu.Help" /></a></div>
                </td>
                <td width="58px" class="imgTopMenuTile"><div align="center">
                  <html:link href="logoutAction.do"><bean:message key="mnu.Logout" /></html:link></div>
                </td>
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
            <a href='<bean:message key="lnk.BugReport"  />' target='_blank' class="menu" style="cursor:pointer;cursor:hand" ><b><bean:message key="mnu.ReportBug"  /></b></a>&nbsp;
          </td>
        </tr>    
      </table>
    </td>
  </tr>
</table>