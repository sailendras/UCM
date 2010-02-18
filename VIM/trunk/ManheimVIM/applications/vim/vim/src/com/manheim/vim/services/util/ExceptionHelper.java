package com.manheim.vim.services.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * ExceptionHelper.java - A helper class to handle exception objects stack trace
 * @author Enterpulse
 * @version 1.0 
 */
public class ExceptionHelper
{
    /**
     * This method takes in the exception object and returns its Stacktrace as HTML String equivalent
     * @param throwable The Throwable exception object
     * @return String HTML String equivalent of the exception object
     */
    static public String getStackTraceAsHTMLString(Throwable throwable)
    {
        String errorMessage = null;
        try
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            errorMessage = sw.toString();
            errorMessage = errorMessage.replaceAll(System.getProperty("line.separator"), "<br /> &nbsp;&nbsp;&nbsp;");
            return "<br />" + errorMessage + "<br />";
        }
        catch (Exception ex)
        {
            return "Unknown Error!";
        }
    }

    /**
     * This method takes in the exception object and returns its Stacktrace as String equivalent
     * @param throwable The Throwable exception object
     * @return String String equivalent of the exception object
     */
    static public String getStackTraceAsString(Throwable throwable)
    {
        String errorMessage = null;
        try
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            errorMessage = sw.toString();
            return errorMessage;
        }
        catch (Exception ex)
        {
            return "Unknown Error!";
        }
    }
}