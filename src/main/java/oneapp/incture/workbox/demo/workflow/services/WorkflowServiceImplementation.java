package oneapp.incture.workbox.demo.workflow.services;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.dao.ActionConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dao.StatusConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.workflow.dao.CrossConstantDao;
import oneapp.incture.workbox.demo.workflow.dao.CustomAttrTemplateDao;
import oneapp.incture.workbox.demo.workflow.dao.OwnerSelectionRuleDao;
import oneapp.incture.workbox.demo.workflow.dao.OwnerSequenceTypeDao;
import oneapp.incture.workbox.demo.workflow.dao.ProcessConfigDao;
import oneapp.incture.workbox.demo.workflow.dao.ProcessTemplateDao;
import oneapp.incture.workbox.demo.workflow.dao.RuleDao;
import oneapp.incture.workbox.demo.workflow.dao.TaskTemplateOwnerDao;
import oneapp.incture.workbox.demo.workflow.dto.AFEOrderDetail;
import oneapp.incture.workbox.demo.workflow.dto.AdvanceCustomProcessCreationDto;
import oneapp.incture.workbox.demo.workflow.dto.CustomProcessCreationDto;
import oneapp.incture.workbox.demo.workflow.dto.DropDownRequestDto;
import oneapp.incture.workbox.demo.workflow.dto.ProcessConfigListDto;
import oneapp.incture.workbox.demo.workflow.dto.ProcessConfigTbDto;
import oneapp.incture.workbox.demo.workflow.dto.TeamDetailDto;
import oneapp.incture.workbox.demo.workflow.dto.WorkflowDto;
import oneapp.incture.workbox.demo.workflow.util.ProcessDetail;
import oneapp.incture.workbox.demo.workflow.util.ValidateRequest;
import oneapp.incture.workbox.demo.workflow.util.WorkflowCreationConstant;

@Service
public class WorkflowServiceImplementation implements WorkflowService {

	@Autowired
	private ProcessConfigDao processConfigDao;

	@Autowired
	private ProcessDetail processDetail;

	@Autowired
	private ValidateRequest validateRequest;

	@Autowired
	private CustomAttrTemplateDao customAttrTemplateDao;

	@Autowired
	private TaskTemplateOwnerDao taskTemplateOwnerDao;

	@Autowired
	private CrossConstantDao crossConstantDao;

	@Autowired
	private ProcessTemplateDao processTemplateDao;

	@Autowired
	private RuleDao ruleDao;

	@Autowired
	private ActionConfigDao actionConfigDao;

	@Autowired
	private StatusConfigDao statusConfigDao;

	@Autowired
	OwnerSelectionRuleDao ownerSelectionRuleDao;
	
	@Autowired
	OwnerSequenceTypeDao ownerSequenceTypeDao;
	
	

	@Override
	public CustomProcessCreationDto fetchProcessDetail(String processName, String processType) {

		return processDetail.fetchProcessDetail(processName, processType);
	}

	@Override
	public ResponseMessage updateProcessWorkflow(CustomProcessCreationDto updateProcessDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(WorkflowCreationConstant.FAILURE);
		resp.setStatus(WorkflowCreationConstant.FAILURE);
		resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);

		String res = "";
		try {
			if ((updateProcessDto.getProcessDetail() != null && updateProcessDto.getProcessDetail().getOrigin() != null)
					&& (updateProcessDto.getProcessDetail().getOrigin()
							.equals(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN))) {
				resp = validateRequest.validate(updateProcessDto);
				if (WorkflowCreationConstant.FAILURE.equals(resp.getStatus()))
					return resp;
			}

			if (processConfigDao.validateProcess(updateProcessDto.getProcessDetail().getProcessName())
					.equals(WorkflowCreationConstant.FAILURE)) {
				resp.setMessage("Process Does Not Exists");
				return resp;
			}
			if ((updateProcessDto.getProcessDetail() != null && updateProcessDto.getProcessDetail().getOrigin() != null)
					&& (updateProcessDto.getProcessDetail().getOrigin()
							.equals(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN))) {
				res = processTemplateDao.updateProcessTemplate(updateProcessDto);
				if (res.equals(WorkflowCreationConstant.FAILURE))
					return resp;
				updateProcessDto = processDetail.addTaskLevelCustomAttr(updateProcessDto);

				// res =
				// customAttrTemplateDao.updateAttibutes(updateProcessDto.getCustomAttribute());

				if (res.equals(WorkflowCreationConstant.FAILURE))
					return resp;
				for (TeamDetailDto detailDto : updateProcessDto.getTeamDetailDto()) {
					ruleDao.saveOrUpdateRules(detailDto.getRules());
					ownerSelectionRuleDao.savaOrUpdateOwnerRule(detailDto.getOwnerSelectionRules()
							,detailDto.getProcessName(),detailDto.getEventName());
					
					
				}
				
				

				if (res.equals(WorkflowCreationConstant.FAILURE))
					return resp;

			}
			res = processConfigDao.updateProcessConfig(updateProcessDto);
			res = customAttrTemplateDao.updateAttibutes(updateProcessDto.getCustomAttribute());

			if (res.equals(WorkflowCreationConstant.FAILURE)) {
				return resp;
			} else if (res.equals(WorkflowCreationConstant.SUCCESS)) {
				resp.setMessage("Workflow updated Successfully for "
						+ updateProcessDto.getProcessDetail().getProcessDisplayName());
				resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_SUCCESS);
				resp.setStatus(WorkflowCreationConstant.SUCCESS);
			}

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][UPDATE WORKFLOW]ERROR:" + e.getMessage());
		}
		return resp;
	}

	@Override
	public ResponseMessage deleteprocess(List<String> processName) {
		return processDetail.deleteprocess(processName);
	}

	@Override
	public ResponseMessage processWorkflowCreation(CustomProcessCreationDto customProcessCreation) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(WorkflowCreationConstant.FAILURE);
		resp.setStatus(WorkflowCreationConstant.FAILURE);
		resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);

		try {
			resp = validateRequest.validate(customProcessCreation);
			if (WorkflowCreationConstant.FAILURE.equals(resp.getStatus()))
				return resp;
			if (processConfigDao
					.validateProcess(customProcessCreation.getProcessDetail().getProcessName().replace(" ", ""))
					.equals(WorkflowCreationConstant.SUCCESS)) {
				resp.setMessage("Process Already Exists");
				return resp;
			}
			Gson g = new Gson();
			WorkflowDto workflowDto = processDetail.generateWorkFlowDtoNew(customProcessCreation);
			System.err.println(
					"[WBP-Dev]WorkflowServiceImplementation.processWorkflowCreation()" + g.toJson(workflowDto));
			customAttrTemplateDao.saveOrUpdateAttributes(workflowDto.getAttributes());
			processConfigDao.saveOrUpdateProcessConfig(workflowDto.getProcessConfigTbDto());
			processTemplateDao.saveOrUpdateProcessTemplate(workflowDto.getProcessTemplateDtos());
			taskTemplateOwnerDao.saveOrUpdateTaskTemplateOwners(workflowDto.getTaskTemplateOwnerDtos());
			crossConstantDao.saveOrUpdateCrossConstants(workflowDto.getCrossConstantDto());
			ruleDao.saveOrUpdateRules(workflowDto.getRuleDtos());
			actionConfigDao.savaOrUpdateActionConfig(workflowDto.getActionConfigDos());
			statusConfigDao.savaOrUpdateStatusConfig(workflowDto.getStatusConfigDos());
			ownerSelectionRuleDao.savaOrUpdateOwnerSelectionRule(workflowDto.getOwnerSelectionRuleDos());
			ownerSequenceTypeDao.saveOrUpdateOwnerSequence(workflowDto.getOwnerSequenceTypeDos());

			resp.setMessage("Workflow for " + customProcessCreation.getProcessDetail().getProcessName()
					+ " created Successfully");
			resp.setStatus(WorkflowCreationConstant.SUCCESS);
			resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_SUCCESS);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][WORKFLOW CREATION] ERROR:" + e.getMessage());
		}

		return resp;
	}

	@Override
	public ProcessConfigListDto getProcess(String processType) {

		ProcessConfigListDto processConfigListDto = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(WorkflowCreationConstant.FAILURE);
		resp.setStatus(WorkflowCreationConstant.FAILURE);
		resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);

		System.err.println("[WBP-Dev]done");
		List<ProcessConfigTbDto> processDetails = processConfigDao.getProcessDetailList(processType);
		if (ServicesUtil.isEmpty(processDetails)) {
			resp.setMessage("Process Does Not Exists");
			processConfigListDto = new ProcessConfigListDto();
			processConfigListDto.setResponseMessage(resp);
			return processConfigListDto;
		}
		System.err.println("[WBP-Dev]done");
		resp.setMessage("Detail Fetched Successfully");
		resp.setStatus(WorkflowCreationConstant.SUCCESS);
		resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_SUCCESS);
		processConfigListDto = new ProcessConfigListDto();
		processConfigListDto.setProcessDetails(processDetails);
		processConfigListDto.setResponseMessage(resp);

		return processConfigListDto;
	}

	@Override
	public ResponseMessage insertDropDownValues(DropDownRequestDto dropDownValues) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(WorkflowCreationConstant.FAILURE);
		resp.setStatus(WorkflowCreationConstant.FAILURE);
		resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);
		try {
			DropDownResposeDto response = processDetail.createCrossConstantDto(dropDownValues);
			if (WorkflowCreationConstant.FAILURE.equals(response.getResposneMessage().getStatus()))
				return resp;
			if (!response.getCrossConstants().isEmpty())
				crossConstantDao.saveOrUpdateCrossConstants(response.getCrossConstants());

			if (!response.getValuesToRemove().isEmpty())
				crossConstantDao.removeValues(dropDownValues.getCustomKey(), response.getValuesToRemove());

			resp = response.getResposneMessage();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][insertDropDownValues]Insert DropDown values " + e.getMessage());
		}
		return resp;
	}

	@Override
	public DropDownRequestDto getDropDownValues(String customKey) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(WorkflowCreationConstant.FAILURE);
		resp.setStatus(WorkflowCreationConstant.FAILURE);
		resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);
		DropDownRequestDto dropDownRequestDto = new DropDownRequestDto();
		try {
			dropDownRequestDto.setResponseMessage(resp);
			dropDownRequestDto = new DropDownRequestDto();
			if (ServicesUtil.isEmpty(customKey))
				return dropDownRequestDto;
			dropDownRequestDto.setCustomKey(customKey);
			dropDownRequestDto.setValues(crossConstantDao.getValues(customKey));
			resp.setMessage(WorkflowCreationConstant.SUCCESS);
			resp.setStatus(WorkflowCreationConstant.SUCCESS);
			resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_SUCCESS);
			dropDownRequestDto.setResponseMessage(resp);

		} catch (Exception e) {
			System.err.println("[WBP-Dev][insertDropDownValues]get DropDown values " + e.getMessage());
		}
		return dropDownRequestDto;
	}

	@Override
	public ResponseMessage serverStatus(String serverUrl) {
		ResponseMessage resp = new ResponseMessage();
		try {
			URL url = new URL(serverUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			int status = con.getResponseCode();
			System.err.println(status);
			if (status == 503) {
				resp.setMessage("FAILURE");
				System.err.println(resp);
			} else {
				resp.setMessage("SUCCESS");
				System.err.println(resp);
			}
		} catch (Exception e) {
			resp.setMessage("FAILURE");
			System.err.println("Unable to reach");
		}
		return resp;

	}

	
	@Override
	public AFEOrderDetail getAFENexusOrder(String processName) {
		AFEOrderDetail  afeOrderDetail = new AFEOrderDetail();
		ResponseMessage res = new ResponseMessage();
		res.setMessage(WorkflowCreationConstant.FAILURE);
		res.setStatus(WorkflowCreationConstant.FAILURE);
		res.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);
		try {
			String orderType = processConfigDao.getAfeNexus(processName);
			afeOrderDetail.setOrderType(orderType);
			res.setMessage(WorkflowCreationConstant.SUCCESS);
			res.setStatus(WorkflowCreationConstant.SUCCESS);
			res.setStatusCode(WorkflowCreationConstant.STATUS_CODE_SUCCESS);
			afeOrderDetail.setResponseMessage(res);
		} catch (Exception e) {
			System.err.println(e);
		}

		return afeOrderDetail;
	}

	@Override
	public ResponseMessage updateAfeNexusOrder(String processName,String orderType) {
		ResponseMessage responseMessage = new ResponseMessage();

		responseMessage.setMessage(WorkflowCreationConstant.FAILURE);
		responseMessage.setStatus(WorkflowCreationConstant.FAILURE);
		responseMessage.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);
		try {
			processConfigDao.updateAfeNexusOrder(processName,orderType);
			responseMessage.setMessage(WorkflowCreationConstant.SUCCESS);
			responseMessage.setStatus(WorkflowCreationConstant.SUCCESS);
			responseMessage.setStatusCode(WorkflowCreationConstant.STATUS_CODE_SUCCESS);
		} catch (Exception e) {
			System.err.println(e);
		}

		return responseMessage;
	}

	@Override
	public ResponseMessage manageProcessWorkflow(AdvanceCustomProcessCreationDto customProcessCreation) {
		
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(WorkflowCreationConstant.FAILURE);
		resp.setStatus(WorkflowCreationConstant.FAILURE);
		resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);

		try {
			
			WorkflowDto workflowDto = processDetail.generateAdvanceWorkFlowDto(customProcessCreation);
			System.err.println(
					"[WBP-Dev]WorkflowServiceImplementation.processWorkflowCreation()" + new Gson().toJson(workflowDto));
			customAttrTemplateDao.saveOrUpdateAttributes(workflowDto.getAttributes());
			processConfigDao.saveOrUpdateProcessConfig(workflowDto.getProcessConfigTbDto());
			processTemplateDao.saveOrUpdateProcessTemplate(workflowDto.getProcessTemplateDtos());
			taskTemplateOwnerDao.saveOrUpdateTaskTemplateOwners(workflowDto.getTaskTemplateOwnerDtos());
			crossConstantDao.saveOrUpdateCrossConstants(workflowDto.getCrossConstantDto());
			ruleDao.saveOrUpdateRules(workflowDto.getRuleDtos());
			actionConfigDao.savaOrUpdateActionConfig(workflowDto.getActionConfigDos());
			statusConfigDao.savaOrUpdateStatusConfig(workflowDto.getStatusConfigDos());
			ownerSelectionRuleDao.savaOrUpdateOwnerSelectionRule(workflowDto.getOwnerSelectionRuleDos());
			ownerSequenceTypeDao.saveOrUpdateOwnerSequence(workflowDto.getOwnerSequenceTypeDos());

			resp.setMessage("Workflow for " + customProcessCreation.getProcessDetail().getProcessName()
					+ " created Successfully");
			resp.setStatus(WorkflowCreationConstant.SUCCESS);
			resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_SUCCESS);
			
			
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][ADVANCE WORKFLOW CREATION] ERROR:" + e.getMessage());
		}
		
		return resp;
	}
	

}
