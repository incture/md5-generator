package oneapp.incture.workbox.demo.adapter_base.dto;

public class PRUpdateDto {

	private String banfn;
	private String bnfpo;
	private String menge;
	private String maktx;
	
	
	public PRUpdateDto() {
		super();
	}
	
	
	public PRUpdateDto(String banfn, String bnfpo, String menge, String maktx) {
		super();
		this.banfn = banfn;
		this.bnfpo = bnfpo;
		this.menge = menge;
		this.maktx = maktx;
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
	public String getMenge() {
		return menge;
	}
	public void setMenge(String menge) {
		this.menge = menge;
	}
	public String getMaktx() {
		return maktx;
	}
	public void setMaktx(String maktx) {
		this.maktx = maktx;
	}
	@Override
	public String toString() {
		return "PRUpdateDto [banfn=" + banfn + ", bnfpo=" + bnfpo + ", menge=" + menge + ", maktx=" + maktx + "]";
	}
	
	
	
}
