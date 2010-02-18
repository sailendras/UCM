package com.manheim.vim.imageloader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manheim.vim.VimCommand;
import com.manheim.vim.schemas.config.manifest.MetadataEntry;
import com.manheim.vim.schemas.config.manifest.ManifestDocument.Manifest;
import com.manheim.vim.schemas.config.metadata.Metadata;
import com.manheim.vim.schemas.config.vim.Group;
import com.manheim.vim.schemas.config.vim.VimConfigDocument.VimConfig;
import com.manheim.vim.schemas.config.ximport.ImportConfigDocument.ImportConfig;
import com.manheim.vim.services.exception.ConfigurationException;
import com.manheim.vim.services.exception.ThreadExceptionHandler;
import com.manheim.vim.services.util.DateHelper;
import com.manheim.vim.services.util.ExceptionHelper;
import com.manheim.vim.services.util.LogHelper;
import com.manheim.vim.services.util.ServerGroupHelper;
import com.manheim.vim.services.util.SpringHelper;

/**
 * ImageLoader.java - A class responsible for initiating batch Import sequence. 
 * @author Enterpulse
 * @version 1.0 
 * @see VimCommand
 */
public class ImageLoader extends VimCommand
{
	/** Logger instance for ImageLoader class. Used to log exceptions */
    private static final Logger exceptionLog = LoggerFactory.getLogger(ImageLoader.class);

    /** Contains application level configuration settings */
    private VimConfig vimConfig;
    /** Contains provider specific import configuration settings */
    private ImportConfig importConfig;
    /** Contains a manifest of all images that need to be checked into the UCM */
    private Manifest manifest;
    /** A logger used to log success or failure into a separate log file or console based on configuration */
    private Logger log = null;
    /** Contains one or more servers (each having credentials like server url, port, description, name, etc) */
    private Group serverGroup;
    /** Contains all default UCM content metadata attributes and their values. Each thread uses this map during importing content */
    private Map<String, String> defaultAttributes;

    
    /** 
     * This method extracts required information from configuration objects,
     * divides the process into smaller tasks, spawn threads and assign one task per thread. 
     */
    public void execute()
    {
        try
        {
        	serverGroup = ServerGroupHelper.getServerGroup(importConfig.getGroupId(), importConfig.getProviderName(), vimConfig);
        	
        	//Query UCM for user's security group by their username. 
        	//Note: A user must have a provider role on the UCM server to retrieve a security group ID
        	String securityGroupId = SpringHelper.getVehicleImageFacade().getSecurityGroupId(importConfig.getProviderName(),
        															serverGroup, 
        															vimConfig.getMaxAttempts());
        	if(StringUtils.isEmpty(securityGroupId))
        	{
                exceptionLog.error("IMPORT -- Provider Name: " + importConfig.getProviderName() + "\t"
                        + "Security group Id cannot be retrieved. Exiting Import sequence !");
                System.exit(-1);
        	}
        	
            defaultAttributes = new HashMap<String, String>();
            loadDefaultAttributes(); // Loads common application level defaults from vim-config.xml
            
            // If a log required attribute is set, create one at the base path
            if(importConfig.getLogRequired())
            {
                log = LogHelper.getLogger(importConfig.getBasePath() + File.separator);
            }
            // Otherwise, print to console
            else
            {
                log = LoggerFactory.getLogger("consoleLogger");
            }

            // Delete previous batch import termination flag if it exists
            File batchTerminationFile = new File(importConfig.getBasePath() + File.separator + "BatchImportComplete.log");
            if (batchTerminationFile.exists())
            {
                System.out.println("Batch import termination flag exists. Deleting it !");
                batchTerminationFile.delete();
            }

            Thread.setDefaultUncaughtExceptionHandler(new ThreadExceptionHandler());

            ExecutorService executor = Executors.newFixedThreadPool(vimConfig.getNumberOfThreads());

            // Assign work to each thread
            for (MetadataEntry metadataEntry : manifest.getMetadataEntriesList())
            {
                Runnable worker = new ImageLoadWorker(
                        log,
                        metadataEntry,
                        serverGroup,
                        importConfig,
                        vimConfig,
                        defaultAttributes,
                        securityGroupId);

                executor.execute(worker);
            }

            // Do not accept any new requests
            executor.shutdown();

            // Wait till all threads managed by the Executor have finished
            while (!executor.isTerminated())
            {
                try
                {
                    Thread.sleep(THREAD_SLEEP_TIME);
                }
                catch (InterruptedException e)
                {
                    exceptionLog.error("IMPORT -- Provider Name: " + importConfig.getProviderName() + "\t" +
                                "An Interrupted Exception has occured in execute() method of ImageLoader class" + "\t" +
                                ExceptionHelper.getStackTraceAsString(e));
                }
            }

            // Create a termination flag to indicate completion of batch import
            new File(importConfig.getBasePath() + File.separator + "BatchImportComplete.log").createNewFile();
        }
        catch (IOException e)
        {
            exceptionLog.error("IMPORT -- Provider Name: " + importConfig.getProviderName() +
                        " An IO Exception has occured in execute() method of ImageLoader class: " +
                        ExceptionHelper.getStackTraceAsString(e));
        }
        catch (ConfigurationException e)
        {
            exceptionLog.error("IMPORT -- Provider Name: " + importConfig.getProviderName() +
                        " A Configuration Exception has occured in execute() method of ImageLoader class: "
                        + ExceptionHelper.getStackTraceAsString(e));
        }
    }

    public ImageLoader(VimConfig vimConfig, Manifest manifest, ImportConfig importConfig)
    {
        this.vimConfig = vimConfig;
        this.importConfig = importConfig;
        this.manifest = manifest;
    }

    
    /**
     * This method iterates over all default metadata attributes specified in both 
     * common and provider specific configuration objects and sets them into the defaultAttributes Map
     */
    private void loadDefaultAttributes()
    {
        if(vimConfig.getDefaultAttributesList() != null)
        {
            int listSize = vimConfig.getDefaultAttributesList().size();
            if(listSize > 0)
            {
				for (Metadata defaultMetadata: vimConfig.getDefaultAttributesList())
				{
					defaultAttributes.put(defaultMetadata.getName(), defaultMetadata.getValue());
				}
            }
        }
        
        if(importConfig.getDefaultAttributesList() != null)
        {
            int listSize = importConfig.getDefaultAttributesList().size();
            if(listSize > 0)
            {
				for (Metadata defaultMetadata: importConfig.getDefaultAttributesList())
				{
					defaultAttributes.put(defaultMetadata.getName(), defaultMetadata.getValue());
				}
            }
        }
    }

}
