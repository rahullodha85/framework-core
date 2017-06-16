package com.hbcd.container.appium;

import com.hbcd.container.common.DriverObjectBase;
import com.hbcd.container.common.ParameterObject;
import com.hbcd.containerinterface.DriverSearch;
import com.hbcd.containerinterface.ElementSearch;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DriverObject extends DriverObjectBase<MobileElement> implements DriverSearch {

	public DriverObject() throws ReflectiveOperationException {
	}

	@Override
	public void init() throws Exception {
		if (driver == null) {
			try {
				driver = (AppiumDriver) new AppiumDriverProperties().setBrowserType(_browserType)
						.setBrowserName(_browserName)
						.setBrowserVersion(_version)
						.setResolution(_resolution)
						.setRemoteHub(_remoteHub)
						.setPlatform(_platform)
						.setProxyServer(_proxy)
						.setDefaultDirectory(_defaultDirectory)
						.getDriver();
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
	public void fw_init(String browserName, String browserType, String browserVersion, String browserPlatform, String defaultDirectory) throws Exception {
		if (driver == null) {
			try {
				driver = (AppiumDriver) new AppiumDriverProperties().setBrowserType(_browserType)
																		.setBrowserName(_browserName)
																		.setBrowserVersion(_version)
																		.setResolution(_resolution)
																		.setRemoteHub(_remoteHub)
																		.setPlatform(_platform)
																		.setProxyServer(_proxy)
																		.setDefaultDirectory(_defaultDirectory)
																		.getDriver();
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
	protected List<MobileElement> fw_findElementsBy(WebDriver drvr, ParameterObject params) throws NoSuchElementException
	{
		final List<MobileElement> objectList = new ArrayList<>();

		String _message = String.format(" FOUND with property[%s] = [%s] ", params.getPropertyType(), params.getPropertyName());
		String _message2 = "";
		List<MobileElement> temp_lst = (drvr == null ? (AppiumDriver) driver : (AppiumDriver) drvr).findElements(getIdentifier(params.getPropertyType(), params.getPropertyName()));
		if (temp_lst.size() > 0)
		{
			int size = temp_lst.size();
			if (params.isElementDisplayed() || params.isElementEnabled()) {

				if (params.isElementDisplayed() && params.isElementEnabled()) {
					_message2 = "DISPLAYED AND ENABLED";
					objectList.addAll(temp_lst.stream().filter(e -> ((MobileElement) e).isDisplayed() && ((MobileElement) e).isEnabled()).collect(Collectors.toList()));
				} else if (params.isElementDisplayed()) {
					_message2 = "DISPLAYED";
					objectList.addAll(temp_lst.stream().filter(e -> params.isElementDisplayed()).collect(Collectors.toList()));
				} else if (params.isElementEnabled()) {
					_message2 = "ENABLED";
					objectList.addAll(temp_lst.stream().filter(e -> ((MobileElement) e).isEnabled()).collect(Collectors.toList()));
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

	@Override
	public List<ElementSearch> fw_findElements(Map<String, Object> params) throws Exception {
		List<ElementSearch> listObj = new ArrayList<ElementSearch>();
		listObj.addAll(fw_findElementsWithLogic(new ParameterObject(params)).stream().map(com.hbcd.container.appium.ElementObject::new).collect(Collectors.toList()));
		return listObj;
	}
}