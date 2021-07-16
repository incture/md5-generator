package oneapp.incture.workbox.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.SubstitutionTaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.dto.SubstitutionTaskAuditResponseDto;
import oneapp.incture.workbox.demo.substitution.dto.StringListResponseDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionApprovalRequestDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionGenericResponseDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionProcessApproverDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRuleAuditDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRuleAuditResponseDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRuleResponseDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRulesDto;
import oneapp.incture.workbox.demo.substitution.services.SubstitutionProcessApprovalFacadeLocal;
import oneapp.incture.workbox.demo.substitution.services.SubstitutionRuleVersionFacadeLocal;

@RestController
@RequestMapping(value = "workbox/substitutionRule", produces = "application/json")
public class SubstitutionRuleRest {

	@Autowired
	private SubstitutionRuleVersionFacadeLocal substitutionRuleFacadeLocal;
	
	@Autowired
	private SubstitutionProcessApprovalFacadeLocal substitutionProcessApprovalFacadeLocal;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseMessage createSubstitution(@RequestBody SubstitutionRulesDto dto,@AuthenticationPrincipal Token token) {
		return substitutionRuleFacadeLocal.createSubstitutionRule(dto,token);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseMessage createUpdateDataAdmin(@RequestBody SubstitutionRulesDto dto,@AuthenticationPrincipal Token token) {
		return substitutionRuleFacadeLocal.updateSubstitutionRule(dto,token);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseMessage deleteProcessConfig(@RequestBody SubstitutionRulesDto dto,@AuthenticationPrincipal Token token) {
		return substitutionRuleFacadeLocal.deleteSubstitutionRule(dto,token);
	}

	@RequestMapping(value = "/substituted", method = RequestMethod.GET)
	public SubstitutionRuleResponseDto getUserSubstituted(
			@RequestParam(value = "userId", required = false) String userId,@AuthenticationPrincipal Token token) {
		return substitutionRuleFacadeLocal.getSubstituted(userId,token);
	}

	@RequestMapping(value = "/substituting", method = RequestMethod.GET)
	public SubstitutionRuleResponseDto getUserSubstituting(
			@RequestParam(value = "userId", required = false) String userId,@AuthenticationPrincipal Token token) {
		return substitutionRuleFacadeLocal.getSubstituting(userId,token);
	}

	@RequestMapping(value = "/getRules", method = RequestMethod.POST)
	public SubstitutionRuleResponseDto getRules(@RequestBody SubstitutionRulesDto dto) {
		return substitutionRuleFacadeLocal.getRules(dto);
	}

	@RequestMapping(value = "/getSubstitutedUsers", method = RequestMethod.GET)
	public StringListResponseDto getSubstitutedUserList(
			@RequestParam(value = "userId", required = false) String userId,@AuthenticationPrincipal Token token) {
		return substitutionRuleFacadeLocal.getSubstitutedUser(userId,token);
	}
	
	@RequestMapping(value = "/getTaskAuditLogs", method = RequestMethod.POST)
	public  SubstitutionTaskAuditResponseDto getTaskAuditLogs(@RequestBody SubstitutionTaskAuditDto dto) {
		return substitutionRuleFacadeLocal.getTaskAuditLogs(dto);
	}
	
	@RequestMapping(value = "/filter", method = RequestMethod.GET	)
	public SubstitutionRuleAuditResponseDto filter(
			@RequestBody SubstitutionRuleAuditDto dto) { 
		return substitutionRuleFacadeLocal.filter(dto);
	}
	
	@RequestMapping(value = "/updateApprovalRequest", method = RequestMethod.POST)
	public  ResponseMessage updateApprovalRequest(@RequestBody SubstitutionApprovalRequestDto dto) {
		return substitutionProcessApprovalFacadeLocal.updateApprovalRequest(dto);
	}
	
	
	@RequestMapping(value = "/getRuleByVersion", method = RequestMethod.GET)
	public  SubstitutionGenericResponseDto getRuleByVersion(@RequestParam String ruleId ,@RequestParam int version) {
		return substitutionProcessApprovalFacadeLocal.getRuleByVersion(ruleId , version);
	}
	
	@RequestMapping(value = "/manageProcesses", method = RequestMethod.GET)
    public SubstitutionGenericResponseDto getAllProcessApprovers() {
        return substitutionProcessApprovalFacadeLocal.getAllSubtitutionProcessApprovers();
    }
	
	@RequestMapping(value = "/createSubtitutionProcessApprover", method = RequestMethod.POST)
    public ResponseMessage createProcessApprovers(@RequestBody SubstitutionProcessApproverDto dto) {
        return substitutionProcessApprovalFacadeLocal.createProcessApprover(dto);
    }
	
	@RequestMapping(value = "/updateSubtitutionProcessApprover", method = RequestMethod.POST)
    public ResponseMessage updateSubtitutionProcessApprover(@RequestBody SubstitutionProcessApproverDto dto) {
        return substitutionProcessApprovalFacadeLocal.updateSubtitutionProcessApprover(dto);
    }
	
	@RequestMapping(value = "/deleteSubtitutionProcessApprover", method = RequestMethod.POST)
    public ResponseMessage deleteProcessApprover(@RequestBody SubstitutionProcessApproverDto dto) {
        return substitutionProcessApprovalFacadeLocal.deleteProcessApprover(dto);
    }
	
	@RequestMapping(value = "/updateSubtitution", method = RequestMethod.POST)
    public ResponseMessage updateSubtitution(@RequestBody SubstitutionProcessApproverDto dto) {
        return substitutionProcessApprovalFacadeLocal.updateSubtitution(dto);
    }
	
	@RequestMapping(value = "/getApprovalRequests", method = RequestMethod.GET)
    public SubstitutionGenericResponseDto getApprovalRequests() {
        return substitutionProcessApprovalFacadeLocal.getApprovalRequests();
    }
	
	@RequestMapping(value = "/activateOrDeactivateRule", method = RequestMethod.POST)
    public ResponseMessage activateOrDeactivateRule(@RequestBody SubstitutionRulesDto dto) {
        return substitutionProcessApprovalFacadeLocal.activateOrDeactivateRule(dto);
    }


}
