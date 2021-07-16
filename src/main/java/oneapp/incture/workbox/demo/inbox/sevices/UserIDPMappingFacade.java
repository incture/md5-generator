package oneapp.incture.workbox.demo.inbox.sevices;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_base.util.UserManagementUtil;
import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.document.dto.DocumentResponseDto;
import oneapp.incture.workbox.demo.document.service.DocumentService;
import oneapp.incture.workbox.demo.inbox.dao.UserIDPMappingDao;
import oneapp.incture.workbox.demo.inbox.dto.UserDetailResponse;
import oneapp.incture.workbox.demo.inbox.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.inbox.dto.UserIDPMappingResponseDto;
import oneapp.incture.workbox.demo.scpadapter.util.DestinationUtil;
import oneapp.incture.workbox.demo.sharepointFileUpload.SharepointUploadFile;

import com.sap.cloud.security.xsuaa.token.Token;
import com.sap.security.um.user.User;

@Service("UserIDPMappingFacade")
//////@Transactional
public class UserIDPMappingFacade implements UserIDPMappingFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(UserIDPMappingFacade.class);

	@Autowired
	private UserIDPMappingDao idpUserDao;

	ResponseMessage responseDto;

	UserIDPMappingResponseDto userDto;

	@Autowired
	private DocumentService documentService;
	
	@Autowired
	SharepointUploadFile sharepointUploadFile;

	@Override
	public UserIDPMappingResponseDto getIDPUser() {
		UserIDPMappingResponseDto userDto = new UserIDPMappingResponseDto();
		responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		// try {
		List<UserIDPMappingDto> dtoList = idpUserDao.getAllUser();

		if (!ServicesUtil.isEmpty(dtoList)) {
			userDto.setDto(dtoList);
			responseDto.setMessage("Data fetched Successfully");
		} else {
			responseDto.setMessage(PMCConstant.NO_RESULT);
		}
		responseDto.setStatus("SUCCESS");
		responseDto.setStatusCode("0");
		// } catch (Exception e) {
		// logger.error("[PMC][UserIDPMappingFacade][getIDPUser][error]" +
		// e.getMessage());
		// responseDto.setMessage("Fetching data failed due to " +
		// e.getMessage());
		// }
		userDto.setMessage(responseDto);
		return userDto;
	}

	@Override
	public ResponseMessage createIdpUsers() {
		responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);

		String userNotCreated = "";
		String jsonString = DestinationUtil.executeWithDest("idpdestination", "scim/Users", "GET",
				"application/scim+json", null, null, null, false, null, null);

		JSONObject jsonObject = new JSONObject(jsonString);
		JSONArray resources = jsonObject.getJSONArray("Resources");
		JSONObject resource;
		UserIDPMappingDto mappingDto = new UserIDPMappingDto();
		List<UserIDPMappingDto> userDto = idpUserDao.getAllUser();
		List<String> existingUser = null;
		List<String> newUsers = new ArrayList<String>();
		System.err.println("[WBP-Dev][Workbox][createIdpUsers][existingUserDto]" + userDto);
		if (!ServicesUtil.isEmpty(userDto)) {
			existingUser = new ArrayList<String>();
			for (UserIDPMappingDto userIDPMappingDto : userDto) {
				existingUser.add(userIDPMappingDto.getUserLoginName());
			}
			System.err.println("[WBP-Dev][Workbox][createIdpUsers][existingUser]" + existingUser);

		}
		for (Object obj : resources) {
			resource = (JSONObject) obj;

			try {
				mappingDto.setUserId(resource.getString("id"));
				mappingDto.setUserFirstName(resource.getJSONObject("name").getString("givenName"));
				mappingDto.setUserLastName(resource.getJSONObject("name").getString("familyName"));
				mappingDto.setUserEmail(resource.getJSONArray("emails").getJSONObject(0).getString("value"));
				boolean loginId = resource.has("userName");
				String loginName = "";
				if (loginId)
					loginName = resource.getString("userName");
				else
					loginName = resource.getString("id");

				mappingDto.setUserLoginName(loginName);
				newUsers.add(loginName);
				idpUserDao.createIDPUser(mappingDto).equals(PMCConstant.SUCCESS);

			} catch (Exception e) {
				userNotCreated += resource.getString("id") + " , ";
				logger.error("[PMC][UserIDPMappingFacade][getIDPUser][error]" + e.getMessage());
			}

		}
		if (existingUser !=  null && !ServicesUtil.isEmpty(newUsers)) {
			existingUser.removeAll(newUsers);
			System.err.println("[WBP-Dev][WORKBOX][createUser][existingUser]" + existingUser);
			if (!ServicesUtil.isEmpty(existingUser)) {
				for (String str : existingUser) {
					UserIDPMappingDto dto = new UserIDPMappingDto();
					dto.setUserLoginName(str);
					try {
						idpUserDao.delete(dto);
					} catch (Exception e) {
						System.err.println("[WBP-Dev][PMC][UserIDPMappingFacade][createUser][error]" + e.getMessage());
					}
				}
			}
		}
		if (ServicesUtil.isEmpty(userNotCreated))
			responseDto.setMessage("IDP User " + PMCConstant.CREATED_SUCCESS);
		else
			responseDto
					.setMessage("IDP User " + PMCConstant.CREATED_SUCCESS + ", Users not created: " + userNotCreated);

		responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
		responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
		return responseDto;
	}

	@Override
	public UserDetailResponse getUserDetail(Token token) {
		responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		responseDto.setMessage(PMCConstant.STATUS_FAILURE);
		UserIDPMappingDto userIDPMappingDto = null;
		UserDetailResponse userDetailResponse = new UserDetailResponse();
		userDetailResponse.setResponseMessage(responseDto);
		try {
//			User user = UserManagementUtil.getLoggedInUser();
			System.err.println("[WBP-Dev][WBProduct-Dev]user : " + token.getLogonName());
			userIDPMappingDto = idpUserDao.getUserDetail(token.getLogonName());

			if (!ServicesUtil.isEmpty(userIDPMappingDto.getCompressedImage())) {
				AttachmentRequestDto attachmentRequestDto = documentService
						.getOriginalAttachment(userIDPMappingDto.getCompressedImage());
				userIDPMappingDto.setCompressedImage(attachmentRequestDto.getEncodedFileContent());
				userIDPMappingDto.setImageType(attachmentRequestDto.getFileType());
			}
			userDetailResponse.setUserIDPMappingDto(userIDPMappingDto);
			responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
			responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			responseDto.setMessage(PMCConstant.STATUS_SUCCESS);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW] USER DETAIL SERVICE ERROR" + e);
		}

		return userDetailResponse;
	}

	@Override
	public ResponseMessage updateUserDetail(UserIDPMappingDto userIDPMappingDto,Token token) {
		Integer res = null;
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		responseDto.setMessage(PMCConstant.STATUS_FAILURE);
		try {
			System.err.println("[WBP-Dev][WORKBOX_PRODUCT]UPDATE USER 1");
			if (ServicesUtil.isEmpty(userIDPMappingDto.getUserId())) {
//				User user = UserManagementUtil.getLoggedInUser();
//				System.err.println("[WBP-Dev][WBProduct-Dev]user : " + user.toString());
				userIDPMappingDto.setUserId(token.getLogonName());
			}
			res = idpUserDao.updateUserDetail(userIDPMappingDto);
			if (!ServicesUtil.isEmpty(userIDPMappingDto.getProfilePic()))
				idpUserDao.updateoriginalImage(userIDPMappingDto);
			System.err.println("[WBP-Dev][WORKBOX_PRODUCT]UPDATE USER5");
			if (res > 0) {
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
				responseDto.setMessage(PMCConstant.STATUS_SUCCESS);
			}
			System.err.println("[WBP-Dev][WORKBOX_PRODUCT]UPDATE USER6");
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW] USER DETAIL SERVICE ERROR" + e);
		}

		return responseDto;
	}

	@Override
	public ResponseMessage addWBUsers(List<UserIDPMappingDto> wBUserDtos) {

		responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);

		String userNotCreated = "FAILURE";

		for (UserIDPMappingDto wBUserDto : wBUserDtos) {

			try {
				List<AttachmentRequestDto> attachmentList = new ArrayList<>();
				AttachmentRequestDto attachmentRequestDto = new AttachmentRequestDto();
				attachmentRequestDto.setEncodedFileContent(wBUserDto.getCompressedImage());
				attachmentRequestDto.setFileName(wBUserDto.getUserFirstName() + wBUserDto.getUserFirstName());
				attachmentRequestDto.setFileType(wBUserDto.getImageType());
				attachmentList.add(attachmentRequestDto);
				DocumentResponseDto dto = sharepointUploadFile.uploadFile(attachmentList,attachmentRequestDto.getFileName());
				if ((PMCConstant.SUCCESS).equals(dto.getResponse().getStatus())) {
					wBUserDto.setCompressedImage(dto.getAttachmentIds().get(0).getAttachmentId());
					wBUserDto.setImageType(dto.getAttachmentIds().get(0).getAttachmentType());

				}
				userNotCreated = idpUserDao.createIDPUser(wBUserDto);

			} catch (Exception e) {
				logger.error("[PMC][UserIDPMappingFacade][getIDPUser][error]" + e.getMessage());
			}

		}

		if (PMCConstant.SUCCESS.equalsIgnoreCase(userNotCreated))
			responseDto.setMessage("IDP User " + PMCConstant.CREATED_SUCCESS);
		else
			responseDto.setMessage("IDP User " + PMCConstant.FAILURE + ", Unable to add user : " + userNotCreated);

		responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
		responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
		return responseDto;

	}
	
	@Override
	public UserIDPMappingResponseDto getIDPUserBudget() {
		UserIDPMappingResponseDto userDto = new UserIDPMappingResponseDto();
		responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		List<UserIDPMappingDto> dtoList = idpUserDao.getAllUserBudget();

		if (!ServicesUtil.isEmpty(dtoList)) {
			userDto.setDto(dtoList);
			responseDto.setMessage("Data fetched Successfully");
		} else {
			responseDto.setMessage(PMCConstant.NO_RESULT);
		}
		responseDto.setStatus("SUCCESS");
		responseDto.setStatusCode("0");
		userDto.setMessage(responseDto);
		return userDto;
	}

	@Override
	public ResponseMessage updateIDPUserBudget(UserIDPMappingDto userIDPMappingDto) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		responseDto.setMessage(PMCConstant.STATUS_FAILURE);
		try {
			idpUserDao.updateIDPUserBudget(userIDPMappingDto);
			responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
			responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			responseDto.setMessage(PMCConstant.STATUS_SUCCESS);
			System.err.println("[WBP-Dev][WORKBOX_PRODUCT]UPDATE USER6");
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW] USER DETAIL SERVICE ERROR" + e);
		}

		return responseDto;
	}
	
	@Override
    public ResponseMessage updateUsers(UserIDPMappingResponseDto userIDPMappingResponseDto) {
        ResponseMessage resp = new ResponseMessage();
        resp.setMessage(PMCConstant.FAILURE);
        resp.setStatus(PMCConstant.FAILURE);
        resp.setStatusCode(PMCConstant.CODE_FAILURE);
        try {
            if(!ServicesUtil.isEmpty(userIDPMappingResponseDto.getDto())) {
                String res = idpUserDao.saveOrUpdateUsers(userIDPMappingResponseDto.getDto());
                if(PMCConstant.SUCCESS.equalsIgnoreCase(res)) {
                    resp.setMessage(PMCConstant.SUCCESS);
                    resp.setStatus(PMCConstant.SUCCESS);
                    resp.setStatusCode(PMCConstant.CODE_SUCCESS);
                    return resp;
                }
            }
            resp.setMessage(PMCConstant.SUCCESS);
            resp.setStatus(PMCConstant.SUCCESS);
            resp.setStatusCode(PMCConstant.CODE_SUCCESS);
        }catch (Exception e) {
            System.err.println("UserIDPMappingFacade.updateUsers() error"+e);
        }
        return resp;
    }
}
