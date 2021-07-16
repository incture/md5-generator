package oneapp.incture.workbox.demo.emailTemplate.Dto;

public class VersionStatusDto {
	private int version;
	private String status;
	
	
	@Override
	public String toString() {
		return "VersionStatusDto [version=" + version + ", status=" + status + "]";
	}


	public int getVersion() {
		return version;
	}


	public void setVersion(int version) {
		this.version = version;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
	
	

}
