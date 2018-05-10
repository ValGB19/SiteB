package prode;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator;

public class User extends Model {

  static{
    validatePresenceOf("nick").message("Please, provide your username");
    validateWith(new UniquenessValidator("nick")).message("This nickname is already taken.");
    validatePresenceOf("password").message("Please, provide your password");
    validatePresenceOf("dni").message("Please, provide your dni");
    validateWith(new UniquenessValidator("dni")).message("This dni is already registered.");
    validateRange("dni", 0, 999999999).message("Dni cannot be less than " + 0 + " or more than 999.999.999");
    validatePresenceOf("email").message("Please, provide your email");
    validateEmailOf("email").message("Please, provide a valid email");
    validateWith(new UniquenessValidator("email")).message("This email is already registered.");
  }
}
