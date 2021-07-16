package oneapp.incture.workbox.demo.inbox.dto;

/**
 * 
 * @author Sreeparna.Kundu
 *
 */
public class SortingDto {
	private String orderBy;
	private String orderType;
	
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
	@Override
	public String toString() {
		return "SortingDto [orderBy=" + orderBy + ", orderType=" + orderType + "]";
	}
	
}
