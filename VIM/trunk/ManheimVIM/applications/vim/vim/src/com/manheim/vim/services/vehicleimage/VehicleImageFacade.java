package com.manheim.vim.services.vehicleimage;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.manheim.vim.schemas.config.manifest.ManifestDocument.Manifest;
import com.manheim.vim.schemas.config.vim.Group;
import com.manheim.vim.services.vehicleimage.model.VehicleImage;
import com.manheim.vim.services.vehicleimage.model.VehicleImageSaveStatus;

/**
 * VehicleImageFacade.java - An Interface to service layer classes that typically use a DAO to communicate with the server.
 * @author Enterpulse
 * @version 1.0 
 */
public interface VehicleImageFacade extends Serializable 
{
    /**
     * This method communicates with the UCM server and imports the content item (Image). Based on the response from the server, it extracts
     * required response headers like contentId, status code, response message etc and constructs the VehicleImageSaveStatus object which is
     * returned back to the caller. 
     * @param vehicleImage The VehicleImage object containing a content's filename and related metadata attributes
     * @param providerName The content provider name
     * @param serverGroup The Group object that contains one or more server details
     * @param maxAttempts The maximum number of times to attempt to complete task if an exception occurs
     * @param defaultAttributes A map of default metadata attribute names and values
     * @param securityGroupId current users security group
     * @return VehicleImageSaveStatus An object to hold header values from the server's response
     */
    public VehicleImageSaveStatus saveImage(VehicleImage vehicleImage, String providerName, 
    		Group serverGroup, int maxAttempts, Map<String, 
    		String> defaultAttributes, String securityGroupId);
    
    /**
     * This method queries the UCM for content items. For each content item (Image) returned by the query, it constructs 
     * a VehicleImage object and adds it to a collection of VehicleImage objects. This
     * collection is returned back to the caller. 
     * @param query String that represents a query to the UCM server
     * @param manifestMetadataFields A collection of strings that are used to create the manifest details for each content item
     * @param providerName The content provider name
     * @param serverGroup The Group object that contains one or more server details
     * @param maxAttempts The maximum number of times to attempt to complete task if an exception occurs
     * @return List<VehicleImage> A collection of VehicleImage objects created based on the server's response to the query
     */
    public List<VehicleImage> getImages(String query, List<String> manifestMetadataFields, String providerName, Group serverGroup, int maxAttempts);

    /**
     * This method constructs the query string from the meta data attributes and calls the getImages() method
     * @param metadata A map of content's metadata attribute names and values
     * @param manifestMetadataFields A collection of strings that are used to create the manifest details for each content item
     * @param providerName The content provider name
     * @param serverGroup The Group object that contains one or more server details
     * @param maxAttempts The maximum number of times to attempt to complete task if an exception occurs
     * @return List<VehicleImage> A collection of VehicleImage objects created based on the server's response to the query
     */
    public List<VehicleImage> getImagesByMetadata(Map<String, String> metadata, List<String> manifestMetadataFields, String providerName,  Group serverGroup, int maxAttempts);
    
    /**
     * This method retrieves the content item from UCM by the contentId and writes its binary data to the directory specified by
     * the destination
     * @param image The VehicleImage object containing a content's filename and related metadata attributes
     * @param destination The path to which the image should be exported
     * @param manifest The manifest object (XMLBeans document) to which an image's metadata, name should be added.
     * @param providername The content provider name
     * @param serverGroup The Group object that contains one or more server details
     * @param maxAttempts The maximum number of times to attempt to complete task if an exception occurs
     */
    public void exportImage(VehicleImage image, String destination, Manifest manifest, String providername, Group serverGroup, int maxAttempts);
    
    /**
     * This method retrieves the current users security group Id using the username
     * @param username The content providers username
     * @param serverGroup The Group object that contains one or more server details
     * @param maxAttempts The maximum number of times to attempt to complete task if an exception occurs
     * @return String The security group Id for current user
     */
    public String getSecurityGroupId(String username, Group serverGroup, int maxAttempts);
    
}
