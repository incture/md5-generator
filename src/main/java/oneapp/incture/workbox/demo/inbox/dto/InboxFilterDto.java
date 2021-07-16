package oneapp.incture.workbox.demo.inbox.dto;

import java.util.List;

/**
 * 
 * @author Sreeparna.Kundu
 *
 */
public class InboxFilterDto {
	private String inboxType;
	private String inboxName;
	private WorkboxRequestFilterDto advanceFilter;
	private QuickFilterDto quickFilter;
	private int page;
	private List<SortingDto> sortingDtos;
	// To apply filter on UserWorkItem count from the dashboard and User tasks
	// from the UserManagement tabs
	private List<String> userId;
	private boolean returnCountOnly;

	public boolean getReturnCountOnly() {
		return returnCountOnly;
	}

	public void setReturnCountOnly(boolean returnCountOnly) {
		this.returnCountOnly = returnCountOnly;
	}

	public List<String> getUserId() {
		return userId;
	}

	public void setUserId(List<String> userId) {
		this.userId = userId;
	}

	public QuickFilterDto getQuickFilter() {
		return quickFilter;
	}

	public void setQuickFilter(QuickFilterDto quickFilter) {
		this.quickFilter = quickFilter;
	}

	public String getInboxType() {
		return inboxType;
	}

	public void setInboxType(String inboxType) {
		this.inboxType = inboxType;
	}

	public String getInboxName() {
		return inboxName;
	}

	public void setInboxName(String inboxName) {
		this.inboxName = inboxName;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<SortingDto> getSortingDtos() {
		return sortingDtos;
	}

	public void setSortingDtos(List<SortingDto> sortingDtos) {
		this.sortingDtos = sortingDtos;
	}

	public WorkboxRequestFilterDto getAdvanceFilter() {
		return advanceFilter;
	}

	public void setAdvanceFilter(WorkboxRequestFilterDto advanceFilter) {
		this.advanceFilter = advanceFilter;
	}

	@Override
	public String toString() {
		return "InboxFilterDto [inboxType=" + inboxType + ", inboxName=" + inboxName + ", advanceFilter="
				+ advanceFilter + ", quickFilter=" + quickFilter + ", page=" + page + ", sortingDtos=" + sortingDtos
				+ ", userId=" + userId + ", returnCountOnly=" + returnCountOnly + "]";
	}

}
