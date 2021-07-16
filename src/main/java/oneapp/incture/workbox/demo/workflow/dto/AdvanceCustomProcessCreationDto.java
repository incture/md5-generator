package oneapp.incture.workbox.demo.workflow.dto;

import java.util.List;

public class AdvanceCustomProcessCreationDto {
	
	AdvanceProcessConfigTbDto processDetail;
	List<TeamDetailDto> teamDetailDto;
	
	
	public AdvanceProcessConfigTbDto getProcessDetail() {
		return processDetail;
	}
	public void setProcessDetail(AdvanceProcessConfigTbDto processDetail) {
		this.processDetail = processDetail;
	}
	public List<TeamDetailDto> getTeamDetailDto() {
		return teamDetailDto;
	}
	public void setTeamDetailDto(List<TeamDetailDto> teamDetailDto) {
		this.teamDetailDto = teamDetailDto;
	}
	
	
	@Override
	public String toString() {
		return "AdvanceCustomProcessCreationDto [processDetail=" + processDetail + ", teamDetailDto=" + teamDetailDto
				+ "]";
	}
	
	
	
}
