package oneapp.incture.workbox.demo.detailpage.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;

public class DynamicDetailDto {

	private String title;
	private List<CustomAttributeTemplate> customDetails;
	private String dataType;
	
	
	public DynamicDetailDto(String title, List<CustomAttributeTemplate> customDetails, String dataType) {
		super();
		this.title = title;
		this.customDetails = customDetails;
		this.dataType = dataType;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<CustomAttributeTemplate> getCustomDetails() {
		return customDetails;
	}
	public void setCustomDetails(List<CustomAttributeTemplate> customDetails) {
		this.customDetails = customDetails;
	}
	@Override
	public String toString() {
		return "DynamicDetailDto [title=" + title + ", customDetails=" + customDetails + ", dataType=" + dataType + "]";
	}
	public DynamicDetailDto() {
		super();
	}
	public DynamicDetailDto(String title, List<CustomAttributeTemplate> customDetails) {
		super();
		this.title = title;
		this.customDetails = customDetails;
	}
	
	
}
