<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<bean:define id="userInfo" name="UserInfo" type="dms.web.beans.user.UserInfo"/>
<html:html> 
<head>
<title><bean:message key="title.Preview" /></title>
<link href="themes/general.css" rel="stylesheet" type="text/css" >
<%
  if (session.getAttribute("styleTagPlaceHolder")!=null){
    out.println(session.getAttribute("styleTagPlaceHolder"));
  }
%>
<script src="general.js"></script>
<script src="menu_functionality.js"></SCRIPT>
<script src="datepicker.js"></script>
<script src="timezones.js"></script>
<script language="Javascript1.1" >
   //Folder Document Menu 
		var folderDoc=gMenu("folderDoc",94,0,13,120);
		folderDoc.menuDelay=1000;
    folderDoc.className="dropMenu bgClrLvl_4 borderClrLvl_2";

    //File Main Menu		
		var menu1=insMainMenu(folderDoc,gMainMenu("menu1",folderDoc,"<bean:message key="mnu.File" />"));
		menu1.align="center";
    menu1.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu1.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //New Folder - File
		var menu11=insSubMenu(menu1, gSubMenu("menu11", menu1,"<bean:message key="mnu.File.NewFolder" />"));
		menu11.align="left"
    menu11.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu11.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //Apply ACL - File
		var menu12=insSubMenu(menu1, gSubMenu("menu12",menu1,"<bean:message key="mnu.File.ApplyACL" />"));
		menu12.align="left"
    menu12.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu12.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //Upload File - File
		var menu13=insSubMenu(menu1, gSubMenu("menu13",menu1,"<bean:message key="mnu.File.UploadFile" />"));
		menu13.align="left"
    menu13.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu13.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //Download File - File
		var menu14=insSubMenu(menu1, gSubMenu("menu14",menu1,"<bean:message key="mnu.File.DownloadFile" />"));
		menu14.align="left"
		menu14.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu14.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //Property - File
		var menu15=insSubMenu(menu1, gSubMenu("menu15",menu1,"<bean:message key="mnu.File.Property" />"));
		menu15.align="left"
    menu15.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu15.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    
    //Encrypt - File
    var menu16=insSubMenu(menu1, gSubMenu("menu16",menu1,"<bean:message key="mnu.File.Encrypt" />"));
		menu16.align="left"
    menu16.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu16.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //Decrypt - File
    var menu17=insSubMenu(menu1, gSubMenu("menu17",menu1,"<bean:message key="mnu.File.Decrypt" />"));
		menu17.align="left"
    menu17.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu17.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //Zip - File
    var menu18=insSubMenu(menu1, gSubMenu("menu18",menu1,"<bean:message key="mnu.File.Zip" />"));
		menu18.align="left"
    menu18.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu18.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //Unzip - File
    var menu19=insSubMenu(menu1, gSubMenu("menu19",menu1,"<bean:message key="mnu.File.Unzip" />"));
		menu19.align="left"
    menu19.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu19.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //Mail - File
    var menu110=insSubMenu(menu1, gSubMenu("menu110",menu1,"<bean:message key="mnu.File.Mail" />"));
		menu110.align="left"
    menu110.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu110.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //Fax - File
    var menu111=insSubMenu(menu1, gSubMenu("menu111",menu1,"<bean:message key="mnu.File.Fax" />"));
		menu111.align="left"
    menu111.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu111.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";   
    
    //Generate file link -File
    var menu112=insSubMenu(menu1, gSubMenu("menu112",menu1,"<bean:message key="mnu.File.GenerateLinks(s)" />"));
		menu112.align="left"
    menu112.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu112.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";        
  
    //View Document Log - File
    var menu113=insSubMenu(menu1, gSubMenu("menu113",menu1,"<bean:message key="mnu.File.ViewDocLog" />"));
    menu113.align="left";
		menu113.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu113.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";



    //Edit Main Menu		
		var menu2=insMainMenu(folderDoc,gMainMenu("menu2",folderDoc,"<bean:message key="mnu.Edit" />"));
		menu2.align="center";
    menu2.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu2.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //Rename - Edit
		var menu21=insSubMenu(menu2, gSubMenu("menu21",menu2,"<bean:message key="mnu.Edit.Rename" />"));
		menu21.align="left"
    menu21.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu21.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //Cut - Edit 
		var menu22=insSubMenu(menu2, gSubMenu("menu22",menu2,"<bean:message key="mnu.Edit.Cut" />"));
		menu22.align="left"
    menu22.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu22.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //Copy - Edit
		var menu23=insSubMenu(menu2, gSubMenu("menu23",menu2,"<bean:message key="mnu.Edit.Copy" />"));
		menu23.align="left"
    menu23.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu23.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";    

    //Paste - Edit
		var menu24=insSubMenu(menu2, gSubMenu("menu24",menu2,"<bean:message key="mnu.Edit.Paste" />"));
		menu24.align="left"
    menu24.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu24.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";    

    //Delete - Edit
		var menu25=insSubMenu(menu2, gSubMenu("menu25",menu2,"<bean:message key="mnu.Edit.Delete" />"));
		menu25.align="left"
    menu25.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu25.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";    

    // Copy To Folder - Edit
		var menu26=insSubMenu(menu2, gSubMenu("menu26",menu2,"<bean:message key="mnu.Edit.CopyToFolder" />"));
		menu26.align="left"
    menu26.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu26.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //Move To Folder - Edit
		var menu27=insSubMenu(menu2, gSubMenu("menu27",menu2,"<bean:message key="mnu.Edit.MoveToFolder" />"));
		menu27.align="left"
    menu27.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu27.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";    

    //Select All  - Edit
		var menu28=insSubMenu(menu2, gSubMenu("menu28",menu2,"<bean:message key="mnu.Edit.SelectAll" />"));
		menu28.align="left"
    menu28.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu28.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";    

    //Invert Selection - Edit 
		var menu29=insSubMenu(menu2, gSubMenu("menu29",menu2,"<bean:message key="mnu.Edit.InvertSelection" />"));
		menu29.align="left"
		menu29.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu29.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //Version Main Menu
		var menu3=insMainMenu(folderDoc,gMainMenu("menu3",folderDoc,"<bean:message key="mnu.Version" />"));
		menu3.align="center";
    menu3.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu3.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";

    //Make Versionable - Version
		var menu31=insSubMenu(menu3, gSubMenu("menu31",menu3,"<bean:message key="mnu.Version.MakeVersionable" />"));
		menu31.align="left"
    menu31.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu31.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";    

    //Checkout - Version
		var menu32=insSubMenu(menu3, gSubMenu("menu32",menu3,"<bean:message key="mnu.Version.CheckOut" />"));
		menu32.align="left"
    menu32.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu32.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";        

    //Checkin - Version
		var menu33=insSubMenu(menu3, gSubMenu("menu33",menu3,"<bean:message key="mnu.Version.CheckIn" />"));
		menu33.align="left"
    menu33.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu33.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";    

    //Undo Checkout - Version
		var menu34=insSubMenu(menu3, gSubMenu("menu34",menu3,"<bean:message key="mnu.Version.UndoCheckOut" />"));
		menu34.align="left"
    menu34.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu34.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";        

    //History - Version
		var menu35=insSubMenu(menu3, gSubMenu("menu35",menu3,"<bean:message key="mnu.Version.History" />"));
		menu35.align="left"
    menu35.mouseoutClassName="dropMenu bgClrLvl_4 borderClrLvl_2";
    menu35.mouseoverClassName="dropMenuover bgClrLvl_2 borderClrLvl_2";        


    var datePicker = new Calendar("datePicker",0,0);
    datePicker.tooltipCalendar='<bean:message key="tooltips.cal.Calendar" />';
    datePicker.tooltipCancel='<bean:message key="tooltips.cal.Cancel" />';
    datePicker.tooltipClear='<bean:message key="tooltips.cal.Clear" />';
    datePicker.tooltipDay='<bean:message key="tooltips.cal.Day" />';
    datePicker.tooltipHour='<bean:message key="tooltips.cal.Hour" />';
    datePicker.tooltipMinute='<bean:message key="tooltips.cal.Minute" />';
    datePicker.tooltipNextMonth='<bean:message key="tooltips.cal.NextMonth" />';
    datePicker.tooltipNextYear='<bean:message key="tooltips.cal.NextYear" />';
    datePicker.tooltipNow='<bean:message key="tooltips.cal.Now" />';
    datePicker.tooltipOk='<bean:message key="tooltips.cal.Ok" />';
    datePicker.tooltipPrevMonth='<bean:message key="tooltips.cal.PrevMonth" />';
    datePicker.tooltipPrevYear='<bean:message key="tooltips.cal.PrevYear" />';
    datePicker.tooltipSecond='<bean:message key="tooltips.cal.Second" />';
    datePicker.tooltipTimezone='<bean:message key="tooltips.cal.Timezone" />';

    var fromDatePicker = new Calendar("fromDatePicker",0,0);
    fromDatePicker.noTimezone=true; 
    fromDatePicker.noTime=true;
    fromDatePicker.tooltipCalendar='<bean:message key="tooltips.cal.Calendar" />';
    fromDatePicker.tooltipCancel='<bean:message key="tooltips.cal.Cancel" />';
    fromDatePicker.tooltipClear='<bean:message key="tooltips.cal.Clear" />';
    fromDatePicker.tooltipDay='<bean:message key="tooltips.cal.Day" />';
    fromDatePicker.tooltipHour='<bean:message key="tooltips.cal.Hour" />';
    fromDatePicker.tooltipMinute='<bean:message key="tooltips.cal.Minute" />';
    fromDatePicker.tooltipNextMonth='<bean:message key="tooltips.cal.NextMonth" />';
    fromDatePicker.tooltipNextYear='<bean:message key="tooltips.cal.NextYear" />';
    fromDatePicker.tooltipNow='<bean:message key="tooltips.cal.Now" />';
    fromDatePicker.tooltipOk='<bean:message key="tooltips.cal.Ok" />';
    fromDatePicker.tooltipPrevMonth='<bean:message key="tooltips.cal.PrevMonth" />';
    fromDatePicker.tooltipPrevYear='<bean:message key="tooltips.cal.PrevYear" />';
    fromDatePicker.tooltipSecond='<bean:message key="tooltips.cal.Second" />';
    fromDatePicker.tooltipTimezone='<bean:message key="tooltips.cal.Timezone" />';

    var toDatePicker = new Calendar("toDatePicker",0,0);
    toDatePicker.noTimezone=true; 
    toDatePicker.noTime=true;
    toDatePicker.tooltipCalendar='<bean:message key="tooltips.cal.Calendar" />';
    toDatePicker.tooltipCancel='<bean:message key="tooltips.cal.Cancel" />';
    toDatePicker.tooltipClear='<bean:message key="tooltips.cal.Clear" />';
    toDatePicker.tooltipDay='<bean:message key="tooltips.cal.Day" />';
    toDatePicker.tooltipHour='<bean:message key="tooltips.cal.Hour" />';
    toDatePicker.tooltipMinute='<bean:message key="tooltips.cal.Minute" />';
    toDatePicker.tooltipNextMonth='<bean:message key="tooltips.cal.NextMonth" />';
    toDatePicker.tooltipNextYear='<bean:message key="tooltips.cal.NextYear" />';
    toDatePicker.tooltipNow='<bean:message key="tooltips.cal.Now" />';
    toDatePicker.tooltipOk='<bean:message key="tooltips.cal.Ok" />';
    toDatePicker.tooltipPrevMonth='<bean:message key="tooltips.cal.PrevMonth" />';
    toDatePicker.tooltipPrevYear='<bean:message key="tooltips.cal.PrevYear" />';
    toDatePicker.tooltipSecond='<bean:message key="tooltips.cal.Second" />';
    toDatePicker.tooltipTimezone='<bean:message key="tooltips.cal.Timezone" />';

    function window_onload(){
    var currentDate=new Date();
    datePicker.setDateTime(currentDate.getYear(),currentDate.getMonth()+1,currentDate.getDate(),
                            currentDate.getHours(),currentDate.getMinutes(),currentDate.getSeconds());
    datePicker.initialize();
    datePicker.click();

    fromDatePicker.setDateTime(currentDate.getYear(),currentDate.getMonth()+1,currentDate.getDate(),
                            currentDate.getHours(),currentDate.getMinutes(),currentDate.getSeconds());
    fromDatePicker.initialize();
    fromDatePicker.click();
    
    toDatePicker.setDateTime(currentDate.getYear(),currentDate.getMonth()+1,currentDate.getDate(),
                            currentDate.getHours(),currentDate.getMinutes(),currentDate.getSeconds());
    toDatePicker.initialize();
    toDatePicker.click();
  }

</script>
</head>
<body style="margin:0" onload="window_onload()">
<!-- This page contains 2 outermost tables, named 'HeaderInluder' and 'tabContainer' -->
<table id="headerIncluder" width="100%"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="95px">
      <!--Header Starts-->
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="7px"></td>
          <td width="182px">
            <table width="182px"  border="0" cellpadding="0" cellspacing="0">
              <tr > 
                <td height="11px"></td>
              </tr>  
              <tr>
                <td height="53px"><html:img src="themes/images/logo_trans.gif" width="182" height="53" styleClass="borderClrLvl_2"/></td>
              </tr>  
            </table>
          </td>
          <td>&nbsp;</td>
          <td width="182px">
            <table width="182px"  border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="20px">
                  <table width="182px" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="10px" height="20px" class="imgTopMenuCorner" ></td>
                      <td width="74px" class="imgTopMenuTile"><div align="center"><html:link href="#"><bean:message key="mnu.Preferences" /></html:link></div></td>
                      <td width="44px" class="imgTopMenuTile"><div align="center"><html:link href="#"><bean:message key="mnu.Help" /></html:link></div></td>
                      <td width="54px" class="imgTopMenuTile"><div align="center"><html:link href="#"><bean:message key="mnu.Logout" /></html:link></div></td>
                    </tr>  
                  </table>
                </td>
              </tr>
              <tr> 
                <td height="46px" align="right">
                  <span><bean:message key="lbl.WelcomeMsg" /><span> &nbsp;<span class="tabText"><bean:write name="UserInfo" property="userID" /><span>&nbsp;
                </td>
              </tr>
            </table>
          </td>
        </tr>  
      </table>
      <table width="100%"  border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td width="215px"class="imgMenuBar29" >&nbsp;</td>
          <td width="31px" class="imgMenuBarMotif"></td>
          <td>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
              <td height="8px" class="bgClrLvl_2"></td>
              </tr>
              <tr>
                <td  height="21px" class="imgMenuBar21" height="21px" align="right" >
                  <%if(userInfo.isSystemAdmin()){%>
                  <html:link href="#" styleClass="menu"><bean:message key="mnu.User" /></html:link><font size="2"> |</font>
                  <html:link href="#" styleClass="menu"><bean:message key="mnu.Group" /></html:link><font size="2"> |</font>
                  <html:link href="#" styleClass="menu"><bean:message key="mnu.Theme" /></html:link><font size="2"> |</font>    
                  <%}%>
                  <html:link href="#" styleClass="menu"><bean:message key="mnu.Security" /></html:link><font size="2"> |</font>
                  <html:link href="#" styleClass="menu"><bean:message key="mnu.Document" /></html:link><font size="2"> |</font>
                  <html:link href="#" styleClass="menu" ><bean:message key="mnu.Scheduler" /></html:link> <font size="2"> |</font>
                  <html:link styleClass="menu" href="#"><bean:message key="mnu.Mail" /></html:link> <font size="2"> |</font>
                  <html:link styleClass="menu" href="#"><bean:message key="mnu.Fax" /></html:link> <font size="2"> |</font>
                  <html:link styleClass="menu" href="#"><bean:message key="mnu.WorkFlow" /></html:link>&nbsp;
                </td>
              </tr>    
            </table>
          </td>
        </tr>
      </table>

      <!--Header Ends-->
	</td>
  </tr>
</table>

<table id="spacer" width="100%"  border="0" cellspacing="0" cellpadding="0" >
<!-- this table is for giving 14 px height bar behind the drop-down menu-->
  <tr><td height="14px" ></td></tr>
</table>
<!-- Begin Menu Generation-->    
<script>
folderDoc.renderMenu();
</script>
<!-- End Menu  Generation-->
<table id="toolBar" width="100%"  border="0" cellpadding="0" cellspacing="0" class="imgToolBar bgClrLvl_3">
	<tr><td height="1px" class="bgClrLvl_F"></td></tr>
	<tr>
		<td height="25px">
      <html:link href="#" styleClass="imgNewFolder" onmouseout="this.className='imgNewFolder'" onmouseover="this.className='imgNewFolderOver'" style="margin-left:5px;" titleKey="tooltips.FolderNew"></html:link> 
      <html:link href="#" styleClass="imgRename" onmouseout="this.className='imgRename'" onmouseover="this.className='imgRenameOver'" titleKey="tooltips.FolderRename" ></html:link>
      <html:link href="#" styleClass="imgUpload" onmouseout="this.className='imgUpload'" onmouseover="this.className='imgUploadOver'" titleKey="tooltips.DocumentUpload" ></html:link>
      <html:link href="#" styleClass="imgApplyACL" onmouseout="this.className='imgApplyACL'" onmouseover="this.className='imgApplyACLOver'" titleKey="tooltips.FolderDocApplyAcl" ></html:link>
      <html:link href="#" styleClass="imgProperty" onmouseout="this.className='imgProperty'" onmouseover="this.className='imgPropertyOver'" titleKey="tooltips.FolderDocProperty" ></html:link>
      <html:link href="#" styleClass="imgGenerateLink" onmouseout="this.className='imgGenerateLink'" onmouseover="this.className='imgGenerateLinkOver'" titleKey="tooltips.DocumentGenerateLink" ></html:link>
      <html:link href="#" styleClass="imgViewLog" onmouseout="this.className='imgViewLog'" onmouseover="this.className='imgViewLogOver'" titleKey="tooltips.DocumentViewLog" ></html:link>
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
      <html:link href="#" styleClass="imgCut" onmouseout="this.className='imgCut'" onmouseover="this.className='imgCutOver'" titleKey="tooltips.FolderDocCut"></html:link>
      <html:link href="#" styleClass="imgCopy" onmouseout="this.className='imgCopy'" onmouseover="this.className='imgCopyOver'" titleKey="tooltips.FolderDocCopy"></html:link>
      <html:link href="#" styleClass="imgPaste" onmouseout="this.className='imgPaste'" onmouseover="this.className='imgPasteOver'" titleKey="tooltips.FolderDocPaste"></html:link>
      <html:link href="#" styleClass="imgDeleteFolder" onmouseout="this.className='imgDeleteFolder'" onmouseover="this.className='imgDeleteFolderOver'" titleKey="tooltips.FolderDocDelete"></html:link>
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
      <html:link href="#" styleClass="imgCopyTo" onmouseout="this.className='imgCopyTo'" onmouseover="this.className='imgCopyToOver'" titleKey="tooltips.FolderDocCopyTo"></html:link>
      <html:link href="#" styleClass="imgMoveTo" onmouseout="this.className='imgMoveTo'" onmouseover="this.className='imgMoveToOver'" titleKey="tooltips.FolderDocMoveTo"></html:link>
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
      <html:link href="#" styleClass="imgVersionable" onmouseout="this.className='imgVersionable'" onmouseover="this.className='imgVersionableOver'" titleKey="tooltips.DocumentMakeVersionable" ></html:link>
      <html:link href="#" styleClass="imgChkOut" onmouseout="this.className='imgChkOut'" onmouseover="this.className='imgChkOutOver'" titleKey="tooltips.DocumentCheckOut" ></html:link>
      <html:link href="#" styleClass="imgChkIn" onmouseout="this.className='imgChkIn'" onmouseover="this.className='imgChkInOver'" titleKey="tooltips.DocumentCheckIn" ></html:link>
      <html:link href="#" styleClass="imgUndoChkOut" onmouseout="this.className='imgUndoChkOut'" onmouseover="this.className='imgUndoChkOutOver'" titleKey="tooltips.DocumentUndoCheckOut" ></html:link>
      <html:link href="#" styleClass="imgHistory" onmouseout="this.className='imgHistory'" onmouseover="this.className='imgHistoryOver'" titleKey="tooltips.DocumentHistory" ></html:link>
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
      <a  style="margin-left:3px" class="imgEncrypt" onmouseout="this.className='imgEncrypt'" onmouseover="this.className='imgEncryptOver'" title="<bean:message key="tooltips.EncryptDoc" />" ></a>
      <a  class="imgDecrypt" onmouseout="this.className='imgDecrypt'" onmouseover="this.className='imgDecryptOver'" title="<bean:message key="tooltips.DecryptDoc" />" ></a>          
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
      <a  style="margin-left:3px" class="imgZip" onmouseout="this.className='imgZip'" onmouseover="this.className='imgZipOver'" title="<bean:message key="tooltips.Zip" />" ></a>
      <a  class="imgUnZip" onmouseout="this.className='imgUnZip'" onmouseover="this.className='imgUnZipOver'" title="<bean:message key="tooltips.UnZip" />" ></a>          
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
      <a   style="margin-left:3px" class="imgMailTo" onmouseout="this.className='imgMailTo'" onmouseover="this.className='imgMailToOver'" title="<bean:message key="tooltips.MailTo" />" ></a>
      <a   class="imgFaxTo" onmouseout="this.className='imgFaxTo'" onmouseover="this.className='imgFaxToOver'" title="<bean:message key="tooltips.FaxTo" />" ></a>          
    </td>
	</tr>
	<tr><td height="1px" ></td></tr>
	<tr><td height="1px" class="bgCLrLvl_2"></td></tr>
</table>
<!--toolBar table ends above-->
<table id="addressBar" width="100%"  border="0" cellpadding="0" cellspacing="0" class="imgToolBar bgClrLvl_3">
  <tr><td colspan="7" height="1px" class="bgClrLvl_F"></td></tr>
  <tr >
		<td height="25px">
      <html:link href="#" styleClass="imgBack" onmouseout="this.className='imgBack'" onmouseover="this.className='imgBackOver'" style="margin-left:5px;" titleKey="tooltips.Back" ></html:link>
      <html:link href="#" styleClass="imgForward" onmouseout="this.className='imgForward'" onmouseover="this.className='imgForwardOver'" titleKey="tooltips.Forward" ></html:link>
      <html:link href="#" styleClass="imgGoUp" onmouseout="this.className='imgGoUp'" onmouseover="this.className='imgGoUpOver'" titleKey="tooltips.Up" ></html:link>
      <html:link href="#" styleClass="imgSearch" onclick="MM_findObj('treeMenuContainer').style.display==''? MM_showHideLayers('treeMenuContainer','','hide','searchContainer','','show') :MM_showHideLayers('treeMenuContainer','','show','searchContainer','','hide')" onmouseout="this.className='imgSearch'" onmouseover="this.className='imgSearchOver'" titleKey="tooltips.FolderDocSearch" ></html:link>
      <html:link href="#" styleClass="imgRefresh" onmouseout="this.className='imgRefresh'" onmouseover="this.className='imgRefreshOver'" titleKey="tooltips.Refresh" ></html:link>
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
      <div style="float:left; width:1px;height:100%;" class="bgClrLvl_F"></div>
      <div style="float:left; width:5px;height:100%;" ></div>
      <table>
        <tr>
          <td>
            <html:text property="txtAddress" readonly="true"  value="" styleClass="borderClrLvl_2 componentStyle" style="width:600px;float:left;" /> 
            <html:link href="#" styleClass="imgGo" titleKey="btn.Go"></html:link>
          </td>
        </tr>
      </table>
    </td>
  </tr>
	<tr><td height="1px" ></td></tr>
	<tr><td height="1px" class="bgClrLvl_2"></td></tr>
</table>
<!--addressBar table ends above-->

<table id="tabContainer" width="100%"  border="0" cellspacing="0" cellpadding="0">
<!-- this table is having 1 tr and 2 td. 1st td contains the divs and teh 2nd one contains tabParent-->
  <tr><td height="10px"></td></tr>
  <tr>
    <td width="245px" valign="top" >
      <div id="treeMenuContainer" style="position:relative; width:222px; left:5px; top:0px; display:'';">
        <table id="tabParentTree" width="222px" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td width="245px">
              <table id="tab" width="245px" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="5px" class="imgTabLeft"></td>
                  <td width="235px" class="imgTabTile"><div align="left" class="tabText"><bean:message key="lbl.Navigation" /></div></td>
                  <td width="5px" class="imgTabRight"></td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td width="245px" class="borderClrLvl_2 imgData bgClrLvl_4">
              <div id="treeview" style="position:relative;height:378px;width:220;left:0px;top:0px;overflow:auto;">
                <!-- Begin Tree View Generation-->    
                <iframe src="theme_preview_treeview.jsp" frameborder="0" style="display:''; overflow:auto; height:370px; width:100%">
                </iframe>
                <!-- End Tree View Generation-->
              </div>
            </td>
          </tr>
        </table>
      </div>
      <!--treeMenuContainer div ends above-->
      <div id="searchContainer" style="position:relative; width:222px; left:5px; top:0px; display:none">
        <table id="tabParentSearch" width="245px"  border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td>
              <table id="tab" width="245px" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="5px" class="imgTabLeft"></td>
                  <td width="235px" class="imgTabTile"><div align="left" class="tabText"><bean:message key="lbl.Search" /></div></td>
                  <td width="5px" class="imgTabRight"></td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td class="borderClrLvl_2 imgData bgClrLvl_4">
            <div style="position:relative; height:378px; width:243px; left:0px; top:0px; overflow:auto;">
              <table id="outermost" width="225px" border="0">
                <tr><td height="5px"></td></tr>
                <tr> 
                  <td>
                  <bean:message key="lbl.SearchforFoldersAndDocuments" />
                  <html:text property="txtFolderOrDocName" styleClass="borderClrLvl_2 componentStyle" readonly="true" value="" style="width:195px" /></br>
                  <bean:message key="lbl.YouCanUseWildCard" />
                  </td>
                </tr>
                <tr><td height="5px"></td></tr>
                <tr>
                  <td>
                    <bean:message key="lbl.ContainingText" />
                    <html:text property="txtContainingText" styleClass="borderClrLvl_2 componentStyle" readonly="true" value="" style="width:195px" /></br>        
                  </td>
                </tr>
                <tr><td height="5px"></td></tr>
                <tr>
                  <td>
                    <table border="0" cellpadding="0" cellspacing="1">
                      <tr>
                        <td>
                          <bean:message key="lbl.LookIn" />
                        <td>
                      </tr>
                      <tr>
                        <td>
                        <html:text  property="txtLookinFolderPath" styleClass="borderClrLvl_2 componentStyle" readonly="true" value="" style="width:173px" />
                        <html:button  property="btnLookup" styleClass="buttons" style="width:20px" >...</html:button>
                        <td>
                      </tr>
                    </table>                                    
                  </td>
                </tr>
                <tr><td height="5px"></td></tr>                
                <tr>
                  <td>
                    <div align="right">
                      <html:button property="btnSearch" styleClass="buttons" style="width:70px" ><bean:message key="btn.Search" /></html:button>
                      <html:button property="btnStop" styleClass="buttons" style="width:70px;margin-right:27px" ><bean:message key="btn.Stop" /></html:button>      
                    </div>
                  </td>
                </tr>
                <tr>
                  <td>
                    <a href="#" class="menu" onClick="MM_findObj('searchDrop').style.display==''? MM_showHideLayers('searchDrop','','hide') :MM_showHideLayers('searchDrop','','show')"><bean:message key="lbl.SearchOption" /></a>
                  </td>
                </tr>
                <tr>
                  <td>
                    <div id="searchDrop" style="display:none">
                      <table id="search" class="borderClrLvl_2" width="100%"  border="0" cellspacing="0" cellpadding="0">
                        <tr>
                          <td><input type="checkbox" id="chkDateSelected" name="chkDateSelected"   class="componentStyle" onclick="this.checked?MM_showHideLayers('dateDrop','','show'):MM_showHideLayers('dateDrop','','hide')" ><bean:message key="chk.Date" /></td>
                        </tr>
                        <tr>
                          <td>
                            <div id="dateDrop" style="display:none">
                              <table id="date" width="100%"  border="0">
                                <tr>
                                  <td>
                                    <div style="margin-left:33px">
                                      <select id="cobDateOption" name="cboDateOption"  class="borderClrLvl_2 componentStyle" style="width:100px">
                                        <option value=""><bean:message key="lbl.FilesModified" /></option>
                                        <option value=""><bean:message key="lbl.FilesCreated" /></option>                                
                                      </select>                
                                    </div>
                                  </td>
                                </tr>
                                <tr>
                                  <td>
                                    <table cellpadding="0" cellspacing="0">
                                      <tr>
                                      <td align="right" width="30px">
                                        <bean:message key="txt.FromDate" />&nbsp;
                                      <td>
                                      <td>
                                        <script>fromDatePicker.render()</script>
                                      <td>
                                      <tr>
                                    </table>  
                                  </td>
                                </tr>
                                <tr>
                                  <td>
                                    <table cellpadding="0" cellspacing="0">
                                      <tr>
                                      <td align="right" width="30px">
                                        <bean:message key="txt.ToDate" />&nbsp;
                                      <td>
                                      <td>
                                        <script>toDatePicker.render()</script>
                                      <td>
                                      <tr>
                                    </table>  
                                  </td>
                                </tr>
                              </table>
                              <!--date table ends-->
                            </div>
                          </td>
                        </tr>
                        <tr>
                          <td>
                            <input type="checkbox"  id="chkDocTypeSelected" name="chkDocTypeSelected"  class="componentStyle"  onclick="this.checked?MM_showHideLayers('typeDrop','','show'):MM_showHideLayers('typeDrop','','hide')" ><bean:message key="chk.Type" />
                          </td>
                        </tr>
                        <tr>
                          <td>
                            <div id="typeDrop" style="display:none;margin-left:33px;">
                              <select id="cboDocType" name="cboDocType" value="" class="borderClrLvl_2 componentStyle" style="width:100px">
                                <option value="0"><bean:message key="lbl.AllFilesAndFolders" /></option>
                              </select>                
                            </div>
                          </td>
                        </tr>
                        <tr>
                          <td>
                            <input type="checkbox"  id="chkSizeSelected" name="chkSizeSelected"  class="componentStyle" onclick="this.checked?MM_showHideLayers('sizeDrop','','show'):MM_showHideLayers('sizeDrop','','hide')" ><bean:message key="chk.Size" />
                          </td>
                          </tr>
                          <tr>
                            <td>
                              <div id="sizeDrop" style="display:none;margin-left:33px;">
                                <select  id="cboSizeOption" name="cboSizeOption" class="borderClrLvl_2 componentStyle" style="width:100px">
                                  <option value=""><bean:message key="lbl.AtLeast" /></option>
                                  <option value=""><bean:message key="lbl.AtMost" /></option>
                                </select>                
                                <html:text property="txtDocSize"  styleClass="borderClrLvl_2 componentStyle" value="" readonly="true" style="width:40px" /><bean:message key="lbl.KB" />
                              </div>
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <input type="checkbox"   id="chkAdvanceFeatureSelected" name="chkAdvanceFeatureSelected"  class="componentStyle" onclick="this.checked?MM_showHideLayers('advanceDrop','','show'):MM_showHideLayers('advanceDrop','','hide')" /><bean:message key="chk.AdvanceOption" />
                            </td>
                          </tr>
                          <tr>
                            <td>
                              <div id="advanceDrop" style="display:none">
                                <table id="advance" width="100%"  border="0">
                                  <tr>
                                    <td>&nbsp;
                                      <input type="checkbox"  id="chkSubFoldersSearch" name="chkSubFoldersSearch"  class="componentStyle" ><bean:message key="chk.SearchSubFolder" />                
                                    </td>
                                  </tr>
                                  <tr>
                                    <td>&nbsp;
                                      <input type="checkbox"  id="chkCaseSensitiveSearch" name="chkCaseSensitiveSearch" class="componentStyle" ><bean:message key="chk.CaseSensitive" />                
                                    </td>
                                  </tr>
                                </table>
                              <!-- advance table ends-->
                              </div>
                            </td>
                          </tr>
                        </table>
                        <!-- search table ends-->
                      </div>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </table>
    </div>
	<!--searchContainer div ends above-->
	</td>
  <td align="center" valign="top">
  
    <table id="tabParent" width="99%"   border="0" cellpadding="0" cellspacing="0" style="margin-left:10px">
      <tr>
        <td width="90px">
          <table id="tab"  border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="5px" class="imgTabLeft"></td>
              <td width="80px" class="imgTabTile">
              <div align="center" class="tabText"
              <a  onclick="MM_showHideLayers('data','','show','navBarLyr','','show','element','','hide');" class="menu" style="cursor:pointer;cursor:hand;">Data</a>
              </div></td>
              <td width="5px" class="imgTabRight"></td>
            </tr>
          </table>
        </td>
        <td>
          <table id="tab" border="0" cellpadding="0" cellspacing="0">
            <tr>
              <td width="5px" class="imgTabLeft"></td>
              <td width="80px" class="imgTabTile">
              <div align="center" class="tabText">
                <a onclick="MM_showHideLayers('element','','show','data','','hide','navBarLyr','','hide');" class="menu" style="cursor:pointer;cursor:hand;">Elements</a>
              </div></td>
              <td width="5px" class="imgTabRight"></td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
    <table width="99%" height="355px" border="0" cellpadding="0" cellspacing="0"  class="borderClrLvl_2 imgData bgClrLvl_4" id="borderClrLvl_2" style="margin-left:10px">
      <tr>
        <td align="center">
        <div id="data" style="width:100%; height:100%; display:''">
          <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr class="imgToolBar bgClrLvl_3">
              <td height="30px">
                <html:link  href="#"  styleClass="imgNewTheme" onmouseout="this.className='imgNewTheme'" onmouseover="this.className='imgNewThemeOver'" style="margin-left:5px;" titleKey="New" tabindex="1" ></html:link>
                <html:link  href="#" styleClass="imgEditTheme" onmouseout="this.className='imgEditTheme'" onmouseover="this.className='imgEditThemeOver'" titleKey="Edit" tabindex="2" ></html:link>
                <html:link  href="#" styleClass="imgDeleteTheme" onmouseout="this.className='imgDeleteTheme'" onmouseover="this.className='imgDeleteThemeOver'" titleKey="Delete" tabindex="3" ></html:link>
              </td>
              <td align="right">
                <html:link href="#"  style="float:right;margin-right:5px" styleClass="imgGo" titleKey="btn.Go" tabindex="5" ></html:link>
                <span style="float:right"><bean:message key="lbl.SearchByName" />
                  <html:text   property="txtSearchByThemeName" styleClass="borderClrLvl_2 componentStyle" style="width:200px" tabindex="4" value="" />
                </span>
             <td>
            </tr>
            <tr>
              <td colspan="2" align="center" >		
                <div class="imgData bgClrLvl_4" style="overflow:auto; width:100%; height:322px">
                  <table class="bgClrLvl_F" width="100%" border="0" align="right" cellspacing="1" cellpadding="0" id="datatable">
                    <tr>
                      <th align="center" width="7%" height="18px"><bean:message key="tbl.head.Select" /></th>
                      <th align="center" width="33%"><bean:message key="tbl.head.Name" /></th>
                      <th align="center" width="25%"><bean:message key="tbl.head.Column3" /></th>
                      <th align="center" width="20%"><bean:message key="tbl.head.Column4" /></th>
                      <th align="center" width="13%"><bean:message key="tbl.head.Column5" /></th>
                    </tr>
                    <tr  class="bgClrLvl_4">
                      <td align="center">
                          <input type=radio name="radSelect" property="radSelect" tabindex="7" value="" >
                      </td>
                      <td align="left"><bean:message key="theme.preview.Manish" /></td>
                      <td align="left"></td>
                      <td align="left"></td>
                      <td align="left"></td>
                    </tr>
                    <tr  class="bgClrLvl_3">
                      <td align="center">
                        <input type=radio name="radSelect" property="radSelect" tabindex="7" value="" >
                      </td>
                      <td align="left"><bean:message key="theme.preview.Rajan" /></td>
                      <td align="left"></td>
                      <td align="left"></td>
                      <td align="left"></td>
                    </tr>
                    <tr  class="bgClrLvl_4">
                      <td align="center">
                        <input type=radio name="radSelect" property="radSelect" tabindex="7" value="" >
                      </td>
                      <td align="left"><bean:message key="theme.preview.Panditji" /></td>
                      <td align="left"></td>
                      <td align="left"></td>
                      <td align="left"></td>
                    </tr>
                    <tr  class="bgClrLvl_3">
                      <td align="center">
                        <input type=radio name="radSelect" property="radSelect" tabindex="7" value="" >
                      </td>
                      <td align="left"><bean:message key="theme.preview.Dada" /></td>
                      <td align="left"></td>
                      <td align="left"></td>
                      <td align="left"></td>
                    </tr>
                    <tr  class="bgClrLvl_4">
                      <td align="center">
                        <input type=radio name="radSelect" property="radSelect" tabindex="7" value="" >
                      </td>
                      <td align="left"><bean:message key="theme.preview.Jeet" /></td>
                      <td align="left"></td>
                      <td align="left"></td>
                      <td align="left"></td>
                    </tr>                   
                    <tr  class="bgClrLvl_3">
                      <td align="center">
                        <input type=radio name="radSelect" property="radSelect" tabindex="7" value="" >
                      </td>
                      <td align="left"><bean:message key="theme.preview.Sudhi" /></td>
                      <td align="left"></td>
                      <td align="left"></td>
                      <td align="left"></td>
                    </tr>                    
                  </table>
                </div>
            </td>
          </tr>
        </table> 
        </div>
        <div id="element" style="width:100%; height:100%; display:none">
          <table width="623px" border="0">
            <tr><td height="20px"></td></tr>
            <tr>
              <td align="center" >
               <fieldset style="width:575px; height:140px">
                <legend align="left" class="tabText"><bean:message key="lbl.DataElements" /></legend>
                  <table width="90%"  border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td width="200px" >
                        <input readonly name="txtPreview" type="text" class="borderClrLvl_2 componentStyle" style="width:180px" />
                      </td>
                      <td  rowspan="4" width="200px">
                      <textarea readonly name="txaPreview" rows="3" style="width:180px; height:45px" class="borderClrLvl_2 componentStyle">
                        <bean:message key="theme.preview.ThisisTextArea" />
                        --------------------
                        --------------------
                        </textarea>
                        <select readonly name="lstPreview" size="3" style="width:180px;height:45px" class="borderClrLvl_2 componentStyle">
                          <option selected><bean:message key="theme.preview.ListItem1" /></option>
                          <option><bean:message key="theme.preview.ListItem2" /></option>
                          <option><bean:message key="theme.preview.ListItem3" /></option>
                        </select>
                      </td>
                      <td rowspan="4" >
                        <div style="overflow:auto;width:30px;height:90px" >
                          <div style="width:10px;height:120px" >
                          </div>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <td >
                        <select  readonly name="cboPreview" style="width:180px" class="borderClrLvl_2 componentStyle" >
                          <option selected><bean:message key="theme.preview.ComboItem1" /></option>
                          <option><bean:message key="theme.preview.ComboItem2" /></option>
                          <option><bean:message key="theme.preview.ComboItem3" /></option>
                        </select>
                      </td >
                    </tr>
                    <tr>
                      <td >
                        <input name="Submit" type="button" value="Submit" class="buttons" />
                        <input type="checkbox" name="chkPreview" value="checkbox" class="borderClrLvl_2 componentStyle" />
                        <input type="radio" name="radPreview" value="radio" class="borderClrLvl_2 componentStyle" />                       
                      </td >
                    </tr>
                    <tr>
                      <td >
                        <script>datePicker.render()</script>                            
                      </td>
                    </tr>
                  </table>
                </fieldset>
              </td>
            </tr>
            <tr><td height="20px"></td></tr>
            <tr>
              <td align="center" >
                <fieldset style="width:575px; height:80px">
                  <legend align="left" class="tabText"><bean:message key="lbl.TextElements" /></legend>
                  <table width="100%"  border="0">
                    <tr>
                      <td><div align="center"><bean:message key="lbl.Headings" /></div></td>
                    </tr>
                    <tr>
                      <td><div align="center"><bean:message key="lbl.BodyText" /></div></td>
                    </tr>
                    <tr>
                      <td><div align="center"><bean:message key="lbl.BoldText" /></div></td>
                    </tr>
                    <tr>
                      <td><div align="center"><bean:message key="lbl.ElementText" /></div></td>
                    </tr>
                  </table>
                </fieldset>
              </td>
            </tr>            
          </table>
        </div>
        </td>
      </tr>
    </table>
    <!-- borderClrLvl_2  table ends above-->
    <table border="0" cellpadding="0" cellspacing="0" ><tr><td height="2px"></td></tr></table>
      <table class="borderClrLvl_2 imgStatusBar bgClrLvl_4 " width="99%" border="0" cellpadding="0" cellspacing="0" id="statusBar" style="margin-left:10px">
        <tr>
        <td height="20px" ><div class="imgStatusMsg"></div></td>
        <td width="60%">
        <logic:present name="actionMessage">
          <html:messages id="actionMessage" message="true">
              <bean:write  name="actionMessage"/>
          </html:messages>
        </logic:present>
          
        <logic:present name="actionError">
          <html:messages id="actionError">
              <font color="red"> <bean:write  name="actionError"/></font>
          </html:messages>
        </logic:present>
        </td>
        <td align="right">
        <div id="navBarLyr" style="width:100%; height:100%; display:''">
          <table border="0"  height="100%" cellpadding="0" cellspacing="2" id="navBar">
            <tr>
            <td>
              <div style="float:left; width:1px;height:100%;" class="bgClrLvl_2"></div>
              <div style="width:1px;height:100%;" class="bgClrLvl_F"></div>
            </td>
            <td >
                <html:link href="#" styleClass="imgNavFirst" titleKey="tooltips.First" />
                <html:link href="#" styleClass="imgNavPrev"   titleKey="tooltips.Previous" />
            </td>
            <td >
            <bean:message key="lbl.Page"/>&nbsp;
            </td>
            <td >              
            <html:text property="txtPageNo" value="1" styleClass="borderClrLvl_2 componentStyle" style="align:center"  size="3"  titleKey="tooltips.PageNo" /> &nbsp;
            </td>
            <td >
            <bean:message key="lbl.Of"/> &nbsp;
            </td>
            <td >
            1&nbsp;
            </td>
            <td >
            <html:link href="#" styleClass="imgGo"  titleKey="btn.Go"></html:link>
            </td>
            <td >
              <html:link href="#" styleClass="imgNavNext" titleKey="tooltips.Next" />
              <html:link href="#" styleClass="imgNavLast" titleKey="tooltips.Last" />
            </td>
            </tr>
          </table>
          <!-- navBar table ends above-->
          </div>
        </td>
        </tr>
      </table>
      <!-- statusBar table ends above-->
  </td>
  </tr>
</table>
</body>
</html:html>