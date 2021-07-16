package oneapp.incture.workbox.demo.successfactors.dto;

import java.util.List;

public class TimesheetResponseDto {

	private List<TimesheetEntryDetailDto> timesheetEntryDetailDto;

	public List<TimesheetEntryDetailDto> getTimesheetEntryDetailDto() {
		return timesheetEntryDetailDto;
	}

	public void setTimesheetEntryDetailDto(List<TimesheetEntryDetailDto> timesheetEntryDetailDto) {
		this.timesheetEntryDetailDto = timesheetEntryDetailDto;
	}

	@Override
	public String toString() {
		return "TimesheetResponseDto [timesheetEntryDetailDto=" + timesheetEntryDetailDto + "]";
	}
	
	
}
