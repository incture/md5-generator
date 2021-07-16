package oneapp.incture.workbox.demo.workflow.dto;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class CrossConstantDto extends BaseDto{

	private String constantId;
	private String constantName;
	private String constantValue;
	@Override
	public String toString() {
		return "CrossConstantDto [constantId=" + constantId + ", constantName=" + constantName + ", constantValue="
				+ constantValue + "]";
	}
	public String getConstantId() {
		return constantId;
	}
	public void setConstantId(String constantId) {
		this.constantId = constantId;
	}
	public String getConstantName() {
		return constantName;
	}
	public void setConstantName(String constantName) {
		this.constantName = constantName;
	}
	public String getConstantValue() {
		return constantValue;
	}
	public void setConstantValue(String constantValue) {
		this.constantValue = constantValue;
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
