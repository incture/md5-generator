package oneapp.incture.workbox.demo.manageGroup.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;


@Entity
@Table(name = "GROUPS" )
public class GroupDo implements BaseDo, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1986756844287602361L;

	@EmbeddedId
	private GroupDoPk groupDoPk;
	
	@Column(name = "USER_NAME" , columnDefinition = "VARCHAR(255)")
	private String userName;
	
	@Column(name = "GROUP_NAME" , columnDefinition = "VARCHAR(255)")
	private String groupName;
	
	@Column(name = "GROUP_TYPE" , columnDefinition = "VARCHAR(20)")
	private String groupType;

	@Override
	public String toString() {
		return "GroupDo [groupDoPk=" + groupDoPk + ", userName=" + userName + ", groupName=" + groupName
				+ ", groupType=" + groupType + "]";
	}

	

	public GroupDoPk getGroupDoPk() {
		return groupDoPk;
	}



	public void setGroupDoPk(GroupDoPk groupDoPk) {
		this.groupDoPk = groupDoPk;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getGroupName() {
		return groupName;
	}



	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}



	public String getGroupType() {
		return groupType;
	}



	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}



	@Override
	public Object getPrimaryKey() {
		return groupDoPk;
	}
	

}
