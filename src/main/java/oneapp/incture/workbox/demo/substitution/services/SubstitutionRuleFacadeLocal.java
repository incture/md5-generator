package oneapp.incture.workbox.demo.substitution.services;

import java.util.List;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.SubstitutionTaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.dto.SubstitutionTaskAuditResponseDto;
import oneapp.incture.workbox.demo.substitution.dto.StringListResponseDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRuleAuditDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRuleAuditResponseDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRuleResponseDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRulesDto;
import oneapp.incture.workbox.demo.substitution.dto.UserTaskMappingDto;

public interface SubstitutionRuleFacadeLocal {

	public ResponseMessage createSubstitutionRule(SubstitutionRulesDto dto,Token token);

	public ResponseMessage updateSubstitutionRule(SubstitutionRulesDto dto,Token token);

	public SubstitutionRuleResponseDto getSubstituting(String loggedInUser,Token token);

	public SubstitutionRuleResponseDto getSubstituted(String loggedInUser,Token token);

	public ResponseMessage deleteSubstitutionRule(SubstitutionRulesDto dto,Token token);

	public SubstitutionRuleResponseDto getRule(SubstitutionRulesDto dto);

	public StringListResponseDto getSubstitutedUser(String userId,Token token);
	
	public SubstitutionTaskAuditResponseDto getTaskAuditLogs(SubstitutionTaskAuditDto dto);

	public List<String> getSubstitutedUserForTask(String userId, String eventId);

	public void saveUserTaskMapping(List<UserTaskMappingDto> list);

	public Boolean getIsSubstituted(String instanceId, String userId);

	public void updateSubs(String approver, String requestor);

	public SubstitutionRuleAuditResponseDto filter(SubstitutionRuleAuditDto dto);

}
