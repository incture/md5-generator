package oneapp.incture.workbox.demo.cai.chatbot.dto;

public class ChatbotDataRequestDto implements ChatbotMemory {

	private UserInfo userInfo;
	private String requestType;
	private String processType;
	private Integer page = 1;
	private String deviceType;
	private String inboxType;
	private String headerValue;
	private String headername;

	private ChatbotDataMemory memory;

	public String getHeaderValue() {
		return headerValue;
	}

	public void setHeaderValue(String headerValue) {
		this.headerValue = headerValue;
	}

	public String getHeadername() {
		return headername;
	}

	public void setHeadername(String headername) {
		this.headername = headername;
	}

	public ChatbotDataMemory getMemory() {
		return memory;
	}

	public void setMemory(ChatbotDataMemory memory) {
		this.memory = memory;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getInboxType() {
		return inboxType;
	}

	public void setInboxType(String inboxType) {
		this.inboxType = inboxType;
	}

	@Override
	public String toString() {
		return "ChatbotDataRequestDto [userInfo=" + userInfo + ", requestType=" + requestType + ", processType="
				+ processType + ", page=" + page + ", deviceType=" + deviceType + ", inboxType=" + inboxType
				+ ", headerValue=" + headerValue + ", headername=" + headername + ", memory=" + memory + "]";
	}

}
