package oneapp.incture.workbox.demo.notification.dto;

public class NotificationViewDetailRequestDto {
	String userId;
	String viewType;
	String viewName;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	@Override
	public String toString() {
		return "NotificationViewDetailRequestDto [userId=" + userId + ", viewType=" + viewType + ", viewName="
				+ viewName + "]";
	}

}
