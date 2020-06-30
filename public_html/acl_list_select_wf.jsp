<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="aclselects" name="aclselects" type="java.util.ArrayList" />  

<bean:define id="pageCount" name="AclListSelectWfForm" property="txtPageCount" />        

<bean:define id="pageNo" name="AclListSelectWfForm" property="txtPageNo" />        

<%

request.setAttribute("topic","security_introduction_html");

//Variable declaration

boolean firstTableRow;

firstTableRow = true;

String control=(String)request.getAttribute("control");

%>



<html:html>

<head>

<title><bean:message key="title.Select.WorkFlow" /></title>

<jsp:include page="/style_sheet_include.jsp" />

<script src="general.js" ></script>

<script src="navigationbar.js" ></script>

<script>

  var navBar=new NavigationBar("navBar");

  navBar.setPageNumber(<%=pageNo%>);

  navBar.setPageCount(<%=pageCount%>);

  navBar.onclick="callSearch('acl_select_workflow')";



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

<html:javascript formName="aclListSelectWfForm" dynamicJavascript="true" staticJavascript="false"/>

<script language="Javascript1.1" src="staticJavascript.jsp"></script>

<script language="Javascript1.1" >

function cancelActionPerformed(){
  window.close();
}
    
function okActionPerformed(thisForm){
  var control=null;
  var selectedValue=null;
  var exists = false;
  if (typeof thisForm.chkSelect!="undefined"){
    if (typeof thisForm.chkSelect.length !="undefined"){
      for(index = 0 ; index < thisForm.chkSelect.length ;index++){
        exists = false;
        if(thisForm.chkSelect[index].checked){   
          selectedValue = thisForm.chkSelect[index].value;
          control=eval("opener.document.forms[0]." +"<%=control%>");
          if (typeof control!="undefined"){
            if((control.type=="select-multiple")||(control.type=="select")){
              for(var openerIndex=0;openerIndex<control.options.length; openerIndex++){
                if(control.options[openerIndex].value==selectedValue){
                  exists = true;            
                }                     
              }
              if( exists!=true ){
                var opt=opener.document.createElement("OPTION");
                opt.text=selectedValue;
                opt.value=selectedValue;
                var newIndex=control.options.length ;
                control.options[newIndex]=opt;
              }
            }
          }          
        }
      }
    }else{
      if(thisForm.chkSelect.checked){   
        selectedValue = thisForm.chkSelect.value;
        control=eval("opener.document.forms[0]." +"<%=control%>");
        if (typeof control!="undefined"){
          if((control.type=="select-multiple")||(control.type=="select")){
            for(var openerIndex=0;openerIndex<control.options.length; openerIndex++){
              if(control.options[openerIndex].value==selectedValue){
                exists = true;
              }   
            }
            if( exists!=true ){
              var opt=opener.document.createElement("OPTION");
              opt.text=selectedValue;
              opt.value=selectedValue;
              var newIndex=control.options.length ;
              control.options[newIndex]=opt;
            }
          }
        }          
      }
    }
    window.close();
    return true;
  }else{
    alert('<bean:message key="errors.radSelect.noitem"/>');         
    return false;
  }      
  alert('<bean:message key="errors.radSelect.required"/>');         
  return false;
}

function callSearch(relayOperation){  
  var thisForm=document.forms[0];
//thisForm.operation.value=relayOperation+thisForm.systemAclParameter.value;
  thisForm.action="relayAction.do?operation="+relayOperation+thisForm.systemAclParameter.value;
  thisForm.txtPageNo.value=navBar.getPageNumber();
  thisForm.submit() ;
}

function enter(thisField,e,relayOperation){
  var i;
  i=handleEnter(thisField,e);
 	if (i==1) {
    return callSearch(relayOperation);
  }
}
//UnSelect All
function unCheckChkAll(me){
  var thisForm=me.form;
  var totalCount =thisForm.chkSelect.length;
  var checkValue = (me.checked)?true:false;
  var isAllSelected=false;
  if(checkValue){        
    if (typeof totalCount != "undefined") {
      for (var count=0;count<totalCount;count++) {
        if(thisForm.chkSelect[count].checked){
          isAllSelected=true;           
        }else{
          isAllSelected=false;
          break;
        }
      }      
    }else{
        if(thisForm.chkSelect.checked){
          isAllSelected=true;           
        }else{
          isAllSelected=false;
        }
    }        
  }
  thisForm.chkAll.checked=isAllSelected;
}
//Select All
function checkAll(menu){
  var thisForm = document.forms[0];
  var checkValue =menu?(thisForm.chkAll.checked=true):((thisForm.chkAll.checked)?true:false);
  if(typeof thisForm.chkSelect != "undefined"){
    var totalRows = thisForm.chkSelect.length;
    if (typeof totalRows != "undefined") {
      var count=0;                
      for (count=0;count<totalRows;count++) {
        thisForm.chkSelect[count].checked=checkValue;
      }
    }else{
        thisForm.chkSelect.checked=checkValue;
    }
  }
}

</script>

</head>

<body style="margin:15px">



<html:form name="aclListSelectWfForm" type="dms.web.actionforms.security.AclListSelectWfForm" action="/relayAction" focus="txtSearchByAclName">    

  <html:hidden property="control" value="<%=control%>" />

  <html:hidden property="operation" />

  <html:hidden name="AclListSelectWfForm" property="txtPageNo" /> 

  <html:hidden name="AclListSelectWfForm" property="systemAclParameter" /> 



<table id="tabParent" align="center"  width="580px"  border="0" cellpadding="0" cellspacing="0" >

  <tr>

    <td>

      <table width="180px" border="0" cellpadding="0" cellspacing="0" id="tab">

        <tr>

          <td width="5px" class="imgTabLeft"></td>

          <td width="170px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.SelectAndSearchWorkFlow" /></div></td>

          <td width="5px" class="imgTabRight"></td>

        </tr>

      </table>

    </td>

  </tr>

</table>

<table width="580px" border="0" align="center" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2" id="borderClrLvl_2">

  <tr>

    <td>

    <table width="97%" border="0" align="center" cellpadding="0" cellspacing="0" id="innerContainer">

      <tr>

        <td>

          <table width="100%" border="0" align="center">

            <tr>

              <td colspan="2" height="10px"></td>

            </tr>

            <tr>

              <td>
              
                <%if(!control.equals("txtWorkFlowAcl")){ %>
                
                  <span style="float:left"><bean:message key="lbl.SearchByAclName" />
                  
                    <html:text name="AclListSelectWfForm" property="txtSearchByAclName" styleClass="borderClrLvl_2 componentStyle" style="width:200px" maxlength="20" tabindex="2" onkeypress="return enter(this,event,'search_acl_select');" titleKey="lbl.YouCanUseWildCard" />
                    
                  </span>

                  <a onclick="return callSearch('search_acl_select')" class="imgGo" title='<bean:message key="btn.Go" />' tabindex="3"></a>
                  
              </td>

            </tr>
      
            <tr>
      
                <td colspan="2"><bean:message key="lst.SearchInfo" /></td>
      
            </tr>
                  
                  <%}else{%>
                  
                    <html:hidden name="AclListSelectWfForm" property="txtSearchByAclName" styleClass="borderClrLvl_2 componentStyle" style="width:200px"  onkeypress="return enter(this,event,'search_acl_select');" titleKey="lbl.YouCanUseWildCard" value="wf" />
                      
                </td>

            </tr>
          
            <tr>
          
                        
          
            </tr>
                  
                  <%}%>
                
              

          </table>

        </td>

      </tr>

      <tr>

        <td height="10px"></td>

      </tr>

      <tr> 

        <td align="center">

          <div class="borderClrLvl_2 bgClrLvl_4" style="overflow:auto; width:100%; height:324px">

            <table class="bgClrLvl_F" id="data" width="100%" border="0" cellpadding="0" cellspacing="1">

              <tr>

                <th width="8%" height="18px"><input type="checkbox" style="height:12px" onclick="checkAll()" name="chkAll" title="tooltips.SelectAll"></th>

                <th width="38%"><bean:message key="tbl.head.Name" /></th>

                <th width="38%"><bean:message key="tbl.head.Owner" /></th>

                <th width="16%"><bean:message key="tbl.head.CreateDate" /></th>

              </tr>

              <% if(aclselects.size()>0) {%>

              <logic:iterate name="aclselects" id="aclselect">

              <bean:define id="name" name="aclselect" property="aclName" scope="page" />

                <%if (firstTableRow == true){ firstTableRow = false; %>

                  <tr class="bgClrLvl_4">

                <%}else{ firstTableRow = true; %>

                  <tr class="bgClrLvl_3">                  

                <%}%>

                <td>

                  <div align="center">

                    <html:multibox property="chkSelect" value="<%=name%>" onclick="unCheckChkAll(this);" tabindex="1"/>

                  </div>

                </td>

                <td><bean:write name="aclselect" property="aclName" /></td>

                <td><bean:write name="aclselect" property="owner" /></td>

                <td><bean:write name="aclselect" property="createDate" /></td>

              </tr>

              </logic:iterate>

              <%}%>

              </table>

              <% if(aclselects.size()==0) {%>

                <div style="position:relative; top:125px; text-align:center;" class="tabText">

                  <bean:message key="info.no_item_found.no_item_found" />

                </div> 

              <%}%>

              </div>

            </td>

          </tr>

        <tr>

          <td height="5px"></td>

        </tr>

        <tr>

          <td align="center" height="30px"> 

            <div align="right"> 

              <html:button property="btnSelect" styleClass="buttons" style="width:70px" onclick="okActionPerformed(this.form)" tabindex="4"><bean:message key="btn.Select" /></html:button>  

              <html:button property="btnCancel" styleClass="buttons" style="width:70px" onclick="cancelActionPerformed()" tabindex="5"><bean:message key="btn.Cancel" /></html:button>

              <html:button property="btnHelp" styleClass="buttons" style="width:70px" onclick="openWindow('help?topic=security_introduction_html','Help',650,800,0,0,true);" tabindex="5"><bean:message key="btn.Help" /></html:button>

            </div>

          </td>

        </tr>	

      </table>

    </td>

  </tr>	

    </table>

 <!-- borderClrLvl_2 table ends above-->

<table border="0" cellpadding="0" cellspacing="0" >

  <tr><td height="2px"></td></tr>

</table>



<table align="center" class="imgStatusBar borderClrLvl_2 bgClrLvl_4 " width="580px" border="0" cellpadding="0" cellspacing="0" id="statusBar">

    <tr>

    <td width="30px"><div class="imgStatusMsg"></div></td>

    <td width="347px">

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

    <td width="3px">

      <div style="float:left; width:1px;height:22px;" class="bgClrLvl_2"></div>

      <div style="float:left; width:1px;height:22px;" class="bgClrLvl_F"></div>

    </td>

    <td width="200px" align="right">

      <script>navBar.render();</script>

    </td>

    </tr>

</table>

<!-- statusBar table ends above-->

</html:form>

</body>

</html:html>