package prode;

import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator;

public class Fixture extends Model {

	static{
		validateWith(new UniquenessValidator("league")).message("This name league already exist.");
		validatePresenceOf("league").message("Please, provide league");
  	}

	/**
	 * Get all fixtures and return to the list of all names
	 * @pre atributte league =! null
	 * @return list of the names of the fixtures
	 */
  	public static List<String> getAllFixtures() {
		  return findAll().collect("league");
	}

  	/**
  	 * Get first fixture found with the name passed by parameter
  	 * @param league: fixture name
  	 * @return First fixture found with the name passed by parameter
  	 */
  	public Fixture getFix(String league) {
  		return findFirst("league = ?", league);
  	}
  	
  	/**
  	 * Get matches of the fixtures
  	 * @pre fixture != null
  	 * @return List of fixture matches.
  	 */
  	public List<Match> getMatch() {  		
  		return Match.find("fixture_id = ?", getInteger("id")).orderBy("schedule");
  	}
}
