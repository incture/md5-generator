package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROCESS_CONTEXT")
public class ProcessContextEntity implements BaseDo, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 201076287479514515L;

	@Id
	@Column(name = "PROCESS_NAME", length = 255)
	private String processName;

	@Column(name = "Context", length = 5000)
	private String context;

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	
	@Override
	public String toString() {
		return "ProcessContextEntity [processName=" + processName + ", context=" + context + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
