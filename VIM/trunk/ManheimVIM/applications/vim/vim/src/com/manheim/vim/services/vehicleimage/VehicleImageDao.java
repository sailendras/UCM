package com.manheim.vim.services.vehicleimage;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.manheim.vim.schemas.config.vim.Group;
import com.manheim.vim.services.vehicleimage.model.VehicleImage;
import com.manheim.vim.services.vehicleimage.model.VehicleImageSaveStatus;

/**
 * VehicleImageDao.java - An Interface to Classes that use
 * UCM specific protocol to communicate with the server.
 * @author Enterpulse
 * @version 1.0 
 */
public interface VehicleImageDao extends Serializable 
{
	
    /**
     * This method communicates with the UCM server and saves the content to it.
     * @param vehicleImage The VehicleImage object containing a content's filename and related metadata attributes
     * @param providerName The content provider name
     * @param serverGroup The Group object that contains one or more server details
     * @param defaultAttributes A map of default metadata attribute names and values
     * @param securityGroupId current users security group
     * @return VehicleImageSaveStatus An object to hold header values from the server's response
     */
    public VehicleImageSaveStatus saveContent(VehicleImage vehicleImage, String providerName,
    		Group serverGroup, Map<String, String> defaultAttributes, 
    		String securityGroupId);
    
    /**
     * This method communicates with the UCM server and queries it to retrieve content items' details.
     * @param query String that represents a query to the UCM server
     * @param manifestMetadataFields A collection of strings that are used to create the manifest details for each content item
     * @param providerName The content provider name
     * @param serverGroup The Group object that contains one or more server details
     * @return List<VehicleImage> A collection of VehicleImage objects created based on the server's response to the query
     */
    public List<VehicleImage> findImages(String query, List<String> manifestMetadataFields, String providerName, Group serverGroup);
    
    /**
     * This method communicates with the UCM server, finds the content
     * by its Id. If a match is found, returns its binary data as a byte array
     * @param imageId The content ID for the content (image)
     * @param providerName The content provider name
     * @param serverGroup The Group object that contains one or more server details
     * @return byte[] The content item returned as a byte array
     */
    public byte[] getImageAsByteArray(String imageId, String providerName, Group serverGroup);
    
    /**
     * This method queries the UCM server for the current users security group Id
     * @param username The content provider name
     * @param serverGroup The Group object that contains one or more server details
     * @return String The security group Id for current user
     */
    public String querySecurityGroupId(String username, Group serverGroup);
    
    /**
     * @param VehicleImageSaveStatus An object to hold header values from the server's response
     * @param providerName The content provider name
     * @param serverGroup The Group object that contains one or more server details
     * @param securityGroupId current users security group
     * @return VehicleImageSaveStatus An object to hold header values from the server's response
     */
    public VehicleImageSaveStatus createLeaseForSavedContent(VehicleImageSaveStatus serverResponse, String providerName, Group serverGroup, String securityGroupId);
    
}
