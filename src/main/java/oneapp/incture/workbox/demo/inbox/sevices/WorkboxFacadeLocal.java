package oneapp.incture.workbox.demo.inbox.sevices;

import java.util.List;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.inbox.dto.InboxFilterDto;
import oneapp.incture.workbox.demo.inbox.dto.PanelResponseDto;
import oneapp.incture.workbox.demo.inbox.dto.PinnedTaskDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxRequestDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxResponseDto;

public interface WorkboxFacadeLocal {

	WorkboxResponseDto getWorkboxFilterData(WorkboxRequestDto taskRequest, Boolean isChatBot,Token tokn);

	WorkboxResponseDto getWorkboxReport(WorkboxRequestDto taskRequest);

	WorkboxResponseDto getWorkboxCompletedFilterData(String processName, String requestId, String createdBy,
			String createdAt, String completedAt, String period, Integer skipCount, Integer maxCount, Integer page);

	WorkboxResponseDto getWorkboxFilterDataNew(InboxFilterDto taskRequest, Boolean isChatBot,Token token);

	ResponseMessage createPinnedTask(PinnedTaskDto pinnedTaskdto,Token token);

	PanelResponseDto getInboxPanel(String deviceType, Integer dropdownCount, Token token);

	List<Object[]> getWorkboxFilterDataForFileExport(InboxFilterDto taskRequest, Boolean isChatBot, String processName,
			String processAttr,Token token);
}
