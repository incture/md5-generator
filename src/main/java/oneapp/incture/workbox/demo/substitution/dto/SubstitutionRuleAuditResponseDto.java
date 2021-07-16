package oneapp.incture.workbox.demo.substitution.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class SubstitutionRuleAuditResponseDto {

	private List<SubstitutionRuleAuditDto> dtoList;
	private ResponseMessage message;
	private int listCount;
	public List<SubstitutionRuleAuditDto> getDtoList() {
		return dtoList;
	}
	public void setDtoList(List<SubstitutionRuleAuditDto> dtoList) {
		this.dtoList = dtoList;
	}
	public ResponseMessage getMessage() {
		return message;
	}
	public void setMessage(ResponseMessage message) {
		this.message = message;
	}
	public int getListCount() {
		return listCount;
	}
	public void setListCount(int listCount) {
		this.listCount = listCount;
	}

	
}


