package javafxpractice.utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafxpractice.utils.UserAccount;

public class accountSystem {
	
	Map<Integer, UserAccount> Users = new HashMap<>();
	int NAID = 1;
	
	public UserAccount createUser(String userName, String passwordHash) {
		int id = NAID++;
		UserAccount newUser = new UserAccount(id, userName, passwordHash);
		Users.put(id, newUser);
		return newUser;
	}
	
	public List<String> getAllUsernames() {
	    List<String> list = new ArrayList<>();
	    for (UserAccount user : Users.values()) {
	        list.add(user.getUsername());
	    }
	    return list;
	}
	

}
