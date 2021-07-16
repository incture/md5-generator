package oneapp.incture.workbox.demo.inbox.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ActionRequestDto {

private String status;

private ActionRequestContextDto context;


public ActionRequestDto(String status, ActionRequestContextDto context) {
	super();
	this.status = status;
	this.context = context;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public ActionRequestContextDto getContext() {
	return context;
}
public void setContext(ActionRequestContextDto context) {
	this.context = context;
}

@Override
public String toString() {
	return "ActionRequestDto [status=" + status + ", context=" + context + ", getStatus()=" + getStatus()
			+ ", getContext()=" + getContext() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
			+ ", toString()=" + super.toString() + "]";
}	
	
	
}
