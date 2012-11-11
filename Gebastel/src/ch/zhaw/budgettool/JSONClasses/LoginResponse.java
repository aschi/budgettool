package ch.zhaw.budgettool.JSONClasses;

public class LoginResponse {

	private String status;
	private String username;
	private String sessionId;
	
	
	public LoginResponse(String status, String username, String sessionId) {
		super();
		this.status = status;
		this.username = username;
		this.sessionId = sessionId;
	}


	public String getStatus() {
		return status;
	}


	public String getUsername() {
		return username;
	}


	public String getSessionId() {
		return sessionId;
	}


	@Override
	public String toString() {
		return "LoginResponse [status=" + status + ", username=" + username
				+ ", sessionId=" + sessionId + "]";
	}
	
	
	
	
}
