package ch.zhaw.budgettool.JSONClasses;

public class LoginSuccessfull {
	private String username;
	private String sessionid;
	
	public LoginSuccessfull(String username, String sessionid) {
		super();
		this.username = username;
		this.sessionid = sessionid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getSessionid() {
		return sessionid;
	}
	
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	@Override
	public String toString() {
		return "LoginSuccessfull [username=" + username + ", sessionid="
				+ sessionid + "]";
	}
	
	
}
