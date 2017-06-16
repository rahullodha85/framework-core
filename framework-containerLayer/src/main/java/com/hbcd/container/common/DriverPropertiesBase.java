package com.hbcd.container.common;

import com.hbcd.containerinterface.DriverProperties;
import org.apache.commons.collections.map.HashedMap;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DriverPropertiesBase implements DriverProperties {
	protected DesiredCapabilities capabilities;
	protected String _browserType;
	protected String _browserName;
	protected String _version;
	protected String _resolution;
	protected String _remoteHub;
	protected  String _platform;
	protected String _proxy;
	protected String _defaultDirectory;

	protected class Resolution
	{
		String _width="";
		String _height="";
		boolean _isSetResolution=false;

		public Resolution(String r)
		{
			_isSetResolution = init(r);
		}

		private boolean init(String resolution)
		{
			if ((resolution==null) || resolution.trim().isEmpty()) return false;
			String tmpR = resolution.toLowerCase();
			tmpR = tmpR.replace(" ", "");
			Pattern p = Pattern.compile("^(\\d+x\\d+)");
			Matcher m = p.matcher(tmpR);
			boolean isMatch = m.matches();
			boolean isSetResolution = false;
			if (isMatch) {
				String number[] = tmpR.split("x");
				if (number.length == 2) {
					if ((Integer.parseInt(number[0]) != 0) && ((Integer.parseInt(number[1]) != 0))) {
						_width = number[0];
						_height = number[1];
					}
				}
			}

			isSetResolution = (!_width.isEmpty() && !_height.isEmpty());
			return isSetResolution;
		}

		public String getWidth()
		{
			return _width;
		}

		public int getWidthAsInt()
		{
			if (_width.isEmpty()) return 0;
			return Integer.parseInt(_width);
		}

		public String getHeight()
		{
			return _height;
		}

		public int getHeightAsInt()
		{
			if(_height.isEmpty()) return 0;
			return Integer.parseInt(_height);
		}

		public boolean isResolutionSet()
		{
			return _isSetResolution;
		}
	}

	public DriverPropertiesBase setBrowserName(String bn)
	{
		_browserName=bn;
		return this;
	}

	public DriverPropertiesBase setBrowserVersion(String bv)
	{
		_version=bv;
		return this;
	}

	public DriverPropertiesBase setBrowserType(String bt)
	{
		_browserType=bt;
		return this;
	}

	public DriverPropertiesBase setResolution (String rs)
	{
		_resolution=rs;
		return this;
	}

	public DriverPropertiesBase setRemoteHub(String rh)
	{
		_remoteHub=rh;
		return this;
	}

	public DriverPropertiesBase setProxyServer(String ps)
	{
		_proxy=ps;
		return this;
	}

	public DriverPropertiesBase setPlatform(String p)
	{
		_platform=p;
		return this;
	}

	public DriverPropertiesBase setDefaultDirectory(String dd)
	{
		_defaultDirectory=dd;
		return this;
	}

	protected String separatorFix(String filePath)
	{
		filePath = filePath.replace("/", System.getProperty("file.separator"));
		filePath = filePath.replace("\\", System.getProperty("file.separator"));
		return filePath;
	}

	public Object getDriver()
	{
		return null;
	}

}
