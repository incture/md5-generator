package oneapp.incture.workbox.demo.inbox.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Sreeparna.Kundu
 *
 */
@Entity
@Table(name="PANEL_TEMPLATE")
public class PanelTemplateDo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1260266607068943603L;
	@Id
	@Column(name = "PANEL_ID", nullable = false)
	private String panelId;
	@Column(name = "PANEL_NAME", length = 100)
	private String panelName;
	@Column(name = "PANEL_TYPE", length = 100)
	private String panelType;
	@Column(name = "PARENT_ID", length = 100)
	private String parentId;
	@Column(name = "ICON", length = 100)
	private String icon;
	@Column(name = "SEQUENCE", length = 100)
	private String sequence;
	@Column(name = "ROLE_ACCESS", length = 100)
	private String roleAccess;
	@Id
	@Column(name = "DEVICE_TYPE", length = 50)
	private String deviceType;
	@Column(name = "PANEL_NAME_IN", length = 100)
	private String panelNameIN;
	@Column(name = "PANEL_NAME_AR", length = 100)
	private String panelNameAR;
	@Column(name = "PANEL_NAME_ZH", length = 100)
	private String panelNameZH;
	
	
	public String getPanelNameIN() {
		return panelNameIN;
	}
	public void setPanelNameIN(String panelNameIN) {
		this.panelNameIN = panelNameIN;
	}
	public String getPanelNameAR() {
		return panelNameAR;
	}
	public void setPanelNameAR(String panelNameAR) {
		this.panelNameAR = panelNameAR;
	}
	public String getPanelNameZH() {
		return panelNameZH;
	}
	public void setPanelNameZH(String panelNameZH) {
		this.panelNameZH = panelNameZH;
	}
	public String getPanelId() {
		return panelId;
	}
	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}
	public String getPanelName() {
		return panelName;
	}
	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}
	public String getPanelType() {
		return panelType;
	}
	public void setPanelType(String panelType) {
		this.panelType = panelType;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getRoleAccess() {
		return roleAccess;
	}
	public void setRoleAccess(String roleAccess) {
		this.roleAccess = roleAccess;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	@Override
	public String toString() {
		return "PanelTemplateDo [panelId=" + panelId + ", panelName=" + panelName + ", panelType=" + panelType
				+ ", parentId=" + parentId + ", icon=" + icon + ", sequence=" + sequence + ", roleAccess=" + roleAccess
				+ ", deviceType=" + deviceType + ", panelNameIN=" + panelNameIN + ", panelNameAR=" + panelNameAR
				+ ", panelNameZH=" + panelNameZH + "]";
	}
	
	
}
