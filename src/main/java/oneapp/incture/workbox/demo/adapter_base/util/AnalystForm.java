package oneapp.incture.workbox.demo.adapter_base.util;

import java.util.ArrayList;
import java.util.List;


public class AnalystForm {
	private List<String> valueHelp=new ArrayList<String>();
	private String value;
	private String key;
	
	public List<String> getValueHelp() {
		return valueHelp;
	}
	public void setValueHelp(List<String> valueHelp) {
		this.valueHelp = valueHelp;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@Override
	public String toString() {
		return "AnalystForm [valueHelp=" + valueHelp + ", value=" + value + ", key=" + key + "]";
	}
	
	
	
}
