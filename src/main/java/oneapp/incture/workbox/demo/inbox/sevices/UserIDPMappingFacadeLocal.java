package oneapp.incture.workbox.demo.inbox.sevices;

import java.util.List;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.inbox.dto.UserDetailResponse;
import oneapp.incture.workbox.demo.inbox.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.inbox.dto.UserIDPMappingResponseDto;

public interface UserIDPMappingFacadeLocal {

	public UserIDPMappingResponseDto getIDPUser();

	public ResponseMessage createIdpUsers();

	public ResponseMessage addWBUsers(List<UserIDPMappingDto> userIDPMappingDto);

	UserDetailResponse getUserDetail(Token token);

	public ResponseMessage updateUserDetail(UserIDPMappingDto userIDPMappingDto,Token token);
	
	public UserIDPMappingResponseDto getIDPUserBudget();

	public ResponseMessage updateIDPUserBudget(UserIDPMappingDto userIDPMappingDto);
	
	public ResponseMessage updateUsers(UserIDPMappingResponseDto userIDPMappingResponseDto);
	

}
