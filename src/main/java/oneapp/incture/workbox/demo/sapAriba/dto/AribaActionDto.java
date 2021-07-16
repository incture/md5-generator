package oneapp.incture.workbox.demo.sapAriba.dto;

public class AribaActionDto {

	private String realm;
	private String user;
	private String passwordAdapter;
	private AribaActionBodyDto actionBody;
	private String apiKey;
	
	//Task Id required to update Workbox DB with the status
	private String id;
	public AribaActionDto() {
		super();
	}
	public AribaActionDto(String realm, String user, String passwordAdapter, AribaActionBodyDto actionBody,
			String apiKey) {
		super();
		this.realm = realm;
		this.user = user;
		this.passwordAdapter = passwordAdapter;
		this.actionBody = actionBody;
		this.apiKey = apiKey;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPasswordAdapter() {
		return passwordAdapter;
	}
	public void setPasswordAdapter(String passwordAdapter) {
		this.passwordAdapter = passwordAdapter;
	}
	public AribaActionBodyDto getActionBody() {
		return actionBody;
	}
	public void setActionBody(AribaActionBodyDto actionBody) {
		this.actionBody = actionBody;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	@Override
	public String toString() {
		return "AribaActionDto [realm=" + realm + ", user=" + user + ", passwordAdapter=" + passwordAdapter
				+ ", actionBody=" + actionBody + ", apiKey=" + apiKey + "]";
	}

	
}
