<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="groups" name="groups" type="java.util.ArrayList" /> 

<bean:define id="pageCount" name="GroupListSelectForm" property="txtPageCount" />        

<bean:define id="pageNo" name="GroupListSelectForm" property="txtPageNo" />        



<% 

//Variable declaration

boolean firstTableRow;

firstTableRow = true;

String control=(String)request.getAttribute("control");

%>

<html:html>

<head>

<title><bean:message key="title.GroupListSelect" /></title>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<jsp:include page="/style_sheet_include.jsp" />

<script src="general.js" ></script>

<script src="navigationbar.js" ></script>

<script>

  var navBar=new NavigationBar("navBar");

  navBar.setPageNumber(<%=pageNo%>);

  navBar.setPageCount(<%=pageCount%>);

  navBar.onclick="callSearch('page_group_select')";



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

<script language="Javascript1.1" >



function cancelActionPerformed(){

  window.close();

}

    

function okActionPerformed(form){

  var selection="<%=control%>";    

  if(selection.substring(0,3)=="lst"){

    var isSelected=0;

    var isAlreadyPresent=0;

    for(index = 1 ; index < form.chkSelect.length ;index++){

      if(form.chkSelect[index].checked){   

        isSelected=1;

        for(openerIndex=0;openerIndex<(eval("opener.document.forms[0]." + "<%=control%>")).options.length; openerIndex++){

          if((eval("opener.document.forms[0]." + "<%=control%>")).options[openerIndex].value==(form.chkSelect[index].value+" [G]")){                  

             isAlreadyPresent=1;

          }       

        }

        if(isAlreadyPresent==0){

          //var opt= new Option(form.chkSelect[index].value +" [G]",form.chkSelect[index].value +" [G]",false,false);

          var opt=opener.document.createElement("OPTION");

          opt.text=form.chkSelect[index].value +" [G]";

          opt.value=form.chkSelect[index].value +" [G]";

          var newIndex=(eval("opener.document.forms[0]." + "<%=control%>")).options.length ;

          (eval("opener.document.forms[0]." + "<%=control%>")).options[newIndex]=opt;

        }

        window.close();

      }

      isAlreadyPresent=0;

    }

    if(isSelected==0)

    alert("<bean:message key="errors.radSelect.required"/>");  

  }else if(selection.substring(0,3)=="txt"){

    var isSelected=0;

    var moreThanOneSelected=0;

    for(index = 1 ; index < form.chkSelect.length ;index++){

      if(form.chkSelect[index].checked){ 

        moreThanOneSelected=moreThanOneSelected+1;

      }

    }

    if(moreThanOneSelected>1){

      alert("<bean:message key="errors.radSelect.more"/>");

      return null;

    }

    for(index = 1 ; index < form.chkSelect.length ;index++){

      if(form.chkSelect[index].checked){ 

        isSelected=1;

        (eval("opener.document.forms[0]." + "<%=control%>")).value=form.chkSelect[index].value;

        window.close();

      }

    }

    if(isSelected==0)

    alert("<bean:message key="errors.radSelect.required"/>");    

  }

}  



function callSearch(relayOperation){  

  var thisForm=document.forms[0];

  thisForm.operation.value=relayOperation;

  thisForm.txtPageNo.value=navBar.getPageNumber();

  thisForm.submit() ;

}



function checkAll(thisForm){

  var checkValue = (thisForm.chkAll.checked)?true:false;

  var totalRows = thisForm.chkSelect.length;

  var count=0;                

  for (count=1;count<totalRows;count++) {

    thisForm.chkSelect[count].checked=checkValue;

  }

}



function unCheckChkAll(me){

  var thisForm=me.form;

  var totalCount =thisForm.chkSelect.length;

  var checkValue = (me.checked)?true:false;

  var isAllSelected=false;

  if(checkValue){        

    for (var count=1;count<totalCount;count++) {

      if(thisForm.chkSelect[count].checked){

        isAllSelected=true;           

      }else{

        isAllSelected=false;

        break;

      }

    }      

  }

  thisForm.chkAll.checked=isAllSelected;

}



  function enter(thisField,e,relayOperation){

    var i;

    i=handleEnter(thisField,e);

    if (i==1) {

      return callSearch(relayOperation);

    }

  }    

  

</script>



</head>

<body style="margin:15px">

<html:form name="groupListSelectForm" type="dms.web.actionforms.group.GroupListSelectForm" action="/relayAction" focus="txtSearchByGroupName" >

    <html:hidden property="control" value="<%=control%>" />

    <html:hidden property="operation" />

    <html:hidden name="GroupListSelectForm" property="txtPageNo" /> 



<table id="tabParent" align="center"  width="410px"  border="0" cellpadding="0" cellspacing="0" >

  <tr>

    <td>

      <table width="170px" border="0" cellpadding="0" cellspacing="0" id="tab">

        <tr>

          <td width="5px" class="imgTabLeft"></td>

          <td width="160px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lst.SearchSelectGroup" /></div></td>

          <td width="5px" class="imgTabRight"></td>

        </tr>

      </table>

    </td>

  </tr>

</table>

<table width="410px" border="0" align="center" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2" id="borderClrLvl_2">

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

            <span style="float:left"><bean:message key="txt.SearchByGroupName" />

            <html:text name="GroupListSelectForm" property="txtSearchByGroupName" styleClass="borderClrLvl_2 componentStyle" style="width:200px" tabindex="1" maxlength="20" onkeypress="return enter(this,event,'search_group_select');" titleKey="lbl.YouCanUseWildCard" /> 

            </span>

            <a onclick="return callSearch('search_group_select')" class="imgGo" title="<bean:message key="btn.Go" />" tabindex="2" ></a>

            </td>

          </tr>

          <tr>

            <td colspan="2"><bean:message key="lst.SearchInfo" /></td>

          </tr>

        </table>

      </td>

    </tr>

    <tr>

      <td height="10px"></td>

    </tr>

    <tr>

      <td>

        <div class="borderClrLvl_2 bgClrLvl_4" style="overflow:auto; width:100%; height:320px">

        <table class="bgClrLvl_F" width="100%" border="0" cellspacing="1" cellpadding="0">

            <tr> 

              <th width="4%"><input type=checkbox name="chkAll"  value="" style="height:12px" onclick="checkAll(this.form)" tabindex="3" /></th>

              <th width="68%"><bean:message key="tbl.head.Name" /></th>

            </tr>

            <html:multibox property="chkSelect" value=""  style="display:none" />

            <% if(groups.size()>0) {%>

              <logic:iterate name="groups" id="group" >

              <bean:define id="name" name="group" property="name" scope="page" />

               <%if (firstTableRow == true){ firstTableRow = false; %>

                    <tr class="bgClrLvl_4">

                  <%}else{ firstTableRow = true; %>

                <tr class="bgClrLvl_3">                  

                  <%}%>

                <td>

                  <div align="center"> 

                    <html:multibox property="chkSelect" value="<%=name%>" onclick="unCheckChkAll(this)" tabindex="4"/>

                  </div>

                </td>

                <td><bean:write name="group" property="name"/></td>

              </tr>

              </logic:iterate>

            <%}%>          

          </table>

          <% if(groups.size()==0) {%>

          <div style="position:relative; top:125px; text-align:center;" class="tabText">

            <bean:message key="info.no_item_found.no_item_found" />

          </div> 

          <%}%>

          </div>

          </td>

      </tr>

      <tr><td height="10px"></td></tr>

      <tr>

        <td align="center" height="30px"> 

          <div align="right"> 

            <html:button property="btnSelect" styleClass="buttons" style="width:70px" onclick="okActionPerformed(this.form)" tabindex="5" ><bean:message key="btn.Select" /></html:button>

            <html:button property="btnCancel" styleClass="buttons" style="width:70px" onclick="cancelActionPerformed()" tabindex="6" ><bean:message key="btn.Cancel" /></html:button>&nbsp;

            <html:button property="btnHelp" styleClass="buttons" style="width:70px" onclick="openWindow('help?topic=group_introduction_html','Help',650,800,0,0,true);" tabindex="5"><bean:message key="btn.Help" /></html:button>                                                                          

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



<table align="center" class="imgStatusBar borderClrLvl_2 bgClrLvl_4 " width="410px" border="0" cellpadding="0" cellspacing="0" id="statusBar">

  <tr>

    <td width="30px"><div class="imgStatusMsg"></div></td>

    <td width="177px">

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

    <td  width="200px" align="right">

      <script>navBar.render();</script>

    </td>

    </tr>

</table>

<!-- statusBar table ends above-->

</html:form>

</body>

</html:html>