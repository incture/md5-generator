package oneapp.incture.workbox.demo.workflow.dto;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class ProcessConfigTbDto extends BaseDto{

	private String processName;
	private String processDisplayName;
	private String processType;
	private Integer slaDays;
	private Integer slaHours;
	private String color;
	private String description;
	private String labelName;
	private String origin;
	private String processRequestId;
	private String sla;
	private String subject;
	
	private String criticalDate;
	private Integer criticalDateDays;
	private Integer criticalDateHours;
	private String url;
	private Boolean validForUsage;
	

	public void setValidForUsage(Boolean validForUsage) {
		this.validForUsage = validForUsage;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getProcessRequestId() {
		return processRequestId;
	}
	public void setProcessRequestId(String processRequestId) {
		this.processRequestId = processRequestId;
	}
	public String getSla() {
		return sla;
	}
	public void setSla(String sla) {
		this.sla = sla;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	
	@Override
	public String toString() {
		return "ProcessConfigTbDto [processName=" + processName + ", processDisplayName=" + processDisplayName
				+ ", processType=" + processType + ", slaDays=" + slaDays + ", slaHours=" + slaHours + ", color="
				+ color + ", description=" + description + ", labelName=" + labelName + ", origin=" + origin
				+ ", processRequestId=" + processRequestId + ", sla=" + sla + ", subject=" + subject + ", criticalDate="
				+ criticalDate + ", criticalDateDays=" + criticalDateDays + ", criticalDateHours=" + criticalDateHours
				+ ", url=" + url + ", validForUsage=" + validForUsage + "]";
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getProcessDisplayName() {
		return processDisplayName;
	}
	public void setProcessDisplayName(String processDisplayName) {
		this.processDisplayName = processDisplayName;
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public Integer getSlaDays() {
		return slaDays;
	}
	public void setSlaDays(Integer slaDays) {
		this.slaDays = slaDays;
	}
	public Integer getSlaHours() {
		return slaHours;
	}
	public void setSlaHours(Integer slaHours) {
		this.slaHours = slaHours;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCriticalDate() {
		return criticalDate;
	}
	public void setCriticalDate(String criticalDate) {
		this.criticalDate = criticalDate;
	}
	
	public Integer getCriticalDateDays() {
		return criticalDateDays;
	}
	public void setCriticalDateDays(Integer criticalDateDays) {
		this.criticalDateDays = criticalDateDays;
	}
	public Integer getCriticalDateHours() {
		return criticalDateHours;
	}
	public void setCriticalDateHours(Integer criticalDateHours) {
		this.criticalDateHours = criticalDateHours;
	}
	@Override
	public Boolean getValidForUsage() {
		return null;
	}
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
	}
	
	
	
}
