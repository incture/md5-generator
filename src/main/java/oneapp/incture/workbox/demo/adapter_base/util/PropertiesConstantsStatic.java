package oneapp.incture.workbox.demo.adapter_base.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PropertiesConstantsStatic {

	public static String CONN_TENANT = "";
	public static String NETWORK_REQUEST_URL = "";
	public static String NETWORK_USER_ID = "";
	public static String NETWORK_PASSWORD = "";
	public static String CC_VIRTUAL_HOST = "";
	public static String TASK_COLLECTION_URL = "";
	public static String IAS_SERVICES ="";
	public static String IAS_USER ="";
	public static String IAS_USER_PASS ="";
	static {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PropertiesConstants.class);

		CONN_TENANT = applicationContext.getEnvironment().getRequiredProperty("CONN_TENANT");
		NETWORK_REQUEST_URL = applicationContext.getEnvironment().getRequiredProperty("NETWORK_REQUEST_URL");
		NETWORK_USER_ID = applicationContext.getEnvironment().getRequiredProperty("NETWORK_USER_ID");
		NETWORK_PASSWORD = applicationContext.getEnvironment().getRequiredProperty("NETWORK_PASSWORD");
		CC_VIRTUAL_HOST = applicationContext.getEnvironment().getRequiredProperty("CC_VIRTUAL_HOST");
		TASK_COLLECTION_URL = applicationContext.getEnvironment().getRequiredProperty("TASK_COLLECTION_URL");

		IAS_SERVICES =applicationContext.getEnvironment().getRequiredProperty("IAS_SERVICES");
		IAS_USER =applicationContext.getEnvironment().getRequiredProperty("IAS_USER");
		IAS_USER_PASS =applicationContext.getEnvironment().getRequiredProperty("IAS_USER_PASS");
		
		((ConfigurableApplicationContext) applicationContext).close();

	}
}
