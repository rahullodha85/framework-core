package com.hbcd.containerinterface;

import java.util.List;
import java.util.Map;

public interface SearchElements {
	public List<ElementSearch> fw_findElements(Map<String, Object> params) throws Exception;
	public ElementSearch fw_findElement(Map<String, Object> params) throws Exception;
	public void fw_overlayMessage(String msg, String color);
	public void fw_overlayMessageBoxRePosition(String color);
	public void fw_overlayStepNumber(String number, String color);
	public Object fw_executeJavaScript(String scrpt);
}
