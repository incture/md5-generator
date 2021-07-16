package oneapp.incture.workbox.demo.dashboard.dto;

public class GraphDetail {

	private String graphName;
	private String chartType;
	private String graphConfigId;
	private Boolean isActive;
	private Integer sequence;
	private String userId;
	
	@Override
	public String toString() {
		return "GraphDetail [graphName=" + graphName + ", chartType=" + chartType + ", graphConfigId=" + graphConfigId
				+ ", isActive=" + isActive + ", sequence=" + sequence + ", userId=" + userId + "]";
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getGraphName() {
		return graphName;
	}
	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}
	public String getChartType() {
		return chartType;
	}
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}
	public String getGraphConfigId() {
		return graphConfigId;
	}
	public void setGraphConfigId(String graphConfigId) {
		this.graphConfigId = graphConfigId;
	}
	
}
