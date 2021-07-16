package oneapp.incture.workbox.demo.adapterJira.dto;

public class DefaultRootCauseDto {
	private String defaultRootCauseId;
	private String defaultRootCauseName;
	
	public String getDefaultRootCauseId() {
		return defaultRootCauseId;
	}
	public void setDefaultRootCauseId(String defaultRootCauseId) {
		this.defaultRootCauseId = defaultRootCauseId;
	}
	public String getDefaultRootCauseName() {
		return defaultRootCauseName;
	}
	public void setDefaultRootCauseName(String defaultRootCauseName) {
		this.defaultRootCauseName = defaultRootCauseName;
	}
	
	@Override
	public String toString() {
		return "DefaultRootCauseDto [defaultRootCauseId=" + defaultRootCauseId + ", defaultRootCauseName="
				+ defaultRootCauseName + "]";
	}
	
}
