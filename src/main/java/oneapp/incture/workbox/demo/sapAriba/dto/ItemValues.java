package oneapp.incture.workbox.demo.sapAriba.dto;

public class ItemValues {

	String itemName;
	String quantity;
	String initialPrize;
	String historicPrize;
	String competitiveTermFieldId;
	@Override
	public String toString() {
		return "ItemValues [itemName=" + itemName + ", quantity=" + quantity + ", initialPrize=" + initialPrize
				+ ", historicPrize=" + historicPrize + ", competitiveTermFieldId=" + competitiveTermFieldId + "]";
	}
	public String getCompetitiveTermFieldId() {
		return competitiveTermFieldId;
	}
	public void setCompetitiveTermFieldId(String competitiveTermFieldId) {
		this.competitiveTermFieldId = competitiveTermFieldId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getInitialPrize() {
		return initialPrize;
	}
	public void setInitialPrize(String initialPrize) {
		this.initialPrize = initialPrize;
	}
	public String getHistoricPrize() {
		return historicPrize;
	}
	public void setHistoricPrize(String historicPrize) {
		this.historicPrize = historicPrize;
	}
	
	
	
}
