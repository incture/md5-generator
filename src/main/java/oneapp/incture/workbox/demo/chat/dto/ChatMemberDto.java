package oneapp.incture.workbox.demo.chat.dto;

public class ChatMemberDto {
	
	private String id;   //userID
	 
    private String displayName;
   
    private String firstName;
   
    private String lastName;
   
    private String email;
   
    private String chatID;   //This is task ID

	@Override
	public String toString() {
		return "ChatMemberDto [id=" + id + ", displayName=" + displayName + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", chatID=" + chatID + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getChatID() {
		return chatID;
	}

	public void setChatID(String chatID) {
		this.chatID = chatID;
	}
    
    
	
}
