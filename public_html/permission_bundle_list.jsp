<%@ page import="dms.web.beans.user.UserPreferences" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="permissionbundles" name="permissionbundles" type="java.util.ArrayList" /> 

<bean:define id="pageCount" name="PermissionBundleListForm" property="txtPageCount" />         

<bean:define id="pageNo" name="PermissionBundleListForm" property="txtPageNo" />

<bean:define id="imagepath" name="UserPreferences" property="treeIconPath" />

<%request.setAttribute("topic","permission_bundle_search_html");%>

<%

//Variable declaration

boolean firstTableRow;

firstTableRow = true;

%>

<html:html>

<head>

<title><bean:message key="title.PermissionBundleList" /></title>

<script src="general.js" ></script>

<script src="navigationbar.js" ></script>

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

<script>

  var navBar=new NavigationBar("navBar");

  navBar.setPageNumber(<%=pageNo%>);

  navBar.setPageCount(<%=pageCount%>);

  navBar.onclick="callPage('page_permission_bundle')";



  navBar.msgPageNotExist="<bean:message key="page.pageNumberNotExists" />";

	navBar.msgNumberOnly="<bean:message key="page.enterNumbersOnly"/>";    

	navBar.msgEnterPageNo="<bean:message key="page.enterPageNo"/>";

	navBar.msgOnFirst="<bean:message key="page.onFirst"/>";

	navBar.msgOnLast="<bean:message key="page.onLast"/>";



	navBar.lblPage="<bean:message key="lbl.Page"/>";

	navBar.lblOf="<bean:message key="lbl.Of"/>";



	navBar.tooltipPageNo="<bean:message key="tooltips.PageNo"/>";

	navBar.tooltipFirst="<bean:message key="tooltips.First"/>";

	navBar.tooltipNext="<bean:message key="tooltips.Next"/>";

	navBar.tooltipPrev="<bean:message key="tooltips.Previous"/>";

	navBar.tooltipLast="<bean:message key="tooltips.Last"/>";

	navBar.tooltipGo="<bean:message key="btn.Go"/>";

</script>

<html:javascript formName="permissionBundleListForm" dynamicJavascript="true" staticJavascript="false"/>

<script language="Javascript1.1" src="staticJavascript.jsp"></script>

<script language="Javascript1.1" >

function callPage(relayOperation){

  var thisForm=document.forms[0];

  if((relayOperation!="permission_bundle_new") && (relayOperation!="search_permission_bundle") && (relayOperation!="page_permission_bundle")){

    if(checkSelected(thisForm)){

      if(relayOperation=="permission_bundle_delete"){

        if (confirm("<bean:message key="msg.delete.confirm" />")){

          thisForm.operation.value=relayOperation;

          thisForm.submit() ;

        }

      }else{

        thisForm.operation.value=relayOperation;

        thisForm.submit() ;

      }

    }

  }else{

    thisForm.operation.value=relayOperation;

    thisForm.txtPageNo.value=navBar.getPageNumber();

    thisForm.submit() ;

  }

}

function checkSelected(thisForm){

  if (typeof thisForm.radSelect!="undefined"){

    if (typeof thisForm.radSelect.length !="undefined"){

      for(index = 0 ; index < thisForm.radSelect.length ;index++){  

        if(thisForm.radSelect[index].checked){   

          return true;

        }

      }

    }else{

      if(thisForm.radSelect.checked){   

        return true;

      }

    }

  }else{

    alert('<bean:message key="errors.radSelect.noitem"/>');         

    return false;

  }      

  alert('<bean:message key="errors.radSelect.required"/>');         

  return false;

}



function enter(thisField,e){

  var i;

  i=handleEnter(thisField,e);

 	if (i==1) {

    return callPage('search_permission_bundle');

  }

}

</script>

</head>

<body style="margin:0" onload="MM_preloadImages('<%=imagepath%>/butt_new_permission_over.gif','<%=imagepath%>/butt_edit_permission_over.gif','<%=imagepath%>/butt_delete_permission_over.gif')">

<html:form name="permissionBundleListForm" type="dms.web.actionforms.security.PermissionBundleListForm" action="/relayAction"  onsubmit="return validatePermissionBundleListForm(this);" focus="txtSearchByPermissionBundleName" >

<!-- This page contains 3 outermost tables, named 'headerIncluder', 'errorContainer' and 'tabContainer' -->

<table width="100%"  border="0" cellspacing="0" cellpadding="0" id="headerIncluder">

  <tr>

    <td height="95px">

      <!--Header Starts-->

      <jsp:include page="/header.jsp" />

      <!--Header Ends-->

    </td>

  </tr>

</table>

<br>

<table id="tabContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">

         <html:hidden property="operation" />

         <html:hidden name="PermissionBundleListForm" property="txtPageNo" /> 

<!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->

<tr>

<td align="center">

	<table id="tabParent" width="700px"  border="0" cellpadding="0" cellspacing="0">

		<tr>

    	<td width="152px">

          <table id="tab1" width="150px" border="0" cellpadding="0" cellspacing="0">

              <tr>

                <td width="5px" class="imgTabLeft"></td>

                <td width="140px" class="imgTabTile"><div align="center" class="tabTextLink"><a class="tabTextLink" href="aclListAdminAction.do"><bean:message key="lbl.AclList" /></a></div></td>

                <td width="5px" class="imgTabRight"></td>

              </tr>

            </table>

          </td>

          <td>

            <table id="tab2" width="150px" border="0" cellpadding="0" cellspacing="0">

              <tr>

                <td width="5px" class="imgTabLeft"></td>

                <td width="140px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.PermissionBundle" /></div></td>

                <td width="5px" class="imgTabRight"></td>

              </tr>

            </table>

      </td>

  	</tr>

	</table>

	<table id="borderClrLvl_2" width="700" border="0" cellpadding="0" cellspacing="0" class="borderClrLvl_2">

		<tr class="imgToolBar bgClrLvl_3" >

        <td width="293px" height="30px">

          <a onclick="return callPage('permission_bundle_new');" class="imgNewPermission" style="margin-left:5px;" onmouseout="this.className='imgNewPermission'" onmouseover="this.className='imgNewPermissionOver'" title="<bean:message key="tooltips.PermissionBundleCreate" />" tabindex="1"></a>

          <a onclick="return callPage('permission_bundle_edit')" class="imgEditPermission" onmouseout="this.className='imgEditPermission'" onmouseover="this.className='imgEditPermissionOver'" title="<bean:message key="tooltips.PermissionBundleEdit" />" tabindex="2"></a>

          <a onclick="return callPage('permission_bundle_delete')" class="imgDeletePermission" onmouseout="this.className='imgDeletePermission'" onmouseover="this.className='imgDeletePermissionOver'" title="<bean:message key="tooltips.PermissionBundleDelete" />" tabindex="3"></a> 

        </td>

		<td align="right">

        <a onclick="return callPage('search_permission_bundle')" style="float:right;margin-right:5px" class="imgGo" title="<bean:message key="btn.Go" />" tabindex="5"></a>

        <span style="float:right"><bean:message key="lbl.SearchPermission" />

          <html:text name="PermissionBundleListForm" property="txtSearchByPermissionBundleName" styleClass="borderClrLvl_2 componentStyle" style="width:200px" tabindex="4" maxlength="20" onkeypress="return enter(this,event);" titleKey="lbl.YouCanUseWildCard" />

        </span>

    </td>

		</tr>

		<tr>

      <td colspan="2" align="center">

      <div class="imgData bgClrLvl_4" style="overflow:auto; width:100%; height:357px">

        <table class="bgClrLvl_F" width="100%" border="0" cellpadding="0" cellspacing="1" id="data">

            <tr>

              <th width="5%" height="18px"><bean:message key="tbl.head.Select" /></th>

              <th width="43%"><bean:message key="tbl.head.PermissionName" /></th>

              <th width="42%"><bean:message key="tbl.head.Description" /></th>

              </tr>

              <% if(permissionbundles.size()>0) {%>

                <logic:iterate name="permissionbundles" id="permissionbundle">

                <bean:define id="name" name="permissionbundle" property="permissionName" scope="page" />

                    <%if (firstTableRow == true){ firstTableRow = false; %>

                        <tr class="bgClrLvl_4">

                    <%}else{ firstTableRow = true; %>

                        <tr class="bgClrLvl_3">                  

                    <%}%>

                    <td>

                    <div align="center">

                      <html:radio property="radSelect" value="<%=name%>" />

                    </div></td>

                    <td><bean:write name="permissionbundle" property="permissionName"  /></td>

                    <td><bean:write name="permissionbundle" property="permissionDescription" /></td>

                  </tr>

                </logic:iterate>

               <%}%> 

            </table>

            <% if(permissionbundles.size()==0) {%>

              <div style="position:relative; top:165px; text-align:center;" class="tabText">

                <bean:message key="info.no_item_found.no_item_found" />

              </div> 

            <%}%>

      </div>      

    </td>

		</tr>

</table>

<!-- borderClrLvl_2 table ends above-->

</td>

</tr>

	<tr><td height="2px"></td></tr>

	<tr>

      <td align="center">

      <table class="imgStatusBar borderClrLvl_2 bgClrLvl_4 " width="700px" border="0" cellpadding="0" cellspacing="0" id="statusBar">

        <tr>

          <td width="30px"><div class="imgStatusMsg"></div></td>

          <td width="467px">

            <logic:messagesPresent >

              <bean:message key="errors.header"/>

                <html:messages id="error">

                  <font color="red"><bean:write name="error"/></font>

                </html:messages>

            </logic:messagesPresent>

            <html:messages id="msg" message="true">

              <bean:write name="msg"/>

            </html:messages>

          </td>

          <td width="3px">

            <div style="float:left; width:1px;height:22px;" class="bgClrLvl_2"></div>

            <div style="float:left; width:1px;height:22px;" class="bgClrLvl_F"></div>

          </td>

          <td  width="200px" align="right">

             <script>navBar.render();</script>

          </td>

          </tr>

      </table>

      <!-- statusBar table ends above-->

      </td>

  </tr>

</table>

</html:form> 

<!-- tabContainer table ends above-->

</body>

</html:html>