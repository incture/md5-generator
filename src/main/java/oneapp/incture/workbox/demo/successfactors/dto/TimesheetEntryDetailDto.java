package oneapp.incture.workbox.demo.successfactors.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TimesheetEntryDetailDto{
	
	
	private String timesheetEntryId;
	private String timesheetEntryDate;
	private String timeType;
	private String projectCode;
	private String activityType;
	private String hours;
	private String requestedOn;
	private String projectName;
	public String getTimesheetEntryId() {
		return timesheetEntryId;
	}
	public void setTimesheetEntryId(String timesheetEntryId) {
		this.timesheetEntryId = timesheetEntryId;
	}
	public String getTimesheetEntryDate() {
		return timesheetEntryDate;
	}
	public void setTimesheetEntryDate(String timesheetEntryDate) {
		this.timesheetEntryDate = timesheetEntryDate;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	public String getRequestedOn() {
		return requestedOn;
	}
	public void setRequestedOn(String requestedOn) {
		this.requestedOn = requestedOn;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	@Override
	public String toString() {
		return "TimesheetEntryDetailDto [timesheetEntryId=" + timesheetEntryId + ", timesheetEntryDate="
				+ timesheetEntryDate + ", timeType=" + timeType + ", projectCode=" + projectCode + ", activityType="
				+ activityType + ", hours=" + hours + ", requestedOn=" + requestedOn + ", projectName=" + projectName
				+ "]";
	}
	
	
	

}
