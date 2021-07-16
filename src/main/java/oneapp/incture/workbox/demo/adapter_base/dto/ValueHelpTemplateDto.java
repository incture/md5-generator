package oneapp.incture.workbox.demo.adapter_base.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;


public class ValueHelpTemplateDto extends BaseDto {

	@Id
	@Column(name = "VALUE_HELP_ID")
	private String valueHelpId;

	@Id
	@Column(name = "VALUE")
	private String value;

	public ValueHelpTemplateDto() {
		super();
	}

	public ValueHelpTemplateDto(String valueHelpId, String value) {
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
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}

}
