package ch.zhaw.budgettool.datatransfer.JSONClasses;

public class ExpenseJSON {
	private String id;
	private String user_id;
	private String description;
	private String value;
	private String date;
	private String created;
	private String modified;
	public String getId() {
		return id;
	}
	public String getUser_id() {
		return user_id;
	}
	public String getDescription() {
		return description;
	}
	public String getValue() {
		return value;
	}
	public String getDate() {
		return date;
	}
	public String getCreated() {
		return created;
	}
	public String getModified() {
		return modified;
	}
}