package oneapp.incture.workbox.demo.inbox.sevices;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessDetailsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessDetailsResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.UserProcessDetailRequestDto;


public interface ProcessFacadeLocal {

	// List<AgingGraphDto> getProcessAgeingGraph(String graphTrendType);
	//
	// AgingResponseDto getProcessAgingTable(String ageingType);

	ProcessDetailsResponse getProcessesByDuration(ProcessDetailsDto processDetailsDto);

	ProcessEventsDto getProcessDetailsByInstance(String processId);

	ProcessDetailsResponse getProcessesByTaskOwner(UserProcessDetailRequestDto request);

//	ProcessAgeingResponse getProcessAgeing(String ageingType, String processName);
    
	ProcessEventsDto getProcessDetails(String requestId,String processName);
	
//	List<UserDetailsDto> getCreatedByList(String inputValue);

	// byte[] generateExcelByDuration(ProcessDetailsDto processDetailsDto);

}
