package oneapp.incture.workbox.demo.inbox.dto;

import java.math.BigDecimal;
import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.WorkBoxDto;

public class WorkboxStoryBoardDto {

	String trayId;
	String trayName;
	String filterData;
	List<WorkBoxDto> workBoxDtos;
	BigDecimal trayCount;
	
	
	public String getTrayId() {
		return trayId;
	}
	public void setTrayId(String trayId) {
		this.trayId = trayId;
	}
	public String getFilterData() {
		return filterData;
	}
	public void setFilterData(String filterData) {
		this.filterData = filterData;
	}
	@Override
	public String toString() {
		return "WorkboxStoryBoardDto [trayId=" + trayId + ", trayName=" + trayName + ", filterData=" + filterData
				+ ", workBoxDtos=" + workBoxDtos + ", trayCount=" + trayCount + "]";
	}
	public BigDecimal getTrayCount() {
		return trayCount;
	}
	public void setTrayCount(BigDecimal trayCount) {
		this.trayCount = trayCount;
	}
	public String getTrayName() {
		return trayName;
	}
	public void setTrayName(String trayName) {
		this.trayName = trayName;
	}
	public List<WorkBoxDto> getWorkBoxDtos() {
		return workBoxDtos;
	}
	public void setWorkBoxDtos(List<WorkBoxDto> workBoxDtos) {
		this.workBoxDtos = workBoxDtos;
	}
	
	
	
}
