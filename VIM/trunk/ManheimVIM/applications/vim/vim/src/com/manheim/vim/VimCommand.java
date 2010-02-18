package com.manheim.vim;

/**
 * @author Enterpulse
 * @version 1.0 
 * @see VimMain
 */
public abstract class VimCommand
{
	/** main thread sleeps for this duration before checking if all threads have 
	 * completed execution. If one or more threads are active, main thread sleeps 
	 * and checks the status again*/
    public static final long THREAD_SLEEP_TIME = 1000L;

    /** ImageExporter and ImageLoader classes implement this abstract method */
    public abstract void execute();
}
