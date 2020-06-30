<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%request.setAttribute("topic","user_introduction_html");%>


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

</head>



  <body onLoad="MM_preloadImages('<%=imagepath%>/themes/images/webtop_user_over.gif','themes/images/webtop_group_over.gif','themes/images/webtop_security_over.gif','themes/images/webtop_document_over.gif','themes/images/webtop_theme_over.gif','themes/images/webtop_scheduler_over.gif','themes/images/webtop_mailing_over.gif','themes/images/webtop_workflow_over.gif')">

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

                            <a class="topLink" style="cursor:pointer;cursor:hand" onClick="openWindow('help','Help',650,800,0,0,true);">

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

            <table id="wallPaper" width="700" height="350" border="0" cellpadding="0" cellspacing="0">

              <tr><td><div style="margin-left:12px" ><span class="font15Bold"><bean:message key="lbl.WelcomeMsg" /></span> &nbsp;<span class="font15Bold"><bean:write name="UserInfo" property="userID" />!</span></div></td></tr>  

              <tr>

                <td class="wallPeper">			

                  <table width="400px"  border="0" align="center" cellpadding="0" cellspacing="3" class="iconText border1px">

                    <tr>

                      <td width="20%" height="90px" align="center">

                        <table border="0" cellpading=0 cellspacing="0">

                          <tr>

                            <td align="center">

                              <%if(userInfo.isSystemAdmin() || userInfo.isAdmin()){%>

                                <a class="imgWebTopUser" href="relayAction.do?operation=user_list" onMouseOut="this.className='imgWebTopUser'" onMouseOver="this.className='imgWebTopUserOver'"></a>

                              <%}else{%>                          

                                <div class="imgWebTopUserDisable"></div>                          

                              <%}%>                            </td>
                          </tr>

                          <tr>

                            <td align="center">

                            <%if(userInfo.isSystemAdmin() || userInfo.isAdmin()){%>

                              <a class="topLink" href="relayAction.do?operation=user_list"><bean:message key="mnu.User" /></a>

                            <%}else{%>                     

                             <bean:message key="mnu.User" />

                            <%}%>                            </td>
                          </tr>
                        </table>                      </td>

                      <td width="20%" align="center">

                        <table border="0" cellpading=0 cellspacing="0" >

                          <tr>

                            <td align="center">

                              <%if(userInfo.isSystemAdmin() || userInfo.isAdmin()){%>

                                <a class="imgWebTopGroup" href="relayAction.do?operation=group_list" onMouseOut="this.className='imgWebTopGroup'" onMouseOver="this.className='imgWebTopGroupOver'"></a>

                              <%}else{%>                          

                                <div class="imgWebTopGroupDisable"></div>                          

                              <%}%>                            </td>
                          </tr>

                          <tr>

                            <td align="center">

                            <%if(userInfo.isSystemAdmin() || userInfo.isAdmin()){%>

                              <a class="topLink" href="relayAction.do?operation=group_list"><bean:message key="mnu.Group" /></a>

                            <%}else{%>                     

                             <bean:message key="mnu.Group" />

                            <%}%>                            </td>
                          </tr>
                        </table>                      </td>

                      <td width="20%" align="center">

                        <table border="0" cellpading=0 cellspacing="0" >

                          <tr>

                            <td align="center">

                              <%if(userInfo.isSystemAdmin() || userInfo.isAdmin()){%>

                                <a class="imgWebTopTheme" href="relayAction.do?operation=theme_list" onMouseOut="this.className='imgWebTopTheme'" onMouseOver="this.className='imgWebTopThemeOver'"></a>

                              <%}else{%>                          

                                <div class="imgWebTopThemeDisable"></div>                          

                              <%}%>                            </td>
                          </tr>

                          <tr>

                            <td align="center">

                            <%if(userInfo.isSystemAdmin() || userInfo.isAdmin()){%>

                              <a class="topLink" href="relayAction.do?operation=theme_list"><bean:message key="mnu.Theme" /></a>

                            <%}else{%>                     

                             <bean:message key="mnu.Theme" />

                            <%}%>                            </td>
                          </tr>
                        </table>                      </td>

                      <td width="20%" align="center">

                        <table border="0" cellpading=0 cellspacing="0" >

                          <tr>

                            <td align="center">

                              <%if(((String)application.getAttribute("nonAdminSecurity")).equals("1") || (userInfo.isSystemAdmin() || userInfo.isAdmin())) {%>

                                <a class="imgWebTopSecurity" href="relayAction.do?operation=acl_list" onMouseOut="this.className='imgWebTopSecurity'" onMouseOver="this.className='imgWebTopSecurityOver'"></a>

                              <%}else{%>                          

                                <div class="imgWebTopSecurityDisable"></div>

                              <%}%>                            
                            </td>
                              
                          </tr>

                          <tr>

                            <td align="center">

                            <%if(((String)application.getAttribute("nonAdminSecurity")).equals("1") || (userInfo.isSystemAdmin() || userInfo.isAdmin())) {%>

                              <a class="topLink" href="relayAction.do?operation=acl_list"><bean:message key="mnu.Security" /></a>

                            <%}else{%>                     

                             <bean:message key="mnu.Security" />

                            <%}%>                            
                            </td>
                          </tr>
                        </table>                      
                      </td>
                      <td width="20%" align="center">

                        <table border="0" cellpading=0 cellspacing="0" >

                          <tr>
                            <td align="center">
                              
                              <a class="imgWebTopAdapter" href="adapterWebtopAction.do" onMouseOut="this.className='imgWebTopAdapter'" onMouseOver="this.className='imgWebTopAdapterOver'"></a>
                            </td>
                          </tr>

                          <tr>

                            <td align="center">
                              <a class="topLink" href="adapterWebtopAction.do"><bean:message key="mnu.Adapter" /></a>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>

                    <tr>

                      <td width="20%" height="90px" align="center">
                        <table border="0" cellpading=0 cellspacing="0" >

                          <tr>

                            <td align="right">

                              <a style="margin-left:4px" class="imgWebTopDocument" href="folderDocMenuClickAction.do" onMouseOut="this.className='imgWebTopDocument'" onMouseOver="this.className='imgWebTopDocumentOver'"></a>                            </td>
                          </tr>

                          <tr>

                            <td align="center">

                              <a class="topLink" href="folderDocMenuClickAction.do"><bean:message key="mnu.Document" /></a>                            </td>
                          </tr>
                        </table>                      </td>                      

                      <td width="20%" align="center">

                        <table border="0" cellpading=0 cellspacing="0" >

                          <tr>

                            <td align="center">
                                <a style="margin-left:4px" class="imgWebTopScheduler" href="schedulerListAction.do" onMouseOut="this.className='imgWebTopScheduler'" onMouseOver="this.className='imgWebTopSchedulerOver'"></a>                            </td>
                          </tr>

                          <tr>

                            <td align="center">

                          

                              <a class="topLink" href="schedulerListAction.do"><bean:message key="mnu.Scheduler" /></a>                            </td>
                          </tr>
                        </table>                      </td>

                      <td width="20%" align="center">

                        <table border="0" cellpading=0 cellspacing="0" >

                          <tr>

                            <td align="center">

                              <a class="imgWebTopMailing" href="mailB4Action.do" onMouseOut="this.className='imgWebTopMailing'" onMouseOver="this.className='imgWebTopMailingOver'"></a>                            </td>
                          </tr>

                          <tr>

                            <td align="center">

                              <a class="topLink" href="mailB4Action.do"><bean:message key="mnu.Mail" /></a>                            </td>
                          </tr>
                        </table>                      </td>                      

                      <td width="20%" align="center">

                        <table border="0" cellpading=0 cellspacing="0" >

                          <tr>

                            <td align="center">

                              <a style="margin-left:4px" class="imgWebTopFaxing" href="faxB4Action.do" onMouseOut="this.className='imgWebTopFaxing'" onMouseOver="this.className='imgWebTopFaxingOver'"></a>
							  </td>
                          </tr>

                          <tr>

                            <td align="center">
                              <a class="topLink" href="faxB4Action.do">
							  <bean:message key="mnu.Fax" />
							  </a>
							  </td>
                          </tr>
                        </table>
						</td>   
					  <td width="20%" align="center">

                        <table border="0" cellpading=0 cellspacing="0" >

                          <tr>

                            <td align="center">

                              <a style="margin-left:5px" class="imgWebTopWorkflow" href="myNotificationB4Action.do" onMouseOut="this.className='imgWebTopWorkflow'" onMouseOver="this.className='imgWebTopWorkflowOver'"></a>                            </td>
                          </tr>

                          <tr>

                            <td align="center">

                              <a class="topLink" href="myNotificationB4Action.do"><bean:message key="mnu.WorkFlow" /></a>                            </td>
                          </tr>
                        </table>                      </td>                   
                  </tr>
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