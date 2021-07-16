package oneapp.incture.workbox.demo.dashboard.dto;

public class TileDetailsDto {

	private String tileId;
	private String key;
	private String value;
	private String label;
	private String status;
	private String type;
	private String threshold;
	private String description;
	private String tilePayload;
	private String createdBy;

	public String getTilePayload() {
		return tilePayload;
	}

	public void setTilePayload(String tilePayload) {
		this.tilePayload = tilePayload;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getThreshold() {
		return threshold;
	}

	public String getTileId() {
		return tileId;
	}

	public void setTileId(String tileId) {
		this.tileId = tileId;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	public TileDetailsDto() {
		super();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TileDetailsDto(String tileId, String key, String value, String label, String status, String type,
			String threshold, String description, String tilePayload, String createdBy) {
		super();
		this.tileId = tileId;
		this.key = key;
		this.value = value;
		this.label = label;
		this.status = status;
		this.type = type;
		this.threshold = threshold;
		this.description = description;
		this.tilePayload = tilePayload;
		this.createdBy = createdBy;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "TileDetailsDto [tileId=" + tileId + ", key=" + key + ", value=" + value + ", label=" + label
				+ ", status=" + status + ", type=" + type + ", threshold=" + threshold + ", description=" + description
				+ ", tilePayload=" + tilePayload + ", createdBy=" + createdBy + "]";
	}

}
