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
 * This code needs to be executed on a schedule which may vary, but it usually
 * less than once per hour. Therefore, it hooks into the 'scheduledSystemEvent'
 * filter, which requires additional configuration. See the 'CustomScheduledEvents'
 * table for more information
 */
public class CustomSystemEvent implements FilterImplementor
{
	/**
	 * Just a quick scheuled event
	 */
	public int doFilter(Workspace ws, DataBinder eventData, ExecutionContext cxt)
		throws DataException, ServiceException
	{
		// get the action, and be sure to only execute your code if the 'action'
		// matches the value for action in the 'CustomScheduledEvents' table 
		String action  = eventData.getLocal("action");
		
		// execute the daily event, or the hourly event
		if (action.equals("CustomDailyEvent"))
		{
			doCustomDailyEvent(ws, eventData, cxt);
			return FINISHED;
		}
		else if (action.equals("CustomHourlyEvent"))
		{
			doCustomHourlyEvent(ws, eventData, cxt); 
			return FINISHED;
		} 
		
		// Return CONTINUE so other filters have a chance at it.
		return CONTINUE;
	}
	
	
	/**
	 * Execute the custom daily event
	 * @return an error string, or null if no error
	 */
	protected void doCustomDailyEvent(Workspace ws, DataBinder eventData, 
		ExecutionContext cxt) throws DataException, ServiceException
	{
		// you MUST perform at least one update
		update("CustomDailyEvent", "event starting...", ws);
		
		trace("doing custom daily event... should be run around midnight");
		
		// event has finished!
		update("CustomDailyEvent", "event finished successfully", ws);
	}


	/**
	 * Execute the custom hourly event
	 * @return an error string, or null if no error
	 */
	protected void doCustomHourlyEvent(Workspace ws, DataBinder eventData, 
	ExecutionContext cxt) throws DataException, ServiceException
	{
		// you MUST perform at least one update
		update("CustomHourlyEvent", "event starting...", ws);
				
		trace("doing custom hourly event");

		// event has finished!
		update("CustomHourlyEvent", "event finished successfully", ws);		
	}
	
	/**
	 * Update the state of the event. Must be done at least once to tell the content server
	 * when the scehduled event is finished.
	 */
	protected void update(String action, String msg, Workspace workspace) throws ServiceException, DataException
	{
		long curTime = System.currentTimeMillis();
		ScheduledSystemEvents sse = IdcSystemLoader.getOrCreateScheduledSystemEvents(workspace);
		sse.updateEventState(action, msg, curTime);
	}
	
	/**
	 * Log a trace message to the 'scheduledevents' section
	 */
	protected void trace(String str)
	{
		SystemUtils.trace("scheduledevents", "- custom - " + str);
	}	
}
