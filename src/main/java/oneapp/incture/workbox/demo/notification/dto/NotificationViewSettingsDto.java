package oneapp.incture.workbox.demo.notification.dto;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class NotificationViewSettingsDto extends BaseDto {

	private String viewType; // what

	private String viewName; // Task

	private String settings; // It'ss setting Id

	private Boolean isDefault;
	
	private Boolean isEnable;

	private String viewIcon;

	private String events;

	private String userId;
	
	private String id;
	
	private String adminUser;
	
	private String category;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getViewIcon() {
		return viewIcon;
	}

	public void setViewIcon(String viewIcon) {
		this.viewIcon = viewIcon;
	}

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

	public String getSettings() {
		return settings;
	}

	public void setSettings(String settings) {
		this.settings = settings;
	}

	public String getEvents() {
		return events;
	}

	public void setEvents(String events) {
		this.events = events;
	}

	@Override
	public String toString() {
		return "NotificationViewSettingsDto [viewType=" + viewType + ", viewName=" + viewName + ", settings=" + settings
				+ ", isDefault=" + isDefault + ", isEnable=" + isEnable + ", viewIcon=" + viewIcon + ", events="
				+ events + ", userId=" + userId + ", id=" + id + ", adminUser=" + adminUser + ", category=" + category
				+ "]";
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
