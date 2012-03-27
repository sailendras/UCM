ManheimImageCopyOutbound Component

Overview:
Component copies the checked in image file to a outbound directory location,
if CopyImageToOutboundDirectory is set to 'true' and dDocType is 'Image'

Configuration :
1. CopyImageToOutboundDirectory : Parameter to store true/false. Parameter will used to check whether to copy image.
2. OutboundDirectoryPath : Parameter to store outbound directory path to copy image (e.g. e:/outbound/ or  /home/outbound/). 
3. CopyOnlyForWebServiceInterface : Boolean parameter that indicates whether to copy images for web service interface (true) or for all interfaces (false/empty). 