package oneapp.incture.workbox.demo.adapter_base.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@PropertySource(value = { "classpath:application.properties" })
@Component
public class PropertiesConstants {

	@Autowired
	private Environment environment;

	public String getValue(String key) {
		String value = "";

		switch (key) {

//		case "APPLICATION_API_OAUTH_TOKEN_URL":
//			value = environment.getRequiredProperty("APPLICATION_API_OAUTH_TOKEN_URL");
//			break;
//		case "APPLICATION_API_OAUTH_CLIENT_ID":
//			value = environment.getRequiredProperty("APPLICATION_API_OAUTH_CLIENT_ID");
//			break;
//		case "APPLICATION_API_OAUTH_CLIENT_SECRET":
//			value = environment.getRequiredProperty("APPLICATION_API_OAUTH_CLIENT_SECRET");
//			break;
//		case "PLATFORM_API_USERS_FROM_GROUPS_URL":
//			value = environment.getRequiredProperty("PLATFORM_API_USERS_FROM_GROUPS_URL");
//			break;
		case "REQUEST_URL_INST":
			value = environment.getRequiredProperty("REQUEST_URL_INST");
			break;
		case "REQUEST_BASE_URL_TC":
			value = environment.getRequiredProperty("REQUEST_BASE_URL_TC");
			break;
		case "MOBILE_RESTNOTIFICATION":
			value = environment.getRequiredProperty("MOBILE_RESTNOTIFICATION");
			break;
		case "MOBILE_APPLICATION_ID":
			value = environment.getRequiredProperty("MOBILE_APPLICATION_ID");
			break;
		case "MOBILE_USER":
			value = environment.getRequiredProperty("MOBILE_USER");
			break;
		case "MOBILE_USER_PASS":
			value = environment.getRequiredProperty("MOBILE_USER_PASS");
			break;
		case "REQUEST_URL_SHAREPOINT":
			value = environment.getRequiredProperty("REQUEST_URL_SHAREPOINT");
			break;
		case "TASK_COLLECTION_URL":
			value = environment.getRequiredProperty("TASK_COLLECTION_URL");
			break;
		case "CC_VIRTUAL_HOST":
			value = environment.getRequiredProperty("CC_VIRTUAL_HOST");
			break;
		case "POApprovalURL":
			value = environment.getRequiredProperty("POApprovalURL");
			break;
		case "PRApprovalURL":
			value = environment.getRequiredProperty("PRApprovalURL");
			break;
		case "FROM_MAIL_ID":
			value = environment.getRequiredProperty("FROM_MAIL_ID");
			break;
		case "FROM_MAIL_ID_PASSWORD":
			value = environment.getRequiredProperty("FROM_MAIL_ID_PASSWORD");
			break;
			
		case "APP_URL":
			value = environment.getRequiredProperty("APP_URL");
			break;
			
		case "PPD_VALUE_UPDATE":
			value = environment.getRequiredProperty("PPD_VALUE_UPDATE");
			break;
			
		}
		System.err.println("[WBP-Dev][Workbox][Properties][Key]" + key + "[value]" + value);
		return value;
	}

}
