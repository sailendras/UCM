package com.manheim.vim.services.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manheim.vim.schemas.config.vim.Group;
import com.manheim.vim.schemas.config.vim.VimConfigDocument.VimConfig;
import com.manheim.vim.services.exception.ConfigurationException;

/**
 * ServerGroupHelper.java - A helper class that returns a server group by a given group Id.
 * @author Enterpulse
 * @version 1.0 
 */
public class ServerGroupHelper
{
	/** Logger to log exceptions in ServerGroupHelper class*/
    private static final Logger exceptionLog = LoggerFactory.getLogger(ServerGroupHelper.class);

    /**
     * This method finds a group object in the common configuration object by a given Id
     * @param groupId The unique ID that identifies a group of configured servers
     * @param providerName The content provider name
     * @param vimConfig The common configuration object
     * @return Group Contains one or more server details
     */
    public static Group getServerGroup(String groupId, String providerName, VimConfig vimConfig)
    {
        if (StringUtils.isNotEmpty(groupId))
        {
            if (vimConfig.getGroupsList() != null)
            {
                // Iterate over all the groups provided in vim-config
                for(Group group : vimConfig.getGroupsList())
                {
                	// Set the group instance if a match is found
                    if (groupId.equals(group.getGroupId()))
                    {
                        return group;
                    }
                }
            }
        }

        exceptionLog.error("Provider Name : " + providerName 
                + " Server Group ID is null. Cannot set serverGroup");

        throw new ConfigurationException("Server Group ID is null. Cannot set server group");
    }
}
