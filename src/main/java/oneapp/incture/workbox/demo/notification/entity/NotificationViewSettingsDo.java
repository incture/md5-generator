package oneapp.incture.workbox.demo.notification.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "VIEW_SETTING")
public class NotificationViewSettingsDo implements BaseDo, Serializable {

	private static final long serialVersionUID = -338057976909649600L;

	@Id
	@Column(name = "VIEW_TYPE", columnDefinition = "VARCHAR(255)")
	private String viewType;

	@Id
	@Column(name = "USER_ID", columnDefinition = "VARCHAR(255)")
	private String userId;

	@Id
	@Column(name = "VIEW_NAME", columnDefinition = "VARCHAR(255)")
	private String viewName;

	@Column(name = "SETTINGS", columnDefinition = "VARCHAR(255)")
	private String settings;

	@Column(name = "VIEW_ICON", columnDefinition = "VARCHAR(255)")
	private String viewIcon;
	
	@Id
	@Column(name = "ID", columnDefinition = "VARCHAR(255)")
	private String id;
	
	@Column(name = "ADMIN_USER", columnDefinition = "VARCHAR(255)")
	private String adminUser;
	
	@Column(name = "CATEGORY", columnDefinition = "VARCHAR(255)")
	private String category;

	@Column(name = "DEFAULT")
	private Boolean isDefault;

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

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

	public String getViewIcon() {
		return viewIcon;
	}

	public void setViewIcon(String viewIcon) {
		this.viewIcon = viewIcon;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return "NotificationViewSettingsDo [viewType=" + viewType + ", userId=" + userId + ", viewName=" + viewName
				+ ", settings=" + settings + ", viewIcon=" + viewIcon + ", id=" + id + ", adminUser=" + adminUser
				+ ", category=" + category + ", isDefault=" + isDefault + "]";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
