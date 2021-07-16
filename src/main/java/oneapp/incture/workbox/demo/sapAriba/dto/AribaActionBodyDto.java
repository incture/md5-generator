package oneapp.incture.workbox.demo.sapAriba.dto;

public class AribaActionBodyDto {

	private String actionableType;
	private String uniqueName;
	private String actionName;
	private ActionComment options;
	
	public AribaActionBodyDto() {
		super();
	}
	public AribaActionBodyDto(String actionableType, String uniqueName, String actionName, ActionComment options) {
		super();
		this.actionableType = actionableType;
		this.uniqueName = uniqueName;
		this.actionName = actionName;
		this.options = options;
	}
	public String getActionableType() {
		return actionableType;
	}
	public void setActionableType(String actionableType) {
		this.actionableType = actionableType;
	}
	public String getUniqueName() {
		return uniqueName;
	}
	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public ActionComment getOptions() {
		return options;
	}
	public void setOptions(ActionComment options) {
		this.options = options;
	}
	@Override
	public String toString() {
		return "AribaActionBodyDto [actionableType=" + actionableType + ", uniqueName=" + uniqueName + ", actionName="
				+ actionName + ", options=" + options + "]";
	}
	
}
