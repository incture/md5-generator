package oneapp.incture.workbox.demo.sharepoint.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

public class WorkFlowTesting {
	
	private WorkFlowTesting() {
	    throw new IllegalAccessError("Utility class");
	  }

//	@SuppressWarnings({ "unused" })
//	public static void main(String[] args) throws IOException {
//		TaskEventsDto task = null;
//		ProcessEventsDto process = null;
//		TaskOwnersDto owner = null;
//		InputStream file = new FileInputStream("D:/SharepointDesigner/TaskJsonResponse.json");
//		BufferedReader buf = new BufferedReader(new InputStreamReader(file));
//		String line = buf.readLine();
//		JSONObject processObject = null;
//		JSONObject taskObject = null;
//		StringBuilder sb = new StringBuilder();
//		while (line != null) {
//			sb.append(line).append("\n");
//			line = buf.readLine();
//		}
//		System.out.println(sb);
//
//		JSONObject jsnobject = new JSONObject(sb.toString());
//		System.out.println(jsnobject);
//		org.json.JSONArray taskObjArray = jsnobject.getJSONArray("value");
//		System.out.println(taskObjArray);
//
//		if (taskObjArray != null && !ServicesUtil.isEmpty(taskObjArray) && taskObjArray.length() > 0) {
//
//			for (Object object : taskObjArray) {
//				processObject = (JSONObject) object;
//				process = new ProcessEventsDto();
//
//				process.setProcessId(processObject.optString("id"));
//				System.out.println(processObject.optString("id"));
//				process.setName(processObject.optString("description"));
//				System.out.println(processObject.optString("description"));
//				process.setSubject(processObject.optString("name"));
//				System.out.println(processObject.optString("name"));
//				process.setStatus(processObject.optString("status"));
//				process.setRequestId(processObject.optString("eTag"));
//				System.out.println(processObject.optString("eTag"));
//				process.setStartedAt(ServicesUtil.isEmpty(processObject.optString("createdDateTime")) ? null
//						: ServicesUtil.convertAdminFromStringToDate(processObject.optString("createdDateTime")));
//				System.out.println(processObject.optString("createdDateTime"));
//				process.setCompletedAt(ServicesUtil.isEmpty(processObject.optString("lastModifiedDateTime")) ? null
//						: ServicesUtil.convertAdminFromStringToDate(processObject.optString("lastModifiedDateTime")));
//				System.out.println(processObject.optString("lastModifiedDateTime"));
//				JSONObject createdBy = processObject.optJSONObject("createdBy");
//				JSONObject user = createdBy.getJSONObject("user");
//				process.setStartedBy(user.optString("displayName"));
//				System.out.println(user.optString("displayName"));
//				
//			}
//			System.out.println("######################task Objects######################");
//			for (Object object : taskObjArray) {
//				taskObject = (JSONObject) object;
//
//				task = new TaskEventsDto();
//
//				task.setEventId(taskObject.optString("id"));
//				task.setProcessId(taskObject.optString("eTag"));
//
//				task.setProcessName(taskObject.optString("name"));
//
//				task.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(taskObject.optString("createdDateTime")));
//				task.setDescription(taskObject.optString("description"));
//				task.setCurrentProcessor(
//						"12");
//
//				task.setSubject(taskObject.optString("name"));
//				task.setStatus(
//						"InProgress"/* taskObject.optString("InProgress") */);
//				task.setName(taskObject.optString("name"));
//				task.setPriority("High"/* taskObject.optString("priority") */);
//				task.setCompletedAt(ServicesUtil.isEmpty(taskObject.optString("lastModifiedDateTime")) ? null
//						: ServicesUtil.convertFromStringToDate(taskObject.optString("lastModifiedDateTime")));
//
//			}
//		}
//		buf.close();
//		file.close();
//	}
}
