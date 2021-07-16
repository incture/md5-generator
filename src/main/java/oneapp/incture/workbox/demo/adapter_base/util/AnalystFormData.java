package oneapp.incture.workbox.demo.adapter_base.util;

import java.util.ArrayList;
import java.util.List;

public class AnalystFormData {

	List<AnalystForm> formData=new ArrayList<AnalystForm>();
	String formId;
	String formStatus;
	
	List<AnalystsLineItems> lineItems=new ArrayList<AnalystsLineItems>();
	
	
	
	public AnalystFormData(List<AnalystForm> formData, String formId, String formStatus,
			List<AnalystsLineItems> lineItems) {
		super();
		this.formData = formData;
		this.formId = formId;
		this.formStatus = formStatus;
		this.lineItems = lineItems;
	}


	public List<AnalystsLineItems> getLineItems() {
		return lineItems;
	}


	public void setLineItems(List<AnalystsLineItems> lineItems) {
		this.lineItems = lineItems;
	}


	public AnalystFormData() {
		super();
	}
	
	
	public AnalystFormData(List<AnalystForm> formData, String formId, String formStatus) {
		super();
		this.formData = formData;
		this.formId = formId;
		this.formStatus = formStatus;
	}


	public String getFormStatus() {
		return formStatus;
	}


	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}


	public List<AnalystForm> getFormData() {
		return formData;
	}
	public void setFormData(List<AnalystForm> formData) {
		this.formData = formData;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	@Override
	public String toString() {
		return "AnalystFormData [formData=" + formData + ", formId=" + formId + ", formStatus=" + formStatus
				+ ", lineItems=" + lineItems + "]";
	}
	

	
}
