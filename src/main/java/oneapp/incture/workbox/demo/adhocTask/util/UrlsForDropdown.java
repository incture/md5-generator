package oneapp.incture.workbox.demo.adhocTask.util;

import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;

public class UrlsForDropdown {

	public static String getDropdownUrl(String attibuteName,String key, Boolean isRunTime,String runTimeType) {
		String url = "";
		
		if(isRunTime){
			if(PMCConstant.INDIVIDUAL.equalsIgnoreCase(runTimeType))
				url = "/idpMapping/getUsers";
			else if(PMCConstant.GROUP.equalsIgnoreCase(runTimeType))
				url = "/group/getAllGroup/CUSTOM";
			else if(PMCConstant.ROLE.equalsIgnoreCase(runTimeType))
				url = "/tasks/getUserRoles";
			
		}
		else 
			url = "/crossConstant/getConstants/"+key;
		
		return url;
	}

	
}