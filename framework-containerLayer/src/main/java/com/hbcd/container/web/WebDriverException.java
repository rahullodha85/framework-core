package com.hbcd.container.web;

import org.openqa.selenium.NoSuchElementException;

public class WebDriverException extends Exception{
 
	WebDriverException(String errorMessage) throws Exception {
		 if(errorMessage.equalsIgnoreCase("element not found")||errorMessage.equalsIgnoreCase("no elements returned"))
			 throw new NoSuchElementException(errorMessage);
		 else if(errorMessage.equalsIgnoreCase("multiple elements found"))
		 {
			 throw new Exception(errorMessage);
		 }
	}
}
