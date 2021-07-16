package oneapp.incture.workbox.demo.zohoExpense.service;

import java.util.Map;

import org.json.JSONObject;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.UserIDPMappingDto;


public interface ZohoExpenseServiceLocal {

	public ResponseMessage setAll();

	public Map<String, UserIDPMappingDto> fetchUsers(int i);

}
