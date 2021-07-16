package oneapp.incture.workbox.demo.dashboard.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "USER_QUICK_LINK")

public class UserQuickLinkDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "USER_ID", length = 100)
	private String userId;
	@Id
	@Column(name = "QUICK_LINK", length = 100)
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
		return "UserQuickLinkDo [userId=" + userId + ", quickLink=" + quickLink + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}