package com.manheim.vim.imageexporter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.xmlbeans.XmlOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manheim.vim.VimCommand;
import com.manheim.vim.schemas.config.export.MetadataQuery;
import com.manheim.vim.schemas.config.export.ExportConfigDocument.ExportConfig;
import com.manheim.vim.schemas.config.manifest.ManifestDocument;
import com.manheim.vim.schemas.config.manifest.ManifestDocument.Manifest;
import com.manheim.vim.schemas.config.metadata.Metadata;
import com.manheim.vim.schemas.config.vim.Group;
import com.manheim.vim.schemas.config.vim.VimConfigDocument.VimConfig;
import com.manheim.vim.services.exception.ThreadExceptionHandler;
import com.manheim.vim.services.util.DateHelper;
import com.manheim.vim.services.util.ExceptionHelper;
import com.manheim.vim.services.util.LogHelper;
import com.manheim.vim.services.util.ServerGroupHelper;
import com.manheim.vim.services.vehicleimage.model.VehicleImage;

/**
 * ImageExporter.java - A class responsible for initiating batch export sequence. 
 * @author Enterpulse
 * @version 1.0 
 * @see ImageExportWorker QueryWorker
 */
public class ImageExporter extends VimCommand
{
	/** Logger instance for ImageExporter class. Used to log exceptions */
    private static final Logger exceptionLog = LoggerFactory.getLogger(ImageExporter.class);
    /** Contains application level configuration settings */
    private VimConfig vimConfig;
    /** Contains provider configuration settings for export */
    private ExportConfig exportConfig;
    /** A logger used to log success or failure into a separate log file or console based on configuration */
    private Logger log = null;
    
    public ImageExporter(VimConfig vimConfig, ExportConfig exportConfig)
    {
        this.vimConfig = vimConfig;
        this.exportConfig = exportConfig;
    }

    /**
     * This method is responsible for extracting required information from configuration objects,
     * dividing the export process into smaller tasks, spawn threads and assign one task per thread. 
     */
    public void execute()
    {
        try
        {
        	// Creates all directories on the path if they do not exist
        	new File(exportConfig.getPath() + File.separator).mkdirs();
        	
            // If a log required attribute is set, create one at the base path
            if(exportConfig.getLogRequired())
            {
                log = LogHelper.getLogger(exportConfig.getPath() + File.separator);
            }
            // Otherwise, print to console
            else
            {
                log = LoggerFactory.getLogger("consoleLogger");
            }
            
            // Delete previous batch export termination flag if exists
            
            File batchTerminationFile = new File(exportConfig.getPath() + File.separator
                    + "BatchExportComplete.log");
            if (batchTerminationFile.exists())
            {
                System.out.println("Batch export termination flag exists. Deleting it !");
                batchTerminationFile.delete();
            }

            Group serverGroup = ServerGroupHelper.getServerGroup(exportConfig.getGroupId(),
                    exportConfig.getProviderName(), vimConfig);

            List<VehicleImage> vehicleImages = Collections.synchronizedList(new ArrayList<VehicleImage>());

            List<MetadataQuery> metadataQueryList = exportConfig.getMetadataQueriesList();

            Thread.setDefaultUncaughtExceptionHandler(new ThreadExceptionHandler());

            ExecutorService executor = Executors.newFixedThreadPool(vimConfig.getNumberOfThreads());

            for (MetadataQuery mq : metadataQueryList)
            {
                List<Metadata> metadatalist = mq.getMetadataList();
                Map<String, String> metadataMap = new HashMap<String, String>();
                for (Metadata m : metadatalist)
                {
                    metadataMap.put(m.getName(), m.getValue());
                }
                
                // This worker thread queries UCM for image metadata, via DAO,
                // creates VehicleImage objects, and sets them into the
                // synchronized collection of VehicleImage objects
                Runnable worker = new QueryWorker(metadataMap, vehicleImages,
                        exportConfig, serverGroup, log, vimConfig.getMaxAttempts());
                executor.execute(worker);
            }

            // Do not accept any new requests until all threads finish
            executor.shutdown();

            // main thread will sleep until executor finishes the job
            while (!executor.isTerminated())
            {
                try
                {
                    Thread.sleep(THREAD_SLEEP_TIME);
                }
                catch (InterruptedException e)
                {
                    exceptionLog.error("EXPORT -- Provider Name :" + exportConfig.getProviderName() + "\t"
                            + " An Interrupted Exception has occured while querying UCM for vehicle images :" + "\t"
                            + ExceptionHelper.getStackTraceAsString(e));
                }
            }

            // At this point vehicleImages collection will have resultset of images 
            // (i.e., VehicleImage objects) each containing information of images to be exported.
            // Do a second query to UCM to export all images each using a single thread.
            ExecutorService executorSecond = Executors.newFixedThreadPool(vimConfig.getNumberOfThreads());

            // Each image exported by this thread needs to populate a ManifestEntry entry in the 
            // manifest.xml document. So, create a manifest object and pass it the thread--
            // which populates ManifestEntry for each image exported. Note: The manifest doc is 
            // internally synchronized.
            ManifestDocument manifestDocument = ManifestDocument.Factory.newInstance();
            Manifest manifest = manifestDocument.addNewManifest();
            for (VehicleImage image : vehicleImages)
            {
                Runnable worker = new ImageExportWorker(image, exportConfig, manifest,
                        serverGroup, log, vimConfig.getMaxAttempts());

                executorSecond.execute(worker);
            }

            // Do not accept any new requests until all threads finish
            executorSecond.shutdown();

            // Main thread will sleep until executor finishes the job
            while (!executorSecond.isTerminated())
            {
                try
                {
                    Thread.sleep(THREAD_SLEEP_TIME);
                }
                catch (InterruptedException e)
                {
                    exceptionLog.error("EXPORT -- Provider Name :" + exportConfig.getProviderName() + "\t"
                            + " An Interrupted Exception has occured while importing vehicle images :" + "\t"
                            + ExceptionHelper.getStackTraceAsString(e));
                }
            }

            // Generate the manifest file
            File file = new File(exportConfig.getPath() + File.separator + "manifest.xml");
            XmlOptions opts = new XmlOptions();
            opts.setSavePrettyPrint();
            opts.setSavePrettyPrintIndent(4);
            manifestDocument.save(file, opts);

            // Drop the termination file
            File terminationFile = new File(exportConfig.getPath() + File.separator + "BatchExportComplete.log");
            terminationFile.createNewFile();
        }
        catch (IOException e)
        {
            exceptionLog.error("EXPORT -- Provider Name :" + exportConfig.getProviderName() + "\t"
                    + " An IO Exception has occured in execute() method of ImageExporter class :" + "\t"
                    + ExceptionHelper.getStackTraceAsString(e)); 
        }
    }
}
