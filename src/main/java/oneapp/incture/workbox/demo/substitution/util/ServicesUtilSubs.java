package oneapp.incture.workbox.demo.substitution.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

/**
 * Contains utility functions to be used by Services
 * 
 * @version R1
 */
public class ServicesUtilSubs extends ServicesUtil {

	public static String getUTCTime() {
		 SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		format1.setTimeZone(TimeZone.getTimeZone("IST"));
		String currentTime = format1.format(new Date());
		return currentTime;
	}
	

}
