package oneapp.incture.workbox.demo.adapter.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDtoChild;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.zohoExpense.service.ZohoExpenseActionFacade;
import oneapp.incture.workbox.demo.zohoExpense.service.ZohoExpenseServiceLocal;
import oneapp.incture.workbox.demo.zohoExpense.util.ZohoExpenseConstants;



@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/zohoExpense")
public class ZohoExpenseAdapterController {

	@Autowired
	ZohoExpenseServiceLocal zohoExpenseServiceLocal;

	@Autowired
	ZohoExpenseActionFacade zohoActionFacade;

	@RequestMapping(value = "/addData", method = RequestMethod.GET)
	public ResponseMessage addData() {
		String res = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ZohoExpenseConstants.FAILURE);
		resp.setStatus(ZohoExpenseConstants.STATUS_CODE_FAILURE);
		resp.setStatusCode(ZohoExpenseConstants.STATUS_CODE_FAILURE);
		try {

			res = zohoExpenseServiceLocal.setAll().getStatus();
			if (res.equalsIgnoreCase(ZohoExpenseConstants.SUCCESS)) {
				resp.setMessage(ZohoExpenseConstants.SUCCESS);
				resp.setStatus(ZohoExpenseConstants.STATUS_CODE_SUCCESS);
				resp.setStatusCode(ZohoExpenseConstants.STATUS_CODE_SUCCESS);
			}
		} catch (Exception e) {

			System.err.println("[WBP-Dev]ZohoExpenseController.addData() error : " + e);
		}

		return resp;

	}

	@RequestMapping(value = "/actions", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage taskActions(@RequestBody ActionDto dto) {
		ResponseMessage resp = null;
		System.err.println("[WBP-Dev][WORKBOX][WorkboxRest][action][dto]" + dto.toString());
		for (ActionDtoChild childDto : dto.getTask()) {
			if (!ServicesUtil.isEmpty(childDto.getActionType())) {
				if (childDto.getActionType().equalsIgnoreCase("Approve")) {
					resp = zohoActionFacade.acceptOrRejectRequest(dto, childDto, "approve");
				} else if (childDto.getActionType().equalsIgnoreCase("Approve")) {
					resp = zohoActionFacade.acceptOrRejectRequest(dto, childDto, "reject");
				} else {
					return new ResponseMessage(PMCConstant.FAILURE, "", "Action Type not supported");
				}

			}
		}

		return resp;

	}

	@RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
	public Map<String, UserIDPMappingDto> getAllUsers() {
		Map<String, UserIDPMappingDto> userDetails = zohoExpenseServiceLocal.fetchUsers(0);
		return userDetails;
	}
}
