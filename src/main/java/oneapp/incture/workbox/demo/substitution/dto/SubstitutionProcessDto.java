package oneapp.incture.workbox.demo.substitution.dto;


import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;


@XmlRootElement
public class SubstitutionProcessDto extends BaseDto {

	public SubstitutionProcessDto() {
		super();
	}
	
	public SubstitutionProcessDto(String ruleId, String process) {
		super();
		this.ruleId = ruleId;
		this.process = process;
	}
	
	public SubstitutionProcessDto(String ruleId, String process, int version, int status , String statusDef ,String approvedAt) {
		super();
		this.ruleId = ruleId;
		this.process = process;
		this.version = version;
		this.status = status;
		this.statusDef = statusDef;
		this.approvedAt = approvedAt;
	}
	private String ruleId;
	private String process;
	private int version;
	private int status;
	private String statusDef;
	private String approvedAt;
	
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	
	
	
	public String getApprovedAt() {
		return approvedAt;
	}

	public void setApprovedAt(String approvedAt) {
		this.approvedAt = approvedAt;
	}

	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusDef() {
		return statusDef;
	}
	public void setStatusDef(String statusDef) {
		this.statusDef = statusDef;
	}
	
	@Override
	public String toString() {
		return "SubstitutionProcessDto [ruleId=" + ruleId + ", process=" + process + ", version=" + version
				+ ", status=" + status + ", statusDef=" + statusDef + "]";
	}
	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}
	
	
}
