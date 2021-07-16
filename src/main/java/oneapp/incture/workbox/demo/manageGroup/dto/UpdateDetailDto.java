package oneapp.incture.workbox.demo.manageGroup.dto;

import java.util.List;

public class UpdateDetailDto {

	List<GroupDto> groupdetail;
	String deleteString;
	@Override
	public String toString() {
		return "UpdateDetailDto [groupdetail=" + groupdetail + ", deleteString=" + deleteString + "]";
	}
	public List<GroupDto> getGroupdetail() {
		return groupdetail;
	}
	public void setGroupdetail(List<GroupDto> groupdetail) {
		this.groupdetail = groupdetail;
	}
	public String getDeleteString() {
		return deleteString;
	}
	public void setDeleteString(String deleteString) {
		this.deleteString = deleteString;
	}
	
	
}
