package oneapp.incture.workbox.demo.adapter_base.dto;

import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

public class TemplateTableDto  extends BaseDto {

	private String templateId;
	private String layoutId;
	private String sequence;
	
	
	

	public TemplateTableDto() {
		super();
	}

	public TemplateTableDto(String templateId, String layoutId, String sequence) {
		super();
		this.templateId = templateId;
		this.layoutId = layoutId;
		this.sequence = sequence;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
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

	@Override
	public String toString() {
		return "TemplateTableDto [templateId=" + templateId + ", layoutId=" + layoutId + ", sequence=" + sequence + "]";
	}

}
