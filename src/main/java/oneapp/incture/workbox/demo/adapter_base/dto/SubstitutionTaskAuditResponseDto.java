package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.List;

public class SubstitutionTaskAuditResponseDto {
	
	private List<SubstitutionTaskAuditDto> substitutionTaskAuditDtos;
	private ResponseMessage message;
	private Integer listCount;
	
	
	public List<SubstitutionTaskAuditDto> getSubstitutionTaskAuditDtos() {
		return substitutionTaskAuditDtos;
	}
	public void setSubstitutionTaskAuditDtos(List<SubstitutionTaskAuditDto> substitutionTaskAuditDtos) {
		this.substitutionTaskAuditDtos = substitutionTaskAuditDtos;
	}
	public ResponseMessage getMessage() {
		return message;
	}
	public void setMessage(ResponseMessage message) {
		this.message = message;
	}
	public Integer getListCount() {
		return listCount;
	}
	public void setListCount(Integer listCount) {
		this.listCount = listCount;
	}
	
	
	

}
