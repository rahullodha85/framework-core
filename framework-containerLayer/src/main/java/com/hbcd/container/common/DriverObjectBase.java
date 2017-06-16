package com.hbcd.container.common;

import com.hbcd.ContainerUtility.Log;
import com.hbcd.containerinterface.DriverSearch;
import com.hbcd.containerinterface.ElementSearch;
import org.openqa.selenium.*;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class DriverObjectBase<T> implements DriverSearch
{
	protected final int DEFAULT_WAIT_TIME = 12;

	protected WebDriver driver;
	protected String _browserType;
	protected String _browserName;
	protected String _version;
	protected String _resolution;
	protected String _remoteHub;
	protected  String _platform;
	protected String _proxy;
	protected String _defaultDirectory;

	public DriverObjectBase<T> setBrowserName(String bn)
	{
		_browserName=bn;
		return this;
	}

	public DriverObjectBase<T> setBrowserVersion(String bv)
	{
		_version=bv;
		return this;
	}

	public DriverObjectBase<T> setBrowserType(String bt)
	{
		_browserType=bt;
		return this;
	}

	public DriverObjectBase<T> setResolution (String rs)
	{
		_resolution=rs;
		return this;
	}

	public DriverObjectBase<T> setRemoteHub(String rh)
	{
		_remoteHub=rh;
		return this;
	}

	public DriverObjectBase<T> setProxyServer(String ps)
	{
		_proxy=ps;
		return this;
	}

	public DriverObjectBase<T> setPlatform(String p)
	{
		_platform=p;
		return this;
	}

	public DriverObjectBase<T> setDefaultDirectory(String dd)
	{
		_defaultDirectory=dd;
		return this;
	}

	@Override
	public boolean fw_isInitialized() {
		if (driver != null)
		{
			return true;
		}
		return false;
	}

	public void init() throws Exception {
	}

	@Override
	public void fw_init(String browserName, String browserType, String browserVersion, String browserPlatform, String defaultDirectory) throws Exception {
	}

	@Override
	public String fw_printDriver()
	{
		if (driver != null)
		{
			return String.format("Current Thread ID# : %s DRIVER IS : %s", Thread.currentThread().getId(), driver.toString());
		}
		else
		{
			return String.format("Current Thread ID# : %s DRIVER IS NULL!!!", Thread.currentThread().getId());
		}
	}

	protected List<T> fw_findElementsBy(ParameterObject parameters) {
		List<T> objectList = new ArrayList<>();
		try {
			objectList = fw_findElementsBy(null, parameters);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return objectList;
	}

	protected boolean fw_findElementsNotExistBy(WebDriver drvr, ParameterObject params) throws NoSuchElementException
	{
		final List<T> objectList = new ArrayList<>();
		String _message = String.format(" FOUND with property[%s] = [%s] ", params.getPropertyType(), params.getPropertyName());
		String _message2 = "";

		//Start HERE
		//Add logic here to Parse Properties Type? Loop?
		String PropType = params.getPropertyType();
		String PropValue = params.getPropertyName();
		List<T> temp_lst = new ArrayList<>();
		if (PropValue.contains("|"))
		{
			String[] valueList = PropValue.split("\\|");
			for (String eachValue : valueList) {
				if ((eachValue != null) && (eachValue.length() > 0))
				{
					List<T> temp_partial_lst = (List<T>)(drvr == null ? driver : drvr).findElements(getIdentifier(PropType, eachValue));
					if (temp_partial_lst.size() > 0) {
						temp_lst.addAll(temp_partial_lst.stream().collect(Collectors.toList()));
					}
				}
			}
		}
		else
		{
			temp_lst = (List<T>)(drvr == null ? driver : drvr).findElements(getIdentifier(PropType, PropValue));
		}
		//END HERE

		if (temp_lst.size() == 0)
		{
			return true;
		}
		else {
			throw new NoSuchElementException(String.format("Element with property[%s=%s] is still on Screen", params.getPropertyType(), params.getPropertyName()));
		}
	}

	protected List<T> fw_findElementBy(WebDriver drvr, String type, String value)
	{
		List<T> temp_lst = new ArrayList<>();
		if (value.contains("|"))
		{
			String[] valueList = value.split("\\|");
			for (String eachValue : valueList) {
				if ((eachValue != null) && (eachValue.length() > 0))
				{
					List<T> temp_partial_lst = (List<T>)(drvr == null ? driver : drvr).findElements(getIdentifier(type, eachValue));
					if (temp_partial_lst.size() > 0) {
						temp_lst.addAll(temp_partial_lst.stream().collect(Collectors.toList()));
					}
				}
			}
		}
		else
		{
			temp_lst = (List<T>)(drvr == null ? driver : drvr).findElements(getIdentifier(type, value));
		}
		return temp_lst;
	}

	protected List<T> fw_findElementsBy(WebDriver drvr, ParameterObject params) throws NoSuchElementException
	{
		final List<T> objectList = new ArrayList<>();
		String _message = String.format(" FOUND with property[%s] = [%s] ", params.getPropertyType(), params.getPropertyName());
		String _message2 = "";

		//Start HERE
		//Add logic here to Parse Properties Type? Loop?
		String PropType = params.getPropertyType();
		String PropValue = params.getPropertyName();
		List<T> temp_lst = fw_findElementBy(drvr, PropType, PropValue);
//		List<T> temp_lst = new ArrayList<>();
//		if (PropValue.contains("|"))
//		{
//			String[] valueList = PropValue.split("\\|");
//			for (String eachValue : valueList) {
//				if ((eachValue != null) && (eachValue.length() > 0))
//				{
//					List<T> temp_partial_lst = (List<T>)(drvr == null ? driver : drvr).findElements(getIdentifier(PropType, eachValue));
//					if (temp_partial_lst.size() > 0) {
//						temp_lst.addAll(temp_partial_lst.stream().collect(Collectors.toList()));
//					}
//				}
//			}
//		}
//		else
//		{
//			temp_lst = (List<T>)(drvr == null ? driver : drvr).findElements(getIdentifier(PropType, PropValue));
//		}
		//END HERE

		if (temp_lst.size() > 0)
		{
			int size = temp_lst.size();
			if (params.isElementDisplayed() || params.isElementEnabled()) {

				if (params.isElementDisplayed() && params.isElementEnabled()) {
					_message2 = "DISPLAYED AND ENABLED";
					objectList.addAll(temp_lst.stream().filter(e -> ((WebElement) e).isDisplayed() && ((WebElement) e).isEnabled()).collect(Collectors.toList()));
				} else if (params.isElementDisplayed()) {
					_message2 = "DISPLAYED";
					objectList.addAll(temp_lst.stream().filter(e -> params.isElementDisplayed()).collect(Collectors.toList()));
				} else if (params.isElementEnabled()) {
					_message2 = "ENABLED";
					objectList.addAll(temp_lst.stream().filter(e -> ((WebElement) e).isEnabled()).collect(Collectors.toList()));
				}

				if (objectList.size() > 0) {
					return objectList;
				} else {
					if (size > 1) {
						throw new NoSuchElementException(String.format("MULTIPLE Elements (%s) %s BUT NONE ARE %s", size, _message, _message2));
					} else {
						throw new NoSuchElementException(String.format("ONE Element %s BUT NOT %s", _message, _message2));
					}
				}
			}
			else
			{
				objectList.addAll(temp_lst.stream().collect(Collectors.toList()));
				return objectList;
			}
		}
		else {
			throw new NoSuchElementException(String.format("Element NOT FOUND with property[%s=%s]", params.getPropertyType(), params.getPropertyName()));
		}
	}

	protected By getIdentifier(String type, String value) {
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

	@Override
	public ElementSearch fw_findElement(Map<String, Object> params) throws Exception {
		List<ElementSearch> elements = fw_findElements(params);
		if (elements.size() == 1)
			return elements.get(0);
		else if (elements.size() == 0)
			throw new Exception("element not found");
		else
			throw new Exception("multiple elements found");
	}

	@Override
	public List<ElementSearch> fw_findElements(Map<String, Object> params) throws Exception {
		return null;
	}

	protected List<T> fw_findElementsWithLogic(ParameterObject parameters) throws Exception {
		List<T> objectList = new ArrayList<T>();

		try
		{
			//AJAX -> CUSTOM EXPLICIT WAIT
			objectList = fw_findElementsAJAX(parameters);
			if (objectList == null)
			{

			}
			else if (objectList.size() <= 0)
			{
				objectList = fw_findElementsWithWait(parameters);
			}
		}
		catch (TimeoutException te)
		{
			System.out.println(te.getMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();
//			Suppress Exception - let lower level dealing with
//			Log.Error("Container Exception", e);
		}

		return objectList;
	}

	//************************************************************************
	//**********     FIND LOGIC
	//************************************************************************

	protected List<T> fw_findElementsAJAX(ParameterObject params) throws Exception
	{
		List<T> objectList = new ArrayList<T>();
		Boolean isJQueryExist = false;
		Boolean object = false;
		long pollTime = 1;

		long accumulatePollingTime = 0;
		long pollingIntervalTime = 500;

		long pollingPerSecond = 1000/pollingIntervalTime;
		//if MaxWaitTime is less than 30 seconds then set it as 30 seconds.
		long totalWaitTimeInSec = (params.getMaxWaitTime() >= 30) ? params.getMaxWaitTime() : 30;
		long totalPoolingTimeBeforeExipred = totalWaitTimeInSec * 1000; //* pollingPerSecond;

		if (params.isFirstTime())  //WAIT for AJAX for FIRST TIME.
		{
			isJQueryExist = ((Boolean) executeScript(
					"return window.jQuery != undefined", null)).booleanValue();
			if (isJQueryExist) {
				System.out.println(String.format("AJAX FIND... [Expired in %s Sec].",totalWaitTimeInSec));
				// JQUERY
				while (!object) {
					object = ((Boolean) executeScript(
							"return jQuery.active == 0", null)).booleanValue();
					if (object) {
						objectList = fw_findElementsBy(params);
					} else {
						System.out.println(String.format("Continue AJAX Waiting - has been waited for %s ms, expected max end wait %s ms", accumulatePollingTime, totalPoolingTimeBeforeExipred));
						accumulatePollingTime += pollingIntervalTime;
						Thread.sleep(pollingIntervalTime); //0.5 a second wait and test again.
						if (accumulatePollingTime >= totalPoolingTimeBeforeExipred)  //Max 30 seconds.
						{
							System.out.println(String.format("AJAX Exit Wait (Expired max wait time %s ms).", totalPoolingTimeBeforeExipred));
							break;
						}
					}
				}
			}
		}
		return objectList;
	}

	protected List<T> fw_findElementsWithWait(ParameterObject params) throws Exception
	{
		List<T> objectList = new ArrayList<T>();
		try {
			if (params.isFirstTime())  //Fluent wait only for first time.
			{
				System.out.println(String.format("FIRST ATTRIBUTE FIND BY [%s = %s]...", params.getPropertyType(), params.getPropertyName()));
				//Check Explicit/Fluent Wait
				objectList = fw_waitExplicitCustomCondition(params);
			} else {
				System.out.println(String.format("SECOND ATTRIBUTE FIND BY [%s = %s]...", params.getPropertyType(), params.getPropertyName()));
				objectList = fw_findElementsBy(params);
			}
		}
		catch (TimeoutException te)
		{
			throw new TimeoutException(String.format("Timeout with Explicit Wait of %s second(s)", params.getMaxWaitTime()), te);
		}
		catch (Exception e)
		{
			throw  e;
		}
		return objectList;
	}

	//ExpectedConditions are:
	//	alertIsPresent : Alert is present
	//	elementSelectionStateToBe: an element state is selection.
	//	elementToBeClickable: an element is present and clickable.
	//	elementToBeSelected: element is selected
	//	frameToBeAvailableAndSwitchToIt: frame is available and frame selected.
	//	invisibilityOfElementLocated: an element is invisible
	//	presenceOfAllElementsLocatedBy: present element located by.
	//	textToBePresentInElement: text present on particular an element
	//	textToBePresentInElementValue: and element value present for a particular element.
	//	visibilityOf: an element visible.
	//	titleContains: title contains
	protected  List<T> fw_waitExplicitCustomCondition(ParameterObject params)
	{
		System.out.println("CUSTOM EXPLICIT WAIT FIND...");

		String _message = String.format(" FOUND with property[%s] = [%s], BUT ARE %s VISIBLE AND %s ENABLED", params.getPropertyType(), params.getPropertyName(), params.isElementDisplayed() ? "" : "NO", params.isElementEnabled() ? "" : "NOT");
		final List<T> objectList = new ArrayList<>();

		//Check if DOM is ready (completed load)
		Boolean isDOMReady = ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");

		//Determine wait-time when DOM is ready.  For check not Present - set Object Repository UserDefineWaitTime to 0 to avoid long wait
		WebDriverWait wait = new WebDriverWait(driver, isDOMReady ? params.getMaxWaitTime() : DEFAULT_WAIT_TIME);

		return wait.until((ExpectedCondition<List<T>>) wd -> fw_findElementsBy( wd, params));
	}

	protected List<T> fw_waitFluent(int pollingTimeMilliSeconds, int maxTimeMilliSeconds, ParameterObject params) {
		System.out.println("FLUENT FIND...");

		List<T> objectList = new ArrayList<T>();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(maxTimeMilliSeconds, TimeUnit.MILLISECONDS)
				.pollingEvery(pollingTimeMilliSeconds, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class);

		//New Implementation
		objectList = wait.until(new ExpectedCondition<List<T>>()
								 {
									 public List<T> apply(WebDriver driver)
									 {
										 List<T> temp_lst = fw_findElementsBy(params);
										 if (temp_lst.size() > 0)
										 {
											 return temp_lst;
										 }
										 else
										 {
											 //This exception type (NoSuchElementException) is important since Fluent wait will ignor this exception and start polling again in next interval
											 throw new NoSuchElementException("NOT FOUND");
										 }
									 }
								 }
		);
		if (objectList.size() > 0)
		{
			return objectList;
		}
		else
		{
			throw new NoSuchElementException(String.format("Element NOT FOUND with property[%s=%s]", params.getPropertyType(), params.getPropertyName()));
		}
	}


	//************************************************************************
	//**********     FIND LOGIC END
	//************************************************************************


	@Override
	public String fw_getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	@Override
	public String fw_getPageSource() {
		return "";
	}

	@Override
	public String fw_getTitle() {
		return driver.getTitle();
	}

	@Override
	public boolean fw_get(String url) {
		boolean status = true;
		//Log.Info("Driver : " + driver.toString());
		Thread t = new Thread(new Runnable()
											{
											  public void run()
											  {
												driver.get(Thread.currentThread().getName());
											  }
											}, url
							);
	    t.start();
	    try
	    {
	      t.join(120000);  /* 2 minutes */
	    }
	    catch (InterruptedException e)
	    { // ignore
			e.printStackTrace();
	    }
	    if (t.isAlive())
	    { // Thread still alive, we need to abort
	    //	Log.Warn("Timeout on loading page " + url);
	      t.interrupt();
	      status = false;
	    }

	    return status;
	}

	@Override
	public void fw_maximize() {
		driver.manage().window().maximize();
	}

	@Override
	public void fw_deleteAllCookies() {
	}

	@Override
	public void fw_sendKeys(char UnicodeKey) {
	}

	@Override
	public void fw_implicitWait(int maximumTimeInSeconds) {
		driver.manage().timeouts()
				.implicitlyWait(maximumTimeInSeconds, TimeUnit.SECONDS);
	}

	public String screenshot() {
		return "";
	}

	@Override
	public void fw_quit() {
	}

	@Override
	public void fw_close() {
	}

	@Override
	public void fw_refresh() {
	}

	@Override
	public void fw_to(String url) {
		driver.navigate().to(url);
	}

	@Override
	public void fw_back() {
	}

	@Override
	public void fw_clickConfirmAlert() {
	}

	@Override
	public void fw_clickDismissAlert() {
	}

	@Override
	public void fw_switchToactiveElement() {
	}

	@Override
	public void fw_switchToFrame(ElementSearch element) {
	}

	@Override
	public void fw_switchToFrame(String IDorName) {
	}

	@Override
	public void fw_switchToFrame(int index)
	{
	}

	@Override
	public void fw_clickAndHoldAndMoveTo(String sourceFrame, ElementSearch source, String targetFrame, ElementSearch target) throws AWTException {

	}

	@Override
	public void fw_mouseClick(ElementSearch object) {

	}

	@Override
	public void fw_switchToDefaultContent()
	{
	}

	@Override
	public void fw_hover(ElementSearch element) {
	}

	@Override
	public void fw_dragDrop(ElementSearch source, ElementSearch target) {
	}

	@Override
	public void fw_doubleClick(ElementSearch element){
	}


	@Override
	public String fw_currentWindowHandle() {
		return null;
	}

	@Override
	public void fw_switchToWindow(String windowHandle) {
	}

	@Override
	public List<ElementSearch> fw_findAllDOMElementsObject() {
		return null;
//		else
//		{
//			return String.format("Current Thread ID# : %s DRIVER IS NULL!!!", Thread.currentThread().getId());
//		}
	}

	@Override
	public com.hbcd.container.web.ElementObject fw_explicitWait(int timeOutInSeconds, PropertiesNames propertyType, String propertyName, int maxUserDefinedWaitTime) {
		return null;
	}

	@Override
	public void fw_switchNewWindow(String windowTitle){
	}

	@Override
	public Object executeScript(String script,Object args) {
		return ((JavascriptExecutor)driver).executeScript(script, args);
	}

	@Override
	public Object executeAsyncScript(String script, Object args) {
		return ((JavascriptExecutor)driver).executeAsyncScript(script, args);
	}

	@Override
	public Object fw_executeJavaScript(String scrpt) {
		return ((JavascriptExecutor)driver).executeScript(scrpt);
	}

	@Override
	public void fw_overlayMessage(String msg, String color) {
	}

	@Override
	public void fw_overlayMessageBoxRePosition(String color) {
	}

	@Override
	public void fw_overlayStepNumber(String number, String color) {
	}

}
