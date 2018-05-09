package prode;

import org.javalite.activejdbc.Model;

public class Fixture extends Model {
    static{
    validatePresenceOf("league").message("Please, provide league");
  }
}
