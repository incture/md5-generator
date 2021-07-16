package oneapp.incture.workbox.demo.dashboard.dto;

import java.math.BigInteger;

public class GraphDataCountDto {

	String id;
	String idDisplayName;
	BigInteger count;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdDisplayName() {
		return idDisplayName;
	}

	public void setIdDisplayName(String idDisplayName) {
		this.idDisplayName = idDisplayName;
	}

	public BigInteger getCount() {
		return count;
	}

	public void setCount(BigInteger count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "GraphDataCountDto [id=" + id + ", idDisplayName=" + idDisplayName + ", count=" + count + "]";
	}
	
}
