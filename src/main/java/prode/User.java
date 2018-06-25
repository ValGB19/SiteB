package prode;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator;

import java.util.ArrayList;
import java.util.List;

public class User extends Model {

  public int totalScore(){
    List<MatchPrediction> puntajes = MatchPrediction.where("user_id = ? and score <>", this.getId(),null);
    int res = 0;
    for (MatchPrediction x : puntajes)
      res += x.getInteger("score");
    return res;
  }
  
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
  
  public User getUser(String us) {
	return User.findFirst("nick = ?",us);
	  
  }
  
  public User getUser(int us) {
		return User.findFirst("id = ?",us);
  }

   public String getNameUser(){
    return this.getString("name");
  }

  public String getSurnameUser(){
    return this.getString("surname");
  }
  
  public List<Fixture> getFixtures() {
	  return this.getAll(Fixture.class);
  }
  
  //lista predictions de user que tienen un puntaje
  public List<MatchPrediction> getMatchPrediction(){
	  List<MatchPrediction> l = new ArrayList<MatchPrediction>();
	  l.addAll(this.getAll(MatchPrediction.class));
	  l.removeIf((MatchPrediction p) -> p.getInteger("score") == null);
	  return l;
  }
  
//lista de todas las predicciones
  public List<MatchPrediction> getTotalMatchPrediction(){
	  List<MatchPrediction> l = new ArrayList<MatchPrediction>();
	  l.addAll(this.getAll(MatchPrediction.class));
	  return l;
  }

  public static boolean log(String user, String psw){
      return User.findFirst("nick = ? and password = ?",user, psw) != null;
  }
}
