package oneapp.incture.workbox.demo.substitution.services;



import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionApprovalRequestDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionGenericResponseDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionProcessApproverDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRulesDto;

public interface SubstitutionProcessApprovalFacadeLocal {
	
	
	
	public  ResponseMessage updateApprovalRequest(SubstitutionApprovalRequestDto dto);
	public  SubstitutionGenericResponseDto getRuleByVersion(String ruleId ,int version);
	public SubstitutionGenericResponseDto getAllSubtitutionProcessApprovers();
	public ResponseMessage createProcessApprover(SubstitutionProcessApproverDto dto);
	public ResponseMessage deleteProcessApprover(SubstitutionProcessApproverDto dto);
	public ResponseMessage updateSubtitution(SubstitutionProcessApproverDto dto);
	public SubstitutionGenericResponseDto getApprovalRequests();
	public ResponseMessage updateSubtitutionProcessApprover(SubstitutionProcessApproverDto dto);
	public ResponseMessage activateOrDeactivateRule(SubstitutionRulesDto dto);

}
