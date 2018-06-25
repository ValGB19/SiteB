package prode;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;


public class UsersFixtures extends Model {

	static{
    	validatePresenceOf("user_id","fixture_id");
	}
	
	public User getPlayer() {
		return User.findFirst("id = ?", getInteger("user_id"));
	}
	
	public List getAllPlayers() {
		List<Integer> idU = findAll().collect("user_id");
		List<User> listu = new ArrayList<User>();
		for(Integer n:idU) {
			listu.add(new User().getUser(n));
		}
		return listu;
	}

}
