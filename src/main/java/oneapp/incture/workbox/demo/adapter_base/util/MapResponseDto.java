package oneapp.incture.workbox.demo.adapter_base.util;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MapResponseDto {
	private String key;
	private BigDecimal value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "MapResponseDto [key=" + key + ", value=" + value + "]";
	}
}
