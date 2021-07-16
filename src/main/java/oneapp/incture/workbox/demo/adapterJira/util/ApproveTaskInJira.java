package oneapp.incture.workbox.demo.adapterJira.util;

import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;


@Component
public class ApproveTaskInJira {
	RestUserJira restUserJira = new RestUserJira();

	public Object approveTask(String requestUrl, String payload) {

		RestResponse restResponse = null;
		System.err.println("[WBP-Dev]ApproveTaskInJira.approveTask()");
		if (!ServicesUtil.isEmpty(requestUrl)) {
			restResponse = restUserJira.callPostService(requestUrl, payload);
		}

		return restResponse.getHttpResponse();
	}

	public Object forwardTask(String requestUrl, String payload) {

		RestResponse restResponse = null;
		System.err.println("[WBP-Dev]ApproveTaskInJira.forwardTask()");
		if (!ServicesUtil.isEmpty(requestUrl)) {
			restResponse = restUserJira.callPutService(requestUrl, payload);
		}

		return restResponse.getHttpResponse();
	}

}
