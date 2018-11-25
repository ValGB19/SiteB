package prode;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator;

public class Team extends Model {

	static {
		validatePresenceOf("name").message("Please, provide a team name");
		validateWith(new UniquenessValidator("name")).message("This team name already exist.");
	}

	/**
	 * @param id: Team id
	 * @return Name of the team
	 */
	public String getNameTeam(int id) {
		return findFirst("id = ?", id).getString("name");
	}
}
