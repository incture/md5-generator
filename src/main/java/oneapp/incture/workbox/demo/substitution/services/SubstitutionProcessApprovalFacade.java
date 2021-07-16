package oneapp.incture.workbox.demo.substitution.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.substitution.dao.ApprovalRequestDao;
import oneapp.incture.workbox.demo.substitution.dao.SubstitutionProcessApproverDao;
import oneapp.incture.workbox.demo.substitution.dao.SubstitutionRuleVersionDao;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionApprovalRequestDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionGenericResponseDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionProcessApproverDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRulesDto;

@Service
public class SubstitutionProcessApprovalFacade implements SubstitutionProcessApprovalFacadeLocal {

	@Autowired
	ApprovalRequestDao approvalRequestDao;

	@Autowired
	SubstitutionRuleVersionDao substitutionRuleDao;
	
	@Autowired
	SubstitutionProcessApproverDao substitutionProcessApproverDao;

	public ResponseMessage updateApprovalRequest(SubstitutionApprovalRequestDto dto) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(PMCConstant.STATUS_FAILURE);
		responseMessage.setMessage(PMCConstant.FAILED);
		responseMessage.setStatusCode("1");
		try {
			approvalRequestDao.updateApprovalRequest(dto);
			responseMessage.setStatus(PMCConstant.STATUS_SUCCESS);
			responseMessage.setMessage("Approval request updated successfully");
			responseMessage.setStatusCode("0");
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox][PMC][SubstitutionProcessApprovalFacade][updateApprovalRequest][error]"
							+ e.getMessage());
		}
		return responseMessage;
	}

	public SubstitutionGenericResponseDto getRuleByVersion(String ruleId, int version) {

		SubstitutionGenericResponseDto ruleResponseDto = new SubstitutionGenericResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus("FAILURE");
		responseMessage.setStatusCode("1");
		try {
			SubstitutionRulesDto substitutionRulesDto = substitutionRuleDao.getRuleByVersion(ruleId, version);
			responseMessage.setStatusCode("0");
			responseMessage.setStatus(PMCConstant.SUCCESS);
			responseMessage.setMessage("Data fetched successfully");
			ruleResponseDto.setData(substitutionRulesDto);
			ruleResponseDto.setListCount(1);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox][PMC][SubstitutionProcessApprovalFacade][updateApprovalRequest][error]" + e.getMessage());
		}
		
		ruleResponseDto.setMessage(responseMessage);
		return ruleResponseDto;

	}
	
	@Override
    public SubstitutionGenericResponseDto getAllSubtitutionProcessApprovers() {
        SubstitutionGenericResponseDto ruleResponseDto = new SubstitutionGenericResponseDto();
        ResponseMessage responseDto = new ResponseMessage();
        responseDto.setStatus("FAILURE");
        responseDto.setStatusCode("1");
        try {   
            List<SubstitutionProcessApproverDto> substitution = substitutionProcessApproverDao.getAllSubtitutionProcessApprovers();
            ruleResponseDto.setData(substitution==null ? new ArrayList<>() : substitution);
            ruleResponseDto.setMessage(responseDto);
            ruleResponseDto.setListCount(substitution==null ? 0 : substitution.size());
            responseDto.setMessage("Data fetched from substitution process Successfully");
            responseDto.setStatus("SUCCESS");
            responseDto.setStatusCode("0");
        } catch (Exception e) {
            System.err.println("[PMC][SubstitutionProcessApprovalFacade][getAllSubstitutionProcess][error]" + e.getMessage());
            responseDto.setMessage("Failed to filter getAllSubstitutionProcess");
        }
        return ruleResponseDto;
    }
	
	@Override
    public ResponseMessage updateSubtitution(SubstitutionProcessApproverDto dto) {
        ResponseMessage responseDto = new ResponseMessage();
        responseDto.setStatus("FAILURE");
        responseDto.setStatusCode("1");
        try {   
            substitutionProcessApproverDao.saveOrUpdateSubstitutionProcessApprover(dto);
            responseDto.setMessage("activated/deactivated Successfully");
            responseDto.setStatus("SUCCESS");
            responseDto.setStatusCode("0");
        } catch (Exception e) {
            System.err.println("[PMC][SubstitutionProcessApprovalFacade][updateSubtitution][error]" + e.getMessage());
            responseDto.setMessage("Failed to update Subtitution");
        }
        return responseDto;
	}
	
	@Override
    public SubstitutionGenericResponseDto getApprovalRequests() {
        SubstitutionGenericResponseDto ruleResponseDto = new SubstitutionGenericResponseDto();
        ResponseMessage responseDto = new ResponseMessage();
        responseDto.setStatus("FAILURE");
        responseDto.setStatusCode("1");
        try {   
            List<SubstitutionApprovalRequestDto> substitution = approvalRequestDao.getApprovalRequests();
            responseDto.setMessage("Data fetched from approval request Successfully");
            responseDto.setStatus("SUCCESS");
            responseDto.setStatusCode("0");
            ruleResponseDto.setData(substitution);
            ruleResponseDto.setMessage(responseDto);
            ruleResponseDto.setListCount(substitution.size());
        } catch (Exception e) {
            System.err.println("[PMC][SubstitutionProcessApprovalFacade][getApprovalRequests][error]" + e.getMessage());
            responseDto.setMessage("Failed to getApprovalRequests");
        }
       
        return ruleResponseDto;
    }
	
	public ResponseMessage createProcessApprover(SubstitutionProcessApproverDto dto){
		ResponseMessage responseDto = new ResponseMessage();
        responseDto.setStatus("FAILURE");
        responseDto.setStatusCode("1");
        try {
			substitutionProcessApproverDao.saveOrUpdateSubstitutionProcessApprover(dto);
			responseDto.setMessage("Substitution process approver created sucessfully");
            responseDto.setStatus("SUCCESS");
            responseDto.setStatusCode("0");
		} catch (Exception e) {
			System.err.println("[PMC][SubstitutionProcessApprovalFacade][createProcessApprover][error]" + e.getMessage());
		}
        return responseDto;
	}
	
	public ResponseMessage deleteProcessApprover(SubstitutionProcessApproverDto dto){
		
		ResponseMessage responseDto = new ResponseMessage();
        responseDto.setStatus("FAILURE");
        responseDto.setStatusCode("1");
        try {
			substitutionProcessApproverDao.deleteProcessApprover(dto.getProcess());
			responseDto.setMessage("Substitution process approver deleted sucessfully");
            responseDto.setStatus("SUCCESS");
            responseDto.setStatusCode("0");
		} catch (Exception e) {
			System.err.println("[PMC][SubstitutionProcessApprovalFacade][deleteProcessApprover][error]" + e.getMessage());
		}
        return responseDto;
	}
    
    public ResponseMessage updateSubtitutionProcessApprover(SubstitutionProcessApproverDto dto) {
        ResponseMessage responseDto = new ResponseMessage();
        responseDto.setStatus("FAILURE");
        responseDto.setStatusCode("1");
        try {   
            substitutionProcessApproverDao.updateSubtitutionProcessApprover(dto);
            responseDto.setMessage("updated Successfully");
            responseDto.setStatus("SUCCESS");
            responseDto.setStatusCode("0");
        } catch (Exception e) {
            System.err.println("[PMC][SubstitutionProcessApprovalFacade][updateSubtitutionProcessApprover][error]" + e.getMessage());
            responseDto.setMessage("Failed to update SubtitutionProcessApprover");
        }
        return responseDto;
    }

	@Override
	public ResponseMessage activateOrDeactivateRule(SubstitutionRulesDto dto) {
		
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus("FAILURE");
		responseMessage.setStatusCode("1");
		try {
			substitutionRuleDao.activateOrDeactivateRule(dto);
			responseMessage.setMessage("updated Successfully");
			responseMessage.setStatus("SUCCESS");
			responseMessage.setStatusCode("0");
		} catch (Exception e) {
            System.err.println("[PMC][SubstitutionProcessApprovalFacade][activateOrDeactivateRule][error]" + e.getMessage());
            responseMessage.setMessage("Failed to update the status");

		}
		return responseMessage;
		
	}

}
