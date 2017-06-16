package com.hbcd.containerinterface;

import org.openqa.selenium.Dimension;

import java.util.List;

public interface Element {
	
	public void fw_click();
	
	public void fw_javascriptClick();

	public void fw_submit();

	public void fw_sendKeys(CharSequence... keysToSend);

	public void fw_clear();

	public String fw_getTagName();

	public String fw_getAttribute(String name);
	
	public void fw_elementHighlight();

	public void fw_elementBlink(int i);

	public void fw_scrollToElement();

	public void fw_elementHighlightOnly();

	public String fw_getText();

	public boolean fw_isDisplayed();

	public boolean fw_isEnabled();

	public boolean fw_isDisabled();

	public String fw_getClass();

	public Dimension fw_getSize();

	public void fw_selectByIndex(int index);

	public void fw_selectByValue(String value);
	
	public void fw_selectByVisibleText(String text);
	
	public boolean fw_isFound();
	
	public boolean fw_isNotFound();
	
	public boolean fw_isMulitpleFound();
	
	public Object fw_executeJavaScript(String script);

	public Object fw_executeAsyncJavaScript(String script);
	
	public List<ElementSearch> fw_getFoundList();

	public ElementSearch fw_getElementInList(int index);

	public ElementSearch fw_getElementInList(String containText);

	public ElementSearch fw_getElementRandomInList();
	
	public int fw_getReturnSize();

	void fw_enter();

	public int fw_getSelectedElementIndexFromMultipleObject();

	public String fw_getDropDownSelectedText();

}
