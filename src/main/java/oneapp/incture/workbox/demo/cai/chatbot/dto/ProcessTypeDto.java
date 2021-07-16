package oneapp.incture.workbox.demo.cai.chatbot.dto;

public class ProcessTypeDto {

	private String definationId;
	private String processType;
	private String processType_arb;
	private String filterKey;
	private String filterLabel;
	private String primaryKey;
	private Integer ranking;
	private Boolean isEnabled;

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getProcessType_arb() {
		return processType_arb;
	}

	public void setProcessType_arb(String processType_arb) {
		this.processType_arb = processType_arb;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getDefinationId() {
		return definationId;
	}

	public void setDefinationId(String definationId) {
		this.definationId = definationId;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getFilterKey() {
		return filterKey;
	}

	public void setFilterKey(String filterKey) {
		this.filterKey = filterKey;
	}

	public String getFilterLabel() {
		return filterLabel;
	}

	public void setFilterLabel(String filterLabel) {
		this.filterLabel = filterLabel;
	}

	@Override
	public String toString() {
		return "ProcessTypeDto [definationId=" + definationId + ", processType=" + processType + ", processType_arb="
				+ processType_arb + ", filterKey=" + filterKey + ", filterLabel=" + filterLabel + ", primaryKey="
				+ primaryKey + ", ranking=" + ranking + "]";
	}

}
