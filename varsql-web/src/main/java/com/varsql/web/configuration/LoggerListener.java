package com.varsql.web.configuration;

import java.io.File;

import com.varsql.core.configuration.ConfigurationFilePath;
import com.varsql.core.configuration.Constants;
import com.vartech.common.utils.StringUtils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;

/**
 * -----------------------------------------------------------------------------
* @fileName		: LoggerListener.java
* @desc		: varsql logback listener
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 6. 6. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class LoggerListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {

    private boolean started = false;

    @Override
    public void start() {
        if (started) return;

        String varsqlRuntime = System.getProperty(Constants.RUNTIME_KEY);

        Context context = getContext();

        if("local".equals(varsqlRuntime)) {
        	context.putProperty("runtime", "local");
        	context.putProperty("LOG_DIR", ConfigurationFilePath.getInstance().getInstallRoot()+File.separator+"logs");
        }else {
	        String logBase = System.getProperty("catalina.base"); 
        	if(!StringUtils.isBlank(logBase)) {
	        	context.putProperty("runtime", "prod");
	        	context.putProperty("LOG_DIR", System.getProperty("catalina.base")+"/logs/varsql");
        	}else {
        		context.putProperty("runtime", "local");
            	context.putProperty("LOG_DIR", ConfigurationFilePath.getInstance().getInstallRoot()+File.separator+"logs");
        	}
        }

        started = true;
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public boolean isResetResistant() {
        return true;
    }

    @Override
    public void onStart(LoggerContext context) {
    }

    @Override
    public void onReset(LoggerContext context) {
    }

    @Override
    public void onStop(LoggerContext context) {
    }

    @Override
    public void onLevelChange(Logger logger, Level level) {
    }
}