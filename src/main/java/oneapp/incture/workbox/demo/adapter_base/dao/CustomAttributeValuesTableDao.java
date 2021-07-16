package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.CustomAttributeValuesTableDto;
import oneapp.incture.workbox.demo.adapter_base.dto.LayoutAttributesTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.dto.LayoutTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValuesTableDo;
import oneapp.incture.workbox.demo.adapter_base.entity.LayoutTemplateDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository
public class CustomAttributeValuesTableDao extends BaseDao<CustomAttributeValuesTableDo, CustomAttributeValuesTableDto> {

	@Override
	protected CustomAttributeValuesTableDo importDto(CustomAttributeValuesTableDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		// TODO Auto-generated method stub
		CustomAttributeValuesTableDo customAttributeValuesdo=new  CustomAttributeValuesTableDo();
		
		if(!ServicesUtil.isEmpty(fromDto.getAttributeValue()))
			customAttributeValuesdo.setAttributeValue(fromDto.getAttributeValue());
		if(!ServicesUtil.isEmpty(fromDto.getKey()))
			customAttributeValuesdo.setKey(fromDto.getKey());
		if(!ServicesUtil.isEmpty(fromDto.getProcessName()))
			customAttributeValuesdo.setProcessName(fromDto.getProcessName());
		if(!ServicesUtil.isEmpty(fromDto.getTaskId()))
			customAttributeValuesdo.setTaskId(fromDto.getTaskId());
		if(!ServicesUtil.isEmpty(fromDto.getIndex()))
			customAttributeValuesdo.setIndex(fromDto.getIndex());
		
		
		return customAttributeValuesdo;
	}

	@Override
	protected CustomAttributeValuesTableDto exportDto(CustomAttributeValuesTableDo entity) {
		// TODO Auto-generated method stub
		CustomAttributeValuesTableDto customAttributeValuesdto=new  CustomAttributeValuesTableDto();
		
		if(!ServicesUtil.isEmpty(entity.getAttributeValue()))
			customAttributeValuesdto.setAttributeValue(entity.getAttributeValue());
		if(!ServicesUtil.isEmpty(entity.getKey()))
			customAttributeValuesdto.setKey(entity.getKey());
		if(!ServicesUtil.isEmpty(entity.getProcessName()))
			customAttributeValuesdto.setProcessName(entity.getProcessName());
		if(!ServicesUtil.isEmpty(entity.getTaskId()))
			customAttributeValuesdto.setTaskId(entity.getTaskId());
		if(!ServicesUtil.isEmpty(entity.getIndex()))
			customAttributeValuesdto.setIndex(entity.getIndex());
		return customAttributeValuesdto;
	}

	@SuppressWarnings("unchecked")
	public Map<Integer, Map<String, String>> getTableDetails(String eventId) {
		Map<Integer, Map<String, String>> tableValues = null;
		Map<String, String> rowValues = null;
		Query q = this.getSession().createSQLQuery("SELECT key,attr_value,row_number FROM CUSTOM_ATTR_VALUES_TABLE WHERE TASK_ID = '"+eventId+"' order by row_number");
		List<Object[]> result = q.list();

		if(!ServicesUtil.isEmpty(result)){
			tableValues = new HashMap<Integer, Map<String,String>>();
			for (Object[] obj : result) {
				if(tableValues.containsKey((Integer)obj[2])){
					rowValues = tableValues.get((Integer)obj[2]);
					rowValues.put(obj[0].toString(), obj[1].toString());
				}else{
					rowValues = new HashMap<>();
					rowValues.put(obj[0].toString(), obj[1].toString());
					tableValues.put((Integer)obj[2], rowValues);
				}
			}
		}
		return tableValues;
	}

	public Integer getTableRowCount(String eventId) {
		try{
			Query q = this.getSession().createSQLQuery("SELECT MAX(ROW_NUMBER) FROM CUSTOM_ATTR_VALUES_TABLE WHERE TASK_ID = '"+eventId+"'");
			Object rowCount =  (Integer)q.uniqueResult();
			if(rowCount == null)
				return 0;
			return (Integer)rowCount;
		}catch (Exception e) {
			System.err.println("CustomAttributeValuesTableDao.getTableRowCount() error"+e);
			return 0;
		}
	}
	
	public Map<Integer, List<LayoutAttributesTemplateDto>> fetchTableAttributes(String taskId , String processName) {

        //List<CustomAttributeValueTableAdhocDo> adhocDos = new ArrayList<>();
        Map<Integer, List<LayoutAttributesTemplateDto>> tableValueMap = new LinkedHashMap<>();
        LayoutAttributesTemplateDto layoutAttributesTemplateDto = null;
        List<LayoutAttributesTemplateDto> list = null;
        try {
            Session session = this.getSession();
            String query  = "SELECT ROW_NUMBER , LAYOUT_ID , CAT.KEY , CAT.IS_EDITABLE , CAT.IS_MAND , CAT.IS_VISIBLE , CAT.LABEL , CAT.DATA_TYPE , ATTR_VALUE " +
                                " FROM LAYOUT_ATTRIBUTES_TEMPLATE_TABLE LATT JOIN CUSTOM_ATTR_TEMPLATE CAT ON " +
                                " LATT.KEY = CAT.PROCESS_NAME JOIN  CUSTOM_ATTR_VALUES_TABLE CAVT ON CAT.KEY = CAVT.KEY " +
                                " WHERE TASK_ID = '" + taskId + "' AND CAVT.PROCESS_NAME = '" + processName + "' ORDER BY CAVT.ROW_NUMBER , CAT.LABEL ";
            List<Object[]> result = session.createSQLQuery(query).list();
            for (Object[] object : result) {
                if(tableValueMap.containsKey((Integer)object[0])) {
                    layoutAttributesTemplateDto = new LayoutAttributesTemplateDto();
                    layoutAttributesTemplateDto.setLayoutId(ServicesUtil.isEmpty(object[1])? "" : object[1].toString());
                    layoutAttributesTemplateDto.setKey(ServicesUtil.isEmpty(object[2])? "" : object[2].toString());
                    layoutAttributesTemplateDto.setIsEditable(ServicesUtil.asBoolean(object[3]));
                    layoutAttributesTemplateDto.setIsMandatory(ServicesUtil.asBoolean(object[4]));
                    layoutAttributesTemplateDto.setIsVisible(ServicesUtil.asBoolean(object[5]));
                    layoutAttributesTemplateDto.setKeyLabel(ServicesUtil.isEmpty(object[6])? "" : object[6].toString());
                    layoutAttributesTemplateDto.setKeyType(ServicesUtil.isEmpty(object[7])? "" : object[7].toString());
                    layoutAttributesTemplateDto.setKeyValue(ServicesUtil.isEmpty(object[8])? "" : object[8].toString());
                    tableValueMap.get((Integer)object[0]).add(layoutAttributesTemplateDto);
                }
                else {
                    list = new ArrayList<>();
                    layoutAttributesTemplateDto = new LayoutAttributesTemplateDto();
                    layoutAttributesTemplateDto.setLayoutId(ServicesUtil.isEmpty(object[1])? "" : object[1].toString());
                    layoutAttributesTemplateDto.setKey(ServicesUtil.isEmpty(object[2])? "" : object[2].toString());
                    layoutAttributesTemplateDto.setIsEditable(ServicesUtil.asBoolean(object[3]));
                    layoutAttributesTemplateDto.setIsMandatory(ServicesUtil.asBoolean(object[4]));
                    layoutAttributesTemplateDto.setIsVisible(ServicesUtil.asBoolean(object[5]));
                    layoutAttributesTemplateDto.setKeyLabel(ServicesUtil.isEmpty(object[6])? "" : object[6].toString());
                    layoutAttributesTemplateDto.setKeyType(ServicesUtil.isEmpty(object[7])? "" : object[7].toString());
                    layoutAttributesTemplateDto.setKeyValue(ServicesUtil.isEmpty(object[8])? "" : object[8].toString());
                    list.add(layoutAttributesTemplateDto);
                    tableValueMap.put((Integer)object[0], list);
                }
            }
//          System.err.println(NewArray);
        } catch (Exception e) {
            System.err.println("Error fetching table attributes");
            e.printStackTrace();
        }
        return tableValueMap;
    }
}
