package oneapp.incture.workbox.demo.inbox.dto;

import java.util.List;

public class InboxTasksHeaderDto {

	public InboxTasksHeaderDto(List<HeaderDto> headers) {
		this.headers = headers;
	}

	public InboxTasksHeaderDto() {
	}

	private List<HeaderDto> headers;

	public List<HeaderDto> getHeaders() {
		return headers;
	}

	public void setHeaders(List<HeaderDto> headers) {
		this.headers = headers;
	}

	@Override
	public String toString() {
		return "InboxTasksHeaderDto [headers=" + headers + "]";
	}

}
