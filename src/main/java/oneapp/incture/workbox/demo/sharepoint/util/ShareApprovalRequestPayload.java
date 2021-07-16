package oneapp.incture.workbox.demo.sharepoint.util;

public class ShareApprovalRequestPayload {
	private String Status; 
	private String PercentComplete;
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getPercentComplete() {
		return PercentComplete;
	}
	public void setPercentComplete(String percentComplete) {
		PercentComplete = percentComplete;
	}
	@Override
	public String toString() {
		return "ShareApprovalRequestPayload [Status=" + Status + ", PercentComplete=" + PercentComplete + "]";
	}

	
	
	
}
