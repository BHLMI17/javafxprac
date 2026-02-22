package javafxpractice.utils;
import javafxpractice.utils.accountValidator;
import javafxpractice.utils.accountSystem;

public class UserAccount {
	int id;
	public String uN;
	public String pWH;
	
	
	UserAccount(int ID, String username, String passwordHash) {
		id = ID;
		uN = username;
		pWH = passwordHash;
	}
	
	public String getUsername() {
		return uN;
	}
	public int getID() {
		return id;
	}
	public String getPasswordHash() {
		return pWH;
	}
	
	

	
}
