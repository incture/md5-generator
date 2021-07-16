package oneapp.incture.workbox.demo.sapAriba.dto;

public class Awards {

	String supplierName;
	String price;
	String quantity;
	String extendedPrize;
	String savings;
	String allocation;
	@Override
	public String toString() {
		return "Awards [supplierName=" + supplierName + ", price=" + price + ", quantity=" + quantity
				+ ", extendedPrize=" + extendedPrize + ", savings=" + savings + ", allocation=" + allocation + "]";
	}
	public String getAllocation() {
		return allocation;
	}
	public void setAllocation(String allocation) {
		this.allocation = allocation;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
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
