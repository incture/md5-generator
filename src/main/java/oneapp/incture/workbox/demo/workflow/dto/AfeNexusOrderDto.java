package oneapp.incture.workbox.demo.workflow.dto;

public class AfeNexusOrderDto {
	private String processName;
	private String orderType;
	private boolean isTrue;

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public boolean isTrue() {
		return isTrue;
	}

	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}

	public AfeNexusOrderDto(String processName, String orderType, boolean isTrue) {
		super();
		this.processName = processName;
		this.orderType = orderType;
		this.isTrue = isTrue;
	}

	public AfeNexusOrderDto() {
		super();
	}

}
