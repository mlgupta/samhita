<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%request.setAttribute("topic","group_edit_html");%>

<html:html>

<head>



<title><bean:message key="title.GroupEdit" /></title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<script src="general.js"></script>

<html:javascript formName="groupNewEditForm" dynamicJavascript="true" staticJavascript="false" />

<script language="Javascript1.1" src="staticJavascript.jsp"></script>

<script language="Javascript1.1" >



function callPage(thisForm){ 

  for(index = 0 ; index < thisForm.lstGroupOrUserList.length ;index++){     

    thisForm.lstGroupOrUserList[index].selected=true;    

  }

  if(validateGroupNewEditForm(thisForm)){    

    thisForm.submit() ;

  }

}



function openList(thisForm){

  if(thisForm.radUserGroup[0].checked){

    openWindow('relayAction.do?operation=page_user_select&control=lstGroupOrUserList','groupEditUser',500,450,0,0,true);

  }else if(document.forms[0].radUserGroup[1].checked){

    openWindow('relayAction.do?operation=page_group_select&control=lstGroupOrUserList','groupEditGroup',515,450,0,0,true);

  }

}



function removeFromList(thisForm){

  var index=0;

  var length=thisForm.lstGroupOrUserList.length;

  while(index <length){     

    if(thisForm.lstGroupOrUserList[index].selected){

      thisForm.lstGroupOrUserList.remove(index);

      length=thisForm.lstGroupOrUserList.length;   

    }else{

      index++;

    }

  }

}

</script>

</head>

<body style="margin:0">

<html:form action="/groupEditAdminAction" focus="txaDescription"

         onsubmit="return validateGroupNewEditForm(this);" >

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

<!--Content Starts-->

<table id="tabContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">

<!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->

<tr>

<td align="center">

<table id="tabParent" width="700"px  border="0" cellpadding="0" cellspacing="0">

  <tr>

    <td>

      <table  id="tab" width="150px" border="0" cellpadding="0" cellspacing="0">

        <tr>

          <td width="5px" class="imgTabLeft"></td>

          <td width="140px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.EditGroup" /></div></td>

          <td width="5px" class="imgTabRight"></td>

        </tr>

      </table>

    </td>

  </tr>

</table>



<table width="700px" border="0" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2" id="borderClrLvl_2" >

      <tr>

        <td height="20px" colspan="2" align="center">&nbsp;</td>

      </tr>

      <tr>

        <td colspan="2" align="center">

            <fieldset style="width:670px;">

            <table width="670px" border="0" id="general">

            <tr>

              <td width="140px"><div align="right"><bean:message key="txt.GroupName" /></div></td>



              <td colspan="3"><html:text name="groupNewEditForm" property="txtGroupName" styleClass="borderClrLvl_2 componentStyle componentStyleDisable" style="width:230px" readonly="true"  /></td>

              <td width="240px" rowspan="3" >

                <fieldset style="width:215px;">

                    <legend align="left" class="tabText"><bean:message key="lbl.AddUsersGroups" />:</legend>

                    <table width="210px" border="0" cellpadding="1">

                      <tr height="20px">

                        <td width="136px"><html:radio property="radUserGroup" value="0" onclick="this.form.btnAddFromList.value=this.alt; " altKey="btn.AddFromUserList" tabindex="5" /><bean:message key="rad.User" /></td>

                        <td width="65px"><html:radio property="radUserGroup" value="1" onclick="this.form.btnAddFromList.value=this.alt; " altKey="btn.AddFromGroupList" tabindex="6" /><bean:message key="rad.Group" /></td>

                      </tr>

                      <tr height="25px">

                        <td colspan="2">

                          <html:button property="btnAddFromList" styleClass="buttons" style="width:205px" onclick="openList(this.form)" tabindex="7" >

                            <bean:message key="btn.AddFromUserList" />

                          </html:button>

                        </td>

                      </tr>

                      <tr height="10px">

                        <td colspan="2">

<!--                        

                          <html:text property="txtGroupOrUserName" styleClass="borderClrLvl_2 componentStyle componentStyleDisable" style="width:135px" readonly="true" />

                          <html:button property="btnAdd" styleClass="buttons" style="width:65px" tabindex="8" ><bean:message key="btn.Add"/> </html:button>

-->

                        </td>

                      </tr>

                      <tr height="70px">

                        <td colspan="2">

                          <html:select   property="lstGroupOrUserList" multiple="true" size="5"   style="width:205px" styleClass="borderClrLvl_2 componentStyle" tabindex="9" >

                            <html:options name="groupNewEditForm" property="lstGroupOrUserList" />

                          </html:select>

                        </td>

                        </tr>

                      <tr height="25px">

                        <td colspan="2">

                          <html:button property="btnRemoveFromList" styleClass="buttons" style="width:205px" onclick="removeFromList(this.form)" tabindex="10" >

                            <bean:message key="btn.RemoveFromList" />

                          </html:button>

                        </td>

                      </tr>

                    </table>

                </fieldset>

              </td>

            </tr>

            <tr>

              <td valign="top">

                <div align="right"><bean:message key="txa.Description" /></div></td>

              <td valign="top" colspan="3"><html:textarea property="txaDescription" styleClass="borderClrLvl_2 componentStyle" style="width:230px; height:155px" tabindex="1" /></td>

            </tr>

            <tr>

              <td><div align="right"><bean:message key="txt.AccessControlList" /></div></td>

              <td colspan="3">

                <html:text name="groupNewEditForm" property="txtAccessControlList" styleClass="borderClrLvl_2 componentStyle componentStyleDisable" style="width:232px" readonly="true"  />

                <html:button property="btnAccessControlList" styleClass="buttons" style="width:20px;height:17px;" value="..."  onclick="openWindow('relayAction.do?operation=page_acl_select&control=txtAccessControlList','groupEditAcl',505,620,0,0,true);" tabindex="4" />

            </tr>

          </table>

          </fieldset>

        </td>

      </tr>

      <tr>

        <td height="25px" align="center">            

          <div align="right">

            <html:button property="btnSave" styleClass="buttons" style="width:70px" onclick="return callPage(this.form)" tabindex="11" ><bean:message key="btn.Save" /></html:button>

            <html:button property="btnCancel" styleClass="buttons" style="width:70px; margin-right:10px" onclick="history.go(-1);" tabindex="12" ><bean:message key="btn.Cancel" /></html:button>

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

      <table class="imgStatusBar borderClrLvl_2 bgClrLvl_4 "  width="700px" height="20px" border="0" cellpadding="0" cellspacing="0" id="statusBar">

        <tr>

          <td width="30px"><div class="imgStatusMsg"></div></td>

          <td>

            <logic:messagesPresent >

               <bean:message key="errors.header"/>

               <html:messages id="error">   

                  <font color="red"><bean:write name="error"/></font>

               </html:messages>

            </logic:messagesPresent>

            <html:messages id="msg" message="true">

              <bean:write  name="msg"/>

            </html:messages>

          </td>

        </tr>

      </table>

      <!-- statusBar table ends above-->

      </td>

    </tr>

  </table>

</html:form>

<!--Content Ends-->

</body>

</html:html>