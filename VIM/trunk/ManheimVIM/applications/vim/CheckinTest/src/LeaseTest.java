import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcClientManager;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.protocol.ServiceResponse;

public class LeaseTest
{
	@SuppressWarnings("unchecked")
	public void execute(String imageID) throws IOException
	{
    	try {
    		
    		Date todaysDate = new java.util.Date();
        	SimpleDateFormat formatter = new SimpleDateFormat("EEE_MMM_dd_HH_mm_ss_zzz_yyyy");
        	String formattedDate = formatter.format(todaysDate);
        	
    		IdcClientManager _clientManager = new IdcClientManager();
    		
			IdcClient _client = _clientManager.createClient("idc://atl-sandbox25.corp.enterpulse.com:4444");
			
			DataBinder dataBinder = _client.createBinder();
			
			IdcContext userContext = new IdcContext("sysadmin", "idc");
			
			/* Set all defaults for the metadata attributes */
			dataBinder.putLocal("dDocType", "Lease");
			dataBinder.putLocal("dDocName", "Test_Doc_Name"); // testing with same docName
			dataBinder.putLocal("dDocAuthor", "sysadmin");
			dataBinder.putLocal("dSecurityGroup", "Lease");
			dataBinder.putLocal("imageID", imageID);
			dataBinder.putLocal("dLinkType", "5");
			dataBinder.putLocal("IdcService", "CHECKIN_UNIVERSAL");
			dataBinder.putLocal("dDocTitle", formattedDate);
			dataBinder.putLocal("createPrimaryMetaFile", "true");
			
			ServiceResponse serviceResponse = _client.sendRequest(userContext, dataBinder);
			
			if(serviceResponse != null)
			{
				System.out.println("Image sent to the server");
				System.out.println(" Response from server is : "+serviceResponse.getResponseAsString());
			}
			else
			{
				System.out.println("Server did not respond.");
			}
			
				
		} catch (IdcClientException e) {
			e.printStackTrace();
		}
	}
}
