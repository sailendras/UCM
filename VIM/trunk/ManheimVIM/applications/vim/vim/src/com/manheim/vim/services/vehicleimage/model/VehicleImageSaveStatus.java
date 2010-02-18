package com.manheim.vim.services.vehicleimage.model;

/**
 * VehicleImageSaveStatus.java - A simple POJO representing UCM server response
 * @author Enterpulse
 * @version 1.0 
 */
public class VehicleImageSaveStatus
{
	/** The unique Id that represents a content item. Usually, this is the dDocName metadata attribute*/
    private String contentId;
    /** Content server returns a value for the status code that informs the success or failure status */
    private String statusCode;
    /** A verbose version of statusCode */
    private String message;
    /** This holds the originalID attribute value from the Image checkin -- the dID attribute */
    private String originalID;
    /** Holds the new Url for the image -- used in url pairing */
    private String newUrl;
    /** Holds the entry date for Lease*/
    private String inDate;
    /** Holds the auction ID */
    private String auctionId;
    /** Holds the work order number for creating a Lease*/
    private String workOrderNumber;
    /** Holds the image sequence number for creating a Lease */
    private String imageSequence;
    /** need to store dDocName and passed as xImageId for creating a Lease */
    private String dDocName;

    public VehicleImageSaveStatus(String contentId, String statusCode, String message, String originalId)
    {
        this.contentId = contentId;
        this.statusCode = statusCode;
        this.message = message;
        this.originalID = originalId;
    }

    public String getContentId()
    {
        return contentId;
    }

    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }

    public String getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(String statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

	public String getOriginalID()
	{
		return originalID;
	}

	public void setOriginalID(String originalID) 
	{
		this.originalID = originalID;
	}

	public String getNewUrl() {
		return newUrl;
	}

	public void setNewUrl(String newUrl) {
		this.newUrl = newUrl;
	}

	public String getInDate() 
	{
		return inDate;
	}

	public void setInDate(String inDate) 
	{
		this.inDate = inDate;
	}

	public String getAuctionId()
	{
		return auctionId;
	}

	public void setAuctionId(String auctionId)
	{
		this.auctionId = auctionId;
	}

	public String getWorkOrderNumber() 
	{
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) 
	{
		this.workOrderNumber = workOrderNumber;
	}

	public String getImageSequence() 
	{
		return imageSequence;
	}

	public void setImageSequence(String imageSequence) 
	{
		this.imageSequence = imageSequence;
	}

	public String getdDocName() 
	{
		return dDocName;
	}

	public void setdDocName(String dDocName) 
	{
		this.dDocName = dDocName;
	}
	
    
}
