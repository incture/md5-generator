package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VALUE_HELP_TEAMPLATE")
public class ValueHelpTemplate implements BaseDo, Serializable {

	@Id
	@Column(name = "VALUE_HELP_ID")
	private String valueHelpId;

	@Id
	@Column(name = "VALUE")
	private String value;

	public ValueHelpTemplate() {
		super();
	}

	public ValueHelpTemplate(String valueHelpId, String value) {
		super();
		this.valueHelpId = valueHelpId;
		this.value = value;
	}

	public String getValueHelpId() {
		return valueHelpId;
	}

	public void setValueHelpId(String valueHelpId) {
		this.valueHelpId = valueHelpId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ValueHelpTemplate [valueHelpId=" + valueHelpId + ", value=" + value + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
