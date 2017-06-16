package com.hbcd.containerinterface;

import com.hbcd.container.common.PropertiesNames;
import com.hbcd.container.web.ElementObject;

import java.awt.*;
import java.util.List;


public interface Driver {
	
	public void fw_init(String browserName, String browserType, String browserVersion, String browserPlatform, String defaultDirectory) throws Exception;
	
	public boolean fw_isInitialized();
	
	public String fw_printDriver();
	
	public String fw_getCurrentUrl();

	public String fw_getPageSource();

	public String fw_getTitle();

	public void fw_switchNewWindow(String windowTitle);

	public boolean fw_get(String url);

	public void fw_maximize();

	public void fw_hover(ElementSearch object);

	public void fw_doubleClick(ElementSearch object);

	public void fw_dragDrop(ElementSearch source, ElementSearch target);

	public void fw_implicitWait(int maximumTimeInSeconds);

	public void fw_quit();

	public void fw_close();

	public void fw_refresh();

	public void fw_to(String url);

	public void fw_back();

	public Object executeScript(String script, Object args);

	public Object executeAsyncScript(String script, Object args);

	public void fw_clickConfirmAlert();

	public void fw_clickDismissAlert();

	public void fw_switchToactiveElement();

	public void fw_switchToFrame(ElementSearch element);

	public void fw_switchToFrame(String IDorName);

	public void fw_switchToDefaultContent();

	public String fw_currentWindowHandle();

	public void fw_switchToWindow(String windowName);

	public List<ElementSearch> fw_findAllDOMElementsObject();

	public ElementObject fw_explicitWait(int timeOutInSeconds,
										 PropertiesNames propertyType, String propertyName, int maxUserDefinedWaitTime);
	
	public String screenshot();

	void fw_deleteAllCookies();

	void fw_sendKeys(char UnicodeKey);

	void fw_switchToFrame(int index);

	void fw_clickAndHoldAndMoveTo(String sourceFrame, ElementSearch source, String targetFrame, ElementSearch target) throws AWTException;

	public void fw_mouseClick(ElementSearch object);

}
