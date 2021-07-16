package oneapp.incture.workbox.demo.detailpage.dto;

public class DynamicButtonDto {

	private String buttonKey;
	private String buttonText;
	private Boolean buttonFlag;
	private String processName;
	
	public DynamicButtonDto(String buttonKey, String buttonText, Boolean buttonFlag, String processName) {
		super();
		this.buttonKey = buttonKey;
		this.buttonText = buttonText;
		this.buttonFlag = buttonFlag;
		this.processName = processName;
	}

	public DynamicButtonDto() {
		super();
	}

	public String getButtonKey() {
		return buttonKey;
	}

	public void setButtonKey(String buttonKey) {
		this.buttonKey = buttonKey;
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public Boolean getButtonFlag() {
		return buttonFlag;
	}

	public void setButtonFlag(Boolean buttonFlag) {
		this.buttonFlag = buttonFlag;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	@Override
	public String toString() {
		return "DynamicButtonDto [buttonKey=" + buttonKey + ", buttonText=" + buttonText + ", buttonFlag=" + buttonFlag
				+ ", processName=" + processName + "]";
	}
	
	
}
