package com.manheim.ecm.filters;

import intradoc.common.ExecutionContext;
import intradoc.common.FileUtils;
import intradoc.common.ServiceException;
import intradoc.common.SystemUtils;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.Workspace;
import intradoc.shared.FilterImplementor;
import intradoc.shared.SharedObjects;

import java.util.Properties;
/**
 * Class implements the FilterImplmentor interface and implements doFilter method.
 * Class is hooked to postDocHistoryInfo filter event to copy images to 
 * an outbound directory location.
 */
public class ImageCopyOutboundFilter implements FilterImplementor {
	
	public static final String CLASS_NAME = "ImageCopyOutboundFilter";
	public static final String TRACING_SECTION = "ImageCopyOutboundFilter";
	public static final String IDC_SERVICE = "IdcService";
	public static final String IDC_SERVICE_CHECKIN_NEW = "CHECKIN_NEW";
	public static final String IDC_SERVICE_CHECKIN_UNIVERSAL = "CHECKIN_UNIVERSAL";
	// Metadata names constants.
	public static final String METADATA_NAME_DDOCTYPE = "dDocType";
	public static final String METADATA_NAME_DDOCNAME = "dDocName";
	public static final String METADATA_NAME_PRIM_FILE = "primaryFile";
	public static final String METADATA_NAME_PRIM_FILE_PATH = "primaryFile:path";
	public static final String METADATA_NAME_DORIGINAL_NAME = "dOriginalName";
	public static final String METADATA_NAME_WORKORDER_NUM = "xWorkOrderNumber";
	public static final String METADATA_NAME_AUCTION_ID = "xAuctionId";
	public static final String METADATA_NAME_IMG_SEQ = "xImageSequence";
	public static final String METADATA_NAME_DEXTENSION = "dExtension";
	public static final String METADATA_VALUE_DDOCTYPE_IMAGE = "Image";

	// Read prefrence variables.
	public static final String COPY_IMAGE_TO_OUTBOUND_DIRECTORY = "CopyImageToOutboundDirectory";
	public static final String OUTBOUND_DIRECTORY_PATH = "OutboundDirectoryPath";
	public static final String COPY_IMAGE_FOR_WS_INTERFACE = "CopyOnlyForWebServiceInterface";
	public static final String OPER_DOT = ".";
	
	/**
	 * Method gets invoked when particular filter event is executed. It copies the images
	 * to an outbound directory location.
	 * 
	 */
	public int doFilter(Workspace ws, DataBinder binder, ExecutionContext cxt)
											throws DataException, ServiceException {

		final String METHOD_NAME = ".doFilter() : ";
		trace(METHOD_NAME, "Entering in method");
		
		//Properties props = binder.m_localData;		
		//trace(METHOD_NAME, "props : " + props.toString());
		
		String copyForWSinterface = SharedObjects.getEnvironmentValue(COPY_IMAGE_FOR_WS_INTERFACE);
		
		String idcServiceName = binder.getLocal(IDC_SERVICE);
		String auth = binder.getLocal("Auth");
		
		trace(METHOD_NAME, "copyForWSinterface : " + copyForWSinterface 
				+ " ; idcServiceName : " + idcServiceName + " ; auth : " + auth);
				
		if ("true".equalsIgnoreCase(copyForWSinterface)) {
			
			// return if auth variable is not present in binder then return.
			if (auth == null) {
				
				return FilterImplementor.CONTINUE;
			}
		}
		
		// return if service is not CHECKIN_NEW or CHECKIN_UNIVERSAL.
		if ((!IDC_SERVICE_CHECKIN_NEW.equalsIgnoreCase(idcServiceName))
				&& (!IDC_SERVICE_CHECKIN_UNIVERSAL.equalsIgnoreCase(idcServiceName))) {
			
			return FilterImplementor.CONTINUE;
		}
		
		String dDocType = binder.getLocal(METADATA_NAME_DDOCTYPE);
		String dDocName = binder.getLocal(METADATA_NAME_DDOCNAME);
		// check whether to copy images to outbound dir.
		String copyImgToOutboundDir = SharedObjects.getEnvironmentValue(COPY_IMAGE_TO_OUTBOUND_DIRECTORY);
		trace(METHOD_NAME, "dDocName : " + dDocName + " ; dDocType : "
				+ dDocType + " ; copyImgToOutboundDir : " + copyImgToOutboundDir);
		
		// Return if image copy flag is not set to true.
		if (!"true".equalsIgnoreCase(copyImgToOutboundDir)) {
			
			return FilterImplementor.CONTINUE;
		}
		
		// return if dDocType is not Image.
		if (! METADATA_VALUE_DDOCTYPE_IMAGE.equalsIgnoreCase(dDocType)) {
			
			return FilterImplementor.CONTINUE;
		}
				
		String outboundDirPath = SharedObjects.getEnvironmentValue(OUTBOUND_DIRECTORY_PATH);
		//String origFileName = binder.getLocal(METADATA_NAME_DORIGINAL_NAME);
		
		String auctionId = binder.getLocal(METADATA_NAME_AUCTION_ID);
		String workOrder = binder.getLocal(METADATA_NAME_WORKORDER_NUM);
		String imageSeq = binder.getLocal(METADATA_NAME_IMG_SEQ);
		String imageSeqfinal = "";
		
		if (imageSeq != null && imageSeq.length() > 0) {
			
			int length = imageSeq.length();
			
			if ( length > 3) {
				
				imageSeqfinal = imageSeq.substring(0, 3);
				
			} else if (length == 3) {
				
				imageSeqfinal = imageSeq;
		
			} else if (length == 2) {
				
				imageSeqfinal = "0" + imageSeq;
			
			} else if (length == 1) {
				imageSeqfinal = "00" + imageSeq;
			}
		} else {			
			imageSeqfinal = "000";
		}
		
		// Get orginal file extension.
		String fileExt = binder.getLocal(METADATA_NAME_DEXTENSION);
		String fileName = binder.getLocal(METADATA_NAME_DORIGINAL_NAME);
		
		// if dExtension is null or empty then get ext from file name
		if (fileExt == null || fileExt.length() <= 0) {

			if (fileName != null) {

				int index = fileName.lastIndexOf(OPER_DOT);
				
				if (index >= 0) {

					fileExt = fileName.substring(index + 1);
				}
			}
		}
		
		
		String imgFileName = "";
			
		if (auctionId != null && auctionId.length() > 0) {
			
			imgFileName = auctionId + "_";
		}
		
		if (workOrder != null && workOrder.length() > 0) {
			
			imgFileName = imgFileName + workOrder;
		}
		
		// if auctionid. work order or image seq is not empty.
		if (imgFileName.length() > 0 | (imageSeq != null && imageSeq.length() > 0)) {
			
			imgFileName = imgFileName + imageSeqfinal + OPER_DOT + fileExt;
		
		} else { // if all 3 are empty then rename with image original name.
			
			imgFileName = fileName;
		}
		
				
		trace(METHOD_NAME, "outboundDirPath : " + outboundDirPath 	+ " ; auctionId : " + auctionId
				+ " ; workOrder : " + workOrder + " ; imageSeq : " + imageSeq
				+ " ; imgFileName : " + imgFileName);
		
		
		String filePath = null;
		String primFileName = binder.getLocal(METADATA_NAME_PRIM_FILE);
		String primFilePath = binder.getLocal(METADATA_NAME_PRIM_FILE_PATH);
		
		if (primFilePath != null && primFilePath.length() > 0 ) {
			
			filePath = primFilePath;
			
		} else if (primFileName != null && primFileName.length() > 0) {
			
			filePath = primFileName;
		}
		
		trace(METHOD_NAME, "primFilePath : " + primFilePath + " ; primFileName : " + primFileName
				+ " ; nativeFileLoc : " + filePath);
		
		if ((outboundDirPath != null && outboundDirPath.length() > 0)
				&& ( filePath != null && filePath.length() > 0)) {
		
			FileUtils.copyFile(filePath, outboundDirPath + imgFileName);
		}
		
		trace(METHOD_NAME, "Exiting from method");
		return FilterImplementor.CONTINUE;
	}
	
	/**
	 * This method is used to trace the information.
	 * 
	 * @param : methodName : name of method writing trace message.
	 * @param traceMessage : message to write in trace.
	 */
	private static void trace(String methodName, String traceMessage) {

		StringBuffer traceMsg = new StringBuffer();
		traceMsg.append(CLASS_NAME);
		traceMsg.append(methodName);
		traceMsg.append(traceMessage);

		SystemUtils.trace(TRACING_SECTION, traceMsg.toString());
	}


}
