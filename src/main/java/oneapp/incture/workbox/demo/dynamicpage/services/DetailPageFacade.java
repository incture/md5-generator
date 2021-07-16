package oneapp.incture.workbox.demo.dynamicpage.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.dynamicpage.dao.DetailPageDao;
import oneapp.incture.workbox.demo.dynamicpage.dto.DynamicPageGroupResponseDto;
import oneapp.incture.workbox.demo.dynamicpage.dto.DynamicPageResponseDto;

@Service("DetailPageFacade")
//////@Transactional
public class DetailPageFacade implements DetailPageFacadeLocal{
	
	@Autowired
	private DynamicPageFacadeLocal dynamicPage;
	
	@Autowired
	private DetailPageDao detailPage;

	@Override
	public DynamicPageResponseDto getDetailPage(String taskId, String processName, String catalogue) {
		DynamicPageResponseDto dto = new DynamicPageResponseDto();
		try{
			List<DynamicPageGroupResponseDto> list = dynamicPage.getDynamicDetailsForUser(catalogue, processName).getGroupList();
			list = detailPage.getDetailPage(list, taskId);
			if(ServicesUtil.isEmpty(list)){
				dto.setMessage("No Data Found");
				dto.setStatus(PMCConstant.SUCCESS);
				dto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}else{
				dto.setGroupList(list);
				dto.setMessage("Details Fetched Successfully");
				dto.setStatus(PMCConstant.SUCCESS);
				dto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
			
		}catch(Exception e){
			System.err.println("DetailPageFacade.getDetailPage() exception : "+e.getMessage());
			dto.setMessage("Internal Server Error");
			dto.setStatus(PMCConstant.FAILURE);
			dto.setStatusCode(PMCConstant.CODE_FAILURE);
		}
		return dto;
		
	}

	

}
