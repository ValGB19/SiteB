package prode;

import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator;

public class Fixture extends Model {

	static{
		validateWith(new UniquenessValidator("league")).message("This name league already exist.");
		validatePresenceOf("league").message("Please, provide league");
  	}

  	public static List<String> getAllFixtures() {
		  return findAll().collect("league");
	  }

  	public Fixture getFix(String n) {
  		return findFirst("league = ?",n);
  	}
  	
  	public List<Match> getMatch() {
  		
  		return Match.find("fixture_id = ?", getInteger("id")).orderBy("schedule");
  	}
}
