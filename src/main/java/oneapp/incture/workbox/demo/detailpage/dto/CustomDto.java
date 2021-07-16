package oneapp.incture.workbox.demo.detailpage.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;

public class CustomDto {

	private String dataType;
	private String title;
	private List<CustomAttributeTemplate> contentDto;
	
	public CustomDto() {
		super();
	}
	
	public CustomDto(String dataType, String title, List<CustomAttributeTemplate> contentDto) {
		super();
		this.dataType = dataType;
		this.title = title;
		this.contentDto = contentDto;
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
	public List<CustomAttributeTemplate> getContentDto() {
		return contentDto;
	}
	public void setContentDto(List<CustomAttributeTemplate> contentDto) {
		this.contentDto = contentDto;
	}
	@Override
	public String toString() {
		return "CustomDto [dataType=" + dataType + ", title=" + title + "]";
	}
	
	
	
}
