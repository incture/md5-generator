package oneapp.incture.workbox.demo.notification.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.adapter_base.util.RestUtil;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.notification.dto.MobileNotification;
import oneapp.incture.workbox.demo.notification.dto.PushNotificationDto;
import oneapp.incture.workbox.demo.notification.dto.UserPushNotificationPayload;

@Service
public class PushNotificationServiceImpl implements PushNotificationService {
	
	@Autowired
	PropertiesConstants getProperty;

	@Override
	public Boolean notifyUser(PushNotificationDto pushNotificationDto) {

		System.err.println("PushNotificationServiceImpl.notifyUser() Notification Rerquest data : "+pushNotificationDto);
		UserPushNotificationPayload payload = null;
		MobileNotification notification = null;
		
		Boolean status=false;
				
		String notificationService=getProperty.getValue("MOBILE_RESTNOTIFICATION") + "application/" + getProperty.getValue("MOBILE_APPLICATION_ID") + "/user";

		if (!ServicesUtil.isEmpty(pushNotificationDto)) {

			// prepare notification payload
			payload = new UserPushNotificationPayload();
			notification = new MobileNotification();

			notification.setAlert(prepareAlert(pushNotificationDto.getAlert(),pushNotificationDto.getData()));
			notification.setData(pushNotificationDto.getData());
			notification.setSound(pushNotificationDto.getSound());

			payload.setNotification(notification);
			
			if (!ServicesUtil.isEmpty(pushNotificationDto.getUsers())) {
				payload.setUsers(pushNotificationDto.getUsers());
				
				try{
					JSONObject jsonPayload = new JSONObject(payload);
					System.err.println("PushNotificationServiceImpl.notifyUser() payload : " + jsonPayload);
					
					RestResponse responseObject = RestUtil.callRestService(notificationService, null, jsonPayload.toString(), "POST", "application/json",
							false, null, getProperty.getValue("MOBILE_USER"), getProperty.getValue("MOBILE_USER_PASS"), null, null);

					if(responseObject.getResponseCode()==201){
						System.err.println("PushNotificationServiceImpl.notifyUser() success : "+responseObject);
						status=true;
					}else{
						System.err.println("PushNotificationServiceImpl.notifyUser() error : "+responseObject);
					}
					
				}catch(Exception e){
					System.err.println("PushNotificationServiceImpl.notifyUser() error : "+e);
				}
				

			} else {
				System.err.println("PushNotificationServiceImpl.notifyUser() No user found to sent Notification :"
						+ pushNotificationDto);
			}
		} else {
			System.err.println(
					"PushNotificationServiceImpl.notifyUser() No data found to send notification : " + pushNotificationDto);
		}

		return status;
	}

	private String prepareAlert(String alert, String data) {
		
		return "{\"body\":\""+data+".\",\"title\" : \""+alert+"\"}";
	}

	public static void main(String[] args) {
		PushNotificationDto dto = new PushNotificationDto();
		dto.setAlert("Test alert");
		dto.setData("Notification to the user");
		List<String> list = new ArrayList<>();
		list.add("P000092");
		list.add("P000100");
		dto.setUsers(list);

		// prepare notification payload
		UserPushNotificationPayload payload = new UserPushNotificationPayload();
		MobileNotification notification = new MobileNotification();
		notification.setAlert(dto.getAlert());
		notification.setData(dto.getData());
		notification.setSound(dto.getSound());

		payload.setNotification(notification);
		payload.setUsers(dto.getUsers());

		JSONObject jsonPayload = new JSONObject(payload);
		System.out.println("PushNotificationServiceImpl.main() payload : " + jsonPayload);

	}

}
