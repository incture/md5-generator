package oneapp.incture.workbox.demo.successfactors.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.successfactors.util.SuccessFactorsUtil;

@Component
public class SuccessFactorActionFacade {

	@Autowired
	SuccessFactorsUtil sfUtil;
	
	public ResponseMessage completeSuccessFactorTask(String instance, String comment, String actionType,
			String subject, String userId, String processType) {
		ResponseMessage responseMessage = null;
		responseMessage = sfUtil.completeSuccessFactorTask(actionType, instance, subject, userId, processType, comment);
		return responseMessage;
	}
	
}
