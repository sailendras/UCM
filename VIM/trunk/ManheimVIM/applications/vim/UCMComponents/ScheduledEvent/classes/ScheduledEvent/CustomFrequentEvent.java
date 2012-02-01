/******************************************************************************/
/*                                                                            */
/*  Stellent, Incorporated Confidential and Proprietary                       */
/*                                                                            */
/*  This computer program contains valuable, confidential and proprietary     */
/*  information.  Disclosure, use, or reproduction without the written        */
/*  authorization of Stellent is prohibited.  This unpublished                */
/*  work by Stellent is protected by the laws of the United States            */
/*  and other countries.  If publication of the computer program should occur,*/
/*  the following notice shall apply:                                         */
/*                                                                            */
/*  Copyright (c) 1997-2001 IntraNet Solutions, Incorporated.  All rights	  */
/*	reserved.																  */
/*  Copyright (c) 2001-2007 Stellent, Incorporated.  All rights reserved.     */
/*                                                                            */
/******************************************************************************/

package ScheduledEvent;

import intradoc.server.*;
import intradoc.shared.*;
import intradoc.common.*;
import intradoc.data.*;

/**
 * Another way to execute arbitraty Java Code is through filters.  There
 * are many hooks in the server that check to see if the user wishes to execute
 * additional Java code before performing the standard functions.  
 * 
 * Common spots for filters include validation of data before checkin,
 * executing special code upon server startup, and execution of special
 * code at the beginning of a workflow.
 * 
 * This class is for code that needs to be executed on a scheduled basis, but
 * more frequently than a system event. It therefore hooks into the 
 * 'checkScheduledEvents' hook, and will be executed about every five minutes
 */
public class CustomFrequentEvent implements FilterImplementor
{
	/**
	 * Just a quick scheuled event
	 */
	public int doFilter(Workspace ws, DataBinder eventData, ExecutionContext cxt)
		throws DataException, ServiceException
	{
		// 
		trace("executing action...");
		
		// filter executed correctly.  Return FINISHED.
		return FINISHED;
	}
	
	
	/**
	 * Log a trace message to the 'scheduledevents' section
	 */
	protected void trace(String str)
	{
		SystemUtils.trace("scheduledevents", "- custom:frequent - " + str);
	}	
}
