package prode;

import org.javalite.activejdbc.Model;

public class User extends Model {

  static{
    validatePresenceOf("username").message("Please, provide your username");
  }
}
