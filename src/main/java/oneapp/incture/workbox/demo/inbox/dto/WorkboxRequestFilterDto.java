package oneapp.incture.workbox.demo.inbox.dto;

import java.util.HashMap;
import java.util.Map;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;

public class WorkboxRequestFilterDto {

	private int skipCount;
	private int maxCount;
	private int page;
	private String orderBy;
	private String orderType;
	private TaskOwnersDto currentUserInfo;

	private Boolean isAdmin = false;
	private String inboxType;

	private String advanceSearch;

	Map<String, AdvanceFilterDetailDto> filterMap = new HashMap<String, AdvanceFilterDetailDto>();

	public Map<String, AdvanceFilterDetailDto> getFilterMap() {
		return filterMap;
	}

	public void setFilterMap(Map<String, AdvanceFilterDetailDto> filterMap) {
		this.filterMap = filterMap;
	}

	public String getInboxType() {
		return inboxType;
	}

	public void setInboxType(String inboxType) {
		this.inboxType = inboxType;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public int getSkipCount() {
		return skipCount;
	}

	public void setSkipCount(int skipCount) {
		this.skipCount = skipCount;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public TaskOwnersDto getCurrentUserInfo() {
		return currentUserInfo;
	}

	public void setCurrentUserInfo(TaskOwnersDto currentUserInfo) {
		this.currentUserInfo = currentUserInfo;
	}

	public Boolean isAdmin() {
		return isAdmin;
	}

	public String getAdvanceSearch() {
		return advanceSearch;
	}

	public void setAdvanceSearch(String advanceSearch) {
		this.advanceSearch = advanceSearch;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return "WorkboxRequestFilterDto [skipCount=" + skipCount + ", maxCount=" + maxCount + ", page=" + page
				+ ", orderBy=" + orderBy + ", orderType=" + orderType + ", currentUserInfo=" + currentUserInfo
				+ ", isAdmin=" + isAdmin + ", inboxType=" + inboxType + ", advanceSearch=" + advanceSearch
				+ ", filterMap=" + filterMap + "]";
	}

}
