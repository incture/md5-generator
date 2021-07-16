package oneapp.incture.workbox.demo.sapAriba.dto;

import java.util.List;

public class AwardDetails {

	String itemName;
	List<Awards> awards;
	@Override
	public String toString() {
		return "AwardDetails [itemName=" + itemName + ", awards=" + awards + "]";
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public List<Awards> getAwards() {
		return awards;
	}
	public void setAwards(List<Awards> awards) {
		this.awards = awards;
	}
	
	
}
