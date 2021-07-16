package oneapp.incture.workbox.demo.adapter_salesforce.dto;

import java.util.List;

public class ApproveRequestDto {

	List<RequestDto> requests;

	@Override
	public String toString() {
		return "ApproveRequestDto [requests=" + requests + "]";
	}

	public List<RequestDto> getRequests() {
		return requests;
	}

	public void setRequests(List<RequestDto> requests) {
		this.requests = requests;
	}

	
	
	
}

