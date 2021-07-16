package oneapp.incture.workbox.demo.adhocTask.dto;

public class UserRoleDto {

	String roleName;
	String roleId;
	@Override
	public String toString() {
		return "UserRoleDto [roleName=" + roleName + ", roleId=" + roleId + "]";
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	
}
