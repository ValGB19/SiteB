package prode;

import org.javalite.activejdbc.Model;

public class MatchPrediction extends Model{
	static{
    validatePresenceOf("prediction").message("Please, provide date");
  }
}
