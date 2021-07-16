package oneapp.incture.workbox.demo.adhocTask.dto;

import java.util.List;

public class TableContentDto {
	
	List<CustomAttributeTemplateDto> tableAttributes;

	public List<CustomAttributeTemplateDto> getTableAttributes() {
		return tableAttributes;
	}

	public void setTableAttributes(List<CustomAttributeTemplateDto> tableAttributes) {
		this.tableAttributes = tableAttributes;
	}

	@Override
	public String toString() {
		return "TableContentDto [tableAttributes=" + tableAttributes + "]";
	}
	
	

}
