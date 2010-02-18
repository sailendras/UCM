package com.manheim.vim.services.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manheim.vim.services.util.ExceptionHelper;

public class ThreadExceptionHandler implements UncaughtExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(ThreadExceptionHandler.class); 
        
    public void uncaughtException(Thread thread, Throwable throwable)
    {
        String message = "Thread " + thread.getName() + " failed";
        
        // TODO:  add in the exception helper and print out the exception stack trace to system.err
        //   this needs to be done everywhere an exception is being caught        
               
        System.err.println(message);
        System.err.println(ExceptionHelper.getStackTraceAsString(throwable));
        
        log.error(message, throwable);
        log.error(ExceptionHelper.getStackTraceAsString(throwable)); 
        
    }
}
