package oneapp.incture.workbox.demo.dashboard.dto;


import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class UserQuickLinkDto extends BaseDto {

	public UserQuickLinkDto() {
		super();
	}

	public UserQuickLinkDto(String userId, String quickLink) {
		super();
		this.userId = userId;
		this.quickLink = quickLink;
	}

	private String userId;
	private String quickLink;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getQuickLink() {
		return quickLink;
	}

	public void setQuickLink(String quickLink) {
		this.quickLink = quickLink;
	}

	@Override
	public String toString() {
		return "UserQuickLinkDto [userId=" + userId + ", quickLink=" + quickLink + "]";
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}

}
