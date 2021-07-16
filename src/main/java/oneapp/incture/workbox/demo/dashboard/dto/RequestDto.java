package oneapp.incture.workbox.demo.dashboard.dto;



import java.util.List;

public class RequestDto {

	private List<String> ids;
	private String strName;

	
	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	@Override
	public String toString() {
		return "RequestDto [ids=" + ids + ", strName=" + strName + "]";
	}

}
