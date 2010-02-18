package com.manheim.vim.services.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateHelper.java - A helper class to retrieve current date in predefined formats
 * @author Enterpulse
 * @version 1.0 
 */
public class DateHelper
{
	
    /**
     * @return String Current date in the format -- EEEddMMMyyyy_HH_mm_ss
     */
    public static String getDate()
    {
        Date todaysDate = new java.util.Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEddMMMyyyy_HH_mm_ss");
        String formattedDate = formatter.format(todaysDate);
        return formattedDate;
    }

    /**
     * @return String Current date in the format -- EEE_MMM_dd_HH_mm_ss_zzz_yyyy
     */
    public static String getCompleteDate()
    {
        Date todaysDate = new java.util.Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE_MMM_dd_HH_mm_ss_zzz_yyyy");
        String formattedDate = formatter.format(todaysDate);
        return formattedDate;
    }
}
