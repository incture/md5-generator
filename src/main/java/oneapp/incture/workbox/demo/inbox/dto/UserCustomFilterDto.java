package oneapp.incture.workbox.demo.inbox.dto;

public class UserCustomFilterDto {

	private int filterId;

	public int getFilterId() {
		return filterId;
	}

	public void setFilterId(int filterId) {
		this.filterId = filterId;
	}

	public void setView(boolean isView) {
		this.isView = isView;
	}

	private String userId;
	private String filterName;
	private String filterData;
	private boolean isView;
	private String viewName;
	private boolean isTile;
	private boolean isActive;
	private String filterType;
	private String threshold;
	private String description;
	private int sequence;
	private boolean isTray;
	private String inboxType;

	public String getInboxType() {
		return inboxType;
	}

	public void setInboxType(String inboxType) {
		this.inboxType = inboxType;
	}

	public boolean isTray() {
		return isTray;
	}

	public void setTray(boolean isTray) {
		this.isTray = isTray;
	}

	public void setTile(boolean isTile) {
		this.isTile = isTile;
	}

	public int getSequence() {
		return sequence;
	}
	
	

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public boolean getIsTile() {
		return isTile;
	}

	public void setIsTile(boolean isTile) {
		this.isTile = isTile;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getFilterData() {
		return filterData;
	}

	public void setFilterData(String filterData) {
		this.filterData = filterData;
	}

	public boolean getIsView() {
		return isView;
	}

	public void setIsView(boolean isView) {
		this.isView = isView;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	@Override
	public String toString() {
		return "UserCustomFilterDto [filterId=" + filterId + ", userId=" + userId + ", filterName=" + filterName
				+ ", filterData=" + filterData + ", isView=" + isView + ", viewName=" + viewName + ", isTile=" + isTile
				+ ", isActive=" + isActive + ", filterType=" + filterType + ", threshold=" + threshold
				+ ", description=" + description + ", sequence=" + sequence + ", isTray=" + isTray + ", inboxType="
				+ inboxType + "]";
	}

}
