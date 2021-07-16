package oneapp.incture.workbox.demo.notification.util;

public interface NotificationConstant {
	
	String FAILURE = "FAILURE";
	String STATUS_CODE_FAILURE = "1";
	String SUCCESS = "SUCCESS";
	String STATUS_CODE_SUCCESS = "0";
	String ALL = "ALL";
	String SINGLE = "SINGLE";
	String HIGH = "HIGH";
	String NEW = "NEW";
	String TAGGED = "Personal Mentions";
	String RELEASED = "RELEASED";
	String APP_URL = "<a href=\"https://workbox-kbniwmq1aj.dispatcher.hana.ondemand.com/index.html?hc_reset#/UnifiedInbox\">"
			+ "here</a>";

	int NOTIFICATTION_SCHEDULER_INTERVAL = 20;

	String APPROVE = "Task Approved"; // "Approve";
	String REJECT = "Task Rejected"; // "Reject";
	String FORWARD = "Task Forwarded";
	String SUBSTITUTION = "New Substitution";
	String ACTIVATION_SUBSTITUTION = "Substitution Activation";
	String SUBSTITUTION_APPROVAL = "Substitution Process Approval";
	String SYSTEM_UPDATE = "System Updates"; 
	String ADMIN_SETTING_CHANGES = "Admin Setting Changes"; 
	String USER_CONFIGURATION_UPDATES = "User Configuration Updates";

	// String WEB = "WEB";
	// String MAIL = "MAIL";

	String NEW_TASK = "New Task Assigned";
	String SLA_TASK = "SLA Breached Task";
	String NOTIFICATION_CHANNEL_EMAIL = "Email";
	String NOTIFICATION_CHANNEL_PUSH = "Mobile";
	String NOTIFICATION_CHANNEL_WEB = "Web";
	String ADMIN = "Admin";
	int NOTIFICATION_LIMIT = 10;
	
	// Notification Events
	String TASK = "Task";
	String CHAT = "Chat";
	String APPLICATION = "Application";
	String PEOPLE = "People";
	String REPORTS = "Reports";
}
