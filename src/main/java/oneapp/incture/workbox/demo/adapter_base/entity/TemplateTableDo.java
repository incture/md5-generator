package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "TEMPLATE_TABLE")
public class TemplateTableDo implements BaseDo, Serializable {

	@Id
	@Column(name = "TEMPLATE_ID")
	private String templateId;
	@Id
	@Column(name = "LAYOUT_ID")
	private String layoutId;
	@Column(name = "SEQUENCE")
	private String sequence;

	public TemplateTableDo() {
		super();
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public TemplateTableDo(String templateId, String layoutId) {
		super();
		this.templateId = templateId;
		this.layoutId = layoutId;
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

	@Override
	public String toString() {
		return "TemplateTableDo [templateId=" + templateId + ", layoutId=" + layoutId + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
