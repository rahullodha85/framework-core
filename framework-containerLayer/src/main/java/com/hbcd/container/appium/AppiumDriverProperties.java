package com.hbcd.container.appium;

import com.hbcd.container.common.DriverPropertiesBase;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ephung on 2/9/2016.
 */
public class AppiumDriverProperties extends DriverPropertiesBase {

    @Override
    public Object getDriver() {

        WebDriver  driver = null;

        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (_browserName.equalsIgnoreCase("IOS")) {
            File appDir = new File(System.getProperty("user.dir"), "/app/");
            File app = new File(appDir, "test.app");
            capabilities.setCapability(CapabilityType.BROWSER_NAME, "iOS");
            capabilities.setCapability(CapabilityType.PLATFORM, "Mac");
            capabilities.setCapability("platformName", "iOS");
            capabilities.setCapability("deviceName", "iPod");
            capabilities.setCapability("platformVersion", "7.1");
            capabilities.setCapability("device", "iphone");
            capabilities.setCapability("udid", "cd827d3778cfdee2fc7210f8f44184821a083c06");
            capabilities.setCapability("app", app);
            try {
                //IOSDriver
                driver = new RemoteWebDriver(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return driver;
        }
        else if (_browserName.equalsIgnoreCase("ANDROID"))
        {
            capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("deviceName","samsung-gt_i9100");
            capabilities.setCapability("platformVersion", "4.4.2");
            //capabilities.setCapability("app", app.getAbsolutePath());
            capabilities.setCapability("appPackage","com.whatsapp");
            capabilities.setCapability("appActivity","com.whatsapp.Main");
            try {
                driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return driver;
        }
        return null;
    }
}
