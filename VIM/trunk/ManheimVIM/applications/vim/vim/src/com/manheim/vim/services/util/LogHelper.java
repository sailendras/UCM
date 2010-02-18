package com.manheim.vim.services.util;

import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.slf4j.LoggerFactory;

/**
 * LogHelper.java - A helper class that helps create and return Logger instance
 * @author Enterpulse
 * @version 1.0 
 */
public class LogHelper
{
	/** Logger to log exceptions in LogHelper class*/
    private static final org.slf4j.Logger exceptionLog = LoggerFactory.getLogger(LogHelper.class);
    
    /**
     * This method creates a Logger, with its appender pointing to a dated log file.
     * @param basePath Path where the log file has to be created
     * @return Logger An instance of slf4j Logger class wrapped around log4j Logger instance
     */
    public static org.slf4j.Logger getLogger(String basePath)
    {
        try
        {   
        	// This logger is used to log success / failure of import and export.
            org.apache.log4j.Logger log4jLog = Logger.getLogger("vimLogger");
            log4jLog.setLevel(Level.DEBUG);
            org.apache.log4j.FileAppender appender;
            appender = new FileAppender(new PatternLayout("%-5p || %d{dd MMM yyyy HH:mm:ss,SSS} || Class: %C || %m%n"), basePath
                    + DateHelper.getCompleteDate() + ".log");
            log4jLog.addAppender(appender);
            appender.activateOptions();
            
            // Since a logger with this name is already created above, the same 
            // instance is returned--thereby isolating the implementation dependency
            org.slf4j.Logger slf4jLog = LoggerFactory.getLogger("vimLogger");
            
            return slf4jLog;
        }
        catch (IOException e)
        {
            exceptionLog.error("IO Exception occured while creating a Logger for " + basePath + System.getProperty("line.separator")
                    + ExceptionHelper.getStackTraceAsString(e));
        }
        
        return null;
    }
}
