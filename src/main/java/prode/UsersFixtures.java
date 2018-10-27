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
	
	public List<User> getAllPlayers() {
		List<Integer> listID = findAll().collect("user_id");
		List<User> listUser = new ArrayList<User>();
		Set<Integer> hs = new HashSet<Integer>();
		hs.addAll(listID);
		listID.clear();
		listID.addAll(hs);
		for(Integer n:listID) {
			listUser.add(new User().getUser(n));
		}
		return listUser;
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
