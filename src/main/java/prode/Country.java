package prode;

import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator;
import org.javalite.activejdbc.Base;

public class Country extends Model{

	@SuppressWarnings("rawtypes")
	public static List getAllCountrys() {
		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true&useSSL=false", "root", "root");
    	List s = Country.findAll().collect("name");
    	Base.close();
		return s;
	}
	
	static{
    	validatePresenceOf("name").message("Please, provide name");
    	validateWith(new UniquenessValidator("name")).message("This name country already exist.");
    }

}
