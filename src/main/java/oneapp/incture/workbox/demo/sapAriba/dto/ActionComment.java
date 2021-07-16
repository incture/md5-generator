package oneapp.incture.workbox.demo.sapAriba.dto;

public class ActionComment {

	private String comment;

	public ActionComment() {
		super();
	}

	public ActionComment(String comment) {
		super();
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "ActionComment [comment=" + comment + "]";
	}

}
