package oneapp.incture.workbox.demo.adhocTask.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

@Component
public class TimeZoneConvertion {

	public String convertToIst()
	{
		DateFormat indianFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		
		Date dateField = Date.from( Instant.now().atZone( ZoneId.systemDefault()).toInstant());
		indianFormat .setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		
		String indianDateTime = indianFormat.format(dateField);
		
		return indianDateTime;
		
	}
	
	public String convertToUTC()
	{
		DateFormat UTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		
		Date dateField = Date.from( Instant.now().atZone( ZoneId.systemDefault()).toInstant());
		UTCFormat .setTimeZone(TimeZone.getTimeZone("UTC"));
		
		String utcDateTime = UTCFormat.format(dateField);
		
		return utcDateTime;
		
	}
}
