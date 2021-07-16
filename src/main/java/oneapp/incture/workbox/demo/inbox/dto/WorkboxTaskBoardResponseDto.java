package oneapp.incture.workbox.demo.inbox.dto;

import java.math.BigDecimal;
import java.util.List;

public class WorkboxTaskBoardResponseDto {

	List<WorkboxStoryBoardDto> workboxStoryBoardDto;
	BigDecimal count;
	

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "WorkboxTaskBoardResponseDto [workboxStoryBoardDto=" + workboxStoryBoardDto + ", count=" + count + "]";
	}

	public List<WorkboxStoryBoardDto> getWorkboxStoryBoardDto() {
		return workboxStoryBoardDto;
	}

	public void setWorkboxStoryBoardDto(List<WorkboxStoryBoardDto> workboxStoryBoardDto) {
		this.workboxStoryBoardDto = workboxStoryBoardDto;
	}

	
	
	
}
