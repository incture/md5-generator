package oneapp.incture.workbox.demo.substitution.dto;


import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class StringListResponseDto extends BaseDto {

private List<String> str;
private ResponseMessage message;


public List<String> getStr() {
	return str;
}
public void setStr(List<String> str) {
	this.str = str;
}
public ResponseMessage getMessage() {
	return message;
}
public void setMessage(ResponseMessage message) {
	this.message = message;
}
@Override
public String toString() {
	return "ListStringResponseDto [str=" + str + ", message=" + message + "]";
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
