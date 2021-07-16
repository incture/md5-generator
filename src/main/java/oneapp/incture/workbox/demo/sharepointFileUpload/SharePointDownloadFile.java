package oneapp.incture.workbox.demo.sharepointFileUpload;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

public class SharePointDownloadFile {
	
	
	@Autowired
	static
	SharePointService sharePointService;
	
	public static String getFileUsingSharepoint(String documentID) {
		String file = " ";
		try {

			byte[] originalDoc = sharePointService.performHttpRequest(HttpMethod.GET,
					"https://incturet.sharepoint.com/:t:/r/sites/Workbox_CF/Shared%20Documents/Workbox%20Attachments/"
							+ documentID);
			byte[] encoded = Base64.getEncoder().encode(originalDoc);
			file = new String(encoded); // This will give base64
			System.err.println(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

}
