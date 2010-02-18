package com.manheim.vim.services.vehicleimage.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manheim.vim.schemas.config.manifest.MetadataEntry;
import com.manheim.vim.schemas.config.manifest.ManifestDocument.Manifest;
import com.manheim.vim.schemas.config.metadata.Metadata;
import com.manheim.vim.schemas.config.vim.Group;
import com.manheim.vim.services.exception.DaoException;
import com.manheim.vim.services.exception.ServicesException;
import com.manheim.vim.services.util.DateHelper;
import com.manheim.vim.services.util.ExceptionHelper;
import com.manheim.vim.services.util.QueryHelper;
import com.manheim.vim.services.vehicleimage.VehicleImageDao;
import com.manheim.vim.services.vehicleimage.VehicleImageFacade;
import com.manheim.vim.services.vehicleimage.model.VehicleImage;
import com.manheim.vim.services.vehicleimage.model.VehicleImageSaveStatus;

/**
 * VehicleImageServices.java - An implementation of VehicleImageFacade for communicating with UCM server via DAO
 * @author Enterpulse
 * @version 1.0 
 */
public class VehicleImageServices implements VehicleImageFacade
{
    private static final long serialVersionUID = 1L;

    /** Logger to log exceptions in VehicleImageServices class */
    private static final Logger exceptionLog = LoggerFactory.getLogger(VehicleImageServices.class);

    /** An implementation of VehicleImageDao, injected by Spring framework */
    private VehicleImageDao vehicleImageDao;
    
    private static final int BUFFER_SIZE = 64 * 1024;

    public VehicleImageServices(VehicleImageDao vehicleImageDao)
    {
        this.vehicleImageDao = vehicleImageDao;
    }
    
    public String getSecurityGroupId(String username, Group serverGroup, int maxAttempts)
    {
    	String securityGroupId = null;
    	
        if (maxAttempts <= 0)
            throw new ServicesException("Max attempts must be greater than 0");
        try
        {
        	// This while loop iterates for maxAttempts times
        	// where maxAttempts is a config parameter
            while (maxAttempts > 0)
            {
                try
                {
                    maxAttempts--;
                    securityGroupId = vehicleImageDao.querySecurityGroupId(username, serverGroup);
                    break;
                }
                catch (DaoException e)
                {
                    if (maxAttempts == 0)
                    {
                        throw e;
                    }
                    exceptionLog.error("IMPORT -- Provider Name :" + username + "\t"
                            + "Unable to retrieve security group Id. Trying again.");
                }
            }
        }
        catch (Exception e)
        {
            exceptionLog.error("IMPORT -- Provider Name :" + username + "\t"
                    + "Unable to retrieve security group Id.");
            throw new ServicesException(e);
        }
        return securityGroupId;
    }

    public VehicleImageSaveStatus saveImage(VehicleImage vehicleImage, String providerName, Group serverGroup,
            int maxAttempts, Map<String, String> defaultAttributes, String securityGroupId)
    {
        VehicleImageSaveStatus saveStatus = null;
        
        if (maxAttempts <= 0)
            throw new ServicesException("Max attempts must be greater than 0");

        try
        {
            while (maxAttempts > 0)
            {
                try
                {
                    maxAttempts--;
                    
                    //Step1 - Image checkin
                    saveStatus = vehicleImageDao.saveContent(
                            vehicleImage, providerName, serverGroup, defaultAttributes, securityGroupId);
                    
                    //Step2 -- Create lease for the above image
                    saveStatus = vehicleImageDao.createLeaseForSavedContent(
                    		saveStatus, providerName, serverGroup, securityGroupId);
                    
                    break;
                }
                catch (DaoException e)
                {
                    if (maxAttempts == 0)
                    {
                        throw e;
                    }
                    exceptionLog.error("IMPORT -- Provider Name :" + providerName + "\t"
                            + vehicleImage.getImage().getName() + " has not been checked in. Trying again.");
                }
            }
        }
        catch (Exception e)
        {
        	exceptionLog.error("IMPORT -- Provider Name :" + providerName + "\t"
                    + vehicleImage.getImage().getName() + " has not been checked in.");
            throw new ServicesException(e);
        }

        return saveStatus;
    }

    public List<VehicleImage> getImages(String query, List<String> requiredFieldsList, String providerName,
            Group serverGroup, int maxAttempts)
    {
        List<VehicleImage> vehicleimages = null;

        try
        {
            while (maxAttempts > 0)
            {
                try
                {
                    maxAttempts--;
                    vehicleimages = vehicleImageDao.findImages(
                            query, requiredFieldsList, providerName, serverGroup);
                    break;
                }
                catch (DaoException e)
                {
                    if (maxAttempts == 0)
                    {
                        throw e;
                    }
                    exceptionLog.error("EXPORT -- Provider Name :" + providerName + query + " has not been executed. Trying again");
                }
            }
        }
        catch (Exception e)
        {
        	exceptionLog.error("EXPORT -- Provider Name :" + providerName + query + " has not been executed.");
            throw new ServicesException(e);
        }

        return vehicleimages;
    }

    public List<VehicleImage> getImagesByMetadata(Map<String, String> metadataMap, List<String> manifestMetadataFields,
            String providerName, Group serverGroup, int maxAttempts)
    {
        return this.getImages(
                QueryHelper.getUCMQuery(metadataMap),
                manifestMetadataFields,
                providerName,
                serverGroup,
                maxAttempts);
    }

    public void exportImage(VehicleImage image, String destination, Manifest manifest, String providerName,
            Group serverGroup, int maxAttempts)
    {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        
        try
        {
            while (maxAttempts > 0)
            {
                try
                {
                    maxAttempts--;
                    
                    String docName = image.getMetadata().get("dDocName");

                    byte[] imageByteArray = vehicleImageDao.getImageAsByteArray(docName, providerName, serverGroup);
                    
                    File file = new File(destination + File.separator + docName + "." + image.getMetadata().get("dExtension"));
                    
                    inputStream = new ByteArrayInputStream(imageByteArray);

                    outputStream = new FileOutputStream(file);

                    int bytesRead;
                    byte[] buffer = new byte[BUFFER_SIZE];

                    do
                    {
                        bytesRead = inputStream.read(buffer);
                        
                        if (bytesRead >= 0)
                        {
                            outputStream.write(buffer, 0, bytesRead);
                            outputStream.flush();
                        }
                    }
                    while (bytesRead >= 0);
                    
                    // vehicle image export was successful, write meta data to manifest
                    MetadataEntry metadataEntry = manifest.addNewMetadataEntries();

                    metadataEntry.setImageName(image.getMetadata().get("dDocName") + "." +image.getMetadata().get("dExtension"));

                    for (String key : image.getMetadata().keySet())
                    {
                        Metadata metaData = metadataEntry.addNewMetadata();
                        metaData.setName(key);
                        metaData.setValue(image.getMetadata().get(key));
                    }
                    
                    break;
                }
                catch (DaoException e)
                {
                    if (maxAttempts == 0)
                    {
                        throw e;
                    }
                    exceptionLog.error("EXPORT -- Provider Name : " + providerName + image.getMetadata().get("dDocName")
                            + " has not been exported. Trying again.");
                }
                finally
                {
                    try
                    {
                        if (inputStream != null)
                        {
                            inputStream.close();
                            inputStream = null;
                        }

                        if (outputStream != null)
                        {
                            outputStream.close();
                            outputStream = null;
                        }
                    }
                    catch (IOException e)
                    {
                        exceptionLog.error(ExceptionHelper.getStackTraceAsString(e));
                        throw new ServicesException(e);
                    }
                }
            }
        }
        catch (Exception e)
        {
        	exceptionLog.error("EXPORT -- Provider Name : " + providerName + image.getMetadata().get("dDocName")
                    + " has not been exported.");
            
            throw new ServicesException(e);
        }
    }
}
