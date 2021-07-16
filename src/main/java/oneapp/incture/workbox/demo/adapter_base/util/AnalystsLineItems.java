package oneapp.incture.workbox.demo.adapter_base.util;

import java.util.List;

import org.apache.axis.attachments.Attachments;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AnalystsLineItems {
	
//	@Expose
//	@SerializedName("Document Id")
//	@JsonProperty("Document Id")
	private String documentId;

//	@Expose
//	@SerializedName("Material Document Number")
//	@JsonProperty("Material Document Number")
	private String materialDocumentNumber;
	
//	@Expose
//	@SerializedName("Line Item Number")
//	@JsonProperty("Line Item Number")
	private String lineItemNumber;
	
//	@Expose
//	@SerializedName("Date")
//	@JsonProperty("Date")
	private String date;
	
//	@Expose
//	@SerializedName("Total Cost")
//	@JsonProperty("Total Cost")
	private String totalCost;
	
//	@Expose
//	@SerializedName("Material Number")
//	@JsonProperty("Material Number")
	private String materialNumber;
	
//	@Expose
//	@SerializedName("Plant")
//	@JsonProperty("Plant")
	private String plant;
	
//	@Expose
//	@SerializedName("Work Cell")
//	@JsonProperty("Work Cell")
	private String workCell;
	
//	@Expose
//	@SerializedName("Movement Type")
//	@JsonProperty("Movement Type")
	private String movementType;
	
//	@Expose
//	@SerializedName("Quantity")
//	@JsonProperty("Quantity")
	private String quantity;
	
//	@Expose
//	@SerializedName("Analyst")
//	@JsonProperty("Analyst")
	private String analyst;
	
	private String lineItemFormId;
	
	

	public String getLineItemFormId() {
		return lineItemFormId;
	}

	public void setLineItemFormId(String lineItemFormId) {
		this.lineItemFormId = lineItemFormId;
	}

	private List<Attachments> attachments;
	

	public String getAnalyst() {
		return analyst;
	}

	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getMovementType() {
		return movementType;
	}

	public void setMovementType(String movementType) {
		this.movementType = movementType;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public List<Attachments> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachments> attachments) {
		this.attachments = attachments;
	}

	public String getMaterialDocumentNumber() {
		return materialDocumentNumber;
	}

	public void setMaterialDocumentNumber(String materialDocumentNumber) {
		this.materialDocumentNumber = materialDocumentNumber;
	}

	public String getLineItemNumber() {
		return lineItemNumber;
	}

	public void setLineItemNumber(String lineItemNumber) {
		this.lineItemNumber = lineItemNumber;
	}

	public String getMaterialNumber() {
		return materialNumber;
	}

	public void setMaterialNumber(String materialNumber) {
		this.materialNumber = materialNumber;
	}

	public String getWorkCell() {
		return workCell;
	}

	public void setWorkCell(String workCell) {
		this.workCell = workCell;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lineItemNumber == null) ? 0 : lineItemNumber.hashCode());
		result = prime * result + ((materialDocumentNumber == null) ? 0 : materialDocumentNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnalystsLineItems other = (AnalystsLineItems) obj;
		if (lineItemNumber == null) {
			if (other.lineItemNumber != null)
				return false;
		} else if (!lineItemNumber.equals(other.lineItemNumber))
			return false;
		if (materialDocumentNumber == null) {
			if (other.materialDocumentNumber != null)
				return false;
		} else if (!materialDocumentNumber.equals(other.materialDocumentNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AnalystsLineItems [documentId=" + documentId + ", materialDocumentNumber=" + materialDocumentNumber
				+ ", lineItemNumber=" + lineItemNumber + ", date=" + date + ", totalCost=" + totalCost
				+ ", materialNumber=" + materialNumber + ", plant=" + plant + ", workCell=" + workCell
				+ ", movementType=" + movementType + ", quantity=" + quantity + ", analyst=" + analyst
				+ ", lineItemFormId=" + lineItemFormId + ", attachments=" + attachments + "]";
	}
	
	
}
