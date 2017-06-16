package com.hbcd.container.common;

public enum PropertiesNames{
	ID("getID", "ID"),
	CLASSNAME("getClassName", "CLASSNAME"),
	NAME("getName", "NAME"),
	CSSSELECTOR("getCssSelector", "CSSSELECTOR"),
	LINKTEXT("getLinkText", "LINKTEXT"),
	PARTIALLINKTEXT("getPartialLinkText", "PARTIALLINKTEXT"),
	TAGNAME("getTagName", "TAGNAME"),
	XPATH("getXPath", "XPATH");

	PropertiesNames(final String value, final String name){
		this._value=value;
		this._name = name;
	}
	
	public String getvalue(){
		return _value;
	}
	public String getName() { return _name; }
	private String _value;
	private String _name;
}