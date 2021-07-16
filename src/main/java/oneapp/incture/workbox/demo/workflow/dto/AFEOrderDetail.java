package oneapp.incture.workbox.demo.workflow.dto;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class AFEOrderDetail {

	String orderType;
	ResponseMessage responseMessage;
	@Override
	public String toString() {
		return "AFEOrderDetail [orderType=" + orderType + ", responseMessage=" + responseMessage + "]";
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
}
