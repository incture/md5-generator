package oneapp.incture.workbox.demo.inbox.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_CUSTOM_FILTER")
public class UserCustomFilter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1479463073275695023L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "FILTER_ID", nullable = false)
	private int filterId;

	@Column(name = "USER_ID", length = 50, nullable = false)
	private String userId;

	@Column(name = "FILTER_NAME", length = 255, nullable = false)
	private String filterName;

	@Column(name = "FILTER_DATA")
	private String filterData;

	@Column(name = "IS_VIEW")
	private boolean isView;

	@Column(name = "VIEW_NAME", length = 100)
	private String viewName;

	@Column(name = "IS_TILE")
	private boolean isTile;

	@Column(name = "FILTER_TYPE")
	private String filterType;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	@Column(name = "THRESHOLD")
	private String threshold;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "FILTER_SEQUENCE", nullable = false)
	private int sequence;
	
	@Column(name = "IS_TRAY")
	private boolean isTray;

	@Column(name = "INBOX_TYPE", length = 100)
	private String inboxType;
	

	public boolean isTray() {
		return isTray;
	}

	public void setTray(boolean isTray) {
		this.isTray = isTray;
	}

	public String getInboxType() {
		return inboxType;
	}

	public void setInboxType(String inboxType) {
		this.inboxType = inboxType;
	}

	public void setTile(boolean isTile) {
		this.isTile = isTile;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
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

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setView(boolean isView) {
		this.isView = isView;
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

	public int getFilterId() {
		return filterId;
	}

	public void setFilterId(int filterId) {
		this.filterId = filterId;
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

	@Override
	public String toString() {
		return "UserCustomFilter [filterId=" + filterId + ", userId=" + userId + ", filterName=" + filterName
				+ ", filterData=" + filterData + ", isView=" + isView + ", viewName=" + viewName + ", isTile=" + isTile
				+ ", filterType=" + filterType + ", isActive=" + isActive + ", threshold=" + threshold
				+ ", description=" + description + ", sequence=" + sequence + ", isTray=" + isTray + ", inboxType="
				+ inboxType + "]";
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
