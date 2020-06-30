<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script name="javascript">
//This is very important statement many functionality on folder_doc_list.jsp depends on this
    window.name="dms"
</script>
<html>
<head>
<title>Login</title>
<style type="text/css">
<!--
.font10Bold 
{
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 10px;
	font-weight: bold;
}
.font13Bold 
{
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 13px;
	font-weight: bold;
}
.font9 
{
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 9px;
	font-weight: normal;
}
.buttons
{
    BORDER-RIGHT: #666666 1px solid;
    BORDER-TOP: #666666 1px solid;
    BORDER-BOTTOM: #666666 1px solid;
    BORDER-LEFT: #666666 1px solid;
    FONT-WEIGHT: normal;
    FONT-SIZE: 10px;
    BACKGROUND-IMAGE:  url("themes/images/butt_tile.gif");
    BACKGROUND-REPEAT: repeat-x;
}
.componentStyle 
{
	font-size: 13px;
}
-->
</style>
<html:javascript formName="loginForm" dynamicJavascript="true" staticJavascript="false"/>
<script language="Javascript1.1" src="staticJavascript.jsp"></script>
</head>
<body>
<html:form action="/loginAction" name="loginForm" type="dms.web.actionforms.loginout.LoginForm" focus="userID" 
  onsubmit="return validateLoginForm(this);">
<table>
  <tr>
    <td>
    <script language="JavaScript">
      <logic:messagesPresent>
        <html:messages id="actionMessage" message="true">
            alert("<bean:write  name="actionMessage"/>")
        </html:messages>
          
        <html:messages id="actionError">
            alert("<bean:write  name="actionError"/>")
        </html:messages>
      </logic:messagesPresent>
    
        <logic:present parameter="sessionExpired" >
              alert("<bean:message key="session.expired" />")
        </logic:present>
    </script>
    </td>
  </tr>
</table>
<table height="550" width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td valign="middle">
<table width="700" border="0" align="center" cellpadding="0" cellspacing="0" style="border:1px solid #949494;">
  <tr>
    <td height="66px">
      <table width="100%" height="66"  border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td height="13px" colspan="3"></td>
        </tr>
        <tr>
          <td width="15px" height="53px"></td>
          <td width="182px">
            <a href="http://www.dbsentry.com" target="_blank">
              <img src="themes/images/logo_dbs_corp.gif" width="182" height="53" border="0" style="border:0px solid #949494;">
            </a>
          </td>
          <td width="503px" align="right" valign="top">
            <img src="themes/images/logo_PoweredbyOracle.gif" width="100" height="35" style="border:0px solid #949494; margin-right:15px; margin-top:5px">
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td height="29px">
      <table width="700px" height="29"  border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="215px" height="29px" background="themes/images/menubar_tile_left.gif"></td>
          <td width="31px"><img src="themes/images/menubar_motif.gif" width="31" height="29"></td>
          <td width="454px" background="themes/images/menubar_tile_right.gif"></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td height="255">
    <table width="670" height="225px"  border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td height="100px" colspan="2" valign="top" background="themes/images/keyboard_1.jpg">
          <table width="670px" border="0" cellspacing="0" cellpadding="1">
            <tr>
              <td width="205" class="font13Bold">
                <a href="http://www.dbsentry.com/solutions.htm" target="_blank">
                  <img src="themes/images/logo_samhita.gif" width="182" height="53" border="0" style="margin-left:10px; margin-top:10px; BORDER: 1px solid #ACACAC">
                </a> 
              </td>
              <td width="461" class="font13Bold" valign="top">
                <div style="margin-top:8px"><bean:message key="msg.login.slogan" /></div>
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td width="335" height="110px"><img src="themes/images/keyboard_2_1.jpg" width="335" height="110"></td>
        <td width="335" background="themes/images/keyboard_2_2.jpg">
          <table width="335" border="0" cellspacing="3" cellpadding="0">
            <tr>
              <td colspan="3" height="5px"></td>
            </tr>
            <tr>
              <td width="161px"><div align="right"><span class="font10Bold"><bean:message key="txt.UserID" /></span></div></td>
              <td width="157px">
                <html:text property="userID" style="width:145px; border:1px solid #949494" styleClass="componentStyle" tabindex="1"/>
              </td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>
                <div align="right"><span class="font10Bold"><bean:message key="txt.Password" /></span></div>
              </td>
              <td>
                <html:password property="userPassword" style="width:145px; border:1px solid #949494" tabindex="2"/>
              </td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td>
                 <html:submit property="btnOk" styleClass="buttons" style="width:70px" tabindex="3"  ><bean:message key="btn.Ok" /></html:submit>
                 <html:reset property="btnReset" styleClass="buttons" style="width:70px" tabindex="4"><bean:message key="btn.Reset" /></html:reset>            
              </td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td colspan="3" height="5px"></td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td height="15px" class="font9"><bean:message key="msg.login.Recomended" /></td>
        <td background="themes/images/keyboard_3_2.jpg"></td>
      </tr>
    </table>
    </td>
  </tr>
</table>
</td>
</tr>
</table>
</html:form>
</body>
</html>