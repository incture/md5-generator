package oneapp.incture.workbox.demo.sharepointFileUpload;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.document.dto.AttachmentDetail;
import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.document.dto.DocumentResponseDto;
import oneapp.incture.workbox.demo.document.util.DocumentServiceUtil;

@Service
public class SharepointUploadFile {

	@Autowired
	SharePointService sharePointService;

	public DocumentResponseDto uploadFile(List<AttachmentRequestDto> attachmentList ,String fileName) {

		DocumentResponseDto response = new DocumentResponseDto();

		List<AttachmentDetail> attachmentIds = new ArrayList<>();
		AttachmentDetail attachmentDetail = null;
		String objectId = null;
		String resultString = null;

		ResponseMessage respMessage = new ResponseMessage();
		respMessage.setMessage("Document upload failed . ");
		respMessage.setStatus(PMCConstant.FAILURE);
		respMessage.setStatusCode(PMCConstant.CODE_FAILURE); 

		try {
			for (AttachmentRequestDto attachment : attachmentList) {

				attachmentDetail = new AttachmentDetail();
				System.err.println(sharePointService);
				String fileId = "" + System.currentTimeMillis() + ServicesUtil.generateRandomString(7);
				resultString = sharePointService.attachFile("https://incturet.sharepoint.com/sites/Workbox_CF/_api/web/"
						+ "GetFolderByServerRelativeUrl(%27Shared%20Documents/Workbox%20Attachments%27)/files/add(url=%27"
						+ fileId + "%27,overwrite=false)",
						Base64.getDecoder().decode(attachment.getEncodedFileContent()));
				objectId = new JSONObject(resultString).getString("UniqueId");

				if (!ServicesUtil.isEmpty(objectId)) {
					attachmentDetail.setAttachmentId(fileId);
					attachmentDetail.setAttachmentType(attachment.getFileType());
					attachmentDetail.setAttachmentName(fileName);
					attachmentDetail.setAttachmentSize(attachment.getFileSize());
					System.err.println("[WBP-Dev]DocumentServiceImpl.uploadDocument() Object Id : " + objectId);
				}

				attachmentIds.add(attachmentDetail);
			}

			respMessage.setMessage("Document upload succefully . ");
			respMessage.setStatus(PMCConstant.SUCCESS);
			respMessage.setStatusCode(PMCConstant.CODE_SUCCESS);

			response.setAttachmentIds(attachmentIds);
			response.setResponse(respMessage);

		} catch (Exception e) {
			System.err.println("[WBP-Dev]DocumentServiceImpl.uploadDocument() error : " + e);
			respMessage.setMessage("Document upload failed . " + e);
			respMessage.setStatus(PMCConstant.FAILURE);
			respMessage.setStatusCode(PMCConstant.CODE_FAILURE);
			response.setResponse(respMessage);
		}

		return response;

	}

	public String getFileUsingSharepoint(String documentID) {
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
