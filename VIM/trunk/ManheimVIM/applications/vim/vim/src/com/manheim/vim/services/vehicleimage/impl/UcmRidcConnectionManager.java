package com.manheim.vim.services.vehicleimage.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcClientManager;
import oracle.stellent.ridc.IdcContext;
import com.manheim.vim.schemas.config.vim.Group;
import com.manheim.vim.schemas.config.vim.ServerInstance;
import com.manheim.vim.services.exception.ServicesException;
import com.manheim.vim.services.util.ExceptionHelper;

/**
 * UcmRidcConnectionManager.java - A singleton class responsible for maintaining a list 
 * of server details and returning a new connection handle to the next server in sequence. 
 * This makes sure that all server instances in a server group are load balanced.
 * @author Enterpulse
 * @version 1.0 
 */
public class UcmRidcConnectionManager
{
	/** An instance of this class, set to null -- used to return a singleton */
    private static UcmRidcConnectionManager instance = null;

    /** Holds a complete url of a server instance -- used to create and return new connection handles */
    private List<String> serverUrlList;
    
    /** This is used to return the next server url in sequence */
    private int serverUrlIndex = 0;

    /** holds IdcContext, used to make a service call */
    private IdcContext userContext = null;
    
    /** Used to create a new IdcClient instance */
    private IdcClientManager clientManager = null;

    /**  Logger to log exceptions in UcmRidcConnectionManager class */
    private static final Logger exceptionLog = LoggerFactory.getLogger(UcmRidcConnectionManager.class);

    private UcmRidcConnectionManager(String providerName, Group serverGroup)
    {
        try
        {
            setClientManager();
            setUserContext(providerName);
            setServerUrls(serverGroup);
        }
        catch (Exception e)
        {
            exceptionLog.error("Provider Name :"+ providerName + ExceptionHelper.getStackTraceAsString(e));
            throw new ServicesException(e);
        }
    }

    
    /**
     * This method is used to get handle to the connection manager instance.
     * @param providerName The content provider name
     * @param serverGroup Group of UCM server details and their details
     * @return UcmRidcConnectionManager A new instance of UcmRidcConnectionManager if one does not exist; an existing instance if otherwise.
     */
    public static synchronized UcmRidcConnectionManager getInstance(String providerName, Group serverGroup)
    {
        if (instance == null)
        {
            instance = new UcmRidcConnectionManager(providerName, serverGroup);
        }

        return instance;
    }

    
    /**
     * This method decides which server connection handle to return to the caller.
     * @return IdcClient IdcClient object which is the connection handle to UCM server
     */
    @SuppressWarnings("unchecked")
    public synchronized IdcClient getNextClient()
    {
        try
        {
            serverUrlIndex = serverUrlIndex++ % serverUrlList.size();
            return clientManager.createClient(serverUrlList.get(serverUrlIndex));
        }
        catch (IdcClientException e)
        {
            exceptionLog.error(ExceptionHelper.getStackTraceAsString(e));
        }
        return null;
    }

    public IdcContext getUserContext()
    {
        return userContext;
    }

    
    /**
     * This method creates a new RIDC IdcClientManager object
     */
    private void setClientManager()
    {
        clientManager = new IdcClientManager();
    }

    
    /**
     * This method creates a new RIDC IdcContext object using the content provider's name 
     * @param providerName The content provider name
     */
    private void setUserContext(String providerName)
    {
        userContext = new IdcContext(providerName);
    }

    
    /**
     * This method Iterates over the server group object. For each server it reads, it constructs a server Url using the url and 
     * port number values. It adds the server Url to its private collection of server Urls. The server url list is used to
     * return a new connection handle to that particular server
     * @param serverGroup Group of UCM server details and their details
     * @throws IdcClientException
     */
    private void setServerUrls(Group serverGroup) throws IdcClientException
    {
        serverUrlList = new ArrayList<String>();
        for(ServerInstance serverInstance : serverGroup.getServerInstancesList())
        {
            String serverAddress = serverInstance.getServerUrl();
            String serverPort = serverInstance.getServerPort();
            String serverUrl = "idc://" + serverAddress + ":" + serverPort;
            serverUrlList.add(serverUrl);
        }
    }
}
