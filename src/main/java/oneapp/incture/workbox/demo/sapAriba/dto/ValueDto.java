package oneapp.incture.workbox.demo.sapAriba.dto;

import java.util.List;

public class ValueDto {

	String itemName;
	String leadParticipant;
	String quantity;
	String initial;
	String historic;
	List<String> prices;
	String leading;
	String savings;
	@Override
	public String toString() {
		return "ValueDto [itemName=" + itemName + ", leadParticipant=" + leadParticipant + ", quantity=" + quantity
				+ ", initial=" + initial + ", historic=" + historic + ", prices=" + prices + ", leading=" + leading
				+ ", savings=" + savings + "]";
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getLeadParticipant() {
		return leadParticipant;
	}
	public void setLeadParticipant(String leadParticipant) {
		this.leadParticipant = leadParticipant;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getInitial() {
		return initial;
	}
	public void setInitial(String initial) {
		this.initial = initial;
	}
	public String getHistoric() {
		return historic;
	}
	public void setHistoric(String historic) {
		this.historic = historic;
	}
	public List<String> getPrices() {
		return prices;
	}
	public void setPrices(List<String> prices) {
		this.prices = prices;
	}
	public String getLeading() {
		return leading;
	}
	public void setLeading(String leading) {
		this.leading = leading;
	}
	public String getSavings() {
		return savings;
	}
	public void setSavings(String savings) {
		this.savings = savings;
	}
	
	
}
