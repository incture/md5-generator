package oneapp.incture.workbox.demo.ecc.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class POActionDto {

	@Expose
	@SerializedName("Ebeln")
	private String ebeln;

	@Expose
	@SerializedName("RelCode")
	private String relCode;

	@Expose
	@SerializedName("Action")
	private String action;

	public POActionDto() {
		super();
	}

	public POActionDto(String ebeln, String relCode, String action) {
		super();
		this.ebeln = ebeln;
		this.relCode = relCode;
		this.action = action;
	}

	public String getEbeln() {
		return ebeln;
	}

	public void setEbeln(String ebeln) {
		this.ebeln = ebeln;
	}

	public String getRelCode() {
		return relCode;
	}

	public void setRelCode(String relCode) {
		this.relCode = relCode;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "POActionDto [ebeln=" + ebeln + ", relCode=" + relCode + ", action=" + action + "]";
	}

}
