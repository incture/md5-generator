package oneapp.incture.workbox.demo.baseAdapter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.inbox.dto.UserDetailResponse;
import oneapp.incture.workbox.demo.inbox.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.inbox.dto.UserIDPMappingResponseDto;
import oneapp.incture.workbox.demo.inbox.sevices.UserIDPMappingFacadeLocal;

@RestController
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/workbox/idpMapping", produces = "application/json")
public class UserIDPMappingRest {

	@Autowired
	private UserIDPMappingFacadeLocal userIDPMappingLocal;

	@RequestMapping(value = "/getUsers", method = RequestMethod.GET)
	public UserIDPMappingResponseDto getIdpUsers() {
		return userIDPMappingLocal.getIDPUser();
	}

	@RequestMapping(value = "/getUsersDetails", method = RequestMethod.GET)
	public UserDetailResponse getUsersDetails(@AuthenticationPrincipal Token token) {
		return userIDPMappingLocal.getUserDetail(token);
	}

	@RequestMapping(value = "/updateUserDetail", method = RequestMethod.POST)
	public ResponseMessage updateUserDetail(@RequestBody UserIDPMappingDto userIDPMappingDto,@AuthenticationPrincipal Token token) {
		ResponseMessage res = userIDPMappingLocal.updateUserDetail(userIDPMappingDto,token);
		System.err.println("[WBP-Dev][WORKBOX_PRODUCT]UPDATE USER7");
		return res;
	}

	@RequestMapping(value = "/addWBUsers", method = RequestMethod.POST)
	public ResponseMessage addUsers(@RequestBody List<UserIDPMappingDto> userIDPMappingDto) {
		return userIDPMappingLocal.addWBUsers(userIDPMappingDto);
	}
	
	@RequestMapping(value = "/getUsersBudget", method = RequestMethod.GET)
	public UserIDPMappingResponseDto getIdpUsersBudget() {
		return userIDPMappingLocal.getIDPUserBudget();
	}
	
	@RequestMapping(value = "/updateUsersBudget", method = RequestMethod.POST)
	public ResponseMessage updateIDPUserBudget(@RequestBody UserIDPMappingDto userIDPMappingDto) {
		ResponseMessage res = userIDPMappingLocal.updateIDPUserBudget(userIDPMappingDto);
		System.err.println("[WBP-Dev][WORKBOX_PRODUCT]UPDATE USER7");
		return res;
	}
	
	@RequestMapping(value = "/updateUsers", method = RequestMethod.POST)
    public ResponseMessage updateUsers(@RequestBody UserIDPMappingResponseDto userIDPMappingResponseDto) {
        return userIDPMappingLocal.updateUsers(userIDPMappingResponseDto);
    }

}
