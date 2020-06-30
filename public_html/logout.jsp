<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><bean:message  key="msg.logout.title" /></title>
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
-->
</style>
</head>
<body>
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
          <td colspan="2" height="100px" valign="top" background="themes/images/keyboard_1.jpg">
            <table width="335" border="0" cellspacing="0" cellpadding="10">
              <tr>
                <td class="font13Bold" ><bean:message  key="msg.logout.successfully" /></td>
              </tr>
            </table>
          </td>
          <!--<td width="335"><img src="themes/images/keyboard_1_2.jpg" width="335" height="100"></td>-->
        </tr>
        <tr>
          <td height="110px"><img src="themes/images/keyboard_2_1.jpg" width="335" height="110"></td>
          <td valign="bottom" background="themes/images/keyboard_2_2.jpg">
            <table  width="335" border="0" cellpadding="10">
              <tr>
                <td class="font13Bold" valign="bottom" align="right">
                <bean:message  key="msg.logout.login" />
                <html:link href="login.jsp"> <bean:message  key="msg.logout.clickhere" /></html:link>              
                </td>
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
</body>
</html>
