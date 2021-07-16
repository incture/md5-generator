package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SEQ_GEN")
public class SeqNumberDo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5598454891394909787L;

	@Id
	@Column(name = "REF_COD", length = 255)
	private String referenceCode;
	
	@Column(name = "RUN_NUM")
	private Integer runningNumber;

	public SeqNumberDo(String referenceCode, Integer runningNumber) {
		super();
		this.referenceCode = referenceCode;
		this.runningNumber = runningNumber;
	}

	public SeqNumberDo() {
		super();
	}

	public String getReferenceCode() {
		return referenceCode;
	}

	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	public Integer getRunningNumber() {
		return runningNumber;
	}

	public void setRunningNumber(Integer runningNumber) {
		this.runningNumber = runningNumber;
	}

	@Override
	public String toString() {
		return "SeqNumberDo [referenceCode=" + referenceCode + ", runningNumber=" + runningNumber + "]";
	}

//	@Override
//	public Object getPrimaryKey() {
//		// TODO Auto-generated method stub
//		return referenceCode;
//	}
	
	
	
}
