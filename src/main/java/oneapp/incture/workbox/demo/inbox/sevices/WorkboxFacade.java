package oneapp.incture.workbox.demo.inbox.sevices;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.KeyValueResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.inbox.dao.InboxFilterDao;
import oneapp.incture.workbox.demo.inbox.dao.PanelTemplateDao;
import oneapp.incture.workbox.demo.inbox.dao.PinnedTaskDao;
import oneapp.incture.workbox.demo.inbox.dao.UserCustomFilterDao;
import oneapp.incture.workbox.demo.inbox.dao.WorkBoxDao;
import oneapp.incture.workbox.demo.inbox.dto.AdvanceFilterDetailDto;
import oneapp.incture.workbox.demo.inbox.dto.FilterMetadataDto;
import oneapp.incture.workbox.demo.inbox.dto.InboxFilterDto;
import oneapp.incture.workbox.demo.inbox.dto.PanelResponseDto;
import oneapp.incture.workbox.demo.inbox.dto.PinnedTaskDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxRequestDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxRequestFilterDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxResponseDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxStoryBoardDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxTaskBoardResponseDto;

/**
 * Session Bean implementation class WorkboxFacade
 */
@Service("WorkboxFacade")
public class WorkboxFacade implements WorkboxFacadeLocal {

	
	@Autowired
	private WorkBoxDao workboxdao;

	@Autowired
	private PanelTemplateDao panelTemplateDao;

	@Autowired
	private PinnedTaskDao pinnedTaskDao;

	@Autowired
	private InboxFilterDao inboxFilterDao;

	@Autowired
	private SessionFactory sf;
	
	@Autowired
	private UserCustomFilterDao userCustomFilterDao;

	public Session getSession() {
		try {
			return sf.getCurrentSession();
		} catch (Exception ex) {
			return sf.openSession();
		}
	}

	@Override
	public WorkboxResponseDto getWorkboxFilterData(WorkboxRequestDto taskRequest, Boolean isChatBot,Token token) {
		WorkboxResponseDto responseDto = null;
		String userId = "";
		if (!ServicesUtil.isEmpty(taskRequest.getCurrentUserInfo()))
			userId = taskRequest.getCurrentUserInfo().getTaskOwner();
		if (!ServicesUtil.isEmpty(taskRequest)) {
			if (isChatBot)
				responseDto = workboxdao.getFilterData(taskRequest.getRequestId(), taskRequest.getProcessName(),
						taskRequest.getCreatedBy(), taskRequest.getCreatedAt(), taskRequest.getStatus(),
						taskRequest.getOrderBy(), taskRequest.getOrderType(), taskRequest.getSkipCount(),
						taskRequest.getMaxCount(), taskRequest.getPage(), true, "CHAT_BOT", taskRequest.getIsAdmin(),
						taskRequest.getInboxType(), taskRequest.getOrigin(), taskRequest.getInboxName());
			else if (ServicesUtil.isEmpty(taskRequest.getCustomAttributeTemplates())) {
				responseDto = workboxdao.getFilterData(taskRequest.getRequestId(), taskRequest.getProcessName(),
						taskRequest.getCreatedBy(), taskRequest.getCreatedAt(), taskRequest.getStatus(),
						taskRequest.getOrderBy(), taskRequest.getOrderType(), taskRequest.getSkipCount(),
						taskRequest.getMaxCount(), taskRequest.getPage(), false, userId, taskRequest.getIsAdmin(),
						taskRequest.getInboxType(), taskRequest.getOrigin(), taskRequest.getInboxName());
			} else {
				// filter on custom attributes
				responseDto = workboxdao.getFilterData(taskRequest.getRequestId(), taskRequest.getProcessName(),
						taskRequest.getCreatedBy(), taskRequest.getCreatedAt(), taskRequest.getStatus(),
						taskRequest.getOrderBy(), taskRequest.getOrderType(), taskRequest.getSkipCount(),
						taskRequest.getMaxCount(), taskRequest.getPage(), false, userId, taskRequest.getIsAdmin(),
						taskRequest.getInboxType(), taskRequest.getOrigin(), taskRequest.getCustomAttributeTemplates());
			}
		}
		return responseDto;
	}

	@Override
	public WorkboxResponseDto getWorkboxReport(WorkboxRequestDto taskRequest) {
		WorkboxResponseDto responseDto = null;
		String userId = "";
		if (!ServicesUtil.isEmpty(taskRequest.getCurrentUserInfo()))
			userId = taskRequest.getCurrentUserInfo().getTaskOwner();
		if (!ServicesUtil.isEmpty(taskRequest)) {
			try {
				responseDto = workboxdao.getInboxReport(taskRequest.getProcessName(), taskRequest.getStatus(),
						taskRequest.getOrderBy(), taskRequest.getOrderType(), taskRequest.getSkipCount(),
						taskRequest.getMaxCount(), taskRequest.getPage(), userId, taskRequest.getReportFilter(),
						taskRequest.getCompletedOn());
			} catch (Exception e) {
				System.err.println("[WBP-Dev][PMC][FilterData]" + e.getLocalizedMessage());
			}
		}
		return responseDto;
	}

	@Override
	public WorkboxResponseDto getWorkboxCompletedFilterData(String processName, String requestId, String createdBy,
			String createdAt, String completedAt, String period, Integer skipCount, Integer maxCount, Integer page) {
		return null;
	}

	public Statistics getStatistics() {
		return sf.getStatistics();
	}

	public FilterMetadataDto getFilterMetadata(String processName,Token token) {

		return workboxdao.getFilterMetadata(processName,token);

	}

	public KeyValueResponseDto getSelectionList(String selection) {

		return workboxdao.getSelectionList(selection);

	}

	@Override
	public PanelResponseDto getInboxPanel(String deviceType, Integer dropDownCount,Token token) {
		// TODO Auto-generated method stub
		if (!ServicesUtil.isEmpty(deviceType) && !ServicesUtil.isEmpty(dropDownCount))
			return panelTemplateDao.getInboxPanelForMobile(dropDownCount,token);
		else
			return panelTemplateDao.getInboxPanel(token);
	}

	@SuppressWarnings("unused")
	@Override
	public WorkboxResponseDto getWorkboxFilterDataNew(InboxFilterDto taskRequest, Boolean isChatBot, Token token) {
		WorkboxResponseDto responseDto = null;
		String userId = "";
		if (!ServicesUtil.isEmpty(taskRequest)) {
			responseDto = inboxFilterDao.getInboxFilterData(taskRequest,token);
		}
		return responseDto;
	}

	public ResponseMessage createPinnedTask(PinnedTaskDto pinnedTaskdto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setMessage(PMCConstant.STATUS_FAILURE);
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode("1");
		try {
			if (!ServicesUtil.isEmpty(pinnedTaskdto.getPinnedTaskId())) {
				if (pinnedTaskdto.getIsPinned().equals(true))
					responseDto = pinnedTaskDao.createPinnedTask(pinnedTaskdto,token);
				if (pinnedTaskdto.getIsPinned().equals(false))
					responseDto = pinnedTaskDao.deletePinnedTask(pinnedTaskdto,token);
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return responseDto;
	}

	@Override
	public List<Object[]> getWorkboxFilterDataForFileExport(InboxFilterDto taskRequest, Boolean isChatBot,
			String processName, String selectQueryForFileExport,Token token) {
		// TODO Auto-generated method stub
		WorkboxResponseDto responseDto = null;
		List<Object[]> resultList = null;
		String userId = "";
		if (!ServicesUtil.isEmpty(taskRequest)) {
			resultList = inboxFilterDao.getInboxFilterDataForFileExport(taskRequest, isChatBot, processName,
					selectQueryForFileExport,token);
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public WorkboxTaskBoardResponseDto getTaskBoardFilter(String inboxType, Integer page,Token token) {
		WorkboxResponseDto workboxResponseDto = null;
		InboxFilterDto taskRequest = null;
		WorkboxRequestFilterDto advanceFilter = null;
		WorkboxTaskBoardResponseDto responseDto = null;
		List<WorkboxStoryBoardDto> workboxStoryBoardDto = null;
		WorkboxStoryBoardDto storyBoardDto = null;
		try{
			ObjectMapper mapper = new ObjectMapper();
			
			List<Object[]> filterDetails = userCustomFilterDao.getTaskBoadFilters(token.getLogonName(),inboxType,page);
			BigDecimal count = userCustomFilterDao.getTaskBoadFiltersCount(token.getLogonName(),inboxType);
			workboxStoryBoardDto = new ArrayList<>();
			for (Object[] obj : filterDetails) {
				JSONObject json = new JSONObject(obj[2].toString());
				System.err.println("userCustomFilterDao"+obj[2].toString());
				taskRequest = new InboxFilterDto();
				advanceFilter = new WorkboxRequestFilterDto();
				if(json.has("inboxType"))
					taskRequest.setInboxType(json.optString("inboxType"));
				if(json.has("advanceSearch"))
					advanceFilter.setAdvanceSearch(json.optString("advanceSearch"));
				if(json.has("filterMap")){
					JSONObject jObject = new JSONObject(json.optString("filterMap"));
			        Iterator<?> keys = jObject.keys();
					Map<String, AdvanceFilterDetailDto> filterMap = mapper.readValue(json.optString("filterMap"),Map.class);
					while( keys.hasNext() ){
						String key = (String)keys.next();
			            JSONObject object = jObject.optJSONObject(key);
			            System.err.println(object.toString());
			            AdvanceFilterDetailDto value = mapper.readValue(object.toString(),AdvanceFilterDetailDto.class); 
			            filterMap.put(key, value);

			        }
					advanceFilter.setFilterMap(filterMap);
				}
				taskRequest.setAdvanceFilter(advanceFilter);
				System.err.println("[WBP-Dev]TaskBoard taskRequest"+taskRequest);
				taskRequest.setPage(1);
				workboxResponseDto = inboxFilterDao.getInboxFilterData(taskRequest,token);
				System.err.println("[WBP-Dev]TaskBoard filter"+responseDto);
				storyBoardDto = new WorkboxStoryBoardDto();
				storyBoardDto.setTrayName(obj[0].toString());
				storyBoardDto.setTrayId(obj[3].toString());
				storyBoardDto.setFilterData(obj[2].toString());
				storyBoardDto.setWorkBoxDtos(ServicesUtil.isEmpty(workboxResponseDto.getWorkBoxDtos())?new ArrayList<>():workboxResponseDto.getWorkBoxDtos());
				storyBoardDto.setTrayCount(workboxResponseDto.getCount());
				workboxStoryBoardDto.add(storyBoardDto);
			}
			responseDto = new WorkboxTaskBoardResponseDto();
			responseDto.setWorkboxStoryBoardDto(workboxStoryBoardDto);
			responseDto.setCount(count);
			
			
		}catch (Exception e) {
			System.err.println("[WBP-Dev]Error in TaskBoard filter"+e);
			e.printStackTrace();
		}
		return responseDto;
	}

	public PanelResponseDto getInboxTiles(Token token) {
		
		return panelTemplateDao.getInboxTiles(token);
	}

}
