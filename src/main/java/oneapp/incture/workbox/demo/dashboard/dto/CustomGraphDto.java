package oneapp.incture.workbox.demo.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.GraphConfigurationDo;


public class CustomGraphDto {

	List<GraphDataDto> data;
	BigDecimal totalCount;
	GraphConfigurationDo graphConfigurationDo;
	ResponseMessage responseMessage;
	public BigDecimal getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(BigDecimal totalCount) {
		this.totalCount = totalCount;
	}
	public List<GraphDataDto> getData() {
		return data;
	}
	public void setData(List<GraphDataDto> data) {
		this.data = data;
	}
	public GraphConfigurationDo getGraphConfigurationDo() {
		return graphConfigurationDo;
	}
	public void setGraphConfigurationDo(GraphConfigurationDo graphConfigurationDo) {
		this.graphConfigurationDo = graphConfigurationDo;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	@Override
	public String toString() {
		return "CustomGraphDto [data=" + data + ", totalCount=" + totalCount + ", graphConfigurationDo="
				+ graphConfigurationDo + ", responseMessage=" + responseMessage + "]";
	}
	
	
}
