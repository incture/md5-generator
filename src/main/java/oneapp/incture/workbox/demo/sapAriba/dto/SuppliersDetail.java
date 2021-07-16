package oneapp.incture.workbox.demo.sapAriba.dto;

import java.util.List;

public class SuppliersDetail {

	List<HeaderDto> headerdto;
	List<ValueDto> valueDto;
	String competativeField;
	
	@Override
	public String toString() {
		return "SuppliersDetail [headerdto=" + headerdto + ", valueDto=" + valueDto + ", competativeField="
				+ competativeField + "]";
	}
	public String getCompetativeField() {
		return competativeField;
	}
	public void setCompetativeField(String competativeField) {
		this.competativeField = competativeField;
	}
	public List<HeaderDto> getHeaderdto() {
		return headerdto;
	}
	public void setHeaderdto(List<HeaderDto> headerdto) {
		this.headerdto = headerdto;
	}
	public List<ValueDto> getValueDto() {
		return valueDto;
	}
	public void setValueDto(List<ValueDto> valueDto) {
		this.valueDto = valueDto;
	}
	
	
	
}

