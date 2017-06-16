package com.hbcd.container.web;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.hbcd.ContainerUtility.Log;
import com.hbcd.container.common.DriverPropertiesBase;
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
import java.util.*;

public class WebDriverProperties extends DriverPropertiesBase {


    private List<Integer> versionParsing()
    {
        //Initialized to all Zero for 3 (THREE) elements.
        List<Integer> rtrnVersion = new ArrayList<Integer>() {{
                                                                add(0);
                                                                add(0);
                                                                add(0);
                                                            }};

        String[] version = null;
        if ((_version == null) || (_version.length() == 0)) {
            return rtrnVersion;
        }
        else
        {
            version = _version.split("\\.");
        }
        if ((version != null) && ((version.length >= 1) || (version.length <= 3)))
        {
            try {
                for (int i = 0; ((i < version.length) && (i < 3)) /* make sure max is 3 */; i++) {
                    rtrnVersion.set(i, Integer.parseInt(version[i]));
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
                //Re-initialized to ensure all Zero
                Collections.fill(rtrnVersion, 0);
            }
        }
        return rtrnVersion;
    }

    //v47.0.1 or earlier is regular firefox driver
    //v47.0.2 or later is gecko driver
    private boolean isFireFoxGeckoDriver(List<Integer> browserVersion)
    {
        boolean isGeckoDriver = false;  //default as standard firefox driver
        if (browserVersion.get(0) == 47) {
            if (browserVersion.get(1) == 0)
            {
                if (browserVersion.get(2) > 1 )
                {
                    isGeckoDriver = true;
                }
            } else
            {
                isGeckoDriver = true;
            }
        } else if (browserVersion.get(0) > 47)
        {
            isGeckoDriver = true;
        }
        return isGeckoDriver;
    }

    private String getWindowsArchitect() {
        String rtrnArchitect = "";
        String os = System.getProperty("os.name").toLowerCase().substring(0, 3);
        if (os.equals("win")) {
            if (System.getenv("ProgramFiles(x86)") != null) {
                rtrnArchitect="32.exe";
                System.out.print("Windows 32-bit");
            } else if (System.getProperty("os.arch").indexOf("64") != -1) {
                rtrnArchitect="64.exe";
                System.out.print("Windows 64-bit");
            }
        }
        return rtrnArchitect;

    }

    private String fullFilePathStartWith(String leading)
    {
        String fullPathFileName = "";
        try {
            String os = System.getProperty("os.name").toLowerCase().substring(0, 3);
            com.hbcd.ContainerUtility.Log.Info(os);
            String driverFileName = String.format("%s-%s%s", leading, os, getWindowsArchitect());
            String driverFullPathFileName = String.format("%slib%s%s", System.getProperty("file.separator"), System.getProperty("file.separator"), driverFileName);
            fullPathFileName = separatorFix(_defaultDirectory + driverFullPathFileName);
            File testFile = new File(fullPathFileName);
            if ((testFile == null) || !testFile.exists()) {
                URL dir_url = WebDriverProperties.class.getClassLoader().getResource(driverFileName);
                if (dir_url == null) {
                    String errMsg = String.format("Unable to find file [%s] nor [%s] in Resource.", fullPathFileName, driverFileName);
                    Log.Error(errMsg);
                    return null;
                }
                fullPathFileName = dir_url.getFile();
            }
        }
        catch (Exception ex)
        {
            com.hbcd.ContainerUtility.Log.Error("Error",ex);
            ex.printStackTrace();
        }
        return fullPathFileName;
    }

    private Object getFireFoxDriver()
    {
        try
        {
            Resolution resolution = new Resolution(_resolution);
            if (isFireFoxGeckoDriver(versionParsing())) {
                System.setProperty("webdriver.gecko.driver", fullFilePathStartWith("geckodriver"));
            }
            else
            {
                System.setProperty("webdriver.firefox.marionette", fullFilePathStartWith("geckodriver"));
            }

            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("network.proxy.type", Proxy.ProxyType.DIRECT.ordinal());
            profile.setPreference("webdriver_enable_native_events", true);
            profile.setPreference("webdriver_accept_untrusted_certs", true);
            profile.setPreference("security.warn_leaving_secure", false);
            profile.setPreference("security.warn_leaving_secure.show_once", false);
            profile.setPreference("security.warn_entering_weak", false);
            profile.setPreference("security.warn_entering_weak.show_once", false);
            profile.setPreference("security.warn_entering_secure", false);
            profile.setPreference("security.warn_entering_secure.show_once", false);
            profile.setPreference("security.warn_entering_weak", false);
            profile.setPreference("security.warn_entering_weak.show_once", false);
            profile.setPreference("reader.parse-on-load.enabled", false);

//			//NO CACHE
//			profile.setPreference("browser.cache.disk.enable", false);
//			profile.setPreference("browser.cache.disk.smart_size.enabled", false);
//			profile.setPreference("browser.cache.disk.capacity", 0);
//
//			profile.setPreference("browser.cache.memory.enable", false);
//			profile.setPreference("browser.cache.offline.enable", false);
//			profile.setPreference("network.http.use-cache", false);
//			profile.setPreference("browser.privatebrowsing.autostart", true);


//			profile.addAdditionalPreference("network.proxy.http", "localhost");
//			profile.addAdditionalPreference("network.proxy.http_port", "3128");
//			profile.set_preference("network.proxy.no_proxies_on", "")

            //Mobile
            String viewDeviceName = "test";
            if (resolution.isResolutionSet())
            {
                String responsiveMode = String.format("[{\"width\": \"%s\", \"name\": \"%s\" \"key\":\"%sx%s\", \"height\": %s}]", resolution.getWidth(), viewDeviceName, resolution.getWidth(), resolution.getHeight(), resolution.getHeight());
                profile.setPreference("devtools.responsiveUI.presets", responsiveMode);
            }

            DesiredCapabilities dc = DesiredCapabilities.firefox();
            dc.setCapability(FirefoxDriver.PROFILE, profile);
            if (_browserType.trim().isEmpty() || _browserType.equalsIgnoreCase("LOCAL"))
            {
                Object driver =  new FirefoxDriver(dc);
                Actions actions = new Actions((WebDriver)driver);
                if (resolution.isResolutionSet()) { actions.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).sendKeys("m").keyUp(Keys.SHIFT).keyUp(Keys.CONTROL).perform(); };
                if (resolution.isResolutionSet()) { actions.keyDown(Keys.COMMAND).keyDown(Keys.ALT).sendKeys("m").keyUp(Keys.ALT).keyUp(Keys.COMMAND).perform(); };
                return  driver;
            } else if (_browserType.equalsIgnoreCase("REMOTE") && !_remoteHub.isEmpty())
            {
                try {
                    Object driver =  new RemoteWebDriver(new URL(_remoteHub), dc);
                    Actions actions = new Actions((WebDriver)driver);
                    if (resolution.isResolutionSet()) { actions.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).sendKeys("m").keyUp(Keys.SHIFT).keyUp(Keys.CONTROL).perform(); };
                    return driver;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }
            else
            {
                Object driver =  new FirefoxDriver(dc);
                Actions actions = new Actions((WebDriver)driver);
                if (resolution.isResolutionSet()) { actions.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).sendKeys("m").keyUp(Keys.SHIFT).keyUp(Keys.CONTROL).perform(); };
                return  driver;
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.Error("FireFox Error", ex);
        }
        return null;
    }

    private Object getChromeDriver()
    {
        try
        {
            Resolution resolution = new Resolution(_resolution);
            DesiredCapabilities chromeCapabilities = DesiredCapabilities.chrome();
            String fullPathFileName = fullFilePathStartWith("chromedriver");
            File testFile = new File(fullPathFileName);
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--allow-running-insecure-content");
            options.addArguments("--disable-application-cache");
            options.addArguments("ignore-certificate-errors");
            options.addArguments("no-sandbox");

            //Mobile Emulation.
            if(resolution.isResolutionSet()) {
                Map<String, Object> deviceMetrics = new HashMap<String, Object>();
                deviceMetrics.put("width", resolution.getWidthAsInt());
                deviceMetrics.put("height", resolution.getHeightAsInt());
                deviceMetrics.put("pixelRatio", 3.0);
                Map<String, Object> mobileEmulation = new HashMap<String, Object>();
                mobileEmulation.put("deviceMetrics", deviceMetrics);
                mobileEmulation.put("userAgent", "Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
                options.setExperimentalOption("mobileEmulation", mobileEmulation);
            }

            //Set Capability to Driver.
            chromeCapabilities.setCapability(ChromeOptions.CAPABILITY, options);

            if (_browserType.trim().isEmpty() || _browserType.equalsIgnoreCase("LOCAL"))
            {
                if (testFile.exists()) { //Default Execution Path
                    System.setProperty("webdriver.chrome.driver", fullPathFileName);
                    return new ChromeDriver(chromeCapabilities);
                }
            } else if (_browserType.equalsIgnoreCase("REMOTE") && !_remoteHub.isEmpty())
            {
                try {
                    return new RemoteWebDriver(new URL(_remoteHub), chromeCapabilities);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }
            else
            {
                if (testFile.exists()) { //Default Execution Path
                    System.setProperty("webdriver.chrome.driver", fullPathFileName);
                    return new ChromeDriver(chromeCapabilities);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.Error("Chrome Driver Error", ex);
        }
        return null;
    }

    private Object getInternetExplorerDriver()
    {
        try
        {
            DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
            ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            System.setProperty("webdriver.ie.driver", "lib/IEDriverServer32.exe");
            if (_browserType.trim().isEmpty() || _browserType.equalsIgnoreCase("LOCAL"))
            {
                return new InternetExplorerDriver(ieCapabilities);
            } else if (_browserType.equalsIgnoreCase("REMOTE") && !_remoteHub.isEmpty())
            {
                try {
                    return new RemoteWebDriver(new URL(_remoteHub), ieCapabilities);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }
            else
            {
                return new InternetExplorerDriver(ieCapabilities);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.Error("Internet Explorer Driver Error", ex);
        }
        return null;
    }

    private Object getInternetExplorer64Driver()
    {
        try
        {
            DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
            ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            System.setProperty("webdriver.ie.driver", "lib/IEDriverServer64.exe");

            if (_browserType.trim().isEmpty() || _browserType.equalsIgnoreCase("LOCAL"))
            {
                return new InternetExplorerDriver(ieCapabilities);
            } else if (_browserType.equalsIgnoreCase("REMOTE") && !_remoteHub.isEmpty())
            {
                try {
                    return new RemoteWebDriver(new URL(_remoteHub), ieCapabilities);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }
            else
            {
                return new InternetExplorerDriver(ieCapabilities);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.Error("Internet Explorer 64-bit Driver Error", ex);
        }
        return null;
    }

    private Object getSafariDriver()
    {
        try
        {
            Platform current = Platform.getCurrent();
            if (Platform.MAC.is(current) || Platform.WINDOWS.is(current)) {
                return new SafariDriver();
            }

            return new SafariDriver();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.Error("Safari Driver Error", ex);
        }
        return null;
    }

    private Object getPhantomJSDriver()
    {
        try
        {
            capabilities = DesiredCapabilities.phantomjs();
            capabilities.setBrowserName("phantomjs");
            capabilities.setVersion("firefox");
            capabilities.setPlatform(Platform.ANY);
            capabilities.setJavascriptEnabled(true);
            return new PhantomJSDriver(capabilities);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.Error("PhantomJS Driver Error", ex);
        }
        return null;
    }

    private Object getHTMLUnitDriver()
    {
        try
        {
            //DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            //capabilities.setBrowserName("Mozilla/5.0 (X11; Linux x86_64; rv:24.0) Gecko/20100101 Firefox/24.0");
            //capabilities.setVersion("24.0");
            //driver = new HtmlUnitDriver(capabilities);
            capabilities = DesiredCapabilities.htmlUnit();
            capabilities.setBrowserName("htmlunit");
            capabilities.setVersion("49.0.1");
            capabilities.setPlatform(Platform.ANY);
            capabilities.setJavascriptEnabled(true);

            if (_browserType.trim().isEmpty() || _browserType.equalsIgnoreCase("LOCAL"))
            {
                return new HtmlUnitDriver(capabilities);
            } else if (_browserType.equalsIgnoreCase("REMOTE") && !_remoteHub.isEmpty())
            {
                try {
                    return new RemoteWebDriver(new URL(_remoteHub), capabilities);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }
            else
            {
                return new HtmlUnitDriver(capabilities);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.Error("HTML Unit Driver Error", ex);
        }
        return null;
    }

    private Object getDefaultDriver() {
        try
        {
            DesiredCapabilities capabilities = DesiredCapabilities.htmlUnitWithJs();
            if (_browserType.trim().isEmpty() || _browserType.equalsIgnoreCase("LOCAL"))
            {
                return new HtmlUnitDriver(capabilities);
            } else if (_browserType.equalsIgnoreCase("REMOTE") && !_remoteHub.isEmpty())
            {
                try {
                    return new RemoteWebDriver(new URL(_remoteHub), capabilities);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return null;
            }
            else
            {
                return new HtmlUnitDriver(capabilities);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.Error("Default Driver (HTMLUNIT) Error", ex);
        }
        return null;
    }
    @Override
    public Object getDriver()
    {
        if (_browserName.toUpperCase().equals("FIREFOX")) {
            return getFireFoxDriver();
        } else if (_browserName.toUpperCase().equals("CHROME")) {
            return getChromeDriver();
        } else if (_browserName.toUpperCase().equals("SAFARI")) {
            return getSafariDriver();
        } else if (_browserName.toUpperCase().equals("PHANTOMJS")) {
            return getPhantomJSDriver();
        } else if (_browserName.toUpperCase().equals("HTMLUNITDRIVER")) {
            return getHTMLUnitDriver();
        } else if (_browserName.toUpperCase().equals("IE")) {
            return getInternetExplorerDriver();
        } else if (_browserName.toUpperCase().equals("IE64")) {
            return getInternetExplorer64Driver();
        } else {
            return getDefaultDriver();
        }
    }
}
