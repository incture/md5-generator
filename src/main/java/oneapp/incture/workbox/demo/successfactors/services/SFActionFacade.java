package oneapp.incture.workbox.demo.successfactors.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.ActionDtoChild;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.successfactors.util.SuccessFactorsUtil;

@Component
public class SFActionFacade {

	@Autowired
	SuccessFactorsUtil sfUtil;
	
	public ResponseMessage completeSuccessFactorTask(ActionDtoChild childDto,
			String comment, String actionType,
			String subject) {
		ResponseMessage responseMessage = null;
		String instance = childDto.getInstanceId();
		responseMessage = sfUtil.completeSuccessFactorTask(actionType, instance,
				subject,null,null,null);

		return responseMessage;
	}
	
	public ResponseMessage completeSFTask(ActionDtoChild childDto, String
			comment, String actionType, String subject) {
		ResponseMessage responseMessage = null;
		String instance = childDto.getInstanceId();
		responseMessage = sfUtil.completeSfTask(actionType, instance, subject);

		return responseMessage;
	}
}
