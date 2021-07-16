package oneapp.incture.workbox.demo.dynamicpage.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dto.CustomAttributeValueDto;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.dynamicpage.dto.DynamicPageGroupResponseDto;
import oneapp.incture.workbox.demo.dynamicpage.dto.ProcessDetailsMasterDto;

@Repository("dynamicDetailPageDao")
public class DetailPageDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CustomAttributeDao customAttributeDao;
	
	public List<DynamicPageGroupResponseDto> getDetailPage(List<DynamicPageGroupResponseDto> groupList, String taskId){
		List<DynamicPageGroupResponseDto> detailList = new ArrayList<DynamicPageGroupResponseDto>();
		System.err.println("DetailPageDao.getDetailPage() groupList : "+groupList);
		try{
			List<CustomAttributeValueDto> customAttributes = customAttributeDao.getCustomAttributesDetails(taskId);
			System.err.println("DetailPageDao.getDetailPage() customAttributes : "+customAttributes);
			
			if(!ServicesUtil.isEmpty(customAttributes)){
	
				for(DynamicPageGroupResponseDto dto : groupList){
					
					for(ProcessDetailsMasterDto childDto : dto.getPoFieldDetails()){
						
						if(dto.getGroupType().equalsIgnoreCase("TABLE")){
							CustomAttributeValueDto cv = customAttributes.stream().filter(a -> a.getDataType().equalsIgnoreCase("LINEITEM")).findFirst().orElse(null);
							
							if(cv != null){
								JSONArray ar = new JSONArray(cv.getAttributeValue());
								
								for(Object ob : ar){
									JSONObject jsonObj = (JSONObject) ob;
									if(jsonObj.has(childDto.getKey())){
//										childDto.setValue(jsonObj.get(childDto.getKey()).toString());// changes asked by Akshatha
										break;
									}
									
								}
							}
						}
						else{
							CustomAttributeValueDto customValue = customAttributes.stream().filter(a -> a.getKey().equalsIgnoreCase(childDto.getKey())).findFirst().orElse(null);
							
							/*if(customValue != null && customValue.getDataType().equalsIgnoreCase("LINEITEM")){
								JSONArray jsonArray = new JSONArray(customValue.getAttributeValue());
								for(Object obj : jsonArray){
									JSONObject jsonObj = new JSONObject();
									
									// If the current block gets uncommented then comment the below if block
									if(jsonObj.containsKey(childDto.getKey()) && childDto.getDataType().equalsIgnoreCase("ATTACHMENTS")){
										JSONArray array = new JSONArray(jsonObj.get("attachments"));
											for(Object object : array){
												JSONObject jsonObject = (JSONObject) object;
												if(jsonObject.containsKey(childDto.getKey())){
													childDto.setValue(jsonObject.get(childDto.getKey()).toString());
												}
											}
										}
									if(jsonObj.containsKey(childDto.getKey())){
										childDto.setValue(jsonObj.get(childDto.getKey()).toString());
										break;
									}
									}
									
							}
							if(customValue != null && customValue.getDataType().equalsIgnoreCase("ATTACHMENTS")){
								JSONArray jsonArray = new JSONArray(customValue.getAttributeValue());
								for(Object obj : jsonArray){
									JSONObject jsonObj = new JSONObject();
									if(jsonObj.containsKey(childDto.getKey())){
										childDto.setValue(jsonObj.get(childDto.getKey()).toString());
										break;
									}
								}
							}
							else{
								childDto.setValue(customValue.getAttributeValue());
							}*/
//							childDto.setValue(customValue.getAttributeValue());
						}
					}
					detailList.add(dto);
				}
			}
		}catch(Exception e){
			System.err.println("DetailPageDao.getDetailPage() exception : "+e.getMessage());
		}
		return detailList;
	}
}
