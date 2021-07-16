package oneapp.incture.workbox.demo.adapter_base.util;

public class OauthDto {

	private String accessToken;
	private String tokenType;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	@Override
	public String toString() {
		return "OauthDto [accessToken=" + accessToken + ", tokenType=" + tokenType + "]";
	}
	
	
}
