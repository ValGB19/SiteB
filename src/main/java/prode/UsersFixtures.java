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
	
	/**
	 * Get the list of users who participated in a fixture
	 * @return list of users who participated in a fixture
	 */
	public List<User> getAllPlayers() {
		List<Integer> listID = findAll().collect("user_id");
		List<User> listUser = new ArrayList<User>();
		Set<Integer> hs = new HashSet<Integer>();
		hs.addAll(listID);
		listID.clear();
		listID.addAll(hs);
		for(Integer n : listID) {
			listUser.add(new User().getUser(n));
		}
		return listUser;
	}
	
	/**
	 * Obtain the number of Fixtures that the user identified with 'idUser' made a prediction
	 * @param idUser: user id
	 * @return Number of fixtures played by a user
	 */
	public int totalFixturesUser(int idUser) { //ver sig code return this.find("user_id = ?", id).size()
		List<Integer> listIdUser = findAll().collect("user_id"); 
		int fixtures = 0;
		for(Integer n : listIdUser) {
			if(n == idUser)
				fixtures++;
		}
		return fixtures;
	}

}
