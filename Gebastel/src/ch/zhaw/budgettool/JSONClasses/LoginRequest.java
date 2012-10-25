package ch.zhaw.budgettool.JSONClasses;

public class LoginRequest {
	private String action = "LoginRequest";
	private String username;
	private String password;
	
	public LoginRequest(String username, String password){
		this.username = username;
		this.password = password;
	}
}
