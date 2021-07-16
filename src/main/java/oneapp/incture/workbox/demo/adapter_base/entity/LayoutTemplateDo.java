package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "LAYOUT_TEMPLATE_TABLE")
public class LayoutTemplateDo implements BaseDo, Serializable {

	@Id
	@Column(name = "LAYOUT_ID")
	private String layoutId;

	@Column(name = "LAYOUT_NAME")
	private String layoutName;

	@Column(name = "LABEL")
	private String label;

	@Column(name = "PARENT_LAYOUT_NAME")
	private String parentLayoutName;

	@Column(name = "SEQUENCE")
	private String sequence;

	@Column(name = "LEVEL")
	private String level;

	@Column(name = "LAYOUT_TYPE")
	private String layoutType;
	
	@Column(name = "LAYOUT_SIZE")
	private String layoutSize;
	
	@Column(name="IS_DELETED")
	private Boolean isDeleted;

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}


	public LayoutTemplateDo() {
		super();
	}

	public LayoutTemplateDo(String layoutId, String layoutName, String label, String parentLayoutName, String sequence,
			String level, String layoutType) {
		super();
		this.layoutId = layoutId;
		this.layoutName = layoutName;
		this.label = label;
		this.parentLayoutName = parentLayoutName;
		this.sequence = sequence;
		this.level = level;
		this.layoutType = layoutType;
	}

	public LayoutTemplateDo(String layoutId, String layoutName, String label, String parentLayoutName, String sequence,
			String level, String layoutType, String layoutSize) {
		super();
		this.layoutId = layoutId;
		this.layoutName = layoutName;
		this.label = label;
		this.parentLayoutName = parentLayoutName;
		this.sequence = sequence;
		this.level = level;
		this.layoutType = layoutType;
		this.layoutSize = layoutSize;
	}

	public String getLayoutSize() {
		return layoutSize;
	}

	public void setLayoutSize(String layoutSize) {
		this.layoutSize = layoutSize;
	}

	public String getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getParentLayoutName() {
		return parentLayoutName;
	}

	public void setParentLayoutName(String parentLayoutName) {
		this.parentLayoutName = parentLayoutName;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(String layoutType) {
		this.layoutType = layoutType;
	}

	

	@Override
	public String toString() {
		return "LayoutTemplateDo [layoutId=" + layoutId + ", layoutName=" + layoutName + ", label=" + label
				+ ", parentLayoutName=" + parentLayoutName + ", sequence=" + sequence + ", level=" + level
				+ ", layoutType=" + layoutType + ", layoutSize=" + layoutSize + ", isDeleted=" + isDeleted + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
