package oneapp.incture.workbox.demo.inbox.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ActionRequestContextDto {

	private String approved;
	private String apprComments;
	
	
	public ActionRequestContextDto(String approved, String apprComments) {
		super();
		this.approved = approved;
		this.apprComments = apprComments;
	}
	public String getApproved() {
		return approved;
	}
	public void setApproved(String approved) {
		this.approved = approved;
	}
	public String getApprComments() {
		return apprComments;
	}
	public void setApprComments(String apprComments) {
		this.apprComments = apprComments;
	}
	@Override
	public String toString() {
		return "ActionRequestContextDto [approved=" + approved + ", apprComments=" + apprComments + ", getApproved()="
				+ getApproved() + ", getApprComments()=" + getApprComments() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
}
