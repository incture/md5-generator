package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "CROSS_CONSTANTS")
public class CrossConstantDo implements BaseDo, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3042520306155363928L;

	@Id
	@Column(name = "CONSTANT_ID" , columnDefinition = "VARCHAR(255)")
	private String constantId;
	
	@Id
	@Column(name = "CONSTANT_NAME" , columnDefinition = "VARCHAR(255)")
	private String constantName;
	
	@Column(name = "CONSTANT_VALUE" , columnDefinition = "VARCHAR(255)")
	private String constantValue;

	@Override
	public String toString() {
		return "CrossConstantDo [constantId=" + constantId + ", constantName=" + constantName + ", constantValue="
				+ constantValue + "]";
	}

	public String getConstantId() {
		return constantId;
	}

	public void setConstantId(String constantId) {
		this.constantId = constantId;
	}

	public String getConstantName() {
		return constantName;
	}

	public void setConstantName(String constantName) {
		this.constantName = constantName;
	}

	public String getConstantValue() {
		return constantValue;
	}

	public void setConstantValue(String constantValue) {
		this.constantValue = constantValue;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public Object getPrimaryKey() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	
	
}
