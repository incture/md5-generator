package oneapp.incture.workbox.demo.emailTemplate.Dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;


public class VersionDetailsDto {
	private List<Integer> version;
	private ResponseMessage respomseMessage;
	@Override
	public String toString() {
		return "VersionDetailsDto [respomseMessage=" + respomseMessage + ", version=" + version + "]";
	}
	
	public ResponseMessage getRespomseMessage() {
		return respomseMessage;
	}
	public void setRespomseMessage(ResponseMessage respomseMessage) {
		this.respomseMessage = respomseMessage;
	}
	public List<Integer> getVersion() {
		return version;
	}
	public void setVersion(List<Integer> version) {
		this.version = version;
	}
	
	
	

}
