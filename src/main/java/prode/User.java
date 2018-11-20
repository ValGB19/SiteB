package prode;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator;

import prode.Utils.Consts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User extends Model {

	static {
		validatePresenceOf("nick").message("Please, provide your username");
		validateWith(new UniquenessValidator("nick")).message("This nickname is already taken.");
		validatePresenceOf("password").message("Please, provide your password");
		validatePresenceOf("dni").message("Please, provide your dni");
		validateWith(new UniquenessValidator("dni")).message("This dni is already registered.");
		validateRange("dni", 0, 999999999).message("Dni cannot be less than " + 0 + " or more than 999.999.999");
		validatePresenceOf("email").message("Please, provide your email");
		validateEmailOf("email").message("Please, provide a valid email");
		validateWith(new UniquenessValidator("email")).message("This email is already registered.");
	}

	/**
	 * Return the total score of the user
	 * @return a int that is the total score of the user
	 */
	public int totalScore() {
		List<MatchPrediction> scores = MatchPrediction.where("user_id = ? and score <>", this.getId(), null);
		int res = 0;
		for (MatchPrediction x : scores)
			res += x.getInteger("score");
		return res;
	}

	/**
	 * @param username: user name
	 * @return user with the username 'username'
	 */
	public User getUser(String username) {
		return User.findFirst("nick = ?", username);

	}

	/**
	 * @param id: user id	
	 * @return User with the id equal to 'id'
	 */
	public User getUser(int id) {
		return User.findFirst("id = ?", id);
	}

	/**
	 * @return Name of the User
	 */
	public String getNameUser() {
		return this.getString("name");
	}

	/**
	 * @return Surname of the User
	 */
	public String getSurnameUser() {
		return this.getString("surname");
	}

	/**
	 * @return List of fixtures in which a user participates
	 */
	public List<Fixture> getFixtures() {
		return this.getAll(Fixture.class);
	}

	/**
	 * @return List of user predictions in which you scored
	 */
	public List<MatchPrediction> getMatchPrediction() {
		List<MatchPrediction> listPred = new ArrayList<MatchPrediction>();
		listPred.addAll(this.getAll(MatchPrediction.class));
		listPred.removeIf((MatchPrediction p) -> p.getInteger("score") == null);
		return listPred;
	}

	/**
	 * @return List of user predictions
	 */
	public List<MatchPrediction> getTotalMatchPrediction() {
		List<MatchPrediction> l = new ArrayList<MatchPrediction>();
		l.addAll(this.getAll(MatchPrediction.class));
		return l;
	}

	/**
	 * Check that the username and password for the login correspond to a registered user
	 * @param user: username
	 * @param psw: user password
	 * @return true if the user and password correspond to a registered user
	 */
	public static boolean log(String user, String psw) {
		return User.findFirst("nick = ? and password = ?", user, psw) != null;
	}

	/**
	 * @param data a map which contains the data of the user to set
	 * @return if the user could be saved in the persistence system
	 */
	public boolean setUserTemp(Map<String, String> data){
		boolean e = false;
		this.set("name", data.get("name"));
		this.set("surname", data.get("surname"));
		this.set("nick", data.get("nick"));
		this.set("email", data.get("email"));
		this.set("password", data.get("pwd"));
		this.set("dni", Integer.parseInt(data.get("dni")));
		this.set("country_id", Country.findFirst("name = ?", data.get("country")).get("id"));
		boolean isAdmin = Consts.SECRETWORD.equals(data.get("key"));
		this.set("admin", isAdmin);
		String k = (String) data.get("key");
		if (isAdmin || k == null || k.isEmpty()) {
			e = this.save();
		}
		return e;
	}
}
