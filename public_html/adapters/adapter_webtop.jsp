<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="adapters.beans.AdaptersConstants" %>
<%@ page import="adapters.beans.XMLBean" %>
<%@ page import="java.util.ArrayList" %>
<bean:define id="adapters" name="adapters" type="java.util.ArrayList" /> 

<%request.setAttribute("topic","adapters_introduction_html");%>

<% if(session.getAttribute("UserInfo")==null){ %>
  <jsp:forward page="login.jsp" >
       <jsp:param name='sessionExpired' value='true' />
  </jsp:forward>
<% }; %>

<bean:define id="userInfo" name="UserInfo" type="dms.web.beans.user.UserInfo"/>
<bean:define id="imagepath" name="UserPreferences" property="treeIconPath" />
<html:html>
<head>
<title><bean:message key="title.WebTop" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<jsp:include page="/style_sheet_include.jsp" />
<script src="general.js" ></script>
<style type="text/css">

<!--

body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

.border1px {
	border: 1px solid #666666;
}

.wallPeper {
	background-image: url(themes/images/wallpaper.gif);
	background-repeat: no-repeat;
	background-position: right;
}

.iconText {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	font-weight: normal;
}

A.topLink:link {
	FONT-WEIGHT: normal;FONT-SIZE: 11px;TEXT-DECORATION: none;font-family: Verdana, Arial, Helvetica, sans-serif;color: #000000;
}

A.topLink:visited {
		FONT-WEIGHT: normal;FONT-SIZE: 11px;TEXT-DECORATION: none;font-family: Verdana, Arial, Helvetica, sans-serif;color: #000000;
}

A.topLink:hover {
		FONT-WEIGHT: normal;FONT-SIZE: 11px;TEXT-DECORATION: underline;font-family: Verdana, Arial, Helvetica, sans-serif;color: #000000;
}

A.topLink:active {
		FONT-WEIGHT: normal;FONT-SIZE: 11px;TEXT-DECORATION: none;font-family: Verdana, Arial, Helvetica, sans-serif;color: #000000;
}

.font13Bold {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 13px;
	font-weight: bold;
}

.font15Bold {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 15px;
	font-weight: bold;
}

.textCopyRight {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 9px;
	font-weight: normal;
	color: #666666;
}

-->
</style>
<script language="JavaScript" type="text/JavaScript">
<!--

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
  var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
  if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
  d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
  if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
<script language="JavaScript" type="text/JavaScript">
</script>

</head>
  <body onLoad="MM_preloadImages('<%=imagepath%>/themes/images/webtop_erp_generic.gif','themes/images/webtop_erp_generic_over.gif')">
  <table width="100%" height="100%"  border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td>
        <table width="700" height="450" class="border1px" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td height="75" bgcolor="#CCCCCC"><table width="700" height="75" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="208px" align="center">
                  <html:img src="themes/images/logo_samhita.gif" width="182" height="53" styleClass="border1px" />
                </td>
                <td width="492" valign="bottom">
                  <div align="right">
                    <table width="100%" height="75"  border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td>
                          <div align="right">
                            <a class="topLink" href="webtop.jsp">
                              <bean:message key="mnu.Home" />
                            </a>&nbsp; | &nbsp;
                            <a class="topLink" style="cursor:pointer;cursor:hand" onClick="openWindow('help?topic=<%=(String)request.getAttribute("topic")%>','Help',650,800,0,0,true);">
                            <bean:message key="mnu.Help" />
                            </a>&nbsp; | &nbsp;
                            <a class="topLink" href="userPreferenceProfileB4Action.do">
                              <bean:message key="mnu.Preferences" />
                            </a>&nbsp; | &nbsp;
                            <a class="topLink" href="logoutAction.do">
                              <bean:message key="mnu.Logout" />
                            </a>&nbsp; 
                          </div>
                        </td>
                      </tr>
                      <tr>
                        <td >&nbsp;</td>
                      </tr>
                      <tr>
                        <td>
                          <div align="right">
                            <span class="font13Bold">
                              <bean:message key="lbl.CompanyName" />
                            </span> &nbsp;
                          </div>
                        </td>
                      </tr>
                    </table>
                  </div>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td height="350" background="themes/images/webtop_grad_350px.gif">
            <table id="wallPaper" width="700px" height="350px" border="0" cellpadding="0" cellspacing="0">
              <tr><td height="30px"><div style="margin-left:12px" ><span class="font15Bold"><bean:message key="lbl.WelcomeMsg" /></span> &nbsp;<span class="font15Bold"><bean:write name="UserInfo" property="userID" />!</span></div></td></tr>  
              <tr>
                <td class="wallPeper" align="center" >			
                  <table width="460px"  border="0" align="center" cellpadding="0" cellspacing="0" class="iconText border1px">
                  <% int count=0;%>
                  <logic:iterate id="adapter" name="adapters" indexId="gIndex">
                    <%
                      count=gIndex.intValue()+1 ;
                      if(((gIndex.intValue())%4)==0){
                    %>
                      <tr>
                    <%
                      }
                    %>
                      <td width="115px" height="90px" align="center" valign="top">
                        <table align="center" width="100%" border="0" cellpadding="0" cellspacing="0" style="margin-top:15px;" >
                          <tr>
                            <td align="center">
                              <table>
                                <tr>
                                  <td>
                                    <bean:define id="actionName" name="adapter" property="adapterStatusListActionName" />
                                    <a class="imgWebTopERP" href="<%=actionName%>" title="<bean:write name="adapter" property="adapterName" />" onMouseOut="this.className='imgWebTopERP'" onMouseOver="this.className='imgWebTopERPOver'"></a>
                                  </td>
                                </tr>
                              </table>
                            </td>
                          </tr>
                          <tr>
                            <td align="center"><bean:write name="adapter" property="adapterName" /></td>
                          </tr>
                        </table>
                      </td>
                      <%if(((gIndex.intValue())%4)==3){%>
                      </tr>
                      <tr><td height="4px" colspan="4" valign="top"></td></tr>
                      <%}%>
                      </logic:iterate>
                      <%
                        if(count==1){
                      %>
                      <td width="25%" ></td>
                      <td width="25%" ></td>
                      <td width="25%" ></td>
                      <%
                        }
                      %>
                      <%
                        if(count==2){
                      %>
                        <td width="25%" ></td>
                        <td width="25%" ></td>
                      <%
                        }
                      %>
                      <%
                        if(count==3){
                      %>
                        <td width="25%"></td>
                      <%
                        }
                      %>
                      <%if (((count)%4)!=0){%>
                        </tr>
                        <tr><td height="4px" colspan="4" valign="top"></td></tr>
                      <%}%>
            
                    <% if(adapters.size() == 0) {%>
                      <tr>
                        <td colspan="4" align="center">No Adapters Enabled</td>
                      </tr>
                    <%}%>
                  </table>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td class="textCopyRight" height="25" bgcolor="#CCCCCC"><div style="margin:5px;" align="right"><bean:message key="lbl.CopyRight" /></div></td>
        </tr>
      </table>
    </td>
  </tr>
  </table>
  </body>
</html:html>
