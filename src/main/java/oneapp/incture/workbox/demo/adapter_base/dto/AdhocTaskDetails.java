package oneapp.incture.workbox.demo.adapter_base.dto;

public class AdhocTaskDetails {
	
	String eventIds;
	String orderBy;
	String ownerTypeSeq;
	
	@Override
	public String toString() {
		return "AdhocTaskDetails [eventIds=" + eventIds + ", orderBy=" + orderBy + ", ownerTypeSeq=" + ownerTypeSeq
				+ "]";
	}
	public String getEventIds() {
		return eventIds;
	}
	public void setEventIds(String eventIds) {
		this.eventIds = eventIds;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getOwnerTypeSeq() {
		return ownerTypeSeq;
	}
	public void setOwnerTypeSeq(String ownerTypeSeq) {
		this.ownerTypeSeq = ownerTypeSeq;
	}
	
	
	
	
}
