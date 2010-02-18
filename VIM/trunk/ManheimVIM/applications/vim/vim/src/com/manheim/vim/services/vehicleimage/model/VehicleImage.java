package com.manheim.vim.services.vehicleimage.model;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.manheim.vim.schemas.config.metadata.Metadata;

/**
 * VehicleImage.java - A simple POJO representing a content item along with its metadata attributes
 * @author Enterpulse
 * @version 1.0 
 */
public class VehicleImage implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** The File object representing the content item*/
    protected File image;
    /** A map of metadata attributes' name and value for each content item (Image)*/
    protected Map<String, String> metadata;
    /** A collection of Metadata objects */
    protected List<Metadata> metadataList;

    public VehicleImage()
    {
    }
    
    public VehicleImage(File image, List<Metadata> metadataList)
    {
        this.image = image;
        this.metadataList = metadataList;
    }

    public File getImage()
    {
        return image;
    }

    public void setImage(File image)
    {
        this.image = image;
    }

    public Map<String, String> getMetadata()
    {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata)
    {
        this.metadata = metadata;
    }

    public List<Metadata> getMetadataList()
    {
        return metadataList;
    }

    public void setMetadataList(List<Metadata> metadataList)
    {
        this.metadataList = metadataList;
    }
}
