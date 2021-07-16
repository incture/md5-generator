package oneapp.incture.workbox.demo.sapAriba.dto;

import java.util.List;

public class AribaResposeDto {

	SuppliersDetail supplierDetail;
	List<AwardDetails> awardDetails;
	Report report;

	@Override
	public String toString() {
		return "AribaResposeDto [supplierDetail=" + supplierDetail + ", awardDetails=" + awardDetails + "]";
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public SuppliersDetail getSupplierDetail() {
		return supplierDetail;
	}

	public void setSupplierDetail(SuppliersDetail supplierDetail) {
		this.supplierDetail = supplierDetail;
	}

	public List<AwardDetails> getAwardDetails() {
		return awardDetails;
	}

	public void setAwardDetails(List<AwardDetails> awardDetails) {
		this.awardDetails = awardDetails;
	}
	
	
	
}
