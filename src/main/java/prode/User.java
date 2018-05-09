package prode;

import org.javalite.activejdbc.Model;

public class User extends Model {

  static{
    validatePresenceOf("nick").message("Please, provide your username");
    validatePresenceOf("password").message("Please, provide your password");
    validatePresenceOf("dni").message("Please, provide your dni");
    validatePresenceOf("email").message("Please, provide your email");
    validateEmailOf("email").message("Please, provide a valid email");
  }
}
