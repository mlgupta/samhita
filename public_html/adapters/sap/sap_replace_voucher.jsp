<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<jsp:include page="/style_sheet_include.jsp" />
<script src="progressbar.js"></script>
<script name="javascript">
// ProgressBar Definition
  varPGBar = new ProgessBar('varPGBar',120,10);
  varPGBar.pgPatelWidth=5;
  varPGBar.seperation=1;
  varPGBar.patelClassName='bgClrLvl_2';
</script>
<script name="javascript">

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

  function upload(){
    var thisForm = document.forms[0];
    var fileObj = MM_findObj('frmFile');
    var fileName = fileObj.value;
    if (fileName.length !=0){
      thisForm.submit();
      varPGBar.start();
      MM_findObj('waitmsg').style.display='';
    }else{
      alert('<bean:message key="msg.AddFile" />');
      return false;
    }
  }
</script>
<html:html>
<head>
<title><bean:message key="title.ReplaceVoucher" /></title>
</head>
<body topmargin="10px"; leftmargin="5px" >

<html:form action="/sapReplaceVoucherAction" enctype="multipart/form-data" name="sapReplaceVoucherForm" type="adapters.sap.actionforms.SAPReplaceVoucherForm" >
<html:hidden name="sapReplaceVoucherForm" property="voucherId" />
<html:hidden name="sapReplaceVoucherForm" property="txtDocumentId" />
<table id="tabContainer" align="center" width="100%"  border="0" cellspacing="0" cellpadding="0">
  <!-- This table contains 1 tr with 1 td containing tables, 'tabParent' and 'blueBorder' -->
  <tr>
    <td>
      <table id="tabParent" align="center" width="590px"  border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td>
            <table id="tab" width="150px" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="5px" class="imgTabLeft"></td>
                <td width="140px" align="center" class="imgTabTile">
                  <div class="tabText">
                    <bean:message key="lbl.UploadVouchers" />
                  </div>
                </td>
                <td width="5px" class="imgTabRight"></td>            
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <table id="borderClrLvl_2" align="center" width="590px"  border="0" cellpadding="0" cellspacing="0" class="imgData bgClrLvl_4 borderClrLvl_2"  >
        <tr>
          <td>
            <table width="95%"  align="left" border="0"  cellpadding="1" cellspacing="1">
              <tr>
                <td height="35px">&nbsp;
                
                </td>
                <td>     
                </td>
                </tr>
                <tr>
                  <td width="110" height="20px" align="right" valign="middle">
                    <bean:message key="txt.LocateFile" />:
                  </td>   
                  <td width="349">
                    <html:file name="sapReplaceVoucherForm" property="frmFile" size="48" styleClass="borderClrLvl_2" style="width:425px"><bean:message key="btn.Browse" />
                    </html:file>
                  </td>
                </tr>
                <tr>
                  <td height="20px" align="right" valign="top">Description:</td>
                  <td height="20px">
                    <html:textarea property="txaFileDesc" styleClass="borderClrLvl_2 componentStyle" style="width:423px;height:57px" />				
                  </td>
                </tr>
                <tr>
                  <td height="20px">&nbsp;</td>
                  <td height="20px"><bean:message key="msg.Note"/></td>
                </tr>
                <tr>
                  <td colspan="2" height="35px" align="right" valign="bottom">
                    <html:button property="btnUpload" onclick="return upload();" styleClass="buttons" style="width:70px" ><bean:message key="btn.Upload" /></html:button>
                    <html:button property="btnCancel" onclick="window.close();" styleClass="buttons" style="width:70px"><bean:message key="btn.Cancel" /></html:button>
                    <html:button property="btnHelp" styleClass="buttons" style="width:70px; margin-right:16px" onclick="openWindow('help?topic=document_upload_html','Help',650,800,0,0,true);" tabindex="5">    <bean:message key="btn.Help" /></html:button>
                  </td>
                </tr>
              <tr>
                <td colspan="2" height="5px" align="right" ></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

<table cellpadding="0" cellspacing="0">
  <tr><td height="2px"></td></tr>
</table>
    <!-- tabContainer ends here -->
<table align="center" class="borderClrLvl_2 imgStatusBar bgClrLvl_4 " width="590px" border="0" cellpadding="0" cellspacing="0" id="statusBar">
  <tr>
    <td height="20px" width="30px">
      <div class="imgStatusMsg"></div>
    </td>
    <td width="375px">
      <div id="waitmsg" style="display:none">
        <bean:message key="msg.processing" />
      </div>
    </td>
    <td  width="125px" align="right">
      <div style="margin-right:3px" >
        <script>
          varPGBar.renderPgBar();
        </script>
      </div>  
    </td>
  </tr>
</table>

<table cellpadding="0" cellspacing="0">
  <tr><td height="2px"></td></tr>
</table>
     <!-- statusBar table ends above-->
     <!-- noteTab ends here -->
</html:form>
</body>

</html:html>
