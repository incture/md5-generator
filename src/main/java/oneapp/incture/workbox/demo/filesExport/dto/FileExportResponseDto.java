package oneapp.incture.workbox.demo.filesExport.dto;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class FileExportResponseDto {
	private ResponseMessage message;
	private String FileContent;

	@Override
	public String toString() {
		return "FileExportResponseDto [message=" + message + ", FileContent=" + FileContent + "]";
	}

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

	public String getFileContent() {
		return FileContent;
	}

	public void setFileContent(String fileContent) {
		FileContent = fileContent;
	}

}
