package oneapp.incture.workbox.demo.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.security.xsuaa.token.Token;
import com.sap.cloud.security.xsuaa.token.XsuaaToken;

import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.KeyValueResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.inbox.dto.FilterMetadataDto;
import oneapp.incture.workbox.demo.inbox.dto.InboxFilterDto;
import oneapp.incture.workbox.demo.inbox.dto.PanelResponseDto;
import oneapp.incture.workbox.demo.inbox.dto.PinnedTaskDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxRequestDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxResponseDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxTaskBoardResponseDto;
import oneapp.incture.workbox.demo.inbox.sevices.PersistODataService;
import oneapp.incture.workbox.demo.inbox.sevices.WorkFlowActionFacadeLocal;
import oneapp.incture.workbox.demo.inbox.sevices.WorkboxFacade;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/workbox/inbox")
public class WorkboxRest {

	@Autowired
	WorkboxFacade workbox;

	@Autowired
	PersistODataService persistODataService;

	@Autowired
	WorkFlowActionFacadeLocal workFlowAction;

	@RequestMapping(value = "/getTaskData", method = RequestMethod.GET, produces = "application/json")
	public List<TaskEventsDto> getAllTasks() {
		try {
			return persistODataService.getTaskList();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	// Service that is called for task actions
	@RequestMapping(value = "/actions", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage taskActions(@RequestBody ActionDto dto,@AuthenticationPrincipal XsuaaToken token) {
		System.err.println("[WBP-Dev][WORKBOX][WorkboxRest][action][dto]" + dto.toString());
		return workFlowAction.taskAction(dto,token);
	}

	// Old filter detail service (UNUSED)
	@RequestMapping(value = "/filterdetail", method = RequestMethod.POST, produces = "application/json")
	public WorkboxResponseDto getWorkboxFilterData(@RequestBody WorkboxRequestDto taskRequest
			,@AuthenticationPrincipal Token token) {
		System.err.println("[WBP-Dev]WorkboxRest.getWorkboxFilterData() filter request : " + taskRequest);
		return workbox.getWorkboxFilterData(taskRequest, false,token);

	}

	@RequestMapping(value = "/reportfilterdetail", method = RequestMethod.POST, produces = "application/json")
	public WorkboxResponseDto getWorkboxReportFilterData(@RequestBody WorkboxRequestDto taskRequest) {
		return workbox.getWorkboxReport(taskRequest);

	}

	@RequestMapping(value = "/completed", method = RequestMethod.GET)
	public WorkboxResponseDto getWorkboxCompletedFilterData(
			@RequestParam(value = "processName", defaultValue = "") String processName,
			@RequestParam(value = "period", defaultValue = "") String period,
			@RequestParam(value = "requestId", defaultValue = "") String requestId,
			@RequestParam(value = "createdBy", defaultValue = "") String createdBy,
			@RequestParam(value = "createdAt", defaultValue = "") String createdAt,
			@RequestParam(value = "completedAt", defaultValue = "") String completedAt,
			@RequestParam(value = "skipCount", defaultValue = "0") Integer skipCount,
			@RequestParam(value = "maxCount", defaultValue = "5") Integer maxCount,
			@RequestParam(value = "page", defaultValue = "1") Integer page) {

		return workbox.getWorkboxCompletedFilterData(processName, requestId, createdBy, createdAt, completedAt, period,
				skipCount, maxCount, page);
	}

	@Autowired
	ProcessEventsDao processEvents;
	@Autowired
	TaskEventsDao taskEvents;
	@Autowired
	TaskOwnersDao ownerEvents;

	@RequestMapping(value = "/getStatistics", method = RequestMethod.GET)
	public Statistics getStatistics() {
		return new Statistics(workbox.getStatistics().getSessionOpenCount(),
				workbox.getStatistics().getSessionCloseCount());
	}

	class Statistics {

		public Statistics(long openSessions, long closedSessions) {
			super();
			this.openSessions = openSessions;
			this.closedSessions = closedSessions;
		}

		private long openSessions;
		private long closedSessions;

		public long getOpenSessions() {
			return openSessions;
		}

		public void setOpenSessions(long openSessions) {
			this.openSessions = openSessions;
		}

		public long getClosedSessions() {
			return closedSessions;
		}

		public void setClosedSessions(long closedSessions) {
			this.closedSessions = closedSessions;
		}

		@Override
		public String toString() {
			return "Statistics [openSessions=" + openSessions + ", closedSessions=" + closedSessions + "]";
		}
	}

	@RequestMapping(value = "/sayHello", method = RequestMethod.GET, produces = "application/json")
	public WorkboxRequestDto sayHello() {
		// WorkboxFacadeLocal workbox = new WorkboxFacade();
		WorkboxRequestDto dto = new WorkboxRequestDto();
		List<String> processName = new ArrayList<>();
		processName.add("leaveapprovalprocess");
		processName.add("testworkflow");
		dto.setRequestId("RequestID");
		dto.setStatus("READY");
		dto.setProcessName(processName);
		dto.setCreatedAt("09/19/2018");
		dto.setMaxCount(5);
		dto.setOrderBy("ASC/DESC");
		dto.setOrderType("createdAt/dueDate");
		dto.setCreatedBy("created by ID");
		dto.setSkipCount(0);
		dto.setPage(1);

		TaskOwnersDto userdto = new TaskOwnersDto();
		userdto.setTaskOwner("P000035");
		userdto.setOwnerEmail("rtwk1001@gmail.com");
		userdto.setTaskOwnerDisplayName("Ritwik Jain");
		dto.setCurrentUserInfo(userdto);
		return dto;
		// return "Hello From Inbox!";

	}

	// Gets the metadata to prepare advancedFilter layout on Ui
	@RequestMapping(value = "/getMetadata", method = RequestMethod.GET)
	public FilterMetadataDto getFilterMetadata(
			@RequestParam(name = "processName", required = false) String processName
			,@AuthenticationPrincipal Token token) {

		return workbox.getFilterMetadata(processName,token);

	}

	// Gets the selection/conditionsin metadata of advancedFilter
	@RequestMapping(value = "/selectionList", method = RequestMethod.GET)
	public KeyValueResponseDto getSelectionlist(
			@RequestParam(name = "selectionParameter", required = false) String selection) {

		return workbox.getSelectionList(selection);

	}

	@RequestMapping(value = "/inboxPanel", method = RequestMethod.GET, produces = "application/json")
	public PanelResponseDto getInboxPanel(@RequestParam(name="deviceType",required=false) String deviceType,
			@RequestParam(name="dropDownCount",required=false) Integer dropDownCount
			,@AuthenticationPrincipal Token token) {
		System.err.println("[WBP-Dev]WorkboxRest.getInboxPanel() ");
		return workbox.getInboxPanel(deviceType,dropDownCount,token);

	}

	@RequestMapping(value = "/filterService", method = RequestMethod.POST, produces = "application/json")
	public WorkboxResponseDto getWorkboxFilterDataNew(@RequestBody InboxFilterDto taskRequest
			,@AuthenticationPrincipal Token token) {
		System.err.println("[WBP-Dev]WorkboxRest.getWorkboxFilterData() filter request : " + taskRequest);
		return workbox.getWorkboxFilterDataNew(taskRequest, false,token);

	}

	@RequestMapping(value = "/pinned", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage createPinnedTask(@RequestBody PinnedTaskDto pinnedTaskdto
			,@AuthenticationPrincipal Token token) {
		System.err.println("[WBP-Dev]WorkboxRest.addPinnedTask() pinnedTask : " + pinnedTaskdto);
		return workbox.createPinnedTask(pinnedTaskdto,token);

	}
	
	@RequestMapping(value = "/filterService", method = RequestMethod.GET, produces = "application/json")
	public WorkboxTaskBoardResponseDto getTaskBoardFilter(@RequestParam String inboxType,
			@RequestParam(name="page",required=false) Integer page
			,@AuthenticationPrincipal Token token) {
		System.err.println("[WBP-Dev]WorkboxRest.getTaskBoardFilter() filter request : " + inboxType);
		return workbox.getTaskBoardFilter(inboxType,page,token);

	}
	
	@RequestMapping(value = "/inboxTiles", method = RequestMethod.GET, produces = "application/json")
	public PanelResponseDto getInboxTiles(@AuthenticationPrincipal Token token) {
		return workbox.getInboxTiles(token);

	}

}
