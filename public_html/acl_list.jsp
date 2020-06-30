<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="acls" name="acls" type="java.util.ArrayList" /> 

<bean:define id="userInfo" name="UserInfo" type="dms.web.beans.user.UserInfo"/>

<bean:define id="pageCount" name="AclListForm" property="txtPageCount" />        

<bean:define id="pageNo" name="AclListForm" property="txtPageNo" />        

<bean:define id="imagepath" name="UserPreferences" property="treeIconPath" />        

<%

request.setAttribute("topic","security_introduction_html");

//Variable declaration

boolean firstTableRow;

firstTableRow = true;

%>

<html:html>

<head>

<title><bean:message key="title.AclList" /></title>

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

  navBar.onclick="callPage('page_acl')";



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



<html:javascript formName="aclListForm" dynamicJavascript="true" staticJavascript="false"/>

<script language="Javascript1.1" src="staticJavascript.jsp"></script>

<script language="Javascript1.1" >

function callPage(relayOperation){

  var thisForm=document.forms[0];

  if((relayOperation!="acl_new") && (relayOperation!="search_acl") && (relayOperation!="page_acl")){

    if(checkSelected(thisForm)){

      if(relayOperation=="acl_delete"){

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

    thisForm.submit();

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

    return callPage('search_acl');

  }

}

</script>

</head>

<body style="margin:0" onload="MM_preloadImages('<%=imagepath%>/butt_new_acl_over.gif','<%=imagepath%>/butt_edit_acl_over.gif','<%=imagepath%>/butt_delete_acl_over.gif')">

<html:form name="aclListForm" type="dms.web.actionforms.security.AclListForm" action="/relayAction" focus="txtSearchByAclName" onsubmit="return validateAclListForm(this);" >



<html:hidden property="operation" />

<html:hidden name="AclListForm" property="txtPageNo" /> 

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

  <!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->



  <tr>

  <td align="center">

    <table id="tabParent" width="700px"  border="0" cellpadding="0" cellspacing="0">

        <tr>

          <td width="152px">

            <table id="tab1" width="150px" border="0" cellpadding="0" cellspacing="0">

              <tr>

                <td width="5px" class="imgTabLeft"></td>

                <td width="140px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.AclList" /></div></td>

                <td width="5px" class="imgTabRight"></td>

              </tr>

            </table>

          </td>

          <%if(userInfo.isSystemAdmin() || userInfo.isAdmin()){%>

          <td>

            <table id="tab2" width="150px" border="0" cellpadding="0" cellspacing="0">

              <tr>

                <td width="5px" class="imgTabLeft"></td>

                <td width="140px" class="imgTabTile"><div align="center" class="tabTextLink"><a class="tabTextLink" href="permissionBundleListAdminAction.do"><bean:message key="lbl.PermissionBundle" /></a></div></td>

                <td width="5px" class="imgTabRight"></td>

              </tr>

            </table>

          </td>

          <%}%>          

        </tr>

    </table>

    <table id="borderClrLvl_2" width="700px" border="0" cellpadding="0" cellspacing="0" class="borderClrLvl_2">

      <tr class="imgToolBar bgClrLvl_3" >

        <td width="293px" height="30px">

          <a onclick="return callPage('acl_new');" class="imgNewACL" style="margin-left:5px;" onmouseout="this.className='imgNewACL'" onmouseover="this.className='imgNewACLOver'" title="<bean:message key="tooltips.AclCreate" />" tabindex="1"></a>

          <a onclick="return callPage('acl_edit')" class="imgEditACL" onmouseout="this.className='imgEditACL'" onmouseover="this.className='imgEditACLOver'" title="<bean:message key="tooltips.AclEdit" />" tabindex="2"></a>

          <a onclick="return callPage('acl_delete')" class="imgDeleteACL" onmouseout="this.className='imgDeleteACL'" onmouseover="this.className='imgDeleteACLOver'" title="<bean:message key="tooltips.AclDelete" />" tabindex="3"></a>

        </td>

        <td align="right">

            <a onclick="return callPage('search_acl')" class="imgGo" style="float:right;margin-right:5px" title="<bean:message key="btn.Go" />" tabindex="5" ></a>

            <span style="float:right"><bean:message key="lbl.SearchAcl" /> 

              <html:text name="AclListForm" property="txtSearchByAclName"  styleClass="borderClrLvl_2 componentStyle" style="width:200px" tabindex="4" maxlength="20" onkeypress="return enter(this,event);" titleKey="lbl.YouCanUseWildCard" />

            </span>

        </td>



      </tr>

      <tr>

        <td colspan="3" align="center">

        <div class="imgData bgClrLvl_4" style="overflow:auto; width:100%; height:357px">

          <table class="bgClrLvl_F" id="data" width="100%" border="0" cellpadding="0" cellspacing="1">

            <tr>

              <th width="5%" height="18px" ><bean:message key="tbl.head.Select" /></th>

              <th width="31%"><bean:message key="tbl.head.ACLName" /></th>

              <th width="20%"><bean:message key="tbl.head.Owner" /></th>

              <th width="22%"><bean:message key="tbl.head.SecuringAcl" /></th>

              <th width="10%"><bean:message key="tbl.head.IsShared" /></th>

              <th width="26%"><bean:message key="tbl.head.Modified" /></th>

            </tr>

            <% if(acls.size()>0) {%>

              <logic:iterate name="acls" id="acl">

                <bean:define id="name" name="acl" property="aclName" scope="page" />

                <bean:define id="isDisabled" name="acl" property="isDisabled" scope="page" />                

                <%if (firstTableRow == true){ firstTableRow = false; %>

                    <tr class="bgClrLvl_4">

                <%}else{ firstTableRow = true; %>

                    <tr class="bgClrLvl_3">                  

                <%}%>

                    <td><div align="center">

                      <html:radio property="radSelect" value="<%=name%>" disabled="<%=isDisabled%>" />

                    </div>

                    </td>

                    <td><bean:write name="acl" property="aclName" /></td>

                    <td><bean:write name="acl" property="owner" /></td>

                    <td><bean:write name="acl" property="securingAcl" /></td>

                    <td><bean:write name="acl" property="isShared" /></td>

                    <td><bean:write name="acl" property="modified" /></td>

                  </tr>

                </logic:iterate>

               <%}%> 

            </table>

            <% if(acls.size()==0) {%>

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

            <logic:messagesPresent>

              <bean:message key="errors.header"/>

              <html:messages id="error">

                <font color="red"><bean:write name="error"/></font>

              </html:messages>

            </logic:messagesPresent>

            <html:messages id="msg" message="true">

              <bean:write  name="msg"/>

            </html:messages>

          </td>

          <td width="3px">

            <div style="float:left; width:1px;height:22px;" class="bgClrLvl_2"></div>

            <div style=" float:left; width:1px;height:22px;" class="bgClrLvl_F"></div>

          </td>

          <td width="200px" align="right">

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