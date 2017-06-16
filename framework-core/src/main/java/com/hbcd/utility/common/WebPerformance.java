package com.hbcd.utility.common;

public enum WebPerformance {

    NAVIGATION_START("navigationStart"),
    REDIRECT_START("redirectStart"),
    UNLOAD_EVENT_START("unloadEventStart"),
    UNLOAD_EVENT_END("unloadEventEnd"),
    REDIRECT_END("redirectEnd"),
    FETCH_START("fetchStart"),
    DOMAIN_LOOKUP_START("domainLookupStart"),
    DOMAIN_LOOKUP_END("domainLookupEnd"),
    CONNECT_START("connectStart"),
    CONNECT_END("connectEnd"),
    REQUEST_START("requestStart"),
    RESPONSE_START("responseStart"),
    RESPONSE_END("responseEnd"),
    DOM_LOADING("domLoading"),
    DOM_INTERACTIVE("domInteractive"),
    DOM_CONTENT_LOADED_EVENT_START("domContentLoadedEventStart"),
    DOM_CONTENT_LOADED_EVENT_END("domContentLoadedEventEnd"),
    DOM_COMPLETE("domComplete"),
    LOAD_EVENT_START("loadEventStart"),
    LOAD_EVENT_END("loadEventEnd"),
    PERFORMANCE_END("performanceEnd"),
    PERFORMANCE_END2("performanceEnd2"),
    PERFORMANCE_START("performanceStart");

    WebPerformance(final String value) {
        this.value = value;
    }

    public String getvalue() {
        return value;
    }

    private String value;
}
