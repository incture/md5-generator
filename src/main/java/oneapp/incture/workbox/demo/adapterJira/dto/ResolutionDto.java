package oneapp.incture.workbox.demo.adapterJira.dto;

public class ResolutionDto {
	private String resolutionId;	
	private String resolutionName;
	
	public String getResolutionId() {
		return resolutionId;
	}
	public void setResolutionId(String resolutionId) {
		this.resolutionId = resolutionId;
	}
	public String getResolutionName() {
		return resolutionName;
	}
	public void setResolutionName(String resolutionName) {
		this.resolutionName = resolutionName;
	}
	
	@Override
	public String toString() {
		return "ResolutionDto [resolutionId=" + resolutionId + ", resolutionName=" + resolutionName + "]";
	}
	
}
