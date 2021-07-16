package oneapp.incture.workbox.demo.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.inbox.dto.ProcessTrackingResponse;
import oneapp.incture.workbox.demo.inbox.dto.TaskEventsResponse;
import oneapp.incture.workbox.demo.inbox.dto.TaskRestDto;
import oneapp.incture.workbox.demo.inbox.sevices.ProcessFacadeLocal;
import oneapp.incture.workbox.demo.inbox.sevices.TaskFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/workbox/task", produces = "application/json")
public class ProcessTrackerRest {

	@Autowired
	private TaskFacadeLocal task;
	@Autowired
	private ProcessFacadeLocal process;

	@RequestMapping(value = "/details", method = RequestMethod.POST)
	public ProcessTrackingResponse getTaskDetailsByProcessInstance(@RequestBody TaskRestDto dto) {
		String s = null;
		ProcessEventsDto processInstance = null;
		ProcessTrackingResponse response = new ProcessTrackingResponse();
		try {
			if (!ServicesUtil.isEmpty(dto.getProcessId()))
				s = URLDecoder.decode(dto.getProcessId(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (!ServicesUtil.isEmpty(s))
			processInstance = process.getProcessDetailsByInstance(s);
		else {
			String requestId = dto.getRequestId();
			String processName = dto.getProcessName();

			processInstance = process.getProcessDetails(requestId, processName);
		}
		if (!ServicesUtil.isEmpty(processInstance)) {
			TaskEventsResponse taskresponse = task.getTaskDetailsByProcessInstance(processInstance, dto.getTaskId());
			response.setProcess(processInstance);
			if (taskresponse != null) {
				response.setTasks(taskresponse.getTaskEventDtos());
				response.setMessage(taskresponse.getResponseMessage());
			} else
				response.setMessage(new ResponseMessage("fail", "500", "fail to fetch the process"));

		} else
			response.setMessage(new ResponseMessage("Fail", "500", "Fail to fetch the process"));

		return response;
	}

	// @RequestMapping(value = "/ageing", method = RequestMethod.GET)
	// public TaskAgeingResponse getProcessAging(@RequestParam(name =
	// "processName", required = false) String processName,
	// @RequestParam(name = "userGroup", required = false) String userGroup,
	// @RequestParam(name = "status", required = false) String status,
	// @RequestParam(name = "requestId", required = false) String requestId,
	// @RequestParam(name = "labelValue", required = false) String labelValue) {
	// return task.getTaskAgeing(processName, userGroup, status, requestId,
	// labelValue);
	// }
	//
	// @RequestMapping(value = "/manager", method = RequestMethod.POST)
	// public ManageTasksResponseDto getTasksByUserAndDuration(@RequestBody
	// ManageTasksRequestDto request) {
	// return task.getTasksByUserAndDuration(request);
	// }
	//
	// @RequestMapping(value = "/getOwners", method = RequestMethod.POST)
	// public TaskOwnersListDto getTaskOwners(@RequestBody WorkBoxActionDto dto)
	// {
	// return task.getTaskOwners(dto);
	// }
	//
	// @RequestMapping(value = "/dynamicDetails",method = RequestMethod.GET)
	// public DynamicTaskDetailsDto getDynamicTestDetails(@RequestParam(name =
	// "taskId", required = false) String taskId) {
	// DynamicTaskDetailsDto dto= task.getDynamicDetails(taskId);
	// return dto;
	// }
}
