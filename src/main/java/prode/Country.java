package prode;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator;

public class Country extends Model{

	static{
    	validatePresenceOf("name").message("Please, provide name");
    	validateWith(new UniquenessValidator("name")).message("This name country already exist.");
    }

}
