package oneapp.incture.workbox.demo.notification.dto;

import java.util.List;

public class NotificationViewDetailResponseDto {

	private String viewType;
	private String viewName;
	private String settingId;
	List<NotificationEventDto> eventDtos;
	//private String validForUsage;
	

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getSettingId() {
		return settingId;
	}

	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}

	public List<NotificationEventDto> getEventDtos() {
		return eventDtos;
	}

	public void setEventDtos(List<NotificationEventDto> eventDtos) {
		this.eventDtos = eventDtos;
	}

	@Override
	public String toString() {
		return "NotificationViewDetailResponseDto [viewType=" + viewType + ", viewName=" + viewName + ", settingId="
				+ settingId + ", eventDtos=" + eventDtos + "]";
	}

}
