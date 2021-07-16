package oneapp.incture.workbox.demo.inbox.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name="PINNED_TASK")
public class PinnedTaskDo implements BaseDo,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private PinnedTaskDoPK pinnedTaskDoPK;

	public PinnedTaskDoPK getPinnedTaskDoPK() {
		return pinnedTaskDoPK;
	}

	public void setPinnedTaskDoPK(PinnedTaskDoPK pinnedTaskDoPK) {
		this.pinnedTaskDoPK = pinnedTaskDoPK;
	}

	@Override
	public String toString() {
		return "PinnedTaskDo [pinnedTaskDoPK=" + pinnedTaskDoPK + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return pinnedTaskDoPK;
	}
	
}
