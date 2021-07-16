package oneapp.incture.workbox.demo.adhocTask.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PpdDto {

	@Expose
	@SerializedName("CostCenter")
	private String costCenter;
	
	@Expose
	@SerializedName("PPD_ID")
	private String ppdId;
	
	@Expose
	@SerializedName("Budget")
	private String budget;
	
	@Expose
	@SerializedName("AvailBudget")
	private String availBudget;
	
	@Expose
	@SerializedName("Requestor")
	private String requestor;

	@Expose
	@SerializedName("Deadline")
	private String deadline;
	
	@Expose
	@SerializedName("Comments")
	private String comments;
	
	@Expose
	@SerializedName("Attachments")
	private String attachments;
	
	@Expose
	@SerializedName("PrjctClosureDate")
	private String prjctClosureDate;
	
	@Expose
	@SerializedName("APPROVER")
	private String approver;

	@Override
	public String toString() {
		return "PpdDto [costCenter=" + costCenter + ", ppdId=" + ppdId + ", budget=" + budget + ", availBudget="
				+ availBudget + ", requestor=" + requestor + ", deadline=" + deadline + ", comments=" + comments
				+ ", attachments=" + attachments + ", prjctClosureDate=" + prjctClosureDate + ", approver=" + approver
				+ "]";
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getPpdId() {
		return ppdId;
	}

	public void setPpdId(String ppdId) {
		this.ppdId = ppdId;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getAvailBudget() {
		return availBudget;
	}

	public void setAvailBudget(String availBudget) {
		this.availBudget = availBudget;
	}

	public String getRequestor() {
		return requestor;
	}

	public void setRequestor(String requestor) {
		this.requestor = requestor;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	public String getPrjctClosureDate() {
		return prjctClosureDate;
	}

	public void setPrjctClosureDate(String prjctClosureDate) {
		this.prjctClosureDate = prjctClosureDate;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	
	
}
