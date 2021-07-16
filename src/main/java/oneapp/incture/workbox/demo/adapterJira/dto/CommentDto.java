package oneapp.incture.workbox.demo.adapterJira.dto;

import java.util.Date;

public class CommentDto {
	private String commentId;
	private String authorDisplayName;
	private String updateAuthorDisplayName;
	private String commentBody;
	private Date createdAt;
	private Date updatedAt;
	
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public String getAuthorDisplayName() {
		return authorDisplayName;
	}
	public void setAuthorDisplayName(String authorDisplayName) {
		this.authorDisplayName = authorDisplayName;
	}
	public String getUpdateAuthorDisplayName() {
		return updateAuthorDisplayName;
	}
	public void setUpdateAuthorDisplayName(String updateAuthorDisplayName) {
		this.updateAuthorDisplayName = updateAuthorDisplayName;
	}
	public String getCommentBody() {
		return commentBody;
	}
	public void setCommentBody(String commentBody) {
		this.commentBody = commentBody;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	@Override
	public String toString() {
		return "CommentDto [commentId=" + commentId + ", authorDisplayName=" + authorDisplayName
				+ ", updateAuthorDisplayName=" + updateAuthorDisplayName + ", commentBody=" + commentBody
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
	
	
}
