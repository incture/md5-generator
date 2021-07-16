package oneapp.incture.workbox.demo.scpadapter.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dao.LayoutTemplateDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessContextDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskCustomAttributesTemplateDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskTemplateDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigurationDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessDefinationDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessTaskDetailsResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskAttributes;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskCustomAttributesTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskDetails;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskTemplateTableDto;
import oneapp.incture.workbox.demo.adapter_base.entity.LayoutAttributesTemplate;
import oneapp.incture.workbox.demo.adapter_base.entity.LayoutTemplateDo;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessContextEntity;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskCustomAttributesTemplate;
import oneapp.incture.workbox.demo.adapter_base.entity.TemplateTableDo;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.scpadapter.util.ProcessTemplateConfigurations;
import oneapp.incture.workbox.demo.scpadapter.util.RestUtil;
import oneapp.incture.workbox.demo.scpadapter.util.TaskDto;

@Service
//////@Transactional
public class ProcessConfigurationServiceImpl implements ProcessConfigurationService {

	@Autowired
	ProcessConfigDao processConfigDao;
	@Autowired
	TaskTemplateDao taskTemplateDao;
	@Autowired
	TaskCustomAttributesTemplateDao taskCustomAttributesTemplateDao;
	
	@Autowired
	LayoutTemplateDao layoutTemplateDao;
	
	@Autowired
	ProcessContextDao processContextDao;

	@Override
	public List<String> getAllConfiguredProcesses() {

		return processConfigDao.getAllConfiguredProcesses();
	}

	@Override
	public ProcessConfigurationDto newProcesses() {
		// get all processes in the SCP system
		List<ProcessDefinationDto> scpProcesses = getAllProcessesFromSCPSystem();
		List<String> configutedProcesses = processConfigDao.getAllConfiguredProcesses();

		for (ProcessDefinationDto dto : scpProcesses) {
			getAttributesData(dto);

		}

		// get all processes from the config table
		// find the process not in config
		// get the attributes from scp

		return new ProcessConfigurationDto(scpProcesses, configutedProcesses);
	}

	private List<TaskAttributes> getAttributesData(ProcessDefinationDto dto) {
		// TODO Auto-generated method stub

		List<TaskAttributes> attributes = new ArrayList<>();

		String[] token = getToken();

		String taskInstancesURL = "https://bpmworkflowruntimea2d6007ea-kbniwmq1aj.hana.ondemand.com/workflow-service/rest/v1/task-instances?workflowDefinitionId="
				+ dto.getProcessId() + "&$expand=attributes&$orderby=createdAt%20desc&$top=1";
		RestResponse restResponse = RestUtil.callRestService(taskInstancesURL, PMCConstant.SAML_HEADER_KEY_TI, null,
				"GET", "application/json", false, null, null, null, token[0], token[1]);

		if (!ServicesUtil.isEmpty(restResponse.getResponseCode()) && restResponse.getResponseCode() >= 200
				&& restResponse.getResponseCode() <= 207) {

			JSONArray processArray = ServicesUtil.isEmpty(restResponse.getResponseObject()) ? null
					: (JSONArray) restResponse.getResponseObject();

			for (Object taskObj : processArray) {

				JSONObject taskJson = (JSONObject) taskObj;

				String definationId[] = taskJson.getString("definitionId").split("@");
				dto.setTaskName(definationId[0]);
				for (Object attrObj : taskJson.getJSONArray("attributes")) {
					TaskAttributes att = new TaskAttributes();
					JSONObject attrJson = (JSONObject) attrObj;
					att.setId(attrJson.getString("id"));
					att.setLabel(attrJson.getString("label"));
					att.setType(attrJson.getString("type"));

					System.out.println("ProcessConfigurationServiceImpl.getAttributesData() " + att);
					attributes.add(att);
				}
				dto.setAttributes(attributes);
				break;
			}
		}

		return attributes;
	}

	// public static void main(String[] args) {
	// System.err.println("ProcessConfigurationServiceImpl.main()"+getAttributesData("analyst_appproval_process"));
	// }

	private List<ProcessDefinationDto> getAllProcessesFromSCPSystem() {

		List<ProcessDefinationDto> allprocesses = new ArrayList<ProcessDefinationDto>();
		ProcessDefinationDto process = new ProcessDefinationDto();
		String[] token = getToken();

		String workflowDefinationsURL = "https://bpmworkflowruntimea2d6007ea-kbniwmq1aj.hana.ondemand.com/workflow-service/rest/v1/workflow-definitions";

		RestResponse restResponse = RestUtil.callRestService(workflowDefinationsURL, PMCConstant.SAML_HEADER_KEY_TI,
				null, "GET", "application/json", true, null, null, null, token[0], token[1]);
		if (!ServicesUtil.isEmpty(restResponse.getResponseCode()) && restResponse.getResponseCode() >= 200
				&& restResponse.getResponseCode() <= 207) {
			process = new ProcessDefinationDto();

			JSONArray processArray = ServicesUtil.isEmpty(restResponse.getResponseObject()) ? null
					: (JSONArray) restResponse.getResponseObject();

			for (Object processObj : processArray) {
				JSONObject processJson = (JSONObject) processObj;
				process = new ProcessDefinationDto();

				process.setProcessId(processJson.getString("id"));
				process.setProcessName(processJson.getString("name"));
				process.setCreatedBy(processJson.getString("createdBy"));
				process.setCreatedAt(processJson.getString("createdAt"));
				System.err
						.println("ProcessConfigurationServiceImpl.getAllProcessesFromSCPSystem() process : " + process);

				allprocesses.add(process);

			}
		}

		return allprocesses;
	}

	private static String[] getToken() {
		String[] result = new String[2];
		try {
			// String requestUrl =
			// "https://oauthasservices-kbniwmq1aj.hana.ondemand.com/oauth2/api/v1/token?grant_type=client_credentials";

			String requestUrl = "https://oauthasservices-kbniwmq1aj.hana.ondemand.com/oauth2/api/v1/token?grant_type=client_credentials";// PropertiesConstantsStatic.NETWORK_REQUEST_URL;
			String userId = "e5422b86-1f6f-33a6-a6b6-2bd5cabde2a1";// PropertiesConstantsStatic.NETWORK_USER_ID;
			String password = "Workbox@123"; // PropertiesConstantsStatic.NETWORK_PASSWORD;

			Object responseObject = RestUtil.callRestService(requestUrl, null, null, "POST", "application/json", false,
					null, userId, password, null, null).getResponseObject();
			JSONObject resources = ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject;
			if (resources != null) {
				result[0] = resources.optString("access_token");
				result[1] = resources.optString("token_type");
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][PMC][SubstitutionRuleFacade][getPrevRecipient][error]" + e.getMessage());
		}
		return result;
	}

	@Override
	public ResponseMessage configureProcess(String content, String processDisplayName,String sla, String origin,String critical) {
		ResponseMessage message = new ResponseMessage();
		message.setMessage(PMCConstant.FAILURE);
		message.setStatusCode(PMCConstant.CODE_FAILURE);
		message.setStatus(PMCConstant.CREATE_FAILURE);

		try {
			JSONObject modelJson = new JSONObject(content);

			List<TaskDto> tasks = new ArrayList<>();

			JSONObject contentJson = modelJson.getJSONObject("contents");

			String processName = "";
			for (String key : JSONObject.getNames(contentJson)) {
				JSONObject subContent = contentJson.getJSONObject(key);
				if ("com.sap.bpm.wfs.Model".equalsIgnoreCase(subContent.getString("classDefinition"))) {
					System.err.println("TestController.uploadFile()" + subContent.getString("classDefinition"));
					JSONObject activity = subContent.getJSONObject("activities");
					 processName = subContent.getString("id");
					System.err.println("ProcessConfigurationServiceImpl.configureProcess() process name : "+processName);
					// check for the process existance in the DB
					Boolean processExist = processConfigDao.checkForProcessExistance(processName);
					
					System.err.println("ProcessConfigurationServiceImpl.configureProcess() process name : "+processDisplayName+" process exist: "+processExist);
					if (processExist) {
						message.setMessage("Process Already Configured ");
						message.setStatusCode(PMCConstant.CODE_SUCCESS);
						message.setStatus(PMCConstant.CREATED_SUCCESS);

						return message;
					} else {
						for (String id : JSONObject.getNames(activity)) {

							tasks.add(new TaskDto(id, processName));
						}
					}
				}
			}

			List<TaskDto> userTasks = new ArrayList<>();
			for (TaskDto d : tasks) {
				JSONObject taskJson = contentJson.getJSONObject(d.getId());
				if ("com.sap.bpm.wfs.UserTask".equalsIgnoreCase(taskJson.getString("classDefinition"))) {
					System.err.println("TestController.uploadFile() task json : " + taskJson);
					d.setTaskName(taskJson.getString("id"));
					d.setTaskDisplayName(taskJson.getString("name"));
					System.err.println("TestController.uploadFile() :" + d);
					List<TaskAttributes> attributes = new ArrayList<>();
					for (Object custObj : taskJson.getJSONArray("customAttributes")) {
						JSONObject attributeJson = (JSONObject) custObj;
						TaskAttributes attr = new TaskAttributes();
						attr.setId(attributeJson.getString("id"));
						attr.setLabel(attributeJson.getString("label"));
						attr.setType(attributeJson.getString("type"));
						attributes.add(attr);
					}
					d.setAttributes(attributes);
					userTasks.add(d);
				}
			}

			// chekc for user tasks existance
			System.err.println("ProcessConfigurationServiceImpl.configureProcess() user tasks : "+userTasks);
			if (userTasks.size() > 0) {
				String parentTaskname = "";
				ProcessConfigDto processConfigDto = new ProcessConfigDto();
				List<TaskTemplateTableDto> taskTemplates = new ArrayList<>();

				List<TaskCustomAttributesTemplateDto> taskCustAttrTemplates = new ArrayList<>();

				String templateId = taskTemplateDao.getNextTemplateId();
				String layoutId=layoutTemplateDao.getNextLayoutId();
				
				Integer temIndex=1;
				Integer layoutIndex=1;
				
				
				List<LayoutTemplateDo> layouts=new ArrayList<>();
				List<LayoutAttributesTemplate> layoutAttrs=new ArrayList<>();
				List<TemplateTableDo> templateTabels=new ArrayList<>();
				
				ProcessContextEntity entity=new ProcessContextEntity();
				
				for (TaskDto taskDto : userTasks) {
					
					processConfigDto.setProcessName(taskDto.getProcessName());
					processConfigDto.setLabelName("Request Id");
					processConfigDto.setSla(ServicesUtil.isEmpty(sla) ? "120" : sla);
					processConfigDto.setProcessDisplayName(ServicesUtil.isEmpty(processDisplayName) ? taskDto.getProcessName() : processDisplayName);
					processConfigDto.setLaneCount(new Random(1000).nextInt());
					processConfigDto.setOrigin(ServicesUtil.isEmpty(origin) ? "SCP" : origin);
					processConfigDto.setCriticalDate(ServicesUtil.isEmpty(critical) ? "100" : critical);

					TaskTemplateTableDto taskTemp = new TaskTemplateTableDto();
					taskTemp.setOrigin(ServicesUtil.isEmpty(origin) ? "SCP" : origin);
					taskTemp.setParentTaskName(parentTaskname);
					taskTemp.setProcessName(taskDto.getProcessName());
					taskTemp.setTaskName(taskDto.getTaskName());

					parentTaskname = taskDto.getTaskName();

					//"TEMP"+String.format("%04d", (Integer.valueOf(templateId.substring(4))+1));

					//templateId = "TEMP000"+(Integer.valueOf(templateId.substring(4))+temIndex);
					templateId ="TEMP"+String.format("%04d", (Integer.valueOf(templateId.substring(4))+temIndex));
					temIndex++;
					
					taskTemp.setTemplateId(templateId);
					//"LYT"+String.format("%04d", (Integer.valueOf(layoutId.substring(3))+1));
					
					//layoutId = "LYT00"+(Integer.valueOf(layoutId.substring(3))+layoutIndex);
					layoutId ="LYT"+String.format("%04d", (Integer.valueOf(layoutId.substring(3))+layoutIndex));
					layoutIndex++;
					
					ProcessTemplateConfigurations tempConfigurations=generateDefaultTemplate(processName,templateId,layoutId);
					layoutIndex=layoutIndex+tempConfigurations.getLayouts().size();
					
					layouts.addAll(tempConfigurations.getLayouts());
					layoutAttrs.addAll(tempConfigurations.getLayoutAttrs());
					templateTabels.addAll(tempConfigurations.getTemplateTabels());
					
					entity=tempConfigurations.getContextEntity();
					
					taskTemplates.add(taskTemp);
					
					for (TaskAttributes attr : taskDto.getAttributes()) {
						TaskCustomAttributesTemplateDto taskcustDto = new TaskCustomAttributesTemplateDto();
						taskcustDto.setKey(attr.getId());
						taskcustDto.setLabel(attr.getLabel());
						taskcustDto.setOrigin(ServicesUtil.isEmpty(origin) ? "SCP" : origin);
						taskcustDto.setProcessName(taskDto.getProcessName());
						taskcustDto.setTaskName(taskDto.getTaskName());

						taskCustAttrTemplates.add(taskcustDto);
					}
				}
				System.err.println("ProcessConfigurationServiceImpl.configureProcess() before ");
				System.err.println("processConfigDto"+processConfigDto);
				System.err.println("taskTemplates"+taskTemplates);
				System.err.println("taskCustAttrTemplates"+taskCustAttrTemplates);
				
				processConfigDao.create(processConfigDto);
				taskTemplateDao.create(taskTemplates);
				// logic to store custom attributes of a task
				taskCustomAttributesTemplateDao.create(taskCustAttrTemplates);
				taskTemplateDao.createTemplateTables(templateTabels);
				
				layoutTemplateDao.createLayoutTemplateDos(layouts);
				layoutTemplateDao.createLayoutAttrDos(layoutAttrs);
			
				try{
				updateProcessContextData(entity);
				}catch(Exception e){
					System.err.println("ProcessConfigurationServiceImpl.configureProcess()");
				}

				//get the sample context data based on the process name 
				message.setMessage("Process Configured Successfully");
				message.setStatusCode(PMCConstant.CODE_SUCCESS);
				message.setStatus(PMCConstant.CREATED_SUCCESS);
				System.err.println("ProcessConfigurationServiceImpl.configureProcess() after ");
				return message;
			} else {

				message.setMessage("No Task Found For The Process");
				message.setStatusCode(PMCConstant.CODE_SUCCESS);
				message.setStatus(PMCConstant.CREATED_SUCCESS);

				return message;
			}

		} catch (Exception e) {
			System.err.println("ProcessConfigurationServiceImpl.configureProcess() error : " + e);
			message.setMessage("Failed to configure process . Please upload proper json file ");
			e.printStackTrace();
		}

		return message;
	}
	
	private ProcessTemplateConfigurations generateDefaultTemplate(String processName,String templateId,String layoutId) {
		
		ProcessTemplateConfigurations tempConfig=new ProcessTemplateConfigurations();
		
		List<LayoutTemplateDo> layouts=new ArrayList<>();
		List<LayoutAttributesTemplate> layoutAttrs=new ArrayList<>();
		List<TemplateTableDo> templateTabels=new ArrayList<>();
		ProcessContextEntity entity=new ProcessContextEntity();
		
		Integer layoutIndex=1;
		

		String[] token = getToken();

		String taskInstancesURL = "https://bpmworkflowruntimea2d6007ea-kbniwmq1aj.hana.ondemand.com/workflow-service/rest/v1/workflow-definitions/"+processName+"/sample-contexts/default-start-context";
			
		RestResponse restResponse = RestUtil.callRestService(taskInstancesURL, PMCConstant.SAML_HEADER_KEY_TI, null,
				"GET", "application/json", false, null, null, null, token[0], token[1]);

		if (!ServicesUtil.isEmpty(restResponse.getResponseCode()) && restResponse.getResponseCode() >= 200
				&& restResponse.getResponseCode() <= 207) {

			JSONObject jsonResp = ServicesUtil.isEmpty(restResponse.getResponseObject()) ? null
					: (JSONObject) restResponse.getResponseObject();
			
			JSONObject contextData=jsonResp.getJSONObject("content");
			
			entity.setProcessName(processName);
			entity.setContext(contextData.toString());
			
			LayoutTemplateDo layout=new LayoutTemplateDo();
			
			//"LYT"+String.format("%04d", (Integer.valueOf(layoutId.substring(3))+layoutIndex));
			
			//String layoutIdH="LYT00"+(Integer.valueOf(layoutId.substring(3))+layoutIndex);
			
			String layoutIdH="LYT"+String.format("%04d", (Integer.valueOf(layoutId.substring(3))+layoutIndex));
			layoutIndex++;
			
			
			LayoutAttributesTemplate layoutAttr=null;
			Integer attrSequence=1;
			Integer layoutSequence=1;
			Integer tempSequence=1;
			
			
			layout.setLabel("Task Details");
			layout.setLayoutId(layoutIdH);
			layout.setLayoutName("Task_Header_Section");
			layout.setLayoutType("Grid");
		    layout.setLevel("1");
		    layout.setParentLayoutName("");
		    layout.setSequence(layoutSequence.toString());
		    layout.setLayoutSize("XL3 L3 M6 S12");
		    layoutSequence++;
		    
		    TemplateTableDo temTab=new TemplateTableDo();
		    
		    temTab.setLayoutId(layoutIdH);
		    temTab.setTemplateId(templateId);
		    temTab.setSequence(tempSequence.toString());
		    tempSequence++;
		    
		    layouts.add(layout);
		    templateTabels.add(temTab);
		    
			 for(String key:JSONObject.getNames(contextData)){
				 try{
				 JSONArray array=contextData.getJSONArray(key);
				 try{
					 Boolean indexExist=false;
					 if(array.length()>1){
						 indexExist=true;
					 }
					 
					 LayoutTemplateDo layout2 = new LayoutTemplateDo();
					 
					 //"LYT"+String.format("%04d", (Integer.valueOf(layoutId.substring(3))+layoutIndex));
					 String layoutIdL="LYT"+String.format("%04d", (Integer.valueOf(layoutId.substring(3))+layoutIndex));
					 layoutIndex++;
					 
					 layout2.setLabel("Task Line Items");
					 layout2.setLayoutId(layoutIdL);
					 layout2.setLayoutName("Task_LineItem_Section");
					 layout2.setLayoutType("Table");
					 layout2.setLevel("1");
					 layout2.setParentLayoutName("");
					 layout2.setSequence(layoutSequence.toString());
					 layout2.setLayoutSize("XL3 L3 M6 S12");
					 layoutSequence++;
					 
					 TemplateTableDo temTab2 = new TemplateTableDo();
					 
					 temTab2.setLayoutId(layoutIdL);
					 temTab2.setTemplateId(templateId);
					 temTab2.setSequence(tempSequence.toString());
					 tempSequence++;
					 
					 layouts.add(layout2);
					 templateTabels.add(temTab2);
					 
					 Integer index=0;
						for (Object obj : array) {
							
							
							JSONObject lineJson = (JSONObject) obj;

							
							for (String lineKey : JSONObject.getNames(lineJson)) {

								 layoutAttr=new LayoutAttributesTemplate();
								 layoutAttr.setActionURL("");
								 layoutAttr.setHasAction(false);
								 layoutAttr.setIsEditable(false);
								 layoutAttr.setIsMandatory(false);
								 layoutAttr.setIsVisible(true);
								 layoutAttr.setKey(lineKey);
								 layoutAttr.setSourceKey(key);
								 layoutAttr.setKeyLabel(lineKey.toUpperCase());
								 layoutAttr.setKeyType("TEXT");
								 layoutAttr.setLayoutId(layoutIdL);
								 layoutAttr.setSequence(attrSequence.toString());
								 attrSequence++;
								 layoutAttr.setValueHelpId("");
								
								 if(indexExist){
									 layoutAttr.setSourceIndex(index.toString());
								 }else{
									 layoutAttr.setSourceIndex(""); 
								 }
								 
								 
								 layoutAttrs.add(layoutAttr);
								 
							}
							index++;
							
						}
				 }catch(Exception e){
					 System.err.println("ProcessConfigurationServiceImpl.generateDefaultTemplate() inner array excetion "+e);
				 }
				 }catch(Exception e){
					// String attrKey=contextData.get(key).toString();
					 layoutAttr=new LayoutAttributesTemplate();
					 layoutAttr.setActionURL("");
					 layoutAttr.setHasAction(false);
					 layoutAttr.setIsEditable(false);
					 layoutAttr.setIsMandatory(false);
					 layoutAttr.setIsVisible(true);
					 layoutAttr.setKey(key);
					 layoutAttr.setKeyLabel(key.toUpperCase());
					 layoutAttr.setKeyType("INPUT");
					 layoutAttr.setLayoutId(layoutIdH);
					 layoutAttr.setSequence(attrSequence.toString());
					 layoutAttr.setSourceIndex(""); 
					 attrSequence++;
					 layoutAttr.setValueHelpId("");
					 
					 layoutAttrs.add(layoutAttr);
				 }
				 
			 }
		}

		tempConfig.setTemplateTabels(templateTabels);
		tempConfig.setLayouts(layouts);
		tempConfig.setLayoutAttrs(layoutAttrs);
		tempConfig.setContextEntity(entity);
		
		return tempConfig;
	}

	@Override
	public String getContextData(String processName){
		String res="";
		String[] token = getToken();

		String taskInstancesURL = "https://bpmworkflowruntimea2d6007ea-kbniwmq1aj.hana.ondemand.com/workflow-service/rest/v1/workflow-definitions/"+processName+"/sample-contexts/default-start-context";
			
		RestResponse restResponse = RestUtil.callRestService(taskInstancesURL, PMCConstant.SAML_HEADER_KEY_TI, null,
				"GET", "application/json", false, null, null, null, token[0], token[1]);

		if (!ServicesUtil.isEmpty(restResponse.getResponseCode()) && restResponse.getResponseCode() >= 200
				&& restResponse.getResponseCode() <= 207) {

			JSONObject jsonResp = ServicesUtil.isEmpty(restResponse.getResponseObject()) ? null
					: (JSONObject) restResponse.getResponseObject();
			
			JSONObject contextData=jsonResp.getJSONObject("content");
			res=contextData.toString();
			
		}
		
		return res;
	}
	private String generateLayoutId() {
		String layoutId="LYT"+new Random().nextInt(10000);
		try{
		 layoutId = layoutTemplateDao.getNextLayoutId();
		layoutId = "LYT00"+(Integer.valueOf(layoutId.substring(3))+1);
		}catch(Exception e){
			System.err.println("ProcessConfigurationServiceImpl.generateLayoutId() error : "+e);
		}
		return layoutId;
	}

	@Override
   public ProcessTaskDetailsResponse getProcessTaskDetails(String processName){
		ProcessTaskDetailsResponse resp=new ProcessTaskDetailsResponse();
		 List<TaskDetails> tasks=new ArrayList<>();
		 List<TaskAttributes> attributes=new ArrayList<>();
		 
		 List<TaskCustomAttributesTemplate> list=taskCustomAttributesTemplateDao.getProcessTaskDetails(processName);
		 Map<String,List<TaskAttributes>> map=new HashMap<>();
		 String procName="";
		 String origin="";
		for (TaskCustomAttributesTemplate tca : list) {
			procName=tca.getProcessName();
			origin=tca.getOrigin();
			
			if (map.containsKey(tca.getTaskName())) {

				List<TaskAttributes> attr=map.get(tca.getTaskName());
				TaskAttributes ta = new TaskAttributes();
				ta.setId(tca.getKey());
				ta.setLabel(tca.getLabel());
				ta.setType("String");
				attr.add(ta);
				map.put(tca.getTaskName(), attr);
			} else {

				List<TaskAttributes> attr = new ArrayList<>();
				TaskAttributes ta = new TaskAttributes();
				ta.setId(tca.getKey());
				ta.setLabel(tca.getLabel());
				ta.setType("String");
				attr.add(ta);

				map.put(tca.getTaskName(), attr);

			}
		}
		
		resp.setOrigin(origin);
		resp.setProcessName(procName);
	
		for(Entry<String,List<TaskAttributes>> entry: map.entrySet()){
			TaskDetails td=new TaskDetails();
			td.setTaskName(entry.getKey());
			td.setAttributes(entry.getValue());
			
			tasks.add(td);
			
		}
		try{
			Collections.reverse(tasks); 
			
		}catch(Exception e){
			System.err.println("ProcessConfigurationServiceImpl.getProcessTaskDetails()"+e);
		}
		resp.setTasks(tasks);
		return resp;
	}

//	public static void main(String[] args) 
//    { 
//		String s="{\r\n\"activityId\": \"usertask1\",\r\n\"claimedAt\": null,\r\n\"completedAt\": null,\r\n\"createdAt\": \"2020-05-22T06:08:43.305Z\",\r\n\"description\": \"Approval of Earned Leave for Sai Tharun from 11-12-2018 to 16-12-2018.\",\r\n\"id\": \"b072dd37-9bf2-11ea-bf39-00163ea6ad8f\",\r\n\"processor\": null,\r\n\"recipientUsers\": [\r\n\"P000100\"\r\n],\r\n\"recipientGroups\": [],\r\n\"status\": \"READY\",\r\n\"subject\": \"Leave Approval For Sai Tharun\",\r\n\"workflowDefinitionId\": \"leaveapprovalmanagement\",\r\n\"workflowInstanceId\": \"b0104aca-9bf2-11ea-bf39-00163ea6ad8f\",\r\n\"priority\": \"MEDIUM\",\r\n\"dueDate\": \"2020-05-22T06:15:43.359Z\",\r\n\"createdBy\": \"P000092\",\r\n\"definitionId\": \"usertask1@leaveapprovalmanagement\",\r\n\"attributes\": [\r\n{\r\n\"id\": \"employeeName\",\r\n\"label\": \"Employee Name\",\r\n\"type\": \"string\",\r\n\"value\": \"Sai Tharun\"\r\n},\r\n{\r\n\"id\": \"employeeId\",\r\n\"label\": \"Employee Id\",\r\n\"type\": \"string\",\r\n\"value\": \"INC00718\"\r\n},\r\n{\r\n\"id\": \"requestId\",\r\n\"label\": \"Request Id\",\r\n\"type\": \"string\",\r\n\"value\": \"LA0001\"\r\n},\r\n{\r\n\"id\": \"leaveType\",\r\n\"label\": \"Leave Type\",\r\n\"type\": \"string\",\r\n\"value\": \"Earned Leave\"\r\n},\r\n{\r\n\"id\": \"leaveDays\",\r\n\"label\": \"Leave Days\",\r\n\"type\": \"string\",\r\n\"value\": \"5\"\r\n},\r\n{\r\n\"id\": \"reason\",\r\n\"label\": \"Reason\",\r\n\"type\": \"string\",\r\n\"value\": \"Leaving Hometown\"\r\n},\r\n{\r\n\"id\": \"leaveStart\",\r\n\"label\": \"Leave Start\",\r\n\"type\": \"string\",\r\n\"value\": \"11-12-2018\"\r\n},\r\n{\r\n\"id\": \"leaveEnd\",\r\n\"label\": \"Leave End\",\r\n\"type\": \"string\",\r\n\"value\": \"16-12-2018\"\r\n}\r\n],\r\n\"lastChangedAt\": \"2020-05-22T06:08:43.293Z\",\r\n\"applicationScope\": \"own\"\r\n}";
//		
//		JSONObject json=new JSONObject(s);
////		System.out.println("ProcessConfigurationServiceImpl.main()"+s.toString());
//		ProcessContextEntity entity=new ProcessContextEntity();
//		entity.setContext(s.toString());
//		System.out.println("ProcessConfigurationServiceImpl.main()"+entity);
////		Integer i=10;
////		System.out.println("ProcessConfigurationServiceImpl.main()"+i.toString());
////        // Declaring arraylist without any initial size 
////        ArrayList<Integer> arrayli = new ArrayList<Integer>(); 
////  
////        // Appending elements at the end of the list 
////        arrayli.add(new Integer(9)); 
////        arrayli.add(new Integer(145)); 
////        arrayli.add(new Integer(878)); 
////        arrayli.add(new Integer(343)); 
////        arrayli.add(new Integer(5)); 
////        JSONArray jsArray2 = new JSONArray(arrayli);
////        System.out.println("ProcessConfigurationServiceImpl.main()"+jsArray2+" \n length : "+jsArray2.length());
//    } 
//  
//    // Iterate through all the elements and print 
//    public static void printElements(ArrayList<Integer> alist) 
//    { 
//        for (int i = 0; i < alist.size(); i++) { 
//            System.out.print(alist.get(i) + " "); 
//        } 
//    }

	@Override
	public ProcessContextEntity processContextData(String processName) {
		// TODO Auto-generated method stub
		return processContextDao.getProcessContextEntity(processName);
	}

	@Override
	public String updateProcessContextData(ProcessContextEntity processName) {
		// TODO Auto-generated method stub
		return processContextDao.updateProcessContextEntity(processName);
	} 
}
