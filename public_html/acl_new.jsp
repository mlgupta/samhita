<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%

request.setAttribute("topic","acl_new_html");

//Variable declaration

boolean firstTableRow;

firstTableRow = true; 

%>

<html:html>

<head>

<title><bean:message key="title.AclNew" /></title>

<script src="general.js"></script>

<html:javascript formName="aclNewEditForm" dynamicJavascript="true" staticJavascript="false"/>

<script language="Javascript1.1" src="staticJavascript.jsp"></script>

<script language="Javascript1.1" >



function callPage(thisForm){ 

  if(thisForm.txtAclName.value==null || thisForm.txtAclName.value==""){

    alert("<bean:message key="errors.aclName.required"/>");

    return;

  }
  
  if(validAclName()){

    if(validateAclNewEditForm(thisForm)){        
  
      thisForm.action="aclNewAdminAction.do?isNewAcl=true";
  
      thisForm.submit();
  
    }

  }

}



function callAddPermission(thisForm){ 

  if(thisForm.txtAclName.value==null || thisForm.txtAclName.value==""){

    alert("<bean:message key="errors.aclName.required"/>");

    return;

  }


  if(validateAclNewEditForm(thisForm)){        

    if(thisForm.txtAddUserGroupAcl.value!=null && thisForm.txtAddUserGroupAcl.value!=""){    
      
      thisForm.action="permissionB4NewEditAdminAction.do?isNew=true&isNewAcl=true";    

      thisForm.submit() ;   

    }else{

      alert("<bean:message key="errors.granteeName.required"/>");

    }

  }

}



function callEditPermission(thisForm,index,name,type){

  var editIndex=index;

  var granteeName=name;

  var granteeType=type;    

  thisForm.action="permissionB4NewEditAdminAction.do?isNew=false&index="+editIndex+"&granteeName="+granteeName+"&granteeType="+granteeType;    

  thisForm.submit();

}



function openList(thisForm){

  if(thisForm.cboUserGroup.value=="User"){

    openWindow('relayAction.do?operation=page_user_select&control=txtAddUserGroupAcl','aclNewUser',505,450,0,0,true);

  }else if(thisForm.cboUserGroup.value=="Group"){

    openWindow('relayAction.do?operation=page_group_select&control=txtAddUserGroupAcl','aclNewGroup',515,450,0,0,true);

  }else if(thisForm.cboUserGroup.value=="Acl"){

    openWindow('relayAction.do?operation=page_acl_select&control=txtAddUserGroupAcl','aclNewAcl',505,620,0,0,true);

  }

}



function cancelActionPerformed(thisForm){

  thisForm.action="relayAction.do?operation=cancel_acl";

  thisForm.submit();  

}

function validAclName(){
  var aclName = document.forms[0].txtAclName.value;
  var aclPrefixValue = document.forms[0].hdnAdapterName.value;
  if( aclPrefixValue == "nonadapter" ){
    var hdnPrefix = document.forms[0].hdnPrefix.value; 
    var splitArr = hdnPrefix.split(",");
    for( var index = 0; index < splitArr.length; index++ ){
      if( aclName.search(splitArr[index]) == 0 ){
        alert("Reserved Prefix Used");
        document.forms[0].txtAclName.focus();
        return false;
      }
    }
  }else{
    if( aclName.search(aclPrefixValue) !=0 ){
      alert("Invalid Prefix Used");
      document.forms[0].txtAclName.focus();
      return false;
    }
  }
  return true;
}


function setAclPrefix(prefix){
  document.forms[0].hdnAdapterName.value = prefix;
  if( prefix == "nonadapter" ){
    document.forms[0].txtAclName.value = "";
  }else{
    document.forms[0].txtAclName.value = prefix;
  }
}
</script>

</head>

<body style="margin:0">

<!-- This page contains 3 outermost tables, named 'headerIncluder', 'errorContainer' and 'tabContainer' -->

<html:form action="/aclNewAdminAction" onsubmit="return validateAclNewEditForm(this);" focus="txtAclName">  

<html:hidden name="aclNewEditForm" property="hdnPrefix" />

<html:hidden name="aclNewEditForm" property="hdnAdapterName" />

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

            <td width="140px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.NewAcl" /></div></td>

            <td width="5px" class="imgTabRight"></td>

          </tr>

        </table>

      </td>

  	</tr>

	</table>

	<table id="borderClrLvl_2" width="770px" border="0" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2">

      <tr>

			<td height="10px" colspan="2" ></td>

      </tr>

      <tr>

			<td colspan="2" align="center">

			  <fieldset style="width:740px;">

			  <legend align="left" class="tabText"></legend>

			  <table width="740px" border="0" id="general">

				<tr>

				  <td width="19%"><div align="right"><bean:message key="txt.AclName" /></div></td>

				  <td width="28%"><html:text name="aclNewEditForm" property="txtAclName" styleClass="borderClrLvl_2 componentStyle" style="width:200px"  tabindex="1" maxlength="20" /></td>

				  <td width="21%"><div align="right"><bean:message key="txt.AclType" /></div></td>

				  <td width="32%">
            <html:select onchange="return setAclPrefix(this.value);" property="cboAclType" styleClass="borderClrLvl_2 componentStyle" style="width:200px" tabindex="2">
              <logic:iterate id="adapter" name="adapters">
              <bean:define id="prefix" name="adapter" property="aclPrefix" />
              <html:option value="<%=prefix%>"><bean:write name="adapter" property="adapterName" /></html:option>
              </logic:iterate>
            </html:select>&nbsp;&nbsp;
          </td>

				</tr>

				<tr>

				  <td valign="top"><div align="right"><bean:message key="txa.Description" /></div></td>

				  <td rowspan="3" valign="top"><html:textarea name="aclNewEditForm" property="txaAclDescription" styleClass="borderClrLvl_2 componentStyle" style="width:200px" tabindex="3"/></td>

				  <td><div align="right"><bean:message key="txt.OwnerName" /></div></td>

				  <td><html:text name="aclNewEditForm" property="txtOwnerName" styleClass="borderClrLvl_2 componentStyle componentStyleDisable" style="width:200px" tabindex="4" readonly="true" maxlength="20" /></td>

				</tr>

				<tr>

				  <td valign="top"><div align="right"></div></td>

				  <td><div align="right"><bean:message key="txt.SecuringAclName" /></div></td>

				  <td>

            <html:text name="aclNewEditForm" property="txtAccessControlList" styleClass="borderClrLvl_2 componentStyle componentStyleDisable" style="width:175px" readonly="true" maxlength="20" />

            <html:button property="btnAccessControlList" styleClass="buttons" style="width:20px" value="..." onclick="openWindow('relayAction.do?operation=page_acl_select&control=txtAccessControlList','aclNewSecuringAcl',505,620,0,0,true);" tabindex="5"></html:button>

				  </td>

				</tr>

			  </table>

			  </fieldset>

			</td>

		</tr>

 		<tr><td colspan="2" height="5px"></td></tr>

		<tr>

		  <td td colspan="2" align="center" >

      <table width="100%">

        <tr>

          <td align="left" >&nbsp;&nbsp;<bean:message key="lbl.Add" />&nbsp;

            <html:select property="cboUserGroup" styleClass="borderClrLvl_2 componentStyle" style="width:100px" tabindex="6">

              <html:option value="User"><bean:message key="lbl.User" /></html:option>

              <html:option value="Group"><bean:message key="lbl.Group" /></html:option>                

            </html:select>&nbsp;&nbsp;

            <html:text property="txtAddUserGroupAcl" styleClass="borderClrLvl_2 componentStyle componentStyleDisable" style="width:200px" readonly="true" />

            <html:button property="btnAdd" styleClass="buttons" style="width:70px" onclick="callAddPermission(this.form)" tabindex="8" titleKey="title.Add"><bean:message key="btn.Add" /></html:button>

            <html:button property="btnFind" styleClass="buttons" style="width:70px" onclick="openList(this.form)" tabindex="7" titleKey="title.Find"><bean:message key="btn.Find" /></html:button>

            </td>

            <td align="right" ></td>  

          </tr>

        </table>

      </td>

	    </tr>

      <tr><td colspan="2" height="5px"></td></tr>

		<tr>

			<td colspan="2" align="center">

			<fieldset style="width:740px;height:240px">

			<legend align="left" class="tabText"></legend>

			<table id="dataParent" width="730px"  border="0" cellpadding="0" cellspacing="0">

			  <tr>

          <td align="center">

            <div class="borderClrLvl_2" style="overflow:auto; width:730px; height:220px">

              <table id="data" width="728px" border="0" cellpadding="0" cellspacing="1" class="borderClrLvl_F">

                <tr>

                  <th height="18px" width="4%"><html:checkbox style="height:12px" property="chkSelectAll" tabindex="9"/></th>

                  <th width="13%"><bean:message key="tbl.head.Category" /></th>

                  <th width="55%"><bean:message key="tbl.head.Name" /></th>

                  <th width="15%"><bean:message key="tbl.head.State" /></th>

                  <th width="13%"><bean:message key="tbl.head.Permission" /></th>

                </tr>                

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