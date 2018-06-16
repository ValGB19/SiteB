package prode;

import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator;

public class Country extends Model{

	public static List getAllCountrys() {
		return Country.findAll().collect("name");
	}
	
	static{
    	validatePresenceOf("name").message("Please, provide name");
    	validateWith(new UniquenessValidator("name")).message("This name country already exist.");
    }

}
