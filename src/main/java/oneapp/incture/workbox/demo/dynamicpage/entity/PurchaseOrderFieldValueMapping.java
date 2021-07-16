package oneapp.incture.workbox.demo.dynamicpage.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PO_FIELD_VALUE_MAPPING")
public class PurchaseOrderFieldValueMapping implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -402440383477272449L;

	@Id
	@Column(name = "KEY")
	private String key;
	
	@Id
	@Column(name = "VALUE")
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "PurchaseOrderFieldValueMapping [key=" + key + ", value=" + value + "]";
	}
	
	
}
