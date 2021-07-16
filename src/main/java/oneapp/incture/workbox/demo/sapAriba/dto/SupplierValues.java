package oneapp.incture.workbox.demo.sapAriba.dto;

public class SupplierValues {

	String supplierId;
	String prize;
	String quantity;
	String extendedPrize;
	String savings;
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	@Override
	public String toString() {
		return "SupplierValues [supplierId=" + supplierId + ", prize=" + prize + ", quantity=" + quantity
				+ ", extendedPrize=" + extendedPrize + ", savings=" + savings + "]";
	}
	public String getPrize() {
		return prize;
	}
	public void setPrize(String prize) {
		this.prize = prize;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getExtendedPrize() {
		return extendedPrize;
	}
	public void setExtendedPrize(String extendedPrize) {
		this.extendedPrize = extendedPrize;
	}
	public String getSavings() {
		return savings;
	}
	public void setSavings(String savings) {
		this.savings = savings;
	}
	
	
}
