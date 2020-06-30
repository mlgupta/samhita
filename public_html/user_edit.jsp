<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%request.setAttribute("topic","user_edit_html");%>

<html:html>

<head> 

<title><bean:message key="title.UserEdit" /></title>

<script src="general.js"></script>

<html:javascript formName="userNewEditForm" dynamicJavascript="true" staticJavascript="false"/>

<script language="Javascript1.1" src="staticJavascript.jsp"></script>

<script language="Javascript1.1" >

  function callPage(thisForm){ 
    for(index = 0 ; index < thisForm.lstGroupOrUserList.length ;index++){     
      thisForm.lstGroupOrUserList[index].selected=true;    
    }
    if(validateUserNewEditForm(thisForm)){
      if (alphaNumeric(thisForm.txtPassword.value)==false){
        alert("<bean:message key="errors.password.alphaNumeric"/>");  
        thisForm.txtPassword.value=""; 
        thisForm.txtConfirmPassword.value="";
        return false;
      }
      if(thisForm.txtPassword.value!=thisForm.txtConfirmPassword.value){
        alert("<bean:message key="errors.password.mismatch"/>");  
        return false;
      }
      if ((thisForm.radQuota[0].checked==true) && (thisForm.txtQuota.value=="")) {
        alert("<bean:message key="errors.user.contentQuota"/>"); 
        return false;
      }
      
      var counter=0;
      var length=thisForm.txtWorkFlowAcl.length;
      while(counter <length){     
        if(thisForm.txtWorkFlowAcl[counter].value =="WORKFLOW_NOT_ENABLED"){
          thisForm.txtWorkFlowAcl.remove(counter);
          length=thisForm.txtWorkFlowAcl.length;   
        }else{
          counter++;
        }
      }
      for(index = 0 ; index < thisForm.txtWorkFlowAcl.length ;index++){
        thisForm.txtWorkFlowAcl[index].selected = true;
      }
      thisForm.submit() ;
    }
  }

  function removeWfFromList(thisForm){
    var index=0;
    var length=thisForm.txtWorkFlowAcl.length;
    while(index <length){     
      if(thisForm.txtWorkFlowAcl[index].selected){
        if(thisForm.txtWorkFlowAcl[index].value!=""){
           thisForm.txtWorkFlowAcl.remove(index);
           length=thisForm.txtWorkFlowAcl.length;   
         }else{
           alert("Nothing To Remove");
        }
      }else{
        index++;
      }
    }
  }

  function removeFromList(thisForm){
    var index=0;
    var length=thisForm.lstGroupOrUserList.length;
    while(index <length){     
      if(thisForm.lstGroupOrUserList[index].selected){
        if(thisForm.lstGroupOrUserList[index].value!="WORLD [G]"){
           thisForm.lstGroupOrUserList.remove(index);
           length=thisForm.lstGroupOrUserList.length;   
         }else{
           alert("<bean:message key="msg.world.delete"/>");
           index++;
        }
      }else{
        index++;
      }
    }
  }

function lookUp(){
  if( document.forms[0].txtWorkFlowAcl.length != 0 ){
    if( document.forms[0].txtWorkFlowAcl[0].value == "WORKFLOW_NOT_ENABLED" ){
      document.forms[0].txtWorkFlowAcl.remove(0);
    }
  }
  openWindow('relayAction.do?operation=acl_select_workflow&control=txtWorkFlowAcl&txtSearchByAclName=wf','userNewFolderAcl',505,620,0,0,true);
}

function clear_textbox(thisForm){
  thisForm.txtWorkFlowAcl.value ="WORKFLOW_NOT_ENABLED";
} 

</script>


</head>

<body style="margin:0">

<html:form  action="/userEditAdminAction" 

         onsubmit="return validateUserNewEditForm(this);" >

<!-- This page contains 3 outermost tables, named 'headerIncluder', 'errorContainer' and 'tabContainer' -->

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

<!--Content Starts-->

<bean:define name="userNewEditForm" type="dms.web.actionforms.user.UserNewEditForm" id="userNewEditForm" />

<table id="tabContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">

<!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->

<tr>

<td align="center">



<table id="tabParent" width="990px"  border="0" cellpadding="0" cellspacing="0" >

  <tr>

    <td>

	<table width="150px" border="0" cellpadding="0" cellspacing="0" id="tab">

      <tr>

      <td width="5px" class="imgTabLeft"></td>

        <td width="140px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.EditUser" /></div></td>

        <td width="5px" class="imgTabRight"></td>

      </tr>

    </table>

	</td>

  </tr>

</table>



<table id="borderClrLvl_2" width="990px" border="0" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2" >

      <tr>

        <td align="center" height="10px"></td>

      </tr>

      <tr>

        <td align="center">

		  <fieldset style="width:965px;">

          <legend align="left" class="tabText"><bean:message key="lbl.General" /></legend>

          <table width="965" border="0" id="general">

            <tr>

                <td width="14%"><div align="right"><bean:message key="txt.UserName" /></div></td>

                <td width="25%"><html:text  property="txtUserName" tabindex="1" styleClass="borderClrLvl_2 componentStyle componentStyleDisable" style="width:200px" readonly="true" /></td>

                <td width="13%"><div align="right"><bean:message key="txt.Password" /></div></td>

                <td width="24%"><html:password  name="userNewEditForm" property="txtPassword" tabindex="5" styleClass="borderClrLvl_2 componentStyle" style="width:155px" value="PasswordNotChanged123" redisplay="false"/></td>

                <td width="24%" rowspan="8" valign="top"><div align="right"></div>

                <fieldset style="width:215x;">

                    <legend align="left" class="tabText"><bean:message key="lbl.AddGroups" /></legend>

                    <table width="210px" border="0" cellpadding="1">

                      <tr height="30px">

                        <td>

                          <html:button property="btnAddFromGroupList" styleClass="buttons" style="width:205px" tabindex="9" onclick="openWindow('relayAction.do?operation=page_group_select&control=lstGroupOrUserList','userEditGroup',515,450,0,0,true);" ><bean:message key="btn.AddFromGroupList" /></html:button>

                        </td>

                      </tr>

                      <tr>

                        <td>

<!--                        <html:text name="userNewEditForm" property="txtGroupName" styleClass="borderClrLvl_2 componentStyle" style="width:135px" value="Enter Group Name" />

                            <html:button property="btnAdd" styleClass="buttons" style="width:65px"><bean:message key="btn.Add" /></html:button>

-->

                        </td>

                      </tr>

                      <tr>

                        <td>

                            <html:select  name="userNewEditForm" property="lstGroupOrUserList" size="4"  multiple="true" style="width:100%" styleClass="borderClrLvl_2 componentStyle" tabindex="11" >

                                <html:options name="userNewEditForm" property="lstGroupOrUserList" />

                            </html:select>

                        </td>

                      </tr>

                      <tr height="30px">

                        <td>

                          <html:button property="btnRemoveFromList" styleClass="buttons" style="width:205px" onclick="removeFromList(this.form)" tabindex="12" ><bean:message key="btn.RemoveFromList" /></html:button>

                        </td>

                      </tr>

                    </table>

                </fieldset>              

            </td>

            </tr>

            <tr>

              <td rowspan="2" valign="top">

                <div align="right"><bean:message key="txa.Description" /></div>

              </td>

              <td rowspan="4" valign="top">

                <html:textarea name="userNewEditForm" property="txaDescription" styleClass="borderClrLvl_2 componentStyle" style="width:200px; height:100px" tabindex="2" />

              </td>

              <td>

              <div align="right"><bean:message key="txt.ConfirmPassword" /></div></td>

              <td>

                <html:password  name="userNewEditForm" property="txtConfirmPassword" styleClass="borderClrLvl_2 componentStyle" style="width:155px" value="PasswordNotChanged123" redisplay="false" tabindex="6" />

              </td>

            </tr>

            <tr>

              <td>&nbsp;</td>

              <td>&nbsp;</td>

            </tr>

            <tr>

              <td>&nbsp;</td>

              <td><div align="right"><bean:message key="rad.Status" />:</div></td>

              <td><html:radio name="userNewEditForm" property="radStatus" value="1" tabindex="7" /><bean:message key="rad.Status.SystemAdmin" /></td>

            </tr>            

            <tr>

              <td>&nbsp;</td>

              <td>&nbsp;</td>

              <td><html:radio name="userNewEditForm" property="radStatus" value="2" tabindex="7" /><bean:message key="rad.Status.Admin" /></td>

            </tr>

            <tr>

                <td><div align="right"><bean:message key="cbo.CredentialManager" />:</div></td>

                <td>

                  <html:select  property="cboCredentialManager" styleClass="borderClrLvl_2 componentStyle" style="width:200px" tabindex="3" >

                    <html:options name="userNewEditForm" property="cboCredentialManager" />    

                  </html:select>

                </td>

                <td>&nbsp;</td>

                <td><html:radio name="userNewEditForm" property="radStatus"  value="3" tabindex="7" /><bean:message key="rad.Status.NonAdmin" /> </td>

            </tr>

            <tr>

              <td>

                <div align="right"><bean:message key="txt.AccessControlList"  /></div></td>

              <td>

                <html:text name="userNewEditForm" property="txtAccessControlList" styleClass="borderClrLvl_2 componentStyle componentStyleDisable" style="width:200px" readonly="true" />

                <html:button property="btnAccessControlList" styleClass="buttons" style="width:20px;height:17px;" value="..." onclick="openWindow('relayAction.do?operation=page_acl_select&control=txtAccessControlList&forUser=true','userEditSystemAcl',505,620,0,0,true);" tabindex="4" />

              </td>

              <td>&nbsp;</td>

            </tr>

           </table>

        </fieldset></td>

      </tr>

            <tr>

        <td align="center" height="10px"></td>

      </tr>



      <tr>

        <td align="center">

            <fieldset style="width:965px">

            <legend align="left" class="tabText"><bean:message key="lbl.Profile" /></legend>

            <table width="965px" border="0" id="profile">

              <tr>

                <td width="13%"><div align="right"><bean:message key="txt.HomeFolder" /></div></td>

                <td width="24%"><html:text name="userNewEditForm" property="txtHomeFolder" styleClass="borderClrLvl_2 componentStyle" style="width:200px" tabindex="13"/> </td>

                <td width="14%"><div align="right"><bean:message key="txt.MailFolder" /></div></td>

                <td width="22%"><html:text name="userNewEditForm" property="txtMailFolder" styleClass="borderClrLvl_2 componentStyle" style="width:155px" tabindex="16" />

                </td>

                <td width="10%"><div align="right"><bean:message key="cbo.Language" />:</div></td>

                <td width="17%">

                <html:select  property="cboLanguage" styleClass="borderClrLvl_2 componentStyle" style="width:155px" tabindex="20" >

                  <html:option value="ENGLISH">English</html:option>

                  <html:option value="SPANISH">Spanish</html:option>

                  <html:option value="LATIN AMERICAN SPANISH">Latin American Spanish</html:option>

                  <html:option value="FRENCH">French</html:option>

                  <html:option value="GERMAN">German</html:option>

                  <html:option value="ITALIAN">Italian</html:option>

                  <html:option value="BRAZILIAN PORTUGUESE">Brazilian Portuguese</html:option>

                  <html:option value="SIMPLIFIED CHINESE">Simplified Chinese</html:option>

                  <html:option value="TRADITIONAL CHINESE">Traditional Chinese</html:option>

                  <html:option value="JAPANESE">Japanese</html:option>

                  <html:option value="KOREAN">Korean</html:option>

                </html:select>

                </td>

              </tr>

              <tr>

                <td>

                  <div align="right"><bean:message key="rad.Quota" />:</div>

                </td>

                <td>

                    <html:radio name="userNewEditForm" property="radQuota" value="1" tabindex="14" onclick="document.forms[0].txtQuota.readOnly=false" />

                    <bean:message key="rad.Quota.Limited" /> &nbsp;

                    ( <bean:message key="rad.Quota.Storage" />

                      <html:text name="userNewEditForm" property="txtQuota" styleClass="borderClrLvl_2 componentStyle" style="width:45px;text-align:right" tabindex="15" readonly='<%=(userNewEditForm.getRadQuota()!=null && ((userNewEditForm.getRadQuota()).equals("1")))?false:true %>' onkeypress="return decimalOnly(this,event,1)" />

                      <bean:message key="rad.Quota.Mb" /> )

                </td>

                <td>

                  <div align="right"><bean:message key="txt.EmailAddress" /> </div></td>

                <td valign="top">

                    <html:text name="userNewEditForm" property="txtEmailAddress" styleClass="borderClrLvl_2 componentStyle" style="width:155px" tabindex="17" />

                </td>

                <td><div align="right"><bean:message key="cbo.CharacterSet" />:</div></td>

                <td valign="top">

                  <html:select property="cboCharacterSet" styleClass="borderClrLvl_2 componentStyle" style="width:155px" tabindex="21" >

                    <html:option value="ISO8859_6">Arabic (ISO-8859-6)</html:option>

                    <html:option value="ISO8859_4">Baltic (ISO-8859-4)</html:option>

                    <html:option value="ISO8859_2">Central European (ISO-8859-2)</html:option>

                    <html:option value="EUC_CN">Chinese Simplified (GB2312)</html:option>

                    <html:option value="MS936">Chinese Simplified (GBK)</html:option>

                    <html:option value="MS950">Chinese Traditional (Big5)</html:option>

                    <html:option value="EUC_TW">Chinese Traditional (EUC-TW)</html:option>

                    <html:option value="ISO8859_5">Cyrillic (ISO-8859-5)</html:option>

                    <html:option value="ISO8859_7">Greek (ISO-8859-7)</html:option>

                    <html:option value="ISO8859_8">Hebrew (ISO-8859-8)</html:option>

                    <html:option value="EUC_JP">Japanese (EUC-JP)</html:option>

                    <html:option value="MS932">Japanese (SHIFT_JIS)</html:option>

                    <html:option value="EUC_KR">Korean (KS_C_5601-1987)</html:option>

                    <html:option value="TIS620">Thai (TIS-620)</html:option>

                    <html:option value="ISO8859_9">Turkish (ISO-8859-9)</html:option>

                    <html:option value="UTF8">Unicode (UTF-8)</html:option>

                    <html:option value="Cp1258">Vietnamese (Windows-1258)</html:option>

                    <html:option value="ISO8859_1">Western (ISO-8859-1)</html:option>

                  </html:select>

                </td>

              </tr>

              <tr>

                <td>&nbsp;</td>

                <td>

                  <html:radio name="userNewEditForm" property="radQuota" value="2" onclick="document.forms[0].txtQuota.value='';document.forms[0].txtQuota.readOnly=true" tabindex="14" />

                  <bean:message key="rad.Quota.Unlimited" /> 

                </td>

                <td><div align="right"><bean:message key="txt.DefaultDocumentAcl" /></div></td>

                <td>

                <html:text name="userNewEditForm" property="txtDefaultDocumentAcl" styleClass="borderClrLvl_2 componentStyle componentStyleDisable" style="width:155px" readonly="true" />

                <html:button  property="btnDefaultDocumentAcl" styleClass="buttons" style="width:20px;height:17px;" value="..." onclick="openWindow('relayAction.do?operation=page_acl_select&control=txtDefaultDocumentAcl','userEditDocumentAcl',505,620,0,0,true);" tabindex="18" />

                </td>

                <td><div align="right"><bean:message key="cbo.Locale" />:</div></td>

                <td>

                  <html:select property="cboLocale" styleClass="borderClrLvl_2 componentStyle" style="width:155px" tabindex="22">

                    <html:option value="en_US">American (en-US)</html:option>

                    <html:option value="en_GB">English (en-GB)</html:option>

                    <html:option value="es_ES">Spanish (es-ES)</html:option>

                    <html:option value="es">Latin American Spanish (es)</html:option>

                    <html:option value="fr">French (fr)</html:option>

                    <html:option value="de">German (de)</html:option>

                    <html:option value="it">Italian (it)</html:option>

                    <html:option value="pt_BR">Brazilian Portuguese (pt-BR)</html:option>

                    <html:option value="zh_CN">Simplified Chinese (zh-CN)</html:option>

                    <html:option value="zh_TW">Traditional Chinese (zh-TW)</html:option>

                    <html:option value="ja">Japanese (ja)</html:option>

                    <html:option value="ko">Korean (ko)</html:option>                        

                  </html:select>

                </td>

              </tr>

              <tr>

                <td>&nbsp;</td>

                <td>&nbsp;</td>

                <td><div align="right"><bean:message key="txt.DefaultFolderAcl" /> </div></td>

                <td>

                  <html:text name="userNewEditForm" property="txtDefaultFolderAcl" styleClass="borderClrLvl_2 componentStyle componentStyleDisable" style="width:155px" readonly="true" />

                  <html:button property="btnDefaultFolderAcl" styleClass="buttons" style="width:20px;height:17px;" value="..." onclick="openWindow('relayAction.do?operation=page_acl_select&control=txtDefaultFolderAcl','userEditFolderAcl',505,620,0,0,true);" tabindex="19"/>

                </td>

                <td><div align="right"><bean:message key="cbo.TimeZone" />:</div></td>

                <td>

                  <html:select property="cboTimeZone" styleClass="borderClrLvl_2 componentStyle" style="width:155px" tabindex="23">

                    <option value="MIT">[GMT-11:00] (WST) MIT</option>

                    <option value="Pacific/Apia">[GMT-11:00] (WST) Pacific/Apia</option>

                    <option value="Pacific/Niue">[GMT-11:00] (NUT) Pacific/Niue</option>

                    <option value="Pacific/Pago_Pago">[GMT-11:00] (SST) Pacific/Pago_Pago</option>

                    <option value="America/Adak">[GMT-10:00] (HAST) America/Adak</option>

                    <option value="HST">[GMT-10:00] (HST) HST</option>

                    <option value="Pacific/Fakaofo">[GMT-10:00] (TKT) Pacific/Fakaofo</option>

                    <option value="Pacific/Honolulu">[GMT-10:00] (HST) Pacific/Honolulu</option>

                    <option value="Pacific/Rarotonga">[GMT-10:00] (CKT) Pacific/Rarotonga</option>

                    <option value="Pacific/Tahiti">[GMT-10:00] (TAHT) Pacific/Tahiti</option>

                    <option value="Pacific/Marquesas">[GMT-09:00] (MART) Pacific/Marquesas</option>

                    <option value="AST">[GMT-09:00] (AKST) AST</option>

                    <option value="America/Anchorage">[GMT-09:00] (AKST) America/Anchorage</option>

                    <option value="Pacific/Gambier">[GMT-09:00] (GAMT) Pacific/Gambier</option>

                    <option value="America/Los_Angeles">[GMT-08:00] (PST) America/Los_Angeles</option>

                    <option value="America/Tijuana">[GMT-08:00] (PST) America/Tijuana</option>

                    <option value="America/Vancouver">[GMT-08:00] (PST) America/Vancouver</option>

                    <option value="PST">[GMT-08:00] (PST) PST</option>

                    <option value="Pacific/Pitcairn">[GMT-08:00] (PST) Pacific/Pitcairn</option>

                    <option value="America/Dawson_Creek">[GMT-07:00] (MST) America/Dawson_Creek</option>

                    <option value="America/Denver">[GMT-07:00] (MST) America/Denver</option>

                    <option value="America/Edmonton">[GMT-07:00] (MST) America/Edmonton</option>

                    <option value="America/Mazatlan">[GMT-07:00] (MST) America/Mazatlan</option>

                    <option value="America/Phoenix">[GMT-07:00] (MST) America/Phoenix</option>

                    <option value="MST">[GMT-07:00] (MST) MST</option>

                    <option value="PNT">[GMT-07:00] (MST) PNT</option>

                    <option value="America/Belize">[GMT-06:00] (CST) America/Belize</option>

                    <option value="America/Chicago">[GMT-06:00] (CST) America/Chicago</option>

                    <option value="America/Costa_Rica">[GMT-06:00] (CST) America/Costa_Rica</option>

                    <option value="America/El_Salvador">[GMT-06:00] (CST) America/El_Salvador</option>

                    <option value="America/Guatemala">[GMT-06:00] (CST) America/Guatemala</option>

                    <option value="America/Managua">[GMT-06:00] (CST) America/Managua</option>

                    <option value="America/Mexico_City">[GMT-06:00] (CST) America/Mexico_City</option>

                    <option value="America/Regina">[GMT-06:00] (CST) America/Regina</option>

                    <option value="America/Tegucigalpa">[GMT-06:00] (CST) America/Tegucigalpa</option>

                    <option value="America/Winnipeg">[GMT-06:00] (CST) America/Winnipeg</option>

                    <option value="CST">[GMT-06:00] (CST) CST</option>

                    <option value="Pacific/Easter">[GMT-06:00] (EAST) Pacific/Easter</option>

                    <option value="Pacific/Galapagos">[GMT-06:00] (GALT) Pacific/Galapagos</option>

                    <option value="America/Bogota">[GMT-05:00] (COT) America/Bogota</option>

                    <option value="America/Cayman">[GMT-05:00] (EST) America/Cayman</option>

                    <option value="America/Grand_Turk">[GMT-05:00] (EST) America/Grand_Turk</option>

                    <option value="America/Guayaquil">[GMT-05:00] (ECT) America/Guayaquil</option>

                    <option value="America/Havana">[GMT-05:00] (CST) America/Havana</option>

                    <option value="America/Indianapolis">[GMT-05:00] (EST) America/Indianapolis</option>

                    <option value="America/Jamaica">[GMT-05:00] (EST) America/Jamaica</option>

                    <option value="America/Lima">[GMT-05:00] (PET) America/Lima</option>

                    <option value="America/Montreal">[GMT-05:00] (EST) America/Montreal</option>

                    <option value="America/Nassau">[GMT-05:00] (EST) America/Nassau</option>

                    <option value="America/New_York">[GMT-05:00] (EST) America/New_York</option>

                    <option value="America/Panama">[GMT-05:00] (EST) America/Panama</option>

                    <option value="America/Port-au-Prince">[GMT-05:00] (EST) America/Port-au-Prince</option>

                    <option value="America/Porto_Acre">[GMT-05:00] (ACT) America/Porto_Acre</option>

                    <option value="America/Rio_Branco">[GMT-05:00] (GMT-05:00) America/Rio_Branco</option>

                    <option selected value="EST">[GMT-05:00] (EST) EST</option>

                    <option value="IET">[GMT-05:00] (EST) IET</option>

                    <option value="America/Anguilla">[GMT-04:00] (AST) America/Anguilla</option>

                    <option value="America/Antigua">[GMT-04:00] (AST) America/Antigua</option>

                    <option value="America/Aruba">[GMT-04:00] (AST) America/Aruba</option>

                    <option value="America/Asuncion">[GMT-04:00] (PYT) America/Asuncion</option>

                    <option value="America/Barbados">[GMT-04:00] (AST) America/Barbados</option>

                    <option value="America/Caracas">[GMT-04:00] (VET) America/Caracas</option>

                    <option value="America/Cuiaba">[GMT-04:00] (AMT) America/Cuiaba</option>

                    <option value="America/Curacao">[GMT-04:00] (AST) America/Curacao</option>

                    <option value="America/Dominica">[GMT-04:00] (AST) America/Dominica</option>

                    <option value="America/Grenada">[GMT-04:00] (AST) America/Grenada</option>

                    <option value="America/Guadeloupe">[GMT-04:00] (AST) America/Guadeloupe</option>

                    <option value="America/Guyana">[GMT-04:00] (GYT) America/Guyana</option>

                    <option value="America/Halifax">[GMT-04:00] (AST) America/Halifax</option>

                    <option value="America/La_Paz">[GMT-04:00] (BOT) America/La_Paz</option>

                    <option value="America/Manaus">[GMT-04:00] (AMT) America/Manaus</option>

                    <option value="America/Martinique">[GMT-04:00] (AST) America/Martinique</option>

                    <option value="America/Montserrat">[GMT-04:00] (AST) America/Montserrat</option>

                    <option value="America/Port_of_Spain">[GMT-04:00] (AST) America/Port_of_Spain</option>

                    <option value="America/Puerto_Rico">[GMT-04:00] (AST) America/Puerto_Rico</option>

                    <option value="America/Santiago">[GMT-04:00] (CLT) America/Santiago</option>

                    <option value="America/Santo_Domingo">[GMT-04:00] (AST) America/Santo_Domingo</option>

                    <option value="America/St_Kitts">[GMT-04:00] (AST) America/St_Kitts</option>

                    <option value="America/St_Lucia">[GMT-04:00] (AST) America/St_Lucia</option>

                    <option value="America/St_Thomas">[GMT-04:00] (AST) America/St_Thomas</option>

                    <option value="America/St_Vincent">[GMT-04:00] (AST) America/St_Vincent</option>

                    <option value="America/Thule">[GMT-04:00] (AST) America/Thule</option>

                    <option value="America/Tortola">[GMT-04:00] (AST) America/Tortola</option>

                    <option value="Antarctica/Palmer">[GMT-04:00] (CLT) Antarctica/Palmer</option>

                    <option value="Atlantic/Bermuda">[GMT-04:00] (AST) Atlantic/Bermuda</option>

                    <option value="Atlantic/Stanley">[GMT-04:00] (FKT) Atlantic/Stanley</option>

                    <option value="PRT">[GMT-04:00] (AST) PRT</option>

                    <option value="America/St_Johns">[GMT-03:00] (NST) America/St_Johns</option>

                    <option value="CNT">[GMT-03:00] (NST) CNT</option>

                    <option value="AGT">[GMT-03:00] (ART) AGT</option>

                    <option value="America/Buenos_Aires">[GMT-03:00] (ART) America/Buenos_Aires</option>

                    <option value="America/Cayenne">[GMT-03:00] (GFT) America/Cayenne</option>

                    <option value="America/Fortaleza">[GMT-03:00] (BRT) America/Fortaleza</option>

                    <option value="America/Godthab">[GMT-03:00] (WGT) America/Godthab</option>

                    <option value="America/Miquelon">[GMT-03:00] (PMST) America/Miquelon</option>

                    <option value="America/Montevideo">[GMT-03:00] (UYT) America/Montevideo</option>

                    <option value="America/Paramaribo">[GMT-03:00] (SRT) America/Paramaribo</option>

                    <option value="America/Sao_Paulo">[GMT-03:00] (BRT) America/Sao_Paulo</option>

                    <option value="BET">[GMT-03:00] (BRT) BET</option>

                    <option value="America/Noronha">[GMT-02:00] (FNT) America/Noronha</option>

                    <option value="Atlantic/South_Georgia">[GMT-02:00] (GST) Atlantic/South_Georgia</option>

                    <option value="America/Scoresbysund">[GMT-01:00] (EGT) America/Scoresbysund</option>

                    <option value="Atlantic/Azores">[GMT-01:00] (AZOT) Atlantic/Azores</option>

                    <option value="Atlantic/Cape_Verde">[GMT-01:00] (CVT) Atlantic/Cape_Verde</option>

                    <option value="Atlantic/Jan_Mayen">[GMT-01:00] (EGT) Atlantic/Jan_Mayen</option>

                    <option value="Africa/Abidjan">[GMT] (GMT) Africa/Abidjan</option>

                    <option value="Africa/Accra">[GMT] (GMT) Africa/Accra</option>

                    <option value="Africa/Banjul">[GMT] (GMT) Africa/Banjul</option>

                    <option value="Africa/Bissau">[GMT] (GMT) Africa/Bissau</option>

                    <option value="Africa/Casablanca">[GMT] (WET) Africa/Casablanca</option>

                    <option value="Africa/Conakry">[GMT] (GMT) Africa/Conakry</option>

                    <option value="Africa/Dakar">[GMT] (GMT) Africa/Dakar</option>

                    <option value="Africa/Freetown">[GMT] (GMT) Africa/Freetown</option>

                    <option value="Africa/Lome">[GMT] (GMT) Africa/Lome</option>

                    <option value="Africa/Monrovia">[GMT] (GMT) Africa/Monrovia</option>

                    <option value="Africa/Nouakchott">[GMT] (GMT) Africa/Nouakchott</option>

                    <option value="Africa/Ouagadougou">[GMT] (GMT) Africa/Ouagadougou</option>

                    <option value="Africa/Sao_Tome">[GMT] (GMT) Africa/Sao_Tome</option>

                    <option value="Africa/Timbuktu">[GMT] (GMT) Africa/Timbuktu</option>

                    <option value="Atlantic/Canary">[GMT] (WET) Atlantic/Canary</option>

                    <option value="Atlantic/Faeroe">[GMT] (WET) Atlantic/Faeroe</option>

                    <option value="Atlantic/Reykjavik">[GMT] (GMT) Atlantic/Reykjavik</option>

                    <option value="Atlantic/St_Helena">[GMT] (GMT) Atlantic/St_Helena</option>

                    <option value="Europe/Dublin">[GMT] (GMT) Europe/Dublin</option>

                    <option value="Europe/Lisbon">[GMT] (WET) Europe/Lisbon</option>

                    <option value="Europe/London">[GMT] (GMT) Europe/London</option>

                    <option value="GMT">[GMT] (GMT) GMT</option>

                    <option value="UTC">[GMT] (UTC) UTC</option>

                    <option value="WET">[GMT] (WET) WET</option>

                    <option value="Africa/Algiers">[GMT+01:00] (CET) Africa/Algiers</option>

                    <option value="Africa/Bangui">[GMT+01:00] (WAT) Africa/Bangui</option>

                    <option value="Africa/Douala">[GMT+01:00] (WAT) Africa/Douala</option>

                    <option value="Africa/Kinshasa">[GMT+01:00] (WAT) Africa/Kinshasa</option>

                    <option value="Africa/Lagos">[GMT+01:00] (WAT) Africa/Lagos</option>

                    <option value="Africa/Libreville">[GMT+01:00] (WAT) Africa/Libreville</option>

                    <option value="Africa/Luanda">[GMT+01:00] (WAT) Africa/Luanda</option>

                    <option value="Africa/Malabo">[GMT+01:00] (WAT) Africa/Malabo</option>

                    <option value="Africa/Ndjamena">[GMT+01:00] (WAT) Africa/Ndjamena</option>

                    <option value="Africa/Niamey">[GMT+01:00] (WAT) Africa/Niamey</option>

                    <option value="Africa/Porto-Novo">[GMT+01:00] (WAT) Africa/Porto-Novo</option>

                    <option value="Africa/Tunis">[GMT+01:00] (CET) Africa/Tunis</option>

                    <option value="Africa/Windhoek">[GMT+01:00] (WAT) Africa/Windhoek</option>

                    <option value="ECT">[GMT+01:00] (CET) ECT</option>

                    <option value="Europe/Amsterdam">[GMT+01:00] (CET) Europe/Amsterdam</option>

                    <option value="Europe/Andorra">[GMT+01:00] (CET) Europe/Andorra</option>

                    <option value="Europe/Belgrade">[GMT+01:00] (CET) Europe/Belgrade</option>

                    <option value="Europe/Berlin">[GMT+01:00] (CET) Europe/Berlin</option>

                    <option value="Europe/Brussels">[GMT+01:00] (CET) Europe/Brussels</option>

                    <option value="Europe/Budapest">[GMT+01:00] (CET) Europe/Budapest</option>

                    <option value="Europe/Copenhagen">[GMT+01:00] (CET) Europe/Copenhagen</option>

                    <option value="Europe/Gibraltar">[GMT+01:00] (CET) Europe/Gibraltar</option>

                    <option value="Europe/Luxembourg">[GMT+01:00] (CET) Europe/Luxembourg</option>

                    <option value="Europe/Madrid">[GMT+01:00] (CET) Europe/Madrid</option>

                    <option value="Europe/Malta">[GMT+01:00] (CET) Europe/Malta</option>

                    <option value="Europe/Monaco">[GMT+01:00] (CET) Europe/Monaco</option>

                    <option value="Europe/Oslo">[GMT+01:00] (CET) Europe/Oslo</option>

                    <option value="Europe/Paris">[GMT+01:00] (CET) Europe/Paris</option>

                    <option value="Europe/Prague">[GMT+01:00] (CET) Europe/Prague</option>

                    <option value="Europe/Rome">[GMT+01:00] (CET) Europe/Rome</option>

                    <option value="Europe/Stockholm">[GMT+01:00] (CET) Europe/Stockholm</option>

                    <option value="Europe/Tirane">[GMT+01:00] (CET) Europe/Tirane</option>

                    <option value="Europe/Vaduz">[GMT+01:00] (CET) Europe/Vaduz</option>

                    <option value="Europe/Vienna">[GMT+01:00] (CET) Europe/Vienna</option>

                    <option value="Europe/Warsaw">[GMT+01:00] (CET) Europe/Warsaw</option>

                    <option value="Europe/Zurich">[GMT+01:00] (CET) Europe/Zurich</option>

                    <option value="ART">[GMT+02:00] (EET) ART</option>

                    <option value="Africa/Blantyre">[GMT+02:00] (CAT) Africa/Blantyre</option>

                    <option value="Africa/Bujumbura">[GMT+02:00] (CAT) Africa/Bujumbura</option>

                    <option value="Africa/Cairo">[GMT+02:00] (EET) Africa/Cairo</option>

                    <option value="Africa/Gaborone">[GMT+02:00] (CAT) Africa/Gaborone</option>

                    <option value="Africa/Harare">[GMT+02:00] (CAT) Africa/Harare</option>

                    <option value="Africa/Johannesburg">[GMT+02:00] (SAST) Africa/Johannesburg</option>

                    <option value="Africa/Kigali">[GMT+02:00] (CAT) Africa/Kigali</option>

                    <option value="Africa/Lubumbashi">[GMT+02:00] (CAT) Africa/Lubumbashi</option>

                    <option value="Africa/Lusaka">[GMT+02:00] (CAT) Africa/Lusaka</option>

                    <option value="Africa/Maputo">[GMT+02:00] (CAT) Africa/Maputo</option>

                    <option value="Africa/Maseru">[GMT+02:00] (SAST) Africa/Maseru</option>

                    <option value="Africa/Mbabane">[GMT+02:00] (SAST) Africa/Mbabane</option>

                    <option value="Africa/Tripoli">[GMT+02:00] (EET) Africa/Tripoli</option>

                    <option value="Asia/Amman">[GMT+02:00] (EET) Asia/Amman</option>

                    <option value="Asia/Beirut">[GMT+02:00] (EET) Asia/Beirut</option>

                    <option value="Asia/Damascus">[GMT+02:00] (EET) Asia/Damascus</option>

                    <option value="Asia/Jerusalem">[GMT+02:00] (IST) Asia/Jerusalem</option>

                    <option value="Asia/Nicosia">[GMT+02:00] (EET) Asia/Nicosia</option>

                    <option value="CAT">[GMT+02:00] (CAT) CAT</option>

                    <option value="EET">[GMT+02:00] (EET) EET</option>

                    <option value="Europe/Athens">[GMT+02:00] (EET) Europe/Athens</option>

                    <option value="Europe/Bucharest">[GMT+02:00] (EET) Europe/Bucharest</option>

                    <option value="Europe/Chisinau">[GMT+02:00] (EET) Europe/Chisinau</option>

                    <option value="Europe/Helsinki">[GMT+02:00] (EET) Europe/Helsinki</option>

                    <option value="Europe/Istanbul">[GMT+02:00] (EET) Europe/Istanbul</option>

                    <option value="Europe/Kaliningrad">[GMT+02:00] (EET) Europe/Kaliningrad</option>

                    <option value="Europe/Kiev">[GMT+02:00] (EET) Europe/Kiev</option>

                    <option value="Europe/Minsk">[GMT+02:00] (EET) Europe/Minsk</option>

                    <option value="Europe/Riga">[GMT+02:00] (EET) Europe/Riga</option>

                    <option value="Europe/Simferopol">[GMT+02:00] (EET) Europe/Simferopol</option>

                    <option value="Europe/Sofia">[GMT+02:00] (EET) Europe/Sofia</option>

                    <option value="Europe/Tallinn">[GMT+02:00] (EET) Europe/Tallinn</option>

                    <option value="Europe/Vilnius">[GMT+02:00] (EET) Europe/Vilnius</option>

                    <option value="Africa/Addis_Ababa">[GMT+03:00] (EAT) Africa/Addis_Ababa</option>

                    <option value="Africa/Asmera">[GMT+03:00] (EAT) Africa/Asmera</option>

                    <option value="Africa/Dar_es_Salaam">[GMT+03:00] (EAT) Africa/Dar_es_Salaam</option>

                    <option value="Africa/Djibouti">[GMT+03:00] (EAT) Africa/Djibouti</option>

                    <option value="Africa/Kampala">[GMT+03:00] (EAT) Africa/Kampala</option>

                    <option value="Africa/Khartoum">[GMT+03:00] (EAT) Africa/Khartoum</option>

                    <option value="Africa/Mogadishu">[GMT+03:00] (EAT) Africa/Mogadishu</option>

                    <option value="Africa/Nairobi">[GMT+03:00] (EAT) Africa/Nairobi</option>

                    <option value="Asia/Aden">[GMT+03:00] (AST) Asia/Aden</option>

                    <option value="Asia/Baghdad">[GMT+03:00] (AST) Asia/Baghdad</option>

                    <option value="Asia/Bahrain">[GMT+03:00] (AST) Asia/Bahrain</option>

                    <option value="Asia/Kuwait">[GMT+03:00] (AST) Asia/Kuwait</option>

                    <option value="Asia/Qatar">[GMT+03:00] (AST) Asia/Qatar</option>

                    <option value="Asia/Riyadh">[GMT+03:00] (AST) Asia/Riyadh</option>

                    <option value="EAT">[GMT+03:00] (EAT) EAT</option>

                    <option value="Europe/Moscow">[GMT+03:00] (MSK) Europe/Moscow</option>

                    <option value="Indian/Antananarivo">[GMT+03:00] (EAT) Indian/Antananarivo</option>

                    <option value="Indian/Comoro">[GMT+03:00] (EAT) Indian/Comoro</option>

                    <option value="Indian/Mayotte">[GMT+03:00] (EAT) Indian/Mayotte</option>

                    <option value="Asia/Tehran">[GMT+03:00] (IRT) Asia/Tehran</option>

                    <option value="MET">[GMT+03:00] (IRT) MET</option>

                    <option value="Asia/Aqtau">[GMT+04:00] (AQTT) Asia/Aqtau</option>

                    <option value="Asia/Baku">[GMT+04:00] (AZT) Asia/Baku</option>

                    <option value="Asia/Dubai">[GMT+04:00] (GST) Asia/Dubai</option>

                    <option value="Asia/Muscat">[GMT+04:00] (GST) Asia/Muscat</option>

                    <option value="Asia/Tbilisi">[GMT+04:00] (GET) Asia/Tbilisi</option>

                    <option value="Asia/Yerevan">[GMT+04:00] (AMT) Asia/Yerevan</option>

                    <option value="Europe/Samara">[GMT+04:00] (SAMT) Europe/Samara</option>

                    <option value="Indian/Mahe">[GMT+04:00] (SCT) Indian/Mahe</option>

                    <option value="Indian/Mauritius">[GMT+04:00] (MUT) Indian/Mauritius</option>

                    <option value="Indian/Reunion">[GMT+04:00] (RET) Indian/Reunion</option>

                    <option value="NET">[GMT+04:00] (AMT) NET</option>

                    <option value="Asia/Kabul">[GMT+04:00] (AFT) Asia/Kabul</option>

                    <option value="Asia/Aqtobe">[GMT+05:00] (AQTT) Asia/Aqtobe</option>

                    <option value="Asia/Ashgabat">[GMT+05:00] (TMT) Asia/Ashgabat</option>

                    <option value="Asia/Ashkhabad">[GMT+05:00] (TMT) Asia/Ashkhabad</option>

                    <option value="Asia/Bishkek">[GMT+05:00] (KGT) Asia/Bishkek</option>

                    <option value="Asia/Dushanbe">[GMT+05:00] (TJT) Asia/Dushanbe</option>

                    <option value="Asia/Karachi">[GMT+05:00] (PKT) Asia/Karachi</option>

                    <option value="Asia/Tashkent">[GMT+05:00] (UZT) Asia/Tashkent</option>

                    <option value="Asia/Yekaterinburg">[GMT+05:00] (YEKT) Asia/Yekaterinburg</option>

                    <option value="Indian/Chagos">[GMT+05:00] (IOT) Indian/Chagos</option>

                    <option value="Indian/Kerguelen">[GMT+05:00] (TFT) Indian/Kerguelen</option>

                    <option value="Indian/Maldives">[GMT+05:00] (MVT) Indian/Maldives</option>

                    <option value="PLT">[GMT+05:00] (PKT) PLT</option>

                    <option value="Asia/Calcutta">[GMT+05:00] (IST) Asia/Calcutta</option>

                    <option value="IST">[GMT+05:00] (IST) IST</option>

                    <option value="Asia/Katmandu">[GMT+05:00] (NPT) Asia/Katmandu</option>

                    <option value="Antarctica/Mawson">[GMT+06:00] (MAWT) Antarctica/Mawson</option>

                    <option value="Asia/Almaty">[GMT+06:00] (ALMT) Asia/Almaty</option>

                    <option value="Asia/Colombo">[GMT+06:00] (LKT) Asia/Colombo</option>

                    <option value="Asia/Dacca">[GMT+06:00] (BDT) Asia/Dacca</option>

                    <option value="Asia/Dhaka">[GMT+06:00] (BDT) Asia/Dhaka</option>

                    <option value="Asia/Novosibirsk">[GMT+06:00] (NOVT) Asia/Novosibirsk</option>

                    <option value="Asia/Thimbu">[GMT+06:00] (BTT) Asia/Thimbu</option>

                    <option value="Asia/Thimphu">[GMT+06:00] (BTT) Asia/Thimphu</option>

                    <option value="BST">[GMT+06:00] (BDT) BST</option>

                    <option value="Asia/Rangoon">[GMT+06:00] (MMT) Asia/Rangoon</option>

                    <option value="Indian/Cocos">[GMT+06:00] (CCT) Indian/Cocos</option>

                    <option value="Asia/Bangkok">[GMT+07:00] (ICT) Asia/Bangkok</option>

                    <option value="Asia/Jakarta">[GMT+07:00] (JAVT) Asia/Jakarta</option>

                    <option value="Asia/Krasnoyarsk">[GMT+07:00] (KRAT) Asia/Krasnoyarsk</option>

                    <option value="Asia/Phnom_Penh">[GMT+07:00] (ICT) Asia/Phnom_Penh</option>

                    <option value="Asia/Saigon">[GMT+07:00] (ICT) Asia/Saigon</option>

                    <option value="Asia/Vientiane">[GMT+07:00] (ICT) Asia/Vientiane</option>

                    <option value="Indian/Christmas">[GMT+07:00] (CXT) Indian/Christmas</option>

                    <option value="VST">[GMT+07:00] (ICT) VST</option>

                    <option value="Antarctica/Casey">[GMT+08:00] (WST) Antarctica/Casey</option>

                    <option value="Asia/Brunei">[GMT+08:00] (BNT) Asia/Brunei</option>

                    <option value="Asia/Hong_Kong">[GMT+08:00] (HKT) Asia/Hong_Kong</option>

                    <option value="Asia/Irkutsk">[GMT+08:00] (IRKT) Asia/Irkutsk</option>

                    <option value="Asia/Kuala_Lumpur">[GMT+08:00] (MYT) Asia/Kuala_Lumpur</option>

                    <option value="Asia/Macao">[GMT+08:00] (CST) Asia/Macao</option>

                    <option value="Asia/Manila">[GMT+08:00] (PHT) Asia/Manila</option>

                    <option value="Asia/Shanghai">[GMT+08:00] (CST) Asia/Shanghai</option>

                    <option value="Asia/Singapore">[GMT+08:00] (SGT) Asia/Singapore</option>

                    <option value="Asia/Taipei">[GMT+08:00] (CST) Asia/Taipei</option>

                    <option value="Asia/Ujung_Pandang">[GMT+08:00] (BORT) Asia/Ujung_Pandang</option>

                    <option value="Asia/Ulaanbaatar">[GMT+08:00] (ULAT) Asia/Ulaanbaatar</option>

                    <option value="Asia/Ulan_Bator">[GMT+08:00] (ULAT) Asia/Ulan_Bator</option>

                    <option value="Australia/Perth">[GMT+08:00] (WST) Australia/Perth</option>

                    <option value="CTT">[GMT+08:00] (CST) CTT</option>

                    <option value="Asia/Jayapura">[GMT+09:00] (JAYT) Asia/Jayapura</option>

                    <option value="Asia/Pyongyang">[GMT+09:00] (KST) Asia/Pyongyang</option>

                    <option value="Asia/Seoul">[GMT+09:00] (KST) Asia/Seoul</option>

                    <option value="Asia/Tokyo">[GMT+09:00] (JST) Asia/Tokyo</option>

                    <option value="Asia/Yakutsk">[GMT+09:00] (YAKT) Asia/Yakutsk</option>

                    <option value="JST">[GMT+09:00] (JST) JST</option>

                    <option value="Pacific/Palau">[GMT+09:00] (PWT) Pacific/Palau</option>

                    <option value="ACT">[GMT+09:00] (CST) ACT</option>

                    <option value="Australia/Adelaide">[GMT+09:00] (CST) Australia/Adelaide</option>

                    <option value="Australia/Broken_Hill">[GMT+09:00] (CST) Australia/Broken_Hill</option>

                    <option value="Australia/Darwin">[GMT+09:00] (CST) Australia/Darwin</option>

                    <option value="AET">[GMT+10:00] (EST) AET</option>

                    <option value="Antarctica/DumontDUrville">[GMT+10:00] (DDUT) Antarctica/DumontDUrville</option>

                    <option value="Asia/Vladivostok">[GMT+10:00] (VLAT) Asia/Vladivostok</option>

                    <option value="Australia/Brisbane">[GMT+10:00] (EST) Australia/Brisbane</option>

                    <option value="Australia/Hobart">[GMT+10:00] (EST) Australia/Hobart</option>

                    <option value="Australia/Sydney">[GMT+10:00] (EST) Australia/Sydney</option>

                    <option value="Pacific/Guam">[GMT+10:00] (ChST) Pacific/Guam</option>

                    <option value="Pacific/Port_Moresby">[GMT+10:00] (PGT) Pacific/Port_Moresby</option>

                    <option value="Pacific/Saipan">[GMT+10:00] (ChST) Pacific/Saipan</option>

                    <option value="Pacific/Truk">[GMT+10:00] (TRUT) Pacific/Truk</option>

                    <option value="Australia/Lord_Howe">[GMT+10:00] (LHST) Australia/Lord_Howe</option>

                    <option value="Asia/Magadan">[GMT+11:00] (MAGT) Asia/Magadan</option>

                    <option value="Pacific/Efate">[GMT+11:00] (VUT) Pacific/Efate</option>

                    <option value="Pacific/Guadalcanal">[GMT+11:00] (SBT) Pacific/Guadalcanal</option>

                    <option value="Pacific/Kosrae">[GMT+11:00] (KOST) Pacific/Kosrae</option>

                    <option value="Pacific/Noumea">[GMT+11:00] (NCT) Pacific/Noumea</option>

                    <option value="Pacific/Ponape">[GMT+11:00] (PONT) Pacific/Ponape</option>

                    <option value="SST">[GMT+11:00] (SBT) SST</option>

                    <option value="Pacific/Norfolk">[GMT+11:00] (NFT) Pacific/Norfolk</option>

                    <option value="Antarctica/McMurdo">[GMT+12:00] (NZST) Antarctica/McMurdo</option>

                    <option value="Asia/Anadyr">[GMT+12:00] (ANAT) Asia/Anadyr</option>

                    <option value="Asia/Kamchatka">[GMT+12:00] (PETT) Asia/Kamchatka</option>

                    <option value="NST">[GMT+12:00] (NZST) NST</option>

                    <option value="Pacific/Auckland">[GMT+12:00] (NZST) Pacific/Auckland</option>

                    <option value="Pacific/Fiji">[GMT+12:00] (FJT) Pacific/Fiji</option>

                    <option value="Pacific/Funafuti">[GMT+12:00] (TVT) Pacific/Funafuti</option>

                    <option value="Pacific/Majuro">[GMT+12:00] (MHT) Pacific/Majuro</option>

                    <option value="Pacific/Nauru">[GMT+12:00] (NRT) Pacific/Nauru</option>

                    <option value="Pacific/Tarawa">[GMT+12:00] (GILT) Pacific/Tarawa</option>

                    <option value="Pacific/Wake">[GMT+12:00] (WAKT) Pacific/Wake</option>

                    <option value="Pacific/Wallis">[GMT+12:00] (WFT) Pacific/Wallis</option>

                    <option value="Pacific/Chatham">[GMT+12:00] (CHAST) Pacific/Chatham</option>

                    <option value="Pacific/Enderbury">[GMT+13:00] (PHOT) Pacific/Enderbury</option>

                    <option value="Pacific/Tongatapu">[GMT+13:00] (TOT) Pacific/Tongatapu</option>

                    <option value="Pacific/Kiritimati">[GMT+14:00] (LINT) Pacific/Kiritimati</option>

                  </html:select>

                </td>

              </tr>
              <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            
                <td valign="top"><div align="right"><bean:message key="txt.WorkFlowAcl" /></div></td>
  
                  <td>
                    <html:select name="userNewEditForm" property="txtWorkFlowAcl" size="4" multiple="true" styleClass="borderClrLvl_2 componentStyle componentStyleDisable" style="width:155px" >
                      <logic:present name="userNewEditForm" property="txtWorkFlowAcl">
                        <html:options name="userNewEditForm" property="txtWorkFlowAcl" />
                      </logic:present>
                    </html:select>
                    <html:button property="btnWorkFlowAcl" styleClass="buttons" style="width:20px;height:17px;" value="..." onclick="return lookUp();" tabindex="19" />
                    <html:button property="btnWorkFlowAclRem" styleClass="buttons" style="width:12px;height:17px;" value="X" onclick="removeWfFromList(this.form)" />
                   
                  </td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
              <tr>
            </table>

          </fieldset>

        </td>

      </tr>

      <tr>

        <td align="center" height="25px">

        <div align="right">

          <html:button property="btnSave" styleClass="buttons" style="width:70px" onclick="return callPage(this.form)"  tabindex="24"><bean:message key="btn.Save" /></html:button>

          <html:button property="btnCancel" styleClass="buttons" style="width:70px; margin-right:10px" onclick="history.go(-1);" tabindex="25"><bean:message key="btn.Cancel" /></html:button>

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

      <table class="imgStatusBar borderClrLvl_2 bgClrLvl_4 " width="990px" height="20px" border="0" cellpadding="0" cellspacing="0" id="statusBar">

        <tr>

          <td width="30px"><div class="imgStatusMsg"></div></td>

          <td>

              <html:messages id="error">

                <font color="red"><bean:write name="error"/></font>

              </html:messages>

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

