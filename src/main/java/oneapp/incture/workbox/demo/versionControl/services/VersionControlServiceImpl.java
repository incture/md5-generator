package oneapp.incture.workbox.demo.versionControl.services;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.sharepointFileUpload.SharePointService;
import oneapp.incture.workbox.demo.versionControl.dao.VersionControlDao;
import oneapp.incture.workbox.demo.versionControl.dto.AttachmentDto;
import oneapp.incture.workbox.demo.versionControl.dto.DocumentResponseDto;
import oneapp.incture.workbox.demo.versionControl.dto.TypesDetailDto;
import oneapp.incture.workbox.demo.versionControl.dto.VersionControlDto;
import oneapp.incture.workbox.demo.versionControl.dto.VersionControlResponseDto;
import oneapp.incture.workbox.demo.versionControl.dto.VersionDetailDto;
import oneapp.incture.workbox.demo.versionControl.dto.VersionsDto;
import oneapp.incture.workbox.demo.versionControl.util.DocumentServiceUtil;
import oneapp.incture.workbox.demo.versionControl.util.Response;
import oneapp.incture.workbox.demo.versionControl.util.ServicesUtilVersion;

@Service("VersionControlServiceImpl")
public class VersionControlServiceImpl implements VersionControlService {

	@Autowired
	VersionControlDao versionControlDao;

	@Autowired
	ServicesUtilVersion timeZoneConvertion;
	
	@Autowired
	SharePointService sharePointService;

	@Override
	public VersionControlResponseDto getAllVersion() {

		VersionControlResponseDto versionControl = new VersionControlResponseDto();
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		try {
			List<VersionsDto> versions = versionControlDao.getAllVersions();

			responseDto.setStatus(PMCConstant.SUCCESS);
			responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);

			versionControl.setVersions(versions);
			versionControl.setVersionDetails(null);
			versionControl.setMessage(responseDto);
			responseDto.setMessage("Data Fetched Successfully");
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");
		} catch (Exception e) {
			System.err.println("[VersionControlServiceImpl][getAllVersion][error]" + e.getMessage());
			responseDto.setMessage("Failed to getAllVersion");
		}
		return versionControl;
	}

	@Override
	public ResponseMessage createNewVersion(VersionDetailDto dto) {

		VersionControlDto versionControl = new VersionControlDto();
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);

		try {

			versionControl.setVersionNumber(dto.getVersionNumber());
			versionControl.setProjectCode(dto.getProjectCode());
			versionControl.setProjectName(dto.getProjectName());
			versionControl
					.setDateOfRelease(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
			versionControl.setLanguage(dto.getTechnicalInformation().getLanguage());
			versionControl.setOsDetails(dto.getTechnicalInformation().getOsDetails());
			versionControl.setAuthor(dto.getTechnicalInformation().getAuthor());
			versionControl.setApplicationSize(dto.getTechnicalInformation().getApplicationSize());
			versionControl.setUsers(dto.getTechnicalInformation().getUsers());
			versionControl.setFrontendVersion(dto.getTechnicalInformation().getFrontendVersion());
			versionControl.setGitDetails(dto.getTechnicalInformation().getGitDetails());

			if (!ServicesUtil.isEmpty(dto.getWhatsNew())) {
				for (TypesDetailDto typesDetailDto : dto.getWhatsNew()) {
					versionControl.setVersionId(UUID.randomUUID().toString());
					versionControl.setDetailType(typesDetailDto.getDetailType());
					versionControl.setLabelDesc(typesDetailDto.getLabelDesc());
					versionControl.setDescription(typesDetailDto.getDescription());
					versionControl.setLinkLabel(typesDetailDto.getLinkLabel());
					versionControl.setLink(typesDetailDto.getLink());
					versionControl.setDocumentId(typesDetailDto.getDocumentId());

					System.err.println("VersionControlServiceImpl.createNewVersion()  " + versionControl);
					versionControlDao.create(versionControl);
				}
			}

			if (!ServicesUtil.isEmpty(dto.getBugFixes())) {
				for (TypesDetailDto typesDetailDto : dto.getBugFixes()) {
					versionControl.setVersionId(UUID.randomUUID().toString());
					versionControl.setDetailType(typesDetailDto.getDetailType());
					versionControl.setLabelDesc(typesDetailDto.getLabelDesc());
					versionControl.setDescription(typesDetailDto.getDescription());
					versionControl.setLinkLabel(typesDetailDto.getLinkLabel());
					versionControl.setLink(typesDetailDto.getLink());
					versionControl.setDocumentId(typesDetailDto.getDocumentId());

					System.err.println("VersionControlServiceImpl.createNewVersion()  " + versionControl);
					versionControlDao.create(versionControl);
				}
			}

			if (!ServicesUtil.isEmpty(dto.getImprovements())) {
				for (TypesDetailDto typesDetailDto : dto.getImprovements()) {
					versionControl.setVersionId(UUID.randomUUID().toString());
					versionControl.setDetailType(typesDetailDto.getDetailType());
					versionControl.setLabelDesc(typesDetailDto.getLabelDesc());
					versionControl.setDescription(typesDetailDto.getDescription());
					versionControl.setLinkLabel(typesDetailDto.getLinkLabel());
					versionControl.setLink(typesDetailDto.getLink());
					versionControl.setDocumentId(typesDetailDto.getDocumentId());

					System.err.println("VersionControlServiceImpl.createNewVersion()  " + versionControl);
					versionControlDao.create(versionControl);
				}
			}

			responseDto.setMessage("Version " + PMCConstant.CREATED_SUCCESS);
			responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
			responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
		} catch (Exception e) {
			responseDto.setMessage("Failed to create new version");
			System.err.println("[WBP-Dev][Workbox][PMC][createNewVersion][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public VersionControlResponseDto getVersionDetail(String versionNumber) {

		VersionControlResponseDto versionControl = new VersionControlResponseDto();
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		try {
			
			if (ServicesUtil.isEmpty(versionNumber))
				versionNumber = versionControlDao.getCurrentVersion();
			
			VersionDetailDto versionDetails = versionControlDao.getVersionDetails(versionNumber);

			responseDto.setStatus(PMCConstant.SUCCESS);
			responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);

			versionControl.setVersionDetails(versionDetails);
			versionControl.setVersions(null);
			versionControl.setMessage(responseDto);
			responseDto.setMessage("Data Fetched Successfully");
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");
		} catch (Exception e) {
			System.err.println("[VersionControlServiceImpl][getAllVersion][error]" + e.getMessage());
			responseDto.setMessage("Failed to getAllVersion");
		}
		return versionControl;
	}

	@Override
	public DocumentResponseDto uploadDocument(List<AttachmentDto> attachmentList, String versionId) {
		DocumentResponseDto response = new DocumentResponseDto();
		String documentID = "";
		boolean flag = false;

		ResponseMessage respMessage = new ResponseMessage();
		respMessage.setMessage("Document upload failed!");
		respMessage.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		respMessage.setStatusCode("" + HttpStatus.INTERNAL_SERVER_ERROR.value());

		try 
		{
			for (AttachmentDto attachment : attachmentList) 
			{
				VersionControlDto versionControl = new VersionControlDto();
				long start = System.currentTimeMillis();
//				documentID = DocumentServiceUtil.uploadToDocumentService(attachment.getFile(), attachment.getFileName(), attachment.getFileType());

				String result = sharePointService.attachFile("https://incturet.sharepoint.com/sites/Workbox_CF/_api/web/"
						+ "GetFolderByServerRelativeUrl(%27Shared%20Documents/Workbox%20Attachments%27)/files/add(url=%27"
						+ attachment.getFileName() + "%27,overwrite=false)",
						attachment.getFile());
				
				documentID = new JSONObject(result).getString("UniqueId");
				System.err.println("documentID"+documentID);
				System.err.println("DocumentService.uploadDocument() file " + attachment.getFileName() + " time : " + (System.currentTimeMillis() - start));

				if (!ServicesUtil.isEmpty(documentID)) 
				{
					versionControl.setDocumentId(documentID);
					versionControl.setVersionId(versionId);
					versionControlDao.updateVersion(versionControl);
					System.err.println("DocumentService.uploadDocument() documentID : " + documentID);
					response.setDocumentId(documentID);
				}
				else 
				{
					flag = true;
					response.addAttachment(attachment);     //details of files that couldn't get uploaded
				}
			}

			if (!flag) {
				respMessage.setMessage("Document upload successfully!");
				respMessage.setStatus(HttpStatus.OK.getReasonPhrase());
				respMessage.setStatusCode("" + HttpStatus.OK.value());
				System.err.println("DocumentService.uploadDocument() documentID : " + documentID);
			} else {
				respMessage.setMessage("Document upload failed!");
				respMessage.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
				respMessage.setStatusCode("" + HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
			response.setResponse(respMessage);
		} 
		catch (Exception e) 
		{
			System.err.println("DocumentService.uploadDocument() error : " + e);
			respMessage.setMessage("Document upload failed!" + e);
			respMessage.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			respMessage.setStatusCode("" + HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setResponse(respMessage);
		}
		return response;
	}

	@Override
	public ResponseEntity<Response<?>> getDocument(String versionId) {
		try {
			List<String> documentID = versionControlDao.getDocumentID(versionId);
			List<AttachmentDto> attachments = new ArrayList<>();
			for(String document: documentID)
			{
				attachments.add(DocumentServiceUtil.getOriginalDocument(document));
			}
			return ResponseEntity.ok().body(new Response<List<AttachmentDto>>(attachments));
		} 
		catch (Exception e) 
		{
			final Response<String> body = new Response<String>(e.getMessage());
			body.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			body.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
		}
	}

	@Override
	public ResponseEntity<Response<?>> getDocumentByID(String documentID) {
		try 
		{
			return ResponseEntity.ok().body(new Response<AttachmentDto>(DocumentServiceUtil.getOriginalDocument(documentID)));
		} 
		catch (Exception e) 
		{
			final Response<String> body = new Response<String>(e.getMessage());
			body.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			body.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
		}
	}

}
