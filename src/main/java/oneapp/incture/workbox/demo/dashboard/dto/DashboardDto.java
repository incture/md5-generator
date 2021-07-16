package oneapp.incture.workbox.demo.dashboard.dto;


import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
@JsonInclude(Include.NON_NULL)
public class DashboardDto extends BaseDto {

	private BigInteger totalActiveTask;
	private BigInteger openTask;
	private BigInteger pendingTask;
	private BigInteger slaBreachedTask;
	private BigInteger myTask;
	private BigInteger criticalTask;
	
	private List<TileDetailsDto> tiles;
	

	private List<TaskNameCountDto> quickLinkList;

	private List<TotalActiveTaskDto> totalActiveTaskList;

	private List<UserWorkCountDto> userWorkCountList;

	private List<TaskNameCountDto> taskCompletionTrendList;

	private List<TaskNameCountDto> taskDonutList;

	public List<TaskNameCountDto> getTaskDonutList() {
		return taskDonutList;
	}

	public void setTaskDonutList(List<TaskNameCountDto> taskDonutList) {
		this.taskDonutList = taskDonutList;
	}

	public List<TaskNameCountDto> getQuickLinkList() {
		return quickLinkList;
	}

	public void setQuickLinkList(List<TaskNameCountDto> quickLinkList) {
		this.quickLinkList = quickLinkList;
	}

	public List<TaskNameCountDto> getTaskCompletionTrendList() {
		return taskCompletionTrendList;
	}

	public void setTaskCompletionTrendList(List<TaskNameCountDto> taskCompletionTrendList) {
		this.taskCompletionTrendList = taskCompletionTrendList;
	}

	public BigInteger getTotalActiveTask() {
		return totalActiveTask;
	}

	public void setTotalActiveTask(BigInteger totalActiveTask) {
		this.totalActiveTask = totalActiveTask;
	}

	public BigInteger getOpenTask() {
		return openTask;
	}

	public void setOpenTask(BigInteger openTask) {
		this.openTask = openTask;
	}

	public BigInteger getPendingTask() {
		return pendingTask;
	}

	public void setPendingTask(BigInteger pendingTask) {
		this.pendingTask = pendingTask;
	}

	public BigInteger getSlaBreachedTask() {
		return slaBreachedTask;
	}

	public void setSlaBreachedTask(BigInteger slaBreachedTask) {
		this.slaBreachedTask = slaBreachedTask;
	}

	public BigInteger getMyTask() {
		return myTask;
	}

	public void setMyTask(BigInteger myTask) {
		this.myTask = myTask;
	}

	public List<TotalActiveTaskDto> getTotalActiveTaskList() {
		return totalActiveTaskList;
	}

	public void setTotalActiveTaskList(List<TotalActiveTaskDto> totalActiveTaskList) {
		this.totalActiveTaskList = totalActiveTaskList;
	}

	public List<UserWorkCountDto> getUserWorkCountList() {
		return userWorkCountList;
	}

	public void setUserWorkCountList(List<UserWorkCountDto> userWorkCountList) {
		this.userWorkCountList = userWorkCountList;
	}
	public BigInteger getCriticalTask() {
		return criticalTask;
	}

	public void setCriticalTask(BigInteger criticalTask) {
		this.criticalTask = criticalTask;
	}

	
	public List<TileDetailsDto> getTiles() {
		return tiles;
	}

	public void setTiles(List<TileDetailsDto> tiles) {
		this.tiles = tiles;
	}

	@Override
	public String toString() {
		return "DashboardDto [totalActiveTask=" + totalActiveTask + ", openTask=" + openTask + ", pendingTask="
				+ pendingTask + ", slaBreachedTask=" + slaBreachedTask + ", myTask=" + myTask + ", criticalTask="
				+ criticalTask + ", tiles=" + tiles + ", quickLinkList=" + quickLinkList + ", totalActiveTaskList="
				+ totalActiveTaskList + ", userWorkCountList=" + userWorkCountList + ", taskCompletionTrendList="
				+ taskCompletionTrendList + ", taskDonutList=" + taskDonutList + "]";
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

}
