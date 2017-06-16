package com.hbcd.scripting.core.fluentInterface;

/**
 * Created by ephung on 9/28/16.
 */

public interface WaitForAction {
    int untilDisappear() throws Exception;
    int untilContainTextChange(String originalText) throws Exception;
    int untilAttribueValueChangeContainText(String key, String changeToValue) throws Exception;
    int untilAttribueValueNotContainText(String key, String changeToValue) throws Exception;
    int untilContainTextHasText(String newText) throws Exception;
}
