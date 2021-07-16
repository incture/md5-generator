package oneapp.incture.workbox.demo.dashboard.dto;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class GraphConfigDetail {

	GraphConfigurationDto graphConfigurationDo;
	ResponseMessage responseMessage;
	@Override
	public String toString() {
		return "GraphConfigDetail [graphConfigurationDo=" + graphConfigurationDo + ", responseMessage="
				+ responseMessage + "]";
	}
	public GraphConfigurationDto getGraphConfigurationDo() {
		return graphConfigurationDo;
	}
	public void setGraphConfigurationDo(GraphConfigurationDto graphConfigurationDo) {
		this.graphConfigurationDo = graphConfigurationDo;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	
}
