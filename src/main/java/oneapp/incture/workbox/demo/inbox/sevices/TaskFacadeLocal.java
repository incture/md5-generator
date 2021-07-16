package oneapp.incture.workbox.demo.inbox.sevices;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.inbox.dto.TaskEventsResponse;

public interface TaskFacadeLocal {

	TaskEventsResponse getTaskDetailsByProcessInstance(ProcessEventsDto processId, String taskId);

}
