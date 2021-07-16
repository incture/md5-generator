package oneapp.incture.workbox.demo.scpadapter.services;

import com.sap.cloud.security.xsuaa.token.Token;
import com.sap.cloud.security.xsuaa.token.XsuaaToken;

import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDtoChild;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public interface ScpWorkflowActionFacadeLocal {

	public ResponseMessage taskAction(ActionDto dto, ActionDtoChild childDto, XsuaaToken token);
}
