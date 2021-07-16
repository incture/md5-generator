package oneapp.incture.workbox.demo.substitution.dto;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement

public class SubstitutionRulesDto extends BaseDto {
	private Object validForUsage;
	private String ruleId;
	private int version;
	private String substitutedUser;
	private String substitutedUserName;
	private String substitutingUser;
	private String substitutingUserName;
	private String startDate;
	private String endDate;
	private boolean active;
	private boolean enabled;
	private String updateAt;
	private String createdBy;
	private String createdByDisp;
	private List<String> processList;
	private boolean disableButton;
	private String createdAt;
	private String modifiedAt;
	private List<ProcessStatusDto> processStatus;
	private boolean changeActive;
	private String onBehalfOf;
	
	
	
	
	
	public String getOnBehalfOf() {
		return onBehalfOf;
	}



	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}



	@Override
	public String toString() {
		return "SubstitutionRulesDto [ruleId=" + ruleId + ", version=" + version + ", substitutedUser="
				+ substitutedUser + ", substitutedUserName=" + substitutedUserName + ", substitutingUser="
				+ substitutingUser + ", substitutingUserName=" + substitutingUserName + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", isActive=" + active + ", isEnabled=" + enabled + ", updateAt="
				+ updateAt + ", createdBy=" + createdBy + ", createdByDisp=" + createdByDisp + ", processList="
				+ processList + ", disableButton=" + disableButton + ", createdAt=" + createdAt + ", modifiedAt="
				+ modifiedAt + ", processStatus=" + processStatus + ", isChangeActive=" + changeActive + "]";
	}

	

	public String getRuleId() {
		return ruleId;
	}



	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}



	public int getVersion() {
		return version;
	}



	public void setVersion(int version) {
		this.version = version;
	}



	public String getSubstitutedUser() {
		return substitutedUser;
	}



	public void setSubstitutedUser(String substitutedUser) {
		this.substitutedUser = substitutedUser;
	}



	public String getSubstitutedUserName() {
		return substitutedUserName;
	}



	public void setSubstitutedUserName(String substitutedUserName) {
		this.substitutedUserName = substitutedUserName;
	}



	public String getSubstitutingUser() {
		return substitutingUser;
	}



	public void setSubstitutingUser(String substitutingUser) {
		this.substitutingUser = substitutingUser;
	}



	public String getSubstitutingUserName() {
		return substitutingUserName;
	}



	public void setSubstitutingUserName(String substitutingUserName) {
		this.substitutingUserName = substitutingUserName;
	}



	public String getStartDate() {
		return startDate;
	}



	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}



	public String getEndDate() {
		return endDate;
	}



	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}



	public boolean isActive() {
		return active;
	}



	public void setActive(boolean active) {
		this.active = active;
	}



	public boolean isEnabled() {
		return enabled;
	}



	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}



	public String getUpdateAt() {
		return updateAt;
	}



	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}



	public String getCreatedBy() {
		return createdBy;
	}



	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}



	public String getCreatedByDisp() {
		return createdByDisp;
	}



	public void setCreatedByDisp(String createdByDisp) {
		this.createdByDisp = createdByDisp;
	}



	public List<String> getProcessList() {
		return processList;
	}



	public void setProcessList(List<String> processList) {
		this.processList = processList;
	}



	public boolean isDisableButton() {
		return disableButton;
	}



	public void setDisableButton(boolean disableButton) {
		this.disableButton = disableButton;
	}



	public String getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}



	public String getModifiedAt() {
		return modifiedAt;
	}



	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	}


	
	public List<ProcessStatusDto> getProcessStatus() {
		return processStatus;
	}



	public void setProcessStatus(List<ProcessStatusDto> processStatus) {
		this.processStatus = processStatus;
	}



	public boolean isChangeActive() {
		return changeActive;
	}



	public void setChangeActive(boolean changeActive) {
		this.changeActive = changeActive;
	}



	public void setValidForUsage(Object validForUsage) {
		this.validForUsage = validForUsage;
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
