<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%

request.setAttribute("topic","acl_edit_html");

//Variable declaration

boolean firstTableRow; 

firstTableRow = true;

%>

<html:html>

<head>

<title><bean:message key="title.AclEdit" /></title>

<script src="general.js"></script>

<html:javascript formName="aclNewEditForm" dynamicJavascript="true" staticJavascript="false"/>

<script language="Javascript1.1" src="staticJavascript.jsp"></script>

<script language="Javascript1.1" >



function cancelActionPerformed(){

    window.location.replace("aclListAdminAction.do");

}        



function callPage(thisForm){ 

    thisForm.action="aclEditAdminAction.do?isNewAcl=false"   ;

    thisForm.submit() ;

   

}



function callRemove(thisForm){ 

  var isSelected=false;

  if (typeof thisForm.chkSelect!="undefined"){

    if (typeof thisForm.chkSelect.length !="undefined"){

      for(var index=0; index< thisForm.chkSelect.length; index++){

        if (thisForm.chkSelect[index].checked){

          isSelected=true;

          break;

        }

      }

    }else{

      isSelected=thisForm.chkSelect.checked;

    }

  }else{

    alert("<bean:message key="errors.acl.remove"/>");

  }



  if (isSelected){

        thisForm.action="aceRemoveAdminAction.do"   ;

        thisForm.submit() ;

  }else{

        alert("<bean:message key="errors.acl.removeSelect"/>");

  }   

}



function callAddPermission(thisForm){ 

    if(thisForm.txtAddUserGroupAcl.value!=null && thisForm.txtAddUserGroupAcl.value!=""){    

    thisForm.action="permissionB4NewEditAdminAction.do?isNew=true&isNewAcl=false";    

    thisForm.submit() ;   

    }else{

      alert("<bean:message key="errors.granteeName.required"/>");

    }

}



function callEditPermission(thisForm,index,name,type){

    var editIndex=index;

    var granteeName=name;

    var granteeType=type;    

    thisForm.action="permissionB4NewEditAdminAction.do?isNew=false&index="+editIndex+"&granteeName="+granteeName+"&granteeType="+granteeType+"&isNewAcl=false";    

    thisForm.submit() ;   

}



function openList(thisForm){

    if(thisForm.cboUserGroup.value=="User"){

      openWindow('relayAction.do?operation=page_user_select&control=txtAddUserGroupAcl','aclEditUser',505,450,0,0,true);

    }else if(thisForm.cboUserGroup.value=="Group"){

      openWindow('relayAction.do?operation=page_group_select&control=txtAddUserGroupAcl','aclEditGroup',515,450,0,0,true);

    }else if(thisForm.cboUserGroup.value=="Acl"){

      openWindowl('relayAction.do?operation=page_acl_select&control=txtAddUserGroupAcl','aclEditAcl',505,620,0,0,true);

    }

}



function cancelActionPerformed(thisForm){

   thisForm.action="relayAction.do?operation=cancel_acl";

   thisForm.submit();  

}



function checkAll(thisForm){

   if (typeof thisForm.chkSelect!="undefined"){

    if (typeof thisForm.chkSelect.length !="undefined"){

       for (var count=0;count<thisForm.chkSelect.length;count++) {

          thisForm.chkSelect[count].checked=thisForm.chkSelectAll.checked;

      }

    }else{

      thisForm.chkSelect.checked=thisForm.chkSelectAll.checked;

    }

   }

}



function unCheckChkAll(me){

  var thisForm=me.form;

  var checkValue = (me.checked)?true:false;

  var isAllSelected=false;



  if (typeof thisForm.chkSelect!="undefined"){

    if (typeof thisForm.chkSelect.length !="undefined"){

      if(checkValue){        

        for (var count=0;count<thisForm.chkSelect.length;count++) {

          if(thisForm.chkSelect[count].checked){

            isAllSelected=true;           

          }else{

            isAllSelected=false;

            break;

          }

        }      

      }

    }else{

      isAllSelected=thisForm.chkSelect.checked;

    }

  }

  thisForm.chkSelectAll.checked=isAllSelected;

}

    

</script>

</head>

<body style="margin:0">

<!-- This page contains 3 outermost tables, named 'headerIncluder', 'errorContainer' and 'tabContainer' -->

<html:form action="/aclEditAdminAction" onsubmit="return validateAclNewEditForm(this);" focus="txaAclDescription">  

<table id="headerIncluder" width="100%"  border="0" cellspacing="0" cellpadding="0">

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

	<table id="tabParent" width="770px"  border="0" cellpadding="0" cellspacing="0">

		<tr>

    	<td>

			<table id="tab" width="150px" border="0" cellpadding="0" cellspacing="0">

      		<tr>

          <td width="5px" class="imgTabLeft"></td>

          <td width="140px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.EditAcl" /></div></td>

          <td width="5px" class="imgTabRight"></td>

      		</tr>

    	</table>

		</td>

 		</tr>

	</table>

	<table id="borderClrLvl_2" width="770px" border="0" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2">



      <tr>

			<td height="10px" colspan="2" align="center"></td>

      </tr>

      <tr>

			<td colspan="2" align="center">

			  <fieldset style="width:740px;">

			  <legend align="left" class="tabText"></legend>

			  <table width="740px" border="0" id="general">

				<tr>

				  <td width="19%"><div align="right"><bean:message key="txt.AclName" /></div></td>

				  <td width="28%"><html:text name="aclNewEditForm" property="txtAclName" styleClass="borderClrLvl_2 componentStyle componentStyleDisable" style="width:200px" readonly="true" maxlength="20" /></td>

				  <td width="21%"><div align="right"><bean:message key="txt.OwnerName" /></div></td>

				  <td width="32%"><html:text name="aclNewEditForm" property="txtOwnerName" styleClass="borderClrLvl_2 componentStyle" style="width:200px" tabindex="2" maxlength="20" /></td>

				</tr>

				<tr>

				  <td valign="top"><div align="right"><bean:message key="txa.Description" /></div></td>

				  <td rowspan="3" valign="top"><html:textarea name="aclNewEditForm" property="txaAclDescription" styleClass="borderClrLvl_2 componentStyle" style="width:200px" tabindex="1" /></td>

				  <td><div align="right"><bean:message key="txt.SecuringAclName" /></div></td>

				  <td>

            <html:text name="aclNewEditForm" property="txtAccessControlList" styleClass="borderClrLvl_2 componentStyle componentStyleDisable" style="width:175px" readonly="true" />

            <html:button property="btnAccessControlList" styleClass="buttons" style="width:20px" value="..." onclick="openWindow('relayAction.do?operation=page_acl_select&control=txtAccessControlList','aclEditSecuringAcl',505,620,0,0,true)" tabindex="3" ></html:button>

				  </td>

				</tr>

				<tr>

				  <td valign="top"><div align="right"></div></td>

				  <td>&nbsp;</td>

				  <td>&nbsp;</td>

				</tr>

				<tr>

				  <td valign="top"><div align="right"></div></td>

				  <td>&nbsp;</td>

				  <td>&nbsp;</td>

				</tr>

			  </table>

			  </fieldset>

			</td>

		</tr>

    <tr><td colspan="2" height="5px"></td></tr>

		<tr>

		  <td colspan="2" align="center" >

      <table width="100%">

      <tr>

      <td align="left" >&nbsp;&nbsp;<bean:message key="lbl.Add" />&nbsp;

        <html:select property="cboUserGroup" styleClass="borderClrLvl_2 componentStyle" style="width:100px" tabindex="5">

          <html:option value="User"><bean:message key="lbl.User" /></html:option>

          <html:option value="Group"><bean:message key="lbl.Group" /></html:option>                

        </html:select>&nbsp;&nbsp;

        <html:text property="txtAddUserGroupAcl" styleClass="borderClrLvl_2 componentStyle componentStyleDisable" style="width:200px" readonly="true" />

        <html:button property="btnAdd" styleClass="buttons" style="width:70px" onclick="callAddPermission(this.form)" tabindex="7" titleKey="title.Add"><bean:message key="btn.Add" /></html:button>

        <html:button property="btnFind" styleClass="buttons" style="width:70px" onclick="openList(this.form)" tabindex="6" titleKey="title.Find"><bean:message key="btn.Find" /></html:button>

        </td>

        <td align="right" >

          <html:button property="btnRemove" styleClass="buttons" style="width:70px" onclick="callRemove(this.form)" tabindex="8"><bean:message key="btn.Remove" /></html:button>&nbsp;&nbsp;

        </td>  

        </tr>

        </table>

			</td>

	    </tr>

      <tr><td colspan="2" height="5px"></td></tr>

		<tr>

			<td colspan="2" align="center">

			<fieldset style="width:740px;">

			<legend align="left" class="tabText"></legend>

			<table id="dataParent" width="740px"  border="0" cellpadding="0" cellspacing="0">

			  <tr>

          <td align="center">

            <div class="bgClrLvl_5" style="overflow:auto; width:740px; height:220px">

              <table id="data" width="728px" border="0" cellspacing="1" cellpadding="0" class="bgClrLvl_F">

                <tr>

                  <th height="18px" width="4%"><html:checkbox property="chkSelectAll" style="height:12px" tabindex="9" onclick="checkAll(this.form)" /></th>

                  <th width="15%"><bean:message key="tbl.head.Category" /></th>

                  <th width="56%"><bean:message key="tbl.head.Name" /></th>

                  <th width="15%"><bean:message key="tbl.head.State" /></th>

                  <th width="10%"><bean:message key="tbl.head.Permission" /></th>

                </tr>                

               <logic:iterate name="aceList" id="aceList" >

               <bean:define id="index" name="aceList" property="index" scope="page"/>

                     <%if (firstTableRow == true){ firstTableRow = false; %>

                    <tr class="bgClrLvl_4">

                  <%}else{ firstTableRow = true; %>

    				      <tr class="bgClrLvl_3">                  

                  <%}%>

                  <td>

                    <div align="center">

                      

                      <html:multibox property="chkSelect" value="<%=index%>" tabindex="10" onclick="unCheckChkAll(this)" />

                    </div>

                  </td>

                  <td align="left"><bean:write name="aceList" property="category"/> </td>

                  <td align="left"><bean:write name="aceList" property="name"/> </td>

                  <td align="left"><bean:write name="aceList" property="state"/> </td>

                  <td align="center" >

                  <input type="button" name="btnPermissions" value="..." onclick=<% out.print("callEditPermission(this.form,"+index+",'");%><bean:write name="aceList" property="name"/>','<bean:write name="aceList" property="category"/>')  style="width:20px" class="buttons">

                    

                  <%//<html:img src="themes/standard/lookup.gif" width="18" height="17" onclick="callEditPermission(this.form)" />%>



                  </td>

                </tr>

                 </logic:iterate>

              </table>

                <!-- data table ends here-->

            </div>

          </td>

			  </tr>

			</table>

			<!-- dataParent table ends here-->

			</fieldset>

			</td>

		</tr>

		<tr>

		<td width="758" height="25px">

      <div align="right">

        <html:button property="btnSave" styleClass="buttons" style="width:70px" onclick="return callPage(this.form)" tabindex="10"><bean:message key="btn.Save" /></html:button>

        <html:button property="btnCancel" styleClass="buttons" style="width:70px" onclick="cancelActionPerformed(this.form)" tabindex="11"><bean:message key="btn.Cancel" /></html:button>

      </div>

		</td>

		<td width="10">&nbsp;</td>

		</tr>		      

	</table>

<!-- borderClrLvl_2 table ends above-->

</td>

</tr>

	<tr><td height="2px"></td></tr>

	<tr>

      <td align="center">

      <table class="imgStatusBar borderClrLvl_2 bgClrLvl_4 " height="20px" width="770px" border="0" cellpadding="0" cellspacing="0" id="statusBar">

          <tr>

          <td width="30px"><div class="imgStatusMsg"></div></td>

          <td>

            <html:messages id="msg" message="true">

              <bean:write  name="msg"/>

            </html:messages>

            <html:messages id="editError">

              <font color="red"> <bean:write  name="editError"/></font>

            </html:messages>


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