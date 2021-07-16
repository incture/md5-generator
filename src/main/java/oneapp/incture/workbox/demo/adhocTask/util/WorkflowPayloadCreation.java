package oneapp.incture.workbox.demo.adhocTask.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.workflow.dto.CustomAttributeTemplateDto;
import oneapp.incture.workbox.demo.workflow.dto.CustomProcessCreationDto;
import oneapp.incture.workbox.demo.workflow.dto.ProcessConfigTbDto;
import oneapp.incture.workbox.demo.workflow.dto.StatusDto;
import oneapp.incture.workbox.demo.workflow.dto.TeamDetailDto;
import oneapp.incture.workbox.demo.workflow.util.WorkflowCreationConstant;



@Component
public class WorkflowPayloadCreation {

	@Autowired
	SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public CustomProcessCreationDto prepareWorkflowPayload(List<CustomAttributeTemplateDto> cats,String processName)
	{
		
		CustomProcessCreationDto customProcessCreationDto = new CustomProcessCreationDto();
		ProcessConfigTbDto processDetail = null;
		List<CustomAttributeTemplateDto> customAttribute = null;
		List<CustomAttributeTemplateDto> customAttribute2 = null;
		CustomAttributeTemplateDto attributeTemplateDto = null;
		List<TeamDetailDto> teamDetailDto = null;
		TeamDetailDto detailDto = null;
		
		// process details
		processDetail = new ProcessConfigTbDto();
		processDetail.setCriticalDateDays(1);
		processDetail.setCriticalDateHours(1);
		processDetail.setDescription("");
		processDetail.setLabelName(processName);
		processDetail.setOrigin("Ad-hoc");
		processDetail.setProcessDisplayName(processName);
		processDetail.setProcessName(processName);
		processDetail.setProcessRequestId("");
		processDetail.setProcessType("Multiple Instance");
		processDetail.setSlaDays(2);
		processDetail.setSlaHours(0);
		processDetail.setSubject("");
		processDetail.setUrl("");
		customProcessCreationDto.setProcessDetail(processDetail);
		
		customAttribute = new ArrayList<CustomAttributeTemplateDto>();
		for (CustomAttributeTemplateDto cat : cats) {
			attributeTemplateDto = new CustomAttributeTemplateDto();
			if("Role".equals(cat.getKey()))
				attributeTemplateDto.setDataType("DROPDOWN");
			else
				attributeTemplateDto.setDataType("INPUT");
			attributeTemplateDto.setDescription("");
			attributeTemplateDto.setIsActive(true);
			attributeTemplateDto.setIsDeleted(false);
			attributeTemplateDto.setIsEditable(true);
			attributeTemplateDto.setIsMandatory(false);
			attributeTemplateDto.setIsVisible(true);
			if("Role".equals(cat.getKey()))
				attributeTemplateDto.setIsRunTime(true);
			else
				attributeTemplateDto.setIsRunTime(false);
			attributeTemplateDto.setKey(cat.getKey());
			attributeTemplateDto.setLabel(cat.getLabel());
			attributeTemplateDto.setProcessName(processName);
			attributeTemplateDto.setOrigin("Process");
			attributeTemplateDto.setAttributePath("");
			customAttribute.add(attributeTemplateDto);
		}
		customProcessCreationDto.setCustomAttribute(customAttribute);
		
		teamDetailDto = new ArrayList<>();
		detailDto = new TeamDetailDto();
		
		detailDto.setProcessName(processName);
		detailDto.setEventName("First Task");
		detailDto.setTemplateId("First Task");
		detailDto.setTaskType("Approve/Reject");
		detailDto.setTaskNature("User Based");
		detailDto.setIndividual(new ArrayList<>());
		detailDto.setGroup(new ArrayList<>());
		detailDto.setRunTimeUser(1);
		detailDto.setCustomKey("Role");
		detailDto.setSourceId(new ArrayList<>());
		detailDto.setTargetId(new ArrayList<>());;
		detailDto.setSubject("${Description of a Task},${Role}");
		detailDto.setDescription("${Description of a Task},${Role}");
		detailDto.setUrl(null);
		StatusDto statusDto = new StatusDto();
		statusDto.setApprove("Approved");
		statusDto.setCompleted("Completed");
		statusDto.setDone("Done");
		statusDto.setReady("Ready");
		statusDto.setReject("Rejected");
		statusDto.setReserved("Reserved");
		detailDto.setStatusDto(statusDto);
		
		customAttribute2  =new ArrayList<>();
		for (CustomAttributeTemplateDto cat : cats) {
			attributeTemplateDto = new CustomAttributeTemplateDto();
			if("Role".equals(cat.getKey()))
				attributeTemplateDto.setDataType("DROPDOWN");
			else
				attributeTemplateDto.setDataType("INPUT");
			attributeTemplateDto.setDescription("");
			attributeTemplateDto.setIsActive(true);
			attributeTemplateDto.setIsDeleted(false);
			attributeTemplateDto.setIsEditable(true);
			attributeTemplateDto.setIsMandatory(false);
			attributeTemplateDto.setIsVisible(true);
			if("Role".equals(cat.getKey()))
				attributeTemplateDto.setIsRunTime(true);
			else
				attributeTemplateDto.setIsRunTime(false);
			attributeTemplateDto.setKey(cat.getKey()+"1");
			attributeTemplateDto.setLabel(cat.getLabel());
			attributeTemplateDto.setProcessName("First Task");
			attributeTemplateDto.setAttributePath("${"+cat.getKey()+"}");
			attributeTemplateDto.setOrigin("Task");
			customAttribute2.add(attributeTemplateDto);
		}
		detailDto.setCustomAttributes(customAttribute2);
		
		teamDetailDto.add(detailDto);
		customProcessCreationDto.setTeamDetailDto(teamDetailDto);
		return customProcessCreationDto;
	}

	public String checkProcessExists(String processName) {
		String result = WorkflowCreationConstant.FAILURE;
		processName = processName.replace(" ", "");
		Query checkProcessExists = getSession().createSQLQuery(
				"SELECT COUNT(*) FROM PROCESS_CONFIG_TB " + "WHERE PROCESS_NAME in (:processName) ");
		checkProcessExists.setParameter("processName", processName);
		if (((BigInteger) checkProcessExists.uniqueResult()).intValue() > 0)
			result = WorkflowCreationConstant.SUCCESS;

		return result;
	}

	public Map<String,String> getGroupsDetails() {
		Map<String,String> groups = new HashMap<>();
		
		Query qry= this.getSession().createSQLQuery("SELECT DISTINCT GROUP_NAME,GROUP_ID FROM GROUPS");
		List<Object[]> list = qry.list();
		
		for (Object[] obj : list) {
			groups.put(obj[0].toString(), obj[1].toString());
	
		}
		return groups;
	}
	
	public Map<String,String> getGroupsDetailsNew() {
		Map<String,String> groups = new HashMap<>();
		
		Query qry= this.getSession().createSQLQuery("SELECT DISTINCT GROUP_NAME,GROUP_ID FROM GROUPS");
		List<Object[]> list = qry.list();
		
		for (Object[] obj : list) {
			groups.put(obj[1].toString(), obj[0].toString());
	
		}
		return groups;
	}
}
