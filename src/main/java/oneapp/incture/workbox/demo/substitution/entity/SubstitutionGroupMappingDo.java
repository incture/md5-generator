package oneapp.incture.workbox.demo.substitution.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;


@Entity
@Table(name = "SUBSTITUTION_GROUP_MAPPING")

public class SubstitutionGroupMappingDo  implements BaseDo , Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PROCESS")
	private String process;

	@Column(name = "USER_GROUP_ID")
	private String userGroupId;
	
	@Column(name="GROUP_NAME")
	private String groupName;
	
	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Override
    public Object getPrimaryKey() {
        // TODO Auto-generated method stub
        return null;
    }

}