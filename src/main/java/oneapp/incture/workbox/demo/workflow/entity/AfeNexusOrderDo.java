package oneapp.incture.workbox.demo.workflow.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "AFE_NEXUS_ORDER")
public class AfeNexusOrderDo implements BaseDo, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PROCESS_NAME")
	private String processName;

	@Column(name = "ORDER_TYPE")
	private String orderType;

	@Override
	public String toString() {
		return "AfeNexusOrderDo [processName=" + processName + ", orderType=" + orderType + "]";
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return processName;
	}

	

}
