package com.hbcd.container.web;

import com.hbcd.container.common.DriverObjectBase;
import com.hbcd.container.common.ParameterObject;
import com.hbcd.containerinterface.DriverSearch;
import com.hbcd.containerinterface.ElementSearch;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.internal.Locatable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DriverObject extends DriverObjectBase<WebElement> implements DriverSearch {
	public DriverObject() throws ReflectiveOperationException {
	}

	@Override
	public void init() throws Exception {
		if (driver == null) {
			try {
				driver = (WebDriver) new WebDriverProperties().setBrowserType(_browserType)
						.setBrowserName(_browserName)
						.setBrowserVersion(_version)
						.setResolution(_resolution)
						.setRemoteHub(_remoteHub)
						.setPlatform(_platform)
						.setProxyServer(_proxy)
						.setDefaultDirectory(_defaultDirectory)
						.getDriver();
				if (driver == null)
				{
					throw new Exception("UNABLE TO CREATE DRIVER");
				}
				driver.manage().window().maximize();
				//Log.Info("[Thead ID# : " + Thread.currentThread().getId() + "] CREATE NEW Browser!!");
				System.out.println(String.format("[Thead ID# : %s] CREATE NEW Browser!!", Thread.currentThread().getId() ));
				//driver.manage().window().maximize();
			} catch (Exception e) {
				e.printStackTrace();
//				Log.Error("ERROR init DRIVER", e);
				throw e;
			}
		}
	}

	@Override
	public void fw_init(String browserName, String browserType, String browserVersion, String browserPlatform, String defaultDirectory) throws Exception {
		if (driver == null) {
			try {
				driver = (WebDriver) new WebDriverProperties().setBrowserType(_browserType)
																.setBrowserName(_browserName)
																.setBrowserVersion(_version)
																.setResolution(_resolution)
																.setRemoteHub(_remoteHub)
																.setPlatform(_platform)
																.setProxyServer(_proxy)
																.setDefaultDirectory(_defaultDirectory)
																.getDriver();
				if (driver == null)
				{
					throw new Exception("UNABLE TO CREATE DRIVER");
				}
				driver.manage().window().maximize();
				System.out.println(String.format("[Thead ID# : %s] CREATE NEW Browser!!", Thread.currentThread().getId()));
			} catch (Exception e) {
				e.printStackTrace();
//				Log.Error("ERROR init DRIVER", e);
				throw e;
			}
		}
	}

	@Override
	public List<ElementSearch> fw_findAllDOMElementsObject()
	{
		List<ElementSearch> listObj= new ArrayList<ElementSearch>();
		if (driver != null)
		{
			listObj.addAll(driver.findElements(By.tagName("*")).stream().map(ElementObject::new).collect(Collectors.toList()));
		}
		return listObj;
	}
	@Override
	public List<ElementSearch> fw_findElements(Map<String, Object> params) throws Exception {
		List<ElementSearch> listObj= new ArrayList<ElementSearch>();
//		List<WebElement> lst = fw_findElementsWithLogic(new ParameterObject(params));
		listObj.addAll(fw_findElementsWithLogic(new ParameterObject(params)).stream().map(ElementObject::new).collect(Collectors.toList()));
		return listObj;
	}

	@Override
	public String fw_getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	@Override
	public String fw_getPageSource() {

		return driver.getPageSource();
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
	    }, url);
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
		//driver.get(url);
	}

	@Override
	public void fw_maximize() {
		driver.manage().window().maximize();
	}
	
	@Override
	public void fw_deleteAllCookies() {
		if (driver == null) return;
		driver.manage().deleteAllCookies();
	}

	@Override
	public void fw_sendKeys(char UnicodeKey) {
		Actions builder = new Actions(driver);
		builder.keyDown(Keys.getKeyFromUnicode(UnicodeKey)).perform();
	}

	@Override
	public void fw_implicitWait(int maximumTimeInSeconds) {
		driver.manage().timeouts()
				.implicitlyWait(maximumTimeInSeconds, TimeUnit.SECONDS);
	}

	public String screenshot() {
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);		
	}

	@Override
	public void fw_quit() {
		if (driver == null) return;
		driver.close();
		driver.quit();
		driver = null;
	}

	@Override
	public void fw_close() {
		driver.close();
		driver = null;
	}

	@Override
	public void fw_refresh() {
		driver.navigate().refresh();
	}

	@Override
	public void fw_to(String url) {
		driver.navigate().to(url);
	}

	@Override
	public void fw_back() {
		driver.navigate().back();
	}

	@Override
	public void fw_clickConfirmAlert() {
		
		Alert alertObject=driver.switchTo().alert();
		alertObject.accept();
	}
	
	@Override
	public void fw_clickDismissAlert() {
		Alert alertObject=driver.switchTo().alert();
		alertObject.dismiss();
	}

	@Override
	public void fw_switchToactiveElement() {
		driver.switchTo().activeElement();
	}

	@Override
	public void fw_switchToFrame(ElementSearch element) {
		driver.switchTo().frame(((ElementObject) element).getElement());
	}
	
	@Override
	public void fw_switchToFrame(String IDorName) {
		driver.switchTo().frame(IDorName);
	}

	@Override
	public void fw_switchToFrame(int index) {
		driver.switchTo().frame(index);
	}
	@Override
	public void fw_switchToDefaultContent() {
		driver.switchTo().defaultContent();
	}

	@Override
	public void fw_hover(ElementSearch element) {
		Actions Builder=new Actions(driver);
		Builder.moveToElement(((ElementObject) element).getElement()).build().perform();
	}

	@Override
	public void fw_dragDrop(ElementSearch source, ElementSearch target) {
		Actions Builder=new Actions(driver);
		Builder.dragAndDrop(((ElementObject) source).getElement(), ((ElementObject) target).getElement()).build().perform();
	}

	@Override
	public void fw_clickAndHoldAndMoveTo(String sourceFrame, ElementSearch source, String targetFrame, ElementSearch target) {
//		Actions Builder=new Actions(driver);
//		Builder.dragAndDrop(((ElementObject) source).getElement(), ((ElementObject) target).getElement()).build().perform();

		if ((sourceFrame == null) || (sourceFrame.length() <= 0))
		{ driver.switchTo().defaultContent(); }
		else
		{ driver.switchTo().frame(sourceFrame); }
		Actions builder = new Actions(driver);
		builder.clickAndHold(((ElementObject) source).getElement());
		builder.build().perform();

		if ((targetFrame == null) || (targetFrame.length() <= 0))
		{ driver.switchTo().defaultContent(); }
		else
		{ driver.switchTo().frame(targetFrame); }

//		builder.moveToElement(((ElementObject) target).getElement()).build().perform();

		if (((ElementObject) target).getElement() instanceof Locatable) {
			Locatable hoverItem = (Locatable)((ElementObject) target).getElement();

			int x = ((ElementObject) target).getElement().getLocation().getX();
			int y = ((ElementObject) target).getElement().getLocation().getY();
			builder.moveByOffset(x,y).build().perform();

			Robot r = null;
			try {
				r = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
			r.mouseMove(x, y);
			r.mouseRelease(0);
//			r.mouseRelease(1);
			r.mousePress(0);
//			r.mousePress(1);
//			hoverItem.getLocationOnScreenOnceScrolledIntoView();
//			Mouse mouse = ((HasInputDevices) driver).getMouse();
//			mouse.mouseMove(hoverItem.getCoordinates());
		}

		builder.release(((ElementObject) target).getElement()).build().perform();

//		(((ElementObject) target).getElement()).click();


//		action = builder.build();
//		action.perform();
		if ((sourceFrame == null) || (sourceFrame.length() <= 0))
		{ driver.switchTo().defaultContent(); }
		else
		{ driver.switchTo().frame(sourceFrame); }

	}

	@Override
	public void fw_doubleClick(ElementSearch element){
		Actions builder=new Actions(driver);
		builder.moveToElement(((ElementObject) element).getElement()).build().perform();
		builder.doubleClick(((ElementObject) element).getElement()).build().perform();
	}

	@Override
	public void fw_mouseClick(ElementSearch element) {
		Actions builder=new Actions(driver);
		builder.moveToElement(((ElementObject) element).getElement()).build().perform();
		builder.click(((ElementObject) element).getElement()).build().perform();
	}

	@Override
	public String fw_currentWindowHandle() {
		
		return driver.getWindowHandle();
	}
	
	@Override
	public void fw_switchToWindow(String windowHandle) {
		driver.switchTo().window(windowHandle);

	}
	
	@Override
	public void fw_switchNewWindow(String windowTitle){
		Set<String> windows=driver.getWindowHandles();
		for(String window: windows){
			fw_switchToWindow(window);
			if(driver.getTitle().contains(windowTitle)){
				return;
			}
		}
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
	public void fw_overlayMessage(String msg, String color) {
		((JavascriptExecutor)driver).executeScript(Utilities.overlayJS(msg, color));
	}

	@Override
	public void fw_overlayMessageBoxRePosition(String color) {
		((JavascriptExecutor)driver).executeScript(Utilities.overlayJSReposition( color));
	}

	@Override
	public void fw_overlayStepNumber(String number, String color) {

	}
}
