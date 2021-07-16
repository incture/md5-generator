package oneapp.incture.workbox.demo.dashboard.dto;

import java.util.List;

public class GraphDataDto {

	String xValue;
	String xDisplayValue;
	String yValue;
	String yDisplayValue;
	List<GraphDataCountDto> graphDataCountDto;
	
	@Override
	public String toString() {
		return "GraphDataDto [xValue=" + xValue + ", xDisplayValue=" + xDisplayValue + ", yValue=" + yValue
				+ ", yDisplayValue=" + yDisplayValue + ", graphDataCountDto=" + graphDataCountDto + "]";
	}
	
	public List<GraphDataCountDto> getGraphDataCountDto() {
		return graphDataCountDto;
	}

	public void setGraphDataCountDto(List<GraphDataCountDto> graphDataCountDto) {
		this.graphDataCountDto = graphDataCountDto;
	}

	public String getxDisplayValue() {
		return xDisplayValue;
	}

	public void setxDisplayValue(String xDisplayValue) {
		this.xDisplayValue = xDisplayValue;
	}

	public String getyDisplayValue() {
		return yDisplayValue;
	}

	public void setyDisplayValue(String yDisplayValue) {
		this.yDisplayValue = yDisplayValue;
	}

	public String getxValue() {
		return xValue;
	}
	public void setxValue(String xValue) {
		this.xValue = xValue;
	}
	public String getyValue() {
		return yValue;
	}
	public void setyValue(String yValue) {
		this.yValue = yValue;
	}

}
