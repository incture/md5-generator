package oneapp.incture.workbox.demo.substitution.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class SubstitutionRuleResponseDto extends BaseDto {

	private List<SubstitutionRulesDto> dtoList;
	private ResponseMessage message;
	private int listCount;

	ResponseMessage getMessage() {
		return message;
	}

	public List<SubstitutionRulesDto> getDtoList() {
		return dtoList;
	}

	public int getListCount() {
		return listCount;
	}

	public void setListCount(int listCount) {
		this.listCount = listCount;
	}

	public void setDtoList(List<SubstitutionRulesDto> dtoList) {
		this.dtoList = dtoList;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "SubstitutionRuleResponseDto [dtoList=" + dtoList + ", message=" + message + ", listCount=" + listCount
				+ "]";
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
