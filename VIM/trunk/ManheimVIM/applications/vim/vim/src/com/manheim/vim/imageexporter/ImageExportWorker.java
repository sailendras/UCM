package com.manheim.vim.imageexporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manheim.vim.schemas.config.export.ExportConfigDocument.ExportConfig;
import com.manheim.vim.schemas.config.manifest.ManifestDocument.Manifest;
import com.manheim.vim.schemas.config.vim.Group;
import com.manheim.vim.services.exception.ServicesException;
import com.manheim.vim.services.util.DateHelper;
import com.manheim.vim.services.util.ExceptionHelper;
import com.manheim.vim.services.util.SpringHelper;
import com.manheim.vim.services.vehicleimage.VehicleImageFacade;
import com.manheim.vim.services.vehicleimage.model.VehicleImage;

/**
 * ImageExportWorker.java A class representing a thread and responsible 
 * for querying UCM server for a particular image.
 * @author Enterpulse
 * @version 1.0 
 */
public class ImageExportWorker implements Runnable
{
	/** Logger to log exceptions in ImageExportWorker class*/
    private static final Logger exceptionLog = LoggerFactory.getLogger(ImageExportWorker.class);

    /** Contains an image name and one or more UCM content attribute details */
    private Manifest manifest;
    /** Contains one or more servers (Each with credentials like server url, port, description, name, etc) */
    private Group serverGroup;
    /** A Logger instance passed to ImageExportWorker from ImageExporter. This logger is used to log
     *  either to a specified file or console depending on the provider configuration */
    private Logger log;
    /** Contains information about the image that needs to be exported */
    private VehicleImage image;
    /** Contains configuration information for export sequence */
    private ExportConfig exportConfig;
    /** It is the maximum number of attempts each instance of ImageExportWorker should retry its task in case of an exception */
    private int maxAttempts;

    public ImageExportWorker(VehicleImage image, ExportConfig exportConfig, Manifest manifest, Group serverGroup,
            Logger log, int maxAttempts)
    {
        this.image = image;
        this.manifest = manifest;
        this.serverGroup = serverGroup;
        this.log = log;
        this.exportConfig = exportConfig;
        this.maxAttempts = maxAttempts;
    }

    /**
     * This is the implementation of Runnable interface's run() method. It queries
     * UCM server via DAO to export a particular image.
     */
    public void run()
    {
        try
        {
            VehicleImageFacade vehicleImageFacade = SpringHelper.getVehicleImageFacade();
            
            vehicleImageFacade.exportImage(image, exportConfig.getPath(), manifest,
                    exportConfig.getProviderName(), serverGroup, maxAttempts);
            
            log.debug("SUCCESS -->  " + " Image Name:  " + image.getMetadata().get("dDocName") + "\t"
                    + " has been successfully exported ");
            System.out.println(image.getMetadata().get("dDocName") + " has been exported successfully");
        }
        catch (ServicesException e)
        {
            log.error("EXCEPTION -->  " + " Image Name:  " + image.getMetadata().get("dDocName") + "\t"
                    + " has not been exported. ");

            exceptionLog.error("EXPORT -- Provider Name :" + exportConfig.getProviderName()
                    + " Image Name:  " + image.getMetadata().get("dDocName") + "\t" + " has not been exported. " + "\t"
                    + ExceptionHelper.getStackTraceAsString(e));
        }
    }
}