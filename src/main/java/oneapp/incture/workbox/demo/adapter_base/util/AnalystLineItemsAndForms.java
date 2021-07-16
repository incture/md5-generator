package oneapp.incture.workbox.demo.adapter_base.util;

import java.util.ArrayList;
import java.util.List;

public class AnalystLineItemsAndForms {

	private List<AnalystsLineItems> lineItems=new ArrayList<AnalystsLineItems>();
	private List<AnalystFormData> forms=new ArrayList<AnalystFormData>();
	
	String error;
	
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public List<AnalystsLineItems> getLineItems() {
		return lineItems;
	}
	public void setLineItems(List<AnalystsLineItems> lineItems) {
		this.lineItems = lineItems;
	}
	public List<AnalystFormData> getForms() {
		return forms;
	}
	public void setForms(List<AnalystFormData> forms) {
		this.forms = forms;
	}
	@Override
	public String toString() {
		return "AnalystLineItemsAndForms [lineItems=" + lineItems + ", forms=" + forms + ", error=" + error + "]";
	}
	
	
}
