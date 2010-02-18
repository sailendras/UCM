package com.manheim.vim.imageexporter;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manheim.vim.schemas.config.export.ExportConfigDocument.ExportConfig;
import com.manheim.vim.schemas.config.vim.Group;
import com.manheim.vim.services.exception.ServicesException;
import com.manheim.vim.services.util.DateHelper;
import com.manheim.vim.services.util.QueryHelper;
import com.manheim.vim.services.util.SpringHelper;
import com.manheim.vim.services.vehicleimage.VehicleImageFacade;
import com.manheim.vim.services.vehicleimage.model.VehicleImage;

/**
 * QueryWorker.java - A class representing a thread and responsible for querying UCM server for images. 
 * For each image in the result set, it constructs VehicleImage objects and populates a
 * synchronized list of VehicleImages. This list is then used by the ImageExporter 
 * class to continue its export process.
 * @author Enterpulse
 * @version 1.0 
 */
public class QueryWorker implements Runnable
{
	/** Logger to log exceptions in QueryWorker class*/
    private static final Logger exceptionLog = LoggerFactory.getLogger(QueryWorker.class);

    /** A map containing metadata attribute name (key) and its value */
    private Map<String, String> metadata; 
    /** A collection of VehicleImage objects */
    private List<VehicleImage> vehicleImages;
    /** Contains one or more servers (Each with credentials like server url, port, description, name, etc) */
    private Group serverGroup;
    /** A Logger instance passed to QueryWorker from ImageExporter. This logger is used to log
     *  either to a specified file or console depending on the provider configuration */
    private Logger log;
    /** Contains provider specific configuration setting for export sequence */
    private ExportConfig exportConfig;
    /** It is the maximum number of attempts each instance of QueryWorker should retry its task in case of an exception */
    private int maxAttempts;

    public QueryWorker(Map<String, String> metadata, List<VehicleImage> vehicleImages, ExportConfig exportConfig,
            Group serverGroup, Logger log, int maxAttempts)
    {
        this.metadata = metadata;
        this.exportConfig = exportConfig;
        this.serverGroup = serverGroup;
        this.log = log;
        this.vehicleImages = vehicleImages;
        this.maxAttempts = maxAttempts;
    }

    /**
     * This is the implementation of Runnable interface's run() method. It queries
     * UCM server via a UCM-Aware DAO to query and retrieve all images' details.
     */
    public void run()
    {
        try
        {
            VehicleImageFacade vehicleImageFacade = SpringHelper.getVehicleImageFacade();
            
            List<VehicleImage> images = vehicleImageFacade.getImagesByMetadata(
                    metadata,
                    exportConfig.getManifestMetadataFieldList(),
                    exportConfig.getProviderName(),
                    serverGroup,
                    maxAttempts);
            
            vehicleImages.addAll(images);
        }
        catch (ServicesException e)
        {
            log.error("EXPORT -- Provider Name :" + exportConfig.getProviderName() + "\t"
            		+ QueryHelper.getUCMQuery(metadata) + "\t"
                    + " has not been executed");

            exceptionLog.error("EXPORT -- Provider Name :" + exportConfig.getProviderName() + "\t"
            		+ QueryHelper.getUCMQuery(metadata) + "\t"
                    + " has not been executed");
        }
    }
}
