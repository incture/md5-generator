package oneapp.incture.workbox.demo.inbox.util;

public class RequestDto {

	String actionType;
	String contextId;
	String comments;

	@Override
	public String toString() {
		return "RequestDto [actionType=" + actionType + ", contextId=" + contextId + ", comments=" + comments + "]";
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getContextId() {
		return contextId;
	}
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
