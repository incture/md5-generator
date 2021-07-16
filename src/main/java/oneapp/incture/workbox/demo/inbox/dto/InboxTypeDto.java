package oneapp.incture.workbox.demo.inbox.dto;

import java.util.List;

public class InboxTypeDto {

	private String name;
	private String type;
	private String count;
	private String inboxId;
	private String viewPayload;

	public String getViewPayload() {
		return viewPayload;
	}

	public void setViewPayload(String viewPayload) {
		this.viewPayload = viewPayload;
	}

	public String getInboxId() {
		return inboxId;
	}

	public void setInboxId(String inboxId) {
		this.inboxId = inboxId;
	}

	private String icon;
	List<InboxTypeDto> dtoList;
	private String parentId;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<InboxTypeDto> getDtoList() {
		return dtoList;
	}

	public void setDtoList(List<InboxTypeDto> dtoList) {
		this.dtoList = dtoList;
	}

	@Override
	public String toString() {
		return "InboxTypeDto [name=" + name + ", type=" + type + ", count=" + count + ", inboxId=" + inboxId
				+ ", viewPayload=" + viewPayload + ", icon=" + icon + ", dtoList=" + dtoList + ", parentId=" + parentId
				+ "]";
	}

}
