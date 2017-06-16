package com.hbcd.logging.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

/**
 * Created by ephung on 8/2/16.
 */
public class LogConfiguration {

    private static LoggerContext _context = null;
    static {
        initialized();
    }

    private static LoggerContext reconfigContext()
    {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setStatusLevel(Level.WARN);
        builder.setConfigurationName("MyApp");

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
        AppenderComponentBuilder appenderBuilder =  builder.newAppender("rollingName1", "RollingFile")
                .addAttribute("fileName", "C:/logs/AutomationFramework.log")
                .addAttribute("filePattern", "C:/logs/AutomationFramework-%i.log")
                .add(layoutBuilder)
                .addComponent(triggeringPolicy)
                .addComponent(rolloverStrategy);

        //Build Rolling Component for logfile 2
        AppenderComponentBuilder appenderBuilder2 =  builder.newAppender("rollingName2", "RollingFile")
                .addAttribute("fileName", "C:/logs/SuiteResult.log")
                .addAttribute("filePattern", "C:/logs/SuiteResult-%i.log")
                .add(layoutBuilder)
                .addComponent(triggeringPolicy)
                .addComponent(rolloverStrategy);

        builder.add(appenderBuilder).add(appenderBuilder2);

        //Create logger name "FrameworkLogger"
        builder.add( builder.newLogger( "FrameworkLogger", Level.ERROR.WARN.INFO )
                .add( builder.newAppenderRef( "rollingName1" ) )
                .addAttribute( "additivity", false ) );

        //Create logger name "SuiteResultLogger"
        builder.add(builder.newLogger("SuiteResultLogger", Level.ERROR.WARN.INFO)
                .add (builder.newAppenderRef("rollingName2"))
                .addAttribute("additivity", false));

//        builder.add( builder.newRootLogger( Level.ERROR.WARN.INFO )
//                .add( builder.newAppenderRef( "rollingName1" ) ) );

//        LoggerContext ctx = Configurator.initialize( builder.build() );
//        Logger logger = ctx.getLogger( "FrameworkLogger" );
//        return logger;

        //Initialize.
        return Configurator.initialize(builder.build());
    }
    private static void initialized() {
        try {
            _context = reconfigContext();
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
    }

    public static Logger getLogger(String name)
    {
        return _context.getLogger(name);
    }
}
