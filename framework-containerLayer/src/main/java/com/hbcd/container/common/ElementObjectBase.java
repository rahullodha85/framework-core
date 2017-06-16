package com.hbcd.container.common;

import com.hbcd.containerinterface.ElementSearch;
import org.openqa.selenium.Dimension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ElementObjectBase<T> implements ElementSearch {

	protected T element = null;
	protected String _originalStyle = "";
	protected int _status = 0;  /* 0: FOUND   1: NOT FOUND  2: multiple  3: timeout */
	protected int _selectIndex = 0; /* case for Multiple Object ONLY */
	protected List<ElementSearch> _list = new ArrayList<ElementSearch>();

	public ElementObjectBase(T e) {
		// check null value
		_status = 0;  //0 : Element is THERE
		this.element = e;
	}

//	protected final static String _javaScriptBlinkTwiceJQuery3 =
//			" var originalStyle=arguments[0].getAttribute('style'); " +
//			"arguments[0].setAttribute('style', originalStyle + ' ' + arguments[1]); " +
//			"$(arguments[0]).delay(100); " +
//			"arguments[0].setAttribute('style', originalStyle); " ;
////			"$(arguments[0]).delay(100); " +
////			"arguments[0].setAttribute('style', originalStyle + ' ' + arguments[1]); " +
////			"$(arguments[0]).delay(100); " +
////			"arguments[0].setAttribute('style', originalStyle); ";

//	protected final static String _javaScriptBlinkTwice =
//			" var originalStyle=arguments[0].getAttribute('style'); " +
//					"arguments[0].setAttribute('style', originalStyle + ' ' + arguments[1]); " +
//					"arguments[0].setAttribute('style', originalStyle); " +
//					"arguments[0].setAttribute('style', originalStyle + ' ' + arguments[1]); " +
//					"arguments[0].setAttribute('style', originalStyle); ";

//	protected final static String _javaScriptBlinkTwiceJQuery =
//			"$ (arguments[0]).fadeTo('fast',0).fadeTo('fast',1).fadeTo('fast',0).fadeTo('fast',1); ";
//
//	protected final static String _javaScriptBlinkTwiceJQuery2=
//			" $(arguments[0]).fadeIn(250).fadeOut(250).fadeIn(250).fadeOut(250).fadeIn(250); ";

	protected final static String _elementHighLightStyle = "color: deeppink; border: 2px solid limegreen;";
	protected final static String _javaScriptJQScrollToElement =
			" var dest = 0; " +
			"if ($(arguments[0]).offset().top > ($(window).height()/2)) { " +
				"dest = $(arguments[0]).offset().top - (($(window).height() - $(arguments[0]).height())/ 2); " +
			"} else {return;} " +
			"$('html,body').animate({ scrollTop: dest}, 250, 'swing'); " +
			"if( $('#SeleniumMessageOverlay').length )" +
			"{ $('#SeleniumMessageOverlay' ).delay(250).position({ my: 'left+3 top+3', at: 'left top', of: window})}; ";

	protected final static String _javaScriptScrollToElement =
			"var height = \"innerHeight\" in window ? window.innerHeight : document.documentElement.offsetHeight; " +
			"var dest = 0; " +
			"if (arguments[0].offsetTop > (height/2)) { dest = arguments[0].offsetTop - height - (arguments[0].offsetHeight/2); } " +
//			"alert('arguments[0].offsetTop : ' + arguments[0].offsetTop);" +
//			"alert('arguments[0].offsetHeight : ' + arguments[0].offsetHeight);" +
//			"alert('height : ' + height);" +
//			"alert('dest : ' + dest);" +
			"if (dest > 0) {alert ('dest > 0 ' + des);}" +
			"window.scrollTo(0,dest);" ;

	protected final static String _scriptHightLightAndMove = " arguments[0].setAttribute('style', arguments[1]); var dest = 0; if ($(arguments[0]).offset().top > ($(window).height()/2)) {dest = $(arguments[0]).offset().top - (($(window).height() - $(arguments[0]).height())/ 2);} else {return;} $('html,body').animate({ scrollTop: dest}, 250, 'swing'); $('#SeleniumMessageOverlay' ).delay(250).position({ my: 'left+3 top+3', at: 'left top', of: window}); ";

	protected final static String _scriptHighLightOnly=" arguments[0].setAttribute('style', arguments[1]); ";

	protected final static String _scriptBlinkOnly = "";

	protected final static String _javaScriptConfirmation =
			"var r = confirm(\"Press a button\");" +
			"if (r == true) {" +
			"    x = \"You pressed OK!\";" +
			"} else {" +
			"    x = \"You pressed Cancel!\";" +
			"} " +
			"return r;";


	public ElementObjectBase(int stat){	_status = stat;	}
	
	public void add(ElementSearch e)
	{
		_list.add(e);
	}

	public T getElement() {	return element;	}

	public void setElement(T el) { this.element = el; }

	@Override
	public void fw_click() {}

	@Override
	public void fw_javascriptClick() {}

	@Override
	public void fw_submit() {}

	@Override
	public void fw_sendKeys(CharSequence... keysToSend) {}

	@Override
	public void fw_enter() {}

	@Override
	public void fw_clear() {}

	@Override
	public String fw_getTagName() {
		return null;
	}

	@Override
	public String fw_getAttribute(String name) {
		return null;
	}

	@Override
	public String fw_getText() {
		return null;
	}

	@Override
	public String fw_getClass() {
		return null;
	}

	@Override
	public Dimension fw_getSize() {
		return null;
	}

	@Override
	public List<ElementSearch> fw_getFoundList() { return _list; }

	@Override
	public ElementSearch fw_getElementInList(int index) {
		if ((index <= 0) || (_list.size() <= 0)) return null;
		if (_list.size() < index) return null;
		_selectIndex = index;
		return _list.get(_selectIndex - 1);
	}

	@Override
	public ElementSearch fw_getElementInList(String containText) {
		if (_list.size() <= 0) return null;
//		List<ElementSearch> l = _list.stream().filter(e -> e.fw_getAttribute("outerHTML").contains(containText)).collect(Collectors.toList());
//		if (l.size() == 1)
//			return l.get(0);
//		else
//			return null;
		int count  = 0; //Keep number of element found
		for (int idx = 0; idx < _list.size(); idx++)
		{
			if (_list.get(idx).fw_getAttribute("outerHTML").contains(containText))
			{
				_selectIndex = idx+1;  //_list were 0 base
				count++;
			}
		}

		if (count == 1)
		{
			return _list.get(_selectIndex-1);
		}
		return null;
	}

	@Override
	public ElementSearch fw_getElementRandomInList() {
		int l_size = _list.size();
		_selectIndex = ThreadLocalRandom.current().nextInt(1, l_size + 1); //Add 1 for inclusive
		return fw_getElementInList(_selectIndex-1);
	}

	@Override
	public int fw_getReturnSize() {
		if (_list != null)
		{
			return _list.size();
		}
		return 0;
	}

	@Override
	public int fw_getSelectedElementIndexFromMultipleObject() { return _selectIndex; }

	@Override
	public String fw_getDropDownSelectedText() {
		return null;
	}

	@Override
	public void fw_selectByIndex(int index) {}

	@Override
	public void fw_selectByValue(String value) {}

	@Override
	public void fw_selectByVisibleText(String text) {}

	@Override
	public boolean fw_isDisplayed() {
		return false;
	}

	@Override
	public boolean fw_isEnabled() {
		return false;
	}

	@Override
	public boolean fw_isDisabled() {
		return false;
	}

	@Override
	public void fw_elementHighlight() {}

	@Override
	public void fw_elementHighlightOnly() {}

	@Override
	public void fw_elementBlink(int i) {}

	@Override
	public void fw_scrollToElement() {}

	@Override
	public void fw_overlayMessage(String msg, String color) {}

	@Override
	public void fw_overlayMessageBoxRePosition(String color) {}

	@Override
	public void fw_overlayStepNumber(String number, String color) {}

	@Override
	public Object fw_executeJavaScript(String script) {
		return null;
	}

	@Override
	public Object fw_executeAsyncJavaScript(String script) {
		return null;
	}


	@Override
	public boolean fw_isFound(){
		return (_status == 0);
	}

	@Override
	public boolean fw_isNotFound() { return (_status != 0); }

	@Override
	public boolean fw_isMulitpleFound()	{ return (_status == 2); }

	@Override
	public ElementSearch fw_findElement(Map<String, Object> params) throws Exception { return null;	}

	@Override
	public List<ElementSearch> fw_findElements(Map<String, Object> params) throws Exception { return null; }

	protected List<T> fw_findElements(ParameterObject parameters) throws Exception { return new ArrayList<>(); }

	protected List<ElementSearch> fw_findElementsBy(String name, String value) { return null; }

}