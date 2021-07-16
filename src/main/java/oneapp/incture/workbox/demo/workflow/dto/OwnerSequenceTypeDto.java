package oneapp.incture.workbox.demo.workflow.dto;

public class OwnerSequenceTypeDto {
	
	private String ownerSequType;
	private String attrTypeId;
	private String attrTypeName;
	private String orderBy;
	
	public String getOwnerSequType() {
		return ownerSequType;
	}
	public void setOwnerSequType(String ownerSequType) {
		this.ownerSequType = ownerSequType;
	}
	public String getAttrTypeId() {
		return attrTypeId;
	}
	public void setAttrTypeId(String attrTypeId) {
		this.attrTypeId = attrTypeId;
	}
	public String getAttrTypeName() {
		return attrTypeName;
	}
	public void setAttrTypeName(String attrTypeName) {
		this.attrTypeName = attrTypeName;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	@Override
	public String toString() {
		return "OwnerSequenceTypeDto [ownerSequType=" + ownerSequType + ", attrTypeId=" + attrTypeId + ", attrTypeName="
				+ attrTypeName + ", orderBy=" + orderBy + "]";
	}
	

	
	
}
