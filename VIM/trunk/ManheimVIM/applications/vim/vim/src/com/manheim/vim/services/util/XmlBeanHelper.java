package com.manheim.vim.services.util;

import java.io.FileInputStream;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.manheim.vim.schemas.config.export.ExportConfigDocument;
import com.manheim.vim.schemas.config.export.ExportConfigDocument.ExportConfig;
import com.manheim.vim.schemas.config.manifest.ManifestDocument;
import com.manheim.vim.schemas.config.manifest.ManifestDocument.Manifest;
import com.manheim.vim.schemas.config.vim.VimConfigDocument;
import com.manheim.vim.schemas.config.vim.VimConfigDocument.VimConfig;
import com.manheim.vim.schemas.config.ximport.ImportConfigDocument;
import com.manheim.vim.schemas.config.ximport.ImportConfigDocument.ImportConfig;
import com.manheim.vim.services.exception.ConfigurationException;

/**
 * XmlBeanHelper.java - A helper class that parses an XML file and returns its object representation
 * @author Enterpulse
 * @version 1.0 
 */
public class XmlBeanHelper
{
	/** Logger to log exceptions in ServerGroupHelper class*/
	private static final Logger exceptionLog = LoggerFactory.getLogger(XmlBeanHelper.class);
	
    /**
     * This method accepts the path to the common XML configuration file, parses it and returns its object representation
     * @param configurationPath The path to the common XML configuration file 
     * @return VimConfig The object representation of the common configuration XML file
     */
    public static VimConfig loadVimConfig(String configurationPath)
    {
        InputStream configInputStream = null;
        
        try
        {
            configInputStream = new FileInputStream(configurationPath);
            
            VimConfigDocument document = VimConfigDocument.Factory.parse(configInputStream);
            
            return document.getVimConfig();
        }
        catch (Exception e)
        {
        	exceptionLog.error(ExceptionHelper.getStackTraceAsString(e));
            throw new ConfigurationException(e);
        }
        finally
        {
            try
            {
                configInputStream.close();
            }
            catch (Exception e)
            {
            	exceptionLog.error(ExceptionHelper.getStackTraceAsString(e));
                throw new ConfigurationException(e);
            }
        }
    }
    
    /**
     * This method accepts the path to the Import XML configuration file, parses it and returns its object representation
     * @param importPath The path to the Import XML configuration file 
     * @return ImportConfig The object representation of the import configuration XML file
     */
    public static ImportConfig loadImportConfig(String importPath)
    {
        InputStream configInputStream = null;
        
        try
        {
            configInputStream = new FileInputStream(importPath);
            
            ImportConfigDocument document = ImportConfigDocument.Factory.parse(configInputStream);
            
            return document.getImportConfig();
        }
        catch (Exception e)
        {
        	exceptionLog.error(ExceptionHelper.getStackTraceAsString(e));
            e.printStackTrace();
            throw new ConfigurationException(e);
        }
        finally
        {
            try
            {
                configInputStream.close();
            }
            catch (Exception e)
            {
            	exceptionLog.error(ExceptionHelper.getStackTraceAsString(e));
                throw new ConfigurationException(e);
            }
        }
    }
    
    /**
     * This method accepts the path to the Export XML configuration file, parses it and returns its object representation
     * @param exportPath The path to the Export XML configuration file 
     * @return ExportConfig The object representation of the export configuration XML file
     */
    public static ExportConfig loadExportConfig(String exportPath)
    {
        InputStream configInputStream = null;
        
        try
        {
            configInputStream = new FileInputStream(exportPath);
            
            ExportConfigDocument document = ExportConfigDocument.Factory.parse(configInputStream);
            
            return document.getExportConfig();
        }
        catch (Exception e)
        {
        	exceptionLog.error(ExceptionHelper.getStackTraceAsString(e));
            throw new ConfigurationException(e);
        }
        finally
        {
            try
            {
                configInputStream.close();
            }
            catch (Exception e)
            {
            	exceptionLog.error(ExceptionHelper.getStackTraceAsString(e));
                throw new ConfigurationException(e);
            }
        }
    }

    /**
     * This method accepts the path to the Manifest XML configuration file, 
     * parses it and returns its object representation
     * @param manifestPath The path to the Manifest XML configuration file 
     * @return Manifest The object representation of the manifest XML file
     */
    public static Manifest loadManifest(String manifestPath)
    {
        InputStream configInputStream = null;
        
        try
        {
            configInputStream = new FileInputStream(manifestPath);
            
            ManifestDocument document = ManifestDocument.Factory.parse(configInputStream);
            
            return document.getManifest();
        }
        catch (Exception e)
        {
        	exceptionLog.error(ExceptionHelper.getStackTraceAsString(e));
            throw new ConfigurationException(e);
        }
        finally
        {
            try
            {
                configInputStream.close();
            }
            catch (Exception e)
            {
            	exceptionLog.error(ExceptionHelper.getStackTraceAsString(e));
                throw new ConfigurationException(e);
            }
        }
    }
}
