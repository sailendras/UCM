package com.manheim.vim.imageloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manheim.vim.schemas.config.manifest.MetadataEntry;
import com.manheim.vim.schemas.config.metadata.Metadata;
import com.manheim.vim.schemas.config.vim.Group;
import com.manheim.vim.schemas.config.vim.VimConfigDocument.VimConfig;
import com.manheim.vim.schemas.config.ximport.ImportConfigDocument.ImportConfig;
import com.manheim.vim.services.util.DateHelper;
import com.manheim.vim.services.util.ExceptionHelper;
import com.manheim.vim.services.util.SpringHelper;
import com.manheim.vim.services.vehicleimage.model.VehicleImage;
import com.manheim.vim.services.vehicleimage.model.VehicleImageSaveStatus;

/**
 * ImageLoadWorker.java - A class representing a thread. It is responsible for importing images into UCM server. 
 * @author Enterpulse
 * @version 1.0 
 * @see ImageLoader
 */
public class ImageLoadWorker implements Runnable
{
	/** Logger instance for ImageLoadWorker class. Used to log exceptions */
    private static final Logger exceptionLog = LoggerFactory.getLogger(ImageLoadWorker.class);

    /** A logger used to log success or failure into a seperate log file or console based on configuration. Passed in by ImageLoader class*/
    private Logger log;
    /** Contains one or more servers (each with credentials like server url, port, description, name, etc) */
    private Group serverGroup;
    /** Contains provider specific import configuration settings */
    private ImportConfig importConfig;
    /** Contains the image name and its metadata information that is being imported */
    private MetadataEntry metadataEntry;
    /** Contains application level configuration information */
    private VimConfig vimConfig;
    /** Contains all default UCM content metadata attributes. */
    private Map<String, String> defaultAttributes;
    /** It is the security group into which content is imported into */
    private String securityGroupId;

    public ImageLoadWorker(Logger log,
            MetadataEntry metadataEntry,
            Group serverGroup,
            ImportConfig importConfig,
            VimConfig vimConfig,
            Map<String, String> defaultAttributes,
            String securityGroupId)
    {
        this.log = log;
        this.metadataEntry = metadataEntry;
        this.serverGroup = serverGroup;
        this.importConfig = importConfig;
        this.vimConfig = vimConfig;
        this.defaultAttributes = defaultAttributes;
        this.securityGroupId = securityGroupId;
    }

    /**
     * This is the implementation of Runnable interface's run() method. It communicates with the
     * UCM server via UCM-aware DAO. It is responsible for Importing one image and log success or failure
     */
    public void run()
    {
        File image = null;

        try
        {
            image = new File(importConfig.getBasePath() + File.separator + metadataEntry.getImageName());

            List<Metadata> metadataList = new ArrayList<Metadata>();
            // Get the metadata entries for the current image and set them into an array list
            for (Metadata metadata: metadataEntry.getMetadataList())
            {
                metadataList.add(metadata);
            }

            VehicleImage vehicle = new VehicleImage(image, metadataList);
            VehicleImageSaveStatus saveStatus = SpringHelper.getVehicleImageFacade().saveImage(vehicle,
                    this.importConfig.getProviderName(), this.serverGroup, this.vimConfig.getMaxAttempts(), this.defaultAttributes, this.securityGroupId);

            // Make sure response object is not null. It will be null if
            // dataBinder was null (meaning server did not send a response back
            // and/or a server side exception has occurred
            if (saveStatus != null)
            {
                if ("0".equals(saveStatus.getStatusCode()))
                {
                    // Remove current image being handled if provider wants it deleted after checkin
                    if (this.importConfig.getDeleteAfterLoad())
                    {
                        image.delete();
                        System.out.println(image.getName() + " Has been deleted after checkin");
                    }

                    log.debug("SUCCESS -->  " + " Image Name:  " + image.getName() + "\t" + " StatusCode:  " + "\t"
                            + saveStatus.getStatusCode() + "\t" + " ContentID:  " + saveStatus.getContentId() + "\t" );
                }
                else
                {
                    log.debug("FAILURE -->  " + " Image Name:  " + image.getName() + "\t" + " StatusCode:  " + "\t"
                            + saveStatus.getStatusCode() + "\t" + " ContentID:  " + saveStatus.getContentId() + "\t"
                            + "Message :" + saveStatus.getMessage() + "\t");
                }
            }
            else
            {
                log.debug("FAILURE --> Image:  " + image.getName() + "\t" + " was not checked in. Server did not respond.");
            }
        }
        catch (Exception e)
        {
            log.error("EXCEPTION -->  " + " Image Name:  " + image.getName() + "\t"
                    + " was not checked in. " );
            exceptionLog.error("IMPORT -- Provider Name : " + importConfig.getProviderName() +
                      metadataEntry.getImageName() + " was not checked in due to an exception " +
                      ExceptionHelper.getStackTraceAsString(e));
        }
    }
}
