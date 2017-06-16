package com.hbcd.container.web;

import org.openqa.selenium.By;

import java.util.Date;

public class Utilities {
	public static By getIdentifier(String type, String value) {
		switch (type) {
		case "CLASSNAME":
			return By.className(value);
		case "CSSSELECTOR":
			return By.cssSelector(value);
		case "ID":
			return By.id(value);
		case "LINKTEXT":
			return By.linkText(value);
		case "NAME":
			return By.name(value);
		case "PARTIALLINKTEXT":
			return By.partialLinkText(value);
		case "TAGNAME":
			return By.tagName(value);
		case "XPATH":
			return By.xpath(value);
		default:
			return null;
		}
	}
	public static final String cssPerspective = "-webkit-perspective:1000; -moz-perspective:1000; -ms-perspective:1000; -o-perspective:1000; perspective:1000;";
	public static final String cssFlip =  "-webkit-transform:rotateY(360deg); -moz-transform:rotateY(3600deg); -ms-transform:rotateY(3600deg); -o-transform:rotateY(360deg); transform:rotateY(360deg);";
	public static final String cssTransformStyle3D = "-webkit-transform-style:preserve-3d; -moz-transform-style:preserve-3d; -ms-transform-style:preserve-3d; -o-transform-style:preserve-3d; transform-style:preserve-3d;";
	public static final String cssTransition = "-webkit-transition:0.6s; -moz-transition:0.6s; -o-transition:0.6s; transition:0.6s;";
	public static final String cssGeneralFormatBox = "display:inline-block;opacity:0.93;position:absolute;top:9px;left:9px;padding:10px;text-indent:50px;vertical-align:middle;color:__REPLACE_COLOR_HERE__;-moz-border-radius:13px;border-radius:13px;border:0px solid #A0A0A0;background-color:#F8F8F8;width:50%;height:auto;box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);z-index:9999;pointer-events:none;";
	public static final String messageFormatBox = String.format("'%s%s%s%s%s';", cssGeneralFormatBox, cssPerspective, cssTransition, cssTransformStyle3D, cssFlip);

	public static String overlayJS(String msg, String color)
	{
		String frmt = messageFormatBox.replace("__REPLACE_COLOR_HERE__", color);
		String exist =	String.format("element.style.cssText =%s;element.innerHTML = \"&#9432; %s\";",  frmt, msg);
		String notExist = String.format("var elemDiv = document.createElement('div'); elemDiv.setAttribute('id', 'SeleniumMessageOverlay'); elemDiv.style.height='25px'; elemDiv.style.cssText = %s; elemDiv.innerHTML = \"&#9432; %s\";document.body.appendChild(elemDiv);", frmt, msg);
		String js = String.format("var element =  document.getElementById('SeleniumMessageOverlay'); if (typeof(element) != 'undefined' && element != null) { %s; element.classList.toggle('flip'); } else { %s; elemDiv.classList.toggle('flip'); }", exist, notExist);
		return js;
	}

	public static String overlayJSReposition(String color) {
		String frmt = messageFormatBox.replace("__REPLACE_COLOR_HERE__", color);
		String js = String.format("var element =  document.getElementById('SeleniumMessageOverlay'); if (typeof(element) != 'undefined' && element != null) { element.style.cssText = %s; }; element.classList.toggle('flip')", frmt);
		return js;
	}

//	public static String overlay(String msg, String color)
//	{
////		String rtn =
////			   "if ($('#SeleniumMessageOverlay').length > 0) {" +
////			   "   $('#SeleniumMessageOverlay').text(\"" + msg + "\");" +
////			   "   $(\"#SeleniumMessageOverlay\")" +
////			   "      .css({" +
////			   "            'top': 3," +
////			   "            'color': '" + color  + "'," +
////			   "			'pointer-events': 'none'" +
////			   "          });" +
////    		   "}" +
////    		   "else {" +
////			   "   var docHeight = \"25px\";" +
////			   "   $(\"body\").prepend(\"<div id='SeleniumMessageOverlay'>" + msg + "</div>\");" +
////			   "   $(\"#SeleniumMessageOverlay\")" +
////			   "      .height(docHeight)" +
////			   "      .css({" +
////			   "            'display': 'inline-block', " +
////			   "            'opacity' : 0.75," +
////			   "            'position': 'absolute'," +
//////			   "            'overflow' : 'scroll'," +
////			   "            'top': 3," +
////			   "            'left': 3," +
////			   "            'padding': '10px'," +
////			   "            'text-indent': '50px'," +
////			   "            'vertical-align' : 'middle'," +
////			   "            'color': '" + color + "'," +
////			   "            '-moz-border-radius' : '4px'," +
////			   "            'border-radius' : '4px'," +
////			   "            'border' : '1px solid #000'," +
////			   "            'background-color': '#CCC'," +
////			   "            'width': '50%'," +
////			   "            'height': 'auto'," +
////			   "            'z-index': 9999," +
////			   "			'pointer-events': 'none'" +
////			   "          });" +
////				"};"
////				;
//
//
//
//		String ifStatement = String.format("if ($('#SeleniumMessageOverlay').length > 0) { $('#SeleniumMessageOverlay').text(\"%s\"); $(\"#SeleniumMessageOverlay\").css({'top': 3,'color': '%s','pointer-events': 'none'});}", msg, color);
//		String elseStatement = String.format("else {var docHeight = \"25px\"; $(\"body\").prepend(\"<div id='SeleniumMessageOverlay'>%s</div>\"); $(\"#SeleniumMessageOverlay\").height(docHeight).css({'display': 'inline-block', 'opacity' : 0.75, 'position': 'absolute', 'top': 3, 'left': 3, 'padding': '10px','text-indent': '50px', 'vertical-align' : 'middle', 'color': '%s', '-moz-border-radius' : '4px', 'border-radius' : '4px', 'border' : '1px solid #000', 'background-color': '#CCC', 'width': '50%', 'height': 'auto', 'z-index': 9999, 'pointer-events': 'none'});};", msg, color);
//		String rtn = String.format("%s %s", ifStatement, elseStatement);
//		return rtn;
//	}

	public static String overlayNotificationNumber(String number, String color)
	{
		String _uniqueValue = Long.toString(new Date().getTime());
		String rtn =
					"var box =  arguments[0].getBoundingClientRect();" +
					"var circlePositionTop  = box.top - 12; " +
					"var circlePosiotionLeft = box.right - 6; " +
//						"var circlePositionTop  = 500;" +
//						"var circlePosiotionLeft = 500;" +
					"if ($('#SeleniumNumberCircleOverlay" + _uniqueValue + "').length > 0) {" +
					"   $('#SeleniumNumberCircleOverlay" + _uniqueValue + "').text(\"" + number + "\");" +
					"   $(\"#SeleniumNumberCircleOverlay" + _uniqueValue + "\")" +
					"      .css({" +
					"            'top': circlePositionTop, " +
					"            'left': circlePosiotionLeft, " +
					"            'color': '" + color  + "'," +
					"			'pointer-events': 'none'" +
					"          });" +
					"}" +
					"else {" +
					"   var docHeight = 36;" +  //18px
					"   $(\"body\").append(\"<div id='SeleniumNumberCircleOverlay" + _uniqueValue + "'>" + number + "</div>\");" +
					"   $(\"#SeleniumNumberCircleOverlay" + _uniqueValue + "\")" +
					"       .height(docHeight)" +
					"		.css ({" +
					"            	'display': 'inline-block', " +
					"            	'position': 'absolute'," +
					"            	'top': circlePositionTop, " +
					"            	'left': circlePosiotionLeft, " +
					"				'border-radius': '50%'," +
					"				'behavior': 'url(PIE.htc)'," + /* remove if you don't care about IE8 */
					"				'min-width': '13px'," +
					"				'width': 'auto'," +
					"				'height': '13px'," +
					"				'padding': '2px'," +
					"				'background': '#dc143c'," +
					"				'border': '2px solid #dc143c'," +
					"				'color': '#fff'," +
					"				'text-align': 'center'," +
					"				'font': '11px Arial, sans-serif'," +
					"   			'z-index': '9999'" +
					"			});" +
					"};";
		return rtn;
	}


//
//	".css ({" +
//	"	'border-radius': '50%'," +
//	"	'behavior': 'url(PIE.htc)'," + /* remove if you don't care about IE8 */
//	"	'width': '36px'," +
//	"	'height': '36px'," +
//	"	'padding': '8px'," +
//	"	'background': '#fff'," +
//	"	'border': '2px solid #666'," +
//	"	'color': '#666'," +
//	"	'text-align': 'center'," +
//	"	'font': '32px Arial, sans-serif'," +
//	"   'z-index': '9999'" +
//	"});" +
	
}
