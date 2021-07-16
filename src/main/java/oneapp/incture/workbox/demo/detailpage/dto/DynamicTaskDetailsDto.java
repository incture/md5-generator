package oneapp.incture.workbox.demo.detailpage.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskAuditDto;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DynamicTaskDetailsDto {

	public List<HeaderDto> headerDto;
	public List<CustomDto> contentDto;
	public List<DynamicButtonDto> buttonsDto;
	public List<TaskAuditDto> activityDto;
//	public List<MessageDetailDto> conversationDto;
	public Integer pageCount;
	public Integer currentPage;
	public Integer totalChatCount;
	
	
	public Integer getTotalChatCount() {
		return totalChatCount;
	}
	public void setTotalChatCount(Integer totalChatCount) {
		this.totalChatCount = totalChatCount;
	}
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public List<TaskAuditDto> getActivityDto() {
		return activityDto;
	}
	public void setActivityDto(List<TaskAuditDto> activityDto) {
		this.activityDto = activityDto;
	}
	
	public List<HeaderDto> getHeaderDto() {
		return headerDto;
	}
	public void setHeaderDto(List<HeaderDto> headerDto) {
		this.headerDto = headerDto;
	}
	public List<CustomDto> getContentDto() {
		return contentDto;
	}
	public void setContentDto(List<CustomDto> contentDto) {
		this.contentDto = contentDto;
	}
	public List<DynamicButtonDto> getButtonsDto() {
		return buttonsDto;
	}
	public void setButtonsDto(List<DynamicButtonDto> buttonsDto) {
		this.buttonsDto = buttonsDto;
	}
	
	@Override
	public String toString() {
		return "DynamicTaskDetailsDto [headerDto=" + headerDto + ", contentDto=" + contentDto + ", buttonsDto="
				+ buttonsDto + ", activityDto=" + activityDto + ", pageCount=" + pageCount + ", currentPage="
				+ currentPage + ", totalChatCount=" + totalChatCount + "]";
	}
	
	
}
