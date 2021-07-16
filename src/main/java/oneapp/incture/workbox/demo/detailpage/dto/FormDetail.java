package oneapp.incture.workbox.demo.detailpage.dto;

public class FormDetail {

	String formId;
	String formStatus;
	@Override
	public String toString() {
		return "FormDetail [formId=" + formId + ", formStatus=" + formStatus + "]";
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getFormStatus() {
		return formStatus;
	}
	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}
	
	
	
}
