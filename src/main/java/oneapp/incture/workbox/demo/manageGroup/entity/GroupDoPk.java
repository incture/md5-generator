package oneapp.incture.workbox.demo.manageGroup.entity;

import java.io.Serializable;

import javax.persistence.Column;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

public class GroupDoPk implements BaseDo, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6175419632484808009L;

	@Column(name = "GROUP_ID" , columnDefinition = "VARCHAR(255)" )
	private String groupId;
	
	@Column(name = "USER_ID" , columnDefinition = "VARCHAR(255)")
	private String userId;

	public GroupDoPk() {
		super();
	}
	
	public GroupDoPk(String groupId, String userId) {
		super();
		this.groupId = groupId;
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		return "GroupDoPk [groupId=" + groupId + ", userId=" + userId + "]";
	}



	public String getGroupId() {
		return groupId;
	}



	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}



	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
