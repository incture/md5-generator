package oneapp.incture.workbox.demo.dashboard.dto;


import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class DashboardResponseDto extends BaseDto {

	private DashboardDto graphDto;
	private ResponseMessage message;

	public DashboardDto getGraphDto() {
		return graphDto;
	}

	public void setGraphDto(DashboardDto graphDto) {
		this.graphDto = graphDto;
	}

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "GraphResponseDto [graphDto=" + graphDto + ", message=" + message + "]";
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}


}
