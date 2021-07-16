package oneapp.incture.workbox.demo.detailpage.dao;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javassist.expr.NewArray;
import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeValuesTableDao;
import oneapp.incture.workbox.demo.adapter_base.dto.DetailPageDto;
import oneapp.incture.workbox.demo.adapter_base.dto.DetailsPageReponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.FormLayoutTemplate;
import oneapp.incture.workbox.demo.adapter_base.dto.LayoutAttributesTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.dto.LayoutDataForFromsAndLineItems;
import oneapp.incture.workbox.demo.adapter_base.dto.LayoutTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskConfigurationDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskTemplateTableDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValueTableAdhocDo;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dao.CustomAttributeValuesTableAdhocDao;
import oneapp.incture.workbox.demo.adhocTask.util.TaskCreationConstant;
import oneapp.incture.workbox.demo.adhocTask.util.UrlsForDropdown;
import oneapp.incture.workbox.demo.detailpage.dto.CustomDetailDto;
import oneapp.incture.workbox.demo.detailpage.dto.DynamicButtonDto;
import oneapp.incture.workbox.demo.detailpage.dto.DynamicDetailDto;
import oneapp.incture.workbox.demo.detailpage.dto.DynamicPageCustomAttributesDto;
import oneapp.incture.workbox.demo.detailpage.dto.FormDetail;
import oneapp.incture.workbox.demo.scpadapter.util.AdminParse;
import oneapp.incture.workbox.demo.taskcontext.util.TaskContextConstant;

@Repository
//////@Transactional
public class DetailPageDao {

	@Autowired
	AdminParse scpAdminParse;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	CustomAttributeValuesTableAdhocDao customAttributeValuesTableAdhocDao;
	
	@Autowired
    CustomAttributeValuesTableDao customAttributeValuesTableDao;
	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}


	@SuppressWarnings({ "unchecked", "unused" })
	public CustomDetailDto getDetail(String processName, String taskId, String selectType,String origin) {

		DynamicPageCustomAttributesDto dynamicPageCustomAttributesDto=new DynamicPageCustomAttributesDto();
		CustomDetailDto customDetail = new CustomDetailDto();

		List<CustomAttributeValue> attributeValues=new ArrayList<>();
		CustomAttributeValue  customAttributeValue=null;

		List<DynamicDetailDto> allDetailObject=new ArrayList<>();
		List<CustomAttributeTemplate> lineItemcustomAttributesdummy = null;
		List<CustomAttributeTemplate> lineItemcustomAttributes = new ArrayList<>();
		List<CustomAttributeTemplate> attachmentCustomAttributes = new ArrayList<>();
		List<CustomAttributeTemplate> customAttributes = null;
		List<DynamicButtonDto> dynamicButtons = null;
		String query = "SELECT *, ROW_NUMBER() OVER (PARTITION BY KEY) AS ROW_NUM " + " FROM ( "
				+ " SELECT CV.PROCESS_NAME, CV.KEY, CT.LABEL, CT.IS_ACTIVE, CT.DATA_TYPE, CT.ATTR_DES, CT.IS_MAND,"
				+ " CT.IS_EDITABLE, CV.ATTR_VALUE, CT.ATTRIBUTE_PATH,CT.IS_VISIBLE,CT.IS_RUN_TIME,CT.RUN_TIME_TYPE , CT.SEQUENCE "
				+ " FROM CUSTOM_ATTR_TEMPLATE CT " + " INNER JOIN CUSTOM_ATTR_VALUES CV "
				+ " ON CT.PROCESS_NAME = CV.PROCESS_NAME AND CT.KEY = CV.KEY " + " WHERE CV.TASK_ID = '" + taskId + "' and IS_VISIBLE = 1 " ;
		// CT.PROCESS_NAME = '"+ processName + "' " + " AND
		if(TaskContextConstant.TASK_STORY.equals(selectType))
			query += " AND CT.DATA_TYPE <> 'LINEITEM' ";

		query += " ) ORDER BY CASE WHEN SEQUENCE IS NOT NULL THEN SEQUENCE END , CASE WHEN SEQUENCE IS NULL THEN LABEL END";

		System.err.println("[WBP-Dev]Custom Detail Query : " + query);

		List<Object[]> resultList = this.getSession().createSQLQuery(query).list();
		if (!ServicesUtil.isEmpty(resultList) && !resultList.isEmpty()) {
			dynamicPageCustomAttributesDto	= convertResultToCustomDto(resultList,processName,origin);

			customAttributes = dynamicPageCustomAttributesDto.getAttributeTemplates();
			System.err.println(new Gson().toJson(customAttributes));
			
			//only for Murphy POC
			if(!ServicesUtil.isEmpty(customAttributes)) {
				if(customAttributes.get(0).getProcessName().equals("6343f4679f565") || customAttributes.get(0).getProcessName().equals("ggf9e8g63af8")
						|| customAttributes.get(0).getProcessName().equals("4gb86hi884ffd") || customAttributes.get(0).getProcessName().equals("3700aaj8i8429")) {
					List<CustomAttributeTemplate> tempCustomAttributeTemplates = new ArrayList<>();
					List<CustomAttributeTemplate> tempLineItemsAttributeTemplates = null;
					Map<Integer, List<CustomAttributeValue>> tableValueMap = new LinkedHashMap<>();
					for(CustomAttributeTemplate customAttributeTemplate : customAttributes) {
						if(customAttributeTemplate.getDataType().equals(PMCConstant.TABLE)) {
							
							if(!ServicesUtil.isEmpty(tempCustomAttributeTemplates)) {
								allDetailObject.add(new DynamicDetailDto("Task Details", tempCustomAttributeTemplates,"TASKDETAILS"));
							}			
							tempCustomAttributeTemplates = new ArrayList<>();
							
							tableValueMap = customAttributeValuesTableAdhocDao.fetchTableAttributes(taskId , customAttributeTemplate.getKey());
							System.err.println(new Gson().toJson(tableValueMap));
							//customAttributeTemplate.setDataType("LINEITEM");
							//creating line items here
							tempLineItemsAttributeTemplates = new ArrayList<>();
							for(Integer rowNumber : tableValueMap.keySet()) {
								CustomAttributeTemplate lineItem = new CustomAttributeTemplate();
								lineItem.setDataType("LINEITEM");
								lineItem.setLabel(customAttributeTemplate.getLabel());
								lineItem.setProcessName(customAttributeTemplate.getProcessName());
								lineItem.setKey(customAttributeTemplate.getKey());
								lineItem.setIsActive(customAttributeTemplate.getIsActive());
								lineItem.setIsMandatory(customAttributeTemplate.getIsMandatory());
								lineItem.setIsEditable(customAttributeTemplate.getIsEditable());
								lineItem.setAttributeValues(tableValueMap.get(rowNumber));
								lineItem.setIsVisible(customAttributeTemplate.getIsVisible());
								tempLineItemsAttributeTemplates.add(lineItem);
							}
							allDetailObject.add(new DynamicDetailDto(customAttributeTemplate.getLabel(),tempLineItemsAttributeTemplates,"LINEITEM"));
						}
						else {
							tempCustomAttributeTemplates.add(customAttributeTemplate);
						}
					}
					if(!ServicesUtil.isEmpty(tempCustomAttributeTemplates)) {
						allDetailObject.add(new DynamicDetailDto("Task Details", tempCustomAttributeTemplates,"TASKDETAILS"));
					}
					customDetail.setDynamicDetails(allDetailObject);
					customDetail.setDynamicButtons(dynamicButtons);
					customDetail.setResponseMessage(new ResponseMessage(PMCConstant.STATUS_SUCCESS, PMCConstant.CODE_SUCCESS,
							"Custom Detail Fetch Success"));
					return customDetail;
				}
			}
			
		}

		allDetailObject.add(new DynamicDetailDto("Task Details", customAttributes,"TASKDETAILS"));

		if(!TaskContextConstant.TASK_STORY.equals(selectType)){
			String buttonQuery = "SELECT DATA_TYPE, LABEL, KEY, PROCESS_NAME FROM CUSTOM_ATTR_TEMPLATE WHERE UPPER(DATA_TYPE) = UPPER('BUTTON') AND PROCESS_NAME = '"
					+ processName + "'";
			resultList = this.getSession().createSQLQuery(buttonQuery).list();
			if (!ServicesUtil.isEmpty(resultList) && !resultList.isEmpty()) {
				dynamicButtons = convertResultToButtonDto(resultList);
			}
		}

		//check for line items
		if(!ServicesUtil.isEmpty(dynamicPageCustomAttributesDto.getLineItemAttributeTemplates())){
			lineItemcustomAttributesdummy=dynamicPageCustomAttributesDto.getLineItemAttributeTemplates();
			int i=0;
			for(CustomAttributeTemplate lineItem:lineItemcustomAttributesdummy){
				lineItemcustomAttributes = new ArrayList<>();
				System.err.println("[WBP-Dev]CustomAttributeDao.getDetail() line items : "+lineItem);
				String lineItemValueString=lineItem.getValue();
				//				CustomAttributeTemplate item=lineItem;
				//JSONArray jsonArray=new Gson().fromJson(lineItemValueString, JSONArray.class);
				JsonParser parser = new JsonParser();
				JsonElement elem = parser.parse(lineItemValueString);
				JsonArray jsonArray = elem.getAsJsonArray();
				System.err.println("DetailPageDao.getDetail() jsonArray : "+jsonArray);

				for(Object obj:jsonArray){
					i++;
					CustomAttributeTemplate item=new CustomAttributeTemplate();
					item.setDataType(lineItem.getDataType());
					item.setProcessName(lineItem.getProcessName());
					item.setKey(lineItem.getKey()+i);
					item.setIsActive(lineItem.getIsActive());
					item.setIsMandatory(lineItem.getIsMandatory());
					item.setIsEditable(lineItem.getIsEditable());
					item.setAttributePath(lineItem.getAttributePath());
					item.setIsVisible(lineItem.getIsVisible());
					item.setLabel(lineItem.getLabel());
					item.setDataType(lineItem.getDataType());
					item.setDescription(lineItem.getDescription());
					item.setOrigin(lineItem.getOrigin());
					item.setDependantOn(lineItem.getDependantOn());
					item.setIsDeleted(lineItem.getIsDeleted());
					item.setIsRunTime(lineItem.getIsRunTime());
					item.setRunTimeType(lineItem.getRunTimeType());
					item.setDefaultValue(lineItem.getDefaultValue());
					attributeValues=new ArrayList<CustomAttributeValue>();
					System.err.println("[WBP-Dev]---------------------------------");
					if(obj instanceof JsonObject){
						JsonObject jobj=(JsonObject) obj;
						System.err.println("[WBP-Dev]CustomAttributeDao.getDetail()");
						//String[] keys=JSONObject.getNames(jobj);
						List<String> keys = jobj.entrySet().stream().map(a -> a.getKey()).collect(Collectors.toList());
						for(String key: keys){
							customAttributeValue=new CustomAttributeValue();
							customAttributeValue.setAttributeValue(jobj.get(key).toString().replace('\"', ' ').trim());
							System.err.println("[WBP-Dev]key "+key +" : Value "+jobj.get(key));
							String tempKey="";
							try{
								String[] camelCaseWords = key.split("(?=[A-Z])");
								for(String str:camelCaseWords){
									String output = str.substring(0, 1).toUpperCase() + str.substring(1);
									System.err.println("DetailPageDao.main()"+output);
									tempKey=tempKey+" "+output;
								}
							}catch(Exception e){
								System.err.println("DetailPageDao.getDetail() error : "+e);
							}

							if(!ServicesUtil.isEmpty(tempKey)){
								key=tempKey.trim();
							}

							customAttributeValue.setKey(key);
							attributeValues.add(customAttributeValue);
						}

					}
					item.setAttributeValues(attributeValues);
					item.setLabel("Item "+i);

					lineItemcustomAttributes.add(item);
				}
				allDetailObject.add(new DynamicDetailDto(lineItem.getLabel(),lineItemcustomAttributes,"LINEITEM"));

			}
		}else{
			allDetailObject.add(new DynamicDetailDto("Line Item Details",lineItemcustomAttributes,"LINEITEM"));
		}

		//check for attachments
		if(!ServicesUtil.isEmpty(dynamicPageCustomAttributesDto.getAttachmentAttributeTemplates())){
			lineItemcustomAttributesdummy=dynamicPageCustomAttributesDto.getAttachmentAttributeTemplates();
			int i=0;
			for(CustomAttributeTemplate lineItem:lineItemcustomAttributesdummy){
				System.err.println("[WBP-Dev]CustomAttributeDao.getDetail() attachemnts  : "+lineItem);
				// customAttributeValue=new CustomAttributeValue();
				String attachmentlueString=lineItem.getValue();
				//				CustomAttributeTemplate item=lineItem;
				if("".equals(attachmentlueString))
				{
					CustomAttributeTemplate item=new CustomAttributeTemplate();
					item.setDataType(lineItem.getDataType());
					item.setProcessName(lineItem.getProcessName());
					item.setKey(lineItem.getKey());
					item.setIsActive(lineItem.getIsActive());
					item.setIsMandatory(lineItem.getIsMandatory());
					item.setIsEditable(lineItem.getIsEditable());
					item.setIsVisible(lineItem.getIsVisible());
					item.setAttributePath(lineItem.getAttributePath());
					attributeValues=new ArrayList<CustomAttributeValue>();
					item.setAttributeValues(attributeValues);
					item.setLabel(lineItem.getLabel());
					attachmentCustomAttributes.add(item);
					continue;
				}
				JSONArray jsonArray=new JSONArray(attachmentlueString);

				for(Object obj:jsonArray){
					i++;
					CustomAttributeTemplate item=new CustomAttributeTemplate();
					item.setDataType(lineItem.getDataType());
					item.setProcessName(lineItem.getProcessName());
					item.setKey(lineItem.getKey());
					item.setIsActive(lineItem.getIsActive());
					item.setIsMandatory(lineItem.getIsMandatory());
					item.setIsEditable(lineItem.getIsEditable());
					item.setIsVisible(lineItem.getIsVisible());
					item.setAttributePath(lineItem.getAttributePath());
					attributeValues=new ArrayList<CustomAttributeValue>();
					System.err.println("[WBP-Dev]---------------------------------");
					if(obj instanceof JSONObject){
						JSONObject jobj=(JSONObject) obj;
						System.err.println("[WBP-Dev]CustomAttributeDao.getDetail()");
						String[] keys=JSONObject.getNames(jobj);
						for(String key: keys){
							try{ 
								customAttributeValue=new CustomAttributeValue();
								customAttributeValue.setKey(key);
								customAttributeValue.setAttributeValue(jobj.get(key).toString());

							}catch (Exception e) {
								System.err.println("[WBP-Dev][WORKBOX]DETAIL PAGE ERROR"+e);
								continue;
							}
							System.err.println("[WBP-Dev]key "+key +" : Value "+jobj.get(key));
							attributeValues.add(customAttributeValue);
						}

					}
					item.setAttributeValues(attributeValues);
					item.setLabel(lineItem.getLabel());

					attachmentCustomAttributes.add(item);
				}


			}
		}
		
		//check for table attributes
		if(!ServicesUtil.isEmpty(dynamicPageCustomAttributesDto.getAttachmentAttributeTemplates())){
			
		}

		//		customDetail.setDynamicDetails(Arrays.asList(new DynamicDetailDto("Task Details", customAttributes),
		//				new DynamicDetailDto("Comment Details",
		//						Arrays.asList(new CustomAttributeTemplate("Text Area", "Comments", false, true)))));

		allDetailObject.add(new DynamicDetailDto("Attachment Details",attachmentCustomAttributes,"ATTACHMENT"));

		customDetail.setDynamicDetails(allDetailObject);
		customDetail.setDynamicButtons(dynamicButtons);
		customDetail.setResponseMessage(new ResponseMessage(PMCConstant.STATUS_SUCCESS, PMCConstant.CODE_SUCCESS,
				"Custom Detail Fetch Success"));
		return customDetail;
	}

	public static void main(String[] args) {
		String s="Date";
		String[] camelCaseWords = s.split("(?=[A-Z])");
		String temp="";
		for(String str:camelCaseWords){
			String output = str.substring(0, 1).toUpperCase() + str.substring(1);
			System.out.println("DetailPageDao.main()"+output);
			temp=temp+" "+output;
		}
		System.out.println("DetailPageDao.main()"+temp.trim());
	}
	private List<DynamicButtonDto> convertResultToButtonDto(List<Object[]> resultList) {
		List<DynamicButtonDto> dynamicButtons = new ArrayList<DynamicButtonDto>();
		DynamicButtonDto button = null;
		for (Object[] row : resultList) {
			button = new DynamicButtonDto();
			button.setButtonText(ServicesUtil.asString(row[1]));
			button.setButtonKey(ServicesUtil.asString(row[2]));
			button.setProcessName(ServicesUtil.asString(row[3]));
			button.setButtonFlag(false);
			dynamicButtons.add(button);
		}
		return dynamicButtons;
	}

	private DynamicPageCustomAttributesDto convertResultToCustomDto(List<Object[]> resultList, String processName, String origin) {
		DynamicPageCustomAttributesDto dynamicPageCustomAttributesDto=new DynamicPageCustomAttributesDto();
		List<String> customKeys = new ArrayList<>();
		List<CustomAttributeTemplate> attributeTemplates = new ArrayList<CustomAttributeTemplate>();
		List<CustomAttributeTemplate> lineItemattributeTemplates = new ArrayList<CustomAttributeTemplate>();
		List<CustomAttributeTemplate> attachemntattributeTemplates = new ArrayList<CustomAttributeTemplate>();
		List<CustomAttributeTemplate> tableItemAttributeTemplates = new ArrayList<CustomAttributeTemplate>();
		CustomAttributeTemplate attributeTemplate = null;
		Map<String, CustomAttributeTemplate> customDetails = new LinkedHashMap<String, CustomAttributeTemplate>();

		for (Object[] row : resultList) {
			if(ServicesUtil.isEmpty(row[8]))
				continue;

			attributeTemplate = new CustomAttributeTemplate();
			attributeTemplate.setProcessName(ServicesUtil.asString(row[0]));
			attributeTemplate.setKey(ServicesUtil.asString(row[1]));
			attributeTemplate.setLabel(ServicesUtil.asString(row[2]));
			attributeTemplate.setIsActive(ServicesUtil.asBoolean(row[3]));
			attributeTemplate.setIsVisible(ServicesUtil.asBoolean(row[10]));
			attributeTemplate.setDataType(ServicesUtil.asString(row[4]));
			attributeTemplate.setDescription(ServicesUtil.asString(row[5]));
			attributeTemplate.setIsMandatory(ServicesUtil.asBoolean(row[6]));
			attributeTemplate.setIsEditable(ServicesUtil.asBoolean(row[7]));
			attributeTemplate.setAttributePath(ServicesUtil.asString(row[9]));
			if(!ServicesUtil.isEmpty(row[11]))
				attributeTemplate.setIsRunTime(ServicesUtil.asBoolean(row[11]));
			else
				attributeTemplate.setIsRunTime(false);
			
			attributeTemplate.setRunTimeType(ServicesUtil.asString(row[12]));
			
			if(attributeTemplate.getIsEditable() && PMCConstant.ADHOC.equals(origin) && PMCConstant.DROPDOWN.equals(attributeTemplate.getDataType())){
				attributeTemplate.setUrl(UrlsForDropdown.getDropdownUrl(attributeTemplate.getLabel(), 
						attributeTemplate.getKey(),attributeTemplate.getIsRunTime() ,attributeTemplate.getRunTimeType()));
				
				String attributePath = ServicesUtil.isEmpty(attributeTemplate.getAttributePath())?"":attributeTemplate.getAttributePath();
				attributePath = attributePath.replace("${", "").replace("}", "");
//				if(customKeys.contains(attributePath))
//					attributeTemplate.setUrl(UrlsForDropdown.getDropdownUrl(attributeTemplate.getLabel(), attributePath, customKeys));
	
				if(customKeys.contains(attributeTemplate.getKey()) || customKeys.contains(attributePath))
				{
					attributeTemplate.setIsRunTime(true);
					attributeTemplate.setDropDownType(PMCConstant.INDIVIDUAL);
				}
				
				// for NOVO POC
				if("NovoPOCSampleData".equalsIgnoreCase(processName))
				{
					attributeTemplate.setDropDownType(PMCConstant.GROUP);
					attributeTemplate.setUrl("/group/getAllGroup/CUSTOM");
				}
			}
			if (ServicesUtil.asInteger(row[14]) == 1) {
				attributeTemplate.setValue(ServicesUtil.asString(row[8]));
				attributeTemplate.setAttributeValues(new ArrayList<CustomAttributeValue>(
						Arrays.asList(new CustomAttributeValue(attributeTemplate.getProcessName(),
								attributeTemplate.getKey(), attributeTemplate.getValue()))));

				//check for line items 
				if(attributeTemplate.getDataType().equalsIgnoreCase("LINEITEM")){
					lineItemattributeTemplates.add(attributeTemplate);
				}

				else if(attributeTemplate.getDataType().equalsIgnoreCase("ATTACHMENT")){
					attachemntattributeTemplates.add(attributeTemplate);
				}
				
//				else if(attributeTemplate.getDataType().equalsIgnoreCase("TABLE")){
//					tableItemAttributeTemplates.add(attributeTemplate);
//				}
				
				else{
					customDetails.put(attributeTemplate.getProcessName() + "||" + attributeTemplate.getKey(),
							attributeTemplate);
				}

			} else if (ServicesUtil.asInteger(row[13]) > 1) {
				if(ServicesUtil.isEmpty(row[8]))
					continue;

				attributeTemplate.setValue(ServicesUtil.asString(row[8]));
				List<CustomAttributeValue> values = customDetails
						.get(attributeTemplate.getProcessName() + "||" + attributeTemplate.getKey())
						.getAttributeValues();
				values.add(new CustomAttributeValue(attributeTemplate.getProcessName(), attributeTemplate.getKey(),
						attributeTemplate.getValue()));
				attributeTemplate.setAttributeValues(values);

				//check for line items 
				if(attributeTemplate.getDataType().equalsIgnoreCase("LINEITEM")){
					lineItemattributeTemplates.add(attributeTemplate);
				}
				else if(attributeTemplate.getDataType().equalsIgnoreCase("ATTACHMENT")){
					attachemntattributeTemplates.add(attributeTemplate);
				}
//				else if(attributeTemplate.getDataType().equalsIgnoreCase("TABLE")){
//					tableItemAttributeTemplates.add(attributeTemplate);
//				}
				else{
					customDetails.put(attributeTemplate.getProcessName() + "||" + attributeTemplate.getKey(),
							attributeTemplate);
				}
			}
		}

		//this login is not required 
		for (Entry<String, CustomAttributeTemplate> entry : customDetails.entrySet()) {

			attributeTemplates.add(entry.getValue());
		}
		dynamicPageCustomAttributesDto.setAttachmentAttributeTemplates(attachemntattributeTemplates);
		dynamicPageCustomAttributesDto.setLineItemAttributeTemplates(lineItemattributeTemplates);
		dynamicPageCustomAttributesDto.setAttributeTemplates(attributeTemplates);
		return dynamicPageCustomAttributesDto;
	}

	@SuppressWarnings("unchecked")
	public DetailsPageReponseDto getAttributesDetials(String taskId) {
		DetailsPageReponseDto detailsPageReponseDto = new DetailsPageReponseDto();		
		try{

			String fetchAttributesDetail = "SELECT LATT.LAYOUT_ID,CAV.TASK_ID,CAV.PROCESS_NAME,CAV.KEY,CAV.INDEX,"
					+ "CAV.ATTR_VALUE,LATT.HAS_ACTION,LATT.ACTION_URL ,LATT.KEY_LABEL,LATT.KEY_TYPE,LTT.LAYOUT_TYPE,"
					+ "LTT.LAYOUT_NAME,LTT.LABEL,LTT.LEVEL,LTT.SEQUENCE AS LAYOUT_SEQUENCE"
					+ ",LTT.PARENT_LAYOUT_NAME,LATT.SEQUENCE AS ATTRIBUTE_SEQUENCE,LATT.IS_VISIBLE,LATT.IS_EDITABLE,"
					+ "LATT.IS_MANDATORY,LATT.VALUE_HELP_ID,LTT.LAYOUT_SIZE "
					+ "FROM CUSTOM_ATTRIBUTE_VALUES_TABLE_JABIL CAV "
					+ "INNER JOIN LAYOUT_ATTRIBUTES_TEMPLATE_TABLE LATT ON LATT.KEY = CAV.KEY "
					+ "INNER JOIN LAYOUT_TEMPLATE_TABLE LTT ON LTT.LAYOUT_ID = LATT.LAYOUT_ID "
					+ "INNER JOIN TEMPLATE_TABLE TT ON TT.LAYOUT_ID = LTT.LAYOUT_ID  OR LTT.PARENT_LAYOUT_NAME = TT.LAYOUT_ID "
					+ "INNER JOIN TASK_TEMPLATE_TABLE TTT ON TTT.TEMPLATE_ID = TT.TEMPLATE_ID "
					+ "INNER JOIN TASK_EVENTS TE ON TTT.PROCESS_NAME = TE.PROC_NAME "
					+ "WHERE TE.EVENT_ID = '"+taskId+"' AND  "
					+ "TE.NAME = TTT.TASK_NAME || '@' || TTT.PROCESS_NAME "
					+ "AND CAV.TASK_ID = '"+taskId+"' "
					+ "ORDER BY TT.SEQUENCE,LTT.LEVEL,LAYOUT_SEQUENCE,ATTRIBUTE_SEQUENCE,CAV.INDEX";

			System.err.println("[WBProduct-Dev]fetchAttributesDetail"+fetchAttributesDetail);
			List<Object[]> resultList = this.getSession().createSQLQuery(fetchAttributesDetail).list();

			if(!ServicesUtil.isEmpty(resultList)){
				detailsPageReponseDto = convertResultToDetailDto(resultList);
			}

		}catch (Exception e) {
			System.err.println("[WBProduct-Dev]get Attributes Detials"+e);
		}
		return detailsPageReponseDto;
	}

	private DetailsPageReponseDto convertResultToDetailDto(List<Object[]> resultList) {
		DetailsPageReponseDto detailsPageReponseDto = new DetailsPageReponseDto();		
		List<DetailPageDto> layouts=null;
		DetailPageDto detailPageDto = null;
		Map<String,DetailPageDto> layoutAttributeMap = new LinkedHashMap<>();
		List<LayoutAttributesTemplateDto> layoutAttributes = null;
		LayoutAttributesTemplateDto layoutAttributesTemplateDto = null;
		List<LayoutDataForFromsAndLineItems> lineItemAndFormData= null;
		LayoutDataForFromsAndLineItems fromsAndLineItems = null;
		Map<String,String> parentLayoutMap = new LinkedHashMap<>();
		Map<String,DetailPageDto> layoutAttributeMapDup = new LinkedHashMap<>();
		List<FormLayoutTemplate> formLayoutData = null;
		FormLayoutTemplate formLayoutTemplate = null;
		Map<Integer,FormDetail> formDetailMap = new HashMap<>();
		List<List<LayoutAttributesTemplateDto>> formDataAttributes=null;
		List<LayoutAttributesTemplateDto> attributesTemplateDtos = null;
		FormDetail formDetail = null;
		Map<String,List<LayoutDataForFromsAndLineItems>> lineItemMap = null;
		try{

			for (Object[] obj : resultList) {

				if(layoutAttributeMap.containsKey(obj[0])){
					detailPageDto = layoutAttributeMap.get(obj[0]);

					if(detailPageDto.getLayoutType().equalsIgnoreCase(PMCConstant.TABLE)
							|| detailPageDto.getLayoutType().equalsIgnoreCase(PMCConstant.FORMS)){
						if(!detailPageDto.getData().isEmpty()){
							lineItemAndFormData = detailPageDto.getData();

							layoutAttributesTemplateDto = new LayoutAttributesTemplateDto();
							layoutAttributesTemplateDto.setActionURL(obj[7]!=null?obj[7].toString():"");
							layoutAttributesTemplateDto.setHasAction((byte) obj[6] != 0);
							layoutAttributesTemplateDto.setIsEditable((byte) obj[18] != 0);
							layoutAttributesTemplateDto.setIsMandatory((byte) obj[19] != 0);
							layoutAttributesTemplateDto.setIsVisible((byte) obj[17] != 0);
							layoutAttributesTemplateDto.setKey(obj[3]!=null?obj[3].toString():"");
							layoutAttributesTemplateDto.setKeyLabel(obj[8]!=null?obj[8].toString():"");
							layoutAttributesTemplateDto.setKeyType(obj[9]!=null?obj[9].toString():"");
							layoutAttributesTemplateDto.setLayoutId(obj[0]!=null?obj[0].toString():"");
							layoutAttributesTemplateDto.setSequence(obj[17]!=null?obj[16].toString():"");
							layoutAttributesTemplateDto.setValueHelpId(obj[20]!=null?obj[20].toString():"");
							layoutAttributesTemplateDto.setKeyValue(obj[5]!=null?obj[5].toString():"");
							layoutAttributesTemplateDto.setIndex(obj[4]!=null?(Integer)obj[4]:null);

							if((Integer)obj[4] < 100){
								if((Integer)obj[4] <= lineItemAndFormData.size()){
									fromsAndLineItems = lineItemAndFormData.get(((Integer)obj[4]-1));
									if(fromsAndLineItems.getIndex().equals((Integer)obj[4])){
										fromsAndLineItems.getLayoutAttributes().add(layoutAttributesTemplateDto);
										lineItemAndFormData.set(((Integer)obj[4]-1), fromsAndLineItems);
									}

								}else{

									fromsAndLineItems = new LayoutDataForFromsAndLineItems();
									fromsAndLineItems.setIndex(obj[4]!=null?(Integer)obj[4]:1);
									fromsAndLineItems.setLayoutType(obj[10]!=null?obj[10].toString():"");
									layoutAttributes = new ArrayList<>();
									layoutAttributes.add(layoutAttributesTemplateDto);
									fromsAndLineItems.setLayoutAttributes(layoutAttributes);
									lineItemAndFormData.add(fromsAndLineItems);
								}
							}else{
								Integer index = (Integer)obj[4]/100;
								if(index <= lineItemAndFormData.size()){
									fromsAndLineItems = lineItemAndFormData.get((index-1));
									if(fromsAndLineItems.getIndex().equals(index)){
										fromsAndLineItems.getLayoutAttributes().add(layoutAttributesTemplateDto);
										lineItemAndFormData.set((index-1), fromsAndLineItems);
									}

									formLayoutData = fromsAndLineItems.getFormLayoutData();
									if(formLayoutData.get(0).getFormDataAttributes().size()<((Integer)obj[4]%100)+1){
										formDataAttributes = formLayoutData.get(0).getFormDataAttributes();
										attributesTemplateDtos = new ArrayList<>();
										attributesTemplateDtos.add(layoutAttributesTemplateDto);
										formDataAttributes.add(attributesTemplateDtos);
										formLayoutTemplate = new FormLayoutTemplate();
										formLayoutTemplate.setFormId(formDetailMap.get(fromsAndLineItems.getIndex()).getFormId());
										formLayoutTemplate.setFormStatus(formDetailMap.get(fromsAndLineItems.getIndex()).getFormStatus());
										formLayoutTemplate.setFormDataAttributes(formDataAttributes);
										formLayoutData.set(0, formLayoutTemplate);
										fromsAndLineItems.setFormLayoutData(formLayoutData);
										lineItemAndFormData.set((index-1), fromsAndLineItems);
									}else{
										fromsAndLineItems.getFormLayoutData().get(0).getFormDataAttributes().get((Integer)obj[4]%100)
										.add(layoutAttributesTemplateDto);
										lineItemAndFormData.set((index-1), fromsAndLineItems);
									}
								}else{

									fromsAndLineItems = new LayoutDataForFromsAndLineItems();
									formLayoutTemplate = new FormLayoutTemplate();
									fromsAndLineItems.setIndex(index!=null?index:1);
									fromsAndLineItems.setLayoutType(obj[10]!=null?obj[10].toString():"");
									layoutAttributes = new ArrayList<>();
									layoutAttributes.add(layoutAttributesTemplateDto);
									fromsAndLineItems.setLayoutAttributes(layoutAttributes);
									if(detailPageDto.getLayoutType().equalsIgnoreCase(PMCConstant.FORMS)){
										formLayoutTemplate.setFormId(formDetailMap.get(fromsAndLineItems.getIndex()).getFormId());
										formLayoutTemplate.setFormStatus(formDetailMap.get(fromsAndLineItems.getIndex()).getFormStatus());
										formDataAttributes = new ArrayList<>();
										attributesTemplateDtos = new ArrayList<>();
										attributesTemplateDtos.add(layoutAttributesTemplateDto);
										formDataAttributes.add(attributesTemplateDtos);
										formLayoutTemplate.setFormDataAttributes(formDataAttributes);
										formLayoutData = new ArrayList<>();
										formLayoutData.add(formLayoutTemplate);
										fromsAndLineItems.setFormLayoutData(formLayoutData);
									}
									lineItemAndFormData.add(fromsAndLineItems);

								}
							}

							detailPageDto.setData(lineItemAndFormData);
						}
					}else{
						layoutAttributes = detailPageDto.getLayoutAttributes();
						layoutAttributesTemplateDto = new LayoutAttributesTemplateDto();
						layoutAttributesTemplateDto.setActionURL(obj[7]!=null?obj[7].toString():"");
						layoutAttributesTemplateDto.setHasAction((byte) obj[6] != 0);
						layoutAttributesTemplateDto.setIsEditable((byte) obj[18] != 0);
						layoutAttributesTemplateDto.setIsMandatory((byte) obj[19] != 0);
						layoutAttributesTemplateDto.setIsVisible((byte) obj[17] != 0);
						layoutAttributesTemplateDto.setKey(obj[3]!=null?obj[3].toString():"");
						layoutAttributesTemplateDto.setKeyLabel(obj[8]!=null?obj[8].toString():"");
						layoutAttributesTemplateDto.setKeyType(obj[9]!=null?obj[9].toString():"");
						layoutAttributesTemplateDto.setLayoutId(obj[0]!=null?obj[0].toString():"");
						layoutAttributesTemplateDto.setSequence(obj[17]!=null?obj[16].toString():"");
						layoutAttributesTemplateDto.setValueHelpId(obj[20]!=null?obj[20].toString():"");
						layoutAttributesTemplateDto.setKeyValue(obj[5]!=null?obj[5].toString():"");
						layoutAttributesTemplateDto.setIndex(obj[4]!=null?(Integer)obj[4]:null);
						layoutAttributes.add(layoutAttributesTemplateDto);
						if("IconTabBar".equalsIgnoreCase(detailPageDto.getLayoutType())){
							if("ic_manager_approval_process".equalsIgnoreCase(obj[2].toString())){
								formDetail = new  FormDetail();
								if(formDetailMap.containsKey((Integer)obj[4])){
									formDetail = formDetailMap.get((Integer)obj[4]);
									if("formId".equals(obj[3]))
										formDetail.setFormId(obj[5].toString());
									if("formStatus".equals(obj[3]))
										formDetail.setFormStatus(obj[5].toString());
								}else{
									if("formId".equals(obj[3]))
										formDetail.setFormId(obj[5].toString());
									if("formStatus".equals(obj[3]))
										formDetail.setFormStatus(obj[5].toString());
								}
								formDetailMap.put((Integer)obj[4], formDetail);
							}
						}
						detailPageDto.setLayoutAttributes(layoutAttributes);
					}
					layoutAttributeMap.replace(obj[0].toString(), detailPageDto);
				}else{
					detailPageDto = new DetailPageDto();
					detailPageDto.setLabel(obj[12]!=null?obj[12].toString():"");
					detailPageDto.setLayoutId(obj[0]!=null?obj[0].toString():"");
					detailPageDto.setLayoutName(obj[11]!=null?obj[11].toString():"");
					detailPageDto.setLayoutType(obj[10]!=null?obj[10].toString():"");
					detailPageDto.setLevel(obj[13]!=null?obj[13].toString():"");
					detailPageDto.setParentLayoutName(obj[15]!=null?obj[15].toString():"");
					detailPageDto.setSequence(obj[14]!=null?obj[14].toString():"");
					detailPageDto.setLayoutSize(obj[21]!=null?obj[21].toString():null);
					if(!ServicesUtil.isEmpty(obj[15]) && !obj[15].toString().equals("")){
						parentLayoutMap.put(obj[0].toString(), obj[15].toString());
					}
					layoutAttributes = new ArrayList<>();
					if(detailPageDto.getLayoutType().equalsIgnoreCase(PMCConstant.TABLE)
							|| detailPageDto.getLayoutType().equalsIgnoreCase(PMCConstant.FORMS)){

						lineItemAndFormData = new ArrayList<>();
						fromsAndLineItems = new LayoutDataForFromsAndLineItems();
						formLayoutTemplate = new FormLayoutTemplate();
						formLayoutData = new ArrayList<>();
						if((Integer)obj[4] >= 100)
							fromsAndLineItems.setIndex(obj[4]!=null?((Integer)obj[4])/100:1);
						else
							fromsAndLineItems.setIndex(obj[4]!=null?(Integer)obj[4]:1);
						fromsAndLineItems.setLayoutType(obj[10]!=null?obj[10].toString():"");
						layoutAttributesTemplateDto = new LayoutAttributesTemplateDto();
						layoutAttributesTemplateDto.setActionURL(obj[7]!=null?obj[7].toString():"");
						layoutAttributesTemplateDto.setHasAction((byte) obj[6] != 0);
						layoutAttributesTemplateDto.setIsEditable((byte) obj[18] != 0);
						layoutAttributesTemplateDto.setIsMandatory((byte) obj[19] != 0);
						layoutAttributesTemplateDto.setIsVisible((byte) obj[17] != 0);
						layoutAttributesTemplateDto.setKey(obj[3]!=null?obj[3].toString():"");
						layoutAttributesTemplateDto.setKeyLabel(obj[8]!=null?obj[8].toString():"");
						layoutAttributesTemplateDto.setKeyType(obj[9]!=null?obj[9].toString():"");
						layoutAttributesTemplateDto.setLayoutId(obj[0]!=null?obj[0].toString():"");
						layoutAttributesTemplateDto.setSequence(obj[17]!=null?obj[16].toString():"");
						layoutAttributesTemplateDto.setValueHelpId(obj[20]!=null?obj[20].toString():"");
						layoutAttributesTemplateDto.setKeyValue(obj[5]!=null?obj[5].toString():"");
						layoutAttributesTemplateDto.setIndex(obj[4]!=null?(Integer)obj[4]:null);
						layoutAttributes.add(layoutAttributesTemplateDto);
						fromsAndLineItems.setLayoutAttributes(layoutAttributes);
						if(detailPageDto.getLayoutType().equalsIgnoreCase(PMCConstant.FORMS)){
							formLayoutTemplate.setFormId(formDetailMap.get(fromsAndLineItems.getIndex()).getFormId());
							formLayoutTemplate.setFormStatus(formDetailMap.get(fromsAndLineItems.getIndex()).getFormStatus());
							formDataAttributes = new ArrayList<>();
							attributesTemplateDtos = new ArrayList<>();
							attributesTemplateDtos.add(layoutAttributesTemplateDto);
							formDataAttributes.add(attributesTemplateDtos);
							formLayoutTemplate.setFormDataAttributes(formDataAttributes);
							formLayoutData = new ArrayList<>();
							formLayoutData.add(formLayoutTemplate);
							fromsAndLineItems.setFormLayoutData(formLayoutData);
						}
						lineItemAndFormData.add(fromsAndLineItems);
						detailPageDto.setData(lineItemAndFormData);

					}else{
						layoutAttributesTemplateDto = new LayoutAttributesTemplateDto();
						layoutAttributesTemplateDto.setActionURL(obj[7]!=null?obj[7].toString():"");
						layoutAttributesTemplateDto.setHasAction((byte) obj[6] != 0);
						layoutAttributesTemplateDto.setIsEditable((byte) obj[18] != 0);
						layoutAttributesTemplateDto.setIsMandatory((byte) obj[19] != 0);
						layoutAttributesTemplateDto.setIsVisible((byte) obj[17] != 0);
						layoutAttributesTemplateDto.setKey(obj[3]!=null?obj[3].toString():"");
						layoutAttributesTemplateDto.setKeyLabel(obj[8]!=null?obj[8].toString():"");
						layoutAttributesTemplateDto.setKeyType(obj[9]!=null?obj[9].toString():"");
						layoutAttributesTemplateDto.setLayoutId(obj[0]!=null?obj[0].toString():"");
						layoutAttributesTemplateDto.setSequence(obj[17]!=null?obj[16].toString():"");
						layoutAttributesTemplateDto.setValueHelpId(obj[20]!=null?obj[20].toString():"");
						layoutAttributesTemplateDto.setKeyValue(obj[5]!=null?obj[5].toString():"");
						layoutAttributesTemplateDto.setIndex(obj[4]!=null?(Integer)obj[4]:null);
						layoutAttributes.add(layoutAttributesTemplateDto);
						if("IconTabBar".equalsIgnoreCase(detailPageDto.getLayoutType())){
							if("ic_manager_approval_process".equalsIgnoreCase(obj[2].toString())){
								formDetail = new  FormDetail();
								if(formDetailMap.containsKey((Integer)obj[4])){
									formDetail = formDetailMap.get((Integer)obj[4]);
									if("formId".equals(obj[3]))
										formDetail.setFormId(obj[5].toString());
									if("formStatus".equals(obj[3]))
										formDetail.setFormStatus(obj[5].toString());
								}else{
									if("formId".equals(obj[3]))
										formDetail.setFormId(obj[5].toString());
									if("formStatus".equals(obj[3]))
										formDetail.setFormStatus(obj[5].toString());
								}
								formDetailMap.put((Integer)obj[4], formDetail);
							}
						}
						detailPageDto.setLayoutAttributes(layoutAttributes);
					}

					layoutAttributeMap.put(obj[0].toString(), detailPageDto);
				}

			}

			detailsPageReponseDto.setProcessName(resultList.get(0)[2].toString());
			detailsPageReponseDto.setTaskId(resultList.get(0)[1].toString());
			if("ic_manager_approval_process".equalsIgnoreCase(detailsPageReponseDto.getProcessName())){
				lineItemMap = new LinkedHashMap<>();
				for (Map.Entry<String,DetailPageDto> entry : layoutAttributeMap.entrySet()) {
					List<LayoutDataForFromsAndLineItems> data = null;
					if(entry.getValue().getLayoutType().equals(PMCConstant.TABLE)){
						String formId = "";
						for (LayoutDataForFromsAndLineItems layoutDataForFromsAndLineItems : entry.getValue().getData()) {
							formId = layoutDataForFromsAndLineItems.getLayoutAttributes().get(0).getKeyValue();

							if(lineItemMap.containsKey(formId)){
								lineItemMap.get(formId).add(layoutDataForFromsAndLineItems);
							}else{
								data = new ArrayList<>();
								data.add(layoutDataForFromsAndLineItems);
								lineItemMap.put(formId, data);
							}
						}
					}

					if(entry.getValue().getLayoutType().equals(PMCConstant.FORMS)){
						for (LayoutDataForFromsAndLineItems layoutDataForFromsAndLineItems : entry.getValue().getData()) {
							String formId = layoutDataForFromsAndLineItems.getFormLayoutData().get(0).getFormId();
							if(lineItemMap.containsKey(formId))
							{
								//								List<List<LayoutAttributesTemplateDto>> lineItemDataAttributes=new ArrayList<>();
								layoutAttributes = new ArrayList<>();
								data = lineItemMap.get(formId);
								for (LayoutDataForFromsAndLineItems list : data) {
									layoutDataForFromsAndLineItems.getFormLayoutData().get(0)
									.getLineItemDataAttributes().add(list.getLayoutAttributes());
								}
							}
						}
					}
				}
			}

			//for getting actions
			List<Object[]> actions = getActionDetails(detailsPageReponseDto.getTaskId());

			if(!actions.isEmpty()){
				for (Object[] obj : actions) {
					layoutAttributesTemplateDto = new LayoutAttributesTemplateDto();
					layoutAttributesTemplateDto.setActionURL(obj[2]!=null?obj[2].toString():"");
					layoutAttributesTemplateDto.setHasAction((byte) obj[1] != 0);
					layoutAttributesTemplateDto.setIsEditable((byte) obj[13] != 0);
					layoutAttributesTemplateDto.setIsMandatory((byte) obj[14] != 0);
					layoutAttributesTemplateDto.setIsVisible((byte) obj[12] != 0);
					layoutAttributesTemplateDto.setKey(obj[16]!=null?obj[16].toString():"");//
					layoutAttributesTemplateDto.setKeyLabel(obj[3]!=null?obj[3].toString():"");
					layoutAttributesTemplateDto.setKeyType(obj[4]!=null?obj[4].toString():"");
					layoutAttributesTemplateDto.setLayoutId(obj[0]!=null?obj[0].toString():"");
					layoutAttributesTemplateDto.setSequence(obj[11]!=null?obj[11].toString():"");
					layoutAttributesTemplateDto.setValueHelpId(obj[15]!=null?obj[15].toString():"");

					if(layoutAttributeMap.containsKey(obj[0])){
						detailPageDto = layoutAttributeMap.get(obj[0]);
						layoutAttributes = detailPageDto.getLayoutAttributes();
						layoutAttributes.add(layoutAttributesTemplateDto);
						detailPageDto.setLayoutAttributes(layoutAttributes);
						layoutAttributeMap.replace(obj[0].toString(), detailPageDto);
					}else{
						detailPageDto = new DetailPageDto();
						detailPageDto.setLabel(obj[7]!=null?obj[7].toString():"");
						detailPageDto.setLayoutId(obj[0]!=null?obj[0].toString():"");
						detailPageDto.setLayoutName(obj[6]!=null?obj[6].toString():"");
						detailPageDto.setLayoutType(obj[5]!=null?obj[5].toString():"");
						detailPageDto.setLevel(obj[8]!=null?obj[8].toString():"");
						detailPageDto.setParentLayoutName(obj[10]!=null?obj[10].toString():"");
						detailPageDto.setSequence(obj[9]!=null?obj[9].toString():"");
						layoutAttributes = new ArrayList<>();
						layoutAttributes.add(layoutAttributesTemplateDto);
						detailPageDto.setLayoutAttributes(layoutAttributes);
						detailPageDto.setLayoutAttributes(layoutAttributes);
						layoutAttributeMap.put(obj[0].toString(), detailPageDto);
					}
				}
			}

			layoutAttributeMapDup.putAll(layoutAttributeMap);
			Gson g = new Gson();
			System.err.println("layoutAttributeMap"+g.toJson(layoutAttributeMap));
			for (Map.Entry<String,DetailPageDto> entry : layoutAttributeMap.entrySet()) {

				if(parentLayoutMap.containsKey(entry.getKey())){
					DetailPageDto pageDto = entry.getValue();
					layoutAttributeMapDup.get(parentLayoutMap.get(entry.getKey())).
					getSubLayots().add(pageDto);

					layoutAttributeMapDup.remove(entry.getKey());
				}
			}


			layouts = new ArrayList<>();
			for(Map.Entry<String,DetailPageDto> entry : layoutAttributeMapDup.entrySet()){
				layouts.add(entry.getValue());
			}
			detailsPageReponseDto.setLayouts(layouts);

		}catch (Exception e) {
			System.err.println("WBProuct-Dev convertResultToDetailDto"+e);
		}
		Gson g = new Gson();
		System.err.println("layoutAttributeMap"+g.toJson(detailsPageReponseDto));
		return detailsPageReponseDto;

	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getActionDetails(String taskId) {
		String actionQuery = "SELECT LATT.LAYOUT_ID,LATT.HAS_ACTION,LATT.ACTION_URL ,LATT.KEY_LABEL,"
				+ "LATT.KEY_TYPE,LTT.LAYOUT_TYPE,"
				+ "LTT.LAYOUT_NAME,LTT.LABEL,LTT.LEVEL,LTT.SEQUENCE AS LAYOUT_SEQUENCE"
				+ ",LTT.PARENT_LAYOUT_NAME,LATT.SEQUENCE AS ATTRIBUTE_SEQUENCE,LATT.IS_VISIBLE,LATT.IS_EDITABLE,"
				+ "LATT.IS_MANDATORY,LATT.VALUE_HELP_ID,TT.SEQUENCE as TEMPLATE_SEQUENCE, LATT.KEY "
				+ "FROM LAYOUT_ATTRIBUTES_TEMPLATE_TABLE LATT "
				+ "INNER JOIN LAYOUT_TEMPLATE_TABLE LTT ON LTT.LAYOUT_ID = LATT.LAYOUT_ID "
				+ "INNER JOIN TEMPLATE_TABLE TT ON TT.LAYOUT_ID = LTT.LAYOUT_ID  OR LTT.PARENT_LAYOUT_NAME = TT.LAYOUT_ID "
				+ "INNER JOIN TASK_TEMPLATE_TABLE TTT ON TTT.TEMPLATE_ID = TT.TEMPLATE_ID "
				+ "INNER JOIN TASK_EVENTS TE ON TTT.PROCESS_NAME = TE.PROC_NAME "
				+ "WHERE TE.EVENT_ID = '"+taskId+"' AND  "
				+ "TE.NAME = TTT.TASK_NAME || '@' || TTT.PROCESS_NAME AND "
				+ "(LTT.LAYOUT_TYPE = 'Actions' AND LATT.KEY_TYPE = 'Button')"
				+ "ORDER BY TEMPLATE_SEQUENCE,LTT.LEVEL,LAYOUT_SEQUENCE,ATTRIBUTE_SEQUENCE";

		return this.getSession().createSQLQuery(actionQuery).list();
	}

	public TaskConfigurationDto getTaskConfiguration(String processName, String taskName) {
		TaskConfigurationDto taskConfigurationDto = new TaskConfigurationDto();
		List<TaskTemplateTableDto> taskTemplateData = new ArrayList<>();
		TaskTemplateTableDto taskTemplateTableDto = null;
		List<LayoutTemplateDto> layoutsData = null;
		Map<String,String> parentLayoutMap = new LinkedHashMap<>();
		LayoutTemplateDto layoutTemplateDto = null;
		List<LayoutAttributesTemplateDto> layoutAttributesData = null;
		LayoutAttributesTemplateDto layoutAttributesTemplateDto = null;
		Map<String,TaskTemplateTableDto> templateLayoutMap = new LinkedHashMap<>();
		Map<String,LayoutTemplateDto> layoutTemplateMap = new LinkedHashMap<>();
		Map<String,LayoutTemplateDto> layoutTemplateMapDup = new LinkedHashMap<>();
		Map<String,Set<String>> templateLayouts = new LinkedHashMap<>();
		Set<String> layoutIds = null;
		try{
			String query = "SELECT TTT.TEMPLATE_ID,TTT.TASK_NAME,TTT.PROCESS_NAME,TTT.ORIGIN,"
					+ "TTT.PARENT_TASK_NAME,LATT.LAYOUT_ID,"
					+ "LATT.KEY,LATT.KEY_LABEL,"
					+ "LATT.KEY_TYPE,LATT.HAS_ACTION,LATT.ACTION_URL,LTT.LAYOUT_TYPE,"
					+ "LTT.LAYOUT_NAME,LTT.LABEL,LTT.LEVEL,LTT.SEQUENCE AS LAYOUT_SEQUENCE"
					+ ",LTT.PARENT_LAYOUT_NAME,LATT.SEQUENCE AS ATTRIBUTE_SEQUENCE,LATT.IS_VISIBLE,LATT.IS_EDITABLE,"
					+ "LATT.IS_MANDATORY,LATT.VALUE_HELP_ID,TT.SEQUENCE as TEMPLATE_SEQUENCE,TT.LAYOUT_ID AS TEMPLATE_LAYOUT "
					+ ",LATT.SOURCE_KEY,LATT.SOURCE_INDEX,LTT.LAYOUT_SIZE,LATT.IS_RUNTIME,LATT.RUNTIME_TYPE, LTT.IS_DELETED  "
					+ "FROM LAYOUT_ATTRIBUTES_TEMPLATE_TABLE LATT "
					+ "INNER JOIN LAYOUT_TEMPLATE_TABLE LTT ON LTT.LAYOUT_ID = LATT.LAYOUT_ID "
					+ "INNER JOIN TEMPLATE_TABLE TT ON TT.LAYOUT_ID = LTT.LAYOUT_ID  OR LTT.PARENT_LAYOUT_NAME = TT.LAYOUT_ID "
					+ "INNER JOIN TASK_TEMPLATE_TABLE TTT ON TTT.TEMPLATE_ID = TT.TEMPLATE_ID WHERE LTT.IS_DELETED = 0";
			if(processName!=null)
				query = query+" AND TTT.PROCESS_NAME = '"+processName+"' ";
			if(taskName!=null){
				query = query+" AND TTT.TASK_NAME = '"+taskName+"' ";
			}

			String orderBy = "ORDER BY TEMPLATE_SEQUENCE,LTT.LEVEL,LAYOUT_SEQUENCE,ATTRIBUTE_SEQUENCE";

			@SuppressWarnings("unchecked")
			List<Object[]> resultList =  this.getSession().createSQLQuery(query+orderBy).list();
			if(!resultList.isEmpty()){

				for (Object[] obj : resultList) {
					if(!templateLayoutMap.containsKey(obj[0])){
						TaskTemplateTableDto templateTableDto = new TaskTemplateTableDto();
						templateTableDto.setTemplateId(obj[0]==null?"":obj[0].toString());
						templateTableDto.setTaskName(obj[1]==null?"":obj[1].toString());
						templateTableDto.setProcessName(obj[2]==null?"":obj[2].toString());
						templateTableDto.setParentTaskName(obj[4]==null?"":obj[4].toString());
						templateTableDto.setOrigin(obj[3]==null?"":obj[3].toString());
						templateLayoutMap.put(obj[0].toString(), templateTableDto);
						layoutIds = new LinkedHashSet();
						layoutIds.add(obj[23].toString());
						templateLayouts.put(obj[0].toString(),layoutIds);
					}else{
						layoutIds = templateLayouts.get(obj[0]);
						layoutIds.add(obj[23].toString());
						templateLayouts.replace(obj[0].toString(), layoutIds);
					}
					layoutAttributesTemplateDto = new LayoutAttributesTemplateDto();
					layoutAttributesTemplateDto.setActionURL(obj[10]!=null?obj[10].toString():"");
					layoutAttributesTemplateDto.setHasAction(ServicesUtil.asBoolean(obj[9]));
					layoutAttributesTemplateDto.setIsEditable(ServicesUtil.asBoolean(obj[19]));
					layoutAttributesTemplateDto.setIsMandatory(ServicesUtil.asBoolean(obj[20]));
					layoutAttributesTemplateDto.setIsVisible(ServicesUtil.asBoolean(obj[18]));
					layoutAttributesTemplateDto.setKey(obj[6]!=null?obj[6].toString():"");
					layoutAttributesTemplateDto.setKeyLabel(obj[7]!=null?obj[7].toString():"");
					layoutAttributesTemplateDto.setKeyType(obj[8]!=null?obj[8].toString():"");
					layoutAttributesTemplateDto.setLayoutId(obj[5]!=null?obj[5].toString():"");
					layoutAttributesTemplateDto.setSequence(obj[17]!=null?obj[17].toString():"");
					layoutAttributesTemplateDto.setValueHelpId(obj[21]!=null?obj[21].toString():"");
					layoutAttributesTemplateDto.setSourceKey(obj[24]!=null?obj[24].toString():null);
					layoutAttributesTemplateDto.setSourceIndex(obj[25]!=null?obj[25].toString():null);
					
					layoutAttributesTemplateDto.setIsRunTime(ServicesUtil.asBoolean(obj[27]));
					layoutAttributesTemplateDto.setRunTimeType(obj[28] != null ? obj[28].toString() : "");
					if(layoutAttributesTemplateDto.getKeyType().equals(TaskCreationConstant.DROPDOWN))
					    layoutAttributesTemplateDto.setUrl(UrlsForDropdown.getDropdownUrl("",layoutAttributesTemplateDto.getKey(),layoutAttributesTemplateDto.getIsRunTime(),layoutAttributesTemplateDto.getRunTimeType()));
					else
						layoutAttributesTemplateDto.setUrl("");
					if(layoutTemplateMap.containsKey(obj[5])){
						layoutTemplateMap.get(obj[5]).getLayoutAttributesData().add(layoutAttributesTemplateDto);
					}else{
						layoutTemplateDto = new LayoutTemplateDto();
						layoutTemplateDto.setLabel(obj[13]==null?"":obj[13].toString());
						layoutTemplateDto.setLayoutId(obj[5]==null?"":obj[5].toString());
						layoutTemplateDto.setLayoutName(obj[12]==null?"":obj[12].toString());
						layoutTemplateDto.setLayoutType(obj[11]==null?"":obj[11].toString());
						layoutTemplateDto.setLevel(obj[14]==null?"":obj[14].toString());
						layoutTemplateDto.setParentLayoutName(obj[16]==null?"":obj[16].toString());
						layoutTemplateDto.setSequence(obj[15]==null?"":obj[15].toString());
						layoutTemplateDto.setLayoutSize(obj[26]==null?"":obj[26].toString());
						layoutTemplateDto.setIsDeleted(ServicesUtil.asBoolean(obj[29]));

						layoutAttributesData = new ArrayList<>();

						layoutAttributesData.add(layoutAttributesTemplateDto);
						layoutTemplateDto.setLayoutAttributesData(layoutAttributesData);
						if(!ServicesUtil.isEmpty(obj[16]) && !obj[16].toString().equals("")){
							parentLayoutMap.put(obj[5].toString(), obj[16].toString());
						}
						layoutTemplateMap.put(obj[5].toString(), layoutTemplateDto);
					}
				}
			}

			layoutTemplateMapDup.putAll(layoutTemplateMap);
			Gson g = new Gson();
			System.err.println("layoutTemplateMap"+g.toJson(layoutTemplateMap));
			for (Map.Entry<String,LayoutTemplateDto> entry : layoutTemplateMap.entrySet()) {

				if(parentLayoutMap.containsKey(entry.getKey())){
					LayoutTemplateDto layoutDto = entry.getValue();
					layoutTemplateMapDup.get(parentLayoutMap.get(entry.getKey())).
					getSubLayoutsData().add(layoutDto);

					layoutTemplateMapDup.remove(entry.getKey());
				}
			}

			for (Map.Entry<String,Set<String>> entry : templateLayouts.entrySet()) {
				taskTemplateTableDto = templateLayoutMap.get(entry.getKey());
				layoutsData = new ArrayList<>();
				for (String layoutId : entry.getValue()) {
					layoutsData.add(layoutTemplateMapDup.get(layoutId));
				}
				taskTemplateTableDto.setLayoutsData(layoutsData);
				taskTemplateData.add(taskTemplateTableDto);
			}
			taskConfigurationDto.setTaskTemplateData(taskTemplateData);

		}catch (Exception e) {
			System.err.println("[WBProduct-Dev] getTaskConfiguration"+e);
		}
		Gson g = new Gson();
		System.err.println("layoutTemplateMapDup"+g.toJson(taskConfigurationDto));
		return taskConfigurationDto;
	}

	public DetailsPageReponseDto getAttributesDetialsFromContext(String taskId) {
		DetailsPageReponseDto detailsPageReponseDto = new DetailsPageReponseDto();
		List<DetailPageDto> layouts=null;
		DetailPageDto detailPageDto = null;
		Map<String,DetailPageDto> layoutAttributeMap = new LinkedHashMap<>();
		Map<String,DetailPageDto> layoutAttributeMapDup = new LinkedHashMap<>();
		Map<String,String> parentLayoutMap = new LinkedHashMap<>();
		List<LayoutAttributesTemplateDto> layoutAttributesData = null;
		LayoutAttributesTemplateDto layoutAttributesTemplateDto = null;
		List<LayoutDataForFromsAndLineItems> data=null;
		LayoutDataForFromsAndLineItems lineItems = null;
		FormLayoutTemplate formLayoutTemplate = null;
		Map<String,TaskTemplateTableDto> templateLayoutMap = new LinkedHashMap<>();
		//		Map<String,LayoutTemplateDto> layoutTemplateMap = new LinkedHashMap<>();
		//		Map<String,LayoutTemplateDto> layoutTemplateMapDup = new LinkedHashMap<>();
		Map<String,Set<String>> templateLayouts = new LinkedHashMap<>();
		Map<Integer, List<LayoutAttributesTemplateDto>> tableValueMap = new LinkedHashMap<>();
		Set<String> layoutIds = null;
		try{
			String query = "SELECT TTT.TEMPLATE_ID,TTT.TASK_NAME,TTT.PROCESS_NAME,TTT.ORIGIN,"
					+ "TTT.PARENT_TASK_NAME,LATT.LAYOUT_ID,"
					+ "LATT.KEY,LATT.KEY_LABEL,"
					+ "LATT.KEY_TYPE,LATT.HAS_ACTION,LATT.ACTION_URL,LTT.LAYOUT_TYPE,"
					+ "LTT.LAYOUT_NAME,LTT.LABEL,LTT.LEVEL,LTT.SEQUENCE AS LAYOUT_SEQUENCE"
					+ ",LTT.PARENT_LAYOUT_NAME,LATT.SEQUENCE AS ATTRIBUTE_SEQUENCE,LATT.IS_VISIBLE,LATT.IS_EDITABLE,"
					+ "LATT.IS_MANDATORY,LATT.VALUE_HELP_ID,TT.SEQUENCE as TEMPLATE_SEQUENCE,TT.LAYOUT_ID AS TEMPLATE_LAYOUT,"
					+ "LATT.SOURCE_KEY,LATT.SOURCE_INDEX,LTT.LAYOUT_SIZE,LATT.IS_RUNTIME,LATT.RUNTIME_TYPE "
					+ "FROM LAYOUT_ATTRIBUTES_TEMPLATE_TABLE LATT "
					+ "INNER JOIN LAYOUT_TEMPLATE_TABLE LTT ON LTT.LAYOUT_ID = LATT.LAYOUT_ID "
					+ "INNER JOIN TEMPLATE_TABLE TT ON TT.LAYOUT_ID = LTT.LAYOUT_ID  OR LTT.PARENT_LAYOUT_NAME = TT.LAYOUT_ID "
					+ "INNER JOIN TASK_TEMPLATE_TABLE TTT ON TTT.TEMPLATE_ID = TT.TEMPLATE_ID "
					+ "INNER JOIN TASK_EVENTS TE ON TTT.PROCESS_NAME = TE.PROC_NAME "
					+ "WHERE TE.EVENT_ID = '"+taskId+"'  AND LTT.IS_DELETED = 0 AND  "
					+ "TE.NAME = (CASE WHEN TE.ORIGIN ='Ad-hoc' THEN TTT.TASK_NAME ELSE TTT.TASK_NAME || '@' || TTT.PROCESS_NAME END) AND LATT.IS_VISIBLE = 1 ";


			String orderBy = "ORDER BY TEMPLATE_SEQUENCE,LTT.LEVEL,LAYOUT_SEQUENCE,ATTRIBUTE_SEQUENCE";

			@SuppressWarnings("unchecked")
			List<Object[]> resultList =  this.getSession().createSQLQuery(query+orderBy).list();
			String origin = "";
			if(!resultList.isEmpty()){

				for (Object[] obj : resultList) {
					if(!templateLayoutMap.containsKey(obj[0])){
						TaskTemplateTableDto templateTableDto = new TaskTemplateTableDto();
						templateTableDto.setTemplateId(obj[0]==null?"":obj[0].toString());
						templateTableDto.setTaskName(obj[1]==null?"":obj[1].toString());
						templateTableDto.setProcessName(obj[2]==null?"":obj[2].toString());
						templateTableDto.setParentTaskName(obj[4]==null?"":obj[4].toString());
						templateTableDto.setOrigin(obj[3]==null?"":obj[3].toString());
						origin=templateTableDto.getOrigin();
						templateLayoutMap.put(obj[0].toString(), templateTableDto);
						layoutIds = new LinkedHashSet();
						layoutIds.add(obj[23].toString());
						templateLayouts.put(obj[0].toString(),layoutIds);
					}else{
						layoutIds = templateLayouts.get(obj[0]);
						layoutIds.add(obj[23].toString());
						templateLayouts.replace(obj[0].toString(), layoutIds);
					}
					layoutAttributesTemplateDto = new LayoutAttributesTemplateDto();
					layoutAttributesTemplateDto.setActionURL(obj[10]!=null?obj[10].toString():"");
					layoutAttributesTemplateDto.setHasAction(ServicesUtil.asBoolean(obj[9]));
					layoutAttributesTemplateDto.setIsEditable(ServicesUtil.asBoolean(obj[19]));
					layoutAttributesTemplateDto.setIsMandatory(ServicesUtil.asBoolean(obj[20]));
					layoutAttributesTemplateDto.setIsVisible(ServicesUtil.asBoolean(obj[18]));
					layoutAttributesTemplateDto.setKey(obj[6]!=null?obj[6].toString():"");
					layoutAttributesTemplateDto.setKeyLabel(obj[7]!=null?obj[7].toString():"");
					layoutAttributesTemplateDto.setKeyType(obj[8]!=null?obj[8].toString():"");
					layoutAttributesTemplateDto.setLayoutId(obj[5]!=null?obj[5].toString():"");
					layoutAttributesTemplateDto.setSequence(obj[17]!=null?obj[17].toString():"");
					layoutAttributesTemplateDto.setValueHelpId(obj[21]!=null?obj[21].toString():"");
					layoutAttributesTemplateDto.setSourceKey(obj[24]!=null?obj[24].toString():null);
					layoutAttributesTemplateDto.setSourceIndex(obj[25]!=null?obj[25].toString():null);
					layoutAttributesTemplateDto.setIsRunTime(ServicesUtil.asBoolean(obj[27]));
					layoutAttributesTemplateDto.setRunTimeType(obj[28] != null ? obj[28].toString() : "");
					if(layoutAttributesTemplateDto.getKeyType().equals(TaskCreationConstant.DROPDOWN))
					    layoutAttributesTemplateDto.setUrl(UrlsForDropdown.getDropdownUrl("",layoutAttributesTemplateDto.getKey(),layoutAttributesTemplateDto.getIsRunTime(),layoutAttributesTemplateDto.getRunTimeType()));
					else
						layoutAttributesTemplateDto.setUrl("");

					if(layoutAttributeMap.containsKey(obj[5])){
						layoutAttributeMap.get(obj[5]).getLayoutAttributes().add(layoutAttributesTemplateDto);
					}else{
						detailPageDto = new DetailPageDto();
						detailPageDto.setLabel(obj[13]==null?"":obj[13].toString());
						detailPageDto.setLayoutId(obj[5]==null?"":obj[5].toString());
						detailPageDto.setLayoutName(obj[12]==null?"":obj[12].toString());
						detailPageDto.setLayoutType(obj[11]==null?"":obj[11].toString());
						detailPageDto.setLevel(obj[14]==null?"":obj[14].toString());
						detailPageDto.setParentLayoutName(obj[16]==null?"":obj[16].toString());
						detailPageDto.setSequence(obj[15]==null?"":obj[15].toString());
						detailPageDto.setSourceKey(obj[24]!=null?obj[24].toString():null);
						detailPageDto.setLayoutSize(obj[26]!=null?obj[26].toString():null);

						layoutAttributesData = new ArrayList<>();

						layoutAttributesData.add(layoutAttributesTemplateDto);
						detailPageDto.setLayoutAttributes(layoutAttributesData);
						if(!ServicesUtil.isEmpty(obj[16]) && !obj[16].toString().equals("")){
							parentLayoutMap.put(obj[5].toString(), obj[16].toString());
						}
						layoutAttributeMap.put(obj[5].toString(), detailPageDto);
					}
				}
			}
			Gson g = new Gson();
			System.err.println("layoutTemplateMap"+g.toJson(layoutAttributeMap));
			
			if ("Ad-hoc".equalsIgnoreCase(origin)) {
				
				Map<String, String> dataDetails = new HashMap<>();
				try {
					String query_details = "SELECT CUSTOM_ATTR_VALUES.KEY, CUSTOM_ATTR_VALUES.ATTR_VALUE "
							+ "FROM CUSTOM_ATTR_VALUES  WHERE CUSTOM_ATTR_VALUES.TASK_ID= '" + taskId + "'";
					List<Object[]> result = this.getSession().createSQLQuery(query_details).list();
					if (!result.isEmpty()) {
						for (Object[] obj1 : result) {
							dataDetails.put(obj1[0].toString(), obj1[1].toString());
						}
						for (Map.Entry<String, DetailPageDto> entry1 : layoutAttributeMap.entrySet()) {

							if ("Grid".equals(entry1.getValue().getLayoutType())) {
								for (LayoutAttributesTemplateDto obj : entry1.getValue().getLayoutAttributes()) {
									if (dataDetails.containsKey(obj.getKey()))
										obj.setKeyValue(dataDetails.get(obj.getKey()));
								}

							}
							else if ("TABLE".equalsIgnoreCase(entry1.getValue().getLayoutType())) {
                                data = new ArrayList<>();
                                
                                if(!ServicesUtil.isEmpty(entry1.getValue().getLayoutAttributes())) {
                                    String attrType = entry1.getValue().getLayoutAttributes().get(0).getKeyType();
                                    Integer index = 1;
                                    if(attrType.equals("TABLE") && entry1.getValue().getLayoutAttributes().size() == 1) {
                                        String key = entry1.getValue().getLayoutAttributes().get(0).getKey();
                                        tableValueMap = customAttributeValuesTableDao.fetchTableAttributes(taskId , key);
                                        for(Integer rowNumber : tableValueMap.keySet()) {
                                            lineItems = new LayoutDataForFromsAndLineItems();
                                            lineItems.setIndex(index);
                                            lineItems.setLayoutType(entry1.getValue().getLayoutType());
                                            lineItems.setLayoutAttributes(tableValueMap.get(rowNumber));
                                            data.add(lineItems);
                                            index++;
                                        }
                                    }
                                    else {
                                        lineItems = new LayoutDataForFromsAndLineItems();
                                        lineItems.setIndex(index);
                                        lineItems.setLayoutType(entry1.getValue().getLayoutType());
                                        List<LayoutAttributesTemplateDto> layoutAttributes = new ArrayList<>();
                                        for (LayoutAttributesTemplateDto obj : entry1.getValue().getLayoutAttributes()) {
                                            if (dataDetails.containsKey(obj.getKey()))
                                                obj.setKeyValue(dataDetails.get(obj.getKey()));
                                            
                                            layoutAttributes.add(obj);
                                        }
                                        lineItems.setLayoutAttributes(layoutAttributes);
                                        data.add(lineItems);
                                    }   
                                }
                            }
							entry1.getValue().setData(data);


						}
					}
				}
					catch (Exception e) {
						System.err.println("[WBProduct-Dev] getTaskDetails" + e);
						e.printStackTrace();
					}

				}
			
			if ("SCP".equalsIgnoreCase(origin)) {
			Object responseObject = scpAdminParse.getContextDetail(taskId);
			//			String responseObject = new String("{\"lineItems\":[{\"date\":\"20200131\",\"lineItemFormId\":\"J-6217\",\"quantity\":\"15\",\"materialNumber\":\"ASPCA-01241-01\",\"movementType\":\"Z91\",\"plant\":\"MY01\",\"workCell\":\"ARISTA\",\"analyst\":\"P000092\",\"lineItemNumber\":\"0001\",\"totalCost\":\"$45.15\",\"materialDocumentNumber\":\"5067\"},{\"date\":\"20200131\",\"lineItemFormId\":\"J-5294\",\"quantity\":\"15\",\"materialNumber\":\"ASPCA-01241-01\",\"movementType\":\"Z92\",\"plant\":\"MY01\",\"workCell\":\"ARISTA\",\"analyst\":\"P000092\",\"lineItemNumber\":\"0002\",\"totalCost\":\"$45.15\",\"materialDocumentNumber\":\"5068\"},{\"date\":\"20200131\",\"lineItemFormId\":\"J-2553\",\"quantity\":\"15\",\"materialNumber\":\"ASPCA-01241-01\",\"movementType\":\"Z91\",\"plant\":\"MY01\",\"workCell\":\"ARISTA\",\"analyst\":\"P000006\",\"lineItemNumber\":\"0003\",\"totalCost\":\"$45.15\",\"materialDocumentNumber\":\"5069\"}],\"role\":\"IC Manager Approval\",\"icManager\":\"P000006\",\"application\":\"Work Pool for IC\",\"urgency\":\"10\",\"currentStatus\":\"Pending investigation\",\"caseId\":\"MVR317\",\"forms\":[{\"formId\":\"J-6217\",\"formStatus\":\"APPROVED\",\"formData\":[{\"valueHelp\":[],\"value\":\"venky 1\",\"key\":\"What measures have been put into place to avoid further correction?\"},{\"valueHelp\":[\"Process\",\"Training\",\"Individual\",\"Tools\"],\"value\":\"Tools\",\"key\":\"What was the main reason for the system correction?\"},{\"valueHelp\":[],\"value\":\"venky 1\",\"key\":\"Please provide details of any additional training or action against an individual or group taken as a result of the correction\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"No\",\"key\":\"What was the issue raised in JOS meeting?\"},{\"valueHelp\":[],\"value\":\"venky 1\",\"key\":\"Analyst Comment\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"No\",\"key\":\"Was there a corrective action raised\"},{\"valueHelp\":[],\"value\":\"\",\"key\":\"Manager Comment\"}]}]}");
			System.err.println("restResponse"+g.toJson(responseObject));
			JSONObject jsonObject = (JSONObject)responseObject;
			//			JSONObject jsonObject = new JSONObject(responseObject);
			for (Map.Entry<String,DetailPageDto> entry1 : layoutAttributeMap.entrySet()) {

				if("Grid".equals(entry1.getValue().getLayoutType())){
					for (LayoutAttributesTemplateDto obj : entry1.getValue().getLayoutAttributes()) {
						if(ServicesUtil.isEmpty(obj.getSourceKey())){
							if(jsonObject.has(obj.getKey()))
								obj.setKeyValue(jsonObject.getString(obj.getKey()));
							else if(jsonObject.has(obj.getKeyLabel()))
								obj.setKeyValue(jsonObject.getString(obj.getKeyLabel()));
						}else{
							if(((Integer)jsonObject.optJSONArray(
									obj.getSourceKey()).length()) > Integer.parseInt(obj.getSourceIndex())){
										obj.setKeyValue(((JSONObject)jsonObject.optJSONArray(
												obj.getSourceKey()).get(Integer.parseInt(obj.getSourceIndex()))).optString("value"));
									}
						}
					}
				}else if("TABLE".equalsIgnoreCase(entry1.getValue().getLayoutType())
						&& "".equals(entry1.getValue().getLayoutAttributes().get(0).getSourceIndex())){
					Iterator<String> keys = (jsonObject).keys();
					String key = ""; 
					if(!ServicesUtil.isEmpty(entry1.getValue().getSourceKey())){
						key = entry1.getValue().getSourceKey();
					}
					else{
						while(keys.hasNext()) {
							key = keys.next();
							if(jsonObject.get(key) instanceof JSONArray) {
								System.err.println("key"+key);
								JSONArray array = (JSONArray) jsonObject.get(key);
								JSONObject object = (JSONObject) array.get(0);
								Iterator<String> innerKeys = object.keys();
								Boolean flag = false;
								while(innerKeys.hasNext()) {
									String innerKey = innerKeys.next();
									if(entry1.getValue().getLayoutAttributes().get(0)
											.getKeyLabel().equalsIgnoreCase(innerKey) || 
											entry1.getValue().getLayoutAttributes().get(0)
											.getKey().equalsIgnoreCase(innerKey)){
										flag = true;
										break;
									}
									System.err.println("innerKey"+innerKey);
								}
								if(flag.equals(true))
									break;
								else
									key = "";
							}
						}
					}

					System.err.println("Key required"+key);
					JSONArray array = jsonObject.optJSONArray(key);

					data = new ArrayList<>();
					Integer index = 1;
					List<LayoutAttributesTemplateDto> layoutAttributes = null;
					if(!ServicesUtil.isEmpty(array)){
						for (Object arrayObject : array) {
							layoutAttributes = new ArrayList<>();
							lineItems = new LayoutDataForFromsAndLineItems();
							lineItems.setIndex(index);
							lineItems.setLayoutType(entry1.getValue().getLayoutType());

							for (LayoutAttributesTemplateDto attr : entry1.getValue().getLayoutAttributes()) {
								if(((JSONObject)arrayObject).has(attr.getKeyLabel()))
									attr.setKeyValue(((JSONObject)arrayObject).optString(attr.getKeyLabel()));
								else if(((JSONObject)arrayObject).has(attr.getKey()))
									attr.setKeyValue(((JSONObject)arrayObject).optString(attr.getKey()));

								layoutAttributes.add(attr.clone());
							}
							lineItems.setLayoutAttributes(layoutAttributes);
							data.add(lineItems);
							index++;
						}
					}

					entry1.getValue().setData(data);

				}else if(("FORMS".equalsIgnoreCase(entry1.getValue().getLayoutType()) ||
						"TABLE".equalsIgnoreCase(entry1.getValue().getLayoutType())) 
						&& !"".equals(entry1.getValue().getLayoutAttributes().get(0).getSourceIndex())){

					String key = entry1.getValue().getSourceKey();

					JSONArray forms = jsonObject.optJSONArray(key);

					data = new ArrayList<>();
					Integer index = 1;
					//					List<List<LayoutAttributesTemplateDto>> formDataAttributes=new ArrayList<>();
					for (Object form : forms) {
						lineItems = new LayoutDataForFromsAndLineItems();
						lineItems.setIndex(index);
						lineItems.setLayoutType(entry1.getValue().getLayoutType());
						//						List<FormLayoutTemplate> formLayoutData=new ArrayList<>();
						//						formLayoutTemplate = new FormLayoutTemplate();

						//						for (Object obj : ((JSONObject)form).optJSONArray("formData")) {
						layoutAttributesData = new ArrayList<>();
						for (LayoutAttributesTemplateDto list : entry1.getValue().getLayoutAttributes()) {
							Integer sourceIndex = index-1;
							if(sourceIndex.toString().equals(list.getSourceIndex())){
								if(list.getKey().equalsIgnoreCase("valueHelp"))
									list.setKeyValue(((JSONObject)form).optJSONArray(list.getKey()).toString());
								else
									list.setKeyValue(((JSONObject)form).optString(list.getKey()));
								layoutAttributesData.add(list);
							}
						}
						lineItems.setLayoutAttributes(layoutAttributesData);
						data.add(lineItems);
						index++;
						//							formDataAttributes.addAll(formDataAttributes);
						//							formLayoutTemplate.setFormDataAttributes(formDataAttributes);
						//							formLayoutData.add(formLayoutTemplate);
						//						}
					}

					entry1.getValue().setData(data);
				}
			}
			}

			layoutAttributeMapDup.putAll(layoutAttributeMap);

			for (Map.Entry<String,DetailPageDto> entry : layoutAttributeMap.entrySet()) {

				if(parentLayoutMap.containsKey(entry.getKey())){
					DetailPageDto detailPage = entry.getValue();
					layoutAttributeMapDup.get(parentLayoutMap.get(entry.getKey())).
					getSubLayots().add(detailPage);

					layoutAttributeMapDup.remove(entry.getKey());
				}
			}


			layouts = new ArrayList<>();
			for(Map.Entry<String,DetailPageDto> entry : layoutAttributeMapDup.entrySet()){
				layouts.add(entry.getValue());
			}
			detailsPageReponseDto.setProcessName(resultList.get(0)[2].toString());
			detailsPageReponseDto.setTaskId(taskId);
			detailsPageReponseDto.setLayouts(layouts);
			System.err.println("layoutTemplateMapDup"+g.toJson(layoutAttributeMapDup));
		}catch (Exception e) {
			System.err.println("[WBProduct-Dev] getTaskConfiguration"+e);
			e.printStackTrace();
		}

		return detailsPageReponseDto;
	}
	
	public Boolean checkIfLayoutExists(String processName , String taskName) {
		
		Boolean flag = false;
		try{		
			String query = "SELECT TTT.TEMPLATE_ID,TTT.TASK_NAME,TTT.PROCESS_NAME,TTT.ORIGIN,"
					+ "TTT.PARENT_TASK_NAME,LATT.LAYOUT_ID,"
					+ "LATT.KEY,LATT.KEY_LABEL,"
					+ "LATT.KEY_TYPE,LATT.HAS_ACTION,LATT.ACTION_URL,LTT.LAYOUT_TYPE,"
					+ "LTT.LAYOUT_NAME,LTT.LABEL,LTT.LEVEL,LTT.SEQUENCE AS LAYOUT_SEQUENCE"
					+ ",LTT.PARENT_LAYOUT_NAME,LATT.SEQUENCE AS ATTRIBUTE_SEQUENCE,LATT.IS_VISIBLE,LATT.IS_EDITABLE,"
					+ "LATT.IS_MANDATORY,LATT.VALUE_HELP_ID,TT.SEQUENCE as TEMPLATE_SEQUENCE,TT.LAYOUT_ID AS TEMPLATE_LAYOUT "
					+ ",LATT.SOURCE_KEY,LATT.SOURCE_INDEX,LTT.LAYOUT_SIZE "
					+ "FROM LAYOUT_ATTRIBUTES_TEMPLATE_TABLE LATT "
					+ "INNER JOIN LAYOUT_TEMPLATE_TABLE LTT ON LTT.LAYOUT_ID = LATT.LAYOUT_ID "
					+ "INNER JOIN TEMPLATE_TABLE TT ON TT.LAYOUT_ID = LTT.LAYOUT_ID  OR LTT.PARENT_LAYOUT_NAME = TT.LAYOUT_ID "
					+ "INNER JOIN TASK_TEMPLATE_TABLE TTT ON TTT.TEMPLATE_ID = TT.TEMPLATE_ID WHERE LTT.IS_DELETED = 0 ";
			if(processName!=null)
				query = query+" AND TTT.PROCESS_NAME = '"+processName+"' ";
			if(taskName!=null){
				query = query+" AND TTT.TASK_NAME = '"+taskName+"' ";
			}

			String orderBy = "ORDER BY TEMPLATE_SEQUENCE,LTT.LEVEL,LAYOUT_SEQUENCE,ATTRIBUTE_SEQUENCE";

			@SuppressWarnings("unchecked")
			List<Object[]> resultList =  this.getSession().createSQLQuery(query+orderBy).list();
			if(!resultList.isEmpty()) 
				flag = true;
			
		}
		catch (Exception e) {
			System.err.println("[WBProduct-Dev] checkIfLayoutExists"+e);
		}
		return flag;
		
	}
}


