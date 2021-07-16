package oneapp.incture.workbox.demo.ecc.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PRActionDto {

	@Expose
	@SerializedName("Banfn")
	private String banfn;
	
	@Expose
	@SerializedName("Bnfpo")
	private String bnfpo;
	
	@Expose
	@SerializedName("RelCode")
	private String relCode;
	
	@Expose
	@SerializedName("Action")
	private String action;
	
	public PRActionDto(String banfn, String bnfpo, String relCode, String action) {
		super();
		this.banfn = banfn;
		this.bnfpo = bnfpo;
		this.relCode = relCode;
		this.action = action;
	}
	public String getBanfn() {
		return banfn;
	}
	public void setBanfn(String banfn) {
		this.banfn = banfn;
	}
	public String getBnfpo() {
		return bnfpo;
	}
	public void setBnfpo(String bnfpo) {
		this.bnfpo = bnfpo;
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
		return "PRActionDto [banfn=" + banfn + ", bnfpo=" + bnfpo + ", relCode=" + relCode + ", action=" + action + "]";
	}
	
	
}
