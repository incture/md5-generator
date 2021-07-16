package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ACTION_TYPE_CONFIG")
public class ActionTypeConfigDo implements BaseDo,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3806276300353648922L;

	@Id
	@Column(name = "ACTION_TYPE")
	private String actionType;
	
	@Id
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "ACTIONS")
	private String actions;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "ACTION_NATURE")
	private String actionNature;
	

	public String getActionType() {
		return actionType;
	}


	public void setActionType(String actionType) {
		this.actionType = actionType;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getActions() {
		return actions;
	}


	public void setActions(String actions) {
		this.actions = actions;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	


	public String getActionNature() {
		return actionNature;
	}


	public void setActionNature(String actionNature) {
		this.actionNature = actionNature;
	}


	@Override
	public String toString() {
		return "ActionTypeConfigDo [actionType=" + actionType + ", status=" + status + ", actions=" + actions
				+ ", type=" + type + ", actionNature=" + actionNature + "]";
	}


	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
}
