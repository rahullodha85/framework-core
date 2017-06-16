//************************************************************************************           
//    class   : ContainerUtility
//   
//    Author  : Quality Assurance Automation Team  
//
//    Pre-Req : Container-Layer provided the basic implementation functions (FindElement) from the content
//
//    Purpose : Provide Helper Functions to allocate element with various attributes   
//
//    Date    : June 27, 2014
//
//    Notes   :  
//   
//***********************************************************************************

package com.hbcd.core.genericfunctions;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hbcd.container.common.ElementObjectBase;
import com.hbcd.container.common.PropertiesNames;
import com.hbcd.container.common.SafeDriver;
import com.hbcd.containerinterface.ElementSearch;
import com.hbcd.containerinterface.SearchElements;
import com.hbcd.logging.log.LogInfo;
import com.hbcd.storage.data.SafeStorage;
import com.hbcd.utility.entity.ObjectProperties;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class Utility {

//	static final Logger log = LogManager.getLogger(GenericFunctions.class);

    static ElementSearch coreFindElement(ObjectProperties obj, LogInfo _log) {
        //return coreFindElement(obj, false, _log);
        //return coreFindElement(obj, true, _log);
        return coreFindElement(null, obj, true, _log, true);
    }

    static ElementSearch coreFindElement(ObjectProperties obj, boolean SuppressError, LogInfo _log) {
        return coreFindElement(null, obj, SuppressError, _log, true);
    }

    //Storage Find Element
    static ElementSearch coreFindElement(SearchElements content, ObjectProperties obj, LogInfo _log) {
        return coreFindElement(content, obj, true, _log, true);
    }

    //Storage Find Element
    static ElementSearch coreFindElement(SearchElements content, ObjectProperties obj,
                                         boolean SuppressError, LogInfo _log, boolean isHighlight) {
        ElementSearch element = null;
        try {
            //User Define ThreadSleep time
            if (obj != null) {
                if (obj.getUserDefinedThreadWaitTime() > 0)
                {
                    Thread.sleep(obj.getUserDefinedThreadWaitTime() * 1000);
                }
            }
            //long startTime = System.nanoTime();
            Instant start = Instant.now();
            element = Utility.coreFindElement(obj,(content == null) ? SafeDriver.Current() : content, _log);
            Instant end = Instant.now();
            //long stopTime = System.nanoTime();
            _log.add(String.format(", Took: %s seconds to EXECUTE Find.", (Duration.between(start, end).toMillis() / 1000)));

            if (element.fw_isFound()) {
                element.fw_overlayMessage(String.format("[%s] ELEMENT FOUND!", _log.toString()), "green");
                ////element.fw_elementHighlight();

                element.fw_scrollToElement();
                element.fw_overlayMessageBoxRePosition("green");
                if (isHighlight) {
                    element.fw_elementBlink(2);
                    element.fw_elementHighlightOnly();
                }
                //element.fw_overlayStepNumber(ContainerUtility.GetCount(), "#dc143c");
                //System.out.println(element.fw_getAttribute("outerHTML"));
            } else if (element.fw_isMulitpleFound()) {
                SafeDriver.Current().fw_overlayMessage(String.format("[%s]!", _log.toString()), "green");
                SafeDriver.Current().fw_overlayMessageBoxRePosition("green");
                _log.add(", MULTIPLE ELEMENTS FOUND");
            } else if (element.fw_isNotFound()) {
                _log.add(", ELEMENT NOT FOUND");
                SafeDriver.Current().fw_overlayMessage(
                        String.format("[%s]!", _log.toString()), "red");
                SafeDriver.Current().fw_overlayMessageBoxRePosition("red");
                //throw new CoreException("Null value returned");
            }
        } catch (Exception e) {
            _log.print();
            if (!SuppressError) {
                e.printStackTrace();
            }
        }
        return element;
    }


//    //Regular Find Element
//    static ElementSearch coreFindElement(ObjectProperties obj, boolean SuppressError, LogInfo _log) {
//
//        return coreFindElement(null, obj, SuppressError, _log);
//        ElementSearch element = null;
//        try {
//
//            //long startTime = System.nanoTime();
//            Instant start = Instant.now();
//            element = ContainerUtility.coreFindElement(obj,
//                    SafeDriver.Current(), _log);
//            Instant end = Instant.now();
//            //long stopTime = System.nanoTime();
//            _log.add(", Took:" + (Duration.between(start, end).toMillis() / 1000) + " seconds to EXECUTE Find");
//
//            if (element.fw_isFound()) {
//                element.fw_overlayMessage(
//                        "[" + /* obj.getObjectName() */_log.toString()
//                                + "] ELEMENT FOUND!", "green");
//                element.fw_elementHighlight();
//
//
//                //System.out.println(element.fw_getAttribute("outerHTML"));
//            } else if (element.fw_isMulitpleFound()) {
//                _log.add(", MULTIPLE ELEMENTS FOUND");
//                SafeDriver.Current().fw_overlayMessage(
//                        "[" + /* obj.getObjectName() */_log.toString()
//                                + "]!", "green");
//            } else if (element.fw_isNotFound()) {
//                _log.add(", ELEMENT NOT FOUND");
//                SafeDriver.Current().fw_overlayMessage(
//                        "[" + /* obj.getObjectName() */_log.toString()
//                                + "]!", "red");
//                SafeDriver.Current().fw_executeJavaScript("$('#SeleniumMessageOverlay' ).position({ my: 'left+3 top+3', at: 'left top', of: window});");
//
//                //throw new CoreException("Null value returned");
//            }
//
//
//        } catch (Exception e) {
//            _log.print();
//            if (!SuppressError) {
//                e.printStackTrace();
//            }
//        }
//
//        return element;
//    }

    private static ElementSearch coreFindElementByAttribute(ObjectProperties obj, PropertiesNames propertyNames, String propertyValue, SearchElements content, LogInfo _log, boolean isFirst) throws Exception {
        List<ElementSearch> elements;

        String ObjHeirachy = "";
        boolean isParent = (obj.getIsParent() != 0);
        if (content.getClass().getName().contains("ElementObject"))    // DriverObject is
        // Parent &
        // WebObjec is a
        // child
        {
            ObjHeirachy = "Child ";
        } else if (content.getClass().getName().contains("DriverObject")) {
            if (isParent && (obj.child != null)) {
                ObjHeirachy = "Parent ";
            }
        }

        _log.add(String.format(", CHECK for Element with PROPERTY: [%s = %s]", propertyNames, propertyValue));
        elements = coreFindElementWithWait(obj, propertyNames, propertyValue,
                content, _log, isFirst);

        if (elements.size() == 1) { // find only one element
            if (elements.get(0).fw_isFound()) {
                _log.add(", ELEMENT FOUND");
                if (isParent) {
                    _log.add(", LOOK for CHILD Element");
                    // log.info(obj.getObjectName() + "-Element is Parent");
                    try {
                        return coreFindElement(obj.child, elements.get(0), _log);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//                    ElementObjectBase returnObject = new ElementObjectBase(0);
//                    returnObject.add(elements.get(0));
//                    return returnObject;
                    return elements.get(0);
                }
            } else {
                _log.add(", ERROR in Element FOUND");
            }
        } else if (elements.size() > 1) { // multiple elements

            _log.add(", MULTIPLE Elements FOUND");
            try {
                ElementSearch woe = coreMultipleElementsFound(elements,
                        obj, propertyNames, content, _log);
                if (woe.fw_isFound()) {
                    if (isParent) {
                        _log.add(", IS PARENT");
                        return coreFindElement(obj.child, woe, _log);
                    } else {
                        return woe;
                    }
                } else if (woe.fw_isMulitpleFound()) {

                    if (isParent) {
                        _log.add(", ERROR in MULTI-ELEMENTS FOUND AS PARENT");
                        return new ElementObjectBase(1);
                    } else {
                        _log.add(", MULTIPLE ELEMENTS FOUND");
                        return woe;
                    }
                } else {
                    return new ElementObjectBase(1);
                }
            } catch (Exception e) {
                throw new CoreException("Element not found");
            }
        }
        return new ElementObjectBase(1);  //NOT FOUND
    }


    private static ElementSearch coreFindElement(ObjectProperties obj,
                                                 SearchElements content, LogInfo _log) throws Exception {

        boolean isFirst = true;
        String ObjHeirachy = "";
        boolean isParent = (obj.getIsParent() != 0);
        if (content.getClass().getName().contains("ElementObject"))    // DriverObject is Parent &
        // WebObjec is a child
        {
            ObjHeirachy = "Child ";
        } else if (content.getClass().getName().contains("DriverObject")) {
            if (isParent && (obj.child != null)) {
                ObjHeirachy = "Parent ";
            }
        }
        _log.add(String.format("%s Object Name: %s ", ObjHeirachy, obj.getObjectName()));
        for (PropertiesNames propertyNames : PropertiesNames.values()) {

            Method getProperties = obj.getClass().getDeclaredMethod(
                    propertyNames.getvalue());
            String propertyValue = (String) getProperties.invoke(obj, null);
            if (propertyValue != null && !"".equals(propertyValue)) {
                //( ObjectProperties obj, SearchElements content, PropertiesNames propertyNames, String propertyValue, LogInfo _log) throws Exception
                ElementSearch rtrnE = Utility.coreFindElementByAttribute(obj, propertyNames, propertyValue, content, _log, isFirst);
                isFirst = false;
                if (rtrnE.fw_isFound() || rtrnE.fw_isMulitpleFound()) {
                    return rtrnE;
                }
            }
        } // End For Loop
        return new ElementObjectBase(1);  //NOT FOUND
    }

    private static ElementSearch coreMultipleElementsFound(List<ElementSearch> list,
                                                           ObjectProperties obj, PropertiesNames propertyNames, SearchElements content, LogInfo _log)
            throws Exception {

        boolean isPreviousFilter = false;
        List<ElementSearch> elements = new ArrayList<ElementSearch>();

        //HTML CONTAIN TEXT
        String comparisonText = obj.getContainsText();
        if (!comparisonText.equalsIgnoreCase("")) {
            elements = coreContainsText(list, obj, propertyNames, _log);

            isPreviousFilter = true;

            if (elements.size() == 1) {
                _log.add(String.format(", FOUND element CONTAINS TEXT [%s]", comparisonText));
                return elements.get(0);
            }

            if (elements.size() == 0) {
                _log.add(String.format(", NOT FOUND ANY element contains text [%s]", comparisonText));

            }
        } else {
            _log.add(", SKIP CONTAINS TEXT CHECK");
        }
        // End Contain Text

        //ATTRIBUTE VALUE HAS EXACT TEXT Combo (name/class/css/tag ...etc) with EXACT Attribute value
        String attKey = obj.getAttributeKey1();
        String attValue = obj.getAttributeValue1();
//		if (!attKey.equalsIgnoreCase("") || !attValue.equalsIgnoreCase("")) {
        if (!attKey.equalsIgnoreCase("")) {
            elements = coreComboFind((isPreviousFilter ? elements : list), obj, attKey, attValue,
                    true /* Exact String compare */, _log);

            isPreviousFilter = true;

            if (elements.size() == 1) {
                _log.add(String.format(", FOUND Element with attribute [%s] = [%s]", attKey, attValue));

                return elements.get(0);
            }
            if (elements.size() == 0) {

                _log.add(String.format(", NOT FOUND ANY Element with attribute [%s] = [%s]", attKey, attValue));
            }
        } else {
            _log.add(", SKIP ATTRIBUTE /W EXACT TEXT CHECK (Attrb1)");
        }
        // End EXACT Combo

        //ATTRIBUTE VALUE CONTAINS TEXT Combo (name/class/css/tag ...etc) with EXACT Attribute value
        String attKey2 = obj.getAttributeKey2();
        String attValue2 = obj.getAttributeValue2();
        //if (!attKey2.equalsIgnoreCase("") || !attValue2.equalsIgnoreCase("")) {
        if (!attKey2.equalsIgnoreCase("")) {

            elements = coreComboFind((isPreviousFilter ? elements : list), obj, attKey2, attValue2,
                    false /* Not EXACT compare - contain string compare */, _log);

            isPreviousFilter = true;

            if (elements.size() == 1) {
                _log.add(String.format(", FOUND Element with attribute [%s] contains text [%s]", attKey2, attValue2));

                return elements.get(0);
            }
            if (elements.size() == 0) {
                _log.add(String.format(", NOT FOUND ANY Element with attribute [%s] contains text [%s]", attKey2, attValue2));
            }
        } else {
            _log.add(", SKIP ATTRIBUTE /W CONTAINS TEXT CHECK (Attrb2)");
        }
        // End CONTAINS Combo

        // Check ith Element (index [start from 1] order of the element in list)
        // Element Index
        if (obj.getIth() > 0) {
            //Previously Filter and size still 0 then take i
            //Otherwise select from Previously Filter list
            elements = coreIthElement((isPreviousFilter && elements != null && elements.size() > 0) ? elements : list, obj, _log);
            if (elements.size() == 1) {
                return elements.get(0);
            }
        } else if (obj.getIth() == -1) {
            //RANDOM?
        }
        // End Element Index


        // If current property is failed to find element. Continuing by try
        // other Properties one by one
        boolean isStart = false;
        boolean isFirst = true;
        List<ElementSearch> localList = new ArrayList<ElementSearch>(isPreviousFilter ? elements : list);
        for (PropertiesNames pnames : PropertiesNames.values()) {
            Method getProp = obj.getClass()
                    .getDeclaredMethod(pnames.getvalue());
            String propValue = (String) getProp.invoke(obj, null);

            if (pnames == propertyNames) {
                isStart = true;
                continue;
            }

            if (!isStart) {
                continue;
            }

            if (propValue != null && !"".equals(propValue)) {

                _log.add(String.format(", RETRY FINDING WITH %s values %s", pnames, propValue));
                //localList = coreReduceElements(localList, pnames, propValue, content, _log);
                ElementSearch el = Utility.coreFindElementByAttribute(obj, pnames, propValue, content, _log, isFirst);
                isFirst = false;
                if (el.fw_isFound()) {
                    return el;
                }
            }
        }

        ElementObjectBase returnObject = new ElementObjectBase(2);
        for (ElementSearch e : isPreviousFilter ? elements : list) {
            returnObject.add(e);
        }
        return returnObject; //MULTIPLE ELEMENT FOUND

    }

    //NOT USE
    private static List<ElementSearch> coreReduceElements(List<ElementSearch> list,
                                                          PropertiesNames pnames, String propValue, SearchElements content, LogInfo _log) {

        List<ElementSearch> reducedElements = new ArrayList<ElementSearch>();

        for (int i = 0; i < list.size(); i++) {

            String elementsPropName = list.get(i)
                    .fw_getAttribute(pnames.name());

            if (elementsPropName.equalsIgnoreCase(propValue)) {

                reducedElements.add(list.get(i));
            }
        }

        return reducedElements;

    }

//	private static List<ElementSearch> coreFindElementWithWait(ObjectProperties obj,
//			PropertiesNames propertyType, String propertyName,
//			SearchElements content, LogInfo _log)
//	{
//		List<ElementSearch> result;
//		try {
//			result = coreAjaxWait(obj, propertyType, propertyName, content, _log);
//			if (result == null)
//			{
//				
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return result;
//	}

    private static List<ElementSearch> coreFindElementWithWait(ObjectProperties obj,
                                                               PropertiesNames propertyType, String propertyName,
                                                               SearchElements content, LogInfo _log, boolean isFirst) throws Exception {

//		Boolean isJQueryExist = false;
//		Boolean object = false;
//		long pollTime = 5;
//
//		if (isFirst)  //CHECK FOR WAIT ONLY FIRST TIME.
//		{
//			isJQueryExist = ((Boolean) SafeDriver.Current().executeScript(
//					"return window.jQuery != undefined", null)).booleanValue();
//			if (isJQueryExist) {
//				// JQUERY
//				_log.add(", Utilize AJAX + FLUENTWAIT (6 Secs with 0.25 sec Poolling) FIND");
//				while (!object) {
//					object = ((Boolean) SafeDriver.Current().executeScript(
//							"return jQuery.active == 0", null)).booleanValue();
//					if (object) {
//						return content.fw_findElements(propertyType, propertyName, isFirst);
//					} else {
//						Thread.sleep(pollTime * 500);
//	
//						pollTime += 5;
//						if (pollTime == 60)  //Max 30 seconds. 
//						{
//							_log.add(", AJAX script failed");
//						}
//					}
//				}
//			} 
//			else // No JQUERY
//			{
//				_log.add(", Utilize ONLY FLUENTWAIT (6 Secs with 0.25 sec Poolling) FIND");
//				return content.fw_findElements(propertyType, propertyName, isFirst);
//			}
//		}
//		else  //SECOND ON no check for wait.
//		{
//			_log.add(", NO WAIT FIND");
//			return content.fw_findElements(propertyType, propertyName, isFirst);
//		}

        if (isFirst) {
//            _log.add(", Utilize AJAX + FLUENTWAIT (6 Secs with 0.25 sec Poolling) TO FIND ELEMENT");
            _log.add(String.format(", Utilize AJAX + EXPLICIT WAIT FOR %s Secs TO FIND ELEMENT", obj.getUserDefinedExplicitWaitTime()));
        } else {
            _log.add(", FIND ELEMENT WITHOUT WAIT");
        }
//        return content.fw_findElements(propertyType, propertyName, isFirst, obj.getUserDefinedExplicitWaitTime());

        return content.fw_findElements(Maps.newHashMap(
                                                        ImmutableMap.<String, Object>builder().
                                                                put("PropertyType", propertyType.getName()).
                                                                put("PropertyName", propertyName).
                                                                put("IsFirstTime", isFirst).
                                                                put("MaxUserDefinedWaitTime", obj.getUserDefinedExplicitWaitTime()).
                                                                put("IsElementNotDisplay", obj.getIsSelectedWithNotVisible()).
                                                                put("IsElementDisabled", obj.getIsSelectedWithDisable()).
                                                                build()
                                                       )
                                       );
        //return new ArrayList<ElementSearch>();
    }

    private static List<ElementSearch> coreIthElement(List<ElementSearch> list,
                                                      ObjectProperties obj, LogInfo _log) throws Exception {

        List<ElementSearch> reducedElements = new ArrayList<ElementSearch>();

        int i = obj.getIth();

        if (i == 0) {
            _log.add(", SKIP ith ELEMENT CHECK");
            return reducedElements;// returns empty list
        }

        _log.add(", CHECK FOR ith ELEMENT");
        if (i > 0 && i <= list.size()) {
            reducedElements.add(list.get(i - 1));
            _log.add(String.format(", FOUND Element with index %dth from the list", i));
            return reducedElements;
        }

        if (i > list.size()) {
            _log.add(String.format(", i (with value %d) is OUT OF RANGE [should be between 1 and %d]", i, list.size()));
            reducedElements.add(new ElementObjectBase(1));  //NOT FOUND
        }

        return reducedElements;

    }

    private static List<ElementSearch> coreContainsText(List<ElementSearch> list,
                                                        ObjectProperties obj, PropertiesNames propertyNames, LogInfo _log) {

        String comparisonText = obj.getContainsText();
        List<ElementSearch> reducedElements = new ArrayList<ElementSearch>();

        for (int i = 0; i < list.size(); i++) {

            boolean containsText = list.get(i).fw_getAttribute("outerHTML")
                    .contains(comparisonText);

            if (containsText) {
                reducedElements.add(list.get(i));
            }
        }

        return reducedElements;

    }

    private static List<ElementSearch> coreComboFind(List<ElementSearch> list,
                                                     ObjectProperties obj, String key, String value, boolean isExact,
                                                     LogInfo _log) {

        List<ElementSearch> reducedElements = new ArrayList<ElementSearch>();

        for (int i = 0; i < list.size(); i++) {

            boolean find = false;
            String attribValue = list.get(i).fw_getAttribute(key);

            if (attribValue == null) {
                attribValue = "";
            }

            if (isExact) {
                find = attribValue.equals(value);
            } else {
                find = attribValue.contains(value);
            }

            if (find) {

                reducedElements.add(list.get(i));
            }
        }

        return reducedElements;
    }

    //Extra Function
    private static void Blink(ElementSearch e, int number, boolean isBlink)
    {
        if (isBlink) e.fw_elementBlink(number);
    }

    //Extra Function
    private static void ScrollToElement(ElementSearch e, boolean isScroll)
    {
        if(isScroll) e.fw_scrollToElement();
    }

    //Extra Function
    private static void HighlightElement(ElementSearch e, boolean isHighlight)
    {
        if (isHighlight) e.fw_elementHighlightOnly();
    }

    private static String GetCount()
    {
        Object count=null;
        Integer newCount;
        count = SafeStorage.Current().get("__StepCount__");
        if (count == null)
        {
            SafeStorage.Current().save("__StepCount__", 0);
        }
        newCount = (int)SafeStorage.Current().get("__StepCount__") + 1;
        SafeStorage.Current().save("__StepCount__", newCount);
        return newCount.toString();
    }
}
