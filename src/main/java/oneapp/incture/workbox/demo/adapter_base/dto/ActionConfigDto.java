package oneapp.incture.workbox.demo.adapter_base.dto;

public class ActionConfigDto {

	String action;
	String icon;
	String type;
	String actionType;
	
	
	
	@Override
	public String toString() {
		return "ActionConfigDto [action=" + action + ", icon=" + icon + ", type=" + type + ", actionType=" + actionType
				+ "]";
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	
	
	
}
