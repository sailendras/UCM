package com.manheim.vim.services.vehicleimage.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.DataObject;
import oracle.stellent.ridc.model.DataResultSet;
import oracle.stellent.ridc.protocol.ServiceResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.manheim.vim.schemas.config.metadata.Metadata;
import com.manheim.vim.schemas.config.vim.Group;
import com.manheim.vim.services.exception.DaoException;
import com.manheim.vim.services.util.ExceptionHelper;
import com.manheim.vim.services.vehicleimage.VehicleImageDao;
import com.manheim.vim.services.vehicleimage.model.VehicleImage;
import com.manheim.vim.services.vehicleimage.model.VehicleImageSaveStatus;

/**
 * UcmRidcVehicleImageDao.java - An implementation of VehicleImageDao for communicating with UCM server using RIDC API
 * @author Enterpulse
 * @version 1.0 
 */
public class UcmRidcVehicleImageDao implements VehicleImageDao
{
    private static final long serialVersionUID = 1L;

    /** Logger to log exceptions in UcmRidcVehicleImageDao class */
    private static final Logger exceptionLog = LoggerFactory.getLogger(UcmRidcVehicleImageDao.class);

    private static final int BUFFER_SIZE = 64 * 1024;

    @SuppressWarnings("unchecked")
    public List<VehicleImage> findImages(String query, List<String> requiredFieldsList, String providerName,
            Group serverGroup)
    {
        List<VehicleImage> vehicleImages = new ArrayList<VehicleImage>();

        UcmRidcConnectionManager ucmConnManager = UcmRidcConnectionManager.getInstance(providerName, serverGroup);
        IdcClient client = ucmConnManager.getNextClient();

        DataBinder contentListBinder = client.createBinder();
        contentListBinder.putLocal("IdcService", "GET_SEARCH_RESULTS");
        contentListBinder.putLocal("QueryText", query);
        //TODO remove this limit after testing
        contentListBinder.putLocal("ResultCount", "30");

        try
        {
        	
            ServiceResponse contentListResponse = 
                client.sendRequest(ucmConnManager.getUserContext(), contentListBinder);
            if(contentListResponse != null)
            {
	            DataBinder contentListData = contentListResponse.getResponseAsBinder();
	            if(contentListData != null)
	            {
		            DataResultSet imagesResultSet = contentListData.getResultSet("SearchResults");
		            if(imagesResultSet != null)
		            {
			            List<DataObject> dataObjects = imagesResultSet.getRows();
			            for (DataObject data : dataObjects)
			            {
			                VehicleImage image = new VehicleImage();
			                Map<String, String> metadata = new HashMap<String, String>();
			                
			                for (String metadataField : requiredFieldsList)
			                {
			                    metadata.put(metadataField, data.get(metadataField));
			                }
			                
			                metadata.put("dExtension", data.get("dExtension"));
			                image.setMetadata(metadata);
			                vehicleImages.add(image);
			            }
		            }
		            else
		            throw new DaoException("findImages() - DataResultSet is null");	
	            }
	            else
		        throw new DaoException("findImages() - DataBinder is null");	
            }
            else
	        throw new DaoException("findImages() - ServiceResponse is null");	
        }
        catch (IdcClientException e)
        {
            exceptionLog.error("EXPORT -- Provider Name: " + providerName + "\t" + ExceptionHelper.getStackTraceAsString(e));
            throw new DaoException(e);
        }
        catch (Exception e)
        {
            exceptionLog.error("EXPORT -- Provider Name: " + providerName + "\t" + ExceptionHelper.getStackTraceAsString(e));
            throw new DaoException(e);
        }

        return vehicleImages;

    }

    public String querySecurityGroupId(String username, Group serverGroup)
    {
    	try
    	{
    		UcmRidcConnectionManager ucmConnManager = UcmRidcConnectionManager.getInstance(username, serverGroup);
    		DataBinder dataBinder = ucmConnManager.getNextClient().createBinder();
			dataBinder.putLocal("IdcService", "QUERY_USER_ATTRIBUTES");
			dataBinder.putLocal("dName", username);
    		ServiceResponse serviceResponse = ucmConnManager.getNextClient().sendRequest(
                    ucmConnManager.getUserContext(), dataBinder);
    		
			if(serviceResponse != null)
			{
				String userAttributeInfo;

				DataBinder responseBinder = serviceResponse.getResponseAsBinder();
				
				if(responseBinder != null)
				{
					DataResultSet imagesResultSet = responseBinder.getResultSet("UserAttribInfo");
					
					if(imagesResultSet != null)
					{
						List<DataObject> dataObjects = imagesResultSet.getRows();
						
		            	DataObject data = dataObjects.get(dataObjects.size()-1);
		            	userAttributeInfo = data.get("AttributeInfo");
		            	if(StringUtils.isNotEmpty(userAttributeInfo))
		            	{
		            		return processAttributeInfo(userAttributeInfo);
		            	}
					}
					else
					throw new DaoException("querySecurityGroupId() - Unable to get data binder from the response");
				}
				else
				throw new DaoException("querySecurityGroupId() - UserAttributeInfo within the response object is null");
			}
			else
			throw new DaoException("querySecurityGroupId() - UCM server response is null");
			
    	}
        catch (DaoException e)
        {
            throw e;
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            throw new DaoException(e);
        }
        
		return null;
    }
    
    // This method parses the attribute info string for a provider role.
    // If a match is found, replace the provider string with image and return
    // Note: If the provider does not have a provider role, security group ID will remain null
    private String processAttributeInfo(String attributeInfo)
    {
    	// AttributeInfo header is a string, seperated by commas
    	// set of 3 values, seperated by commas represent information regarding an attribute 
    	String[] attributes = attributeInfo.split(",");
    	int roleNameIndex = 0;
    	String securityGroupId = null;
    	
    	// While there are more attributes (containing 3 csv values)
    	while(roleNameIndex + 3 <= attributes.length )
    	{
    		// If the current index of csv array equals role, it means that
    		// current index + 1 will have the value of the role. Note: this value
    		// will not change as this is a fixed result format from UCM
    		if("role".equals(attributes[roleNameIndex]))
    		{
    			if(attributes[roleNameIndex+1].contains("_provider"))
    			{
    				// replace the _provider text with _image, which is the security group of the user
    				securityGroupId = attributes[roleNameIndex+1].split("_")[0] + "_image" ;
    				break;
    			}
    		}
    		// Increment the index of next attribute to read from attribute info 
    		roleNameIndex = roleNameIndex + 3;
    	}
    	return securityGroupId;
    }
    
    
    @SuppressWarnings("unchecked")
	public VehicleImageSaveStatus createLeaseForSavedContent(VehicleImageSaveStatus serverResponse, 
    								String providerName, Group serverGroup, String securityGroupId)
    {
        try
        {
            UcmRidcConnectionManager ucmConnManager = UcmRidcConnectionManager.getInstance(providerName, serverGroup);
            IdcClient idcClient = ucmConnManager.getNextClient();
            DataBinder dataBinder = idcClient.createBinder();
            
            dataBinder.putLocal("originalSource", "CS"); // Hard coded as per requirement
            dataBinder.putLocal("dDocType", "Lease"); // Hard coded as per requirement
            dataBinder.putLocal("originalID", serverResponse.getOriginalID());
            dataBinder.putLocal("dLinkTypeID", "5"); // Hard coded as per requirement
            dataBinder.putLocal("isRelatedContent", "1"); // Hard coded as per requirement
            dataBinder.putLocal("xIsRecord", "0"); // Hard coded as per requirement
            dataBinder.putLocal("createPrimaryMetaFile", "true"); // Hard coded as per requirement
            dataBinder.putLocal("dSecurityGroup", "Admin_Lease"); // Hard coded as per requirement
            dataBinder.putLocal("xImageId", serverResponse.getdDocName());
            dataBinder.putLocal("IdcService", "CHECKIN_UNIVERSAL"); 
            
            ServiceResponse serviceResponse = idcClient.sendRequest(
                    ucmConnManager.getUserContext(), dataBinder);
            
            if (serviceResponse != null)
            {
                DataBinder binderResponse = serviceResponse.getResponseAsBinder();

                if (binderResponse != null)
                {
                    String statusCode = binderResponse.getLocal("StatusCode");
                    
                    if (StringUtils.isEmpty(statusCode))
                    {
                        throw new DaoException("createLeaseForSavedContent() - UCM server did not return a success status code");
                    }

                    String message = binderResponse.getLocal("StatusMessage");
                    String newUrl = binderResponse.getLocal("xNewUrl");
                    String inDate = binderResponse.getLocal("dInDate");


                    if (!"0".equals(statusCode))
                    {
                        throw new DaoException("createLeaseForSavedContent() - Checkin was unsuccessful. Status code is not 0. ");
                    }
                    
                    serverResponse.setStatusCode(statusCode);
                    serverResponse.setMessage(message);
                    serverResponse.setNewUrl(newUrl);
                    serverResponse.setInDate(inDate);

                    return serverResponse;
                }
                else
                throw new DaoException("createLeaseForSavedContent() - DataBinder is null");
            }
            else
            throw new DaoException("createLeaseForSavedContent() - ServiceResponse is null");
        }
        catch (DaoException e)
        {
            throw e;
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            throw new DaoException(e); 
        }
    }
    
    @SuppressWarnings("unchecked")
	public VehicleImageSaveStatus saveContent(VehicleImage vehicleImage, String providerName, Group serverGroup,
            Map<String, String> defaultAttributes, String securityGroupId)
    {
        try
        {
            UcmRidcConnectionManager ucmConnManager = UcmRidcConnectionManager.getInstance(providerName, serverGroup);
            IdcClient idcClient = ucmConnManager.getNextClient();
            DataBinder dataBinder = idcClient.createBinder();
        	
            // Set all defaults for the metadata attributes
            if (defaultAttributes != null)
            {
                for (Iterator<String> it = defaultAttributes.keySet().iterator(); it.hasNext(); )
                {
                    String metadataName = it.next();
                    dataBinder.putLocal(metadataName, defaultAttributes.get(metadataName));
                }
            }
            
            // Set all metadata attributes to the data binder
            for (Metadata metadata : vehicleImage.getMetadataList())
            {
                dataBinder.putLocal(metadata.getName(), metadata.getValue());
            }
            
            dataBinder.putLocal("dSecurityGroup", securityGroupId);
            dataBinder.putLocal("IdcService", "CHECKIN_UNIVERSAL");
            dataBinder.addFile("primaryFile", vehicleImage.getImage());
            
            ServiceResponse serviceResponse = idcClient.sendRequest(
                    ucmConnManager.getUserContext(), dataBinder);
            
            if (serviceResponse != null)
            {
                DataBinder binderResponse = serviceResponse.getResponseAsBinder();

                if (binderResponse != null)
                {
                    String statusCode = binderResponse.getLocal("StatusCode");
                    
                    if (StringUtils.isEmpty(statusCode))
                        throw new DaoException("saveContent() - UCM server did not return a success status code");

                    String contentId = binderResponse.getLocal("xImageId");
                    String message = binderResponse.getLocal("StatusMessage");
                    String originalId = binderResponse.getLocal("dID"); 
                    String auctionId = binderResponse.getLocal("xAuctionId");
                    String workOrderNumber = binderResponse.getLocal("xWorkOrderNumber");
                    String imageSequence = binderResponse.getLocal("xImageSequence");
                    String docName = binderResponse.getLocal("dDocName");

                    if (!"0".equals(statusCode))
                        throw new DaoException("saveContent() - Checkin was unsuccessful. Status code is not 0. ");
                    
                    
                    VehicleImageSaveStatus status = new VehicleImageSaveStatus(contentId, statusCode, message, originalId);
                    status.setAuctionId(auctionId);
                    status.setWorkOrderNumber(workOrderNumber);
                    status.setImageSequence(imageSequence);
                    status.setdDocName(docName);
                    
                    return status;
                }
                else
                throw new DaoException("saveContent() - UCM server did not return a valid response");
            }
            else
            throw new DaoException("saveContent() - UCM server did not return a valid response");
        }
        catch (DaoException e)
        {
            throw e;
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            throw new DaoException(e); 
        }
    }

    @SuppressWarnings("unchecked")
    public byte[] getImageAsByteArray(String imageId, String providerName, Group serverGroup)
    {
        UcmRidcConnectionManager ucmConnManager = UcmRidcConnectionManager.getInstance(
                providerName, serverGroup);

        IdcClient client = ucmConnManager.getNextClient();

        DataBinder dataBinder = client.createBinder();
        dataBinder.putLocal("IdcService", "GET_FILE");
        dataBinder.putLocal("dDocName", imageId);
        dataBinder.putLocal("RevisionSelectionMethod", "LatestReleased");

        ServiceResponse response = null;
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;

        try
        {
            response = client.sendRequest(ucmConnManager.getUserContext(), dataBinder);
            
            outputStream = new ByteArrayOutputStream();
            // get response as stream returns the image as input stream
            inputStream = response.getResponseStream();

            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            do
            {
                bytesRead = inputStream.read(buffer);

                if (bytesRead >= 0)
                {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            while (bytesRead >= 0);

            outputStream.flush();
            
            inputStream.close();

            byte[] imageByteArray = outputStream.toByteArray();
            
            return imageByteArray;
        }
        catch (IdcClientException e)
        {
            exceptionLog.error("EXPORT -- Provider Name: " + providerName + "\t An IdcClient Exception occurred. dDocName : " + imageId + "\t" + ExceptionHelper.getStackTraceAsString(e));
            throw new DaoException(e);
        }
        catch (IOException e)
        {
            exceptionLog.error("EXPORT -- Provider Name: " + providerName + "\t An IOException has occurred. dDocName : " + imageId + "\t"  + ExceptionHelper.getStackTraceAsString(e));
            throw new DaoException(e);
        }
        catch (Exception e)
        {
            exceptionLog.error("EXPORT -- Provider Name: " + providerName + "\t An Exception has occurred. dDocName : " + imageId + "\t"  + ExceptionHelper.getStackTraceAsString(e));
            throw new DaoException(e);
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
                
                if (response != null)
                    response.close();
            }
            catch (IOException e)
            {
                exceptionLog.error("EXPORT -- Provider Name: " + providerName + "\t An IOException has occurred while exporting the image. dDocName : " + imageId + "\t"  + ExceptionHelper.getStackTraceAsString(e));
                throw new DaoException(e);
            }
        }
    }
}
