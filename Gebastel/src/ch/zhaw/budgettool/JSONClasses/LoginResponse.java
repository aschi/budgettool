package ch.zhaw.budgettool.JSONClasses;

public class LoginResponse {

	private String status;
	private String username;
	private String sessionid;
	
	
	public LoginResponse(String status, String username, String sessionid) {
		super();
		this.status = status;
		this.username = username;
		this.sessionid = sessionid;
	}


	public String getStatus() {
		return status;
	}


	public String getUsername() {
		return username;
	}


	public String getSessionid() {
		return sessionid;
	}


	@Override
	public String toString() {
		return "LoginResponse [status=" + status + ", username=" + username
				+ ", sessionid=" + sessionid + "]";
	}
	
	
	
	
}
