package oneapp.incture.workbox.demo.workload.dto;


import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class SearchListResponseDto extends BaseDto {

private List<SearchListDto> procList;
private List<SearchListDto> statusList;
private ResponseMessage message;
public List<SearchListDto> getProcList() {
	return procList;
}
public void setProcList(List<SearchListDto> procList) {
	this.procList = procList;
}
public List<SearchListDto> getStatusList() {
	return statusList;
}
public void setStatusList(List<SearchListDto> statusList) {
	this.statusList = statusList;
}
public ResponseMessage getMessage() {
	return message;
}
public void setMessage(ResponseMessage message) {
	this.message = message;
}
@Override
public String toString() {
	return "SearchListResponseDto [procList=" + procList + ", statusList=" + statusList + ", message=" + message + "]";
}
@Override
public Boolean getValidForUsage() {
	// TODO Auto-generated method stub
	return null;
}
@Override
public void validate(EnOperation enOperation) throws InvalidInputFault {
	// TODO Auto-generated method stub
	
}


}
