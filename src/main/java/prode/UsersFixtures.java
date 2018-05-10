package prode;

import org.javalite.activejdbc.Model;


public class UsersFixtures extends Model {

	static{
    	validatePresenceOf("user_id","fixture_id");
	}
}
