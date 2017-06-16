package com.hbcd.container.web;

import com.hbcd.container.common.ElementObjectBase;
import com.hbcd.container.common.ParameterObject;
import com.hbcd.containerinterface.ElementSearch;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ElementObject extends ElementObjectBase<WebElement> implements ElementSearch {
//    private WebElement element = null;

    public ElementObject(WebElement element) {
        // check null value
        super(0);  //0 : Element is THERE
        this.element = element;
        this._originalStyle =  element.getAttribute("style");
    }

    @Override
    protected List<ElementSearch> fw_findElementsBy(String PropType, String PropValue)
    {
        //Start HERE
        //Add logic here to Parse Properties Type? Loop?
        List<ElementSearch> temp_lst = new ArrayList<>();
        if (PropValue.contains("|"))
        {
            String[] valueList = PropValue.split("\\|");
            for (String eachValue : valueList) {
                if ((eachValue != null) && (eachValue.length() > 0))
                {
                    temp_lst.addAll(((WebElement)element).findElements(Utilities.getIdentifier(PropType, eachValue)).stream().map(ElementObject::new).collect(Collectors.toList()));
                }
            }
        }
        else
        {
            temp_lst.addAll(((WebElement)element).findElements(Utilities.getIdentifier(PropType, PropValue)).stream().map(ElementObject::new).collect(Collectors.toList()));
        }
        //END HERE
        return temp_lst;
    }

    @Override
    public List<ElementSearch> fw_findElements(Map<String, Object> params) throws Exception {
        ParameterObject parameters = new ParameterObject(params);
        List<ElementSearch> objectList = new ArrayList<>();
        if (element == null)
            throw new WebDriverException("no elements returned");
        //Update This Section Too

//        objectList.addAll(((WebElement)element).findElements(
//                Utilities.getIdentifier(parameters.getPropertyType(), parameters.getPropertyName())).stream().map(ElementObject::new).collect(Collectors.toList()));
//        return objectList;
        return fw_findElementsBy(parameters.getPropertyType(), parameters.getPropertyName());
    }

    @Override
    public void fw_click() { element.click(); }

    @Override
    public void fw_javascriptClick() {
//		WrapsDriver wd = (WrapsDriver)element;
//		JavascriptExecutor executor = (JavascriptExecutor)wd;
//		executor.executeScript("arguments[0].click();", element);
        WrapsDriver wd = (WrapsDriver) element;
        ((JavascriptExecutor) wd.getWrappedDriver()).executeScript("arguments[0].click();", element);
    }

    @Override
    public void fw_sendKeys(CharSequence... keysToSend) { element.sendKeys(keysToSend); }

    @Override
    public void fw_clear() { element.clear(); }

    @Override
    public void fw_selectByIndex(int index) { new Select(element).selectByIndex(index); }

    @Override
    public void fw_selectByValue(String value) {
        new Select(element).selectByValue(value);
    }

    @Override
    public void fw_selectByVisibleText(String text) { new Select(element).selectByVisibleText(text); }

    @Override
    public String fw_getDropDownSelectedText(){  return new Select(element).getFirstSelectedOption().getText(); }

    @Override
    public String fw_getTagName() {
        return element.getTagName();
    }

    @Override
    public String fw_getAttribute(String name) { return element.getAttribute(name); }

    @Override
    public String fw_getText() { return element.getText(); }

    @Override
    public boolean fw_isEnabled() {
        return element.isEnabled();
    }

    @Override
    public boolean fw_isDisabled() {
        return (!element.isEnabled());
    }

    @Override
    public boolean fw_isDisplayed() {
        return element.isDisplayed();
    }

    @Override
    public String fw_getClass() { return element.getClass().toString(); }

    @Override
    public Dimension fw_getSize() { return element.getSize(); }

    @Override
    public void fw_submit() { element.submit(); }

    @Override
    public void fw_enter() { element.sendKeys(Keys.RETURN); }

    @Override
    public void fw_elementHighlight() {
            WrapsDriver wd = (WrapsDriver) element;
            _originalStyle = _originalStyle.replace(_elementHighLightStyle, ""); //Remove Highlight
            ((JavascriptExecutor) wd.getWrappedDriver()).executeScript(_scriptHightLightAndMove, element, _originalStyle + "color: deeppink; border: 2px solid limegreen; z-index: 5009;");
    }

    @Override
    public void fw_elementBlink(int i) {
        boolean isHighlight = false;
        try {
            WrapsDriver wd = (WrapsDriver) element;
            _originalStyle =  element.getAttribute("style");
            //Remove Highlight if exist
            if (_originalStyle.contains(_elementHighLightStyle)) {
                isHighlight = true;
                _originalStyle = _originalStyle.replace(_elementHighLightStyle, "");
            }
            for (int count = 0; count <= i; count++) {
                Thread.sleep(150);
                ((JavascriptExecutor) wd.getWrappedDriver()).executeScript(_scriptHighLightOnly, element, _originalStyle + _elementHighLightStyle);
                Thread.sleep(150);
                ((JavascriptExecutor) wd.getWrappedDriver()).executeScript(_scriptHighLightOnly, element, _originalStyle );
            }
            if (isHighlight)  //Restored Highlight
            {
                ((JavascriptExecutor) wd.getWrappedDriver()).executeScript(_scriptHighLightOnly, element, _originalStyle + _elementHighLightStyle);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
//            Log.Error("ERROR ToggleType: configuration not loaded properly\n", e);
        }
    }

    @Override
    public void fw_scrollToElement() {
        WrapsDriver wd = (WrapsDriver) element;
        ((JavascriptExecutor) wd.getWrappedDriver()).executeScript(_javaScriptScrollToElement, element);
    }

    @Override public void fw_elementHighlightOnly(){
        WrapsDriver wd = (WrapsDriver) element;
        _originalStyle = _originalStyle.replace(_elementHighLightStyle, ""); //Remove Highlight
        ((JavascriptExecutor) wd.getWrappedDriver()).executeScript(_scriptHighLightOnly, element, _originalStyle + _elementHighLightStyle);
    }

    @Override
    public Object fw_executeJavaScript(String script) {
        WrapsDriver wd = (WrapsDriver) element;
        return ((JavascriptExecutor) wd.getWrappedDriver()).executeScript(script, element);
    }

    //executeAsyncScript
    @Override
    public Object fw_executeAsyncJavaScript(String script) {
        WrapsDriver wd = (WrapsDriver) element;
        return ((JavascriptExecutor) wd.getWrappedDriver()).executeAsyncScript(script, element);
    }

    @Override
    public void fw_overlayMessage(String msg, String color) {
        WrapsDriver wd = (WrapsDriver) element;
        ((JavascriptExecutor) wd.getWrappedDriver()).executeScript(Utilities.overlayJS(msg, color));
    }
    @Override
    public void fw_overlayMessageBoxRePosition(String color) {
        WrapsDriver wd = (WrapsDriver) element;
        ((JavascriptExecutor) wd.getWrappedDriver()).executeScript(Utilities.overlayJSReposition(color));
    }

    @Override
    public void fw_overlayStepNumber(String number, String color) {
        WrapsDriver wd = (WrapsDriver) element;
        String scrpt= Utilities.overlayNotificationNumber(number, color);
        ((JavascriptExecutor) wd.getWrappedDriver()).executeScript(scrpt, element);
    }

}
