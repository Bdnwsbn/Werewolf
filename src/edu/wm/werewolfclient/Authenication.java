package edu.wm.werewolfclient;

public class Authenication {

	public static String username;
	public static String password;
	
	public static void setUsername(String usernameText) {
		username = usernameText;
	}
	
	public static void setPassword(String passwordText) {
		password = passwordText;
	}
	
	public static String getUsername() {
		return username;
	}
	
	public static String getPassword() {
		return password;
	}
	
}
