package oneapp.incture.workbox.demo.adapter_base.dto;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class WorkloadRangeDto extends BaseDto {

	private String loadType;
	private Integer lowLimit;
	private Integer highLimit;
	private String validForUsage;
	
	public void setValidForUsage(String validForUsage) {
		this.validForUsage = validForUsage;
	}

	public String getLoadType() {
		return loadType;
	}

	public void setLoadType(String loadType) {
		this.loadType = loadType;
	}

	public Integer getLowLimit() {
		return lowLimit;
	}

	public void setLowLimit(Integer lowLimit) {
		this.lowLimit = lowLimit;
	}

	public Integer getHighLimit() {
		return highLimit;
	}

	public void setHighLimit(Integer highLimit) {
		this.highLimit = highLimit;
	}

	@Override
	public String toString() {
		return "WorkloadRangeDto [loadType=" + loadType + ", lowLimit=" + lowLimit + ", highLimit=" + highLimit
				+ ", validForUsage=" + validForUsage + "]";
	}

	@Override
	public Boolean getValidForUsage() {
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}

}
