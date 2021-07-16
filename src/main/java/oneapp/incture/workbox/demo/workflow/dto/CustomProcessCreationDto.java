package oneapp.incture.workbox.demo.workflow.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class CustomProcessCreationDto {

	ProcessConfigTbDto processDetail;
	List<CustomAttributeTemplateDto> customAttribute;
	List<TeamDetailDto> teamDetailDto;
	ResponseMessage responseMessage ;
	
	@Override
	public String toString() {
		return "CustomProcessCreationDto [processDetail=" + processDetail + ", customAttribute=" + customAttribute
				+ ", teamDetailDto=" + teamDetailDto + ", responseMessage=" + responseMessage + "]";
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public ProcessConfigTbDto getProcessDetail() {
		return processDetail;
	}
	public void setProcessDetail(ProcessConfigTbDto processDetail) {
		this.processDetail = processDetail;
	}
	public List<CustomAttributeTemplateDto> getCustomAttribute() {
		return customAttribute;
	}
	public void setCustomAttribute(List<CustomAttributeTemplateDto> customAttribute) {
		this.customAttribute = customAttribute;
	}
	public List<TeamDetailDto> getTeamDetailDto() {
		return teamDetailDto;
	}
	public void setTeamDetailDto(List<TeamDetailDto> teamDetailDto) {
		this.teamDetailDto = teamDetailDto;
	}
	
}
