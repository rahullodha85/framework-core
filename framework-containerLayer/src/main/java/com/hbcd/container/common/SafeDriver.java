package com.hbcd.container.common;

import com.hbcd.container.web.DriverObject;
import com.hbcd.containerinterface.DriverSearch;

public class SafeDriver {
	private static ThreadLocal<DriverSearch> _mydriver = 
			null;
//			new ThreadLocal<DriverSearch>()
//			{
//				public DriverSearch initialValue()
//				{
//					try {
//						return new DriverObject();
//					} catch (ReflectiveOperationException e) {
//						e.printStackTrace();
//					}
//					return null;
//				}
//			}
//	;


	public synchronized  static DriverSearch Current()
	{
		//System.out.println("Current Thread ID: " + Thread.currentThread().getId() + ((_mydriver == null)? " DRIVER IS NULL" : " WRAPPER@ :" + _mydriver.toString() + " DRIVER@ :" + _mydriver.get().fw_printDriver()));
		return _mydriver.get();
	}
	
	public synchronized static void init(String browserType
											, String browserName
											, String browserVersion
											, String resolution
											, String remoteHub
											, String browserPlatform
											, String proxy
											, String defaultDirectory) throws Exception
	{
		if (_mydriver == null)
		{
			_mydriver = new ThreadLocal<DriverSearch>()
			{
				public DriverSearch initialValue()
				{
					try {
						return new DriverObject().setBrowserType(browserType)
													.setBrowserName(browserName)
													.setBrowserVersion(browserVersion)
													.setResolution(resolution)
													.setRemoteHub(remoteHub)
													.setPlatform(browserPlatform)
													.setProxyServer(proxy)
													.setDefaultDirectory(defaultDirectory);
					} catch (ReflectiveOperationException e) {
						e.printStackTrace();
					}
					return null;
				}
			};
		}

		((DriverObject)_mydriver.get()).init();
//		_mydriver.get().fw_init(browserName, browserType, browserVersion, browserPlatform, defaultDirectory);
	}
	
	public synchronized static void teardown()
	{
		System.out.println(String.format("Current Thread ID# : %s CLOSED THE BROWSER!!!", Thread.currentThread().getId()));
		Current().fw_quit();
	}
}
