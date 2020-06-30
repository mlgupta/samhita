<%@ page import="dms.web.beans.filesystem.*" %>

<%@ page import="dms.web.beans.user.*" %>

<%@ page import="java.util.*" %>



<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="docHistoryListForm" name="docHistoryListForm" type="dms.web.actionforms.filesystem.DocHistoryListForm" />

<bean:define id="documentHistoryDetails" name="documentHistoryDetails" type="ArrayList" />

<bean:define id="txtHistoryPageCount" name="docHistoryListForm" property="txtHistoryPageCount" />        

<bean:define id="txtHistoryPageNo" name="docHistoryListForm" property="txtHistoryPageNo" />

<bean:define id="imagepath" name="UserPreferences" property="treeIconPath" />

<%

    boolean firstTableRow = true;

    boolean isCheckedOut;

    

    if(((DocumentHistoryDetail)documentHistoryDetails.get(0)).getDocId() == null){

        isCheckedOut = true;

    }else{

        isCheckedOut = false;

    }

%> 

<html:html>

<head>

<title><bean:message key="title.DocumentHistory" /></title>

<jsp:include page="/style_sheet_include.jsp" />

<script src="general.js"></script>

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


<script language="JavaScript" type="text/JavaScript">



  var navBar=new NavigationBar("navBar");

  navBar.setPageNumber(<%=txtHistoryPageNo%>);

  navBar.setPageCount(<%=txtHistoryPageCount%>);

  navBar.onclick="historyNavgation()";



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



  function historyNavgation(){

    document.forms[0].action="docHistoryListAction.do";

    document.forms[0].txtHistoryPageNo.value=navBar.getPageNumber();

    document.forms[0].submit();

  }

  

  function initialize(){

      MM_preloadImages('<%=imagepath%>/butt_version_detail_over.gif','<%=imagepath%>/butt_version_delete_over.gif');

      if (typeof document.forms[0].radDocId[0] !="undefined") { 

          document.forms[0].radDocId[0].checked=true;

      }else{

          if (typeof document.forms[0].radDocId !="undefined") { 

              document.forms[0].radDocId.checked=true;

          }

      }

  }







  //Called when delete action is performed

  function docHistoryDelete(){

      var thisForm = document.forms[0];

      var execute = true;

      var isCheckedOut = <%=isCheckedOut%>

      if(!isCheckedOut){

          if (typeof document.forms[0].radDocId[0] !="undefined") { 

              if(document.forms[0].radDocId[0].checked == true){

                  execute = false;

                  alert("<bean:message key="msg.latestversion.deletedenied" />");

              }

          }else{

              if (typeof document.forms[0].radDocId !="undefined") { 

                  if(document.forms[0].radDocId.checked == true){

                      execute = false;

                      alert("<bean:message key="msg.latestversion.deletedenied" />");

                  }

              }

          }

      }else{

          if(document.forms[0].radDocId[0].checked == true ){

              execute = false;

              alert("<bean:message key="msg.latestversion.deletedenied" />");

          }



          if(document.forms[0].radDocId[1].checked == true ){

              execute = false;

              alert("<bean:message key="msg.latestversion.deletedenied" />");

          }

    

      }    

    

      if(execute){

          if (confirm("<bean:message key="msg.delete.confirm" />")){

              thisForm.target = "_self";

              thisForm.action = "docHistoryDeleteAction.do";

              thisForm.submit();

          }

      }

  }



  //Called when rollback action is performed

  function docHistoryRollback(){

      var thisForm = document.forms[0];

      if (confirm("<bean:message key="msg.rollback.confirm" />")){

          thisForm.target = "_self";

          thisForm.action = "docHistoryRollbackAction.do";

          thisForm.submit();

      }

  }



  function docHistoryDetail(){

      var thisForm = document.forms[0];

      openWindow("","DocHistoryDetail",330,375,0,0,true);

      thisForm.target = "DocHistoryDetail";

      thisForm.action = "docHistoryDetailAction.do";

      thisForm.submit();

  }



</script>



</head>



<body onload="initialize();">



<html:form action="/docHistoryListAction" >

<html:hidden property="hdnActionType" value="" />

<html:hidden property="chkFolderDocIds" value="<%=docHistoryListForm.getChkFolderDocIds()[0]%>" />

<html:hidden property="documentName" value="<%=docHistoryListForm.getDocumentName()%>" />

<html:hidden property="txtHistoryPageNo" value="<%=txtHistoryPageNo%>" /> 



<table id="tabParent" align="center"  width="500px"  border="0" cellpadding="0" cellspacing="0" >

  <tr>

    <td>

	<table width="160px" border="0" cellpadding="0" cellspacing="0" id="tab">

      <tr>

        <td width="5px" class="imgTabLeft"></td>

        <td width="150px" class="imgTabTile"><div align="center" class="tabText"><bean:message key="lbl.DocumentHistory" /></div></td>

        <td width="5px" class="imgTabRight"></td>

      </tr>

    </table>

	</td>

  </tr>

</table>

<table width="500px" border="0" align="center" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2" id="borderClrLvl_2">

  <tr>

    <td>

    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" id="innerContainer">

      <tr>

        <td>

          <table width="100%" border="0" cellspacing="0" cellpadding="1">

              <tr class="imgToolBar bgClrLvl_3"> 

                <td>

                    <a onclick="docHistoryDetail();" class="imgVersionDetail" onmouseout="this.className='imgVersionDetail'" onmouseover="this.className='imgVersionDetailOver'" style="margin-left:5px;" title="<bean:message key="tooltips.VersionDetail" /> "></a>

<!--                    <a onclick="docHistoryRollback();" class="imgVersionRollBack" onmouseout="this.className='imgVersionRollBack'" onmouseover="this.className='imgVersionRollBackOver'" title="<bean:message key="tooltips.VersionRollBack" />"></a> -->

                    <a onclick="docHistoryDelete();" class="imgVersionDelete" onmouseout="this.className='imgVersionDelete'" onmouseover="this.className='imgVersionDeleteOver'" title="<bean:message key="tooltips.VersionDelete" /> "></a>

                </td>

                <td align="right">

                  <bean:message key="lbl.Document" />&nbsp;:&nbsp;

                  <bean:write name="docHistoryListForm" property="documentName" />&nbsp;

                </td>

              </tr>

          </table>

        </td>

      </tr>    

      <tr>

        <td>

          <div class=" bgClrLvl_4" style="overflow:auto; width:100%; height:324px">

          <table class="bgClrLvl_F" id="data" width="100%" border="0" cellpadding="0" cellspacing="1">

            <tr> 

              <th width="8%" height="18px"><bean:message key="tbl.head.Select" /></th>

              <th width="10%"><bean:message key="tbl.head.VersionNo" /></th>

              <th width="37%"><bean:message key="tbl.head.User" /></th>

              <th width="31%"><bean:message key="tbl.head.VersionDate" /></th>

              <th width="14%"><bean:message key="tbl.head.Action" /></th>

            </tr>

                <logic:iterate id="documentHistoryDetail" name="documentHistoryDetails" type="dms.web.beans.filesystem.DocumentHistoryDetail" >

                <%if (firstTableRow == true){ firstTableRow = false; %>

                    <tr class="bgClrLvl_4">

                <%}else{ firstTableRow = true; %>

                    <tr class="bgClrLvl_3">                  

                <%}%>



                    <logic:empty name="documentHistoryDetail" property="docId" > 

                      <td align="center"><html:radio property="radDocId" value=""  /></td>

                      <td><bean:write name="documentHistoryDetail" property="versionNumber" /></td>

                    </logic:empty>

                    <logic:notEmpty name="documentHistoryDetail" property="docId" > 

                      <td align="center">

                          <bean:define  id="docId" name="documentHistoryDetail" property="docId" />

                          <html:radio property="radDocId" value="<%=docId%>"  />

                      </td>

                      <td>

                          <a target="newWindow" href="docDownloadAction.do?documentId=<bean:write name="documentHistoryDetail" property="docId" />" class="menu">

                              <bean:write name="documentHistoryDetail" property="versionNumber" />...

                          </a>

                      </td>

                    </logic:notEmpty>

                    <td><bean:write name="documentHistoryDetail" property="userName" /></td>

                    <td><bean:write name="documentHistoryDetail" property="versionDate" /></td>

                    <td><bean:write name="documentHistoryDetail" property="actionType" /></td>

                  </tr>

                </logic:iterate>

          </table>

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



<table align="center" class="imgStatusBar borderClrLvl_2 bgClrLvl_4 " width="500px" border="0" cellpadding="0" cellspacing="0" id="statusBar">

    <tr>

    <td><div class="imgStatusMsg"></div></td>

    <td width="50%">

        <html:messages id="actionMessage" message="true">

            <bean:write  name="actionMessage"/>

        </html:messages>



        <html:messages id="actionError">

            <font color="red"><bean:write  name="actionError"/></font>

        </html:messages>

    </td>

    <td>

      <div style="float:left; width:1px;height:22px;" class="bgClrLvl_2"></div>

      <div style="float:left; width:1px;height:22px;" class="bgClrLvl_F"></div>

    </td>

    <td  align="right">

    <script>navBar.render();</script>

    </td>

    </tr>

</table>

<!-- statusBar table ends above-->

<table align="center"  width="500px"  border="0" cellpadding="0" cellspacing="0" >

    <tr>

      <td align="right" height="25px" >

        <html:button property="btnClose" onclick="window.close()" styleClass="buttons" style="width:70px"  ><bean:message key="btn.Close" /></html:button>

        <html:button property="btnHelp" styleClass="buttons" style="width:70px" onclick="openWindow('help?topic=document_versioned_html','Help',650,800,0,0,true);" tabindex="5"><bean:message key="btn.Help" /></html:button>        

      </td>

    </tr>

</table>



</html:form>

</body>

</html:html>

