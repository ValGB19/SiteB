package prode;

import org.javalite.activejdbc.Model;

public class Country extends Model{

	static{
    validatePresenceOf("name").message("Please, provide name");
    }

}
