package oneapp.incture.workbox.demo.cai.chatbot.dto;

public class AuthenticationHeaderResposne {
	
	private String headerName;
	private String headerValue;
	
	
	public String getHeaderName() {
		return headerName;
	}
	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	public String getHeaderValue() {
		return headerValue;
	}
	public void setHeaderValue(String headerValue) {
		this.headerValue = headerValue;
	}
	@Override
	public String toString() {
		return "AuthenticationHeaderResposne [headerName=" + headerName + ", headerValue=" + headerValue + "]";
	}

}
