package oneapp.incture.workbox.demo.workflow.services;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.workflow.dto.CrossConstantDto;

public class DropDownResposeDto {

	List<CrossConstantDto> crossConstants;
	List<String> valuesToRemove;
	ResponseMessage resposneMessage;
	
	@Override
	public String toString() {
		return "DropDownResposeDto [crossConstants=" + crossConstants + ", valuesToRemove=" + valuesToRemove
				+ ", resposneMessage=" + resposneMessage + "]";
	}
	public ResponseMessage getResposneMessage() {
		return resposneMessage;
	}
	public void setResposneMessage(ResponseMessage resposneMessage) {
		this.resposneMessage = resposneMessage;
	}
	public List<CrossConstantDto> getCrossConstants() {
		return crossConstants;
	}
	public void setCrossConstants(List<CrossConstantDto> crossConstants) {
		this.crossConstants = crossConstants;
	}
	public List<String> getValuesToRemove() {
		return valuesToRemove;
	}
	public void setValuesToRemove(List<String> valuesToRemove) {
		this.valuesToRemove = valuesToRemove;
	}
	
	
}
