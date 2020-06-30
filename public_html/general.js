/***********************Opening Modal and Modalless Widnows Starts **************************/
var winModalWindow
 
function openWindow(url,name,height,width,top,left,alignCenter,otherfeatures)
{
	if (alignCenter){
				top=(window.screen.availHeight/2)-(height/2);
				left=(window.screen.availWidth/2)-(width/2);
	 }  
    var features="width=" + width + "px,height=" + height +"px,";
		features=features + "left=" + left + "px," + "top=" + top + "px,";    
		features=features+otherfeatures;
   // alert(url);
    return window.open (url,name,features);

 }


function openModal(url, height,width,top,left,alignCenter)
{
	if (alignCenter){
				top=(window.screen.availHeight/2)-(height/2);
				left=(window.screen.availWidth/2)-(width/2);
	 }  
    
  if (window.showModalDialog)
  {
    var features="dialogWidth:" + width + "px;dialogHeight:" + height + "px;";
    features=features + "dialogLeft:" + left + "px;" + "dialogTop:" + top + "px;";
    window.showModalDialog(url,null,features)
  }
  else 
  {
    
    var features="dependent=yes,width=" + width + "px,height=" + height +"px,";
		features=features + "left=" + left + "px," + "top=" + top + "px,";    
    window.top.captureEvents (Event.CLICK|Event.FOCUS)
    window.top.onclick=false
    window.top.onfocus=handleFocus 
    winModalWindow = window.open (url,"ModalChild",features)
    winModalWindow.focus()
  }
}

 
function handleFocus()
{
  if (winModalWindow)
  {
    if (!winModalWindow.closed)
    {
      winModalWindow.focus()
    }
    else
    {
      window.top.releaseEvents (Event.CLICK|Event.FOCUS)
      window.top.onclick = ""
    }
  }
  return false
}

/***********************Opening Modal and Modalless Widnows Ends **************************/

/*****************Findobj and ShowHider Layers from Dream Weaver - Starts*****************/

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'':(v=='hide')?'none':v; }
    obj.display=v; }
}

/************** Mouse Over Image fethcing on Load Starts***********************************/
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}
/************** Mouse Over Image fethcing on Load Ends***********************************/

/*****************Findobj and ShowHider Layers from Dream Weaver - Ends*****************/


/*****************Trims a string - Starts*****************/
function trim(inputString) {
// Removes leading and trailing spaces from the passed string. Also removes
// consecutive spaces and replaces it with one space. If something besides
// a string is passed in (null, custom object, etc.) then return the input.
if (typeof inputString != "string") { return inputString; }

var retValue = inputString;
var ch = retValue.substring(0, 1);

while (ch == " ") { // Check for spaces at the beginning of the string
retValue = retValue.substring(1, retValue.length);
ch = retValue.substring(0, 1);
}

ch = retValue.substring(retValue.length-1, retValue.length);
while (ch == " ") { // Check for spaces at the end of the string
retValue = retValue.substring(0, retValue.length-1);
ch = retValue.substring(retValue.length-1, retValue.length);
}

return retValue; // Return the trimmed string back to the user
} // Ends the "trim" function 

/*****************Trims a string - Ends*****************/

/*Integer only text starts*/
function integerOnly(e){
	var key;
	if (window.event){
	   key = window.event.keyCode;
	}else if (e){
	   key = e.which;
	}else{
	   return true;
	}
	// control keys
	if ((key==null) || (key==0) || (key==8) || 
	    (key==9) || (key==13) || (key==27) ){
	   return true;
	// numbers
	}else if ((("0123456789").indexOf(String.fromCharCode(key)) > -1)){
			return true;
	}else{
	   return false;
	}
}

/********* Integer only text ends ********************************/

/*Decimal only text starts */
function decimalOnly(thisField, e, dec){
	var key;
	var keychar;

	if (window.event){
	   key = window.event.keyCode;
	}else if (e){
	   key = e.which;
	}else{
	   return true;
  }
	
	keychar = String.fromCharCode(key);

	// control keys
	if ((key==null) || (key==0) || (key==8) || 
	    (key==9) || (key==13) || (key==27) )
	   return true;

	// numbers
	else if ((("0123456789.").indexOf(keychar) > -1)){
		if (thisField.value.indexOf(".")>-1){
			if ((("0123456789").indexOf(keychar) > -1)){
				if ((thisField.value.length) >(thisField.value.indexOf(".")+ dec)) {
				 return false;
				}else{
				return true;
				}
			}else{
			 return false;
			}
		}else{
			return true;
		}
	}else{
	   return false;
	}
}
/************** Decimal only text ends ***********************************/
/* checks if the field entry is numeric only or not */
function isNumericOnly(thisVal){
    var numeric = thisVal;
    if( numeric.length == 0 )
      return false;
    for(var j=0; j<numeric.length; j++)	{
      var alphaa = numeric.charAt(j);
      var hh = alphaa.charCodeAt(0);
    //Checks the string passed as numeric
      if(!(hh >= 48 && hh<=57)) {
        return false;
      }
    }
    return true;
}
/* end isNumericOnly */
/************** Handles Enter Key ***********************************/
function handleEnter(thisField,e){
	var key;
  var keychar;
	if (window.event){
	   key = window.event.keyCode;
	}else if (e){
	   key = e.which;
	}else{
	   return true;
  }
	
	keychar = String.fromCharCode(key);
	// control keys
	if (key==13) {
    return 1;
    }
}
/************** Handles Enter Key ends***********************************/

/************** Checks whether the string passed is AlphaNumeric starts ***********************************/
function alphaNumeric(strPassword){
    var numeric = strPassword;
    var numberLoop=false;
    var alphaLoop=false;
    for(var j=0; j<numeric.length; j++)	{
      var alphaa = numeric.charAt(j);
      var hh = alphaa.charCodeAt(0);
    
    //Checks the first character of the string passed for integer
      if(j==0 && (hh > 47 && hh<59)){
        //when first character of the string passed is numeric returns false
        return false;  
      }
    
    //Checks the string passed as numeric
      if((hh > 47 && hh<59)) {
        numberLoop=true;
      }
    
    //Checks the string passed as alphabets
      if((hh > 64 && hh<91) || (hh > 96 && hh<123)) {
        alphaLoop=true;
      }
    }
    if (numberLoop==true && alphaLoop==true){
    }else{
      return false;
    }
  }
/************** Checks whether the string passed is AlphaNumeric ends ***********************************/

/*AlphaNumericOnly text starts*/
function alphaNumericOnly(e){
    if (window.event){
       key = window.event.keyCode;
    }else if (e){
       key = e.which;
    }else{
       return true;
    }	
    
    if((key > 47 && key<59) || (key > 64 && key<91) || (key > 96 && key<123) || (key==null) || (key==0) || (key==8) || 
	    (key==9) || (key==13) || (key==27)){
      //when first character of the string passed is numeric returns false
      return true;  
    }else{
      return false;
    }
  }
/*AlphaNumericOnly text ends*/


