package javafxpractice.utils;
import javafxpractice.utils.UserAccount;
import java.util.ArrayList;

public class accountValidator {
	
	private accountSystem aS;
	
	public accountValidator(accountSystem aS) {
		this.aS = aS;
	}
	
	public boolean isUsernameUnique(String username) {
		return !aS.getAllUsernames().contains(username);
	}
	
	public boolean checkUNL(String givenUsername) {
		if((givenUsername.length() > 9)) {
				return true;
		}
		return false;
	}
	
	
	
	
	
	
	public boolean checkPWAR(String password) {
		if((password.matches(".*\\d.*") && password.length() > 10)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
}
