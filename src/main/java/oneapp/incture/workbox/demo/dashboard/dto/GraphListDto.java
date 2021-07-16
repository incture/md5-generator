package oneapp.incture.workbox.demo.dashboard.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;


public class GraphListDto {

	List<GraphDetail> graphDetails;
	ResponseMessage responseMessage;
	@Override
	public String toString() {
		return "GraphListDto [graphDetails=" + graphDetails + ", responseMessage=" + responseMessage + "]";
	}
	public List<GraphDetail> getGraphDetails() {
		return graphDetails;
	}
	public void setGraphDetails(List<GraphDetail> graphDetails) {
		this.graphDetails = graphDetails;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	
}
