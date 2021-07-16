package oneapp.incture.workbox.demo.zohoExpense.dto;

public class ExpenseDto {

	String date;
	String categoryName;
	String amount;
	@Override
	public String toString() {
		return "ExpenseDto [date=" + date + ", categoryName=" + categoryName + ", amount=" + amount + "]";
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
}
