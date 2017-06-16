package com.hbcd.ContainerUtility;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

/**
 * Created by ephung on 9/16/16.
 */
public class LogConfiguration {

//    public LogConfiguration() {
//        initialized();
//    }

    private LoggerContext reconfigContext()
    {
//        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilder<Log>.newConfigurationBuilder(Log.class);
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setStatusLevel(Level.WARN);
        builder.setConfigurationName("MyContainer");
        //builder.setConfigurationSource()

        //Building Layout
        LayoutComponentBuilder layoutBuilder = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d %p %c{1.} [%t] %m%n");

        //Build Policies Component
        ComponentBuilder triggeringPolicy = builder.newComponent("Policies")
//                .addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"))
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", "10MB"));

        //Building Roller Strategy Component
        ComponentBuilder rolloverStrategy = builder.newComponent("DefaultRolloverStrategy")
                .addAttribute("max", "15");

        //Build Rolling Component for logfile 1
        AppenderComponentBuilder appenderBuilder =  builder.newAppender("rollingName9", "RollingFile")
                .addAttribute("fileName", "C:/logs/ContainerAutomationFramework.log")
                .addAttribute("filePattern", "C:/logs/ContainerAutomationFramework-%i.log")
                .add(layoutBuilder)
                .addComponent(triggeringPolicy)
                .addComponent(rolloverStrategy);

        AppenderComponentBuilder appenderBuilder2 = builder.newAppender("Stdout", "CONSOLE").addAttribute("target",
                ConsoleAppender.Target.SYSTEM_OUT);

        builder.add(appenderBuilder).add(appenderBuilder2);

        builder.add(builder.newRootLogger(Level.ERROR).add(builder.newAppenderRef("Stdout")));

        //Create logger name "FrameworkLogger"
        builder.add(builder.newLogger( "ContainerLogger", Level.ERROR.WARN.INFO )
                .add( builder.newAppenderRef( "rollingName9" ) )
                .addAttribute( "additivity", false ) );

        //Initialize.
        return Configurator.initialize(builder.build());

    }
//    private static void initialized() {
//        try {
//            _context = reconfigContext();
//        }
//        catch (Exception e){
//            System.out.println("UNABLE to locate file: log4j2.xml");
//            e.printStackTrace();
//        }
//        finally {
//            if (_context == null) {
//                _context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false); //Default
//            }
////            logger = context.getRootLogger();
////            logger = context.getLogger("FrameworkLogger");
////            reportLogger = context.getLogger("SuiteResultLogger");
//        }
//    }

    public Logger getLogger(String name)
    {
        LoggerContext _context = null;

        if (LogManager.getContext(true) != null)
        {
            _context = (LoggerContext)LogManager.getContext(false);
        }
        try {
            _context = reconfigContext();
//            _context.updateLoggers();
//            (_context.getConfiguration()).getc;

        }
        catch (Exception e){
            System.out.println("UNABLE to locate file: log4j2.xml");
            e.printStackTrace();
        }
        finally {
            if (_context == null) {
                _context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false); //Default
            }
//            logger = context.getRootLogger();
//            logger = context.getLogger("FrameworkLogger");
//            reportLogger = context.getLogger("SuiteResultLogger");
        }
        if (_context != null) {
            return _context.getLogger(name);
        }
        return null;
    }
}
