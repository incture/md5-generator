package oneapp.incture.workbox.demo.adapter_base.util;

import java.util.List;

public class AnalystFilterLineItemAndForm {
	
	private AnalystsLineItems lineItem;
	private AnalystFormData formData;
	
	public AnalystsLineItems getLineItem() {
		return lineItem;
	}
	public void setLineItem(AnalystsLineItems lineItem) {
		this.lineItem = lineItem;
	}
	public AnalystFormData getFormData() {
		return formData;
	}
	public void setFormData(AnalystFormData formData) {
		this.formData = formData;
	}
	@Override
	public String toString() {
		return "AnalystFilterLineItemAndForm [lineItem=" + lineItem + ", formData=" + formData + "]";
	}
	
	
}
