package com.hbcd.container.appium;

import org.openqa.selenium.NoSuchElementException;

public class AppiumDriverException extends Exception{
 
	AppiumDriverException(String errorMessage) throws Exception {
		 if(errorMessage.equalsIgnoreCase("element not found")||errorMessage.equalsIgnoreCase("no elements returned"))
			 throw new NoSuchElementException(errorMessage);
		 else if(errorMessage.equalsIgnoreCase("multiple elements found"))
		 {
			 throw new AppiumDriverException(errorMessage);
		 }
	}
}
