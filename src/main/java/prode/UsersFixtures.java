package prode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		Set hs = new HashSet();
		hs.addAll(idU);
		idU.clear();
		idU.addAll(hs);
		for(Integer n:idU) {
			listu.add(new User().getUser(n));
		}
		return listu;
	}
	
	public int cantFixUser(int id) {
		List<Integer> idU = findAll().collect("user_id");
		int i=0;
		for(Integer n:idU) {
			if(n==id)
				i++;
		}
		return i;
	}

}
