package oneapp.incture.workbox.demo.substitution.services;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sap.cloud.security.xsuaa.token.Token;
import com.sap.security.um.user.User;

import oneapp.incture.workbox.demo.adapter_base.dao.UserIdpMappingDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.SubstitutionTaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.dto.SubstitutionTaskAuditResponseDto;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_base.util.UserManagementUtil;
import oneapp.incture.workbox.demo.substitution.dao.SubstitutionRuleAuditDao;
import oneapp.incture.workbox.demo.substitution.dao.SubstitutionRuleDao;
import oneapp.incture.workbox.demo.substitution.dao.SubstitutionRuleVersionDao;
import oneapp.incture.workbox.demo.substitution.dao.UserTaskMappingDao;
import oneapp.incture.workbox.demo.substitution.dto.StringListResponseDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRuleAuditDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRuleAuditResponseDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRuleResponseDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRulesDto;
import oneapp.incture.workbox.demo.substitution.dto.UserTaskMappingDto;
import oneapp.incture.workbox.demo.substitution.util.ServicesUtilSubs;

@Service("SubstitutionRuleVersionFacade")
//@Transactional

public class SubstitutionRuleVersionFacade implements SubstitutionRuleVersionFacadeLocal{
	
	private static final Logger logger = LoggerFactory.getLogger(SubstitutionRuleFacade.class);

	@Autowired
	private SubstitutionRuleVersionDao substitutionDao;

	@Autowired
	UserTaskMappingDao userTaskMappingDao;

	@Autowired
	UserIdpMappingDao userIDPMappingDao;
	
	@Autowired
	SubstitutionRuleAuditDao substitutionRuleAuditDao;
	
	

	ResponseMessage responseDto;

	public ResponseMessage createSubstitutionRule(SubstitutionRulesDto dto,Token token) {

		responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			//User user = UserManagementUtil.getLoggedInUser();
			dto.setCreatedBy(token.getLogonName().toUpperCase());
			dto.setCreatedByDisp(userIDPMappingDao.getUserName(token.getLogonName().toUpperCase()));
			String validateRes = substitutionDao.validateRule(dto);
			if(validateRes.equals(PMCConstant.FAILURE)){
				responseDto.setMessage("Update the Existing Rule");
				return responseDto;
			}
			String result = substitutionDao.createRule(dto,token);
			if (result.equals(PMCConstant.SUCCESS)) {
				responseDto.setMessage("Substitution Rule " + PMCConstant.CREATED_SUCCESS);
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			} else
				responseDto.setMessage("Substitution rule " + PMCConstant.CREATE_FAILURE);
		} catch (Exception e) {
			responseDto.setMessage("Failed to create Substitution rule");
			System.err.println("[WBP-Dev][Workbox][PMC][SubstitutionRuleFacde][create][error]" + e.getMessage() + ","
					+ e.getLocalizedMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	public ResponseMessage deleteSubstitutionRule(SubstitutionRulesDto dto,Token token) {
		responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			//User user = UserManagementUtil.getLoggedInUser();
			dto.setSubstitutedUser(token.getLogonName().toUpperCase());
			dto.setSubstitutedUserName(userIDPMappingDao.getUserName(token.getLogonName().toUpperCase()));
			
			String result = substitutionDao.deleteRule(dto);
			if (result.equals(PMCConstant.SUCCESS)) {
				responseDto.setMessage("Substitution Rule " + PMCConstant.DELETE_SUCCESS);
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			} else {
				responseDto.setMessage("Substitution Rule " + PMCConstant.DELETE_FAILURE);
			}
		} catch (Exception e) {
			responseDto.setMessage("Failed to delete Substitution rule");
			System.err.println("[WBP-Dev][Workbox][PMC][SubstitutionRuleFacde][delete][error]" + e.getMessage());
		}
		return responseDto;

	}

	public ResponseMessage updateSubstitutionRule(SubstitutionRulesDto dto,Token token) {
		responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			//User user = UserManagementUtil.getLoggedInUser();
			dto.setCreatedBy(token.getLogonName().toUpperCase());
			dto.setCreatedByDisp(userIDPMappingDao.getUserName(token.getLogonName().toUpperCase()));
			String result = "";
			if(dto.isChangeActive()) {
				dto.setUpdateAt(ServicesUtilSubs.getUTCTime());
				result = substitutionDao.activateOrDeactivateRule(dto);
			}
			else{
				result = substitutionDao.updateRule(dto, PMCConstant.SUBSTITUTION_INSTANT_TYPE,token);
			}
			
			if (result.equals(PMCConstant.SUCCESS)) {
				responseDto.setMessage("Substitution rule " + PMCConstant.UPDATE_SUCCESS);
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
			} else
				responseDto.setMessage("Substitution rule " + PMCConstant.UPDATE_FAILURE);
		} catch (Exception e) {
			responseDto.setMessage("Failed to update Substitution rule");
			System.err.println("[WBP-Dev][Workbox][PMC][SubstitutionRuleFacde][create][error]" + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;

	}

	public SubstitutionRuleResponseDto getSubstituting(String userId,Token token) {
		SubstitutionRuleResponseDto ruleResponseDto = new SubstitutionRuleResponseDto();
		String user = token.getLogonName();
		/*if (ServicesUtil.isEmpty(userId)) {*/
		/*
		 * User loggedIn = UserManagementUtil.getLoggedInUser(); user =
		 * loggedIn.getName().toUpperCase();
		 */
		/*} else
			user = userId;*/


		responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		try {

			List<SubstitutionRulesDto> dtoList = substitutionDao.getSubstitution(user, PMCConstant.SUBSTITUTED, null);

			if (!ServicesUtil.isEmpty(dtoList)) {
				ruleResponseDto.setDtoList(dtoList);
				ruleResponseDto.setListCount(dtoList.size());
				responseDto.setMessage("Data fetched Successfully");
			} else {
				responseDto.setMessage(PMCConstant.NO_RESULT);
			}
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");
		} catch (Exception e) {
			System.err.println("[WBP-Dev][PMC][SubstitutionRuleFacade][getUserSubstitution][error]" + e.getMessage());
			responseDto.setMessage("Failed to get Substituting rules");
		}
		ruleResponseDto.setMessage(responseDto);
		return ruleResponseDto;
	}

	public SubstitutionRuleResponseDto getSubstituted(String userId,Token token) {
		SubstitutionRuleResponseDto ruleResponseDto = new SubstitutionRuleResponseDto();
		String user = token.getLogonName();
		/*if (ServicesUtil.isEmpty(userId)) {*/
		/*
		 * User loggedIn = UserManagementUtil.getLoggedInUser(); user =
		 * loggedIn.getName().toUpperCase();
		 */
		try{
			System.err.println(token.getUsername());
		}catch (Exception e) {
			System.err.println("[WBP-Dev]error in fetching user");
		}
		/*} else
			user = userId;*/
		responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		try {

			List<SubstitutionRulesDto> dtoList = substitutionDao.getSubstitution(user, PMCConstant.SUBSTITUTING, null);
			if (!ServicesUtil.isEmpty(dtoList)) {
				ruleResponseDto.setDtoList(dtoList);
				ruleResponseDto.setListCount(dtoList.size());
				responseDto.setMessage("Data fetched Successfully");
			} else {
				responseDto.setMessage(PMCConstant.NO_RESULT);
			}
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");
		} catch (Exception e) {
			System.err.println("[WBP-Dev][PMC][SubstitutionRuleFacade][getUserAsSubstitute][error]" + e.getMessage());
			responseDto.setMessage("Failed to get Substituted rules");
		}
		ruleResponseDto.setMessage(responseDto);
		return ruleResponseDto;
	}

	public SubstitutionRuleResponseDto getRules(SubstitutionRulesDto dto) {
		SubstitutionRuleResponseDto ruleResponseDto = new SubstitutionRuleResponseDto();
		responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		try {
			//User user = UserManagementUtil.getLoggedInUser();
			//dto.setSubstitutedUser(user.getName().toUpperCase());
			//			if(!ServicesUtil.isEmpty(user.getAttribute("lastName"))){
			//				dto.setSubstitutedUserName(user.getAttribute("firstName")+" "+user.getAttribute("lastName"));
			//			}else{
			//				dto.setSubstitutedUserName(userIDPMappingDao.getUserName(user.getName().toUpperCase()));
			//			}
			List<SubstitutionRulesDto> dtoList = null;

			dtoList = substitutionDao.getRule(dto.getSubstitutedUser(), dto.getSubstitutingUser(), dto.getStartDate(),
					dto.getEndDate(), dto.getCreatedBy());
			if (!ServicesUtil.isEmpty(dtoList)) {
				ruleResponseDto.setDtoList(dtoList);
				ruleResponseDto.setListCount(dtoList.size());
				responseDto.setMessage("Data fetched Successfully");
			} else {
				responseDto.setMessage(PMCConstant.NO_RESULT);
			}
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");
		} catch (Exception e) {
			logger.error("[PMC][SubstitutionRuleFacade][getUserAsSubstitute][error]" + e.getMessage());
			responseDto.setMessage("Failed to create Substitution rules");
		}
		ruleResponseDto.setMessage(responseDto);
		return ruleResponseDto;
	}

	public StringListResponseDto getSubstitutedUser(String userId,Token token) {
		StringListResponseDto ruleResponseDto = new StringListResponseDto();
		String user = null;
		if (ServicesUtil.isEmpty(userId)) {
			/*
			 * User loggedIn = UserManagementUtil.getLoggedInUser(); user =
			 * loggedIn.getName();
			 */
			user = token.getLogonName();
		} else
			user = userId;
		responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		try {
			List<String> dtoList = null;
			if (!ServicesUtil.isEmpty(user)) {
				dtoList = substitutionDao.getSubstitutedUser(user);
			}
			if (!ServicesUtil.isEmpty(dtoList)) {
				ruleResponseDto.setStr(dtoList);
				responseDto.setMessage("Data fetched Successfully");
			} else {
				responseDto.setMessage(PMCConstant.NO_RESULT);
			}
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");
		} catch (Exception e) {
			logger.error("[PMC][SubstitutionRuleFacade][getUserAsSubstitute][error]" + e.getMessage());
			responseDto.setMessage("Failed to create Substitution rules");
		}
		ruleResponseDto.setMessage(responseDto);
		return ruleResponseDto;
	}

	@Override
	public List<String> getSubstitutedUserForTask(String userId, String eventId) {
		List<String> dtoList = null;
		try {
			dtoList = substitutionDao.getSubstitutedUserForTask(userId,eventId);
		} catch (Exception e) {
			System.err.println("[WBP-Dev]Error in getting substituted User"+e);
		}

		return dtoList;
	}

	@Override
	public void saveUserTaskMapping(List<UserTaskMappingDto> list) {
		try {
			userTaskMappingDao.saveOrUpdateTasks(list);
		} catch (Exception e) {
			System.err.println("[WBP-Dev]Error Inserting to UserTaskMapping"+e);
		}

	}

	@Override
	public Boolean getIsSubstituted(String instanceId, String userId) {
		try {
			return userTaskMappingDao.deletedSubstitutingTask(userId,instanceId);
		} catch (Exception e) {
			System.err.println("[WBP-Dev]Error Deleting to UserTaskMapping"+e);
		}
		return false;
	}

	@Override
	public void updateSubs(String approver, String requestor) {
		substitutionDao.updateSubsRule(approver, requestor);
	}
	
	public SubstitutionTaskAuditResponseDto getTaskAuditLogs(SubstitutionTaskAuditDto dto){
		
		SubstitutionTaskAuditResponseDto substitutionTaskAuditResponseDto = new SubstitutionTaskAuditResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(PMCConstant.STATUS_FAILURE);
		responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);

		
		try {
			
			List<Object[]> taskAuditDetails = substitutionDao.getTaskAuditLogs(dto);
			List<SubstitutionTaskAuditDto> substitutionTaskAuditDtos = new ArrayList<>();
			for(Object[] taskAudit : taskAuditDetails) {
				System.err.println(taskAudit);
				SubstitutionTaskAuditDto substitutionTaskAuditDto  = new SubstitutionTaskAuditDto();
				substitutionTaskAuditDto.setEventId((String) taskAudit[0]);
				substitutionTaskAuditDto.setSubstitutedUser((String) taskAudit[1]);
				substitutionTaskAuditDto.setSubstitutingUser((String) taskAudit[2]);
				substitutionTaskAuditDto.setActionType((String) taskAudit[3]);
				substitutionTaskAuditDto.setUpdateAt((Date) taskAudit[4]);
				substitutionTaskAuditDto.setTaskAuditId((String) taskAudit[5]);
				substitutionTaskAuditDto.setUpdateAtInString(ServicesUtil.convertFromDateToString((Date) taskAudit[4]));
				substitutionTaskAuditDtos.add(substitutionTaskAuditDto);
				
			}
			
			responseMessage.setStatus(PMCConstant.STATUS_SUCCESS);
			responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
			responseMessage.setMessage("Data fetched successfully");
			substitutionTaskAuditResponseDto.setSubstitutionTaskAuditDtos(substitutionTaskAuditDtos);
			substitutionTaskAuditResponseDto.setMessage(responseMessage);
			substitutionTaskAuditResponseDto.setListCount(substitutionTaskAuditDtos.size());
		}
		catch (Exception e) {
			
			System.err.println("[WBP-Dev] getTaskAuditLogs  Error : "+e.getMessage());
		}
		
		
		
		return substitutionTaskAuditResponseDto;
	}

	@Override
	public SubstitutionRuleAuditResponseDto filter(SubstitutionRuleAuditDto dto) {
		SubstitutionRuleAuditResponseDto ruleResponseDto = new SubstitutionRuleAuditResponseDto();
		responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		try {	
			responseDto.setMessage("Data filtered Successfully");
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");
		} catch (Exception e) {
			logger.error("[PMC][SubstitutionRuleFacade][getUserAsSubstitute][error]" + e.getMessage());
			responseDto.setMessage("Failed to create Substitution rules");
		}
		List<SubstitutionRuleAuditDto> substitution = substitutionRuleAuditDao.filter(dto);
		ruleResponseDto.setDtoList(substitution);
		ruleResponseDto.setMessage(responseDto);
		ruleResponseDto.setListCount(substitution.size());
		return ruleResponseDto;
	}


}
