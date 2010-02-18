package com.manheim.vim.services.util;

import java.util.Map;

/**
 * QueryHelper.java - A helper class that helps construct UCM query string.
 * @author Enterpulse
 * @version 1.0 
 */
public class QueryHelper
{

    /**
     * This method receives a map of metadata attributes and constructs a query from it.
     * @param metadata A map of metadata attribute name value
     * @return String The query string constructed from the map 
     */
    static public String getUCMQuery(Map<String, String> metadata)
    {

        StringBuilder query = new StringBuilder("");
        for (Map.Entry<String, String> entry : metadata.entrySet())
        {
            if (query.length() > 0)
            {
                query = query.append(" <OR> ");
            }
            
            query = query.append("(" + entry.getKey() + " <matches> " + "`" + entry.getValue() + "`)");
        }

        return query.toString();
    }

}
