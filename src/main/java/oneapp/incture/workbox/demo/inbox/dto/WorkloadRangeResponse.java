package oneapp.incture.workbox.demo.inbox.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.WorkloadRangeDto;



public class WorkloadRangeResponse {

	private List<WorkloadRangeDto> workloadRangeDtos;
	private ResponseMessage responseMessage;

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	public List<WorkloadRangeDto> getWorkloadRangeDtos() {
		return workloadRangeDtos;
	}

	public void setWorkloadRangeDtos(List<WorkloadRangeDto> workloadRangeDtos) {
		this.workloadRangeDtos = workloadRangeDtos;
	}

	@Override
	public String toString() {
		return "WorkloadRangeResponse [workloadRangeDtos=" + workloadRangeDtos + "]";
	}

}
