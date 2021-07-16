package oneapp.incture.workbox.demo.sapAriba.dto;

public class Report {

	String historic;
	String leading;
	String initial;
	String leadingVsHistoric;
	String leadingVsInitial;
	String savings;
	@Override
	public String toString() {
		return "Report [historic=" + historic + ", leading=" + leading + ", initial=" + initial + ", leadingVsHistoric="
				+ leadingVsHistoric + ", leadingVsInitial=" + leadingVsInitial + ", savings=" + savings + "]";
	}
	public String getInitial() {
		return initial;
	}
	public void setInitial(String initial) {
		this.initial = initial;
	}
	public String getLeadingVsInitial() {
		return leadingVsInitial;
	}
	public void setLeadingVsInitial(String leadingVsInitial) {
		this.leadingVsInitial = leadingVsInitial;
	}
	public String getHistoric() {
		return historic;
	}
	public void setHistoric(String historic) {
		this.historic = historic;
	}
	public String getLeading() {
		return leading;
	}
	public void setLeading(String leading) {
		this.leading = leading;
	}
	public String getLeadingVsHistoric() {
		return leadingVsHistoric;
	}
	public void setLeadingVsHistoric(String leadingVsHistoric) {
		this.leadingVsHistoric = leadingVsHistoric;
	}
	public String getSavings() {
		return savings;
	}
	public void setSavings(String savings) {
		this.savings = savings;
	}
	
}
