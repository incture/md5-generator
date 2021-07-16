package oneapp.incture.workbox.demo.adhocTask.dto;

import java.util.List;
import java.util.Map;

public class ResourceDetailDto {

	List<List<String>> resourceIdList;
	Map<String,String> resourceNameList;
	@Override
	public String toString() {
		return "ResourceDetailDto [resourceIdList=" + resourceIdList + ", resourceNameList=" + resourceNameList + "]";
	}
	public List<List<String>> getResourceIdList() {
		return resourceIdList;
	}
	public void setResourceIdList(List<List<String>> resourceIdList) {
		this.resourceIdList = resourceIdList;
	}
	public Map<String, String> getResourceNameList() {
		return resourceNameList;
	}
	public void setResourceNameList(Map<String, String> resourceNameList) {
		this.resourceNameList = resourceNameList;
	}
	
	
	
}
