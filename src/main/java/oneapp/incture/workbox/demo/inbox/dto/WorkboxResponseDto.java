package oneapp.incture.workbox.demo.inbox.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.WorkBoxDto;

public class WorkboxResponseDto {
	
	private List<WorkBoxDto> workBoxDtos;
	private BigDecimal count;
	private ResponseMessage responseMessage;

	
	public WorkboxResponseDto() {
	}

	private int pageCount;
	
	/* Custom Attribute values */
	private InboxTasksHeaderDto headerDto;
//	private CustomAttributeDto2 customAttributeDto;
	
	/* Custom Attribute value - other way */
//	public CustomAttributeDtoResponse attributeDtoResponse;
//	private List<CustomAttributeDto> customAttributes;
	
//	@JsonIgnore
//	public JSONArray customAttributesJson;
	
//	public ArrayNode getArrayNode() {
//		return customAttributes;
//	}
//
//	public void setArrayNode(ArrayNode customAttributes) {
//		this.customAttributes = customAttributes;
//	}

	public ArrayNode customAttributes;
	
//	public JSONArray getCustomAttributesJson() {
//		return customAttributesJson;
//	}
//
//	public void setCustomAttributesJson(JSONArray customAttributesJson) {
//		this.customAttributesJson = customAttributesJson;
//	}

	public ArrayNode getCustomAttributes() {
		return customAttributes;
	}

	public void setCustomAttributes(ArrayNode customAttributes) {
		this.customAttributes = customAttributes;
	}

	public InboxTasksHeaderDto getHeaderDto() {
		return headerDto;
	}

//	public CustomAttributeDtoResponse getAttributeDtoResponse() {
//		return attributeDtoResponse;
//	}
//
//	public void setAttributeDtoResponse(CustomAttributeDtoResponse attributeDtoResponse) {
//		this.attributeDtoResponse = attributeDtoResponse;
//	}

	public void setHeaderDto(InboxTasksHeaderDto headerDto) {
		this.headerDto = headerDto;
	}

//	public CustomAttributeDto2 getCustomAttributeDto() {
//		return customAttributeDto;
//	}
//
//	public void setCustomAttributeDto(CustomAttributeDto2 customAttributeDto) {
//		this.customAttributeDto = customAttributeDto;
//	}

	public List<WorkBoxDto> getWorkBoxDtos() {
		return workBoxDtos;
	}

	public void setWorkBoxDtos(List<WorkBoxDto> workBoxDtos) {
		this.workBoxDtos = workBoxDtos;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	@Override
	public String toString() {
		return "WorkboxResponseDto [workBoxDtos=" + workBoxDtos + ", count=" + count + ", responseMessage="
				+ responseMessage + ", pageCount=" + pageCount + ", headerDto=" + headerDto + ", customAttributes="
				+ customAttributes + "]";
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = count;
	}

}
