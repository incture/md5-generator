package oneapp.incture.workbox.demo.docusign.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.document.dto.DocumentResponseDto;
import oneapp.incture.workbox.demo.document.service.DocumentService;
import oneapp.incture.workbox.demo.docusign.dto.KeyDto;
import oneapp.incture.workbox.demo.sharepointFileUpload.SharePointService;
import oneapp.incture.workbox.demo.sharepointFileUpload.SharepointUploadFile;

@Component
public class AddKeys {

	private String privateKeyPath = "D:/WorkBox Adapter/Prkey.txt";
	private String publicKeyPath = "D:/WorkBox Adapter/PKey.txt";

	@Autowired
	DocumentService documentService;

	@Autowired
	SharePointService sharePointService;
	
	@Autowired
	SharepointUploadFile sharepointUploadFile;

	public DocumentResponseDto addpkey(KeyDto keys) throws IOException {
		AttachmentRequestDto attachmentRequestDto = new AttachmentRequestDto();
		AttachmentRequestDto privateattdto = new AttachmentRequestDto();
		// byte[] bytes = Files.readAllBytes(Paths.get(publicKeyPath));
		// String encodedPublicKey = Base64.getEncoder().encodeToString(bytes);
		attachmentRequestDto.setEncodedFileContent(keys.getEncpubkey());
		attachmentRequestDto.setFileName("publickey");
		attachmentRequestDto.setFileSize(keys.getEncpubkey().length());
		privateattdto.setEncodedFileContent(keys.getEncprikey());
		privateattdto.setFileName("privatekey");
		privateattdto.setFileSize(keys.getEncprikey().length());
		List<AttachmentRequestDto> listattachmentreqestdto = new ArrayList<>();
		listattachmentreqestdto.add(attachmentRequestDto);
		listattachmentreqestdto.add(privateattdto);
		System.err.println(listattachmentreqestdto);
		return sharepointUploadFile.uploadFile(listattachmentreqestdto,attachmentRequestDto.getFileName());
	}

	public AttachmentRequestDto getFile(String objectid) {
		System.err.println(objectid);
		return documentService.getOriginalAttachment(objectid);
	}

	public String getFileUsingSharepoint(String documentID) {
		String file = " ";
		try {
			
			//AttachmentDto attachment = attachmentDao.getAttachmentDetailsByID(documentID);
			byte[] originalDoc = sharePointService.performHttpRequest(HttpMethod.GET,
					"https://incturet.sharepoint.com/:t:/r/sites/Workbox_CF/Shared%20Documents/Workbox%20Attachments/"
							+ documentID); // + attachment.getFileType().split("/")[1]);
			byte[] encoded = Base64.getEncoder().encode(originalDoc);
			// attachment.setFile(originalDoc); //This line will give byte array
			file = new String(encoded); // This will give base64
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}
}